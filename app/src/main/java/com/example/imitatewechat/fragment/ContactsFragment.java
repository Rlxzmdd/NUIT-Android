package com.example.imitatewechat.fragment;

import android.os.Bundle;
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

    TextView mNewFriendsUnreadNumTv;

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
//        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        PreferencesUtil.getInstance().init(getActivity());
//        mUserDao = new UserDao();
//        mUser = PreferencesUtil.getInstance().getUser();

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

        RelativeLayout mNewFriendsRl = headerView.findViewById(R.id.rl_new_friends);
        mNewFriendsRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo 添加新好友
//                startActivity(new Intent(getActivity(), NewFriendsActivity.class));
//                PreferencesUtil.getInstance().setNewFriendsUnreadNumber(0);
            }
        });


        mNewFriendsUnreadNumTv = headerView.findViewById(R.id.tv_new_friends_unread);

        // 对list进行排序
//        Collections.sort(mFriendList, new PinyinComparator() {
//        });

//        mStarFriendList.addAll(mFriendList);

        mFriendsListAdapter = new FriendsListAdapter(getActivity(), R.layout.item_contacts, mFriendList);
        mFriendsLv.setAdapter(mFriendsListAdapter);

        mFriendsCountTv.setText(MessageFormat.format("{0}位联系人", mFriendList.size()));

        mFriendsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // todo 查看好友信息
//                if (position != 0 && position != mStarFriendList.size() + 1) {
//                    User friend = mStarFriendList.get(position - 1);
//                    String userType = friend.getUserType();
//                    if (Constant.USER_TYPE_REG.equals(userType)) {
//                        if (friend.getUserId().equals(mUser.getUserId())) {
//                            startActivity(new Intent(getActivity(), UserInfoMyActivity.class));
//                        } else {
//                            startActivity(new Intent(getActivity(), UserInfoActivity.class).
//                                    putExtra("userId", friend.getUserId()));
//                        }
//                    } else if (Constant.USER_TYPE_WEIXIN.equals(userType)) {
//                        startActivity(new Intent(getActivity(), UserInfoActivity.class).
//                                putExtra("userId", friend.getUserId()));
//                    } else if (Constant.USER_TYPE_FILEHELPER.equals(userType)) {
//                        startActivity(new Intent(getActivity(), UserInfoFileHelperActivity.class).
//                                putExtra("userId", friend.getUserId()));
//                    }
//                }
            }
        });
    }


    public void refreshFriendsList() {
        mFriendList = mDao.queryAllFriendsByUser(currentUser);

        // 对list进行排序
//        Collections.sort(mFriendList, new PinyinComparator() {
//        });
//        mStarFriendList.addAll(mFriendList);
        mFriendsListAdapter.setData(mFriendList);
        mFriendsListAdapter.notifyDataSetChanged();
        mFriendsCountTv.setText(MessageFormat.format("{0}位联系人", mFriendList.size()));
    }

}