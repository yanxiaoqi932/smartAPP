package com.example.xhello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import utils.PrefUtils;



public class GuideActivity extends Activity {
    private int mPointDis;// 小红点移动距离
    private ViewPager mViewPager;
    private LinearLayout llContainer;
    private ImageView ivRedPoint;
    private Button btnStart;
    //图片集合
    private int[] mImageIds = new int[]{R.drawable.guide_1,
            R.drawable.guide_2,R.drawable.guide_3};
    private ArrayList<ImageView> mImageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initViews(); //初始化布局
        initData();  //初始化数据
    }

    //初始化布局
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        btnStart = (Button) findViewById(R.id.btn_start);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
    }

    //初始化数据
    private void initData() {
        mImageViews = new ArrayList<ImageView>();

        //开始放置三张引导图片以及三个点
        for(int i=0;i<mImageIds.length;i++){
            //初始化图片
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            mImageViews.add(view);     //将三张图片依次放入mImageViews列表中

            //初始化小圆点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.shape_point_normal);  //生成小灰点
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);   //设置小灰点间隔  LinearLayout.LayoutParams用于设置线性布局的宽和高
            if(i>0){
                params.leftMargin = 10;  //从第二个点开始设置左边距10px
            }
            point.setLayoutParams(params);
            llContainer.addView(point);
        }

        mViewPager.setAdapter(new GuideAdapter());  //将GuideAdapter类中操作放入ViewPager中运行（ViewPager指闪屏页面）


        //监听ViewPager滑动事件,更新小红点位置
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == mImageIds.length - 1) {
                    //最后一个页面显示开始体验按钮
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    //非最后一个页面则隐藏
                    btnStart.setVisibility(View.GONE);
                }
            }

            //监听滑动事件
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                System.out.println("当前位置:" + position + ";偏移百分比:"
                        + positionOffset);

                //通过修改小红点的左边距来更新小红点的位置
                int leftMargin = (int) (mPointDis * positionOffset + position
                        * mPointDis + 0.5f);//要将当前的位置信息产生的距离加进来
                //获取小红点的布局参数
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint
                        .getLayoutParams();
                params.leftMargin = leftMargin;
                ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //计算圆点移动距离 = 第二个圆点的左边距-第一个圆点左边距
        //		mPointDis = llContainer.getChildAt(1).getLeft()
        //				- llContainer.getChildAt(0).getLeft();
        //		System.out.println("mPointDis:" + mPointDis);
        //		System.out.println("mPointDis1:" + llContainer.getChildAt(1).getLeft());
        //		System.out.println("mPointDis2:" + llContainer.getChildAt(0).getLeft());
        //measure->layout->draw, 必须onCreate执行结束之后,才会开始绘制布局,走此三个方法

        //监听layout执行结束的事件, 一旦结束之后, 在去获取当前的left位置
        //视图树
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    //一旦视图树的layout方法调用完成, 就会回调此方法
                    @Override
                    public void onGlobalLayout() {
                        //布局位置已经确定,可以拿到位置信息了
                        mPointDis = llContainer.getChildAt(1).getLeft()
                                - llContainer.getChildAt(0).getLeft();
                        System.out.println("mPointDis:" + mPointDis);

                        //移除观察者
                        //ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ivRedPoint.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });

        //开始体验按钮点击
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //在sp中记录访问过引导页的状态
                PrefUtils.putBoolean(getApplicationContext(), "is_guide_show",
                        true);
                //跳到主要展示页面
                startActivity(new Intent(getApplicationContext(),
                        ShowActivity.class));
                finish();
            }
        });
    }



    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        //取出mImageViews，放入类的容器中
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view=mImageViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
