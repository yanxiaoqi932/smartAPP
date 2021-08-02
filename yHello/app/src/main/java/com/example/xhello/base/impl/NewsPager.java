package com.example.xhello.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xhello.ShowActivity;
import com.example.xhello.base.BaseMenuDetailPager;
import com.example.xhello.base.BasePager;
import com.example.xhello.base.impl.menudetail.InteractMenuDetailPager;
import com.example.xhello.base.impl.menudetail.NewsMenuDetailPager;
import com.example.xhello.base.impl.menudetail.PhotosMenuDetailPager;
import com.example.xhello.base.impl.menudetail.TopicMenuDetailPager;
import com.example.xhello.domain.NewsMenu;
import com.example.xhello.fragment.LeftMenuFragment;
import com.example.xhello.global.GlobalConstants;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import utils.CacheUtils;

/**
 * 新闻
 */

public class NewsPager extends BasePager {
    private NewsMenu mNewsMenu;
    private ArrayList<BaseMenuDetailPager> mPagers;  //侧边栏主题对应的四个具体对象的集合

    public NewsPager(Activity activity) {
        super(activity);

    }

    @Override
    public void initData() {
        //给空的帧布局动态添加布局图像
        System.out.println("新闻中心初始化了...");
//        TextView view = new TextView(mActivity); //mActivity是父类BasePager创建的public变量
//        view.setTextSize(22);
//        view.setTextColor(Color.RED);
//        view.setGravity(Gravity.CENTER);
//        view.setText("新闻");
//
//        flContainer.addView(view); //将做好的view加入flContainer帧布局中

        //修改标题
        tvTitle.setText("新闻");

        //读缓存
        String cache = CacheUtils.getCache(mActivity,GlobalConstants.CATEGORY_URL);
        if(!TextUtils.isEmpty(cache)){  //如果cache不为空
            System.out.println("发现缓存...");
            //有缓存则对cache数据进行解析
            processData(cache);
        }  //没有缓存则从服务器获取数据
//        else {
//            getDataFromServer();
//        }
        //继续请求服务器数据，保证缓存更新，如果没有请求到服务器，则不会进行更新
        getDataFromServer();

    }

    //从服务器获取数据
    private void getDataFromServer(){
        //XUtils
        HttpUtils utils = new HttpUtils();
        //原生模拟器使用10.0.2.2
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {
            //CATEGORY_URL代表一个链接
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("服务器分类数据：" + result);
                //解析json，获取四个页面的对象和信息
                processData(result);
                //通过网络获取数据后，写缓存到本地
                CacheUtils.setCache(mActivity,GlobalConstants.CATEGORY_URL,result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show(); //展示错误信息
            }
        });
    }

    //public:可以被项目中所有地方调用
    //protected:可以被文件中同级别private或protected方法调用
    //private:只可以被文件中public方法调用
    protected void processData(String json){   //用于解析数据
        Gson gson = new Gson();
        //通过json和对象类，生成一个对象
        mNewsMenu = gson.fromJson(json,NewsMenu.class);  //NewsMenu中包含json对象的写法
        System.out.println("解析结果：" + mNewsMenu);

        //新闻中心找到侧边栏对象
        ShowActivity mainUI = (ShowActivity) mActivity;
        LeftMenuFragment fragment = mainUI.getLeftMenuFragment();
        fragment.setMenuData(mNewsMenu.data);

        //网络请求成功后，初始化四个菜单详情页
        mPagers = new ArrayList<BaseMenuDetailPager>();
        //页面集合mPagers依次加载四个页面的详情页
        mPagers.add(new NewsMenuDetailPager(mActivity,
                mNewsMenu.data.get(0).children));  //通过构造方法传递数据
        mPagers.add(new TopicMenuDetailPager(mActivity));
        mPagers.add(new PhotosMenuDetailPager(mActivity));
        mPagers.add(new InteractMenuDetailPager(mActivity));

        //设置侧边栏新闻主题页面为默认页面
        setMenuDetailPager(0);
    }

    //修改菜单详情页,该函数被LeftMenuFragment所调用，监听到按键事件时调用该函数更新页面
    public void setMenuDetailPager(int position) {
        System.out.println("新闻中心要修改菜单详情页:" + position);

        BaseMenuDetailPager pager = mPagers.get(position);  //获取四个侧边栏主题对应的布局

        //清除之前帧布局显示的内容
        flContainer.removeAllViews();
        //四个页面内容写成一个基类和多个类实现
        //修改新闻中心的帧布局
        flContainer.addView(pager.mRootView);  //mRootView是BaseMenuDetailPager的根布局，覆盖在新闻页面的帧布局上
        //初始化当前页面的数据
        pager.initData();
        //修改标题栏
        tvTitle.setText(mNewsMenu.data.get(position).title);
    }
}
