package com.example.xhello.base;

import android.app.Activity;
import android.view.View;

import com.example.xhello.R;
import com.lidroid.xutils.ViewUtils;

/**
 *
 */

public class HomeDetail {
    public static Activity mActivity;
    public View homeView;  //菜单详情页根布局
    public HomeDetail(Activity activity) {
        mActivity = activity;
        homeView = initViews();
    }

    private View initViews() {
        View view = View.inflate(mActivity, R.layout.home_detail,
                null);
        ViewUtils.inject(this,view);  //注入
        return view;
    }
}
