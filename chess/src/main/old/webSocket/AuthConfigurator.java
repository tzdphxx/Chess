package com.game.old.webSocket;

import com.game.model.User;
import com.game.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;


import java.util.logging.Logger;

public class AuthConfigurator extends ServerEndpointConfig.Configurator {
    private static final Logger logger = Logger.getLogger(AuthConfigurator.class.getName());
    private static final UserService userService = new UserService();

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        try {
            HttpSession httpSession = (HttpSession) request.getHttpSession();
            if (httpSession != null) {
                String username = (String) httpSession.getAttribute("user");
                if (username != null && !username.isEmpty()) {
                    User user = userService.selectByName(username);
                    if (user != null) {
                        sec.getUserProperties().put("user", user);
                        logger.info("从HTTP会话获取用户成功: " + username);
                        return;
                    }
                }
            }

            String token = null;
            if (request.getParameterMap().containsKey("token")) {
                token = request.getParameterMap().get("token").get(0);
            }
            if (token == null || token.isEmpty()) {
                logger.warning("WebSocket连接尝试未提供token且会话中无用户信息");
                return;
            }

            User user = validateToken(token);
            if (user != null) {
                sec.getUserProperties().put("user", user);
                logger.info("从token参数获取用户成功: " + token);
            } else {
                logger.warning("无效的token: " + token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User validateToken(String token) {
        try {

            String username = token;

            User user = userService.selectByName(username);
            if (user != null) {
                logger.info("用户 " + username + " 验证成功");
                return user;
            } else {
                logger.warning("用户 " + username + " 不存在");
                return null;
            }
        } catch (Exception e) {
            logger.severe("Token验证失败: " + e.getMessage());
            return null;
        }
    }
}
