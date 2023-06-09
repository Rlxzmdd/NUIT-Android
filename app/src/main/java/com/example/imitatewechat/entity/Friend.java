package com.example.imitatewechat.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.imitatewechat.util.ChatType;

import java.util.Date;

public class Friend extends ChatFriend implements Parcelable {
    private final String phone; // 好友的手机号
    private final int age; // 好友的年龄
    private final int uid;
    public Friend(int cid,int uid, String name, String phone, int age, String pic,String msg, Date time_send) {
        super(ChatType.User,cid,name,pic,msg,time_send);
        this.uid = uid;
        this.phone = phone;
        this.age = age;
    }


    public String getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }

    @Override
    public int getObjectId() {
        return this.uid;
    }
    // 实现Parcelable接口需要重写以下方法

    // 从Parcel中读取数据，并赋值给对象的各个字段
    protected Friend(Parcel in) {
        super(in); // 调用父类的构造方法，读取父类的字段
        phone = in.readString();
        age = in.readInt();
        uid = in.readInt();
    }

    // 将对象的各个字段写入到Parcel中
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags); // 调用父类的方法，写入父类的字段
        dest.writeString(phone);
        dest.writeInt(age);
        dest.writeInt(uid);
    }

    // 返回一个标志位，一般返回0即可
    @Override
    public int describeContents() {
        return 0;
    }

    // 生成一个静态常量Creator，用于创建Friend对象的数组或列表等容器
    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

}