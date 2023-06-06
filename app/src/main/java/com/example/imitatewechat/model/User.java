package com.example.imitatewechat.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    // 其他属性和方法

    private String name; // 好友的名字
    private int pic; // 好友的头像
    private int id;
    private String msg; // 好友的最近一条消息
    private Drawable drawable;
    private boolean flag = false;
    public User(int id, String name, int pic, String msg) {
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.msg = msg;
    }
    public User(int id, String name, int pic) {
        this.id = id;
        this.name = name;
        this.pic = pic;
    }
    public User(int id, String name, Drawable pic) {
        this.id = id;
        this.name = name;
        this.drawable = pic;
        flag = true;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Drawable getPic(Context context) {
        if(flag){
            return drawable;
        }else{
            return context.getDrawable(this.pic);
            // todo 修复聊天对象图片显示
        }
    }

    public String getMsg() {
        return msg;
    }

    public int getId() {
        return id;
    }


    // 实现Parcelable接口的方法
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 把对象的属性写入Parcel中
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(pic);
        dest.writeString(msg);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            // 从Parcel中读取对象的属性，返回一个Friend对象
            return new User(in.readInt(), in.readString(), in.readInt(), in.readString());
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
