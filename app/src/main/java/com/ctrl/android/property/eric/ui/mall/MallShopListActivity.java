package com.ctrl.android.property.eric.ui.mall;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.property.R;
import com.ctrl.android.property.base.AppHolder;
import com.ctrl.android.property.base.AppToolBarActivity;
import com.ctrl.android.property.base.Constant;
import com.ctrl.android.property.eric.dao.MallDao;
import com.ctrl.android.property.eric.entity.Item;
import com.ctrl.android.property.eric.entity.MallShop;
import com.ctrl.android.property.eric.ui.adapter.ItemAdapter;
import com.ctrl.android.property.eric.ui.adapter.MallShopListAdapter;
import com.ctrl.android.property.eric.util.S;
import com.ctrl.android.property.eric.util.StrConstant;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 社区商城  商家列表 activity
 * Created by Eric on 2015/9/23.
 */
public class MallShopListActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.sort_shop_layout)//排序方式和商铺类型所在的layout
    LinearLayout sort_shop_layout;

    @InjectView(R.id.mall_main_sort_style_btn)//排序方式按钮
    LinearLayout mall_main_sort_style_btn;
    @InjectView(R.id.mall_main_sort_style_btn_left_icon)//排序方式左侧图标
    ImageView mall_main_sort_style_btn_left_icon;
    @InjectView(R.id.mall_main_sort_style_btn_text)//排序方式文字标题
    TextView mall_main_sort_style_btn_text;
    @InjectView(R.id.mall_main_sort_style_btn_right_icon)//排序方式右侧图标
    ImageView mall_main_sort_style_btn_right_icon;

    @InjectView(R.id.mall_main_shop_style_btn)//商铺分类按钮
    LinearLayout mall_main_shop_style_btn;
    @InjectView(R.id.mall_main_shop_style_btn_left_icon)//商铺分类左侧图标
    ImageView mall_main_shop_style_btn_left_icon;
    @InjectView(R.id.mall_main_shop_style_btn_text)//商铺分类文字标题
    TextView mall_main_shop_style_btn_text;
    @InjectView(R.id.mall_main_shop_style_btn_right_icon)//商铺分类右侧图标
    ImageView mall_main_shop_style_btn_right_icon;

    @InjectView(R.id.mall_main_pop_layout)//
    LinearLayout mall_main_pop_layout;
    @InjectView(R.id.sort_style_layout)//排序方式
    LinearLayout sort_style_layout;
    @InjectView(R.id.shop_style_layout)//商铺分类
    LinearLayout shop_style_layout;

    @InjectView(R.id.pull_to_refresh_listView)//可刷新的列表
    PullToRefreshListView mPullToRefreshListView;

    @InjectView(R.id.sort_style_listView)
    ListView sort_style_listView;
    @InjectView(R.id.shop_style_listView)
    ListView shop_style_listView;

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

    private String sortType;

    private List<MallShop> listMallShop = new ArrayList<>();

    private String TITLE = StrConstant.SHOP_LIST_TITLE;
    private String keyword = "";

    private List<Item> listItem = new ArrayList<>();
    private ItemAdapter itemAdapter;

    private List<Item> listItem2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.mall_shop_list_activity);
        ButterKnife.inject(this);
        init();
    }

    /**
     * 初始化执行的 方法
     * */
    private void init(){

        listItem.add(new Item(0,"默认排序",true));
        listItem.add(new Item(1,"销量从高到低",false));
        listItem.add(new Item(2,"评价最高",false));
        //listItem.add(new Item(3,"信用最高",false));

        listItem2.add(new Item(0,"全部",true));
        listItem2.add(new Item(1,"美食",false));
        listItem2.add(new Item(2,"服装",false));
        listItem2.add(new Item(3,"酒店",false));
        listItem2.add(new Item(4,"电影",false));
        listItem2.add(new Item(5,"KTV",false));
        listItem2.add(new Item(6,"外卖",false));
        listItem2.add(new Item(7,"足疗按摩",false));

        mall_main_sort_style_btn.setOnClickListener(this);
        mall_main_shop_style_btn.setOnClickListener(this);
        sort_style_layout.setOnClickListener(this);
        shop_style_layout.setOnClickListener(this);

        keyword = getIntent().getStringExtra("keyword");
        Log.d("demo", "keyword: " + keyword);

        mallDao = new MallDao(this);
        showProgress(true);
        mallDao.requestShopListByKeyword(keyword,String.valueOf(longitude), String.valueOf(latitude), provinceName, cityName, areaName, String.valueOf(currentPage), String.valueOf(rowCountPerPage));

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        mPullToRefreshListView.onRefreshComplete();

        if(1 == requestCode){

            listMallShop = new ArrayList<>();
            listMallShop = mallDao.getListShop();

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
                    mallDao.getListShop().clear();
                    mallDao.requestShopListByKeyword(keyword, String.valueOf(longitude), String.valueOf(latitude), provinceName, cityName, areaName, String.valueOf(currentPage), String.valueOf(rowCountPerPage));
                }

                //上拉加载
                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    currentPage = currentPage + 1;
                    mallDao.requestShopListByKeyword(keyword, String.valueOf(longitude), String.valueOf(latitude), provinceName, cityName, areaName, String.valueOf(currentPage), String.valueOf(rowCountPerPage));
                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MallShopListActivity.this, MallShopMainActivity.class);
                    intent.putExtra("companyId",listMallShop.get(position - 1).getId());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(MallShopListActivity.this);

                }
            });
        }

        if(9 == requestCode){

            listMallShop = new ArrayList<>();
            listMallShop = mallDao.getListShopBySort();

            mListView = mPullToRefreshListView.getRefreshableView();
            mAdapter = new MallShopListAdapter(this);
            mAdapter.setList(listMallShop);
            mAdapter.notifyDataSetChanged();

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
                    mallDao.getListShopBySort().clear();
                    mallDao.requestShopListBySort(0,sortType, String.valueOf(longitude), String.valueOf(latitude),
                            provinceName, cityName, areaName,
                            String.valueOf(currentPage), String.valueOf(rowCountPerPage));
                }

                //上拉加载
                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    currentPage = currentPage + 1;
                    mallDao.requestShopListBySort(0,sortType,String.valueOf(longitude),String.valueOf(latitude),
                            provinceName,cityName,areaName,
                            String.valueOf(currentPage),String.valueOf(rowCountPerPage));
                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MallShopListActivity.this, MallShopMainActivity.class);
                    intent.putExtra("companyId",listMallShop.get(position - 1).getId());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(MallShopListActivity.this);

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

    private int sort_flg = 0;
    private int shop_flg = 0;

    @Override
    public void onClick(View v) {

        //排序方式
        if(v == mall_main_sort_style_btn){
            if(sort_flg == 0){

                itemAdapter = new ItemAdapter(this);
                itemAdapter.setList(listItem);
                sort_style_listView.setAdapter(itemAdapter);
                sort_style_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0; i < listItem.size(); i++) {
                            listItem.get(i).setCheck(false);
                        }
                        listItem.get(position).setCheck(true);
                        itemAdapter.setList(listItem);
                        itemAdapter.notifyDataSetChanged();
                        //sort_style_layout.requestFocus();
                        sort_style_layout.setVisibility(View.GONE);
                        shop_style_layout.setVisibility(View.GONE);
                        sort_flg = 0;

                        currentPage = 1;
                        mall_main_sort_style_btn_text.setText(S.getStr(listItem.get(position).getName()));
                        sortType = String.valueOf(listItem.get(position).getFlg());
                        showProgress(true);
                        mallDao.requestShopListBySort(1,sortType,String.valueOf(longitude),String.valueOf(latitude),
                                provinceName,cityName,areaName,
                                String.valueOf(currentPage),String.valueOf(rowCountPerPage));
                    }
                });

                setSortStyleBtnChecked();
                sort_style_layout.setVisibility(View.VISIBLE);
                shop_style_layout.setVisibility(View.GONE);
                sort_flg = 1;
                shop_flg = 0;
            } else if(sort_flg == 1){
                //sort_style_layout.clearFocus();
                sort_style_layout.setVisibility(View.GONE);
                shop_style_layout.setVisibility(View.GONE);
                sort_flg = 0;
            }


        }

        //商铺分类
        if(v == mall_main_shop_style_btn){

            if(shop_flg == 0){

                itemAdapter = new ItemAdapter(this);
                itemAdapter.setList(listItem2);
                shop_style_listView.setAdapter(itemAdapter);
                shop_style_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0; i < listItem2.size(); i++) {
                            listItem2.get(i).setCheck(false);
                        }
                        listItem2.get(position).setCheck(true);
                        itemAdapter.setList(listItem2);
                        itemAdapter.notifyDataSetChanged();
                        //sort_style_layout.requestFocus();
                        sort_style_layout.setVisibility(View.GONE);
                        shop_style_layout.setVisibility(View.GONE);
                        shop_flg = 0;

                        mall_main_shop_style_btn_text.setText(S.getStr(listItem2.get(position).getName()));
                    }
                });

                setShopStyleBtnChecked();
                sort_style_layout.setVisibility(View.GONE);
                shop_style_layout.setVisibility(View.VISIBLE);
                shop_flg = 1;
                sort_flg = 0;
            } else if(shop_flg == 1){
                //sort_style_layout.clearFocus();
                sort_style_layout.setVisibility(View.GONE);
                shop_style_layout.setVisibility(View.GONE);
                shop_flg = 0;
            }
        }

        if(v == sort_style_layout){
            //
        }

        if(v == shop_style_layout){
            //
        }
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
                Intent intent = new Intent(MallShopListActivity.this, MallSearchActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(MallShopListActivity.this);
            }
        });
        return true;
    }

    /**
     * 排序方式按钮 点击后效果
     * */
    private void setSortStyleBtnChecked(){

        //排序方式左侧图标
         mall_main_sort_style_btn_left_icon.setImageDrawable(getResources().getDrawable(R.drawable.sort_icon_checked));
        //排序方式文字标题
         mall_main_sort_style_btn_text.setTextColor(getResources().getColor(R.color.text_green));
        //排序方式右侧图标
         mall_main_sort_style_btn_right_icon.setImageDrawable(getResources().getDrawable(R.drawable.gray_arrow_up));

        //商铺分类左侧图标
         mall_main_shop_style_btn_left_icon.setImageDrawable(getResources().getDrawable(R.drawable.shop_icon));
        //商铺分类文字标题
         mall_main_shop_style_btn_text.setTextColor(getResources().getColor(R.color.text_gray));
        //商铺分类右侧图标
         mall_main_shop_style_btn_right_icon.setImageDrawable(getResources().getDrawable(R.drawable.gray_arrow_down));

    }

    /**
     * 商铺分类按钮 点击后效果
     * */
    private void setShopStyleBtnChecked(){

        //排序方式左侧图标
        mall_main_sort_style_btn_left_icon.setImageDrawable(getResources().getDrawable(R.drawable.sort_icon));
        //排序方式文字标题
        mall_main_sort_style_btn_text.setTextColor(getResources().getColor(R.color.text_gray));
        //排序方式右侧图标
        mall_main_sort_style_btn_right_icon.setImageDrawable(getResources().getDrawable(R.drawable.gray_arrow_down));

        //商铺分类左侧图标
        mall_main_shop_style_btn_left_icon.setImageDrawable(getResources().getDrawable(R.drawable.shop_icon_checked));
        //商铺分类文字标题
        mall_main_shop_style_btn_text.setTextColor(getResources().getColor(R.color.text_green));
        //商铺分类右侧图标
        mall_main_shop_style_btn_right_icon.setImageDrawable(getResources().getDrawable(R.drawable.gray_arrow_up));

    }

}
