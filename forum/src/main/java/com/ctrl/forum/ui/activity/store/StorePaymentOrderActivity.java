package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.weixin.WeixinPayUtil;
import com.ctrl.forum.wxapi.WXPayEntryActivity;
import com.ctrl.forum.zhifubao.AliplyPayUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城支付订单 activity
* */

public class StorePaymentOrderActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.tv_payment_sucess)//确认支付11
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
    private String orderNum;
    private double productsTotal;
    private String orderId;
    private String youHuiPrice;

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
        productsTotal = getIntent().getDoubleExtra("productsTotal", 0.0);
        orderNum=getIntent().getStringExtra("orderNum");
        orderId=getIntent().getStringExtra("orderId");
        youHuiPrice=getIntent().getStringExtra("redenvelope");
        tv_payment_order_id.setText(orderNum);
        tv_payment_order_total_price.setText(productsTotal+"元");
        if(youHuiPrice!=null) {
            tv_payment_order_youhui_price.setText(youHuiPrice+"元");
            tv_payment_order_residue_price.setText((productsTotal - Double.parseDouble(youHuiPrice)) + "元");
        }else {
            tv_payment_order_youhui_price.setText("0.0元");
            tv_payment_order_residue_price.setText(productsTotal+"元");
        }
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
                if(checkbox_payment_order_zhifubao.isChecked()){//支付宝支付
                    AliplyPayUtil aliplyPayUtil = new AliplyPayUtil(StorePaymentOrderActivity.this, new AliplyPayUtil.PayStateListener() {
                        @Override
                        public void doAfterAliplyPay(boolean isSuccess) {
                            if (isSuccess) {
                                MessageUtils.showShortToast(StorePaymentOrderActivity.this, "支付成功");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(StorePaymentOrderActivity.this, StoreOrderStatusActivity.class);
                                        intent.putExtra("id", orderId);
                                        startActivity(intent);
                                        AnimUtil.intentSlidIn(StorePaymentOrderActivity.this);
                                        finish();
                                    }
                                }, 2000);
                            } else {
                                MessageUtils.showShortToast(StorePaymentOrderActivity.this,"支付失败");
                                finish();
                            }
                        }
                    });
                    /***   此处金额 写了0.01元  实际情况具体问题具体分析   ***/
              aliplyPayUtil.pay(orderId, Constant.ALIPLY_URL, "烟台项目商品", "烟台项目商城商品",productsTotal);

                }
                if(checkbox_payment_order_weixin.isChecked()){//微信支付
                    WeixinPayUtil weixinPayUtil = new WeixinPayUtil(this);
                    WXPayEntryActivity.setPayStateListener(new WXPayEntryActivity.PayStateListener() {
                        @Override
                        public void doAfterWeixinPay(int payStatus) {
                            if (payStatus == Constant.PAY_STATUS_SUCCESS) {
                                MessageUtils.showLongToast(StorePaymentOrderActivity.this, "支付成功");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(StorePaymentOrderActivity.this, StoreOrderStatusActivity.class);
                                        intent.putExtra("id", orderId);
                                        startActivity(intent);
                                        AnimUtil.intentSlidIn(StorePaymentOrderActivity.this);
                                        finish();
                                    }
                                },2000);

                            } else if (payStatus == Constant.PAY_STATUS_FAILED) {
                                MessageUtils.showLongToast(StorePaymentOrderActivity.this, "支付失败");
                                finish();
                            } else if (payStatus == Constant.PAY_STATUS_CANCLE) {
                                MessageUtils.showLongToast(StorePaymentOrderActivity.this, "支付被取消");
                                finish();
                            }
                        }
                    });
                    //(int)Double.parseDouble(totalPrice)*100
                    weixinPayUtil.pay(orderId, Constant.NOTICE_URL,"烟台项目商城商品", (int)(productsTotal*100));
                }
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
