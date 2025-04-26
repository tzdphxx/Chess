package com.game.old.webSocket;

import com.game.old.Room;

import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private static ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, String> userIdToRoomId =  new ConcurrentHashMap<>();

    private static final RoomManager instance = new RoomManager();
    public static RoomManager getInstance() {
        return instance;
    }

    public void addRoom(Room room, int userId1, int userId2) {
        rooms.put(room.getRoomId(), room);
        userIdToRoomId.put(userId1, room.getRoomId());
        userIdToRoomId.put(userId2, room.getRoomId());
    }

    public Room getRoomByRoomId(String roomId){
        return rooms.get(roomId);
    }

    public Room getRoomByUserId(int userId){
        String roomId = userIdToRoomId.get(userId);
        if (roomId == null){
            return null;
        }
        return getRoomByRoomId(roomId);
    }

    public void removeRoom(String roomId, int userId1, int userId2){
        Room room = rooms.get(roomId);
        if (room == null){
            return;
        }
        userIdToRoomId.remove(userId1);
        userIdToRoomId.remove(userId2);
        rooms.remove(roomId);
    }

    public void removeUserFromRoom(int userId) {
        String roomId = userIdToRoomId.get(userId);
        if (roomId == null){
            return;
        }
        Room room = rooms.get(roomId);
        if (room == null){
            return;
        }
        // 清理用户关联的房间
        userIdToRoomId.remove(userId);
        if (room.getUser1() != null && room.getUser1().getUserId() == userId) {
            room.setUser1(null);
        } else if (room.getUser2() != null && room.getUser2().getUserId() == userId) {
            room.setUser2(null);
        }

        // 如果房间为空则销毁
        if (room.getUser1() == null && room.getUser2() == null) {
            rooms.remove(roomId);
        }
    }
}
