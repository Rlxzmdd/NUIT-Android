package com.example.imitatewechat.db;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
@Deprecated

public class MySQLHelper implements Parcelable {
    // 定义数据库的连接参数 免费的线上数据库，建议自己注册一个
    private static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String URL = "jdbc:mysql://mysql.sqlpub.com:3306/db_wechat?characterEncoding=utf-8&serverTimezone=UTC&useSSL=false";
    private static String USER = "db_wechat";
    private static String PASSWORD = "86a5d4caa5cb275e";

    public MySQLHelper(){

    }
    // 加载驱动
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 获取数据库连接对象
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭数据库资源
    public void close(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 实现Parcelable接口需要重写以下方法

    // 从Parcel中读取数据，并赋值给对象的各个字段
    protected MySQLHelper(Parcel in) {
        // 由于Connection，ResultSet，Statement等类不实现Parcelable接口，无法直接写入或读取Parcel
        // 因此，这里只读取数据库的连接参数，如果需要使用数据库连接对象，可以调用getConnection方法重新获取
        DRIVER = in.readString();
        URL = in.readString();
        USER = in.readString();
        PASSWORD = in.readString();
    }

    // 将对象的各个字段写入到Parcel中
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 由于Connection，ResultSet，Statement等类不实现Parcelable接口，无法直接写入或读取Parcel
        // 因此，这里只写入数据库的连接参数，如果需要使用数据库连接对象，可以调用getConnection方法重新获取
        dest.writeString(DRIVER);
        dest.writeString(URL);
        dest.writeString(USER);
        dest.writeString(PASSWORD);
    }

    // 返回一个标志位，一般返回0即可
    @Override
    public int describeContents() {
        return 0;
    }

    // 生成一个静态常量Creator，用于创建MySQLHelper对象的数组或列表等容器
    public static final Creator<MySQLHelper> CREATOR = new Creator<MySQLHelper>() {
        @Override
        public MySQLHelper createFromParcel(Parcel in) {
            return new MySQLHelper(in);
        }

        @Override
        public MySQLHelper[] newArray(int size) {
            return new MySQLHelper[size];
        }
    };
}