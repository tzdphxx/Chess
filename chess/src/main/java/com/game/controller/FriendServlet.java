package com.game.controller;


import com.alibaba.fastjson.JSON;
import com.game.model.Friend;
import com.game.model.User;
import com.game.service.FriendService;
import com.game.service.Impl.FriendServiceImpl;
import com.game.service.Impl.UserServiceImpl;
import com.game.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/friend/*")
public class FriendServlet extends BaseServlet{

    private static FriendService friendService = new FriendServiceImpl();
    private static UserService userService = new UserServiceImpl();



    //申请好友
    public void requestShip(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String userId = req.getParameter("userId");
        String objectId = req.getParameter("objectId");



        if (userId == null || objectId == null || userId.isEmpty() || objectId.isEmpty()) {
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"success\":false,\"message\":\"用户ID或好友ID为空\"}");
            System.err.println("requestShip: 参数为空 - userId=" + userId + ", objectId=" + objectId);
            return;
        }

        Friend friend = new Friend();
        friend.setStatus("UNPROCESSED");
        friend.setUser1Id(Integer.parseInt(userId));
        friend.setUser2Id(Integer.parseInt(objectId));

        String message;
        if (friendService.addRequest(friend) > 0) {
            message = "{\"success\":true,\"message\":\"成功发送请求\"}";
        } else {
            message = "{\"success\":false,\"message\":\"发送请求失败\"}";
        }
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(message);

    }


    //接受请求
    public void accept(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {

        String friendId = req.getParameter("friendId");
        String userId = req.getParameter("userId");

        // 验证参数有效性
        if (userId == null || friendId == null || userId.isEmpty() || friendId.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\":false,\"message\":\"参数缺失\"}");
            return;
        }

        Friend friend = new Friend();
        friend.setStatus("PROCESSED");
        friend.setUser1Id(Integer.parseInt(userId));
        friend.setUser2Id(Integer.parseInt(friendId));

        String message;
        int result = friendService.acceptRequest(friend);
        if (result > 0) {
            message = "{\"success\":true,\"message\":\"成功添加好友\"}";
        } else {
            message = "{\"success\":false,\"message\":\"添加好友失败\"}";
        }

        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(message);
    }

    //拒绝好友
    public void reject(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String friendId = req.getParameter("friendId");
        String userId = req.getParameter("userId");

        Friend friend = new Friend();
        friend.setUser1Id(Integer.parseInt(userId));
        friend.setUser2Id(Integer.parseInt(friendId));


        String message ;
        if (friendService.rejectRequest(friend) > 0){
            message = "{\"success\":true,\"message\":\"拒绝添加好友\"}";
        }else {
            message = "{\"success\":false,\"message\":\"拒绝失败\"}";
        }
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(message);
    }

    //删除好友
    public void removed(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String friendId = req.getParameter("friendId");
        String userId = req.getParameter("userId");

        // 验证参数有效性
        if (userId == null || friendId == null || userId.isEmpty() || friendId.isEmpty()) {
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"success\":false,\"message\":\"参数缺失\"}");
            return;
        }

        Friend friend = new Friend();
        friend.setUser1Id(Integer.parseInt(userId));
        friend.setUser2Id(Integer.parseInt(friendId));
        friend.setStatus("PROCESSED"); // 设置状态为已处理，因为只能删除已经是好友的关系

        String message;
        if (friendService.deleteFriend(friend) > 0){
            message = "{\"success\":true,\"message\":\"成功删除好友\"}";
        }else {
            message = "{\"success\":false,\"message\":\"删除好友失败\"}";
        }
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(message);
    }


    //查看好友列表
    public void selectFriend(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        HttpSession session = req.getSession(false);

        if (session==null){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,"未登录");
            return;
        }
        String username = (String) session.getAttribute("user");
        if (username==null){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,"未登录");
            return;
        }

        User user = userService.selectByName(username);
        if (user == null){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,"用户不存在");
            return;
        }

        int userId = user.getUserId();
        List<Friend> friends = friendService.getFriends(userId);
        List<User> users = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();

        // 添加空值检查
        if (friends == null || friends.isEmpty()) {
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("[]");
            return;
        }

        for (Friend friend : friends) {
            if (friend != null) {  // 添加null检查
                int id1 = friend.getUser1Id();
                int id2 = friend.getUser2Id();
                if (id1 != userId && !userIds.contains(id1)) {
                    User myFriend = userService.selectById(friend.getUser1Id());
                    if (myFriend != null) {  // 添加null检查
                        users.add(myFriend);
                        userIds.add(id1);
                    }
                }
                if (id2 != userId && !userIds.contains(id2)) {
                    User myFriend = userService.selectById(friend.getUser2Id());
                    if (myFriend != null) {  // 添加null检查
                        users.add(myFriend);
                        userIds.add(id2);
                    }
                }
            }
        }

        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(JSON.toJSONString(users));
    }


    //搜索用户
    public void search(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String username = req.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("[]");
            return;
        }

        List<User> users = userService.searchByUsername(username.trim());
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(JSON.toJSONString(users));
    }


    //查看好友请求
    public void friendRequest(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        HttpSession session = req.getSession(false);

        if (session==null){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,"未登录");
            return;
        }
        String username = (String) session.getAttribute("user");
        if (username==null){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,"未登录");
            return;
        }

        User user = userService.selectByName(username);
        if (user == null){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,"用户不存在");
            return;
        }

        int userId = user.getUserId();
        List<Friend> friends = friendService.getRequest(userId);

        // 如果没有好友请求，直接返回空数组
        if (friends == null || friends.isEmpty()) {
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("[]");
            return;
        }

        List<User> users = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();


        for (Friend friend : friends) {
            if (friend != null) {  // 添加null检查
                int id1 = friend.getUser1Id();
                int id2 = friend.getUser2Id();
                if (id1 != userId && !userIds.contains(id1)) {
                    User myFriend = userService.selectById(friend.getUser1Id());
                    if (myFriend != null) {  // 添加null检查
                        users.add(myFriend);
                        userIds.add(id1);
                    }
                }
                if (id2 != userId && !userIds.contains(id2)) {
                    User myFriend = userService.selectById(friend.getUser2Id());
                    if (myFriend != null) {  // 添加null检查
                        users.add(myFriend);
                        userIds.add(id2);
                    }
                }
            }
        }

        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(JSON.toJSONString(users));
    }
}
