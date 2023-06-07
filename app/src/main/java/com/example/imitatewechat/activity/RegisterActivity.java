package com.example.imitatewechat.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imitatewechat.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText nickname;
    private Boolean check_nickname = false; // 检测昵称合法性
    private EditText password;
    private EditText confirm_password;
    private Boolean check_password = false; // 检测密码合法性
    private AutoCompleteTextView country;
    private String[] res_country={"中国大陆（+86）🇨🇳","中国🇨🇳","新加坡🇸🇬","加拿大🇨🇦",
            "美国🇺🇸","澳大利亚🇦🇺","日本🇯🇵","瑞典🇸🇪","韩国🇰🇷","俄罗斯🇷🇺","奥地利🇦🇹","其他"};
    private String p1="",p2="";
    private Button register;
    private TextView birth;
    private Boolean check_birth = false;
    private CheckBox agree;
    private Handler handler = new Handler();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nickname = findViewById(R.id.edit_nickname);
        password = findViewById(R.id.edit_password);
        confirm_password = findViewById(R.id.edit_confirm_the_password);
        birth = findViewById(R.id.edit_birth);
        country = findViewById(R.id.edit_country);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,res_country);
        country.setAdapter(adapter);
        register = findViewById(R.id.btn_register);
        agree = findViewById(R.id.agree);

        /**
         * 点击注册按钮，执行条件判断，倘若用户填写的注册信息正确，则完成用户注册操作，
         * 否则弹出toast提醒用户注册失败
         */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_nickname&&check_password&&check_birth&&agree.isChecked()){
                    // 如果注册信息填写完善，则将必要用户信息数据存入数据库
//                    LoginActivity.dao.insert(nickname.getText().toString(),password.getText().toString());
                    // 将必要数据写入SharedPreferences,以便实现自动填充
                    Context context = RegisterActivity.this;
                    SharedPreferences sharedPreferences = context.getSharedPreferences("quickLogin",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("name",nickname.getText().toString());
                    editor.putString("password",password.getText().toString());
                    editor.apply();

                    // 手机弹出交互提示
                    Toast toast = Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(RegisterActivity.this,PhoneNumberLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    },500);
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(),"请检查信息",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        /**
         * 点击「国家」按钮后显示所有可选国家
         */
        country.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                country.showDropDown();
                return false;
            }
        });

        /**
         * 监听昵称的输入，倘若用户输入空格，则提示用户不能输入空格
         */
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = nickname.getText().toString();
                if(name.indexOf(" ")>=0){
                    nickname.setError("昵称不能键入空格");
                    check_nickname = false;
                }else {
                    check_nickname = true;
                }
            }
        });

        /**
         * 监听密码的输入，检测用户两次输入的密码是否一致
         */
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                p1 = password.getText().toString();
                p2 = confirm_password.getText().toString();
                if (!p2.isEmpty()){
                    if (!p1.equals(p2)){
                        confirm_password.setError("两次密码不一致");
                        check_password = false;
                    }else {
                        confirm_password.setError(null);
                        check_password = true;
                    }
                }
            }
        });

        /**
         * 监听密码的输入，检测用户两次输入的密码是否一致
         */
        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                p2 = confirm_password.getText().toString();
                if (!p1.equals(p2)){
                    confirm_password.setError("密码不一致");
                    check_password = false;
                }else {
                    check_password = true;
                }
            }
        });

        /**
         * 点击「生日」按钮时，弹出日期选择框，让用户选择自己的生日
         */
        birth.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(RegisterActivity.this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String text = "你选择了：" + i + "年" + (i1 + 1) + "月" + i2 + "日";
                        birth.setText(i + "-" + (i1 + 1) + "-" + i2);
                        if (calendar.get(Calendar.YEAR)-i >= 100 || calendar.get(Calendar.YEAR)-i <= 0){
                            birth.setError("请重新选择年龄");
                            check_birth = false;
                        }
                        else{
                            birth.setError(null);
                            check_birth = true;
                            Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                    }
                },calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONDAY)
                        ,calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }
}