package com.example.imitatewechat.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class User{
    private int uid; // 用户的id
    private String name; // 用户的名字
    private String password; // 用户的密码
    private String phone; // 用户的手机号
    private int age; // 用户的年龄
    private String pic; // 用户的头像
    private HashMap<Integer, Friend> friends; // 用户的好友信息，键为好友的uid，值为Friend对象
    private HashMap<Integer, Group> groups; // 用户的群组信息，键为群组的gid，值为Group对象
    private HashMap<Integer, Message> messages; // 用户的聊天记录，键为消息的mid，值为Message对象

    public User(int uid, String name, String password, String phone, int age, String pic) {
        this.uid = uid;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.age = age;
        this.pic = pic;
        this.friends = new HashMap<>();
        this.groups = new HashMap<>();
        this.messages = new HashMap<>();
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }

    public HashMap<Integer, Friend> getFriends() {
        return friends;
    }

    public HashMap<Integer, Group> getGroups() {
        return groups;
    }

    public HashMap<Integer, Message> getMessages() {
        return messages;
    }
}