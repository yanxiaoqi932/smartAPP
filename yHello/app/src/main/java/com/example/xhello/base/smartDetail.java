package com.example.xhello.base;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.xhello.GeneralCardRecognitionActivity;
import com.example.xhello.GovActivity;
import com.example.xhello.R;
import com.example.xhello.SmartActivity;
import com.example.xhello.XpicActivity;
import com.example.xhello.global.GeneralCardProcessor;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 *
 */

public class smartDetail {

    @ViewInject(R.id.yuyin)
    private Button yuyinBtn;

    @ViewInject(R.id.erweima)
    private Button erweimaBtn;

    @ViewInject(R.id.card)
    private Button cardBtn;

    public static Activity mActivity;
    public View govView;  //菜单详情页根布局
    public smartDetail(Activity activity) {
        mActivity = activity;
        govView = initViews();

        yuyinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //语音助手按键监听
                Intent intent = new Intent(mActivity, SmartActivity.class);
                mActivity.startActivity(intent);
            }
        });

        cardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //语音助手按键监听
                Intent intent = new Intent(mActivity, GeneralCardRecognitionActivity.class);
                mActivity.startActivity(intent);
            }
        });

        erweimaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //语音助手按键监听
                Intent intent = new Intent(mActivity, XpicActivity.class);
                mActivity.startActivity(intent);
            }
        });

    }

    private View initViews() {
        View view = View.inflate(mActivity, R.layout.smart_all_detail,
                null);
        ViewUtils.inject(this,view);  //注入
        return view;
    }



}
