package com.game.service.Impl;

import com.alibaba.fastjson.JSON;
import com.game.dao.GameMapper;
import com.game.model.Game;
import com.game.model.User;
import com.game.service.GameService;


import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class GameServiceImpl implements GameService {
    private final GameMapper gameMapper = new GameMapper();


    @Override
    public Game getGameById(String gameId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return gameMapper.selectById(gameId);
    }

    @Override
    public Game getActiveGameByUserId(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return gameMapper.selectActiveGameByUserId(userId);
    }

    @Override
    public boolean makeMove(String gameId, int x, int y, boolean isBlack) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Game game = getGameById(gameId);
        if (game == null||game.getState()!=Game.GameState.PLAYING){
            return false;
        }
        if (isBlack != game.isIsBlackTurn()){
            return false;
        }
        if(x < 0 || x >= 15 || y < 0 || y >= 15 || game.getBoard()[x][y] != 0){
            return false;
        }

        int [][] board = game.getBoard();
        board[x][y] = isBlack ? 1 : 2;
        game.setBoard(board);
        game.setIsBlackTurn(!isBlack);
        game.setLastMoveTime(System.currentTimeMillis());

        int i = gameMapper.updateMakeMove(JSON.toJSONString(board), !isBlack, gameId);

        if (checkWin(board,x,y)){
            updateGameStatus(gameId,isBlack ? Game.GameState.BLACK_WIN : Game.GameState.WHITE_WIN);

        }
        return i>0;
    }

    @Override
    public String createGame(User blackPlayer, User whitePlayer) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return gameMapper.insertGame(blackPlayer,whitePlayer);
    }

    @Override
    public void updateGameStatus(String gameId, Game.GameState status) throws SQLException {
        gameMapper.updateGameStatus(gameId,status);
    }

    @Override
    public void saveGameRecord(String gameId, String winnerId, String loserId, int winnerEloChange, int loserEloChange) throws SQLException {
        gameMapper.saveGameRecord(gameId,winnerId,loserId,winnerEloChange,loserEloChange);
    }




    private boolean checkWin(int[][] board, int x, int y) {

        if (x < 0 || x >= 15 || y < 0 || y >= 15 || board[x][y] == 0){
            return false;
        }

        int color = board[x][y];
        int[] dx = {1, 1, 0, 1};
        int[] dy = {0, 1, 1, -1};

        for (int i = 0; i < dx.length; i++) {
            int count = 1;
            int currentDx = dx[i];
            int currentDy = dy[i];

            // 正向检查
            for (int step = 1; step < 5; step++) {
                int newX = x + currentDx * step;
                int newY = y + currentDy * step;
                if (newX < 0 || newX >= 15 || newY < 0 || newY >= 15 || board[newX][newY] != color) {
                    break;
                }
                count++;
            }

            // 反向检查
            for (int step = 1; step < 5; step++) {
                int newX = x - currentDx * step;
                int newY = y - currentDy * step;
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
