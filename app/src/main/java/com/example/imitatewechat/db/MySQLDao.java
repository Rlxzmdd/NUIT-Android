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
    private final MySQLHelper helper;

    public MySQLDao() {
        helper = new MySQLHelper();
    }

    /**
     * 实例化数据
     */
    public void init() {
        Connection conn = helper.getConnection();
        String sql = "INSERT INTO user (id, name, password, age, pic) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, 1);
            ps.setString(2, "admin");
            ps.setString(3, "1234");
            ps.setInt(4, 21);
            ps.setString(5, "R.drawable.cai");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.closeConnection(conn);
        }
    }

    public void insertUser(int id, String name, String password, int age, String pic) {
        Connection conn = helper.getConnection();
        String sql = "INSERT INTO user (id, name, password, age, pic) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, password);
            ps.setInt(4, age);
            ps.setString(5, pic);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.closeConnection(conn);
        }
    }

    public void insertFriend(int user_id, int friend_id) {
        Connection conn = helper.getConnection();
        String sql = "INSERT INTO friend (user_id, friend_id) VALUES (?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user_id);
            ps.setInt(2, friend_id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.closeConnection(conn);
        }
    }

    public void insertMessage(int id, String content, int sender_id, int receiver_id) {
        Connection conn = helper.getConnection();
        String sql = "INSERT INTO message (id, content, sender_id, receiver_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, content);
            ps.setInt(3, sender_id);
            ps.setInt(4, receiver_id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.closeConnection(conn);
        }
    }

    public User queryUserById(int id) {
        Connection conn = helper.getConnection();
        String sql = "SELECT * FROM user WHERE id = ?";
        User user = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String password = rs.getString("password");
                int age = rs.getInt("age");
                String pic = rs.getString("pic");
                user = new User(id, name);
//                friend = new Friend(id, name, pic);
//                friend.setPassword(password);
//                friend.setAge(age);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.closeConnection(conn);
        }
        return user;
    }

    public ArrayList<User> queryFriendsByUserId(int user_id) {
        Connection conn = helper.getConnection();
        String sql = "SELECT * FROM friend WHERE user_id = ?";
        ArrayList<User> users = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int friend_id = rs.getInt("friend_id");
                User user = queryUserById(friend_id);
                users.add(user);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.closeConnection(conn);
        }
        return users;
    }

    public ArrayList<Message> queryMessagesByUserId(int user_id) {
        Connection conn = helper.getConnection();
        String sql = "SELECT * FROM message WHERE sender_id = ? OR receiver_id = ?";
        ArrayList<Message> messages = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user_id);
            ps.setInt(2, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String content = rs.getString("content");
                int sender_id = rs.getInt("sender_id");
                int receiver_id = rs.getInt("receiver_id");
                User sender = queryUserById(sender_id);
                User receiver = queryUserById(receiver_id);
                Message message = new Message(content, sender, receiver, new Date());
                messages.add(message);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.closeConnection(conn);
        }
        return messages;
    }
}