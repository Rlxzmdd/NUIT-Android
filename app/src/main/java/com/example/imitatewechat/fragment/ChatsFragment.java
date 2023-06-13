package com.example.imitatewechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.imitatewechat.R;
import com.example.imitatewechat.activity.ChatActivity;
import com.example.imitatewechat.adapter.ConversationAdapter;
import com.example.imitatewechat.db.SQLiteDao;
import com.example.imitatewechat.entity.ChatFriend;
import com.example.imitatewechat.entity.Message;
import com.example.imitatewechat.entity.User;
import com.example.imitatewechat.task.WeatherTask;
import com.example.imitatewechat.util.PreferencesUtil;
import com.example.imitatewechat.widget.EditDialog;
import com.example.imitatewechat.widget.NoTitleAlertDialog;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends BaseFragment {

    TextView mTitleTv;

    ListView mConversationLv;

    public ConversationAdapter mConversationAdapter;


    private static final int REFRESH_CONVERSATION_LIST = 0x3000;
    User currentUser;
    SQLiteDao mDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        this.mTitleTv = view.findViewById(R.id.tv_title);
        this.mConversationLv = view.findViewById(R.id.lv_conversation);
        view.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 EditDialog mEditDialog = new EditDialog(getActivity(), "添加好友",
                        "", "输入要想添加的好友id",
                        "添加", "取消");
                mEditDialog.setOnDialogClickListener(new EditDialog.OnDialogClickListener() {
                    @Override
                    public void onOkClick() {
                        mEditDialog.dismiss();
                        String user_id = mEditDialog.getContent();
                        boolean status = mDao.addFriendByUserId(currentUser, Integer.parseInt(user_id));
                        Log.d("add_userid",user_id+","+status);
                        if(status){
                            (new NoTitleAlertDialog(getActivity(),"成功添加好友","确认")).show();
                            refreshConversationList();
                        }else{
                            (new NoTitleAlertDialog(getActivity(),"添加好友失败，请确认好友id","确认")).show();
                        }
                    }

                    @Override
                    public void onCancelClick() {
                        mEditDialog.dismiss();
                    }
                });

                mEditDialog.show();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitleStrokeWidth(mTitleTv);
        // 更新天气情况
        WeatherTask weather = new WeatherTask(this.getContext(),mTitleTv);
        new Thread(weather).start();

        mDao = new SQLiteDao(getActivity());
        currentUser = mDao.queryUserById(PreferencesUtil.getInstance().getUserID());
        currentUser.setChats(mDao.queryChatListByUser(currentUser));

        mConversationAdapter = new ConversationAdapter(getActivity(), currentUser);
        mConversationLv.setAdapter(mConversationAdapter);

        mConversationLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatFriend friend = currentUser.getChats().get(position);

                // 点击子项时，跳转到聊天界面，并传递当前用户和聊天对象的信息
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("chatTo", friend);
                Log.d("chatto",getActivity().getLocalClassName());
                getActivity().startActivityForResult(intent,friend.getChatId());

            }
        });
    }

    public void refreshConversationList() {
        currentUser.setChats(mDao.queryChatListByUser(currentUser));
        mConversationAdapter.setData(currentUser.getChats());
        mConversationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == REFRESH_CONVERSATION_LIST) {
//                List<Conversation> newConversationList = JMessageClient.getConversationList();
//                mConversationAdapter.setData(newConversationList);
//                mConversationAdapter.notifyDataSetChanged();
//            }
//        }
//    };
}
