package com.ctrl.forum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.KeyDao;
import com.ctrl.forum.dao.RegisteDao;
import com.ctrl.forum.entity.ItemValues;
import com.ctrl.forum.utils.RegexpUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 注册页面 activity
 * Created by jason on 2016/4/7.
 */
public class RegisterActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.et_username)//手机号
            EditText et_username;
    @InjectView(R.id.et_pass_word)//密码
            EditText et_pass_word;
    @InjectView(R.id.et_auth_code)//验证码
            EditText et_auth_code;
    @InjectView(R.id.et_verify)//确认密码
            EditText et_verify;
    @InjectView(R.id.tv_login) //注册按钮
            TextView tv_login;
    @InjectView(R.id.checkbox)//单选按钮
            CheckBox checkbox;
    @InjectView(R.id.tv_auth_code)//获取验证码控件
            TextView tv_auth_code;
    @InjectView(R.id.tv_user_agreement)//《用户使用协议》
            TextView tv_user_agreement;
    private TimeCount time;
    private RegisteDao rdao;
    private String code;
    private RegisterActivity activity;
    private KeyDao kdao;
    private ItemValues itemValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        time = new TimeCount(60000, 1000);
        tv_auth_code.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_user_agreement.setOnClickListener(this);
        rdao = new RegisteDao(this);
        activity = new RegisterActivity();
        kdao = new KeyDao(this);

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 0) {
           MessageUtils.showShortToast(this, "注册成功");
            finish();
        }

        if (requestCode == 1) {
            code = rdao.getCode();
          //  MessageUtils.showShortToast(this, "获取短信验证码成功" + code);
        }

        if (requestCode == 66) {
            itemValues = kdao.getItemValues();
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("data",itemValues.getItemValue());
            intent.putExtra("title","用户使用协议");
            startActivity(intent);
        }
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
    public String setupToolBarTitle() {
        return getResources().getString(R.string.register);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_auth_code:
                if (checkInput(et_username)) {
                    if (RegexpUtil.isMobileNO(et_username.getText().toString().trim())) {
                        rdao.requestAuthCode(et_username.getText().toString().trim());
                        time.start();
                    } else {
                        MessageUtils.showShortToast(this, "手机号格式不正确");

                    }
                } else {
                    MessageUtils.showShortToast(this, "手机号为空");
                }
                break;

            case R.id.tv_login:
                if (checkAllInput()) {
                    rdao.requestRegiste(et_username.getText().toString().trim(), et_pass_word.getText().toString().trim(), "");
                }
            case R.id.tv_user_agreement:
                kdao.ueryDictionary("APP_PROTOCOL"); //用户使用协议
                break;
        }

    }


    /*
    * 输入检测
    * */
    public boolean checkInput(EditText et) {
        if (TextUtils.isEmpty(et.getText().toString())) return false;
        return true;

    }

    /*
    * 输入检测
    * */
    public boolean checkAllInput() {
        if (TextUtils.isEmpty(et_username.getText().toString())) {
            MessageUtils.showShortToast(this, "手机号为空");
            return false;
        }
        if (TextUtils.isEmpty(et_auth_code.getText().toString())) {
            MessageUtils.showShortToast(this, "验证码为空");
            return false;
        }
        if (TextUtils.isEmpty(et_pass_word.getText().toString())) {
            MessageUtils.showShortToast(this, "密码为空");
            return false;
        }
        if (et_pass_word.getText().toString().length() < 6) {
            MessageUtils.showShortToast(this, "密码为长度大于6的数字或字母");
            return false;
        }

        if (TextUtils.isEmpty(et_verify.getText().toString())) {
            MessageUtils.showShortToast(this, "确认密码为空");
            return false;
        }

        if (!et_verify.getText().toString().trim().equals(et_pass_word.getText().toString().trim())) {
            MessageUtils.showShortToast(this, "两次输入密码不一致");
            return false;
        }

        if (!et_auth_code.getText().toString().trim().equals(code)) {
            MessageUtils.showShortToast(this, "验证码错误");
            return false;
        }

        if (!checkbox.isChecked()) {
            MessageUtils.showShortToast(this, "请仔细阅读用户协议");
            return false;
        }

        return true;

    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            tv_auth_code.setText("获取验证码");
            tv_auth_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            if (!activity.isFinishing()) {
                tv_auth_code.setClickable(false);//防止重复点击
                tv_auth_code.setText(millisUntilFinished / 1000 + "s");
            } else {
                time.cancel();
            }
        }
    }

}
