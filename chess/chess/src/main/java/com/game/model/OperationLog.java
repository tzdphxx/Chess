package com.game.model;

import java.sql.Timestamp;

public class OperationLog {
    private int id;
    private String username;
    private String operation;
    private String detail;
    private String ip;
    private Timestamp createTime;


    public OperationLog() {
    }

    public OperationLog(int id, String username, String operation, String detail, String ip, Timestamp createTime) {
        this.id = id;
        this.username = username;
        this.operation = operation;
        this.detail = detail;
        this.ip = ip;
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取
     * @return operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * 设置
     * @param operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * 获取
     * @return detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置
     * @param detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * 获取
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取
     * @return createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String toString() {
        return "OperationLog{id = " + id + ", username = " + username + ", operation = " + operation + ", detail = " + detail + ", ip = " + ip + ", createTime = " + createTime + "}";
    }
}
