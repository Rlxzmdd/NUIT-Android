package com.example.imitatewechat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.imitatewechat.exception.UserNotFoundException;
import com.example.imitatewechat.entity.ChatFriend;
import com.example.imitatewechat.entity.Friend;
import com.example.imitatewechat.entity.Message;
import com.example.imitatewechat.entity.User;

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
        if(user == null){
            throw new UserNotFoundException();
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
    public ArrayList<Friend> queryAllFriendsByUser(User user){
        ArrayList<Friend> friends = new ArrayList<>();
        Cursor cursor = null;
        try {
            // 获取SQLiteDatabase对象
            SQLiteDatabase db = SqlHelper.getReadableDatabase();
            // 定义SQL语句
            String sql = "select * from t_user where uid in (SELECT friend_uid FROM t_friend WHERE user_uid = ?)";
            // 执行查询操作，返回Cursor对象
            cursor = db.rawQuery(sql, new String[]{String.valueOf(user.getUid())});
            int index = 0;
            while (cursor.moveToNext()) {
                int uid = cursor.getInt(cursor.getColumnIndexOrThrow("uid"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String pic = cursor.getString(cursor.getColumnIndexOrThrow("pic")); // 获取头像
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                friends.add(new Friend(index++,uid, name, phone, age, pic, "",new Date()));
                Log.d("friend-list",name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭Cursor对象
            if (cursor != null) {
                cursor.close();
            }
        }
        return friends;
    }
    public ArrayList<ChatFriend> queryChatListByUser(User user) {
        ArrayList<ChatFriend> chatItems = new ArrayList<>();
        Cursor cursor = null;
        int index = 0;
        try {
            // 获取SQLiteDatabase对象
            SQLiteDatabase db = SqlHelper.getReadableDatabase();
            // 使用一条SQL语句来查询聊天列表
            String sql = "select m.sender_uid as uid, u.name, u.phone, u.age, u.pic, m.content, m.time_send \n" +
                    "from t_message as m \n" +
                    "left join t_user as u on m.is_group = false and u.uid = m.sender_uid \n" +
                    "where m.receiver_id = ? and is_group = false\n" +
                    "group by m.sender_uid\n" +
                    "having m.time_send = max(m.time_send)";
            /*
-- 查询作为发送者的最新信息
select m.sender_uid as uid, u.name, u.phone, u.age, u.pic, m.content, m.time_send
from t_message as m
left join t_user as u on m.is_group = false and u.uid = m.sender_uid
where m.sender_uid = ? and is_group = false
group by m.receiver_id
having m.time_send = max(m.time_send)
-- 使用union子句来合并另一个查询
union
-- 查询作为接收者的最新信息
select m.sender_uid as uid, u.name, u.phone, u.age, u.pic, m.content, m.time_send
from t_message as m
left join t_user as u on m.is_group = false and u.uid = m.sender_uid
where m.receiver_id = ? and is_group = false
group by m.sender_uid
having m.time_send = max(m.time_send)
             */
            // 执行查询操作，返回Cursor对象
            cursor = db.rawQuery(sql, new String[]{String.valueOf(user.getUid())});
            while (cursor.moveToNext()) {
                //int index = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                Log.d("index: ", String.valueOf(index));
                int uid = cursor.getInt(cursor.getColumnIndexOrThrow("uid"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
                String pic = cursor.getString(cursor.getColumnIndexOrThrow("pic")); // 获取头像
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone")); // 获取电话
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content")); // 获取年龄
                Date time_send = new Date(cursor.getLong(cursor.getColumnIndexOrThrow("time_send")));
                chatItems.add(new Friend(index,uid, name,phone,age,pic,content,time_send));
                index++;
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
                friend = new Friend(-1,uid, name, phone, age, pic,"",new Date());
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
            // todo 修复根据时间排序问题。目前排序有点奇怪。
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
        try {
            // 获取SQLiteDatabase对象
            SQLiteDatabase db = SqlHelper.getWritableDatabase();
            // 创建一个ContentValues对象，用于存储消息的属性和值
            ContentValues values = new ContentValues();
            values.put("content", message.getContent());
            values.put("sender_uid", message.getSenderUid());
            values.put("is_group", message.isGroup());
            values.put("receiver_id", message.getReceiverId());
            values.put("time_send", message.getTime().getTime());
            // 使用insert方法向t_message表中插入一条数据，返回插入的行号
            long rowId = db.insert("t_message", null, values);
            if (rowId != -1) {
                // 插入成功，打印日志
                Log.d("Database", "Insert message successfully, rowId = " + rowId);
            } else {
                // 插入失败，打印日志
                Log.e("Database", "Insert message failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 一个方法来关闭数据库资源
    public void closeDatabase() {
        SqlHelper.closeDatabase();
    }

    public boolean addFriendByUserId(User currentUser,int user_id) {
        try{
            User addto = queryUserById(user_id);
            SQLiteDatabase db = SqlHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("user_uid", currentUser.getUid());
            values.put("friend_uid", addto.getUid());
            values.put("time_add", (new Date()).toString());
            long rowId = db.insert("t_friend", null, values);
            if (rowId != -1) {
                values = new ContentValues();
                values.put("user_uid", addto.getUid());
                values.put("friend_uid", currentUser.getUid());
                values.put("time_add", (new Date()).toString());
                db.insert("t_friend", null, values);
                insertMessage(new Message(0,"我和你已经是好友了，快来聊天吧",addto.getUid(),false,currentUser.getUid(),false,new Date()));
                return true;
            } else {
                return false;
            }
        }catch (UserNotFoundException e){
            return false;
        }
    }
}
