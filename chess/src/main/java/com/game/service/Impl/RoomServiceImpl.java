package com.game.service.Impl;

import com.game.dao.RoomMapper;
import com.game.model.Room;
import com.game.service.RoomService;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class RoomServiceImpl implements RoomService {
    private static RoomMapper roomMapper = new RoomMapper();

    @Override
    public Room getRoomById(String id) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return roomMapper.getRoom(id);
    }

    @Override
    public int addRoom(Room room) throws SQLException {
        return roomMapper.addRoom(room);
    }
}
