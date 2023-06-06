package com.example.imitatewechat.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements Parcelable {
    private int uid; // 好友的id
    private String name; // 好友的名字
    private String phone; // 好友的手机号
    private int age; // 好友的年龄
    private String pic; // 好友的头像
    private String msg; // 好友的最近一条消息

    public Friend(int uid, String name, String phone, int age, String pic,String msg) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.pic = pic;
        this.msg = msg;
    }

//
//    public Friend(int uid, String name, String pic, String msg) {
//        this.uid = uid;
//        this.name = name;
//        this.pic = pic;
//        this.msg = msg;
//    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }

    public String getMsg() {
        return msg;
    }


    // 实现Parcelable接口的方法
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 把对象的属性写入Parcel中
        dest.writeInt(uid);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeInt(age);
        dest.writeString(pic);
        dest.writeString(msg);
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            // 从Parcel中读取对象的属性，返回一个Friend对象
            return new Friend(in.readInt(), in.readString(),in.readString(), in.readInt(), in.readString(), in.readString());
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
}
