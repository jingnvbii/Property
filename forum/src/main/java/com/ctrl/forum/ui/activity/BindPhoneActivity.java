package com.ctrl.forum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import java.io.Serializable;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 绑定手机号页面 activity
 * Created by Eric on 2015/11/23.
 * */
public class BindPhoneActivity extends AppToolBarActivity implements View.OnClickListener {
   @InjectView(R.id.et_put_phone)
    EditText et_put_phone;
   @InjectView(R.id.et_yanzhen_code)
    EditText et_yanzhen_code;
   @InjectView(R.id.tv_get_yanzhencode)
    TextView tv_get_yanzhencode;
   @InjectView(R.id.tv_bind_phone)
    TextView tv_bind_phone;
    private RegisteDao rdao;
    private String code;
    private String openId;
    private String thirdLoginType;
    private LoginDao ldao;
    private String deviceImei;
    private MemberInfo memberInfo;
    private String lontitude;
    private String latitude;
    private String address;

    private TimeCount time;
    private BindPhoneActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_phone_activity);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initData() {
        rdao=new RegisteDao(this);
        ldao=new LoginDao(this);
        activity = new BindPhoneActivity();
        openId=getIntent().getStringExtra("openId");
        thirdLoginType=getIntent().getStringExtra("thirdLoginType");
        deviceImei=getIntent().getStringExtra("deviceImei");
        lontitude=getIntent().getStringExtra("lontitude");
        latitude=getIntent().getStringExtra("latitude");
        address=getIntent().getStringExtra("address");
    }

    private void initView() {
        tv_bind_phone.setOnClickListener(this);
        tv_get_yanzhencode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_bind_phone:
                if(checkInput()){
                    MessageUtils.showShortToast(this,"手机号绑定成功");
                    ldao.requestLogin("0",et_put_phone.getText().toString().trim(),"",deviceImei,"1",openId,thirdLoginType);
                }
                break;
            case R.id.tv_get_yanzhencode:
                if(et_put_phone.getText().toString().trim().length()!=11){
                    MessageUtils.showShortToast(this,"手机号格式不对");
                    return;
                }
                if(!TextUtils.isEmpty(et_put_phone.getText().toString())) {
                    rdao.requestAuthCode(et_put_phone.getText().toString().trim());
                }else {
                    MessageUtils.showShortToast(this,"请输入手机号");
                }
                break;
        }

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==1) {
            code = rdao.getCode();
        }
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
    }

    private void setAlias(){
        String alias = Arad.preferences.getString("memberId");
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(BindPhoneActivity.this, "格式不对", Toast.LENGTH_SHORT).show();
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

    private boolean checkInput() {
        if(TextUtils.isEmpty(et_put_phone.getText().toString())){
            MessageUtils.showShortToast(this,"请输入手机号");
            return false;
        }
        if(TextUtils.isEmpty(et_yanzhen_code.getText().toString())){
            MessageUtils.showShortToast(this,"请输入验证码");
            return false;
        }

        if(!code.equals(et_yanzhen_code.getText().toString().trim())){
            MessageUtils.showShortToast(this,"验证码不一致");
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
            tv_get_yanzhencode.setText("获取验证码");
            tv_get_yanzhencode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            if (!activity.isFinishing()) {
                tv_get_yanzhencode.setClickable(false);//防止重复点击
                tv_get_yanzhencode.setText(millisUntilFinished / 1000 + "s");
            } else {
                time.cancel();
            }
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
        return "绑定手机号";
    }
}
