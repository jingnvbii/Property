package com.ctrl.forum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.LoginDao;
import com.ctrl.forum.dao.RegisteDao;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.ui.activity.rim.ExampleUtil;
import com.ctrl.forum.utils.RegexpUtil;

import java.io.Serializable;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

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
    private String openId;
    private String thirdLoginType;
    private String tel;
    private LoginDao ldao;
    private String deviceImei;
    private String lontitude;
    private String latitude;
    private String address;
    private MemberInfo memberInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initData() {
        ldao=new LoginDao(this);
        openId=getIntent().getStringExtra("openId");
        thirdLoginType=getIntent().getStringExtra("thirdLoginType");
        tel=getIntent().getStringExtra("tel");
        deviceImei=getIntent().getStringExtra("deviceImei");
        lontitude=getIntent().getStringExtra("lontitude");
        latitude=getIntent().getStringExtra("latitude");
        address=getIntent().getStringExtra("address");

        if(tel!=null){
            et_username.setText(tel);
        }

    }

    private void initView() {
        time = new TimeCount(60000, 1000);
        tv_auth_code.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_user_agreement.setOnClickListener(this);
        rdao = new RegisteDao(this);
        activity = new RegisterActivity();
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==0){
            memberInfo=ldao.getMemberInfo();
            Arad.preferences.putString("memberId", memberInfo.getId());
            Arad.preferences.putString("nickName", memberInfo.getMemberName());
            Arad.preferences.putString("mobile", memberInfo.getMobile());
            Arad.preferences.putString("point", memberInfo.getPoint());
            Arad.preferences.putString("remark", memberInfo.getRemark());
            Arad.preferences.putString("memberLevel", memberInfo.getMemberLevel());

            if (memberInfo.getImgUrl()==null){
                Arad.preferences.putString("imgUrl","");
            }else{
                Arad.preferences.putString("imgUrl", memberInfo.getImgUrl());
            }

            Arad.preferences.putString("imgUrl", memberInfo.getImgUrl());
            Arad.preferences.putString("address", memberInfo.getAddress());
            Arad.preferences.putString("companyId", memberInfo.getCompanyId());
            Arad.preferences.putString("communityName", memberInfo.getCommunityName());
            Arad.preferences.putString("communityId",memberInfo.getCommunityId());
            Arad.preferences.putString("isShielded",memberInfo.getIsShielded());
            Arad.preferences.putString("latitude", latitude);
            Arad.preferences.putString("lontitude", lontitude);
            Arad.preferences.putString("address", address);
            Arad.preferences.flush();

            Intent intent02=new Intent(this,MainActivity.class);
            intent02.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent02.putExtra("listNagationBar", (Serializable) ldao.getListNavigationBar());
            startActivity(intent02);
            AnimUtil.intentSlidIn(this);
            setAlias();
        }
        if (requestCode == 666) {
            MessageUtils.showShortToast(this, "注册成功");
            if(openId!=null){
                ldao.requestLogin("0",et_username.getText().toString().trim(),"",deviceImei,"1",openId,thirdLoginType);
            }
            finish();
        }

        if (requestCode == 1) {
            code = rdao.getCode();
          //  MessageUtils.showShortToast(this, "获取短信验证码成功" + code);
        }
    }

    private void setAlias(){
        String alias = Arad.preferences.getString("memberId");
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(RegisterActivity.this, "格式不对", Toast.LENGTH_SHORT).show();
            return;
        }
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final String TAG = "JPush";
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            // ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
            //ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

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
                    rdao.requestRegiste(et_username.getText().toString().trim(), et_pass_word.getText().toString().trim(), "",openId,thirdLoginType);
                }
                break;
            case R.id.tv_user_agreement:
                Intent intent = new Intent(this,WebViewActivity.class);
                intent.putExtra("title","用户使用协议");
                startActivity(intent);
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
