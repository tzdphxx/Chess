package com.game.model;

import java.sql.Timestamp;

public class Message {

    private int messageId;
    private int senderId;
    private int receiverId;
    private String content;
    private Timestamp sendTime;
    private String type;


    public Message() {
    }

    public Message(int messageId, int senderId, int receiverId, String content, Timestamp sendTime, String type) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sendTime = sendTime;
        this.type = type;
    }

    /**
     * 获取
     * @return messageId
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * 设置
     * @param messageId
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    /**
     * 获取
     * @return senderId
     */
    public int getSenderId() {
        return senderId;
    }

    /**
     * 设置
     * @param senderId
     */
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    /**
     * 获取
     * @return receiverId
     */
    public int getReceiverId() {
        return receiverId;
    }

    /**
     * 设置
     * @param receiverId
     */
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * 获取
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取
     * @return sendTime
     */
    public Timestamp getSendTime() {
        return sendTime;
    }

    /**
     * 设置
     * @param sendTime
     */
    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 获取
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * 设置
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "Message{messageId = " + messageId + ", senderId = " + senderId + ", receiverId = " + receiverId + ", content = " + content + ", sendTime = " + sendTime + ", type = " + type + "}";
    }
}
