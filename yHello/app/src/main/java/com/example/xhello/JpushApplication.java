package com.example.xhello;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送
 */

public class JpushApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);     // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }
}
