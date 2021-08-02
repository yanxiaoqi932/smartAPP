package com.example.xhello.base.impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xhello.R;
import com.example.xhello.base.BaseMenuDetailPager;
import com.example.xhello.domain.PhotosBean;
import com.example.xhello.domain.PhotosBean.PhotoNews;
import com.example.xhello.global.GlobalConstants;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import utils.CacheUtils;

/**
 * 菜单详情页-组图
 */

public class PhotosMenuDetailPager extends BaseMenuDetailPager {
    @ViewInject(R.id.lv_list)
    private ListView lvlist;
    @ViewInject(R.id.gv_list)
    private GridLayout gvlist;
    public PhotosMenuDetailPager(Activity activity) {  //实现构造方法
        super(activity);
    }
    private ArrayList<PhotoNews> mPhotoList;
    @Override
    public View initViews() {
//        TextView view = new TextView(mActivity); //mActivity是父类BasePager创建的public变量
//        view.setTextSize(22);
//        view.setTextColor(Color.RED);
//        view.setGravity(Gravity.CENTER);
//        view.setText("菜单详情页-组图");
        View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail,null);
        ViewUtils.inject(this,view);

        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity,GlobalConstants.PHOTOS_URL); //读缓存
        if(!TextUtils.isEmpty(cache)){
            processData(cache);
        }
        getDataFromServer();
    }

    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);

                CacheUtils.setCache(mActivity,GlobalConstants.PHOTOS_URL,result); //写缓存
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);
        mPhotoList = photosBean.data.news;

        //给listview设置数据
        lvlist.setAdapter(new PhotosAdapter());
    }

    class PhotosAdapter extends BaseAdapter{
        private BitmapUtils mBitmapUtils;

        public PhotosAdapter(){
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(mActivity,R.layout.list_item_photo,null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            PhotoNews item = (PhotoNews) getItem(position);

            holder.tvTitle.setText(item.title);
            mBitmapUtils.display(holder.ivPic,item.listimage);
            return convertView;
        }
    }
    static class ViewHolder{
        public TextView tvTitle;
        public ImageView ivPic;
    }
}
