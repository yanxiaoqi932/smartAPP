package com.example.xhello.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止滑动的ViewPager
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NoScrollViewPager(Context context){
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //重写父类的onTouchEvent方法，此处直接为true，什么都不做，从而达到禁用的目的
        return true;
    }

    //时间中断
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //true表示拦截，false表示不拦截，传给子控件（使newspager布局可以滑动）
        return false;  //标签页的viewpager不拦截新闻菜单详情页页签的viewpager
    }
}
