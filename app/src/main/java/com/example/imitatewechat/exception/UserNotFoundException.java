package com.example.imitatewechat.exception;

public class UserNotFoundException extends RuntimeException {
    // 构造方法，接收一个用户ID作为参数
    public UserNotFoundException() {
        // 调用父类的构造方法，传入异常信息
        super("当前没有保存用户信息");
    }
}
