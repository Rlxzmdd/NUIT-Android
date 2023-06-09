package com.example.imitatewechat.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.example.imitatewechat.model.ChatFriend;
import com.example.imitatewechat.model.Friend;
import com.example.imitatewechat.model.Group;
import com.example.imitatewechat.model.User;
import com.example.imitatewechat.model.Message;

/**
 * Tips：Dao类封装了一系列数据库操作，比如插入条目，搜索条目等等
 */
public class MySQLDao{
    private final MySQLHelper SqlHelper;

    public MySQLDao() {
        SqlHelper = new MySQLHelper();
    }

    public MySQLHelper getSqlHelper() {
        return SqlHelper;
    }

    // 一个方法来根据用户id查询用户信息
    public User queryUserById(int uid) {
        User user = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = SqlHelper.getConnection();
            String sql = "SELECT * FROM t_user WHERE uid = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, uid);
            rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                int age = rs.getInt("age");
                String pic = rs.getString("pic");
                user = new User(uid, name, password, phone, age, pic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlHelper.close(rs, ps, conn);
        }
        return user;
    }

    public User queryUserByUserAndPass(String username,String pass){
        User user = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = SqlHelper.getConnection();
            String sql = "SELECT * FROM t_user WHERE name = ? and password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            System.out.println(rs);
            if (rs.next()) {
                int uid = rs.getInt("uid");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String phone = rs.getString("phone");
                int age = rs.getInt("age");
                String pic = rs.getString("pic");
                user = new User(uid, name, password, phone, age, pic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlHelper.close(rs, ps, conn);
        }
        return user;
    }
    public ArrayList<ChatFriend> queryChatListByUser(User user) {
        ArrayList<ChatFriend> chatItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = SqlHelper.getConnection();
            // 使用一条SQL语句来查询聊天列表
            String sql = "select (row_number() over (order by m.time_send desc)) as id, \n" +
                    "       m.sender_uid as uid, u.name, u.phone, u.age, u.pic, m.content, m.time_send\n" +
                    "from t_message as m left join t_user as u\n" +
                    "on m.is_group = false and u.uid = m.sender_uid\n" +
                    "where m.receiver_id = ? and is_group = false;";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getUid()); // 设置用户id参数
            rs = ps.executeQuery();
            while (rs.next()) {
                int index = rs.getInt("id");
                int uid = rs.getInt("uid");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String pic = rs.getString("pic"); // 获取头像
                String phone = rs.getString("phone"); // 获取电话
                String content = rs.getString("content"); // 获取年龄
                Date time_send = rs.getDate("time_send");
                chatItems.add(new Friend(index,uid, name,phone,age,pic,content,time_send));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlHelper.close(rs, ps, conn);
        }
        return chatItems;
    }


    // 一个方法来根据用户id和聊天对象id查询消息列表
    public ArrayList<Message> queryMessagesByUserId(int user_id, int chatTo_id) {
        ArrayList<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = SqlHelper.getConnection();
            String sql = "SELECT * FROM t_message WHERE (sender_uid = ? AND receiver_id = ?) OR (sender_uid = ? AND receiver_id = ?) ORDER BY time_send";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user_id);
            ps.setInt(2, chatTo_id);
            ps.setInt(3, chatTo_id);
            ps.setInt(4, user_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int mid = rs.getInt("mid");
                String content = rs.getString("content");
                int sender_uid = rs.getInt("sender_uid");
                boolean is_group = rs.getBoolean("is_group");
                int receiver_id = rs.getInt("receiver_id");
                boolean is_withdraw = rs.getBoolean("is_withdraw");
                Date time_send = rs.getDate("time_send");
                //User sender = queryUserById(sender_uid); // 查询发送者的信息
                Message message = new Message(mid, content, sender_uid, is_group, receiver_id, is_withdraw, time_send);
                messages.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlHelper.close(rs, ps, conn);
        }
        return messages;
    }


    // 一个方法来插入一条新的消息到数据库中
    public void insertMessage(Message message) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = SqlHelper.getConnection();
            String sql = "INSERT INTO t_message (content, sender_uid, is_group, receiver_id, time_send) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, message.getContent());
            ps.setInt(2, message.getSenderUid());
            ps.setBoolean(3, message.isGroup());
            ps.setInt(4, message.getReceiverId());
            ps.setDate(5, new java.sql.Date(message.getTime().getTime()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlHelper.close(null, ps, conn);
        }
    }
}
