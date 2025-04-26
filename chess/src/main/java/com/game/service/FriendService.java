package com.game.service;

import com.game.model.Friend;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface FriendService {

    int addRequest(Friend friend) throws SQLException;

    int rejectRequest(Friend friend) throws SQLException;

    int deleteFriend(Friend friend) throws SQLException;

    List<Friend> getFriends(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    int acceptRequest(Friend friend) throws SQLException;

    List<Friend> getRequest(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
