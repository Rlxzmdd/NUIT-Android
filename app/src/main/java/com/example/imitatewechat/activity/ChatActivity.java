package com.example.imitatewechat.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.imitatewechat.R;
import com.example.imitatewechat.adapter.MessageAdapter;
import com.example.imitatewechat.model.User;
import com.example.imitatewechat.model.Message;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessageInput;
    private Button buttonSendMessage;
    private ImageButton buttonReturn;
    private TextView chatName;

    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;

    private User chatTo; // 聊天对象
    private User me; // 当前用户



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 从布局文件中获取视图
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessageInput = findViewById(R.id.editTextMessageInput);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        chatName = findViewById(R.id.chat_name);
        buttonReturn = findViewById(R.id.chat_return);

        chatTo = getIntent().getParcelableExtra("chatTo");//从intent取出chatTo
        me = getIntent().getParcelableExtra("me");//从intent取出me
        chatName.setText(chatTo.getName());

        // 初始化消息列表和适配器
        messageList = new ArrayList<>();
        loadMessages();

        messageAdapter = new MessageAdapter(this,messageList,me);

        // 为RecyclerView设置适配器和布局管理器
        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        //为返回按钮设置监听
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 为发送按钮设置点击监听器
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入文本并去除任何空白
                String input = editTextMessageInput.getText().toString().trim();

                // 检查输入是否不为空
                if (!input.isEmpty()) {
                    // 创建一个新的消息对象，传入输入文本和当前时间
                    Message message = new Message(input, me,chatTo , new Date());

                    // 将消息发送到聊天频道
                    sendMessage(message);

                    // 将消息添加到列表并通知适配器
                    messageList.add(message);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);

                    // 滚动到RecyclerView的底部
                    recyclerViewMessages.scrollToPosition(messageList.size() - 1);

                    // 清空输入文本框
                    editTextMessageInput.setText("");
                }
            }
        });
    }

    // 一个方法来加载聊天频道的消息
    private void loadMessages() {
        // TODO: 实现加载聊天频道的消息
        messageList.add(new Message("你好",  chatTo, me, new Date()));
        messageList.add(new Message("你好，很高兴认识你", me,chatTo,  new Date()));
        messageList.add(new Message("我也是", chatTo, me, new Date()));
        messageList.add(new Message("你喜欢什么？",me,chatTo,  new Date()));
        messageList.add(new Message("我喜欢编程", chatTo, me, new Date()));
    }

    // 一个方法来将消息发送到聊天频道
    private void sendMessage(Message message) {
        // TODO: 实现消息发送到服务器
    }
}

