package com.game.dao;

import com.alibaba.fastjson.JSON;
import com.game.model.Game;

import com.game.model.User;
import com.game.util.JDBC.curd;

import java.lang.reflect.InvocationTargetException;

import java.sql.SQLException;
import java.util.List;

public class GameMapper {
    public Game selectById(String id) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from games where id = ?";
        List<Game> games = curd.Query(Game.class,sql,id);
        return games.size() == 0 ? null : games.get(0);
    }

    public Game selectActiveGameByUserId(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from games where (black_player_id = ? or white_player_id = ?) and status = 'PLAYING' order by created_at desc limit 1";
        List<Game> games = curd.Query(Game.class,sql,userId,userId);
        return games.size() == 0 ? null : games.get(0);
    }

    public String insertGame(User blackPlayer, User whitePlayer) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "insert into games (black_player_id,white_player_id,status,board_status) values (?,?,'PLAYING',?)";
        int [][] boardStatus = new int[15][15];
        String boardJson = JSON.toJSONString(boardStatus);

        List<Game> games = curd.Query(Game.class,sql,blackPlayer.getUserId(),whitePlayer.getUserId(),boardJson);
        return games.size() == 0 ? null : games.get(0).getGameId();
    }


    public int updateGameStatus(String gameId, Game.GameState status) throws SQLException {
        String sql = "update games set status = ? where game_id = ?";
        return curd.UpdateData(sql,status.toString(),gameId);
    }

    public int saveGameRecord(String gameId, String winnerId, String loserId, int winnerEloChange, int loserEloChange) throws SQLException {
        String sql = "insert into game_records (game_id,winner_id,loser_id,winner_elo_change,loser_elo_change) values(?,?,?,?,?)";
        return curd.UpdateData(sql,gameId,winnerId,loserId,winnerEloChange,loserEloChange);
    }

    public int updateMakeMove(String board, boolean isBlack, String gameId) throws SQLException {
        String sql = "update games set board_status = ?,is_black_turn = ?,last_move = current_timestamp where game_id = ?";
        return curd.UpdateData(sql,board,isBlack,gameId);
    }


}
