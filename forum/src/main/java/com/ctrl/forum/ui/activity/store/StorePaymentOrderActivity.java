package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_payment_order);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {
        tv_payment_sucess.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_payment_sucess:
                Intent intent=new Intent(this,StorePaymentSucessActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
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
