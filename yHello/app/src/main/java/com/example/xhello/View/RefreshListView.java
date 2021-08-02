package com.example.xhello.View;

import com.example.xhello.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 下拉刷新listview
 */

public class RefreshListView extends ListView {

    private View mHeaderView;
    private int mHeaderViewHeight;
    private int startY = -1;
    private int endY;
    private static final int STATE_PULL_TO_REFRESH = 0;  //下拉刷新状态
    private static final int STATE_RELEASE_TO_REFRESH = 1;  //松开刷新状态
    private static final int STATE_REFRESHING = 2;  //正在刷新状态

    private int mCurrentState = STATE_PULL_TO_REFRESH;  //当前状态，默认下拉刷新

    private TextView tvState;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pbLoading;

    public RefreshListView(Context context) {
        this(context,null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
    }


    //初始化头布局
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(),
                R.layout.pull_to_refresh_hea, null);
        addHeaderView(mHeaderView);//给ListView添加头布局

        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        pbLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);


        //隐藏头布局
        //获取当前头布局高度，然后设置负paddingTop，布局就会往上走
        mHeaderView.measure(0,0);  //手动测量
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                endY = (int) ev.getY();
                int dy = endY - startY;

                if (startY==-1) { //没有获取到按下的事件
                    startY = (int) ev.getY();  //重新获取起点位置
                }

                int firstVisiblePosition = this.getFirstVisiblePosition();  //当前显示的第一个item的位置
                if(dy>0 && firstVisiblePosition==0){
                    //下拉动作&&在listview顶部
                    int padding = -mHeaderViewHeight + dy;
                    if(padding>0 && firstVisiblePosition == 0){
                        //切换到松开刷新的状态
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    }else if(padding <= 0){
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }

                    mHeaderView.setPadding(0,padding,0,0);

                    return true;  //消费此事件，处理下拉控件滑动，不需要listview原生效果参与
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if(mCurrentState == STATE_RELEASE_TO_REFRESH){
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                }else if(mCurrentState == STATE_PULL_TO_REFRESH){
                    //隐藏刷新控件
                    mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    //根据当前状态状态刷新界面
    private void refreshState() {
        switch (mCurrentState){
            case STATE_PULL_TO_REFRESH:

                break;
            case STATE_RELEASE_TO_REFRESH:

                break;
            case STATE_REFRESHING:

                break;
            default:
                break;
        }
    }
}
