package com.example.xhello;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 *
 */

public class GovActivity extends Activity {


    @ViewInject(R.id.webview)
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gov_detail);

        ViewUtils.inject(this);

        //获取webview的设置对象
        WebSettings settings = mWebView.getSettings();
        //settings.setJavaScriptEnabled(true);  //启用js功能
        settings.setBuiltInZoomControls(true);  //显示放大缩小按钮
        settings.setUseWideViewPort(true);  //双击缩放

        String url = getIntent().getStringExtra("url");  //接收showactivity页面传递的intent信息"url"
        //开始加载网页
        mWebView.loadUrl(url);

    }

}
