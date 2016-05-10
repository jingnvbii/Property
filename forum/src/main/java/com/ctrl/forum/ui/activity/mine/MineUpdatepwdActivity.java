package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.RegisteDao;
import com.ctrl.forum.utils.RegexpUtil;

/**
 * 忘记/修改密码
 */
public class MineUpdatepwdActivity extends AppToolBarActivity implements View.OnClickListener{
    private TextView tv_next;
    private TextView get_test;
    private EditText et_phone;
    private EditText et_test;
    private MineUpdatepwdActivity activity;
    private RegisteDao rdao;
    private TimeCount time;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_updatepwd);
        init();

    }

    private void init() {
        activity = new MineUpdatepwdActivity();
        time = new TimeCount(60000, 1000);
        rdao = new RegisteDao(this);
        tv_next = (TextView) findViewById(R.id.tv_next);
        get_test = (TextView) findViewById(R.id.get_test);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_test = (EditText) findViewById(R.id.et_test);

        get_test.setOnClickListener(this);
        tv_next.setOnClickListener(this);
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
        return getResources().getString(R.string.updatepwd);
    }

    public Boolean checkInput(EditText et){
        if (TextUtils.isEmpty(et.getText().toString().trim())){return false;}
          return true;
    }

    public Boolean checkAll(){
        if (TextUtils.isEmpty(et_phone.getText().toString().trim())){
            MessageUtils.showShortToast(this, "手机号不能为空" + code);
            return false;}
        if (TextUtils.isEmpty(et_test.getText().toString().trim())){
            MessageUtils.showShortToast(this, "验证码不能为空" + code);
            return false;}
        if (!et_test.getText().toString().trim().equals(code)) {
            MessageUtils.showShortToast(this, "验证码错误");
            return false;}
        return true;
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 1) {
            code = rdao.getCode();
            MessageUtils.showShortToast(this, "获取短信验证码成功" + code);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id){
            case R.id.tv_next:
                if (checkAll()){
                    intent = new Intent(getApplicationContext(),MineNewpwdActivity.class);
                    intent.putExtra("userName",et_phone.getText().toString().trim());
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.get_test:
                if (checkInput(et_phone)){
                    if (RegexpUtil.isMobileNO(et_phone.getText().toString().trim())) {
                        rdao.requestAuthCode(et_phone.getText().toString().trim());
                        time.start();
                    } else {
                        MessageUtils.showShortToast(this, "手机号格式不正确");
                    }
                }else
                {
                    MessageUtils.showShortToast(this, "手机号不能为空");
                }
                break;
           default:
               break;
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            get_test.setText("点击获取验证码");
            get_test.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            if (!activity.isFinishing()) {
                get_test.setClickable(false);//防止重复点击
                get_test.setText(millisUntilFinished / 1000 + "s后重新获取");
            } else {
                time.cancel();
            }
        }
    }
}
