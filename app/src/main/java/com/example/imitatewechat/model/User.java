package com.example.imitatewechat.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class User implements Parcelable{
    private int uid; // 用户的id
    private String name; // 用户的名字
    private String password; // 用户的密码
    private String phone; // 用户的手机号
    private int age; // 用户的年龄
    private String pic; // 用户的头像
    private ArrayList<ChatFriend> chats; // 用户的聊天对象，值为ChatFriend对象
    private HashMap<Integer, Message> messages; // 用户的聊天记录，键为聊天对象的cid，值为Message对象

    public User(int uid, String name, String password, String phone, int age, String pic) {
        this.uid = uid;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.age = age;
        this.pic = pic;
        this.chats = new ArrayList<>();
        this.messages = new HashMap<>();
    }
    public User(int uid, String name, String phone, int age, String pic) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.pic = pic;
        this.chats = new ArrayList<>();
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

    public void setChats(ArrayList<ChatFriend> chats) {
        this.chats = chats;
    }

    public ArrayList<ChatFriend> getChats() {
        return chats;
    }

    public HashMap<Integer, Message> getMessages() {
        return messages;
    }

    // 从Parcel中读取数据，并赋值给对象的各个字段
    protected User(Parcel in) {
        uid = in.readInt();
        name = in.readString();
        password = in.readString();
        phone = in.readString();
        age = in.readInt();
        pic = in.readString();
        chats = in.readArrayList(ChatFriend.class.getClassLoader());
        messages = in.readHashMap(Message.class.getClassLoader());
    }

    // 将对象的各个字段写入到Parcel中
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(phone);
        dest.writeInt(age);
        dest.writeString(pic);
        dest.writeList(chats);
        dest.writeMap(messages);
    }

    // 返回一个标志位，一般返回0即可
    @Override
    public int describeContents() {
        return 0;
    }

    // 生成一个静态常量Creator，用于创建User对象的数组或列表等容器
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
