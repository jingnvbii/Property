package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城支付成功 activity
* */

public class StorePaymentSucessActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.tv_payment_sucess_ok)//完成
    TextView tv_payment_sucess_ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_payment_sucess);
        ButterKnife.inject(this);
        // 隐藏输入法
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {
        tv_payment_sucess_ok.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_payment_sucess_ok:
                Intent intent=new Intent(this,StoreOrderStatusActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
        }


    }


    @Override
    public String setupToolBarTitle() {
        return "支付成功";
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
