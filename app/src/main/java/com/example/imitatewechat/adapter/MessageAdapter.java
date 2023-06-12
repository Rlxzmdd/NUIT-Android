package com.example.imitatewechat.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imitatewechat.R;
import com.example.imitatewechat.entity.ChatFriend;
import com.example.imitatewechat.entity.Message;
import com.example.imitatewechat.entity.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<Message> mMessages; // 消息列表
//    private String mUserId; // 当前用户的ID

    private static final int TYPE_LEFT = 0; // 左边布局的类型
    private static final int TYPE_RIGHT = 1; // 右边布局的类型

    private User currentUser; // 当前用户
    private ChatFriend chatTo;
    // 一个构造方法来创建一个适配器，传入上下文、消息列表和当前用户ID参数
    public MessageAdapter(Context context, ArrayList<Message> messages, User me,ChatFriend chatTo) {
        mContext = context;
        mMessages = messages;
        currentUser = me;
        this.chatTo = chatTo;
    }
    // 一个方法来为RecyclerView中的每个项目创建一个视图持有者
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LEFT) { // 如果是左边布局的类型
            // 为每个左边消息项目填充布局文件
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            // 创建并返回一个新的左边视图持有者，传入填充好的视图
            return new LeftHolder(view);
        } else { // 如果是右边布局的类型
            // 为每个右边消息项目填充布局文件
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            // 创建并返回一个新的右边视图持有者，传入填充好的视图
            return new RightHolder(view);
        }
    }

    // 一个方法来将数据绑定到RecyclerView中每个项目的视图持有者上
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 获取给定位置的消息对象
        Message message = mMessages.get(position);
        // 格式化消息时间
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String time = sdf.format(message.getTime());
        Log.d("chat-time",time+"->"+message.getContent());
        // 获取消息发送者
        //User sender = message.getSender ();
        // 判断视图持有者的类型并设置文本和图片
        if (holder instanceof LeftHolder) { // 如果是左边视图持有者
            LeftHolder leftHolder = (LeftHolder) holder; // 强制类型转换
            leftHolder.contentTv.setText(message.getContent());
            leftHolder.timeTv.setText(time);
//            leftHolder.picImv.setImageDrawable(Drawable.createFromPath(chatTo.getPic()));
            leftHolder.nameTv.setText(chatTo.getName());
        } else { // 如果是右边视图持有者
            RightHolder rightHolder = (RightHolder) holder; // 强制类型转换
            rightHolder.contentTv.setText(message.getContent());
            rightHolder.timeTv.setText(time);
//            rightHolder.picImv.setImageDrawable(Drawable.createFromPath(currentUser.getPic()));
            rightHolder.nameTv.setText(currentUser.getName());
        }
    }

    // 一个方法来获取RecyclerView中的项目数量
    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    // 一个方法来获取RecyclerView中每个项目的类型，根据消息的发送者和接收者来判断是左边还是右边的布局
    @Override
    public int getItemViewType(int position) {
        Message message = mMessages.get(position);
        if (message.getSenderUid() == currentUser.getUid()) { // 如果消息发送者是当前用户
            return TYPE_RIGHT; // 返回右边布局的类型
        } else { // 如果消息发送者不是当前用户
            return TYPE_LEFT; // 返回左边布局的类型
        }
    }

    // 一个类来表示RecyclerView中每个左边项目的视图持有者
    public class LeftHolder extends RecyclerView.ViewHolder {
        public TextView contentTv; // 用于显示消息内容的文本视图
        public TextView timeTv; // 用于显示消息时间的文本视图
        public ImageView picImv; // 用于显示头像
        public TextView nameTv; // 用于显示姓名

        // 一个构造方法来创建一个左边视图持有者，传入视图参数
        public LeftHolder(@NonNull View itemView) {
            super(itemView);
            // 从布局文件中获取文本视图
            contentTv = itemView.findViewById(R.id.message_left_content);
            timeTv = itemView.findViewById(R.id.message_left_time);
            picImv = itemView.findViewById(R.id.message_left_avatar);
            nameTv = itemView.findViewById(R.id.message_left_name);
        }
    }

    // 一个类来表示RecyclerView中每个右边项目的视图持有者
    public class RightHolder extends RecyclerView.ViewHolder {
        public TextView contentTv; // 用于显示消息内容的文本视图
        public TextView timeTv; // 用于显示消息时间的文本视图
        public ImageView picImv; // 用于显示头像
        public TextView nameTv; // 用于显示姓名

        // 一个构造方法来创建一个右边视图持有者，传入视图参数
        public RightHolder(@NonNull View itemView) {
            super(itemView);
            // 从布局文件中获取文本视图
            contentTv = itemView.findViewById(R.id.message_right_content);
            timeTv = itemView.findViewById(R.id.message_right_time);
            picImv = itemView.findViewById(R.id.message_right_avatar);
            nameTv = itemView.findViewById(R.id.message_right_name);
        }
    }
}
