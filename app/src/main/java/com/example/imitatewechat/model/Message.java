package com.example.imitatewechat.model;

import java.util.Date;

public class Message {
    private int mid; // 消息的id
    private String content; // 消息的内容
    private User sender; // 消息的发送者
    private boolean isGroup; // 消息的接收者是否是群组
    private int receiverId; // 消息的接收者的id，可能是用户或群组
    private boolean isWithdraw; // 消息是否撤回
    private Date time; // 消息的发送时间

    public Message(int mid, String content, User sender, boolean isGroup, int receiverId, boolean isWithdraw, Date time) {
        this.mid = mid;
        this.content = content;
        this.sender = sender;
        this.isGroup = isGroup;
        this.receiverId = receiverId;
        this.isWithdraw = isWithdraw;
        this.time = time;
    }

    public int getMid() {
        return mid;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public int getReceiverId() {
        return receiverId;
    }
    public boolean isWithdraw(){
        return isWithdraw;
    }

    public Date getTime() {
        return time;
    }
}
