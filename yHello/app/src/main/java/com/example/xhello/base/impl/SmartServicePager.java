package com.example.xhello.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.xhello.NewsDetailActivity;
import com.example.xhello.SmartActivity;
import com.example.xhello.base.BasePager;
import com.example.xhello.base.GovDetail;
import com.example.xhello.base.smartDetail;

/**
 * 智慧服务
 */

public class SmartServicePager extends BasePager {
    private smartDetail mpager;
    public SmartServicePager(Activity activity) {
        super(activity);

    }

    @Override
    public void initData() {


        mpager = new smartDetail(mActivity);
        flContainer.removeAllViews();  //清除之前帧布局显示的内容
        flContainer.addView(mpager.govView);  //govView是BaseMenuDetailPager的根布局
    }
}
