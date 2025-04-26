package com.game.webSocket;


import com.game.util.IdGenerator;
import jakarta.websocket.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoom {
    private final String roomId;
    private final Map<String, Session> players = new ConcurrentHashMap<>();
    private final int[][] board = new int[15][15];
    private String currentPlayerId;
    private boolean gameStarted = false;
    private String blackPlayerId;
    private String whitePlayerId;
    private Timer moveTimer; // 计时器
    private final Map<String, Long> disconnectedPlayers = new ConcurrentHashMap<>(); // 记录掉线玩家及重连截止时间

    private static final int MOVE_TIMEOUT = 60000; // 60秒超时
    private static final int RECONNECT_TIMEOUT = 60000; // 60秒重连时间限制


    public GameRoom(String roomId) {
        this.roomId = IdGenerator.generateAlphaNumericId(6);
    }

    public void addPlayer(String playerId, Session session) {

        try {
            System.out.println("尝试添加玩家: roomId=" + roomId + ", playerId=" + playerId);

            if (players.size() >= 2) {
                System.out.println("房间已满，拒绝玩家" + playerId + "加入");
                GameMessage message = new GameMessage();
                message.setType("ERROR");
                message.setMessage("房间已满");
                session.getBasicRemote().sendText(message.toJson());
                return;
            }

            // 检查玩家是否已在房间中
            if (players.containsKey(playerId)) {
                System.out.println("玩家" + playerId + "已在房间中");
                return;
            }

            players.put(playerId, session);
            System.out.println("玩家" + playerId + "成功加入房间" + roomId + "，当前玩家数：" + players.size());
            broadcastPlayerList();

            // 如果玩家断线重连
            if (disconnectedPlayers.containsKey(playerId)) {
                disconnectedPlayers.remove(playerId);
                players.put(playerId, session);
                System.out.println("玩家" + playerId + "断线重连成功");
                if (disconnectedPlayers.isEmpty() && gameStarted) {
                    // 所有人都在线，恢复游戏
                    broadcast(new GameMessage("RECONNECT_SUCCESS", "玩家" + playerId + "已重连，游戏继续"));
                    startMoveTimer();
                }
                return;
            }
        } catch (Exception e) {
            System.err.println("添加玩家时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removePlayer(String playerId) {
        try {
            System.out.println("移除玩家: playerId=" + playerId);
            Session session = players.remove(playerId);
            if (session != null && session.isOpen()) {
                try {
                    session.close();
                } catch (Exception e) {
                    System.err.println("关闭会话时出错: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            if (gameStarted) {
                // 断线等待重连
                long deadline = System.currentTimeMillis() + RECONNECT_TIMEOUT;
                disconnectedPlayers.put(playerId, deadline);
                broadcast(new GameMessage("WAIT_RECONNECT", "玩家" + playerId + "掉线，等待重连..."));
                pauseGameForReconnect();
                // 启动定时任务检测重连超时
                new Timer(true).schedule(new TimerTask() {
                    @Override
                    public void run() {
                        checkReconnectTimeout(playerId);
                    }
                }, RECONNECT_TIMEOUT + 1000);
            } else {
                broadcast(new GameMessage("OPPONENT_LEFT", "对手已离开"));
            }
            broadcastPlayerList();
        } catch (Exception e) {
            System.err.println("移除玩家时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void pauseGameForReconnect() {
        if (moveTimer != null) {
            moveTimer.cancel();
            moveTimer = null;
        }
        // 游戏暂停，等待重连
    }

    private void checkReconnectTimeout(String playerId) {
        Long deadline = disconnectedPlayers.get(playerId);
        if (deadline != null && System.currentTimeMillis() > deadline) {
            // 超时未重连，判平局，掉线方扣分
            disconnectedPlayers.remove(playerId);
            gameStarted = false;
            if (moveTimer != null) {
                moveTimer.cancel();
                moveTimer = null;
            }
            GameMessage drawMsg = new GameMessage();
            drawMsg.setType("GAME_OVER");
            drawMsg.setWinner(null); // 平局
            drawMsg.setMessage("玩家" + playerId + "超时未重连，判平局，掉线方扣分");
            broadcast(drawMsg);
            saveGameRecordOnDisconnect(playerId);
            // TODO: 调用用户扣分逻辑（如UserService.updateEloScore(playerId, -50);）
        }
    }

    private void saveGameRecordOnDisconnect(String disconnectedPlayerId) {
        // TODO: 实现自动保存对局记录到数据库
        // 可调用GameRecordServlet或GameRecordService
        System.out.println("自动保存对局，掉线玩家: " + disconnectedPlayerId);
    }

    public boolean isPlayerDisconnected(String playerId) {
        return disconnectedPlayers.containsKey(playerId);
    }

    public boolean isAnyPlayerDisconnected() {
        return !disconnectedPlayers.isEmpty();
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean canStartGame() {
        return players.size() == 2 && !gameStarted; // 两名玩家且游戏未开始
    }

    public void startGame() {
        if (!canStartGame()) {
            System.err.println("无法开始游戏，条件不满足");
            return;
        }

        List<String> playerIds = new ArrayList<>(players.keySet());
        Collections.sort(playerIds);

        gameStarted = true;
        blackPlayerId = playerIds.get(0);
        whitePlayerId = playerIds.get(1);
        currentPlayerId = blackPlayerId;

        GameMessage message = new GameMessage();
        message.setType("GAME_START");
        message.setCurrentPlayerId(currentPlayerId);
        message.setWhitePlayerId(whitePlayerId);
        message.setBlackPlayerId(blackPlayerId);
        broadcast(message);

        startMoveTimer(); // 开始计时
    }

    private void startMoveTimer() {
        if (moveTimer != null) {
            moveTimer.cancel();
        }
        moveTimer = new Timer(true);
        moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (gameStarted && currentPlayerId != null) {
                    gameOver(getOpponentId(currentPlayerId)); // 超时判负
                }
            }
        }, MOVE_TIMEOUT);
    }

    public String getBlackPlayerId() {
        return blackPlayerId;
    }

    public String getWhitePlayerId() {
        return whitePlayerId;
    }

    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void makeMove(String playerId, int x, int y) {
        // 禁止在等待重连时下棋
        if (isAnyPlayerDisconnected()) {
            System.out.println("有玩家掉线，暂停下棋");
            return;
        }
        if (!gameStarted || !playerId.equals(currentPlayerId)) return;
        if (x < 0 || x >= 15 || y < 0 || y >= 15 || board[x][y] != 0) return;

        int pieceValue = playerId.equals(blackPlayerId) ? 1 : 2;
        board[x][y] = pieceValue;

        // 三三禁手检测（仅黑子）
        if (pieceValue == 1 && isDoubleThree(x, y, 1)) {
            // 禁手，判负
            gameOver(getOpponentId(playerId));
            GameMessage forbiddenMsg = new GameMessage();
            forbiddenMsg.setType("FORBIDDEN");
            forbiddenMsg.setMessage("黑方三三禁手，判负！");
            broadcast(forbiddenMsg);
            return;
        }

        GameMessage message = new GameMessage();
        message.setType("MOVE");
        message.setX(x);
        message.setY(y);
        message.setPlayerId(playerId);
        message.setPieceValue(pieceValue);
        broadcast(message);

        if (checkWin(x, y)) {
            gameOver(playerId);
        } else {
            currentPlayerId = getOpponentId(playerId);
            // 重置计时器
            startMoveTimer();
            message = new GameMessage();
            message.setType("TURN_CHANGE");
            message.setCurrentPlayerId(currentPlayerId);
            broadcast(message);
        }
    }

    public void surrender(String playerId) {
        if (!gameStarted) return;
        gameOver(getOpponentId(playerId));
    }

    private void gameOver(String winnerId) {
        gameStarted = false;
        if (moveTimer != null) {
            moveTimer.cancel();
            moveTimer = null;
        }
        GameMessage message = new GameMessage();
        message.setType("GAME_OVER");
        message.setWinner(winnerId);
        broadcast(message);
    }

    private boolean checkWin(int x, int y) {
        int player = board[x][y];


        int [] dx = {1, 0, 1, 1};
        int [] dy = {0, 1, 1, -1};

        for (int t = 0; t < 4; t++) {
            int count = 1;
            for (int i = 1; i < 5; i++) {
                int nx = x + dx[t] * i;
                int ny = y + dy[t] * i;
                if (nx >= 0 && nx < 15 && ny >= 0 && ny < 15 && board[nx][ny] == player) {
                    count++;
                } else break;
            }
            for (int i = 1; i < 5; i++) {
                int nx = x - dx[t] * i;
                int ny = y - dy[t] * i;
                if (nx >= 0 && nx < 15 && ny >= 0 && ny < 15 && board[nx][ny] == player) {
                    count++;
                } else break;
            }
            if (count >= 5) return true;
        }
        return false;
    }

    // 检查是否三三禁手（仅黑子，返回true表示禁手）
    private boolean isDoubleThree(int x, int y, int color) {
        // 检查4个方向，统计活三数量
        int liveThreeCount = 0;
        int[][] dirs = {{1,0},{0,1},{1,1},{1,-1}};
        for (int[] dir : dirs) {
            if (isLiveThree(x, y, dir[0], dir[1], color)) liveThreeCount++;
            if (liveThreeCount >= 2) return true;
        }
        return false;
    }

    // 检查(x,y)为中心，dir方向是否形成活三
    private boolean isLiveThree(int x, int y, int dx, int dy, int color) {
        // 以(x,y)为中心，取7个点的序列
        int[] line = new int[7];
        for (int i = -3; i <= 3; i++) {
            int nx = x + dx * i;
            int ny = y + dy * i;
            if (nx < 0 || nx >= 15 || ny < 0 || ny >= 15) {
                line[i + 3] = -1; // 边界外
            } else if (nx == x && ny == y) {
                line[i + 3] = color; // 当前落子
            } else {
                line[i + 3] = board[nx][ny];
            }
        }
        // 检查是否有活三模式（0空，1黑，2白，-1边界）
        for (int i = 0; i <= 2; i++) {
            if (line[i] == 0 && line[i+1] == color && line[i+2] == color && line[i+3] == color && line[i+4] == 0) {
                // 检查两端都为空且不是被堵死
                if (line[i] == 0 && line[i+4] == 0) {
                    // 还需排除四连和五连（只允许正好三个）
                    if ((i == 0 || line[i-1] != color) && (i+5 == 7 || line[i+5] != color)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String getOpponentId(String playerId) {
        for (String id : players.keySet()) {
            if (!id.equals(playerId)) return id;
        }
        return null;
    }

    private String getOpponentName(String playerId) {
        String opponentId = getOpponentId(playerId);
        return opponentId != null ? "玩家" + opponentId : null;
    }

    public void broadcast(GameMessage message) {
        if (message == null) {
            System.err.println("尝试广播空消息");
            return;
        }

        System.out.println("广播消息: type=" + message.getType() + ", roomId=" + roomId);

        // 移除的无效会话列表
        List<String> invalidPlayers = new ArrayList<>();

        for (Map.Entry<String, Session> entry : players.entrySet()) {
            String playerId = entry.getKey();
            Session session = entry.getValue();

            try {
                if (session != null && session.isOpen()) {
                    String jsonMessage = message.toJson();
                    System.out.println("向玩家" + playerId + "发送消息: " + jsonMessage);
                    session.getBasicRemote().sendText(jsonMessage);
                } else {
                    System.out.println("玩家" + playerId + "的会话已关闭，将移除");
                    invalidPlayers.add(playerId);
                }
            } catch (Exception e) {
                System.err.println("向玩家" + playerId + "发送消息时出错: " + e.getMessage());
                e.printStackTrace();
                invalidPlayers.add(playerId);
            }
        }

        // 移除无效的会话
        for (String playerId : invalidPlayers) {
            System.out.println("移除无效会话: playerId=" + playerId);
            removePlayer(playerId);
        }
    }

    private void broadcastPlayerList() {
        try {
            GameMessage message = new GameMessage();
            message.setType("PLAYER_LIST");

            message.setMessage("玩家列表:" + String.join(",", players.keySet()));
            broadcast(message);
        } catch (Exception e) {
            System.err.println("广播玩家列表时出错: " + e.getMessage());
        }
    }

    // 新增：获取棋盘副本用于同步
    public int[][] getBoardCopy() {
        int[][] copy = new int[15][15];
        for (int i = 0; i < 15; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 15);
        }
        return copy;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}
