package com.example.imitatewechat.task;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.imitatewechat.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

public class WeatherTask implements Runnable {

    private TextView mView; // the view to update
    private Handler mHandler; // the handler to send message
    private Context mContext;

    public WeatherTask(Context context,TextView view) {
        this.mView = view;
        this.mContext = context;
        this.mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                // update the UI in the main thread
                if (msg.what == 1) {
                    view.setText(MessageFormat.format(
                            mContext.getString(R.string.tab_chats_weather)
                            // 获取 R.string.tab_chats_weather 数据
                            , msg.obj));
                }
                return true;
            }
        });
    }

    @Override
    public void run() {
        // 发送get请求
        try {
            String url = "http://d1.weather.com.cn/sk_2d/101280800.html?_=" + System.currentTimeMillis();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Referer", "http://www.weather.com.cn/weather1d/101280800.shtml");
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            connection.disconnect();
            Log.d("json dump",sb.toString().substring(11));
            JSONObject json = JSONObject.parseObject(sb.substring(11));
            Message message = new Message();
            message.what = 1;
            message.obj = json.getString("temp");
            mHandler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
