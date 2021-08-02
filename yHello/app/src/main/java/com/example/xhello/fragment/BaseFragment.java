package com.example.xhello.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment基类，意思是碎片
 */
//BaseFragment作为fragment对象的父类,其子类有侧边栏和主页面栏
public abstract class BaseFragment extends Fragment {

    public Activity mActivity;
    public View mRootView;

    //fragment创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();  //获取fragment所依赖的activity对象，即ShowActivity
    }

    //初始化fragment布局
    //Acttivity设置布局：直接onCreate-》setContentView即可；
    //fragment设置布局：必须在onCreateView函数中调用自定义的生成布局函数，并返回一个根布局的View对象
    //                  而且父类和布局函数都应该写成抽象类，因为抽象类只能由子类来实现具体布局函数
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = initViews();
        return mRootView;
    }

    //fragment的activity创建完成
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();   //布局完全生成后初始化其数据
    }

    //初始化布局，抽象类型，必须由子类实现
    public abstract View initViews();

    //初始化数据，子类可以不实现
    public void initData(){

    }
}


