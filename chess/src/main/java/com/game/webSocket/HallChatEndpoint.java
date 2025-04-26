package com.game.webSocket;

import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/hallChat")
public class HallChatEndpoint {
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    private static final Map<String, Session> userSessionMap = new ConcurrentHashMap<>();
    private static final Map<Session, String> sessionUserMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        broadcastOnlineCount();
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);

        String userId = sessionUserMap.remove(session);
        if (userId != null) {
            userSessionMap.remove(userId);
        }
        broadcastOnlineCount();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            JSONObject obj = JSONObject.parseObject(message);
            String type = obj.getString("type");
            if ("CHAT".equals(type)) {
                JSONObject chatMsg = new JSONObject();
                chatMsg.put("type", "CHAT");
                chatMsg.put("sender", obj.getString("sender"));
                chatMsg.put("content", obj.getString("content"));
                broadcast(chatMsg.toJSONString());
            } else if ("BIND_USER".equals(type)) {
                // 绑定userId到session
                String userId = obj.getString("userId");
                if (userId != null) {
                    userSessionMap.put(userId, session);
                    sessionUserMap.put(session, userId);
                }
            }
        } catch (Exception ignored) {}
    }

    @OnError
    public void onError(Session session, Throwable error) {
        sessions.remove(session);
        String userId = sessionUserMap.remove(session);
        if (userId != null) {
            userSessionMap.remove(userId);
        }
        broadcastOnlineCount();
    }

    // 广播所有
    private void broadcast(String msg) {
        for (Session s : sessions) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(msg);
                } catch (IOException ignored) {}
            }
        }
    }

    // 单发
    private void sendToUser(String userId, String msg) {
        Session s = userSessionMap.get(userId);
        if (s != null && s.isOpen()) {
            try {
                s.getBasicRemote().sendText(msg);
            } catch (IOException ignored) {}
        }
    }

    private void broadcastOnlineCount() {
        JSONObject obj = new JSONObject();
        obj.put("type", "ONLINE_COUNT");
        obj.put("count", sessions.size());
        broadcast(obj.toJSONString());
    }


    public static void sendAdminBroadcast(String content) {
        JSONObject obj = new JSONObject();
        obj.put("type", "ADMIN_BROADCAST");
        obj.put("content", content);
        String msg = obj.toJSONString();
        for (Session s : sessions) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(msg);
                } catch (IOException ignored) {}
            }
        }
    }

    public static void sendAdminSingle(String userId, String content) {
        JSONObject obj = new JSONObject();
        obj.put("type", "ADMIN_SINGLE");
        obj.put("content", content);
        obj.put("targetUserId", userId);
        String msg = obj.toJSONString();
        Session s = userSessionMap.get(userId);
        if (s != null && s.isOpen()) {
            try {
                s.getBasicRemote().sendText(msg);
            } catch (IOException ignored) {}
        }
    }
}
