package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城管理收货地址 activity
* */

public class StoreManageAddressActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.rl_add_address)//新增地址
    RelativeLayout rl_add_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manage_address);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {
        rl_add_address.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_add_address:
                Intent intent =new Intent(this,StoreAddAddressActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
        }


    }


    @Override
    public String setupToolBarTitle() {
        return "管理配送地址";
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
