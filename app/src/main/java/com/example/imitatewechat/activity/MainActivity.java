package com.example.imitatewechat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.imitatewechat.R;
import com.example.imitatewechat.adapter.MessageListAdapter;
import com.example.imitatewechat.db.SQLiteDao;
import com.example.imitatewechat.model.ChatFriend;
import com.example.imitatewechat.model.Friend;
import com.example.imitatewechat.model.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDao mDao; // 数据库操作对象
    private User currentUser;//当前用户
    private MessageListAdapter adapter;
    private void initData() {
        //更新 User 中的好友关系
        currentUser.setChats(mDao.queryChatListByUser(currentUser));
//        mUsers.add(new User(1,"陈晓贤", R.drawable.cai, "你试试看通过那个找回密码吧"));
//        mFriends.add(new Friend(2,"在佛山也要爬山", R.drawable.shujia, "我:什么时候去爬山"));
//        mUsers.add(new User(3,"东软要你死 你活不到三更", R.drawable.neu, "大哥: 嗯嗯路上"));
//        mUsers.add(new User(4,"立", R.drawable.li, "践行一下废话文学"));
//        mFriends.add(new Friend(5,"相亲相爱一家人", R.drawable.dongmen, "虹:[视频]"));
//        mFriends.add(new Friend(6,"温馨家庭", R.drawable.woai, "虹:[视频]"));
//        mFriends.add(new Friend(7,"503", R.drawable.nanfang, "[旺财][旺财][旺财][旺财][旺财]"));
//        mFriends.add(new Friend(8,"2023讨论群", R.drawable.yangyang, "Li:是的[强][强][强]"));
//        mUsers.add(new User(9,"奶奶", R.drawable.nainai, "[语音通话]"));
//        mFriends.add(new Friend(10,"工作", R.drawable.findmie, "思晴弟弟:[强]"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRv = findViewById(R.id.message_list);
        currentUser = getIntent().getParcelableExtra("currentUser");
        mDao = new SQLiteDao(this);

        initData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(linearLayoutManager);

        adapter = new MessageListAdapter(this,currentUser);
        mRv.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果结果码是成功的
            // 查看从那个聊天记录返回，并更新最新Friend中的msg
            ChatFriend friend = currentUser.getChats().get(requestCode);
            friend.setMsg(data.getStringExtra("message"));
            adapter.notifyDataSetChanged();
        }
    }
}
