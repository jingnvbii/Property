package com.ctrl.android.property.eric.ui.mall;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.property.R;
import com.ctrl.android.property.base.AppHolder;
import com.ctrl.android.property.base.AppToolBarActivity;
import com.ctrl.android.property.base.Constant;
import com.ctrl.android.property.eric.dao.MallDao;
import com.ctrl.android.property.eric.entity.MallShop;
import com.ctrl.android.property.eric.ui.adapter.MallShopListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 社区商城的主页面 activity
 * Created by Eric on 2015/9/23.
 */
public class MallMainActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.toolbar_title)//标题
    TextView toolbar_title;

//    @InjectView(R.id.sort_shop_layout)//排序方式和商铺类型所在的layout
//    LinearLayout sort_shop_layout;

//    @InjectView(R.id.mall_main_sort_style_btn)//排序方式按钮
//    LinearLayout mall_main_sort_style_btn;
//    @InjectView(R.id.mall_main_sort_style_btn_left_icon)//排序方式左侧图标
//    ImageView mall_main_sort_style_btn_left_icon;
//    @InjectView(R.id.mall_main_sort_style_btn_text)//排序方式文字标题
//    TextView mall_main_sort_style_btn_text;
//    @InjectView(R.id.mall_main_sort_style_btn_right_icon)//排序方式右侧图标
//    ImageView mall_main_sort_style_btn_right_icon;

    @InjectView(R.id.mall_main_pop_layout)//
    LinearLayout mall_main_pop_layout;

    @InjectView(R.id.mall_main_ad)//广告图片
    RelativeLayout mall_main_ad;
    @InjectView(R.id.mall_main_ad_close_cross)//关闭广告 X 号
    ImageView mall_main_ad_close_cross;

//    @InjectView(R.id.mall_main_shop_style_btn)//商铺分类按钮
//    LinearLayout mall_main_shop_style_btn;
//    @InjectView(R.id.mall_main_shop_style_btn_left_icon)//商铺分类左侧图标
//    ImageView mall_main_shop_style_btn_left_icon;
//    @InjectView(R.id.mall_main_shop_style_btn_text)//商铺分类文字标题
//    TextView mall_main_shop_style_btn_text;
//    @InjectView(R.id.mall_main_shop_style_btn_right_icon)//商铺分类右侧图标
//    ImageView mall_main_shop_style_btn_right_icon;

    @InjectView(R.id.mall_food_btn)//美食
    LinearLayout mall_food_btn;
    @InjectView(R.id.mall_movie_btn)//电影
    LinearLayout mall_movie_btn;
    @InjectView(R.id.mall_takeout_btn)//外卖
    LinearLayout mall_takeout_btn;
    @InjectView(R.id.mall_hotel_btn)//酒店
    LinearLayout mall_hotel_btn;

    @InjectView(R.id.pull_to_refresh_listView)//可刷新的列表
    PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;
    private View mMenuView;//显示pop的view
    private MallShopListAdapter mAdapter;

    private MallDao mallDao;

    private String provinceName = AppHolder.getInstance().getBdLocation().getProvince();
    private String cityName = AppHolder.getInstance().getBdLocation().getCity();
    private String areaName = AppHolder.getInstance().getBdLocation().getDistrict();
    private Double longitude = AppHolder.getInstance().getBdLocation().getLongitude();
    private Double latitude = AppHolder.getInstance().getBdLocation().getLatitude();
    private int currentPage = 1;
    private int rowCountPerPage = Constant.PAGE_CAPACITY;
    private List<MallShop> listMallShop;

    private String TITLE = AppHolder.getInstance().getBdLocation().getAddrStr();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.mall_main_activity);
        ButterKnife.inject(this);
        init();
    }

    /**
     * 初始化执行的 方法
     * */
    private void init(){

        toolbar_title.setOnClickListener(this);

        //mall_main_sort_style_btn.setOnClickListener(this);
        //mall_main_shop_style_btn.setOnClickListener(this);

        mall_main_ad.setOnClickListener(this);
        mall_main_ad_close_cross.setOnClickListener(this);

        mall_food_btn.setOnClickListener(this);
        mall_movie_btn.setOnClickListener(this);
        mall_takeout_btn.setOnClickListener(this);
        mall_hotel_btn.setOnClickListener(this);

        mallDao = new MallDao(this);

        showProgress(true);
        mallDao.requestShopArroundList(provinceName, cityName, areaName, String.valueOf(longitude), String.valueOf(latitude), String.valueOf(currentPage), String.valueOf(rowCountPerPage));

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        mPullToRefreshListView.onRefreshComplete();

        if(0 == requestCode){

            listMallShop = mallDao.getListShopArround();

            mListView = mPullToRefreshListView.getRefreshableView();
            mAdapter = new MallShopListAdapter(this);
            mAdapter.setList(listMallShop);
            mListView.setAdapter(mAdapter);
            mListView.setDivider(null);
            mListView.setDividerHeight(20);

            //注册上下拉定义事件
            mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
            mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                //下拉刷新
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    currentPage = 1;
                    mallDao.getListShopArround().clear();
                    mallDao.requestShopArroundList(provinceName, cityName, areaName, String.valueOf(longitude), String.valueOf(latitude), String.valueOf(currentPage), String.valueOf(rowCountPerPage));
                }

                //上拉加载
                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    currentPage = currentPage + 1;
                    mallDao.requestShopArroundList(provinceName, cityName, areaName, String.valueOf(longitude), String.valueOf(latitude), String.valueOf(currentPage), String.valueOf(rowCountPerPage));
                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MallMainActivity.this, MallShopMainActivity.class);
                    intent.putExtra("companyId",listMallShop.get(position - 1).getId());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(MallMainActivity.this);

                }
            });
        }
    }

    /**
     * 数据请求失败后
     * */
    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        showProgress(false);
        mPullToRefreshListView.onRefreshComplete();
        MessageUtils.showShortToast(this, errorMessage);
    }

    @Override
    public void onClick(View v) {

        if(v == toolbar_title){
            Intent intent = new Intent(MallMainActivity.this, MallLocateActivity.class);
            startActivity(intent);
            AnimUtil.intentSlidIn(MallMainActivity.this);
        }

        //美食
        if(v == mall_food_btn){
            //mAdapter.setList(getListMap("美食"));
            Intent intent = new Intent(MallMainActivity.this, MallShopListActivity.class);
            intent.putExtra("keyword","美食");
            startActivity(intent);
            AnimUtil.intentSlidIn(MallMainActivity.this);
        }

        //电影
        if(v == mall_movie_btn){
            //mAdapter.setList(getListMap("电影"));
            Intent intent = new Intent(MallMainActivity.this, MallShopListActivity.class);
            intent.putExtra("keyword","电影");
            startActivity(intent);
            AnimUtil.intentSlidIn(MallMainActivity.this);
        }

        //外卖
        if(v == mall_takeout_btn){
            //mAdapter.setList(getListMap("外卖"));
            Intent intent = new Intent(MallMainActivity.this, MallShopListActivity.class);
            intent.putExtra("keyword","外卖");
            startActivity(intent);
            AnimUtil.intentSlidIn(MallMainActivity.this);
        }

        //酒店
        if(v == mall_hotel_btn){
            //mAdapter.setList(getListMap("酒店"));
            Intent intent = new Intent(MallMainActivity.this, MallShopListActivity.class);
            intent.putExtra("keyword","酒店");
            startActivity(intent);
            AnimUtil.intentSlidIn(MallMainActivity.this);
        }

        //广告图片
        if(v == mall_main_ad){
            MessageUtils.showShortToast(this,"广告图片");
        }

        //关闭广告的 X 号
        if(v == mall_main_ad_close_cross){
            mall_main_ad.setVisibility(View.GONE);
        }

        //排序方式
//        if(v == mall_main_sort_style_btn){
//            setSortStyleBtnChecked();
//            showSortStyleListPop();
//        }

        //商铺分类
//        if(v == mall_main_shop_style_btn){
//            setShopStyleBtnChecked();
//            showShopStyleListPop();
//        }
    }


    /**
     * header 左侧按钮
     * */
    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.drawable.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        return true;
    }

    /**
     * 页面标题
     * */
    @Override
    public String setupToolBarTitle() {
        return TITLE;
    }


    /**
     * header 右侧按钮
     * */
    @Override
    public boolean setupToolBarRightButton(ImageView rightButton) {
        rightButton.setImageResource(R.drawable.search_icon);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MallMainActivity.this, MallSearchActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(MallMainActivity.this);
            }
        });
        return true;
    }

    /**
     * 排序方式按钮 点击后效果
     * */
//    private void setSortStyleBtnChecked(){
//
//        //排序方式左侧图标
//         mall_main_sort_style_btn_left_icon.setImageDrawable(getResources().getDrawable(R.drawable.sort_icon_checked));
//        //排序方式文字标题
//         mall_main_sort_style_btn_text.setTextColor(getResources().getColor(R.color.text_green));
//        //排序方式右侧图标
//         mall_main_sort_style_btn_right_icon.setImageDrawable(getResources().getDrawable(R.drawable.gray_arrow_up));
//
//        //商铺分类左侧图标
//         mall_main_shop_style_btn_left_icon.setImageDrawable(getResources().getDrawable(R.drawable.shop_icon));
//        //商铺分类文字标题
//         mall_main_shop_style_btn_text.setTextColor(getResources().getColor(R.color.text_gray));
//        //商铺分类右侧图标
//         mall_main_shop_style_btn_right_icon.setImageDrawable(getResources().getDrawable(R.drawable.gray_arrow_down));
//
//    }

    /**
     * 商铺分类按钮 点击后效果
     * */
//    private void setShopStyleBtnChecked(){
//
//        //排序方式左侧图标
//        mall_main_sort_style_btn_left_icon.setImageDrawable(getResources().getDrawable(R.drawable.sort_icon));
//        //排序方式文字标题
//        mall_main_sort_style_btn_text.setTextColor(getResources().getColor(R.color.text_gray));
//        //排序方式右侧图标
//        mall_main_sort_style_btn_right_icon.setImageDrawable(getResources().getDrawable(R.drawable.gray_arrow_down));
//
//        //商铺分类左侧图标
//        mall_main_shop_style_btn_left_icon.setImageDrawable(getResources().getDrawable(R.drawable.shop_icon_checked));
//        //商铺分类文字标题
//        mall_main_shop_style_btn_text.setTextColor(getResources().getColor(R.color.text_green));
//        //商铺分类右侧图标
//        mall_main_shop_style_btn_right_icon.setImageDrawable(getResources().getDrawable(R.drawable.gray_arrow_up));
//
//    }

    /**
     * 排序方式下拉  列表
     * */
    private void showSortStyleListPop(){
        //setRoomData();
        mMenuView = LayoutInflater.from(MallMainActivity.this).inflate(R.layout.choose_sort_style_top_pop, null);

        final PopupWindow Pop = new PopupWindow();

        // 设置SelectPicPopupWindow的View
        Pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        Pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        Pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        Pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //Pop.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        Pop.setBackgroundDrawable(dw);

        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        Pop.setFocusable(true);

        int[] location = new int[2];
        mall_main_pop_layout.getLocationOnScreen(location);
        Pop.showAtLocation(mall_main_pop_layout, Gravity.NO_GRAVITY, location[0], location[1] - Pop.getHeight());

        //Pop.showAtLocation(mall_main_pop_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 店铺分类下拉  列表
     * */
    private void showShopStyleListPop(){
        //setRoomData();
        mMenuView = LayoutInflater.from(MallMainActivity.this).inflate(R.layout.choose_shop_style_top_pop, null);

        final PopupWindow Pop = new PopupWindow();

        // 设置SelectPicPopupWindow的View
        Pop.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        Pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        Pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        Pop.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //Pop.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        Pop.setBackgroundDrawable(dw);

        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        Pop.setFocusable(true);

        int[] location = new int[2];
        mall_main_pop_layout.getLocationOnScreen(location);
        Pop.showAtLocation(mall_main_pop_layout, Gravity.NO_GRAVITY, location[0], location[1] - Pop.getHeight());

        //Pop.showAtLocation(mall_main_pop_layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 测试 获取数据的方法
     * */
    private List<Map<String,String>> getListMap(String str){
        List<Map<String,String>> list = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i ++){
            Map<String,String> map = new HashMap<String,String>();
            map.put("pic","aa" + i);
            map.put("name",str + "商家名称商家名称商家名称商家名称商家名称商家名称" + i);
            map.put("time","1" + i + ": 00 - " + "1" + i + ": 00");
            map.put("rate","" + (i*(0.5)));
            list.add(map);
        }
        return list;
    }

}
