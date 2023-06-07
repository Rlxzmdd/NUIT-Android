package com.example.imitatewechat.util;

public enum ChatType {
    User, // 用户类型
    Group; // 群组类型

    @Override
    public String toString() {
        return this.name();
    }
}
