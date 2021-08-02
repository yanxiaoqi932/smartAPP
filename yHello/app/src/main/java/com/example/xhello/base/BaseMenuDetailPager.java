package com.example.xhello.base;

import android.app.Activity;
import android.view.View;

/**
 * 四个菜单详情页基类
 */

public abstract class BaseMenuDetailPager {
    public static Activity mActivity;
    public View mRootView;  //菜单详情页根布局

    //首先写布局基类的构造方法
    public BaseMenuDetailPager(Activity activity){
        mActivity = activity;
        mRootView = initViews();
    }

    public abstract View initViews();  //四个页面布局没有共同点，只能改成抽象类，交给子类去具体实现

    public void initData(){

    }
}
