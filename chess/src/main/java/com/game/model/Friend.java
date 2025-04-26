package com.game.model;

import java.sql.Timestamp;

public class Friend {

    private int friendshipId;
    private int user1Id;
    private int user2Id;
    private Timestamp createTime;
    private String status;


    public Friend() {
    }

    public Friend(int user1Id, int user2Id, String status) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.status = status;
    }


    public Friend(int friendshipId, int user1Id, int user2Id, Timestamp createTime, String status) {
        this.friendshipId = friendshipId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.createTime = createTime;
        this.status = status;
    }

    /**
     * 获取
     * @return friendshipId
     */
    public int getFriendshipId() {
        return friendshipId;
    }

    /**
     * 设置
     * @param friendshipId
     */
    public void setFriendshipId(int friendshipId) {
        this.friendshipId = friendshipId;
    }

    /**
     * 获取
     * @return user1Id
     */
    public int getUser1Id() {
        return user1Id;
    }

    /**
     * 设置
     * @param user1Id
     */
    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    /**
     * 获取
     * @return user2Id
     */
    public int getUser2Id() {
        return user2Id;
    }

    /**
     * 设置
     * @param user2Id
     */
    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    /**
     * 获取
     * @return createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return "Friend{friendshipId = " + friendshipId + ", user1Id = " + user1Id + ", user2Id = " + user2Id + ", createTime = " + createTime + ", status = " + status + "}";
    }
}