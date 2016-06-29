package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.AddressDao;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城添加收货地址 activity
* */

public class StoreAddAddressActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.et_add_adress_name)//联系人姓名
            EditText et_add_adress_name;
    @InjectView(R.id.et_add_adress_detail)//联系人详细地址
            EditText et_add_adress_detail;
    @InjectView(R.id.et_add_adress_tel)//联系人电话
            EditText et_add_adress_tel;
    @InjectView(R.id.rl_add_address)//基本地址
            RelativeLayout rl_add_address;
    @InjectView(R.id.tv_add_address_save)//保存
            TextView tv_add_address_save;
    @InjectView(R.id.tv_add_address_base)//基本地址
            TextView tv_add_address_base;


    private String province;
    private String city;
    private String area;
    private String latitude;
    private String longitude;
    private String addressBase;
    private AddressDao adao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_add_address);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initData() {
        adao=new AddressDao(this);
    }

    private void initView() {
        rl_add_address.setOnClickListener(this);
        tv_add_address_save.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add_address:
                Intent intent=new Intent(StoreAddAddressActivity.this,StoreLocateListMapActivity.class);
                startActivityForResult(intent,200);
                AnimUtil.intentSlidIn(StoreAddAddressActivity.this);
                break;
            case R.id.tv_add_address_save:
               if(checkInput()){
                   showProgress(true);
                   adao.requestAddReceiveAddress(et_add_adress_detail.getText().toString().trim(),
                           Arad.preferences.getString("memberId"),
                           et_add_adress_tel.getText().toString().trim(),
                           province,
                           city,
                           area,
                           latitude,
                           longitude,
                           et_add_adress_name.getText().toString().trim(),
                           addressBase);

               }

                break;
        }


    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        if(requestCode== 1){
           // MessageUtils.showShortToast(this,"添加收货地址成功");
            finish();
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        showProgress(false);
    }

    private boolean checkInput(){
        if(TextUtils.isEmpty(et_add_adress_name.getText().toString())){
            MessageUtils.showShortToast(this,"姓名为空");
            return false;
        }
        if(TextUtils.isEmpty(et_add_adress_tel.getText().toString())){
            MessageUtils.showShortToast(this,"联系方式为空");
            return false;
        }
        if(TextUtils.isEmpty(et_add_adress_detail.getText().toString())){
            MessageUtils.showShortToast(this,"详细地址为空");
            return false;
        }
        if(TextUtils.isEmpty(tv_add_address_base.getText().toString())){
            MessageUtils.showShortToast(this,"基本地址为空");
            return false;
        }
        if(et_add_adress_tel.getText().toString().trim().length()!=11){
            MessageUtils.showShortToast(this,"手机号格式不正确");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200&&resultCode==RESULT_OK){
            province=data.getStringExtra("province");
            city=data.getStringExtra("city");
            area=data.getStringExtra("area");
            latitude=data.getStringExtra("latitude");
            longitude=data.getStringExtra("longitude");
            addressBase=data.getStringExtra("address");
            tv_add_address_base.setText(addressBase);
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
