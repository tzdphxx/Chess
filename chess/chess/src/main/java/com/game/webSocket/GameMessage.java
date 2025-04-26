package com.game.webSocket;

import com.alibaba.fastjson.JSON;

public class GameMessage {
    private String type;
    private String message;
    private int x;
    private int y;
    private String playerId;
    private String opponentName;
    private String winner;
    private String currentPlayerId;
    private String whitePlayerId;
    private String blackPlayerId;
    private int pieceValue;
    private String chatContent;
    private int senderId;
    private int receiverId;
    private String senderName;
    private String gameId;
    private String roomId;
    private int[][] board; // 新增：用于同步棋盘
    private boolean gameStarted; // 新增：用于同步游戏状态


    public GameMessage(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public GameMessage() {
    }

    public GameMessage(String type, String message, int x, int y, String playerId, String opponentName, String winner, String currentPlayerId, String whitePlayerId, String blackPlayerId, int pieceValue, String chatContent, int senderId, int receiverId, String senderName, String gameId, String roomId) {
        this.type = type;
        this.message = message;
        this.x = x;
        this.y = y;
        this.playerId = playerId;
        this.opponentName = opponentName;
        this.winner = winner;
        this.currentPlayerId = currentPlayerId;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.pieceValue = pieceValue;
        this.chatContent = chatContent;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.gameId = gameId;
        this.roomId = roomId;
    }


    public static GameMessage fromJson(String json) {
        try {
            if (json == null || json.trim().isEmpty()) {
                System.err.println("尝试解析空JSON字符串");
                return null;
            }
            return JSON.parseObject(json, GameMessage.class);
        } catch (Exception e) {
            System.err.println("解析JSON失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String toJson() {
        try {
            return JSON.toJSONString(this);
        } catch (Exception e) {
            System.err.println("JSON转换失败: " + e.getMessage());
            e.printStackTrace();
            return "{\"type\":\"ERROR\",\"message\":\"消息格式错误\"}";
        }
    }


    /**
     * 获取
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * 设置
     * @param type
     */
    public void setType(String type) {
        this.type = type;
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
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * 设置
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * 获取
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * 设置
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * 获取
     * @return playerId
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * 设置
     * @param playerId
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    /**
     * 获取
     * @return opponentName
     */
    public String getOpponentName() {
        return opponentName;
    }

    /**
     * 设置
     * @param opponentName
     */
    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    /**
     * 获取
     * @return winner
     */
    public String getWinner() {
        return winner;
    }

    /**
     * 设置
     * @param winner
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * 获取
     * @return currentPlayerId
     */
    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    /**
     * 设置
     * @param currentPlayerId
     */
    public void setCurrentPlayerId(String currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    /**
     * 获取
     * @return whitePlayerId
     */
    public String getWhitePlayerId() {
        return whitePlayerId;
    }

    /**
     * 设置
     * @param whitePlayerId
     */
    public void setWhitePlayerId(String whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
    }

    /**
     * 获取
     * @return blackPlayerId
     */
    public String getBlackPlayerId() {
        return blackPlayerId;
    }

    /**
     * 设置
     * @param blackPlayerId
     */
    public void setBlackPlayerId(String blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
    }

    /**
     * 获取
     * @return pieceValue
     */
    public int getPieceValue() {
        return pieceValue;
    }

    /**
     * 设置
     * @param pieceValue
     */
    public void setPieceValue(int pieceValue) {
        this.pieceValue = pieceValue;
    }

    /**
     * 获取
     * @return chatContent
     */
    public String getChatContent() {
        return chatContent;
    }

    /**
     * 设置
     * @param chatContent
     */
    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    /**
     * 获取
     * @return senderId
     */
    public int getSenderId() {
        return senderId;
    }

    /**
     * 设置
     * @param senderId
     */
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    /**
     * 获取
     * @return receiverId
     */
    public int getReceiverId() {
        return receiverId;
    }

    /**
     * 设置
     * @param receiverId
     */
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * 获取
     * @return senderName
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * 设置
     * @param senderName
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * 获取
     * @return gameId
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * 设置
     * @param gameId
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
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

    public int[][] getBoard() {
        return board;
    }
    public void setBoard(int[][] board) {
        this.board = board;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public String toString() {
        return "GameMessage{type = " + type + ", message = " + message + ", x = " + x + ", y = " + y + ", playerId = " + playerId + ", opponentName = " + opponentName + ", winner = " + winner + ", currentPlayerId = " + currentPlayerId + ", whitePlayerId = " + whitePlayerId + ", blackPlayerId = " + blackPlayerId + ", pieceValue = " + pieceValue + ", chatContent = " + chatContent + ", senderId = " + senderId + ", receiverId = " + receiverId + ", senderName = " + senderName + ", gameId = " + gameId + ", roomId = " + roomId + "}";
    }
}