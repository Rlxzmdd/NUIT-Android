package com.example.imitatewechat.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.imitatewechat.exception.UserNotFoundException;
import com.example.imitatewechat.model.ChatFriend;
import com.example.imitatewechat.model.Friend;
import com.example.imitatewechat.model.Message;
import com.example.imitatewechat.model.User;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;

public class SQLiteDao {
    private final SQLiteHelper SqlHelper;

    public SQLiteDao(Context context) {
        // 初始化SQLiteHelper对象
        SqlHelper = new SQLiteHelper(context);
        // 创建或打开数据库
        SqlHelper.createDatabase();
    }

    public SQLiteHelper getSqlHelper() {
        return SqlHelper;
    }

    // 一个方法来根据用户id查询用户信息
    public User queryUserById(int uid) {
        User user = null;
        Cursor cursor = null;
        try {
            // 获取SQLiteDatabase对象
            SQLiteDatabase db = SqlHelper.getReadableDatabase();
            // 定义SQL语句
            String sql = "SELECT * FROM t_user WHERE uid = ?";
            // 执行查询操作，返回Cursor对象
            cursor = db.rawQuery(sql, new String[]{String.valueOf(uid)});
            if (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                String pic = cursor.getString(cursor.getColumnIndexOrThrow("pic"));
                user = new User(uid, name, password, phone, age, pic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭Cursor对象
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }

    public User queryUserByUserAndPass(String username,String pass){
        User user = null;
        Cursor cursor = null;
        try {
            // 获取SQLiteDatabase对象
            SQLiteDatabase db = SqlHelper.getReadableDatabase();
            // 定义SQL语句
            String sql = "SELECT * FROM t_user WHERE name = ? and password = ?";
            // 执行查询操作，返回Cursor对象
            cursor = db.rawQuery(sql, new String[]{username, pass});
            if (cursor.moveToNext()) {
                int uid = cursor.getInt(cursor.getColumnIndexOrThrow("uid"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                String pic = cursor.getString(cursor.getColumnIndexOrThrow("pic"));
                user = new User(uid, name, password, phone, age, pic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭Cursor对象
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }
    public ArrayList<ChatFriend> queryChatListByUser(User user) {
        ArrayList<ChatFriend> chatItems = new ArrayList<>();
        Cursor cursor = null;
        try {
            // 获取SQLiteDatabase对象
            SQLiteDatabase db = SqlHelper.getReadableDatabase();
            // 使用一条SQL语句来查询聊天列表
            String sql = "select (select count(*) from t_message as m2 where m2.time_send <= m.time_send and m2.is_group = false and m2.sender_uid = m.sender_uid) as id,\n" +
                    "       m.sender_uid as uid, u.name, u.phone, u.age, u.pic, m.content, m.time_send\n" +
                    "from t_message as m left join t_user as u\n" +
                    "on m.is_group = false and u.uid = m.sender_uid\n" +
                    "where m.receiver_id = ? and is_group = false;";
            // 执行查询操作，返回Cursor对象
            cursor = db.rawQuery(sql, new String[]{String.valueOf(user.getUid())});
            while (cursor.moveToNext()) {
                int index = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int uid = cursor.getInt(cursor.getColumnIndexOrThrow("uid"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                String pic = cursor.getString(cursor.getColumnIndexOrThrow("pic")); // 获取头像
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")); // 获取电话
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content")); // 获取年龄
                Date time_send = new Date(cursor.getLong(cursor.getColumnIndexOrThrow("time_send")));
                chatItems.add(new Friend(index,uid, name,phone,age,pic,content));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭Cursor对象
            if (cursor != null) {
                cursor.close();
            }
        }
        return chatItems;
    }

    public Friend queryFriendByUserId(int user_id) {
        Cursor cursor = null;
        Friend friend = null;
        try {
            // 获取SQLiteDatabase对象
            SQLiteDatabase db = SqlHelper.getReadableDatabase();
            // 使用一条SQL语句来查询聊天列表
            String sql = "SELECT * FROM t_user WHERE uid = ?";
            // 执行查询操作，返回Cursor对象
            cursor = db.rawQuery(sql, new String[]{String.valueOf(user_id)});
            if (cursor.moveToNext()) {
                int uid = cursor.getInt(cursor.getColumnIndexOrThrow("uid"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                String pic = cursor.getString(cursor.getColumnIndexOrThrow("pic"));
                friend = new Friend(-1,uid, name, phone, age, pic,"");
            }
            if(friend==null){
                throw  new UserNotFoundException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭Cursor对象
            if (cursor != null) {
                cursor.close();
            }
        }
        return friend;
    }

    // 一个方法来根据用户id和聊天对象id查询消息列表
    public ArrayList<Message> queryMessagesByChaTo(User user, int chat_id) {
        ArrayList<Message> messages = new ArrayList<>();
        Cursor cursor = null;
        try {
            // 获取SQLiteDatabase对象
            SQLiteDatabase db = SqlHelper.getReadableDatabase();
            // 定义SQL语句
            String sql = "SELECT * FROM t_message WHERE (sender_uid = ? AND receiver_id = ? AND is_group = false) OR (sender_uid = ? AND receiver_id = ? AND is_group = false) ORDER BY time_send ASC";
            // 执行查询操作，返回Cursor对象
            cursor = db.rawQuery(sql, new String[]{String.valueOf(user.getUid()), String.valueOf(chat_id), String.valueOf(chat_id), String.valueOf(user.getUid())});
            while (cursor.moveToNext()) {
                int mid = cursor.getInt(cursor.getColumnIndexOrThrow("mid"));
                int sender_uid = cursor.getInt(cursor.getColumnIndexOrThrow("sender_uid"));
                int receiver_id = cursor.getInt(cursor.getColumnIndexOrThrow("receiver_id"));
                boolean is_group = cursor.getInt(cursor.getColumnIndexOrThrow("is_group")) == 1;
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                Date time_send = new Date(cursor.getLong(cursor.getColumnIndexOrThrow("time_send")));
                //Friend friend = queryFriendByUserId(sender_uid);
                Message msg = new Message(mid,content,sender_uid,is_group, receiver_id,false , time_send);
                messages.add(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭Cursor对象
            if (cursor != null) {
                cursor.close();
            }
        }
        return messages;
    }

    public void insertMessage(Message message) {
        // todo 目前无法插入信息
        Cursor cursor = null;
        try {
            // 获取SQLiteDatabase对象
            SQLiteDatabase db = SqlHelper.getReadableDatabase();
            PreparedStatement ps = null;
            // 使用一条SQL语句来查询聊天列表
            String sql = "INSERT INTO t_message (content, sender_uid, is_group, receiver_id, time_send) VALUES (?, ?, ?, ?, ?)";
            cursor = db.rawQuery(sql, new String[]{String.valueOf(message.getContent()), String.valueOf(message.getSenderUid()), String.valueOf(message.isGroup()), String.valueOf(message.getReceiverId()), String.valueOf(message.getTime().getTime())});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭Cursor对象
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // 一个方法来关闭数据库资源
    public void closeDatabase() {
        SqlHelper.closeDatabase();
    }
}
