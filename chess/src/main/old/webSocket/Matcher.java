package com.game.old.webSocket;

import com.alibaba.fastjson.JSON;
import com.game.old.Room;
import com.game.model.User;
import jakarta.websocket.Session;

import java.util.LinkedList;
import java.util.Queue;

public class Matcher {
    private final Queue<User> lowQueue = new LinkedList<>();
    private final Queue<User> normalQueue = new LinkedList<>();
    private final Queue<User> highQueue = new LinkedList<>();
    private final RoomManager roomManager;

    private final static Matcher instance = new Matcher(RoomManager.getInstance());

    public static Matcher getInstance() {
        return instance;
    }

    private boolean running = true;

    private Thread t1, t2, t3;

    public Matcher(RoomManager roomManager) {

        this.roomManager = roomManager;

        t1 = new Thread(() -> {
            while (true) {
                handlerMatch(lowQueue);
            }
        });
        t1.start();

        t2 = new Thread(() -> {
            while (true) {
                handlerMatch(normalQueue);
            }
        });
        t2.start();

        t3 = new Thread(() -> {
            while (true) {
                handlerMatch(highQueue);
            }
        });
        t3.start();
    }


    public void shop(){
        running = false;
        synchronized (lowQueue){
            lowQueue.notifyAll();
        }
        synchronized (normalQueue){
            normalQueue.notifyAll();
        }
        synchronized (highQueue){
            highQueue.notifyAll();
        }
        try{
            if (t1 != null)t1.join();
            if (t2 != null)t2.join();
            if (t3 != null)t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void add(User user) {
        if (user.getEloScore() < 2000) {
            synchronized (lowQueue) {
                lowQueue.offer(user);
                lowQueue.notify();
            }
            System.out.println("把玩家 " + user.getUsername() + " 加入到了lowQueue");
        } else if (user.getEloScore() >= 2000 && user.getEloScore() < 3000) {
            synchronized (normalQueue) {
                normalQueue.offer(user);
                normalQueue.notify();
            }
            System.out.println("把玩家 " + user.getUsername() + " 加入到了normalQueue");
        } else {
            synchronized (highQueue) {
                highQueue.offer(user);
                highQueue.notify();
            }
            System.out.println("把玩家 " + user.getUsername() + " 加入到了highQueue");
        }
        System.out.println("low: " + lowQueue.size() + "normal: " + normalQueue.size() + "high: " + highQueue.size());
    }

    public void remove(User user) {
        if (user.getEloScore() < 2000) {
            synchronized (lowQueue) {
                lowQueue.remove(user);
            }
            System.out.println("把玩家 " + user.getUsername() + " 移除了 lowQueue!");
        } else if (user.getEloScore() >= 2000 && user.getEloScore() < 3000) {
            synchronized (normalQueue) {
                normalQueue.remove(user);
            }
            System.out.println("把玩家 " + user.getUsername() + " 移除了 normalQueue!");
        } else {
            synchronized (highQueue) {
                highQueue.remove(user);
            }
            System.out.println("把玩家 " + user.getUsername() + " 移除了 highQueue!");
        }
    }

    private void handlerMatch(Queue<User> queue) {
        synchronized (queue) {
            try {
                while (queue.size() < 2 && running) {
                    queue.wait();
                }

                User user1 = queue.poll();
                User user2 = queue.poll();
                if (user1 == null || user2 == null) return;

                // 创建房间并立即添加到RoomManager
                Room room = new Room();
                room.setUser1(user1);
                room.setUser2(user2);
                room.setWhiteUserId(user1.getUserId()); // 明确设置白方
                roomManager.addRoom(room, user1.getUserId(), user2.getUserId());

                // 立即将玩家移出队列
                OnlineUserManager.enterGameRoom(user1.getUserId(),
                        OnlineUserManager.getFromGameHall(user1.getUserId()));
                OnlineUserManager.enterGameRoom(user2.getUserId(),
                        OnlineUserManager.getFromGameHall(user2.getUserId()));

                // 发送匹配成功消息
                MatchResponse response = new MatchResponse();
                response.setMessage("matchSuccess");
                response.setOk(true);
                String jsonResponse = JSON.toJSONString(response);

                Session session1 = OnlineUserManager.getFromGameHall(user1.getUserId());
                Session session2 = OnlineUserManager.getFromGameHall(user2.getUserId());
                if (session1 != null) session1.getBasicRemote().sendText(jsonResponse);
                if (session2 != null) session2.getBasicRemote().sendText(jsonResponse);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
