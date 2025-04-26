package com.game.old.webSocket;

import jakarta.websocket.Session;

import java.util.concurrent.ConcurrentHashMap;

public class OnlineUserManager {
    private static final ConcurrentHashMap<Integer, Session> gameHall = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Session> gameRoom = new ConcurrentHashMap<>();

    private static final OnlineUserManager instance = new OnlineUserManager();
    public static OnlineUserManager getInstance() {
        return instance;
    }

    // 用户进入大厅
    public static void enterGameHall(int userId, Session session) {
        gameHall.put(userId, session);
    }

    // 用户退出大厅
    public static void exitGameHall(int userId) {
        gameHall.remove(userId);
    }

    // 从大厅获取用户会话
    public static Session getFromGameHall(int userId) {
        return gameHall.get(userId);
    }

    // 用户进入房间
    public static void enterGameRoom(int userId, Session session) {
        gameRoom.put(userId, session);
        gameHall.remove(userId); // 进入房间时自动退出大厅
    }

    // 用户退出房间
    public static void exitGameRoom(int userId) {
        gameRoom.remove(userId);
    }

    // 从房间获取用户会话
    public static Session getFromGameRoom(int userId) {
        return gameRoom.get(userId);
    }

    //检查是否在线
    public static boolean isUserOnline(int userId) {
        return gameHall.containsKey(userId) || gameRoom.containsKey(userId);
    }

}
