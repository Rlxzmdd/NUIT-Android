package com.example.imitatewechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.imitatewechat.util.ChatType;

public class Group extends ChatFriend implements Parcelable {
    private int creatorUid; // 群组的创建者的id
    private String userNickname; // 用户在群组中的别名
    private int gid;

    public Group(int cid,int gid, String name,String pic,String msg, int creatorUid, String userNickname) {
        super(ChatType.Group,gid,name,pic,msg);
        this.gid = gid;
        this.creatorUid = creatorUid;
        this.userNickname = userNickname;
    }

    public int getCreatorUid() {
        return creatorUid;
    }

    public String getUserNickname() {
        return userNickname;
    }

    @Override
    public int getObjectId() {
        return this.gid;
    }

    // 实现Parcelable接口需要重写以下方法

    // 从Parcel中读取数据，并赋值给对象的各个字段
    protected Group(Parcel in) {
        super(in); // 调用父类的构造方法，读取父类的字段
        creatorUid = in.readInt();
        userNickname = in.readString();
        gid = in.readInt();
    }

    // 将对象的各个字段写入到Parcel中
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags); // 调用父类的方法，写入父类的字段
        dest.writeInt(creatorUid);
        dest.writeString(userNickname);
        dest.writeInt(gid);
    }

    // 返回一个标志位，一般返回0即可
    @Override
    public int describeContents() {
        return 0;
    }

    // 生成一个静态常量Creator，用于创建Group对象的数组或列表等容器
    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}