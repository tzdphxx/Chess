package com.game.controller;

import com.alibaba.fastjson.JSON;
import com.game.model.User;
import com.game.service.MatchService;
import com.game.service.Impl.MatchServiceImpl;
import com.game.service.UserService;
import com.game.service.Impl.UserServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/gameHall/*")
public class GameHallServlet extends BaseServlet{
    private static final UserService userService = new UserServiceImpl();
    private static final MatchService matchService = MatchServiceImpl.getInstance();


    public void getUserInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        HttpSession session = req.getSession(false);
        if (session==null || session.getAttribute("user")==null){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,"未登录");
            return;
        }

        String username = (String) session.getAttribute("user");

        User user = userService.selectByName(username);
        if (user == null){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND,"用户不存在");
            return;
        }

        Map<String,Object> response = new HashMap<>();
        response.put("userId",user.getUserId());
        response.put("username",user.getUsername());
        response.put("eloScore",user.getEloScore());
        response.put("totalWins",user.getTotalWins());
        response.put("totalLosses",user.getTotalLosses());
        response.put("isMatch",matchService.isMatching(user));

        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(JSON.toJSONString(response));

    }

    public void startMatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        HttpSession session = req.getSession(false);
        if (session==null || session.getAttribute("user")==null){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,"未登录");
            return;
        }
        String username = (String) session.getAttribute("user");
        User user = userService.selectByName(username);
        if (user == null){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND,"用户不存在");
            return;
        }

        matchService.startMatch(user);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "已开始匹配");


        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(JSON.toJSONString(response));
    }
    public void cancelMatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        HttpSession session = req.getSession(false);

        if (session==null || session.getAttribute("user")==null){
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED,"未登录");
            return;
        }
        String username = (String) session.getAttribute("user");

        User user = userService.selectByName(username);
        if (user == null){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND,"用户不存在");
            return;
        }


        //取消匹配

        boolean cancelled = matchService.cancelMatch(user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", cancelled);
        response.put("message", cancelled ? "已取消匹配" : "取消匹配失败或不在匹配中");

        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(JSON.toJSONString(response));
    }
}
