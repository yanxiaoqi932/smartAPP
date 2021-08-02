package com.example.xhello;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.xhello.fragment.ContentFragment;
import com.example.xhello.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import com.example.xhello.fragment.ContentFragment;
import com.example.xhello.fragment.LeftMenuFragment;

/*
*1.当一个activity要展示Fragment的话，必须继承FragmentActivity
* 2.使用
 */
public class ShowActivity extends SlidingFragmentActivity {

    private static final String TAG_LEFT_MENU="TAG_LEFT_MENU";
    private static final String TAG_CONTENT="TAG_CONTENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        //设置侧边栏布局
        setBehindContentView(R.layout.menu);

        SlidingMenu slidingMenu=getSlidingMenu();//获得SlidingMenu对象
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏点击
        slidingMenu.setBehindOffset(500);//预留屏幕大小

        initFragment();  //将上述两个主次布局用fragment替换
    }

    //初始化Fragment
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//开始事务
        //用fragment替换帧布局,参1是帧布局容器的id，参2是要替换的fragment类,参3是标记
        transaction.replace(R.id.fl_content, new ContentFragment(),TAG_CONTENT);   //打一个标记，方便以后找到Fragment对象
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),TAG_LEFT_MENU);
        transaction.commit();//提交事务

        //Fragment tag = fm.findFragmentByTag(TAG_left_menu);根据标记查找fragment对象

        //通过tag找到fragment的方法
        //ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
    }
    //获取侧边栏fragment对象
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);  //获取侧边栏标志
        return  fragment;
    }

    //获取主页fragment对象
    public ContentFragment getContentFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);  //获取主页标志，从而得到主页fragment对象
        return  fragment;
    }


}
