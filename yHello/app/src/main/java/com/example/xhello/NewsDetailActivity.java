package com.example.xhello;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
*新闻详情页
 **/

public class NewsDetailActivity extends Activity implements View.OnClickListener{
    @ViewInject(R.id.btn_back)
    private ImageButton btnBack;
    @ViewInject(R.id.btn_menu)
    private ImageButton btnMenu;
    @ViewInject(R.id.ll_control)
    private LinearLayout llControl;
    @ViewInject(R.id.btn_textsize)
    private ImageButton btnTextSize;
    @ViewInject(R.id.btn_share)
    private ImageButton btnShare;
    @ViewInject(R.id.pb_loading)
    private ProgressBar pbLoading;

    @ViewInject(R.id.webview)
    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);

        ViewUtils.inject(this);
        initViews();

        //获取webview的设置对象
        WebSettings settings = mWebView.getSettings();
        //settings.setJavaScriptEnabled(true);  //启用js功能
        settings.setBuiltInZoomControls(true);  //显示放大缩小按钮
        settings.setUseWideViewPort(true);  //双击缩放

        //给webview设置监听
        mWebView.setWebViewClient(new WebViewClient(){
            //页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbLoading.setVisibility(View.VISIBLE);
                System.out.println("开始加载....");
            }

            //跳转连接
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转链接...." + url);
                //所有跳转强制在当前webview加载，不跳浏览器
                mWebView.loadUrl(url);
                return true;
            }

            //加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("加载结束....");
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                System.out.println("网页标题" + title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                System.out.println("网页进度" + newProgress);
            }
        });

        String url = getIntent().getStringExtra("url");  //接收showactivity页面传递的intent信息"url"
        //开始加载网页
        mWebView.loadUrl(url);
    }

    private void initViews() {
        btnBack.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);
        llControl.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);
    }

    //拦截物理返回键，让网页返到上一个页面
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){  //判断是否可以返回
            mWebView.goBack();  //返回上一页
        }else{
            finish();  //销毁页面
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
                showChooseDialog();
                break;
            case R.id.btn_share:
                break;
            default:
                break;
        }
    }

    private int mTempWhich;
    private  int mCurrentWhich = 2;  //当前选中的字体位置
    //显示选择字体的弹窗
    private void showChooseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};

        //显示单选框
        builder.setSingleChoiceItems(items,mCurrentWhich,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("which:" + which);
                mTempWhich = which;

                WebSettings settings = mWebView.getSettings();
                switch (mTempWhich) {
                    case 0:
                        //超大号
                        settings.setTextSize(TextSize.LARGEST);
                        //settings.setTextZoom(20);
                        break;
                    case 1:
                        //大号
                        settings.setTextSize(TextSize.LARGER);
                        break;
                    case 2:
                        //正常
                        settings.setTextSize(TextSize.NORMAL);
                        break;
                    case 3:
                        //小号
                        settings.setTextSize(TextSize.SMALLER);
                        break;
                    case 4:
                        //超小号
                        settings.setTextSize(TextSize.SMALLEST);
                        break;

                    default:
                        break;
                }
                mCurrentWhich = mTempWhich;  //更新字体选中位置
            }
        });

        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog,int which) {

            }
        });

        builder.setNegativeButton("取消",null);

        builder.show();
    }

}
