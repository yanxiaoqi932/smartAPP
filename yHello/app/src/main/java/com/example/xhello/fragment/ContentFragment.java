package com.example.xhello.fragment;

import android.support.v4.view.PagerAdapter;

import com.example.xhello.ShowActivity;
import com.example.xhello.View.NoScrollViewPager;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.xhello.R;
import com.example.xhello.base.BasePager;
import com.example.xhello.base.impl.GovAffairsPager;
import com.example.xhello.base.impl.HomePager;
import com.example.xhello.base.impl.NewsPager;
import com.example.xhello.base.impl.SmartServicePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 主页面栏
 */

public class ContentFragment extends BaseFragment {

    //采用注释的方法运行xutils框架,可以直接将vp_content控件赋给mViewPager变量，简化了代码
    @ViewInject(R.id.vp_content)
    private NoScrollViewPager mViewPager;
    @ViewInject(R.id.rg_group)      //同上
    private RadioGroup rgGroup;

    private ArrayList<BasePager> mList; //五个标签页的集合



    @Override
    public View initViews() {   //重写BaseFragment的initViews函数
        View view = View.inflate(mActivity, R.layout.fragment_content_menu,null);
        ViewUtils.inject(this,view);  //注入view和事件进入xutils框架

//        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);  采用xutils框架后，不需要再用代码为控件赋值了
//        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        //初始化五个标签页面对象
        mList = new ArrayList<BasePager>();
        mList.add(new HomePager(mActivity));  //添加首页
        mList.add(new NewsPager(mActivity));
        mList.add(new SmartServicePager(mActivity));
        mList.add(new GovAffairsPager(mActivity));
        mViewPager.setAdapter(new ConterntAdapter());  //运行pager滑屏布局

        //监听按钮RadioGroup切换事件，如果发生切换事件，则跳入该函数执行操作
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        //如果发现当前页面为首页，则设置mViewPager的item为0，从而进入第一个页面
                        mViewPager.setCurrentItem(0,false); //false代表取消切换页面时平滑滑动
                        break;
                    case R.id.rb_news:
                        //同理
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.rb_smart:
                        //同理
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.rb_gov:
                        //同理
                        mViewPager.setCurrentItem(3,false);
                        break;
                    default:
                        break;
                }
            }
        });

        //监听页面ViewPager切换事件
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                BasePager pager = mList.get(position);
                pager.initData();  //仅加载对应页面的页面数据，节省程序运行pager压力

                if(position==0 || position==mList.size()-1){  //如果是主页或设置页面，禁用侧边栏
                    setSlidingMenuEnable(false);
                }
                else{
                    //否则启用侧边栏
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //手动初始化第一个图片的页面数据
        mList.get(0).initData();
        //手动禁用第一个页面的侧边栏
        setSlidingMenuEnable(false);
    }



    //重写PagerAdapter类
    class ConterntAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //确定当前页面是那个页面,然后生成该页面的pager对象
            BasePager pager = mList.get(position);
            System.out.println("即将实现一个根布局对象");
            //实现根布局对象  pager.mRootView：当前页面根布局，mRootView在BasePager中已经public定义
            container.addView(pager.mRootView);
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //开启/禁用侧边栏的方法
    private void setSlidingMenuEnable(boolean enable) {
        //获取SlidingMenu对象 -》 获取ShowActivity的对象
        ShowActivity mainUI = (ShowActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if(enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);  //禁用侧边栏
        }
    }

    //获取新闻中心对象
    public NewsPager getNewsCenterPager() {
        NewsPager pager = (NewsPager) mList.get(1);//新闻中心在集合的第二个位置添加
        return pager;
    }
}
