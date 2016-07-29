package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.LoginDao;
import com.ctrl.forum.utils.InputMethodUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 输入新密码
 */
public class MineNewpwdActivity extends AppToolBarActivity {

    private String userName;
    @InjectView(R.id.et_input)
    EditText et_input;  //输入密码
    @InjectView(R.id.et_ok)
    EditText et_ok;    //确认密码
    private LoginDao ldao;
    @InjectView(R.id.ll_all)
    LinearLayout ll_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_newpwd);
        ButterKnife.inject(this);
        init();
    }

    //初始化数据
    private void init() {
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        ldao = new LoginDao(this);

        ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtils.hide(MineNewpwdActivity.this);
            }
        });
    }

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
    public String setupToolBarTitle() {
        return getResources().getString(R.string.input_newpwd);
    }

    public Boolean checkInput(){
        if (TextUtils.isEmpty(et_input.getText().toString().trim())){
            MessageUtils.showShortToast(this, "输入密码为空");
            return false;}
        if (TextUtils.isEmpty(et_ok.getText().toString().trim())){
            MessageUtils.showShortToast(this, "确认密码为空");
            return false;}
        if (et_input.getText().toString().length() < 6) {
            MessageUtils.showShortToast(this, "密码为长度大于6的数字或字母");
            return false;
        }
        if (!et_input.getText().toString().trim().equals(et_ok.getText().toString().trim())) {
            MessageUtils.showShortToast(this, "两次输入密码不一致");
            return false;
        }
        return true;
    }

    public void onClick(View V){
        if (checkInput()){
           ldao.requestChangePassword(userName, et_input.getText().toString().trim());
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 1) {
            MessageUtils.showShortToast(this, "修改密码成功");
            finish();
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        if(errorNo.equals("002")){
            MessageUtils.showShortToast(this, "该手机号尚未注册");
        }
    }
}
