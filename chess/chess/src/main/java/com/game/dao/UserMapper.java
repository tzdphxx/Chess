package com.game.dao;

import com.game.model.User;
import com.game.util.JDBC.curd;


import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class UserMapper {

    public User selectById(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from users where user_id = ?";
        List<User> users = curd.Query(User.class,sql,userId);
        return users.size() == 0 ? null : users.get(0);
    }

    public User selectByName(String username) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from users where username = ?";
        List<User> users = curd.Query(User.class,sql,username);
        return users.size() == 0 ? null : users.get(0);
    }

    public int insertUser(String username, String password, String email, String phone) throws SQLException {
        String sql = "insert into users (username, password, email, phone) values (?, ?, ?, ?)";
        return curd.UpdateData(sql,username,password,email,phone);
    }

    public int updateUser(User user) throws SQLException {
        String sql = "update users set username = ?, password = ?, email = ?, phone = ? ,elo_score = ?, total_wins = ?, total_losses = ?  where user_id = ?";
        return curd.UpdateData(sql,user.getUsername(),user.getPassword(),user.getEmail(),user.getPhone(),user.getEloScore(),user.getTotalWins(),user.getTotalLosses(),user.getUserId());
    }

    public User selectByEmail(String email) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from users where email = ?";

            List<User> users = curd.Query(User.class,sql,email);
            return users.size() == 0 ? null : users.get(0);

    }
    public User selectByPhone(String phone) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from users where phone = ?";

            List<User> users = curd.Query(User.class,sql,phone);
            return users.size() == 0 ? null : users.get(0);

    }
    public int userWin(int userId) throws SQLException {
        String sql = "update users set total_wins = total_wins + 1 where user_id = ?";
        return curd.UpdateData(sql,userId);
    }
    public int userLose(int userId) throws SQLException {
        String sql = "update users set total_losses = total_losses + 1 where user_id = ?";
        return curd.UpdateData(sql,userId);
    }

    public int delete(int userId) throws SQLException {
        String sql = "delete from users where user_id = ?";
        return curd.UpdateData(sql,userId);
    }
    public int updateEloScore(int userId, int eloChange) throws SQLException {
        String sql = "update users set elo_score = elo_score + ? where user_id = ?";
        return curd.UpdateData(sql,eloChange,userId);
    }
    public int updateLastLogin(int userId) throws SQLException {
        String sql = "update users set last_login = current_timestamp where user_id = ?";
        return curd.UpdateData(sql,userId);
    }
    public int updateStatus(int userId, String status) throws SQLException {
        String sql = "update users set status = ? where user_id = ?";
        return curd.UpdateData(sql,status,userId);
    }
    public List<User> selectByElo() throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from users order by elo_score desc";
        return curd.Query(User.class,sql);
    }
    public List<User> searchUserByName(String name) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from users where username like ?";
        return curd.Query(User.class,sql,"%"+name+"%");
    }
}
