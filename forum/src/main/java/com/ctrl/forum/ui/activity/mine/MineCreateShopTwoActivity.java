package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.StoreManageDao;
import com.ctrl.forum.utils.InputMethodUtils;

/**
 * 我的店铺_马上申请我的店铺
 */
public class MineCreateShopTwoActivity extends AppToolBarActivity implements View.OnClickListener{
    private EditText et_shop_name,et_address,et_apply_name,et_shop_phone;
    private TextView tv_num,tv_commit;
    private StoreManageDao storeManageDao;
    private LinearLayout ll_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_shop_two);

        init();
    }

    public Boolean checkInputAll(){
        if (TextUtils.isEmpty(et_shop_name.getText().toString())){
            MessageUtils.showShortToast(this,"店铺名称不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(et_address.getText().toString())){
            MessageUtils.showShortToast(this,"联系地址不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(et_apply_name.getText().toString())){
            MessageUtils.showShortToast(this,"申请人姓名不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(et_shop_phone.getText().toString())){
            MessageUtils.showShortToast(this,"联系方式不能为空!");
            return false;
        }
        return true;
    }

    private void init() {
        et_shop_name = (EditText) findViewById(R.id.et_shop_name);
        et_address = (EditText) findViewById(R.id.et_shop_address);
        et_apply_name = (EditText) findViewById(R.id.et_apply_name);
        et_shop_phone = (EditText) findViewById(R.id.et_shop_phone);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        ll_all = (LinearLayout) findViewById(R.id.ll_all);

        et_shop_name.setOnClickListener(this);
        et_address.setOnClickListener(this);
        et_apply_name.setOnClickListener(this);
        et_shop_phone.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
        ll_all.setOnClickListener(this);

        storeManageDao = new StoreManageDao(this);
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {return getResources().getString(R.string.apply_shop);}

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_commit:
                if (checkInputAll()) {
                    storeManageDao.requestChangeBasicInfo(Arad.preferences.getString("memberId"), et_address.getText().toString(),
                            et_apply_name.getText().toString(), et_shop_phone.getText().toString(),
                            et_shop_name.getText().toString());
                }
            break;
            case R.id.ll_all:
                InputMethodUtils.hide(MineCreateShopTwoActivity.this);
                break;
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==0){
            Intent intent = new Intent(this,MineCreateShopThreeActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
