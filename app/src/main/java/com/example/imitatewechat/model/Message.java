package com.example.imitatewechat.model;

import java.util.Date;

public class Message {
    private String content; // 消息内容
    private final Friend sender; // 消息发送者
    private final Friend receiver; // 消息接收者
    private final Date time; // 消息时间

    public Message(String content, Friend sender, Friend receiver, Date time) {
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
    public Friend getSender() {
        return sender;
    }
    public Friend getReceiver() {
        return receiver;
    }
    public Date getTime() {
        return time;
    }

}