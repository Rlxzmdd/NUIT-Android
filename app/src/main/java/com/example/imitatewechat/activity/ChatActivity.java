package com.example.imitatewechat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imitatewechat.R;
import com.example.imitatewechat.adapter.MessageAdapter;
import com.example.imitatewechat.db.SQLiteDao;
import com.example.imitatewechat.entity.ChatFriend;
import com.example.imitatewechat.entity.Message;
import com.example.imitatewechat.entity.User;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private User currentUser; // 当前用户
    private ChatFriend chatTo; // 聊天对象
    private ArrayList<Message> mMessages; // 消息列表
    private SQLiteDao mDao; // 数据库操作对象
    private MessageAdapter mAdapter; // 消息适配器

    private EditText mInputEt; // 输入框
    private Button mSendBtn; // 发送按钮
    private TextView mChatTitle; // 聊天标题
    private ImageButton mChatButton; // 回退按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mDao = new SQLiteDao(this);
        currentUser = getIntent().getParcelableExtra("currentUser"); // 从intent中获取当前用户信息
        chatTo = getIntent().getParcelableExtra("chatTo"); // 从intent中获取聊天对象信息

        mInputEt = findViewById(R.id.editTextMessageInput); // 获取输入框
        mSendBtn = findViewById(R.id.buttonSendMessage); // 获取发送按钮
        mChatTitle = findViewById(R.id.chat_title);
        mChatButton = findViewById(R.id.chat_return);

        RecyclerView mRv = findViewById(R.id.recyclerViewMessages); // 获取消息列表视图

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this); // 创建一个线性布局管理器
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // 设置垂直方向
        mRv.setLayoutManager(linearLayoutManager); // 为消息列表视图设置布局管理器

        mMessages = mDao.queryMessagesByChaTo(currentUser, chatTo.getObjectId()); // 查询当前用户和聊天对象的消息记录

        mAdapter = new MessageAdapter(this, mMessages, currentUser,chatTo); // 创建一个消息适配器，传入上下文、消息列表和当前用户信息
        mRv.setAdapter(mAdapter); // 为消息列表视图设置适配器

        mChatTitle.setText(chatTo.getName());
        mSendBtn.setOnClickListener(v -> {
            // 点击发送按钮时，获取输入框的内容，并判断是否为空
            String content = mInputEt.getText().toString();
            if (!content.isEmpty()) {
                // 如果不为空，创建一个新的消息对象，设置其属性
                Message message = new Message(0, content, currentUser.getUid(), false, chatTo.getObjectId(), false, new Date());
                mDao.insertMessage(message); // 将新的消息插入到数据库中
                mMessages.add(message); // 将新的消息添加到消息列表中
                mAdapter.notifyItemInserted(mMessages.size() - 1); // 通知适配器有新的项目插入到最后一个位置
                mRv.scrollToPosition(mMessages.size() - 1); // 让消息列表视图滚动到最后一个位置
                mInputEt.setText(""); // 清空输入框的内容
            }else{
                Toast toast = Toast.makeText(v.getContext(),"请输入信息",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        mChatButton.setOnClickListener(view -> {
            Intent result = new Intent();
            result.putExtra("message", mMessages.get(mMessages.size()-1).getContent());
            setResult(RESULT_OK, result);
            finish();
        });
    }

}