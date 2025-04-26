package com.game.service;

import com.game.model.Room;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface RoomService {
    public Room getRoomById(String id) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    public int addRoom(Room room) throws SQLException;
}
