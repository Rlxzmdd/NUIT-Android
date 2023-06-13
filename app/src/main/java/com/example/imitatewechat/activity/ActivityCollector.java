package com.example.imitatewechat.activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    private List<BaseActivity> activities = new ArrayList<BaseActivity>();

    public void addActivity(BaseActivity a){
        activities.add(a);
    }

    public void removeActivity(BaseActivity a){
        activities.remove(a);
    }
    public void finishAll() {
        for (BaseActivity activity: activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }
}
