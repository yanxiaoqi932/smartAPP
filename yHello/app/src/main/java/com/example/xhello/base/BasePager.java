package com.example.xhello.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xhello.R;
import com.example.xhello.ShowActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 5个标签页的基类
 */

public class BasePager {

    public Activity mActivity;
    public TextView tvTitle;
    public ImageButton btnMenu;
    public FrameLayout flContainer;  //空的帧布局，由子类动态填充

    public View mRootView;  //当前页面根布局

    public BasePager(Activity activity){
        mActivity = activity;
        mRootView = initViews();  //在页面创建时初始化了布局，
        // 这个布局是五个布局的共性布局（如果没有共性则不需要在父类实现initView，直接abstract让子类分别实现即可，例如fragment包）
    }
    //初始化布局
    public View initViews(){
        //inflate()的作用就是将一个用xml定义的布局文件查找出来，
        // 注意与findViewById()的区别，inflate是加载一个布局文件，而baifindViewById则是从布局文件中查找一个控件。
        View view = View.inflate(mActivity, R.layout.base_pager,null);  //加载布局文件base_pager
        tvTitle = (TextView) view.findViewById(R.id.tv_title);  //获取布局文件中的标题
        btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);  //获取布局文件中的按键
        flContainer = (FrameLayout) view.findViewById(R.id.fl_container);  //获取布局文件中的帧布局

        //点击菜单按钮，自动控制侧边栏开关
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击选择侧边栏菜单后，自动打开或关闭侧边栏
                toggle();
            }
        });

        return view;
    }

    //改变侧边栏开关状态
    protected void toggle(){
        ShowActivity mainUI = (ShowActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();//获取侧边栏对象
        slidingMenu.toggle();  //如果当前为开，则关；反之亦然
    }

    //初始化数据
    public void initData(){

    }
}
