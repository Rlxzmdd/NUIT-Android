package com.example.imitatewechat.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class Group {
    private int gid; // 群组的id
    private String name; // 群组的名字
    private int creatorUid; // 群组的创建者的id
    private String userNickname; // 用户在群组中的别名
    private Drawable drawable;

    public Group(int gid, String name, int creatorUid, String userNickname) {
        this.gid = gid;
        this.name = name;
        this.creatorUid = creatorUid;
        this.userNickname = userNickname;
    }

    public Group(int gid, String name, Drawable drawable) {
        this.gid = gid;
        this.name = name;
        this.drawable = drawable;
    }

    public Drawable getDrawable(Context context) {
        if (drawable == null) {
            Resources resources = context.getResources();
            int resId = resources.getIdentifier(name, "drawable", context.getPackageName());
            drawable = resources.getDrawable(resId);
        }
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getGid() {
        return gid;
    }

    public String getName() {
        return name;
    }

    public int getCreatorUid() {
        return creatorUid;
    }

    public String getUserNickname() {
        return userNickname;
    }
}
