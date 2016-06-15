package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城支付订单 activity
* */

public class StorePaymentOrderActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.tv_payment_sucess)//确认支付
    TextView tv_payment_sucess;
    @InjectView(R.id.tv_payment_order_id)//订单id
    TextView tv_payment_order_id;
    @InjectView(R.id.tv_payment_order_total_price)//交易金额
    TextView tv_payment_order_total_price;
    @InjectView(R.id.tv_payment_order_youhui_price)//优惠券抵扣
    TextView tv_payment_order_youhui_price;
    @InjectView(R.id.tv_payment_order_residue_price)//还需支付
    TextView tv_payment_order_residue_price;
    @InjectView(R.id.rl_weixin)//微信支付布局
    RelativeLayout rl_weixin;
    @InjectView(R.id.rl_zfb)//支付宝支付布局
    RelativeLayout rl_zfb;
    @InjectView(R.id.checkbox_payment_order_weixin)//微信单选按钮
    CheckBox checkbox_payment_order_weixin;
    @InjectView(R.id.checkbox_payment_order_zhifubao)//支付宝单选按钮
    CheckBox checkbox_payment_order_zhifubao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_payment_order);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initData() {
        String productsTotal = getIntent().getStringExtra("productsTotal");
        String youHuiPrice = "0.0";
        tv_payment_order_id.setText(getIntent().getStringExtra("orderNum"));
        tv_payment_order_total_price.setText(productsTotal+"元");
        tv_payment_order_youhui_price.setText(youHuiPrice+"元");
        tv_payment_order_residue_price.setText((Double.parseDouble(productsTotal)-Double.parseDouble(youHuiPrice))+"元");
    }

    private void initView() {
        tv_payment_sucess.setOnClickListener(this);
        rl_weixin.setOnClickListener(this);
        rl_zfb.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_payment_sucess:
                Intent intent=new Intent(this,StorePaymentSucessActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.rl_weixin:
                checkbox_payment_order_weixin.setChecked(true);
                checkbox_payment_order_zhifubao.setChecked(false);
                break;
            case R.id.rl_zfb:
                checkbox_payment_order_zhifubao.setChecked(true);
                checkbox_payment_order_weixin.setChecked(false);
                break;
        }


    }


    @Override
    public String setupToolBarTitle() {
        return "支付订单";
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
