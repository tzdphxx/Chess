package com.game.service;

import com.game.model.Message;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface MessageService {
    int sendMessages(Message message) throws SQLException;

    List<Message> getMessages(int sendId, int receiveId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
