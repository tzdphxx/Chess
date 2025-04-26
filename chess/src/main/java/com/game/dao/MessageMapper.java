package com.game.dao;

import com.game.model.Message;
import com.game.util.JDBC.curd;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class MessageMapper {

    public int insertMessage(Message message) throws SQLException {
        String sql = "insert into messages (sender_id, receiver_id, content, type) values (?, ?, ?, ?)";
        return curd.UpdateData(sql, message.getSenderId(), message.getReceiverId(), message.getContent(), message.getType());
    }

    public List<Message> selectMessages(int senderId, int receiverId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from messages where (sender_id = ? AND receiver_id = ?) or (sender_id = ? AND receiver_id = ?) order by send_time ASC";
        return curd.Query(Message.class, sql, senderId, receiverId, receiverId, senderId);
    }
}
