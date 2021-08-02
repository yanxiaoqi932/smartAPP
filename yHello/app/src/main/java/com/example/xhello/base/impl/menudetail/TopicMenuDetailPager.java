package com.example.xhello.base.impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.xhello.base.BaseMenuDetailPager;

/**
 * 菜单详情页-专题
 */

public class TopicMenuDetailPager extends BaseMenuDetailPager {
    public TopicMenuDetailPager(Activity activity) {  //实现构造方法
        super(activity);
    }

    @Override
    public View initViews() {
        TextView view = new TextView(mActivity); //mActivity是父类BasePager创建的public变量
        view.setTextSize(22);
        view.setTextColor(Color.RED);
        view.setGravity(Gravity.CENTER);
        view.setText("菜单详情页-专题");
        return view;
    }
}
