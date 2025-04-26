package com.game.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private int eloScore;
    private int totalWins;
    private int totalLosses;
    private Timestamp createTime;
    private Timestamp lastLogin;
    private String status;


    public User(int userId){
        this.userId = userId;
    }

    public User() {
    }

    public User(int userId, String username, String password, String email, String phone, int eloScore, int totalWins, int totalLosses, Timestamp createTime, Timestamp lastLogin, String status) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.eloScore = eloScore;
        this.totalWins = totalWins;
        this.totalLosses = totalLosses;
        this.createTime = createTime;
        this.lastLogin = lastLogin;
        this.status = status;
    }

    /**
     * 获取
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
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
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取
     * @return eloScore
     */
    public int getEloScore() {
        return eloScore;
    }

    /**
     * 设置
     * @param eloScore
     */
    public void setEloScore(int eloScore) {
        this.eloScore = eloScore;
    }

    /**
     * 获取
     * @return totalWins
     */
    public int getTotalWins() {
        return totalWins;
    }

    /**
     * 设置
     * @param totalWins
     */
    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    /**
     * 获取
     * @return totalLosses
     */
    public int getTotalLosses() {
        return totalLosses;
    }

    /**
     * 设置
     * @param totalLosses
     */
    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
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

    /**
     * 获取
     * @return lastLogin
     */
    public Timestamp getLastLogin() {
        return lastLogin;
    }

    /**
     * 设置
     * @param lastLogin
     */
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * 获取
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return "User{userId = " + userId + ", username = " + username + ", password = " + password + ", email = " + email + ", phone = " + phone + ", eloScore = " + eloScore + ", totalWins = " + totalWins + ", totalLosses = " + totalLosses + ", createTime = " + createTime + ", lastLogin = " + lastLogin + ", status = " + status + "}";
    }
}
