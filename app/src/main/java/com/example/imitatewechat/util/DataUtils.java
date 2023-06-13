package com.example.imitatewechat.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.imitatewechat.db.SQLiteDao;
import com.example.imitatewechat.exception.UserNotFoundException;
import com.example.imitatewechat.entity.User;

public class DataUtils {
    private final Context context;

    public DataUtils(Context context){
        this.context = context;
    }
    public void saveUser(User user) {
        SharedPreferences settings = this.context.getSharedPreferences("quickLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.putInt("user_id", user.getUid());
        editor.putString("user_name", user.getName());
        editor.apply();
    }
    public User getUser(SQLiteDao dao) {
        SharedPreferences settings = this.context.getSharedPreferences("quickLogin",MODE_PRIVATE);
        int uid = settings.getInt("user_id",-1);
        if(uid == -1)
            throw new UserNotFoundException();
        return dao.queryUserById(uid);
    }
    public void clear(){
        SharedPreferences settings = this.context.getSharedPreferences("quickLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }
}
