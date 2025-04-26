package com.game.controller;


import com.alibaba.fastjson.JSON;
import com.game.model.GameRecord;
import com.game.service.GameRecordService;
import com.game.service.Impl.GameRecordServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import java.util.List;


@WebServlet("/gameRecord/*")
public class GameRecordServlet extends BaseServlet{

    private static GameRecordService gameRecordService = new GameRecordServiceImpl();


    //记录对局
    public void gameOver(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=UTF-8");


        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();


        GameRecord gameRecord = JSON.parseObject(json, GameRecord.class);

        int result = gameRecordService.saveGameRecord(
                gameRecord.getGameId(),
                gameRecord.getRoomId(),
                gameRecord.getBlackPlayerId(),
                gameRecord.getWhitePlayerId(),
                gameRecord.getWinnerId(),
                gameRecord.getFinalBoard(),
                gameRecord.getStartTime(),
                gameRecord.getEndTime(),
                gameRecord.getTotalMoves(),
                gameRecord.getMoveHistory()
        );
        if (result > 0) {
            resp.getWriter().write("{\"status\":\"success\"}");
        } else {
            resp.getWriter().write("{\"status\":\"failed\"}");
        }
    }


    //查看对局
    public void getGameRecordByUserId(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // 获取当前用户ID
        String userId = req.getParameter("userId");
        if (userId == null || userId.isEmpty()) {
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"缺少用户ID\"}");
            return;
        }

        // 获取用户的历史战绩
        List<GameRecord> records = gameRecordService.getGameRecordByUserId(Integer.parseInt(userId));


        String json = JSON.toJSONString(records);
        resp.getWriter().write(json);

    }


    //查看所有对局
    public void getAllRecords(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        List<GameRecord> records = gameRecordService.getAllRecords();

        String json = JSON.toJSONString(records);
        resp.getWriter().write(json);
    }

}
