package com.game.old;

public class GameRequest {
    private String type = "putChess";
    private int userId ;
    private int row;
    private int col;


    public GameRequest() {
    }

    public GameRequest(String type, int userId, int row, int col) {
        this.type = type;
        this.userId = userId;
        this.row = row;
        this.col = col;
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

    public String toString() {
        return "GameRequest{type = " + type + ", userId = " + userId + ", row = " + row + ", col = " + col + "}";
    }
}
