package com.example.xhello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.List;

import utils.PrefUtils;

public class MainActivity extends Activity {
    private RelativeLayout rlRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

        //旋转动画0-360度
        RotateAnimation animRotate=new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);

        animRotate.setDuration(1000);//动画时间
        animRotate.setFillAfter(true);//保持动画结束状态

        //缩放动画0-360度
        ScaleAnimation animScale=new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animScale.setDuration(1000);
        animScale.setFillAfter(true);

        //渐变动画0-360度
        AlphaAnimation animAlpha=new AlphaAnimation(0,1);
        animAlpha.setDuration(2000);
        animAlpha.setFillAfter(true);

        //动画集合
        AnimationSet set=new AnimationSet(true);
        set.addAnimation(animRotate);
        set.addAnimation(animScale);
        set.addAnimation(animAlpha);

        //启动动画
        rlRoot.startAnimation(set);

        //启动动画之后开始监听
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //判断是否展示过引导页
                boolean isGuideShow = PrefUtils.getBoolean(getApplicationContext(),"is_guide_show",false);
                if(!isGuideShow)  //如果没有展示过引导页
                {
                    //动画结束，跳到新手引导
                    startActivity(new Intent(getApplicationContext(),GuideActivity.class));
                }else{
                    //直接跳到主要展示页面
                    startActivity(new Intent(getApplicationContext(),ShowActivity.class));
                }
                finish();
            }
        });

    }

}