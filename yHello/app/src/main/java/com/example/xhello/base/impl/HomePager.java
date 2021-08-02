package com.example.xhello.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.xhello.base.BasePager;
import com.example.xhello.base.HomeDetail;
import com.example.xhello.base.smartDetail;

/**
 * 首页布局
 */

public class HomePager extends BasePager {
    private HomeDetail mpager;
    public HomePager(Activity activity) {
        super(activity);

    }

    @Override
    public void initData() {
        System.out.println("主页初始化了...");

        mpager = new HomeDetail(mActivity);
        flContainer.removeAllViews();  //清除之前帧布局显示的内容
        flContainer.addView(mpager.homeView);  //govView是BaseMenuDetailPager的根布局
        //修改标题
        tvTitle.setText("百事通");
        btnMenu.setVisibility(View.GONE); //隐藏标题栏按钮
    }
}
