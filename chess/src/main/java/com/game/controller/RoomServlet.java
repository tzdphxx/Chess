package com.game.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.game.model.Room;
import com.game.service.Impl.RoomServiceImpl;
import com.game.service.RoomService;
import com.game.util.IdGenerator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

@WebServlet("/gameRoom/*")
public class RoomServlet extends BaseServlet {

    private static RoomService roomService = new RoomServiceImpl();


    //创建房间
    public void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        try {
            // 读取请求体
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = req.getReader().readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = JSON.parseObject(sb.toString());

            // 获取参数
            String roomName = jsonObject.getString("roomName");
            boolean isPublic = jsonObject.getBooleanValue("isPublic");
            String password = jsonObject.getString("password");
            String userId = jsonObject.getString("userId");

            // 参数验证
            if (roomName == null || userId == null) {
                resp.getWriter().write("{\"success\":false,\"message\":\"缺少必要参数\"}");
                return;
            }

            int publicRoom = 1;
            if (!isPublic) {
                publicRoom = 0;
            }

            // 生成房间ID并创建房间
            String roomId = IdGenerator.generateAlphaNumericId(6);
            Room room = new Room(roomId, roomName, publicRoom, password, userId);

            int result = roomService.addRoom(room);

            if (result > 0) {
                resp.getWriter().write("{\"success\":true,\"roomId\":\"" + roomId + "\"}");
            } else {
                resp.getWriter().write("{\"success\":false,\"message\":\"创建房间失败\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
        }
    }


    //加入房间
    public void joinRoom(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String roomId = req.getParameter("roomId");
        String password = req.getParameter("password");
        String userId = req.getParameter("userId");

        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            resp.getWriter().write("{\"success\":false,\"message\":\"房间不存在\"}");
            return;
        }
        if (!room.isIsPublic() && !room.getPassword().equals(password)) {
            resp.getWriter().write("{\"success\":false,\"message\":\"密码错误或房间不公开\"}");
            return;
        }

        room.addPlayer(Integer.parseInt(userId));
        resp.getWriter().write("{\"success\":true}");
    }
}
