package com.game.webSocket;


import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/hallChat")
public class HallChatEndpoint {
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        broadcastOnlineCount();
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        broadcastOnlineCount();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            JSONObject obj = JSONObject.parseObject(message);
            if ("CHAT".equals(obj.getString("type"))) {
                JSONObject chatMsg = new JSONObject();
                chatMsg.put("type", "CHAT");
                chatMsg.put("sender", obj.getString("sender"));
                chatMsg.put("content", obj.getString("content"));
                broadcast(chatMsg.toJSONString());
            }
        } catch (Exception ignored) {}
    }

    @OnError
    public void onError(Session session, Throwable error) {
        sessions.remove(session);
        broadcastOnlineCount();
    }

    private void broadcast(String msg) {
        for (Session s : sessions) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(msg);
                } catch (IOException ignored) {}
            }
        }
    }

    private void broadcastOnlineCount() {
        JSONObject obj = new JSONObject();
        obj.put("type", "ONLINE_COUNT");
        obj.put("count", sessions.size());
        broadcast(obj.toJSONString());
    }
}
