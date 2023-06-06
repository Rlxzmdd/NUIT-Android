package com.example.imitatewechat.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class DataUtils {
    private final Context context;

    public DataUtils(Context context){
        this.context = context;
    }
    public void saveName(String value) {
        SharedPreferences settings = this.context.getSharedPreferences("quickLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.putString("username", value);
        editor.apply();
    }
    public String getName(){
        SharedPreferences settings = this.context.getSharedPreferences("quickLogin",MODE_PRIVATE);
        return settings.getString("username","");
    }
}
