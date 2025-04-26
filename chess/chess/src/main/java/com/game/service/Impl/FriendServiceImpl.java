package com.game.service.Impl;

import com.game.dao.FriendMapper;
import com.game.model.Friend;
import com.game.service.FriendService;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class FriendServiceImpl implements FriendService {
    private static FriendMapper friendMapper = new FriendMapper();

    @Override
    public int addRequest(Friend friend) throws SQLException {
        if (!"UNPROCESSED".equals(friend.getStatus()) || friend.getStatus() == null) {
            friend.setStatus("UNPROCESSED");
        }
        return friendMapper.addFriends(friend);
    }

    @Override
    public int rejectRequest(Friend friend) throws SQLException {
        if (!"UNPROCESSED".equals(friend.getStatus())) {
            return 0;
        }
        return friendMapper.deleteFriends(friend);
    }

    @Override
    public int deleteFriend(Friend friend) throws SQLException {
        if (!"PROCESSED".equals(friend.getStatus())) {
            return 0;
        }
        return friendMapper.deleteFriends(friend);
    }

    @Override
    public List<Friend> getFriends(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Friend> friends = friendMapper.selectFriendsById(userId);
        return friends.size()==0? null : friends;
    }

    @Override
    public int acceptRequest(Friend friend) throws SQLException {
        if (!"UNPROCESSED".equals(friend.getStatus())){
            friend.setStatus("PROCESSED");
        }
        return friendMapper.acceptFriends(friend);
    }

    @Override
    public List<Friend> getRequest(int userId) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Friend> friends =friendMapper.selectRequestById(userId);
        return friends.size()==0? null : friends;
    }
}
