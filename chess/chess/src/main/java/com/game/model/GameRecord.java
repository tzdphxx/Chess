package com.game.model;

import java.sql.Timestamp;

public class GameRecord {
    private int recordId;
    private String roomId   ;
    private int blackPlayerId;
    private int whitePlayerId;
    private int winnerId ;
    private String finalBoard;
    private Timestamp startTime;
    private Timestamp endTime  ;
    private int totalMoves;
    private String moveHistory;
    private String gameId;


    public GameRecord() {
    }

    public GameRecord(int recordId, String roomId, int blackPlayerId, int whitePlayerId, int winnerId, String finalBoard, Timestamp startTime, Timestamp endTime, int totalMoves, String moveHistory, String gameId) {
        this.recordId = recordId;
        this.roomId = roomId;
        this.blackPlayerId = blackPlayerId;
        this.whitePlayerId = whitePlayerId;
        this.winnerId = winnerId;
        this.finalBoard = finalBoard;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalMoves = totalMoves;
        this.moveHistory = moveHistory;
        this.gameId = gameId;
    }

    /**
     * 获取
     * @return recordId
     */
    public int getRecordId() {
        return recordId;
    }

    /**
     * 设置
     * @param recordId
     */
    public void setRecordId(int recordId) {
        this.recordId = recordId;
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
     * @return blackPlayerId
     */
    public int getBlackPlayerId() {
        return blackPlayerId;
    }

    /**
     * 设置
     * @param blackPlayerId
     */
    public void setBlackPlayerId(int blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
    }

    /**
     * 获取
     * @return whitePlayerId
     */
    public int getWhitePlayerId() {
        return whitePlayerId;
    }

    /**
     * 设置
     * @param whitePlayerId
     */
    public void setWhitePlayerId(int whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
    }

    /**
     * 获取
     * @return winnerId
     */
    public int getWinnerId() {
        return winnerId;
    }

    /**
     * 设置
     * @param winnerId
     */
    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    /**
     * 获取
     * @return finalBoard
     */
    public String getFinalBoard() {
        return finalBoard;
    }

    /**
     * 设置
     * @param finalBoard
     */
    public void setFinalBoard(String finalBoard) {
        this.finalBoard = finalBoard;
    }

    /**
     * 获取
     * @return startTime
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * 设置
     * @param startTime
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取
     * @return endTime
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * 设置
     * @param endTime
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取
     * @return totalMoves
     */
    public int getTotalMoves() {
        return totalMoves;
    }

    /**
     * 设置
     * @param totalMoves
     */
    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }

    /**
     * 获取
     * @return moveHistory
     */
    public String getMoveHistory() {
        return moveHistory;
    }

    /**
     * 设置
     * @param moveHistory
     */
    public void setMoveHistory(String moveHistory) {
        this.moveHistory = moveHistory;
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

    public String toString() {
        return "GameRecord{recordId = " + recordId + ", roomId = " + roomId + ", blackPlayerId = " + blackPlayerId + ", whitePlayerId = " + whitePlayerId + ", winnerId = " + winnerId + ", finalBoard = " + finalBoard + ", startTime = " + startTime + ", endTime = " + endTime + ", totalMoves = " + totalMoves + ", moveHistory = " + moveHistory + ", gameId = " + gameId + "}";
    }
}
