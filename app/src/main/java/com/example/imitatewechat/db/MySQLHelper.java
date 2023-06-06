package com.example.imitatewechat.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Tips：Helper类封装了数据库连接和关闭的方法
 */
public class MySQLHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/wechat"; // 数据库的URL
    private static final String USER = "root"; // 数据库的用户名
    private static final String PASSWORD = "12341948"; // 数据库的密码

    // 一个静态方法来获取数据库连接
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 加载驱动类
            Class.forName("com.mysql.jdbc.Driver");
            // 获取连接对象
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 一个静态方法来关闭数据库连接
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}