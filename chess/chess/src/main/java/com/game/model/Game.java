package com.game.model;

import com.game.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private String gameId;
    private User blackPlayer;
    private User whitePlayer;
    private int [][] board;
    private boolean isBlackTurn;
    private GameState state;
    private long startTime;
    private long lastMoveTime;
    private List<Move> moveHistory ;//历史
    private User winner;
    private int timeLimit;//时间限制
    //剩余时间
    private long blackRemainingTime;
    private long whiteRemainingTime;

    public Game() {
        this.gameId = IdGenerator.generateAlphaNumericId(8);
        this.board = new int[15][15];
        this.moveHistory = new ArrayList<>();
        this.state = GameState.WAITING;
        this.timeLimit = 30;
        this.blackRemainingTime = timeLimit * 1000;
        this.whiteRemainingTime = timeLimit * 1000;
    }

    public Game(String gameId, User blackPlayer, User whitePlayer, int[][] board, boolean isBlackTurn, GameState state, long startTime, long lastMoveTime, List<Move> moveHistory, User winner, int timeLimit, long blackRemainingTime, long whiteRemainingTime) {
        this.gameId = gameId;
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        this.board = board;
        this.isBlackTurn = isBlackTurn;
        this.state = state;
        this.startTime = startTime;
        this.lastMoveTime = lastMoveTime;
        this.moveHistory = moveHistory;
        this.winner = winner;
        this.timeLimit = timeLimit;
        this.blackRemainingTime = blackRemainingTime;
        this.whiteRemainingTime = whiteRemainingTime;
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
     * @return blackPlayer
     */
    public User getBlackPlayer() {
        return blackPlayer;
    }

    /**
     * 设置
     * @param blackPlayer
     */
    public void setBlackPlayer(User blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    /**
     * 获取
     * @return whitePlayer
     */
    public User getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * 设置
     * @param whitePlayer
     */
    public void setWhitePlayer(User whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    /**
     * 获取
     * @return board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * 设置
     * @param board
     */
    public void setBoard(int[][] board) {
        this.board = board;
    }

    /**
     * 获取
     * @return isBlackTurn
     */
    public boolean isIsBlackTurn() {
        return isBlackTurn;
    }

    /**
     * 设置
     * @param isBlackTurn
     */
    public void setIsBlackTurn(boolean isBlackTurn) {
        this.isBlackTurn = isBlackTurn;
    }

    /**
     * 获取
     * @return state
     */
    public GameState getState() {
        return state;
    }

    /**
     * 设置
     * @param state
     */
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * 获取
     * @return startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * 设置
     * @param startTime
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取
     * @return lastMoveTime
     */
    public long getLastMoveTime() {
        return lastMoveTime;
    }

    /**
     * 设置
     * @param lastMoveTime
     */
    public void setLastMoveTime(long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    /**
     * 获取
     * @return moveHistory
     */
    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    /**
     * 设置
     * @param moveHistory
     */
    public void setMoveHistory(List<Move> moveHistory) {
        this.moveHistory = moveHistory;
    }

    /**
     * 获取
     * @return winner
     */
    public User getWinner() {
        return winner;
    }

    /**
     * 设置
     * @param winner
     */
    public void setWinner(User winner) {
        this.winner = winner;
    }

    /**
     * 获取
     * @return timeLimit
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * 设置
     * @param timeLimit
     */
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * 获取
     * @return blackRemainingTime
     */
    public long getBlackRemainingTime() {
        return blackRemainingTime;
    }

    /**
     * 设置
     * @param blackRemainingTime
     */
    public void setBlackRemainingTime(long blackRemainingTime) {
        this.blackRemainingTime = blackRemainingTime;
    }

    /**
     * 获取
     * @return whiteRemainingTime
     */
    public long getWhiteRemainingTime() {
        return whiteRemainingTime;
    }

    /**
     * 设置
     * @param whiteRemainingTime
     */
    public void setWhiteRemainingTime(long whiteRemainingTime) {
        this.whiteRemainingTime = whiteRemainingTime;
    }

    public String toString() {
        return "Game{gameId = " + gameId + ", blackPlayer = " + blackPlayer + ", whitePlayer = " + whitePlayer + ", board = " + board + ", isBlackTurn = " + isBlackTurn + ", state = " + state + ", startTime = " + startTime + ", lastMoveTime = " + lastMoveTime + ", moveHistory = " + moveHistory + ", winner = " + winner + ", timeLimit = " + timeLimit + ", blackRemainingTime = " + blackRemainingTime + ", whiteRemainingTime = " + whiteRemainingTime + "}";
    }


    public static class Move{
        private int x;
        private int y;
        private boolean isBlack;
        private long timeStamp;

        public Move(int x, int y, boolean isBlack) {
            this.x = x;
            this.y = y;
            this.isBlack = isBlack;
            this.timeStamp = System.currentTimeMillis();
        }

        public Move() {
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
         * @return isBlack
         */
        public boolean isIsBlack() {
            return isBlack;
        }

        /**
         * 设置
         * @param isBlack
         */
        public void setIsBlack(boolean isBlack) {
            this.isBlack = isBlack;
        }

        /**
         * 获取
         * @return timeStamp
         */
        public long getTimeStamp() {
            return timeStamp;
        }

        /**
         * 设置
         * @param timeStamp
         */
        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String toString() {
            return "Move{x = " + x + ", y = " + y + ", isBlack = " + isBlack + ", timeStamp = " + timeStamp + "}";
        }
    }

    public enum GameState {
        WAITING,    // 等待
        PLAYING,
        PAUSED,     // 暂停
        BLACK_WIN,  // 黑胜
        WHITE_WIN,  // 白胜
        DRAW        // 平
    }

    //下子
    public boolean makeMove(int x, int y, boolean isBlack) {
        if (x < 0||x>15||y<0||y>15 || board[x][y]!=0) return false;

        board[x][y] = isBlack ? 1 : 2;

        moveHistory.add(new Move(x, y, isBlack));
        isBlackTurn = !isBlackTurn;
        lastMoveTime = System.currentTimeMillis();
        return true;
    }

    //检查是否胜利
    public boolean checkWin(int x, int y) {
        int color = board[x][y];
        int[] dx = {1, 1, 0, 1};
        int[] dy = {0, 1, 1, -1};

        for (int i = 0; i < dx.length; i++) {
            int count = 1;

            // 正向检查
            for (int step = 1; step < 5; step++) {
                int newX = x + dx[i] * step;
                int newY = y + dy[i] * step;
                if (newX < 0 || newX >= 15 || newY < 0 || newY >= 15 || board[newX][newY] != color) {
                    break;
                }
                count++;
            }

            // 反向检查
            for (int step = 1; step < 5; step++) {
                int newX = x - dx[i] * step;
                int newY = y - dy[i] * step;
                if (newX < 0 || newX >= 15 || newY < 0 || newY >= 15 || board[newX][newY] != color) {
                    break;
                }
                count++;
            }

            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

}
