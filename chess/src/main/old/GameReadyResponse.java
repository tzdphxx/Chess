package com.game.old;

public class GameReadyResponse {
    private String message = "gameReady";
    private boolean ok = true;
    private String reason = "";
    private String roomId = "";
    private int thisUserId = 0;
    private int thatUserId = 0;
    private int whiteUserId = 0;

    public GameReadyResponse() {
    }

    public GameReadyResponse(String message, boolean ok, String reason, String roomId, int thisUserId, int thatUserId, int whiteUserId) {
        this.message = message;
        this.ok = ok;
        this.reason = reason;
        this.roomId = roomId;
        this.thisUserId = thisUserId;
        this.thatUserId = thatUserId;
        this.whiteUserId = whiteUserId;
    }

    /**
     * 获取
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取
     * @return ok
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * 设置
     * @param ok
     */
    public void setOk(boolean ok) {
        this.ok = ok;
    }

    /**
     * 获取
     * @return reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取
     * @return roomId
     */
    public String getRoomId() {
        return this.roomId;
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
     * @return thisUserId
     */
    public int getThisUserId() {
        return thisUserId;
    }

    /**
     * 设置
     * @param thisUserId
     */
    public void setThisUserId(int thisUserId) {
        this.thisUserId = thisUserId;
    }

    /**
     * 获取
     * @return thatUserId
     */
    public int getThatUserId() {
        return thatUserId;
    }

    /**
     * 设置
     * @param thatUserId
     */
    public void setThatUserId(int thatUserId) {
        this.thatUserId = thatUserId;
    }

    /**
     * 获取
     * @return whiteUserId
     */
    public int getWhiteUserId() {
        return whiteUserId;
    }

    /**
     * 设置
     * @param whiteUserId
     */
    public void setWhiteUserId(int whiteUserId) {
        this.whiteUserId = whiteUserId;
    }

    public String toString() {
        return "GameReadyResponse{message = " + message + ", ok = " + ok + ", reason = " + reason + ", roomId = " + roomId + ", thisUserId = " + thisUserId + ", thatUserId = " + thatUserId + ", whiteUserId = " + whiteUserId + "}";
    }
}
