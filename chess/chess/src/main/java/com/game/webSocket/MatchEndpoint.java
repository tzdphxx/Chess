package com.game.webSocket;


import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/match/{userId}")
public class MatchEndpoint {
    private static final Map<Integer, Session> sessions = new ConcurrentHashMap<>();
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") int userId){
        sessions.put(userId,session);
        System.out.println("用户 " + userId + " 已连接到匹配服务器");

    }

    @OnClose
    public void onClose(Session session,@PathParam("userId") int userId){
        sessions.remove(userId);
        System.out.println("用户 " + userId + " 已断开连接");
    }

    @OnMessage
    public void onMessage(Session session, String message,@PathParam("userId") int userId){

        System.out.println("收到来自用户 " + userId + " 的消息: " + message);
    }

    @OnError
    public void onError(Session session, Throwable error,@PathParam("userId") int userId){
        System.out.println("用户 " + userId + " 发生错误: " + error.getMessage());
        error.printStackTrace();
    }

    public static void notifyMatchSuccess(int userId, int opponentId,String roomId){
        Session session = sessions.get(userId);
        if(session != null && session.isOpen()){
            JSONObject message = new JSONObject();
            message.put("type","MATCH_SUCCESS");
            message.put("opponentId",opponentId);
            message.put("roomId",roomId);
            try {
                session.getBasicRemote().sendText(message.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }




}
