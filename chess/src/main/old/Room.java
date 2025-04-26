package com.game.old;

import com.alibaba.fastjson.JSON;
import com.game.model.User;
import com.game.service.UserService;
import com.game.old.webSocket.OnlineUserManager;
import com.game.old.webSocket.RoomManager;
import jakarta.websocket.Session;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;


public class Room {
    private String roomId;
    private User user1;
    private User user2;
    private int whiteUserId ;
    private int currentTurnUserId;

    private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
    private UserService userService = new UserService();
    private RoomManager roomManager = RoomManager.getInstance();

    private static final int MAX_ROW = 15;
    private static final int MAX_COL = 15;
    private int[][] chessBoard = new int[MAX_ROW][MAX_COL];

    public Room(){
        roomId = UUID.randomUUID().toString();
    }

    public Room(String roomId, User user1, User user2, int whiteUserId,int[][] chessBoard) {
        this.roomId = roomId;
        this.user1 = user1;
        this.user2 = user2;
        this.whiteUserId = whiteUserId;
        this.chessBoard = chessBoard;
        this.currentTurnUserId = whiteUserId;
    }



    public int getCurrentTurnUserId() {
        return currentTurnUserId;
    }
    public void setCurrentTurnUserId(int currentTurnUserId) {
        this.currentTurnUserId = currentTurnUserId;
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
     * @return user1
     */
    public User getUser1() {
        return user1;
    }

    /**
     * 设置
     * @param user1
     */
    public void setUser1(User user1) {
        this.user1 = user1;
    }

    /**
     * 获取
     * @return user2
     */
    public User getUser2() {
        return user2;
    }

    /**
     * 设置
     * @param user2
     */
    public void setUser2(User user2) {
        this.user2 = user2;
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

    /**
     * 获取
     * @return chessBoard
     */
    public int[][] getChessBoard() {
        return chessBoard;
    }

    /**
     * 设置
     * @param chessBoard
     */
    public void setChessBoard(int[][] chessBoard) {
        this.chessBoard = chessBoard;
    }

    public String toString() {
        return "Room{roomId = " + roomId + ", user1 = " + user1 + ", user2 = " + user2 + ", whiteUserId = " + whiteUserId + ", MAX_ROW = " + MAX_ROW + ", MAX_COL = " + MAX_COL + ", currentTurnUserId = " + currentTurnUserId + "}";
    }

    public void putChess(String message) throws IOException, SQLException {
        GameRequest req = JSON.parseObject(message, GameRequest.class);
        GameResponse response = new GameResponse();

        if (req.getUserId() != currentTurnUserId) {
            System.out.println("现在不是该玩家的回合，当前回合: " + currentTurnUserId + ", 请求方: " + req.getUserId());

            //检查是否在线
            Session session1 = OnlineUserManager.getFromGameRoom(user1.getUserId());
            Session session2 = OnlineUserManager.getFromGameRoom(user2.getUserId());

            if (session1 == null || session2 == null) {
                System.out.println("玩家掉线，终止游戏");
                roomManager.removeRoom(roomId, user1.getUserId(), user2.getUserId());
                return;
            }


            // 发送错误消息给请求方
            Session requestSession = onlineUserManager.getFromGameRoom(req.getUserId());
            if (requestSession != null) {
                GameResponse errorResponse = new GameResponse();
                errorResponse.setType("error");
                errorResponse.setReason("当前不是你的回合");
                try {
                    requestSession.getBasicRemote().sendText(JSON.toJSONString(errorResponse));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }

        // 落子逻辑保持不变
        int chess = req.getUserId() == user1.getUserId() ? 1 : 2;
        int row = req.getRow();
        int col = req.getCol();
        if (chessBoard[row][col] != 0) {
            System.out.println("该点已经落子" + req);
            return;
        }
        chessBoard[row][col] = chess;

        // 换人
        currentTurnUserId = (currentTurnUserId == user1.getUserId()) ? user2.getUserId() : user1.getUserId();
        System.out.println("切换玩家: " + currentTurnUserId);

        // 检查游戏结束
        int winner = checkWinner(chess, row, col);
        response.setUserId(req.getUserId());
        response.setRow(req.getRow());
        response.setCol(req.getCol());
        response.setWinner(winner);
        response.setNextTurn(currentTurnUserId);

        // 检查玩家是否掉线
        Session session1 = onlineUserManager.getFromGameRoom(user1.getUserId());
        Session session2 = onlineUserManager.getFromGameRoom(user2.getUserId());

        if (session2 == null) {
            response.setWinner(user1.getUserId());
            System.out.println("玩家2 掉线");
        }
        if (session1 == null) {
            response.setWinner(user2.getUserId());
            System.out.println("玩家1 掉线");
        }

        String responseString = JSON.toJSONString(response);

        if (session1 != null) {
            session1.getBasicRemote().sendText(responseString);
        }
        if (session2 != null) {
            session2.getBasicRemote().sendText(responseString);
        }

        if (response.getWinner() != 0) {
            userService.userWin(response.getWinner() == user1.getUserId() ? user1.getUserId() : user2.getUserId());
            userService.userLose(response.getWinner() == user1.getUserId() ? user2.getUserId() : user1.getUserId());
            roomManager.removeRoom(roomId, user1.getUserId(), user2.getUserId());
            System.out.println("游戏结束, 房间已经销毁! roomId: " + roomId + " 获胜方为: " + response.getWinner());
        }
    }




    public int checkWinner(int chess, int row, int col) {
        boolean done = false;

        // 检查水平
        for (int c = Math.max(0, col - 4); c <= Math.min(MAX_COL - 5, col); c++) {
            if (chessBoard[row][c] == chess
                    && chessBoard[row][c+1] == chess
                    && chessBoard[row][c+2] == chess
                    && chessBoard[row][c+3] == chess
                    && chessBoard[row][c+4] == chess) {
                done = true;
                break;
            }
        }

        // 检查垂直
        if (!done) {
            for (int r = Math.max(0, row - 4); r <= Math.min(MAX_ROW - 5, row); r++) {
                if (chessBoard[r][col] == chess
                        && chessBoard[r+1][col] == chess
                        && chessBoard[r+2][col] == chess
                        && chessBoard[r+3][col] == chess
                        && chessBoard[r+4][col] == chess) {
                    done = true;
                    break;
                }
            }
        }

        // 检查左上到右下
        if (!done) {
            for (int i = -4; i <= 0; i++) {
                int r = row + i;
                int c = col + i;
                if (r < 0 || r + 4 >= MAX_ROW || c < 0 || c + 4 >= MAX_COL) {
                    continue;
                }
                if (chessBoard[r][c] == chess
                        && chessBoard[r+1][c+1] == chess
                        && chessBoard[r+2][c+2] == chess
                        && chessBoard[r+3][c+3] == chess
                        && chessBoard[r+4][c+4] == chess) {
                    done = true;
                    break;
                }
            }
        }

        // 检查右上到左下
        if (!done) {
            for (int i = -4; i <= 0; i++) {
                int r = row + i;
                int c = col - i;
                if (r < 0 || r + 4 >= MAX_ROW || c - 4 < 0 || c >= MAX_COL) {
                    continue;
                }
                if (chessBoard[r][c] == chess
                        && chessBoard[r+1][c-1] == chess
                        && chessBoard[r+2][c-2] == chess
                        && chessBoard[r+3][c-3] == chess
                        && chessBoard[r+4][c-4] == chess) {
                    done = true;
                    break;
                }
            }
        }

        if (!done) {
            return 0;
        }

        return chess == 1 ? user1.getUserId() : user2.getUserId();
    }


}
