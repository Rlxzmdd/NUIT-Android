package com.example.imitatewechat.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.example.imitatewechat.model.User;
import com.example.imitatewechat.model.Message;

/**
 * Tips：Dao类封装了一系列数据库操作，比如插入条目，搜索条目等等
 */
public class MySQLDao {
    private final MySQLHelper SqlHelper;

    public MySQLDao() {
        SqlHelper = new MySQLHelper();
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

    // 一个方法来根据用户id查询用户的好友列表
    public ArrayList<User> queryFriendsByUserId(int user_uid) {
        ArrayList<User> friends = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = SqlHelper.getConnection();
            String sql = "SELECT * FROM t_user WHERE uid IN (SELECT friend_uid FROM t_friend WHERE user_uid = ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user_uid);
            rs = ps.executeQuery();
            while (rs.next()) {
                int uid = rs.getInt("uid");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                int age = rs.getInt("age");
                String pic = rs.getString("pic");
                String msg = rs.getString("msg");
                User friend = new User(uid, name, pic, msg);
                friends.add(friend);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlHelper.close(rs, ps, conn);
        }
        return friends;
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
                User sender = queryUserById(sender_uid); // 查询发送者的信息
                Message message = new Message(mid, content, sender, is_group, receiver_id, is_withdraw, time_send);
                messages.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlHelper.close(rs, ps, conn);
        }
        return messages;
    }

    public ArrayList<User> queryUsersByUserId(int user_id) {
        ArrayList<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = SqlHelper.getConnection();
            String sql = "SELECT * FROM t_user WHERE uid IN (SELECT DISTINCT receiver_id FROM t_message WHERE sender_uid = ?) OR uid IN (SELECT DISTINCT sender_uid FROM t_message WHERE receiver_id = ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user_id);
            ps.setInt(2, user_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int uid = rs.getInt("uid");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                int age = rs.getInt("age");
                String pic = rs.getString("pic");
                User user = new User(uid, name, phone, age, pic);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlHelper.close(rs, ps, conn);
        }
        return users;
    }

    // 一个方法来根据消息id更新消息的撤回状态
    public void updateMessage(int mid, boolean isWithdraw) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = SqlHelper.getConnection();
            String sql = "UPDATE t_message SET is_withdraw = ? WHERE mid = ?";
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, isWithdraw);
            ps.setInt(2, mid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlHelper.close(null, ps, conn);
        }
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
            ps.setInt(2, message.getSender().getUid());
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
