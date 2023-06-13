package com.example.imitatewechat.activity;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.imitatewechat.LoadingActivity;
import com.example.imitatewechat.util.DataUtils;
import com.example.imitatewechat.widget.AlertDialog;
import com.example.imitatewechat.widget.NoTitleAlertDialog;


import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
/**
 * activity基类
 *
 * @author zhou
 */
public abstract class BaseActivity extends FragmentActivity {
    private ActivityCollector ac = new ActivityCollector();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initData();
        initListener();
        initView();
        ac.addActivity(this);
    }

    public abstract int getContentView();

    public abstract void initView();

    public abstract void initListener();

    public abstract void initData();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ac.removeActivity(this);
    }



    protected void initStatusBar() {
        Window win = getWindow();
        // KITKAT也能满足，只是SYSTEM_UI_FLAG_LIGHT_STATUS_BAR（状态栏字体颜色反转）只有在6.0才有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
            win.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 显示警告弹窗
     *
     * @param context    context
     * @param title      标题
     * @param content    内容
     * @param confirm    确认键
     * @param cancelable 点击空白处是否消失
     */
    protected void showAlertDialog(Context context, String title, String content, String confirm, boolean cancelable) {
        final AlertDialog mAlertDialog = new AlertDialog(context, title, content, confirm);
        mAlertDialog.setOnDialogClickListener(new AlertDialog.OnDialogClickListener() {
            @Override
            public void onOkClick() {
                mAlertDialog.dismiss();
            }

        });
        // 点击空白处消失
        mAlertDialog.setCancelable(cancelable);
        mAlertDialog.show();
    }

    /**
     * 显示警告弹窗(无标题)
     *
     * @param context context
     * @param content 内容
     * @param confirm 确认键
     */
    protected void showNoTitleAlertDialog(Context context, String content, String confirm) {
        final NoTitleAlertDialog mNoTitleAlertDialog = new NoTitleAlertDialog(context, content, confirm);
        mNoTitleAlertDialog.setOnDialogClickListener(new NoTitleAlertDialog.OnDialogClickListener() {
            @Override
            public void onOkClick() {
                mNoTitleAlertDialog.dismiss();
            }

        });
        // 点击空白处消失
        mNoTitleAlertDialog.setCancelable(true);
        mNoTitleAlertDialog.show();
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 渲染标题粗细程度
     *
     * @param textView 标题textView
     */
    protected void setTitleStrokeWidth(TextView textView) {
        TextPaint paint = textView.getPaint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        // 控制字体加粗的程度
        paint.setStrokeWidth(0.8f);
    }



    // 强制下线动作
    private ForceOfflineReceiver receiver; // 广播接收器

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(); // 创建一个广播过滤器
        intentFilter.addAction("com.example.forceoffline.FORCE_OFFLINE"); // 添加强制下线的动作
        receiver = new ForceOfflineReceiver(); // 创建一个广播接收器
        registerReceiver(receiver, intentFilter); // 注册广播接收器
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver); // 注销广播接收器
            receiver = null;
        }
    }

    class ForceOfflineReceiver extends BroadcastReceiver { // 定义一个内部类继承自BroadcastReceiver
        @Override
        public void onReceive(final Context context, Intent intent) {
            final AlertDialog mAlertDialog = new AlertDialog(context, "下线通知", "即将下线", "确认");
            mAlertDialog.setOnDialogClickListener(new AlertDialog.OnDialogClickListener() {
                @Override
                public void onOkClick() {
                    mAlertDialog.dismiss();
                    ac.finishAll();
                    (new DataUtils(context)).clear();
                    Intent intent = new Intent(context, LoadingActivity.class);
                    context.startActivity(intent); // 启动登录界面活动
                }

            });
            // 点击空白处消失
            mAlertDialog.show();
        }
    }
}