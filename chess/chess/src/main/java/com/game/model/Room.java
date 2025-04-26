package com.game.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomId;
    private String roomName;
    private int isPublic;
    private String password;
    private String ownerId;
    private List<Integer> players = new ArrayList<>();


    public boolean isIsPublic(){

        return this.isPublic == 1;
    }

    public Room() {
    }

    public Room(String roomId, String roomName, int isPublic, String password, String ownerId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.isPublic = isPublic;
        this.password = password;
        this.ownerId = ownerId;
    }

    /**
     * 获取
     * @return roomId
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     * 设置
     * @param roomId
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    /**
     * 获取
     * @return roomName
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * 设置
     * @param roomName
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * 获取
     * @return isPublic
     */
    public int getIsPublic() {
        return isPublic;
    }

    /**
     * 设置
     * @param isPublic
     */
    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * 获取
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return ownerId
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * 设置
     * @param ownerId
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * 获取
     * @return players
     */
    public List<Integer> getPlayers() {
        return players;
    }

    /**
     * 设置
     * @param players
     */
    public void setPlayers(List<Integer> players) {
        this.players = players;
    }

    public String toString() {
        return "Room{roomId = " + roomId + ", roomName = " + roomName + ", isPublic = " + isPublic + ", password = " + password + ", ownerId = " + ownerId + ", players = " + players + "}";
    }
    public void addPlayer(int playerId) {
        this.players.add(playerId);
    }
}

