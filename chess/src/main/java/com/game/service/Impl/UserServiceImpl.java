package com.game.service.Impl;

import com.game.dao.UserMapper;
import com.game.model.User;
import com.game.service.UserService;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserMapper userMapper = new UserMapper();
    @Override
    public User selectByName(String username) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return userMapper.selectByName(username);
    }

    @Override
    public User selectByEmail(String email) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return userMapper.selectByEmail(email);
    }

    @Override
    public User selectByPhone(String phone) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return userMapper.selectByPhone(phone);
    }

    @Override
    public User selectById(int id) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return userMapper.selectById(id);
    }

    @Override
    public int insertUser(String username, String password, String email, String phone) throws SQLException {
        return userMapper.insertUser(username, password, email, phone);
    }

    @Override
    public int updateUser(User user) throws SQLException {
        return userMapper.updateUser(user);
    }

    @Override
    public int deleteUser(int userId) throws SQLException {
        return userMapper.delete(userId);
    }

    @Override
    public int userWin(int userId) throws SQLException {
        return userMapper.userWin(userId);
    }

    @Override
    public int userLose(int userId) throws SQLException {
        return userMapper.userLose(userId);
    }

    @Override
    public int updateEloScore(int userId, int eloChange) throws SQLException {
        return userMapper.updateEloScore(userId, eloChange);
    }

    @Override
    public int updateLastLogin(int userId) throws SQLException {
        return userMapper.updateLastLogin(userId);
    }

    @Override
    public int updateStatus(int userId, String status) throws SQLException {
        return userMapper.updateStatus(userId, status);
    }

    @Override
    public List<User> selectByElo() throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return userMapper.selectByElo();
    }

    @Override
    public List<User> searchByUsername(String username) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return userMapper.searchUserByName(username);
    }
}
