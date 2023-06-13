package com.example.imitatewechat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imitatewechat.db.SQLiteDao;
import com.example.imitatewechat.entity.User;
import com.example.imitatewechat.util.DataUtils;
import com.example.imitatewechat.R;
import com.example.imitatewechat.util.StatusBarUtil;

public class PhoneNumberLoginActivity extends AppCompatActivity {
    private EditText account;
    private EditText password;
    private Button login_btn;
    private TextView register;
    private Handler handler = new Handler();
    public SQLiteDao mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_login);

        account = findViewById(R.id.edit_account);
        password = findViewById(R.id.edit_password);
        login_btn = findViewById(R.id.btn_login);
        register = findViewById(R.id.register);

        mDao = new SQLiteDao(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneNumberLoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 使用dao封装的方法查询数据库中的数据，如果账户密码匹配成功，则允许用户进行登录
                User currentUser = mDao.queryUserByUserAndPass(account.getText().toString(),password.getText().toString());
                if(currentUser!=null){
                    // 成功登陆后将用户名存入SharedPreferences中，以便重新启动程序时，能显示最新的用户名
                    (new DataUtils(view.getContext())).saveUser(currentUser);
                    Toast toast = Toast.makeText(getApplicationContext(),"登录中",Toast.LENGTH_SHORT);
                    toast.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(PhoneNumberLoginActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("currentUser", currentUser); // 传递当前用户信息
                            startActivity(intent);
                            finish();
                        }
                    },1000);
                }else if(account.getText().toString().length()!=0 && password.getText().toString().length()!=0){
                    Toast toast = Toast.makeText(getApplicationContext(),"账号或密码错误",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (password.getText().toString().length()!=0){
                    login_btn.setBackgroundResource(R.drawable.green_btn);
                    login_btn.setTextColor(getResources().getColor(R.color.white));
                }else {
                    login_btn.setBackgroundResource(R.drawable.gray_btn);
                    login_btn.setTextColor(getResources().getColor(R.color.disable));
                }
            }

        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (account.getText().toString().length()!=0){
                    login_btn.setBackgroundResource(R.drawable.green_btn);
                    login_btn.setTextColor(getResources().getColor(R.color.white));
                }else{
                    login_btn.setBackgroundResource(R.drawable.gray_btn);
                    login_btn.setTextColor(getResources().getColor(R.color.disable));
                }
            }
        });
    }

}