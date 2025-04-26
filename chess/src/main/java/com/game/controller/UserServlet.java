package com.game.controller;


import com.alibaba.fastjson.JSON;
import com.game.model.User;
import com.game.service.UserService;
import com.game.service.Impl.UserServiceImpl;
import com.game.util.CaptchaUtils;
import com.game.util.OperationLogger;
import com.game.util.PasswordUtil;
import com.mysql.cj.protocol.x.XMessage;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet{

    private static final UserService userService = new UserServiceImpl();

    Random random = new Random();


    //登录
    public void login (HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");

        String message = null;

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()){

            message = "empty";
            extracted(resp, message);
            return;
        }

        User user = userService.selectByName(username);

        if (user == null){
            message = "null";
            extracted(resp, message);
            // 记录失败日志
            OperationLogger.log(username, "LOGIN_FAIL", "用户名不存在", req.getRemoteAddr());
            return;
        }
        if (!PasswordUtil.checkPassword(password, user.getPassword())){

            message = "null";
            extracted(resp, message);
            // 记录失败日志
            OperationLogger.log(username, "LOGIN_FAIL", "密码错误", req.getRemoteAddr());
            return;
        }

        userService.updateLastLogin(user.getUserId());
        userService.updateStatus(user.getUserId(), "ONLINE");

        message = "success";
        HttpSession session = req.getSession();
        session.setAttribute("user", user.getUsername());
        // 记录成功日志
        OperationLogger.log(username, "LOGIN_SUCCESS", "登录成功", req.getRemoteAddr());


        if ("true".equals(remember)) {
            Cookie c_username = new Cookie("username", username);
            Cookie c_password = new Cookie("password", password);

            c_username.setPath("/");
            c_password.setPath("/");

            c_username.setMaxAge(60 * 60 * 24 * 7);
            c_password.setMaxAge(60 * 60 * 24 * 7);

            resp.addCookie(c_username);
            resp.addCookie(c_password);
        } else {
            Cookie c_name = new Cookie("username", "");
            Cookie c_pwd = new Cookie("password", "");

            c_name.setPath("/");
            c_pwd.setPath("/");

            c_name.setMaxAge(60 * 60 * 24 * 7);
            c_pwd.setMaxAge(60 * 60 * 24 * 7);

            resp.addCookie(c_name);
            resp.addCookie(c_pwd);
        }

        extracted(resp, message);
    }


    //注册
    public void register (HttpServletRequest req, HttpServletResponse resp) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, UnsupportedEncodingException {
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");



        String captcha = req.getParameter("captcha");

        HttpSession session = req.getSession();
        String captchaSession = (String) session.getAttribute("captcha");


        String message = null;

        if (captcha == null || !captcha.toLowerCase().equals(captchaSession)){
            message = "captcha";
            extracted(resp, message);
            return;
        }


        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty()){
            message = "empty";
            extracted(resp, message);
            return;
        }
        if (userService.selectByName(username) != null){
            message = "exist";
            extracted(resp, message);
            return;
        }
        if(userService.selectByEmail(email) != null){
            message = "email";
            extracted(resp, message);
            return;
        }
        if(userService.selectByPhone(phone) != null){
            message = "phone";
            extracted(resp, message);
            return;
        }

        String password_hash = PasswordUtil.encodedPassword(password);

        int i = userService.insertUser(username, password_hash, email, phone);

        if (i == 1){
            message = "success";
        }else {
            message = "error";
        }

        extracted(resp, message);
    }

    private static void extracted(HttpServletResponse resp, String message) {
        try {
            resp.setContentType("application/json;charset=utf-8");

            resp.getWriter().write(JSON.toJSONString(message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //获取信息
    public void getUserInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
        //不把密码给前端的页面
        user.setPassword(null);

        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(JSON.toJSONString(user));
    }

    //用户胜利
    public void userWin(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String userId = req.getParameter("userId");
        if (userId == null || userId.trim().isEmpty()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"userId不能为空");
            return;
        }
        int eloChange = random.nextInt(100) + 1;

        int j = userService.updateEloScore(Integer.parseInt(userId),eloChange);

        int i = userService.userWin(Integer.parseInt(userId));
        if (i> 0&& j>0){
            extracted(resp, "success");
        }else{
            extracted(resp, "error");
        }
    }

    //失败
    public void userLose(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String userId = req.getParameter("userId");
        if (userId == null || userId.trim().isEmpty()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"userId不能为空");
            return;
        }

        int eloChange = random.nextInt(50) + 1;
        eloChange = eloChange * -1;

        int j = userService.updateEloScore(Integer.parseInt(userId),eloChange);
        int i = userService.userLose(Integer.parseInt(userId));
        if (i> 0 && j>0){
            extracted(resp, "success");
        }else{
            extracted(resp, "error");
        }
    }

    //按分数获取用户列表
    public void getEloList(HttpServletRequest req, HttpServletResponse resp) throws SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {

       List<User> users = userService.selectByElo();

        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(JSON.toJSONString(users));

    }


    //获取验证码
    public void getCaptcha(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String captcha = CaptchaUtils.createCaptchaImage(resp);
        HttpSession session = req.getSession();
        session.setAttribute("captcha", captcha.toLowerCase());
    }
}
