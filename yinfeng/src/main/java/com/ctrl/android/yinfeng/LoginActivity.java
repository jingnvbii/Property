package com.ctrl.android.yinfeng;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.base.Constant;
import com.ctrl.android.yinfeng.base.MyApplication;
import com.ctrl.android.yinfeng.dao.LoginDao;
import com.ctrl.android.yinfeng.entity.StaffInfo;
import com.ctrl.android.yinfeng.utils.S;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 登录页面
 * Created by jason on 2015/10/12.
 */
public class LoginActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.et_user_name)//用户名
    EditText et_user_name;
    @InjectView(R.id.et_user_pwd)//密码
    EditText et_user_pwd;
    @InjectView(R.id.tv_login)//登录
    TextView tv_login;
    @InjectView(R.id.tv_forget_pwd)//忘记密码
    TextView tv_forget_pwd;
    @InjectView(R.id.cb_remeber)//记住密码
    CheckBox cb_remeber;
    private String username;
    private LoginDao loginDao;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.login_activity);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        tv_login.setOnClickListener(this);
        cb_remeber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_remeber.isChecked()) {
                    Arad.preferences.putBoolean("ISCHECKED", true);
                } else {
                    Arad.preferences.putBoolean("ISCHECKED", false);
                }
            }
        });


        if(Arad.preferences.getBoolean("ISCHECKED")) {
            cb_remeber.setChecked(true);
            String username = Arad.preferences.getString(Constant.USERNAME);
            String password = Arad.preferences.getString(Constant.PASSWORD);
            if(!S.isNull(username)){
                et_user_name.setText(username);
            }
            if(!S.isNull(password)){
                et_user_pwd.setText(password);
            }
            et_user_pwd.requestFocus();
            et_user_pwd.setSelection(et_user_pwd.getText().toString().length());
        }else {
            cb_remeber.setChecked(false);
        }


    }





    /**
     * 请求数据成功后 调用此方法
     * */
    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        if(0 == requestCode){
            //MessageUtils.showShortToast(this,"登陆成功");
            StaffInfo staffInfo = loginDao.getStaffInfo();
            Arad.preferences.putString("staffId",staffInfo.getStaffId());
            Arad.preferences.putString("communityId",staffInfo.getCommunityId());
            Arad.preferences.putString("grade",staffInfo.getGrade());
            Arad.preferences.putString("jobType",staffInfo.getJobType());
            if(cb_remeber.isChecked()) {
                Arad.preferences.putString(Constant.USERNAME, username);
                Arad.preferences.putString(Constant.PASSWORD, password);
            }else {
                Arad.preferences.remove(Constant.USERNAME);
                Arad.preferences.remove(Constant.PASSWORD);
            }
            Arad.preferences.flush();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            AnimUtil.intentSlidIn(LoginActivity.this);
            finish();
        }


    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        //super.onRequestFaild(errorNo, errorMessage);
        showProgress(false);
        if(errorNo.equals("005")){
            MessageUtils.showShortToast(this,"用户名、密码错误");
        }


    }

    @Override
    public void onClick(View v) {
        if(tv_login==v){
            if(checkInput()){
                username =  et_user_name.getText().toString();
                password =  et_user_pwd.getText().toString();
                loginDao  = new LoginDao(this);
                loginDao.requestLogin(username, password);
                showProgress(true);
            }
        }
    }

    private boolean checkInput(){
        if(S.isNull(et_user_name.getText().toString())){
            MessageUtils.showShortToast(this, "用户名不可为空");
            return false;
        }

        if(S.isNull(et_user_pwd.getText().toString())){
            MessageUtils.showShortToast(this, "密码不可为空");
            return false;
        }

        return true;
    }


}
