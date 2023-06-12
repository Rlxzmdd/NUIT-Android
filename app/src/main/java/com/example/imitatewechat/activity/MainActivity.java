package com.example.imitatewechat.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imitatewechat.R;
import com.example.imitatewechat.adapter.MessageListAdapter;
import com.example.imitatewechat.db.SQLiteDao;
import com.example.imitatewechat.entity.Friend;
import com.example.imitatewechat.fragment.ChatsFragment;
import com.example.imitatewechat.fragment.ContactsFragment;
import com.example.imitatewechat.entity.ChatFriend;
import com.example.imitatewechat.entity.User;
import com.example.imitatewechat.util.PreferencesUtil;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {


    private Fragment[] mFragments;
    private ChatsFragment mChatsFragment;
    private ContactsFragment mContactsFragment;
    private ImageView[] mMainButtonIvs;
    private TextView[] mMainButtonTvs;
    private SQLiteDao mDao; // 数据库操作对象
    private User currentUser;//当前用户
//    private MessageListAdapter adapter;
    private int mIndex;
    private int mCurrentTabIndex;
    public void initData() {
        PreferencesUtil.getInstance().init(this);
        PreferencesUtil.getInstance().setUser(currentUser);
        currentUser.setChats(mDao.queryChatListByUser(currentUser));
    }
    @Override
    public void initView() {
        initStatusBar();
        mChatsFragment = new ChatsFragment();
        mContactsFragment = new ContactsFragment();
        mFragments = new Fragment[]{mChatsFragment, mContactsFragment};

        mMainButtonIvs = new ImageView[2];
        mMainButtonIvs[0] = findViewById(R.id.iv_chats);
        mMainButtonIvs[1] = findViewById(R.id.iv_contacts);
        mMainButtonIvs[0].setSelected(true);

        mMainButtonTvs = new TextView[2];
        mMainButtonTvs[0] = findViewById(R.id.tv_chats);
        mMainButtonTvs[1] = findViewById(R.id.tv_contacts);
        mMainButtonTvs[0].setTextColor(0xFF45C01A);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rl_fragment_container, mChatsFragment)
                .add(R.id.rl_fragment_container, mContactsFragment)
                .hide(mContactsFragment)
                .show(mChatsFragment).commit();

    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentUser = getIntent().getParcelableExtra("currentUser");
        mDao = new SQLiteDao(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        RecyclerView mRv = findViewById(R.id.message_list);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRv.setLayoutManager(linearLayoutManager);
//
//        adapter = new MessageListAdapter(this,currentUser);
//        mRv.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果结果码是成功的
            Log.d("chat_code", String.valueOf(requestCode));
            // 查看从那个聊天记录返回，并更新最新Friend中的msg
            currentUser.setChats(mDao.queryChatListByUser(currentUser));
            ArrayList<ChatFriend> fs = currentUser.getChats();
            ChatFriend f = fs.get(requestCode);
            f.setMsg(data.getStringExtra("message"));
            fs.set(requestCode,f);
            mChatsFragment.mConversationAdapter.setData(fs);
            mChatsFragment.mConversationAdapter.notifyDataSetChanged();
        }
    }

    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_chats:
                // 会话列表
                // 主动加载一次会话
                mChatsFragment.refreshConversationList();
                mIndex = 0;
                //StatusBarUtil.setStatusBarColor(MainActivity.this, R.color.app_common_bg);
                break;
            case R.id.rl_contacts:
                mIndex = 1;
                //StatusBarUtil.setStatusBarColor(MainActivity.this, R.color.app_common_bg);
                break;
        }

        if (mCurrentTabIndex != mIndex) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(mFragments[mCurrentTabIndex]);
            if (!mFragments[mIndex].isAdded()) {
                trx.add(R.id.rl_fragment_container, mFragments[mIndex]);
            }
            trx.show(mFragments[mIndex]).commit();
        }
        mMainButtonIvs[mCurrentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mMainButtonIvs[mIndex].setSelected(true);
        mMainButtonTvs[mCurrentTabIndex].setTextColor(getColor(R.color.black_deep));
        mMainButtonTvs[mIndex].setTextColor(0xFF45C01A);
        mCurrentTabIndex = mIndex;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }
}
