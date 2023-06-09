package com.example.imitatewechat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imitatewechat.R;

// LoginActivity已被抛弃，部份功能由LoadingActivity取代
@Deprecated
public class LoginActivity extends AppCompatActivity {
    private Button btnVoice;
    private Button btnChange;
    private TextView disName;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnVoice = findViewById(R.id.btn_voice);
        btnChange = findViewById(R.id.btn_change);
        disName = findViewById(R.id.display_name);

        btnVoice.setOnClickListener(view -> {
            Toast toast = Toast.makeText(getApplicationContext(), "登录中", Toast.LENGTH_SHORT);
            toast.show();
//            User me = new User(0, finalSaveName, R.drawable.li);// 创建我
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.putExtra("me", me);
                    startActivity(intent);
                    finish();
                }
            }, 500);
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PhoneNumberLoginActivity.class);
                startActivity(intent);
            }
        });
    }


}