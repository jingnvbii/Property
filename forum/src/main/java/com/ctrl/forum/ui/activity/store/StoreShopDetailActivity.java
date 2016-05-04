package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.customview.ListViewForScrollView;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.ui.adapter.StoreLianMengGridViewAdapter;
import com.ctrl.forum.ui.adapter.StoreShopDetailPingLunListViewAdapter;
import com.ctrl.forum.ui.adapter.StoreXianjinQuanListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城店铺详情 activity
* */

public class StoreShopDetailActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.lv_xianjinquan)//现金券
            ListViewForScrollView lv_xianjinquan;
    @InjectView(R.id.gridview_lianmeng)//联盟商家
            GridView gridview_lianmeng;
    @InjectView(R.id.lv_pinglun)//评论列表
    ListViewForScrollView lv_pinglun;

    private List<Merchant> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_shop_detail);
        ButterKnife.inject(this);

        // 隐藏输入法
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
        StoreXianjinQuanListViewAdapter mStoreXianjinQuanListViewAdapter = new StoreXianjinQuanListViewAdapter(this);
        mStoreXianjinQuanListViewAdapter.setList(list);
        lv_xianjinquan.setAdapter(mStoreXianjinQuanListViewAdapter);

        StoreLianMengGridViewAdapter mStoreLianMengGridViewAdapter=new StoreLianMengGridViewAdapter(this);
        mStoreLianMengGridViewAdapter.setList(list);
        gridview_lianmeng.setAdapter(mStoreLianMengGridViewAdapter);
        int size = list.size();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int allWidth = (int) (110 * size * density);
        int itemWidth = (int) (90 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridview_lianmeng.setLayoutParams(params);
        gridview_lianmeng.setColumnWidth(itemWidth);
        gridview_lianmeng.setHorizontalSpacing(10);
        gridview_lianmeng.setStretchMode(GridView.NO_STRETCH);
        gridview_lianmeng.setNumColumns(size);

        StoreShopDetailPingLunListViewAdapter mStoreShopDetailPingLunListViewAdapter=new StoreShopDetailPingLunListViewAdapter(this);
        mStoreShopDetailPingLunListViewAdapter.setList(list);
        lv_pinglun.setAdapter(mStoreShopDetailPingLunListViewAdapter);

        lv_pinglun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(StoreShopDetailActivity.this,StorePingjiaDetailActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreShopDetailActivity.this);
            }
        });



    }

    private void initData() {
        list=new ArrayList<>();
        for(int i=0;i<10;i++){
            Merchant merchant=new Merchant();
            merchant.setName("小贝便利"+i);
            list.add(merchant);
        }
    }

    private void initView() {

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }


    }


    @Override
    public String setupToolBarTitle() {
        TextView mTitle = getmTitle();
        mTitle.setTextColor(Color.BLACK);
        return "小贝商品";
    } 

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_red);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public boolean setupToolBarRightButton(ImageView rightButton) {
        rightButton.setImageResource(R.mipmap.zan_red);
        return true;
    }
}
