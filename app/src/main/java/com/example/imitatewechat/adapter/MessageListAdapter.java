package com.example.imitatewechat.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imitatewechat.R;
import com.example.imitatewechat.activity.ChatActivity;
import com.example.imitatewechat.activity.MainActivity;
import com.example.imitatewechat.db.SQLiteDao;
import com.example.imitatewechat.entity.ChatFriend;
import com.example.imitatewechat.entity.User;

import java.text.SimpleDateFormat;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    private MainActivity mContext; // 上下文对象
    private User currentUser; // 当前用户
    //private ArrayList<ChatFriend> mUsers; // 有聊天记录的好友和群组列表
    private SQLiteDao mDao; // 数据库操作对象

    public MessageListAdapter(MainActivity context, User me) {
        this.mContext = context;
        this.currentUser = me;
        //this.mUsers = users;
        this.mDao = new SQLiteDao(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载子项布局，返回一个ViewHolder对象
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 为子项绑定数据
        ChatFriend friend = currentUser.getChats().get(position); // 获取当前位置的用户对象
        holder.itemImg.setImageDrawable(Drawable.createFromPath(friend.getPic())); // 设置用户头像
        holder.itemTv.setText(friend.getName()); // 设置用户昵称
//        ArrayList<Message> messages = mDao.queryMessagesByUserId(currentUser, friend.getObjectId()); // 查询当前用户和该用户的聊天记录
//        if (!messages.isEmpty()) {
//            Message lastMessage = messages.get(messages.size() - 1); // 获取最后一条消息
            holder.itemMsg.setText(friend.getMsg()); // 设置最后一条消息的内容
//        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        holder.itemTime.setText(sdf.format(friend.getDate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击子项时，跳转到聊天界面，并传递当前用户和聊天对象的信息
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("chatTo", friend);
                mContext.startActivityForResult(intent,friend.getChatId());
            }
        });
    }

    @Override
    public int getItemCount() {
        // 返回列表的长度
        return currentUser.getChats().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // 定义子项的视图控件
        ImageView itemImg;
        TextView itemTv;
        TextView itemMsg;
        TextView itemTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 获取子项的视图控件
            itemImg = itemView.findViewById(R.id.item_img);
            itemTv = itemView.findViewById(R.id.item_tv);
            itemMsg = itemView.findViewById(R.id.item_msg);
            itemTime =  itemView.findViewById(R.id.item_time);
        }
    }
}
