package com.example.xhello.base.impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xhello.R;
import com.example.xhello.ShowActivity;
import com.example.xhello.base.BaseMenuDetailPager;
import com.example.xhello.base.impl.TabDetailPager;
import com.example.xhello.base.impl.menudetail.*;
import com.example.xhello.domain.NewsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{
    @ViewInject(R.id.vp_news_menu_detail)  //导入vp_news_menu_detail控件
    private ViewPager mViewPager;
    private ArrayList<NewsMenu.NewsTabData> children;
    private ArrayList<TabDetailPager> mPagers;

    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {  //实现构造方法
        super(activity);
        this.children = children;
    }

    @Override
    public View initViews() {
//        TextView view = new TextView(mActivity); //mActivity是父类BasePager创建的public变量
//        view.setTextSize(22);
//        view.setTextColor(Color.RED);
//        view.setGravity(Gravity.CENTER);
//        view.setText("菜单详情页-新闻");
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail,
                null);
        ViewUtils.inject(this,view);  //注入

        return view;
    }

    @Override
    public void initData() {
        //初始化12个页签对象,但是具体实现initViews和initData，需要TabDetailPager自己具体实现
        //以服务器为准
        mPagers = new ArrayList<TabDetailPager>();
        for (int i=0;i<children.size();i++){
            TabDetailPager pager = new TabDetailPager(mActivity,
                    children.get(i));
            mPagers.add(pager);  //将new出来的页签对象加入mPagers中  ArrayList<TabDetailPager> mPagers
        }
        mViewPager.setAdapter(new NewsMenuDetailAdapter());
        mIndicator.setViewPager(mViewPager);  //将viewpager和indicator关联在一起。注意：必须setAdaptec之后再关联
        mIndicator.setOnPageChangeListener(this);  //设置页签切换监听
                                                    // （当iewpager和indicator关联在一起时，事件应该设置给mIndicator）
    }
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("当前页面" + position);
        if(position==0){  //打开侧边栏
            setSlidingMenuEnable(true);
        }else{           //禁用侧边栏
            setSlidingMenuEnable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    //开启/禁用侧边栏
    private void setSlidingMenuEnable(boolean enable) {
        //获取SlidingMenu对象
        //获取MainActivity对象
        ShowActivity mainUI = (ShowActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class NewsMenuDetailAdapter extends PagerAdapter{

        //返回指示器Indicator的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).title;
        }

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagers.get(position);

            pager.initData();  //初始化对应题目页面的布局数据

            container.addView(pager.mRootView);  //获取新闻页下该题目下的根布局
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

    @OnClick(R.id.btn_next)   //通过注解的方式绑定事件。注意：在xml中配置onclick属性只适用于activity
    public void nextPage(View view){
        //跳到下一个页面
        int currentPos =  mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++currentPos);
    }

}
