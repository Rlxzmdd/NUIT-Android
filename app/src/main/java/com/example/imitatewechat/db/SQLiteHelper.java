package com.example.imitatewechat.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SQLiteHelper extends SQLiteOpenHelper {

    // 定义数据库的名称和版本
    private static final String DB_NAME = "db_wechat.sqlite";
    private static final int DB_VERSION = 1;

    // 定义数据库的路径
    private static String DB_PATH;

    // 定义上下文对象
    private Context context;

    // 定义数据库对象
    private SQLiteDatabase database;

    // 构造方法
    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        // 获取数据库的路径
        DB_PATH = context.getFilesDir().getAbsolutePath() + File.separator + DB_NAME;
    }

    // 创建数据库
    public void createDatabase() {
        // 判断数据库是否存在
        if (!isDatabaseExist()) {
            // 不存在则复制assets文件夹下的.db文件到指定路径
            copyDatabase();
        }
        // 打开数据库
        openDatabase();
    }

    // 判断数据库是否存在
    private boolean isDatabaseExist() {
        File file = new File(DB_PATH);
        return file.exists() && file.length() > 0;
    }

    // 复制数据库
    private void copyDatabase() {
        try {
            // 获取assets文件夹下的.db文件的输入流
            InputStream inputStream = context.getAssets().open(DB_NAME);
            // 创建输出流，指向目标路径
            FileOutputStream outputStream = new FileOutputStream(DB_PATH);
            // 定义缓冲区大小
            byte[] buffer = new byte[1024];
            // 定义读取长度
            int length;
            // 循环读取和写入
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            // 关闭输入输出流
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 打开数据库
    private void openDatabase() {
        database = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
    }

    // 关闭数据库
    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    // 执行SQL语句，用于创建表，插入数据，更新数据，删除数据等操作
    public void execSQL(String sql) {
        if (database != null) {
            database.execSQL(sql);
        }
    }

    // 查询数据，返回Cursor对象
    public Cursor query(String sql) {
        if (database != null) {
            return database.rawQuery(sql, null);
        }
        return null;
    }

    // 重写onCreate方法，在这里可以创建表，初始化数据等
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建t_user表
        db.execSQL("CREATE TABLE t_user (" +
                "uid INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "phone TEXT NOT NULL," +
                "age INTEGER NOT NULL," +
                "pic TEXT NOT NULL" +
                ");");
        // 创建t_friend表
        db.execSQL("CREATE TABLE t_friend (" +
                "fid INTEGER PRIMARY KEY," +
                "user_uid INTEGER NOT NULL," + // 对应t_user表的uid
                "friend_uid INTEGER NOT NULL," + // 对应t_user表的uid
                "time_add DATETIME NOT NULL" +
                ");");
        // 创建t_group表
        db.execSQL("CREATE TABLE t_group (" +
                "gid INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "creator_uid INTEGER NOT NULL," + // 对应t_user表的uid
                "time_create DATETIME NOT NULL" +
                ");");
        // 创建t_group_members表
        db.execSQL("CREATE TABLE t_group_members (" +
                "gmid INTEGER PRIMARY KEY," +
                "gid INTEGER NOT NULL," + // 对应t_group表的gid
                "user_uid INTEGER NOT NULL," + // 对应t_user表的uid
                "invite_uid INTEGER NOT NULL," + // 对应t_user表的uid
                "invite_type TEXT CHECK(invite_type IN ('qrcode', 'invite')) NOT NULL," +
                "user_nickname TEXT NOT NULL," +
                "time_join DATETIME NOT NULL" +
                ");");
        // 创建t_message表
        db.execSQL("CREATE TABLE t_message (" +
                "mid INTEGER PRIMARY KEY," +
                "sender_uid INTEGER NOT NULL," + // 对应t_user表的uid
                "content TEXT NOT NULL," +
                "is_group BOOLEAN NOT NULL," +
                "receiver_id INTEGER NOT NULL," + // 对应t_user或t_group表的uid或gid，根据is_group判断
                "is_withdraw BOOLEAN NOT NULL DEFAULT 0," +
                "time_send DATETIME NOT NULL" +
                ");");

        // 插入用户数据
        db.execSQL("INSERT INTO t_user (uid, name, password, phone, age, pic) VALUES (1, 'admin', '1234', '12345678901', 21, 'R.drawable.Li');");
        db.execSQL("INSERT INTO t_user (uid, name, password, phone, age, pic) VALUES (2, 'Alice', '1234', '12345678902', 20, 'R.drawable.alice');");
        db.execSQL("INSERT INTO t_user (uid, name, password, phone, age, pic) VALUES (3, 'Bob','1234','12345678903',24,'R.drawable.Bob');");
        db.execSQL("INSERT INTO t_user (uid, name, password, phone, age, pic) VALUES (4, 'Charlie', '1234', '12345678904', 21, 'R.drawable.charlie');");
        db.execSQL("INSERT INTO t_user (uid, name, password, phone, age, pic) VALUES (5, 'David', '1234', '12345678905', 23, 'R.drawable.david');");

// 插入好友数据
        db.execSQL("INSERT INTO t_friend (fid, user_uid ,friend_uid ,time_add) VALUES (1 ,1 ,2 ,'2021-12-20 10:00:00');");
        db.execSQL("INSERT INTO t_friend (fid ,user_uid ,friend_uid ,time_add) VALUES (2 ,1 ,3 ,'2021-12-20 10:10:00');");
        db.execSQL("INSERT INTO t_friend (fid ,user_uid ,friend_uid ,time_add) VALUES (3 ,2 ,3 ,'2021-12-20 10:20:00');");
        db.execSQL("INSERT INTO t_friend (fid ,user_uid ,friend_uid ,time_add) VALUES (4 ,2 ,4 ,'2021-12-20 10:30:00');");
        db.execSQL("INSERT INTO t_friend (fid ,user_uid ,friend_uid ,time_add) VALUES (5 ,3 ,5 ,'2021-12-20 10:40:00');");

// 插入群组数据
        db.execSQL("INSERT INTO t_group (gid ,name ,creator_uid ,time_create) VALUES (1 ,'2023讨论群' ,1 ,'2021-12-20 11:00:00');");
        db.execSQL("INSERT INTO t_group (gid ,name ,creator_uid ,time_create) VALUES (2 ,'503' ,2 ,'2021-12-20 11:10:00');");
// 插入群组成员数据
        db.execSQL("INSERT INTO t_group_members (gmid,gid,user_uid,invite_uid,invite_type,user_nickname,time_join) VALUES (1,1,1,0,'qrcode','admin','2021-12-20 11:00:00');");
        db.execSQL("INSERT INTO t_group_members (gmid,gid,user_uid,invite_uid,invite_type,user_nickname,time_join) VALUES (2,1,2,1,'invite','Alice','2021-12-20 11:05:00');");
        db.execSQL("INSERT INTO t_group_members (gmid,gid,user_uid,invite_uid,invite_type,user_nickname,time_join) VALUES (3,1,3,2,'invite','Bob','2021-12-20 11:06:00');");
        db.execSQL("INSERT INTO t_group_members (gmid,gid,user_uid,invite_uid,invite_type,user_nickname,time_join) VALUES (4,2,2,0,'qrcode','Alice','2021-12-20 11:10:00');");
        db.execSQL("INSERT INTO t_group_members (gmid,gid,user_uid,invite_uid,invite_type,user_nickname,time_join) VALUES (5,2,4,2,'invite','Charlie','2021-12-20 11:15:00');");

// 插入消息数据
        db.execSQL("INSERT INTO t_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (1,'Hi Alice',1,FALSE,2,FALSE,'2021-12-20 11:30:00');");
        db.execSQL("INSERT INTO t_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (2,'Hi admin',2,FALSE,1,FALSE,'2021-12-20 11:31:00');");
        db.execSQL("INSERT INTO t_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (3,'Hello Bob',1,FALSE,3,FALSE,'2021-12-20 11:32:00');");
        db.execSQL("INSERT INTO t_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (4,'Hello admin',3,FALSE,1,FALSE,'2021-12-20 11:33:00');");
        db.execSQL("INSERT INTO t_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (5,'Hey Alice',3,FALSE,2,FALSE,'2021-12-20 11:34:00');");
        db.execSQL("INSERT INTO t_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (6,'Hey Bob',2,FALSE,3,FALSE,'2021-12-20 11:35:00');");
        db.execSQL("INSERT INTO t_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (7,'Hello everyone',1,TRUE,1,FALSE,'2021-12-20 11:36:00');");
        db.execSQL("INSERT INTO t_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (8,'Hello admin',2,TRUE,1,FALSE,'2021-12-20 11:37:00');");
        db.execSQL("INSERT INTO t_message (mid,content,sender_uid,is_group,receiver_id,is_withdraw,time_send) VALUES (9,'Hi guys',4,FALSE,2,FALSE,'2021-12-20 11:38:00');");
    }


    // 重写onUpgrade方法，在这里可以更新表结构，删除表，添加表等
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 这里可以执行一些SQL语句，如更新表结构，删除表，添加表等
        // 例如：db.execSQL("DROP TABLE IF EXISTS questions");
    }
}
