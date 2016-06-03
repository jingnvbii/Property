package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.AddressDao;
import com.ctrl.forum.entity.Address;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城编辑地址 activity
* */

public class StoreEditAddressActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.et_edit_name)//联系人姓名
            EditText et_edit_name;
    @InjectView(R.id.et_edit_door_number)//联系人门牌号
            EditText et_edit_door_number;
    @InjectView(R.id.et_edit_phoneNumber)//联系人电话
            EditText et_edit_phoneNumber;
    @InjectView(R.id.tv_edit_base_address)//基本地址
            TextView tv_edit_base_address;
    @InjectView(R.id.tv_edit_save)//保存
            TextView tv_edit_save;
    @InjectView(R.id.rl_edit_base_address)//基本地址布局
            RelativeLayout rl_edit_base_address;


    private Address address;
    private AddressDao adao;
    private String province;
    private String city;
    private String area;
    private String latitude;
    private String longitude;
    private String addressBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_edit_address);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();
        initData();
    }

    private void initData() {
        adao=new AddressDao(this);
        address = (Address) getIntent().getSerializableExtra("address");
        tv_edit_base_address.setText(address.getAddressBase());
        et_edit_name.setText(address.getReceiveName());
        et_edit_door_number.setText(address.getAddressDetail());
        et_edit_phoneNumber.setText(address.getMobile());
    }

    private void initView() {
        tv_edit_save.setOnClickListener(this);
        rl_edit_base_address.setOnClickListener(this);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==2){
            MessageUtils.showShortToast(this,"更改收货地址成功");
        }
        if(requestCode==3){
            MessageUtils.showShortToast(this,"删除收货地址成功");
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_edit_base_address:
                Intent intent=new Intent(StoreEditAddressActivity.this,StoreLocateListMapActivity.class);
                startActivityForResult(intent,500);
                AnimUtil.intentSlidIn(StoreEditAddressActivity.this);
                break;
            case R.id.tv_edit_save:
                if(checkInput()){
                    adao.requesUpdateReceiveAddress(et_edit_door_number.getText().toString().trim(),
                            address.getId(),
                            et_edit_phoneNumber.getText().toString().trim(),
                            province,
                            city,
                            area,
                            latitude,
                            longitude,
                            et_edit_name.getText().toString().trim(),
                            addressBase
                            );
                }

                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==500&&resultCode==RESULT_OK){
            province=data.getStringExtra("province");
            city=data.getStringExtra("city");
            area=data.getStringExtra("area");
            latitude=data.getStringExtra("latitude");
            longitude=data.getStringExtra("longitude");
            addressBase=data.getStringExtra("address");
            tv_edit_base_address.setText(addressBase);
        }
    }

    private boolean checkInput(){
        if(TextUtils.isEmpty(et_edit_name.getText().toString())){
            MessageUtils.showShortToast(this, "姓名为空");
            return false;
        }
        if(TextUtils.isEmpty(et_edit_phoneNumber.getText().toString())){
            MessageUtils.showShortToast(this,"联系方式为空");
            return false;
        }
        if(TextUtils.isEmpty(et_edit_door_number.getText().toString())){
            MessageUtils.showShortToast(this,"详细地址为空");
            return false;
        }
        if(TextUtils.isEmpty(tv_edit_base_address.getText().toString())){
            MessageUtils.showShortToast(this,"基本地址为空");
            return false;
        }
        return true;
    }


    @Override
    public String setupToolBarTitle() {
        return "编辑地址";
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

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("删除地址");
        mRightText.setTextColor(Color.WHITE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adao.requestDelteReceiveAddress(address.getId());
            }
        });
        return true;
    }
}
