package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城订单详情 activity
* */

public class StoreOrderDetailActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.rl_address)//收货地址
    RelativeLayout rl_address;
    @InjectView(R.id.btn_jiesuan)//马上结算
    Button btn_jiesuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_detail);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {
        rl_address.setOnClickListener(this);
        btn_jiesuan.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.rl_address:
                intent =new Intent(this,StoreManageAddressActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.btn_jiesuan:
                intent =new Intent(this,StorePaymentOrderActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
        }


    }


    @Override
    public String setupToolBarTitle() {
        return "订单详情";
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
