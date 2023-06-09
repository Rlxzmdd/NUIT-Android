package com.example.imitatewechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.imitatewechat.util.ChatType;

import java.util.Date;

public abstract class ChatFriend implements Parcelable {
    private final Enum<ChatType> mType;
    private final int mId;
    private final String mName;
    private String mPic;
    private String mMsg;
    private Date mDate;
    public ChatFriend(Enum<ChatType> type, int id, String name, String pic, String msg, Date time_send){
        mType = type;
        mId = id;
        mName = name;
        mPic = pic;
        mMsg = msg;
        mDate = time_send;
    }

    public Enum<ChatType> getType() {
        return mType;
    }
    /*
    获取对象ID
    比如Group的gid，Friend的fid
     */
    public abstract int getObjectId();
    /*
    本地的聊天ID
    每次实例化根据Message的先后关系自增长
     */
    public int getChatId() {
        return mId;
    }
    public String getName() {
        return mName;
    }

    public String getPic() {
        return mPic;
    }

    public void setPic(String mPic) {
        this.mPic = mPic;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    // 实现Parcelable接口需要重写以下方法

    // 从Parcel中读取数据，并赋值给对象的各个字段
    protected ChatFriend(Parcel in) {
        // 由于Enum类不实现Parcelable接口，无法直接写入或读取Parcel
        // 因此，这里使用ordinal方法获取枚举类型的序号，然后使用values方法根据序号获取枚举类型
        int ordinal = in.readInt();
        mType = ChatType.values()[ordinal];
        mId = in.readInt();
        mName = in.readString();
        mPic = in.readString();
        mMsg = in.readString();
    }

    // 将对象的各个字段写入到Parcel中
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 由于Enum类不实现Parcelable接口，无法直接写入或读取Parcel
        // 因此，这里使用ordinal方法获取枚举类型的序号，然后写入到Parcel中
        dest.writeInt(mType.ordinal());
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mPic);
        dest.writeString(mMsg);
    }

    // 返回一个标志位，一般返回0即可
    @Override
    public int describeContents() {
        return 0;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{" +
                String.format("type=%s, index=%d, name=%s, pic=%s, msg=%s, date=%s", mType, mId, mName, mPic, mMsg, mDate) +
                "}";
    }
}
