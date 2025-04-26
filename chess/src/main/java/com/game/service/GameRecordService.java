package com.game.service;

import com.game.model.GameRecord;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface GameRecordService {
    GameRecord getGameRecordById(String gameId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    int saveGameRecord(String gameId, String roomId, int blackPlayerId, int whitePlayerId, int winnerId, String finalBoard, Timestamp startTime, Timestamp endTime, int totalMoves, String moveHistory) throws SQLException;

    List<GameRecord> getGameRecordByUserId(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    List<GameRecord> getAllRecords() throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
