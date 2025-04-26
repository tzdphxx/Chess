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
    private Timer moveTimer; // 添加计时器
    private static final int MOVE_TIMEOUT = 60000; // 60秒超时


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
                broadcast(new GameMessage("OPPONENT_LEFT", "对手已离开"));
            }
            broadcastPlayerList();
        } catch (Exception e) {
            System.err.println("移除玩家时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean canStartGame() {
        return players.size() == 2 && !gameStarted; // 修正逻辑：两名玩家且游戏未开始
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
        if (!gameStarted || !playerId.equals(currentPlayerId)) return;
        if (x < 0 || x >= 15 || y < 0 || y >= 15 || board[x][y] != 0) return;

        int pieceValue = playerId.equals(blackPlayerId) ? 1 : 2;
        board[x][y] = pieceValue;


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

        // 创建��移除的无效会话列表
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


}
