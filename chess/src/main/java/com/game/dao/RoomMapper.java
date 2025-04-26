package com.game.dao;

import com.game.model.Room;
import com.game.util.JDBC.curd;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class RoomMapper {
    public int addRoom(Room room) throws SQLException {
        String sql = "insert into rooms (room_id,room_name,is_public,password,owner_id) values(?,?,?,?,?)";
        return curd.UpdateData(sql,room.getRoomId(),room.getRoomName(),room.isIsPublic(),room.getPassword(),room.getOwnerId());
    }

    public Room getRoom(String roomId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from rooms where room_id=?";
        List<Room>rooms =  curd.Query(Room.class,sql,roomId);
        return rooms.isEmpty() ? null : rooms.get(0);
    }
}
