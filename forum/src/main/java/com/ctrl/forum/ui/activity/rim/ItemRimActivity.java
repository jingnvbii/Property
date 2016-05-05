package com.ctrl.forum.ui.activity.rim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.RimShop;
import com.ctrl.forum.ui.adapter.RimShopListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目一
 */
public class ItemRimActivity extends ActionBarActivity implements View.OnClickListener{
    private ListView lv_shop;
    private ImageView iv_bo_phone;
    private ImageView iv_map;//地图
    private List<RimShop> rimShops;
    private RimShopListAdapter rimShopListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_item);
        initView();
        initData();

        rimShopListAdapter = new RimShopListAdapter(this,rimShops);
        lv_shop.setAdapter(rimShopListAdapter);

        lv_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iv_bo_phone = (ImageView) view.findViewById(R.id.iv_phone);
                iv_bo_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("iv_bo_phone=========","Click......");
                    }
                });
                Intent intent = new Intent(getApplicationContext(),RimShopDetailActivity.class);
                startActivity(intent);
           }
        });
    }

    private void initData() {
        rimShops = new ArrayList<>();
        for (int i=0;i<4;i++){
            RimShop rimShop = new RimShop();
            rimShop.setName(getResources().getString(R.string.rim_shop_name));
            rimShops.add(rimShop);
        }
    }

    private void initView() {
        lv_shop = (ListView) findViewById(R.id.lv_shop);
        iv_map = (ImageView) findViewById(R.id.iv_map);

        iv_map.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.iv_map://地图
              Intent intent = new Intent(this,RimMapDetailActivity.class);
              startActivity(intent);
              break;
          case R.id.iv_back:
              this.finish();
              break;
      }
    }
}
