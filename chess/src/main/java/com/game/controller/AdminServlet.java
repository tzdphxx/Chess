package com.game.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.game.webSocket.HallChatEndpoint;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/admin/*")
public class AdminServlet extends BaseServlet{

    // 管理员发送消息接口
    public void sendAdminMsg(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        JSONObject obj = JSON.parseObject(sb.toString());
        String type = obj.getString("type");
        String content = obj.getString("content");
        String targetUserId = obj.getString("targetUserId");

        if ("ADMIN_BROADCAST".equals(type)) {
            HallChatEndpoint.sendAdminBroadcast(content);
            resp.getWriter().write("{\"success\":true,\"msg\":\"广播已发送\"}");
        } else if ("ADMIN_SINGLE".equals(type) && targetUserId != null) {
            HallChatEndpoint.sendAdminSingle(targetUserId, content);
            resp.getWriter().write("{\"success\":true,\"msg\":\"单发消息已发送\"}");
        } else {
            resp.getWriter().write("{\"success\":false,\"msg\":\"参数错误\"}");
        }
    }
}
