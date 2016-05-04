package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.ui.adapter.StoreFragmentAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城店铺搜索 activity
* */

public class StoreSearchShopActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.listview_shop)//下拉列表
    PullToRefreshListView listview_shop;
    private StoreFragmentAdapter listviewAdapter;
    private List<Merchant> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search_shop);
        ButterKnife.inject(this);
        // 隐藏输入法
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initData();
        initView();

        listviewAdapter=new StoreFragmentAdapter(this);
        listviewAdapter.setList(list);
        listview_shop.setAdapter(listviewAdapter);
        listview_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_shop = new Intent(StoreSearchShopActivity.this, StoreShopListHorzitalStyleActivity.class);
                startActivity(intent_shop);
                AnimUtil.intentSlidIn(StoreSearchShopActivity.this);
            }
        });
    }

    private void initData() {
        list=new ArrayList<>();

        for(int i=0;i<20;i++){
            Merchant manchant = new Merchant();
            manchant.setName("章子怡"+i+"便利店");
            list.add(manchant);
        }



        
    }

    private void initView() {
    }



    @Override
    public void onClick(View v) {


    }



}
