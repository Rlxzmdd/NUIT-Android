package com.example.imitatewechat.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imitatewechat.R;
import com.example.imitatewechat.adapter.FriendsListAdapter;
import com.example.imitatewechat.db.SQLiteDao;
import com.example.imitatewechat.entity.Friend;
import com.example.imitatewechat.entity.User;
import com.example.imitatewechat.util.PreferencesUtil;
import com.example.imitatewechat.widget.EditDialog;
import com.example.imitatewechat.widget.NoTitleAlertDialog;

/**
 * 通讯录
 *
 * @author zhou
 */
public class ContactsFragment extends BaseFragment {

    FriendsListAdapter mFriendsListAdapter;

    TextView mTitleTv;

    ListView mFriendsLv;

    LayoutInflater mInflater;

    // 好友总数
    TextView mFriendsCountTv;


    // 好友列表
    List<Friend> mFriendList;

    SQLiteDao mDao;
    User currentUser;
    // todo 未实例化

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        mTitleTv = view.findViewById(R.id.tv_title);
        mFriendsLv = view.findViewById(R.id.lv_friends);
        view.findViewById(R.id.iv_add).setOnClickListener(view1 -> {
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
                        refreshFriendsList();
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
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDao = new SQLiteDao(getActivity());
        currentUser = mDao.queryUserById(PreferencesUtil.getInstance().getUserID());
        currentUser.setChats(mDao.queryChatListByUser(currentUser));
//        mFriendList = currentUser.getChats();
        mFriendList = mDao.queryAllFriendsByUser(currentUser);


        setTitleStrokeWidth(mTitleTv);

        mInflater = LayoutInflater.from(getActivity());
        View headerView = mInflater.inflate(R.layout.item_contacts_header, null);
        mFriendsLv.addHeaderView(headerView);
        View footerView = mInflater.inflate(R.layout.item_contacts_footer, null);
        mFriendsLv.addFooterView(footerView);

        mFriendsCountTv = footerView.findViewById(R.id.tv_total);


        mFriendsListAdapter = new FriendsListAdapter(getActivity(), R.layout.item_contacts, mFriendList);
        mFriendsLv.setAdapter(mFriendsListAdapter);

        mFriendsCountTv.setText(MessageFormat.format("{0}位联系人", mFriendList.size()));

        mFriendsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // todo 查看好友信息
            }
        });
    }


    public void refreshFriendsList() {
        mFriendList = mDao.queryAllFriendsByUser(currentUser);
        mFriendsListAdapter.setData(mFriendList);
        mFriendsListAdapter.notifyDataSetChanged();
        mFriendsCountTv.setText(MessageFormat.format("{0}位联系人", mFriendList.size()));
    }

}