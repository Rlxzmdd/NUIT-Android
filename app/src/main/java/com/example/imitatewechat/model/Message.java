package com.example.imitatewechat.model;

import java.util.Date;

public class Message {
    private String content; // 消息内容
    private final User sender; // 消息发送者
    private final User receiver; // 消息接收者
    private final Date time; // 消息时间

    public Message(String content, User sender, User receiver, Date time) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public User getSender() {
        return sender;
    }
    public User getReceiver() {
        return receiver;
    }
    public Date getTime() {
        return time;
    }

}