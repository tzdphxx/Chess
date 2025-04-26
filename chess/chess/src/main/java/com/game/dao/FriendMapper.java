package com.game.dao;

import com.game.model.Friend;
import com.game.util.JDBC.curd;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class FriendMapper {

    public int addFriends(Friend friend) throws SQLException {
        String sql = "insert into friendships (user1_id, user2_id,status) values (?,?,?)";
        return curd.UpdateData(sql,friend.getUser1Id(),friend.getUser2Id(),"UNPROCESSED");
    }

    public int deleteFriends(Friend friend) throws SQLException {
        String sql = "delete from friendships where (user1_id=? and user2_id=?) or (user2_id=? and user1_id=?)";
        return curd.UpdateData(sql,friend.getUser1Id(),friend.getUser2Id(),friend.getUser1Id(),friend.getUser2Id());
    }

    public List<Friend> selectFriendsById(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from friendships where (user1_id=? or user2_id=?) and status = 'PROCESSED' ";
        return curd.Query(Friend.class,sql,userId,userId);
    }

    public int acceptFriends(Friend friend) throws SQLException {
        String sql = "update friendships set status = 'PROCESSED' where (user1_id=? and user2_id=?) or (user2_id=? and user1_id=?) ";
        return curd.UpdateData(sql,friend.getUser1Id(),friend.getUser2Id(),friend.getUser1Id(),friend.getUser2Id());

    }
    public List<Friend> selectRequestById(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String sql = "select * from friendships where user2_id=? and status = 'UNPROCESSED' ";
        return curd.Query(Friend.class,sql,userId,userId);
    }

}
