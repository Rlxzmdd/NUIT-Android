package com.example.imitatewechat.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.imitatewechat.R;
import com.example.imitatewechat.db.SQLiteDao;
import com.example.imitatewechat.entity.ChatFriend;
import com.example.imitatewechat.entity.Message;
import com.example.imitatewechat.entity.User;
import com.example.imitatewechat.util.TimestampUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater inflater;
    private SQLiteDao mDao;
    private User currentUser;

    public ConversationAdapter(Context context,User currentUser) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        mDao = new SQLiteDao(context);
        this.currentUser = currentUser;
    }

    public void setData(ArrayList<ChatFriend> messageList) {
        if (null != messageList) {
            currentUser.setChats(new ArrayList<>());
            currentUser.setChats(messageList);
        }
    }

    @Override
    public int getCount() {
        return currentUser.getChats().size();
    }

    @Override
    public ChatFriend getItem(int position) {
        return currentUser.getChats().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatFriend conversation = currentUser.getChats().get(position);

            // 单聊
            convertView = creatConvertView(0);
            TextView mNickNameTv = convertView.findViewById(R.id.tv_nick_name);
            TextView mLastMsgTv = convertView.findViewById(R.id.tv_last_msg);
            ImageView mAvatarSdv = convertView.findViewById(R.id.sdv_avatar);
            TextView mCreateTimeTv = convertView.findViewById(R.id.tv_create_time);

            //UserInfo userInfo = (UserInfo) conversation.getTargetInfo();
            //User user = mUserDao.getUserById(userInfo.getUserName());
            mNickNameTv.setText(conversation.getName());
//            if (!TextUtils.isEmpty(user.getUserAvatar())) {
//                mAvatarSdv.setImageURI(Uri.parse(user.getUserAvatar()));
//            }

            // 如果消息被清除
            // conversation.getLastestMessage() == null
            mLastMsgTv.setText(conversation.getMsg());
//
//            int unReadMsgCnt = conversation.getUnReadMsgCnt();
//            if (unReadMsgCnt <= 0) {
//                mUnreadTv.setVisibility(View.GONE);
//            } else if (unReadMsgCnt > 99) {
//                mUnreadTv.setText("99+");
//            } else {
//                mUnreadTv.setText(String.valueOf(conversation.getUnReadMsgCnt()));
//            }
            // conversation由极光维护
            // 如果消息被清除，conversation.getLastestMessage() == null
            // 这个时间不好显示

            mCreateTimeTv.setText(TimestampUtil.getTimePoint(conversation.getDate()));


        // 不考虑群聊情况
        return convertView;
    }
    private View creatConvertView(int size) {
        View convertView;

        convertView = inflater.inflate(R.layout.item_conversation_single,
                null, false);

        return convertView;
    }

    private String generateGroupDesc(String myNickName, String... userNickNames) {
        StringBuffer groupDescBuffer = new StringBuffer();
        for (String userNickName : userNickNames) {
            if (!userNickName.equals(myNickName)) {
                groupDescBuffer.append(userNickName).append("、");
            }
        }
        if (groupDescBuffer.length() > 1) {
            groupDescBuffer.deleteCharAt(groupDescBuffer.length() - 1);
        }
        return groupDescBuffer.toString();
    }
}
