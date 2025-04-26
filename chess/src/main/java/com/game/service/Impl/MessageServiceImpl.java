package com.game.service.Impl;

import com.game.dao.MessageMapper;
import com.game.model.Message;
import com.game.service.MessageService;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class MessageServiceImpl implements MessageService {

    private static MessageMapper messageMapper = new MessageMapper();


    @Override
    public int sendMessages(Message message) throws SQLException {
        return messageMapper.insertMessage(message);
    }

    @Override
    public List<Message> getMessages(int sendId, int receiveId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return messageMapper.selectMessages(sendId,receiveId);
    }

}
