package com.example.xhello.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xhello.GovActivity;
import com.example.xhello.NewsDetailActivity;
import com.example.xhello.R;
import com.example.xhello.base.BasePager;
import com.example.xhello.base.GovDetail;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 政务
 */

public class GovAffairsPager extends BasePager {

    private GovDetail mpager;

    @ViewInject(R.id.buttonCSU)
    private Button csuBtn;

    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        mpager = new GovDetail(mActivity);
        flContainer.removeAllViews();  //清除之前帧布局显示的内容
        flContainer.addView(mpager.govView);  //govView是BaseMenuDetailPager的根布局
    }

//    public void csu(View view) {
//        Intent intent = new Intent(mActivity, GovActivity.class);
//        intent.putExtra("url", "http://www.csu.edu.cn");  //传递网页链接
//        mActivity.startActivity(intent);
//    }


}
