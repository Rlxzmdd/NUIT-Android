package com.example.imitatewechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imitatewechat.R;
import com.example.imitatewechat.entity.User;

import java.util.ArrayList;

import android.content.Intent;

import com.example.imitatewechat.activity.ChatActivity;

@Deprecated
// 一个类来表示一个适配器，用于在RecyclerView中显示好友
public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<User> mUsers; // 好友列表
    private User mMe;

    // 一个构造方法来创建一个适配器，传入上下文和好友列表参数
    public UserListAdapter(Context context, User me, ArrayList<User> users) {
        mMe = me;
        mContext = context;
        mUsers = users;
    }

    // 一个方法来为RecyclerView中的每个项目创建一个视图持有者
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 为每个好友项目填充布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_layout, parent, false);
        // 创建并返回一个新的视图持有者，传入填充好的视图
        return new NormalHolder(view);
    }

    // 一个方法来将数据绑定到RecyclerView中每个项目的视图持有者上
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 获取给定位置的好友对象
        User user = mUsers.get(position);
        // 将好友的名字、头像和最近一条消息设置到视图持有者中的文本视图和图片视图上
//        ((NormalHolder) holder).mName.setText(user.getName());
//        ((NormalHolder) holder).mMsg.setText(user.getCon());
//        ((NormalHolder) holder).mPic.setImageDrawable(user.getPic(this.mContext));
//        ((NormalHolder) holder).mId.setText(String.valueOf(user.getId()));
    }

    // 一个方法来获取RecyclerView中的项目数量
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    // 一个类来表示RecyclerView中每个项目的视图持有者
    public class NormalHolder extends RecyclerView.ViewHolder {
        public TextView mName; // 用于显示好友名字的文本视图
        public ImageView mPic; // 用于显示好友头像的图片视图
        public TextView mMsg; // 用于显示好友最近一条消息的文本视图
        public TextView mId; // 用于记录好友ID

        // 一个构造方法来创建一个视图持有者，传入视图参数
        public NormalHolder(@NonNull View itemView) {
            super(itemView);
            // 从布局文件中获取文本视图和图片视图
            mName = itemView.findViewById(R.id.item_tv);
            mMsg = itemView.findViewById(R.id.item_msg);
            mPic = itemView.findViewById(R.id.item_img);
            mId = itemView.findViewById(R.id.item_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 使用意图来启动ChatActivity，并传递好友Friend作为参数
                    Intent intent = new Intent(mContext, ChatActivity.class);
//                    intent.putExtra("chatTo", new User(
//                            Integer.parseInt(mId.getText().toString()),
//                            mName.getText().toString(),
//                            mPic.getDrawable()
//                    ));
                    intent.putExtra("me",mMe);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}