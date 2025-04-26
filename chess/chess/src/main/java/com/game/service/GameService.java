package com.game.service;

import com.game.model.Game;
import com.game.model.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface GameService {

    Game getGameById(String gameId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    Game getActiveGameByUserId(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    boolean makeMove(String gameId, int x, int y, boolean isBlack) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    String createGame(User blackPlayer, User whitePlayer) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    void updateGameStatus(String gameId, Game.GameState status) throws SQLException;


    void saveGameRecord(String gameId, String winnerId, String loserId, int winnerEloChange, int loserEloChange) throws SQLException;

}
