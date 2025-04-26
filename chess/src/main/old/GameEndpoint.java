package com.game.old;

import com.alibaba.fastjson.JSON;
import com.game.model.*;
import com.game.old.webSocket.AuthConfigurator;
import com.game.old.webSocket.OnlineUserManager;
import com.game.old.webSocket.RoomManager;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/game",configurator = AuthConfigurator.class)
public class GameEndpoint {


    private static final Map<Integer,Session> userSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        User user = (User) session.getUserProperties().get("user");
        if (user == null) {
            closeSession(session, "用户尚未登录");
            return;
        }

        // 直接获取房间不再等待
        Room room = RoomManager.getInstance().getRoomByUserId(user.getUserId());
        if (room == null) {
            closeSession(session, "用户未在房间");
            return;
        }

        // 检查现有会话
        Session existing = userSessions.get(user.getUserId());
        if (existing != null && existing.isOpen()) {
            closeSession(existing, "关闭旧连接");
        }

        // 更新会话
        userSessions.put(user.getUserId(), session);

        // 初始化玩家到房间
        synchronized (room) {
            if (room.getUser1() == null) {
                room.setUser1(user);
            } else if (room.getUser2() == null) {
                room.setUser2(user);
                // 当第二个玩家加入时发送游戏准备消息
                noticeGameReady(room);
            }
        }
    }


    private static void closeSession(Session session, String reason) {
        try {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY,reason));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void noticeGameReady(Room room) {
        GameReadyResponse resp1 = new GameReadyResponse();
        resp1.setRoomId(room.getRoomId());
        resp1.setThisUserId(room.getUser1().getUserId());
        resp1.setThatUserId(room.getUser2().getUserId());
        resp1.setWhiteUserId(room.getWhiteUserId());

        GameReadyResponse resp2 = new GameReadyResponse();
        resp2.setRoomId(room.getRoomId());
        resp2.setThisUserId(room.getUser2().getUserId());
        resp2.setThatUserId(room.getUser1().getUserId());
        resp2.setWhiteUserId(room.getWhiteUserId());

        sendResponse(room.getUser1().getUserId(), resp1);
        sendResponse(room.getUser2().getUserId(), resp2);
    }


    @OnClose
    public void onClose(Session session) {
        User user = (User) session.getUserProperties().get("user");
        if (user != null) {
            userSessions.remove(user.getUserId());
            RoomManager.getInstance().removeUserFromRoom(user.getUserId());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        User user = (User) session.getUserProperties().get("user");
        if (user != null) {
            userSessions.remove(user.getUserId());
            System.out.println("用户连接错误: userId=" + user.getUserId() + ", error=" + error.getMessage());
        }
    }


    @OnMessage
    public void onMessage(String message,Session session) throws IOException {
        User user = (User) session.getUserProperties().get("user");
        if (user == null) {
            return;
        }

        Session currentSession = userSessions.get(user.getUserId());
        if (currentSession == null || currentSession.equals(session)) {
            System.out.println("会话无效: userId=" + user.getUserId());
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "会话无效"));
            return;
        }





        if ("heartbeat".equals(JSON.parseObject(message).getString("type"))) {
            session.getBasicRemote().sendText("{\"type\":\"heartbeat\"}");
            return;
        }

        // 处理初始化消息
        if ("init".equals(JSON.parseObject(message).getString("type"))) {
            System.out.println("收到初始化消息: userId=" + user.getUserId());
            Room room = RoomManager.getInstance().getRoomByUserId(user.getUserId());
            if (room != null) {
                GameReadyResponse resp = new GameReadyResponse();
                resp.setOk(true);
                resp.setRoomId(room.getRoomId());
                resp.setThisUserId(user.getUserId());
                resp.setThatUserId(user.getUserId() == room.getUser1().getUserId() ?
                        room.getUser2().getUserId() : room.getUser1().getUserId());
                resp.setWhiteUserId(room.getWhiteUserId());
                session.getBasicRemote().sendText(JSON.toJSONString(resp));
            }
            return;
        }



        Room room = RoomManager.getInstance().getRoomByUserId(user.getUserId());
        if (room != null) {
            try {
                System.out.println("收到落子消息: userId=" + user.getUserId() + ", message=" + message);
                room.putChess(message);
            } catch (IOException |SQLException e) {
                System.out.println("处理落子消息异常: " + e.getMessage());
                e.printStackTrace();
            }
        }else {
            System.out.println("用户尝试落子但没有找到房间: userId=" + user.getUserId());
        }
    }

    private void sendResponse(int userId, GameReadyResponse response) {
        Session session = OnlineUserManager.getFromGameRoom(userId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(JSON.toJSONString(response));
            } catch (IOException e) {
                System.out.println("发送准备消息失败: " + e.getMessage());
            }
        }
    }

}
