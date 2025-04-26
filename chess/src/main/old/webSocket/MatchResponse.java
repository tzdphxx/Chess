package com.game.old.webSocket;

import com.alibaba.fastjson.annotation.JSONField;

public class MatchResponse {
    @JSONField(name = "ok")
    private boolean ok; //是否响应成功
    @JSONField(name = "reason")
    private String reason; //响应失败原因
    @JSONField(name = "message")
    private String message; //响应信息

    public MatchResponse() {
    }

    public MatchResponse(boolean ok, String reason, String message) {
        this.ok = ok;
        this.reason = reason;
        this.message = message;
    }

    /**
     * 获取
     * @return ok
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * 设置
     * @param ok
     */
    public void setOk(boolean ok) {
        this.ok = ok;
    }

    /**
     * 获取
     * @return reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "MatchResponse{ok = " + ok + ", reason = " + reason + ", message = " + message + "}";
    }
}
