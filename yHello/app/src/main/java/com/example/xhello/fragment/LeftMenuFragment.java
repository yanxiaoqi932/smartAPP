package com.example.xhello.fragment;
import com.example.xhello.R;
import com.example.xhello.ShowActivity;
import com.example.xhello.base.BasePager;
import com.example.xhello.base.impl.NewsPager;
import com.example.xhello.domain.NewsMenu.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * 侧边栏fragment
 */

public class LeftMenuFragment extends BaseFragment {
    //子类LeftMenuFragment必须实现BaseFragment的抽象类函数，即initViews()
    @ViewInject(R.id.lv_menu)
    private ListView lvList;
    private int mCurrentPos; //记录当前选中菜单的位置
    private  ArrayList<NewsMenuData> data;  //分类的网络数据
    LeftMenuAdapter mAdapter;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity,R.layout.fragment_left_menu,null);
        ViewUtils.inject(this,view);
        return view;
    }

    //设置侧边栏数据的方法
    //通过该方法，可以将新闻中心所得数据传递到侧边栏
    public void setMenuData(ArrayList<NewsMenuData> data) {
        System.out.println("侧边栏已获取数据" + data.size());

        //当从菜单详情页转回新闻页时，出现侧边栏选中位置和菜单详情页不同步，
        // 所以每次都要将当前选中位置归零
        mCurrentPos = 0;

        this.data = data;   //将数据赋给侧边栏
        mAdapter = new LeftMenuAdapter();
        lvList.setAdapter(mAdapter);  //运行pager页面
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position; //更新当前点击位置
                mAdapter.notifyDataSetChanged(); //刷新ListView的Adapter

                //点击选择侧边栏菜单后，自动收回侧边栏
                toggle();

                //修改新闻中心帧布局
                setMenuDetailPager(position);
            }
        });
    }

    //修改菜单详情页
    protected void setMenuDetailPager(int position){
        //修改新闻中心帧布局
        //***获取新闻中心对象：侧边栏fragment -》 ShowActivity -》 NewsPager新闻Content页面***
        ShowActivity mainUI = (ShowActivity) mActivity;
        ContentFragment fragment = mainUI.getContentFragment();  //获取了contentFragment
        NewsPager pager = fragment.getNewsCenterPager();  //利用contentFragment获取了获取了新闻中心对象
        pager.setMenuDetailPager(position);  //修改新闻中心对象的页面
    }

    //改变侧边栏开关状态
    protected void toggle(){
        ShowActivity mainUI = (ShowActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();//获取侧边栏对象
        slidingMenu.toggle();  //如果当前为开，则关；反之亦然
    }

    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();  //获取pager的数量
        }

        @Override
        public NewsMenuData getItem(int position) {
            return data.get(position);  //获取当前pager页面
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity,R.layout.list_item_left_menu,null);

            TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);  //获取侧边栏布局的tv_menu

            //设置textview的可用或不可用来控制颜色
            if(mCurrentPos == position){
                //当前item被选中
                tvMenu.setEnabled(true);  //true会启用selector中的红色
            }else {
                tvMenu.setEnabled(false);
            }

            NewsMenuData info =  getItem(position);  //获取当前页面的数据
            tvMenu.setText(info.title);
            return view;
        }

    }
}
