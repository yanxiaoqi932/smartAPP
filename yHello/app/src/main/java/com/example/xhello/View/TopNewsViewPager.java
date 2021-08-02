package com.example.xhello.View;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 头条新闻的viewpager
 */

public class TopNewsViewPager extends ViewPager{
    int startX;int startY;
    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //上下滑动，需要拦截
    //左滑拦截最后一个页面
    //右滑拦截第一个页面
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求父控件不要拦截
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int dx = endX-startX;
                int dy = endY-startY;
                if(Math.abs(dx)>Math.abs(dy)){
                    //左右滑动
                    int currtentItem = getCurrentItem();
                    if(dx>0){
                        //右滑
                        if(currtentItem==0){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else {
                        //左滑
                        int count = getAdapter().getCount();
                        if(currtentItem == count-1){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else{
                    //上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
