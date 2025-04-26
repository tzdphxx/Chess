package com.game.old.webSocket;

import com.alibaba.fastjson.annotation.JSONField;

public class MatchRequest {
    @JSONField(name = "message")
    private String message;

    public MatchRequest() {
    }

    public MatchRequest(String message) {
        this.message = message;
    }

    /**
     * 获取
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "MatchRequest{message = " + message + "}";
    }
}

