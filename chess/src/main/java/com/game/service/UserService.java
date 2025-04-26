package com.game.service;

import com.game.model.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface UserService {

    User selectByName(String username) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    User selectByEmail(String email) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    User selectByPhone(String phone) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    User selectById(int id) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    int insertUser(String username, String password, String email, String phone) throws SQLException;

    int updateUser(User user) throws SQLException;

   int deleteUser(int userId) throws SQLException;

    int userWin(int userId) throws SQLException;

    int userLose(int userId) throws SQLException;

    int updateEloScore(int userId, int eloChange) throws SQLException;


    int updateLastLogin(int userId) throws SQLException;

    int updateStatus(int userId, String status) throws SQLException;

    List<User> selectByElo() throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    List<User> searchByUsername(String username) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
