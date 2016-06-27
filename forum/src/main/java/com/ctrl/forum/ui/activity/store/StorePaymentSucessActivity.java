package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.OrderDao;
import com.ctrl.forum.entity.OrderItem;
import com.ctrl.forum.entity.OrderState;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城支付成功 activity
* */

public class StorePaymentSucessActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.tv_payment_sucess_ok)//完成
    TextView tv_payment_sucess_ok;
    @InjectView(R.id.tv_money)//支付订单金额
    TextView tv_money;
    @InjectView(R.id.tv_comodity_name)//商品名称
    TextView tv_comodity_name;
    @InjectView(R.id.tv_comodity_time)//交易时间
    TextView tv_comodity_time;
    @InjectView(R.id.tv_payment_way)//支付方式
    TextView tv_payment_way;
    @InjectView(R.id.tv_payment_id)//交易单号
    TextView tv_payment_id;


    private OrderDao odao;
    private List<OrderItem> listOrderItem;
    private List<OrderState> listOrderState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_payment_sucess);
        ButterKnife.inject(this);
        // 隐藏输入法
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initData() {
        odao=new OrderDao(this);
      //  odao.requesOrderDetail(getIntent().getStringExtra("orderId"));
    }

    private void initView() {
        tv_payment_sucess_ok.setOnClickListener(this);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
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
