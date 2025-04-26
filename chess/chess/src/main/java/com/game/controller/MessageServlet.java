package com.game.controller;


import com.alibaba.fastjson.JSON;
import com.game.model.Message;
import com.game.service.Impl.MessageServiceImpl;
import com.game.service.MessageService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/message/*")
public class MessageServlet extends BaseServlet{
    private static MessageService messageService = new MessageServiceImpl();

    //发送消息
    public void sendMessages(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        // 读取请求体中的JSON数据
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String json = sb.toString();
        Message message = JSON.parseObject(json, Message.class);

        // 设置消息类型（根据原有代码逻辑）
        message.setType("FRIEND_CHAT");

        int i = messageService.sendMessages(message);
        String response = i>0 ? "{\"success\":true}" : "{\"success\":false,\"message\":\"发送消息失败\"}";
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(response);
    }

    //获取消息
    public void getMessages(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int senderId = Integer.parseInt(req.getParameter("senderId"));
        int receiverId = Integer.parseInt(req.getParameter("receiverId"));

        List<Message> messages = messageService.getMessages(senderId, receiverId);
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(JSON.toJSONString(messages));
    }
}
