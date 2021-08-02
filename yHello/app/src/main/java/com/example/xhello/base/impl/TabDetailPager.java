package com.example.xhello.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xhello.NewsDetailActivity;
import com.example.xhello.R;
import com.example.xhello.View.RefreshListView;
import com.example.xhello.View.TopNewsViewPager;
import com.example.xhello.base.BaseMenuDetailPager;
import com.example.xhello.domain.NewsMenu.NewsTabData;
import com.example.xhello.domain.NewsTab;
import com.example.xhello.global.GlobalConstants;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.PreferenceInject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import utils.CacheUtils;
import utils.PrefUtils;

/**
 * 页签详情页BaseMenuDetailPager（北京，中国，国际...），从代码角度来讲比较简洁
 * 但当前页不属于菜单详情页，这是个干爹
 */

public class TabDetailPager extends BaseMenuDetailPager {
    private NewsTabData newsTabData;
    private TextView view;
    ArrayList<NewsTab.TopNews> mTopNewsList;
    BitmapUtils mBitmapUtils;

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;

    @ViewInject(R.id.vp_tab_detail)
    private TopNewsViewPager mViewpager;
    private String mUrl;

    @ViewInject(R.id.lv_list)
    private RefreshListView lvList;

    private ArrayList<NewsTab.News> mNewsList;

    public TabDetailPager(Activity activity, NewsTabData newsTabData){   //构造函数
        super(activity);    //super指的是父类的构造函数，这里它会调用initViews()函数
                            //同时，super必须是子类第一个调用的函数，因为子类必须优先调用父类的构造函数
        this.newsTabData = newsTabData;
        mUrl = GlobalConstants.SERVERL_URL + newsTabData.url;
    }

    @Override
    public View initViews() {
//        view = new TextView(mActivity); //mActivity是父类BasePager创建的public变量
//        view.setTextSize(22);
//        view.setTextColor(Color.RED);
//        view.setGravity(Gravity.CENTER);

        View view = View.inflate(mActivity,R.layout.pager_tab_detail,null);

        //加载头条新闻头布局
        View headerView = View.inflate(mActivity,R.layout.list_item_header,null);

        ViewUtils.inject(this,view);
        ViewUtils.inject(this,headerView);

        lvList.addHeaderView(headerView);//给listview添加头布局
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity,mUrl);  //缓存对应页签下的数据
        if (!TextUtils.isEmpty(cache)){
            processData(cache);
        }
        getDataFormServer();
    }

    //请求页签获取数据
    private void getDataFormServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.SERVERL_URL + newsTabData.url, new RequestCallBack<String>() {
            //进入SERVERL_URL + newsTabData.url的网址，获取对应页签的json数据
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);

                CacheUtils.setCache(mActivity,mUrl,result);  //写缓存
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show(); //打印错误日志

            }
        });
    }

    protected void processData(String result){
        Gson gson = new Gson();
        NewsTab newsTab = gson.fromJson(result,NewsTab.class);
        System.out.println("页签结果" + newsTab);

        mTopNewsList = newsTab.data.topnews;
        if(mTopNewsList!=null){
            mViewpager.setAdapter(new TopNewsAdapter());

            mIndicator.setViewPager(mViewpager); //将圆形指示器和Viewpager绑定
            mIndicator.setSnap(true);  //快照展示方式
            mIndicator.onPageSelected(0);//将圆点位置归0, 保证圆点和页面同步
            mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    //更新头条新闻标题
                    tvTitle.setText(mTopNewsList.get(position).title);
                }

                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });

            //初始化第一页头条新闻标题
            tvTitle.setText(mTopNewsList.get(0).title);
        }

        //初始化新闻列表数据
        mNewsList = newsTab.data.news;
        if (mNewsList != null){
            lvList.setAdapter(new NewsAdapter());
        }
    }

    //头条新闻数据适配器
    class TopNewsAdapter extends PagerAdapter{

        public TopNewsAdapter(){
            mBitmapUtils = new BitmapUtils(mActivity);  //加载图片的工具类
            //mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default); //首先加载默认图片
        }
        @Override
        public int getCount() {
            return mTopNewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);

            NewsTab.TopNews topnews = mTopNewsList.get(position);
            String topimage = topnews.topimage;  //图片下载链接

            //1.根据url下载图片
            //2.将图片设置给ImageView
            //3.图片缓存
            //4.避免内存溢出
            //BitmapUtils：xutils中专用于图片下载的函数
            mBitmapUtils.display(view,topimage);
            view.setScaleType(ImageView.ScaleType.FIT_XY);  //设置缩放模式，图片宽高匹配窗体
            container.addView(view);

            //设置点击事件
            lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //头布局也算，所以使用position时要减掉头布局个数
                    int headerViewsCount = lvList.getHeaderViewsCount();
                    position-=headerViewsCount;

                    System.out.println("当前点击位置:" + position);

                    NewsTab.News news = mNewsList.get(position);

                    //标记已读未读：将已存新闻id保存在sp中
                    //读取现有ID
                    String readIds = PrefUtils.getString(mActivity,"read_ids","");

                    //判断之前是否保存过该id
                    if(!readIds.contains(news.id)){
                        //在现有ID的基础上追加新的id
                        readIds = readIds + news.id + ",";
                        System.out.println(news.id);
                        //保存最新的ids
                        PrefUtils.putString(mActivity,"read_ids",readIds);
                    }
                    //局部刷新
                    TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                    tvTitle.setTextColor(Color.GRAY);

                    //跳到新闻详情页
                    Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                    if(news.id.equals("35311")){
                        System.out.println("第一"+news.id);
                        intent.putExtra("url","http://172.17.67.241:8080/zhbj/10007/724D6A55496A11726628.html");  //传递网页链接
                    }else if(news.id.equals("35312")){
                        System.out.println("第二"+news.id);
                        intent.putExtra("url","http://172.17.67.241:8080/zhbj/10110/724D6A55496A11726629.html");
                    }else{
                        System.out.println("其它"+news.id);
                        intent.putExtra("url","http://172.17.67.241:8080/zhbj/10110/724D6A55496A11726629.html");
                    }

                    mActivity.startActivity(intent);
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

    //新闻列表适配器
    class NewsAdapter extends BaseAdapter{
        private BitmapUtils mBitmapUtils;
        public NewsAdapter(){
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);  //加载默认图片
        }
        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(mActivity,R.layout.list_item_news,null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            NewsTab.News info = (NewsTab.News) getItem(position);

            holder.tvTitle.setText(info.title);

            mBitmapUtils.display(holder.ivIcon,info.listimage);

            //判断已读未读
            String readIds = PrefUtils.getString(mActivity,"read_ids","");
            if(readIds.contains(info.id)){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }

    static class ViewHolder{
        public ImageView ivIcon;
        public TextView tvTitle;
    }
}
