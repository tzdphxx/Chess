package com.game.old;

public class GameResponse {
    private String type = "putChess";
    private int userId;
    private int row;
    private int col;
    private int winner;
    private String reason;
    private int nextTurn;

    public GameResponse() {
    }

    public GameResponse(String type, int userId, int row, int col, int winner, String reason, int nextTurn) {
        this.type = type;
        this.userId = userId;
        this.row = row;
        this.col = col;
        this.winner = winner;
        this.reason = reason;
        this.nextTurn = nextTurn;
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
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 获取
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * 设置
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * 获取
     * @return col
     */
    public int getCol() {
        return col;
    }

    /**
     * 设置
     * @param col
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * 获取
     * @return winner
     */
    public int getWinner() {
        return winner;
    }

    /**
     * 设置
     * @param winner
     */
    public void setWinner(int winner) {
        this.winner = winner;
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
     * @return nextTurn
     */
    public int getNextTurn() {
        return nextTurn;
    }

    /**
     * 设置
     * @param nextTurn
     */
    public void setNextTurn(int nextTurn) {
        this.nextTurn = nextTurn;
    }

    public String toString() {
        return "GameResponse{type = " + type + ", userId = " + userId + ", row = " + row + ", col = " + col + ", winner = " + winner + ", reason = " + reason + ", nextTurn = " + nextTurn + "}";
    }
}
