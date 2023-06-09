package com.example.imitatewechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.imitatewechat.activity.MainActivity;
import com.example.imitatewechat.activity.PhoneNumberLoginActivity;
import com.example.imitatewechat.db.SQLiteDao;
import com.example.imitatewechat.exception.UserNotFoundException;
import com.example.imitatewechat.entity.User;
import com.example.imitatewechat.util.DataUtils;

public class LoadingActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private User currentUser;
    private SQLiteDao dao;
    private Class toActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        hideSystemUI();
        //初始化数据库
        dao=new SQLiteDao(this);


        try {
            currentUser = (new DataUtils(this)).getUser(dao);
            toActivity = MainActivity.class;
        } catch (UserNotFoundException e) {
            //本地用户不存在
            toActivity = PhoneNumberLoginActivity.class;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, toActivity);
                if(toActivity == MainActivity.class){
                    intent.putExtra("currentUser", currentUser); // 传递当前用户信息
                }
                startActivity(intent);
                finish();
            }
        },2000);
    }

    private void hideSystemUI() {
        //启用向后倾斜模式
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}