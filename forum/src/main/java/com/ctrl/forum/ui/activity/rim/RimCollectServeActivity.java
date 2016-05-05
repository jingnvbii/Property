package com.ctrl.forum.ui.activity.rim;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.RimShop;
import com.ctrl.forum.ui.adapter.RimShopListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RimCollectServeActivity extends ActionBarActivity implements View.OnClickListener{
    private ListView lv_collect;
    private ImageView iv_back;
    private RimShopListAdapter rimShopListAdapter;
    private List<RimShop> serves;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_collect_serve);

        lv_collect = (ListView) findViewById(R.id.lv_collect);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        initData();

        rimShopListAdapter = new RimShopListAdapter(this,serves);
        lv_collect.setAdapter(rimShopListAdapter);
    }
   //��ʼ�����
    private void initData() {
        serves = new ArrayList<>();
        for (int i =0;i<7;i++){
                RimShop rimShop = new RimShop();
                rimShop.setName(getResources().getString(R.string.rim_shop_name));
                serves.add(rimShop);
            }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                this.finish();
                break;
        }
    }
}
