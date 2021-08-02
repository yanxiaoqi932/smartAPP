package com.example.xhello.base;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.xhello.GovActivity;
import com.example.xhello.R;
import com.example.xhello.domain.NewsMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 *
 */

public class GovDetail {

    public static Activity mActivity;
    public View govView;  //菜单详情页根布局

    @ViewInject(R.id.buttonCSU)
    private Button csuBtn;
    @ViewInject(R.id.buttonCSUWD)
    private Button csuWdBtn;
    @ViewInject(R.id.buttonWANFANG)
    private Button wanfangBtn;
    @ViewInject(R.id.buttonCNKI)
    private Button cnkiBtn;

    public GovDetail(Activity activity){
        mActivity = activity;
        govView = initViews();

        csuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, GovActivity.class);
                intent.putExtra("url", "http://www.csu.edu.cn");  //传递网页链接
                mActivity.startActivity(intent);
            }
        });
        csuWdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, GovActivity.class);
                intent.putExtra("url", "http://wl.csu.edu.cn");  //传递网页链接
                mActivity.startActivity(intent);
            }
        });
        wanfangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, GovActivity.class);
                intent.putExtra("url", "http://www.wanfangdata.com.cn");  //传递网页链接
                mActivity.startActivity(intent);
            }
        });
        cnkiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, GovActivity.class);
                intent.putExtra("url", "https://www.cnki.net");  //传递网页链接
                mActivity.startActivity(intent);
            }
        });
    }
    private View initViews() {
        View view = View.inflate(mActivity, R.layout.gov_detail,
                null);
        ViewUtils.inject(this,view);  //注入
        return view;
    }
}
