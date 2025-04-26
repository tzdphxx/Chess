package com.game.old;

import com.alibaba.fastjson.JSON;
import com.game.model.User;
import com.game.old.webSocket.*;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;

@ServerEndpoint(value = "/match", configurator = AuthConfigurator.class)
public class MatchAPI {
    private static final OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
    private static final Matcher matcher = Matcher.getInstance();


    @OnOpen
    public void onOpen(Session session) {
        User user = (User)session.getUserProperties().get("user");
        MatchResponse response = new MatchResponse();
        if (user == null) {
            response.setOk(false);
            response.setReason("玩家尚未登录！");
            sendErrorResponse(session,response);
            return;
        }
        if (onlineUserManager.getFromGameHall(user.getUserId()) != null
                || onlineUserManager.getFromGameRoom(user.getUserId())!=null) {
            response.setOk(false);
            response.setReason("禁止多开游戏大厅界面！");
            sendErrorResponse(session,response);
            return;
        }
        //设置玩家上线
        onlineUserManager.enterGameHall(user.getUserId(), session);
        System.out.println("玩家进入匹配页面："+user.getUserId());
    }

    private void sendErrorResponse(Session session, MatchResponse response) {
        try {
            session.getBasicRemote().sendText(JSON.toJSONString(response));
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, response.getReason()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        User user = (User)session.getUserProperties().get("user");
        if (user == null) {
            return;
        }

        try {
            MatchRequest request = JSON.parseObject(message, MatchRequest.class);
            MatchResponse response = new MatchResponse();



            if ("startMatch".equals(request.getMessage())){
                response.setOk(true);
                matcher.add(user);
                response.setMessage("startMatch");
            }else if ("stopMatch".equals(request.getMessage())){
                response.setOk(false);
                matcher.remove(user);
                response.setMessage("stopMatch");
            }else {
                response.setOk(false);
                response.setMessage("unknown");
            }

            session.getBasicRemote().sendText(JSON.toJSONString(response));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @OnClose
    public void onClose(Session session,CloseReason reason) {
        handleDisconnect(session);
    }

    private void handleDisconnect(Session session) {
        User user = (User)session.getUserProperties().get("user");
        if (user == null) {
            return;
        }

        if (OnlineUserManager.getFromGameHall(user.getUserId()) == session) {
            OnlineUserManager.exitGameHall(user.getUserId());
            matcher.remove(user);
            System.out.println("玩家离开了匹配页面： "+user.getUserId());
        }
    }


    @OnError
    public void onError(Session session, @PathParam("userId") int userId, Throwable error) {
        System.out.println("匹配页面连接出现异常! userId: " + userId + ", message: " + error.getMessage());
        handleSessionClose(session,userId,"连接异常");
    }

    private static void handleSessionClose(Session session, int userId, String message) {
        User user = (User) session.getUserProperties().get("user");
        if (user == null) {
            System.out.println("[onClose]玩家尚未登录！");
            return;
        }
        Session existSession = onlineUserManager.getFromGameHall(userId);
        if (existSession != session) {
            System.out.println("当前的会话不是玩家游戏中的会话");
            return;
        }
        System.out.println(message + ": " + userId);
        onlineUserManager.exitGameHall(userId);
        matcher.remove(user);
    }

    
}
