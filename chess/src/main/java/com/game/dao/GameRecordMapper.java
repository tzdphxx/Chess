package com.game.dao;

import com.game.model.GameRecord;
import com.game.util.JDBC.curd;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class GameRecordMapper {
    public int insertGameRecord(String gameId, String roomId, int blackPlayerId, int whitePlayerId, int winnerId, String finalBoard, Timestamp startTime, Timestamp endTime, int totalMoves, String moveHistory) throws SQLException {
        String sql = "insert into game_records (game_id, room_id, black_player_id, white_player_id, winner_id, final_board, start_time, end_time, total_moves, move_history) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return curd.UpdateData(sql, gameId, roomId, blackPlayerId, whitePlayerId, winnerId, finalBoard, startTime, endTime, totalMoves, moveHistory);
    }

    public GameRecord selectGameRecordById(String gameId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from game_records where game_id = ?";
        List<GameRecord> gameRecords = curd.Query(GameRecord.class, sql, gameId);
        if(gameRecords.size() == 0){
            return null;
        }
        return gameRecords.get(0);
    }

    public List<GameRecord> selectGameRecordsByPlayerId(int playerId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from game_records where black_player_id = ? or white_player_id = ? order by start_time desc";
        return curd.Query(GameRecord.class, sql, playerId, playerId);
    }

    public List<GameRecord> selectGameRecordsByRoomId(String roomId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from game_records where room_id = ? order by start_time desc";
        return curd.Query(GameRecord.class, sql, roomId);
    }

    public List<GameRecord> selectAll () throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from game_records";
        return curd.Query(GameRecord.class,sql);
    }

}
