package com.game.webSocket;


import com.game.dao.UserMapper;
import com.game.model.Message;
import com.game.model.User;

import com.game.service.Impl.MessageServiceImpl;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/game/{roomId}/{userId}")
public class GameEndpoint {
    private static final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();
    private String roomId;
    private String userId;
    private GameRoom gameRoom;



    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId, @PathParam("userId") String userId) throws IOException {
        try {
            System.out.println("新的WebSocket连接: roomId=" + roomId + ", userId=" + userId);

            if (roomId == null || userId == null) {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "无效的连接参数"));
                return;
            }

            this.roomId = roomId;
            this.userId = userId;

            // 获取或创建房间
            gameRoom = rooms.computeIfAbsent(roomId, k -> {
                System.out.println("创建新房间: " + k);
                return new GameRoom(k);
            });

            System.out.println("玩家" + userId + "加入房间" + roomId);

            // 检查是否断线重连
            if (gameRoom.isPlayerDisconnected(userId)) {
                gameRoom.addPlayer(userId, session);
                // 广播重连成功
                GameMessage reconnectMsg = new GameMessage();
                reconnectMsg.setType("RECONNECT_SUCCESS");
                reconnectMsg.setMessage("玩家" + userId + "重连成功，游戏继续");
                gameRoom.broadcast(reconnectMsg);

                // 广播当前棋盘和状态给所有玩家
                broadcastSyncState();

                return;
            }

            gameRoom.addPlayer(userId, session);

            // 发送欢迎消息
            GameMessage welcomeMsg = new GameMessage();
            welcomeMsg.setType("SYSTEM");
            welcomeMsg.setMessage("欢迎加入房间 " + roomId);
            welcomeMsg.setRoomId(roomId);
            session.getBasicRemote().sendText(welcomeMsg.toJson());

            // 新玩家加入时也广播棋盘
            broadcastSyncState();
        } catch (Exception e) {
            System.err.println("处理WebSocket连接时出错: " + e.getMessage());
            if (session.isOpen()) {
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "服务器错误"));
            }
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException, SQLException {

        try {
            GameMessage gameMessage = GameMessage.fromJson(message);
            if (gameMessage == null) {
                sendMessage(session, new GameMessage("ERROR", "消息格式错误"));
                return;
            }

            String type = gameMessage.getType();
            System.out.println("消息类型: " + type);


            if ("START_GAME".equals(type)) {
                if (gameRoom.canStartGame()) {
                    gameRoom.startGame();
                    GameMessage startMessage = new GameMessage();
                    startMessage.setType("GAME_START");
                    startMessage.setBlackPlayerId(gameRoom.getBlackPlayerId());
                    startMessage.setWhitePlayerId(gameRoom.getWhitePlayerId());
                    startMessage.setCurrentPlayerId(gameRoom.getCurrentPlayerId());
                    gameRoom.broadcast(startMessage); // 通知所有玩家
                } else {
                    sendMessage(session, new GameMessage("ERROR", "无法开始游戏，玩家人数不足或游戏已开始"));
                }
            } else if ("MOVE".equals(type)) {
                gameRoom.makeMove(userId, gameMessage.getX(), gameMessage.getY());
            } else if ("SURRENDER".equals(type)) {
                gameRoom.surrender(userId);
            } else if ("CHAT".equals(type)) {
                try {
                    UserMapper userMapper = new UserMapper();
                    User sender = userMapper.selectById(Integer.parseInt(userId));
                    String senderName = sender != null ? sender.getUsername() : "未知玩家";


                    Message chatMsg = new Message();
                    chatMsg.setSenderId(Integer.parseInt(userId));
                    chatMsg.setReceiverId(-1); // -1表示房间广播
                    chatMsg.setContent(gameMessage.getChatContent());
                    chatMsg.setType("ROOM_CHAT");

                    new MessageServiceImpl().sendMessages(chatMsg);

                    GameMessage broadcastMsg = new GameMessage();
                    broadcastMsg.setType("CHAT");
                    broadcastMsg.setSenderId(Integer.parseInt(userId));
                    broadcastMsg.setSenderName(senderName);
                    broadcastMsg.setChatContent(gameMessage.getChatContent());
                    gameRoom.broadcast(broadcastMsg);
                } catch (Exception e) {
                    System.err.println("处理聊天消息时出错: " + e.getMessage());
                    e.printStackTrace();
                    // 发送错误消息给发送者
                    sendMessage(session, new GameMessage("ERROR", "发送消息失败: " + e.getMessage()));
                }
            } else {
                System.err.println("未知的消息类型: " + type);
                sendMessage(session, new GameMessage("ERROR", "未知的消息类型"));
            }
        } catch (Exception e) {
            System.err.println("处理消息时发生错误: " + e.getMessage());
            e.printStackTrace();
            try {
                sendMessage(session, new GameMessage("ERROR", "��理消息时发生错误"));
            } catch (IOException ex) {
                System.err.println("发送错误消息时出错: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        try {
            System.out.println("WebSocket连接关闭: roomId=" + roomId + ", userId=" + userId);
            if (gameRoom != null) {
                gameRoom.removePlayer(userId);

                // 如果房间为空，从rooms中移除
                if (gameRoom.isEmpty()) {
                    System.out.println("房间" + roomId + "已空，移除房间");
                    rooms.remove(roomId);
                }
            }
        } catch (Exception e) {
            System.err.println("处理WebSocket关闭时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("WebSocket错误: roomId=" + roomId + ", userId=" + userId);
        error.printStackTrace();
        try {
            if (session.isOpen()) {
                session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, "发生错误: " + error.getMessage()));
            }
        } catch (IOException e) {
            System.err.println("关闭WebSocket连接时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendMessage(Session session, GameMessage gameMessage) throws IOException {
        if (session == null || !session.isOpen()) {
            System.err.println("尝试向已关闭的会话发送消息");
            return;
        }
        try {
            String jsonMessage = gameMessage.toJson();
            System.out.println("发送消息: " + jsonMessage);
            session.getBasicRemote().sendText(jsonMessage);
        } catch (Exception e) {
            System.err.println("发送消息时出错: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    //广播棋盘和状态给所有玩家
    private void broadcastSyncState() {
        try {
            GameMessage syncMsg = new GameMessage();
            syncMsg.setType("SYNC_STATE");
            syncMsg.setBoard(gameRoom.getBoardCopy());
            syncMsg.setCurrentPlayerId(gameRoom.getCurrentPlayerId());
            syncMsg.setBlackPlayerId(gameRoom.getBlackPlayerId());
            syncMsg.setWhitePlayerId(gameRoom.getWhitePlayerId());
            syncMsg.setGameStarted(gameRoom.isGameStarted());
            gameRoom.broadcast(syncMsg);
        } catch (Exception e) {
            System.err.println("广播棋盘状态失败: " + e.getMessage());
        }
    }
}

