package com.ctrl.forum.ui.activity.store;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

/*
* 商城添加收货地址 activity
* */

public class StoreAddAddressActivity extends AppToolBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add_address);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
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
        return "添加收货地址";
    } 

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left_white);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

}
