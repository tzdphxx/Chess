package com.game.service.Impl;

import com.game.dao.GameRecordMapper;
import com.game.model.GameRecord;
import com.game.service.GameRecordService;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class GameRecordServiceImpl implements GameRecordService {
    private static GameRecordMapper gameRecordMapper = new GameRecordMapper();

    @Override
    public GameRecord getGameRecordById(String gameId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return gameRecordMapper.selectGameRecordById(gameId);
    }

    @Override
    public int saveGameRecord(String gameId, String roomId, int blackPlayerId, int whitePlayerId, int winnerId, String finalBoard, Timestamp startTime, Timestamp endTime, int totalMoves, String moveHistory) throws SQLException {
        return gameRecordMapper.insertGameRecord(gameId, roomId,blackPlayerId,whitePlayerId,winnerId,finalBoard,startTime,endTime,totalMoves,moveHistory);
    }

    @Override
    public List<GameRecord> getGameRecordByUserId(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return gameRecordMapper.selectGameRecordsByPlayerId(userId);
    }

    @Override
    public List<GameRecord> getAllRecords() throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return gameRecordMapper.selectAll();
    }

}
