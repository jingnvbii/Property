package com.ctrl.forum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.LoginDao;
import com.ctrl.forum.entity.MemberInfo;

import butterknife.ButterKnife;

/**
 * 登录 activity
 * Created by Eric on 2015/11/23.
 * */
public class LoginActivity extends AppToolBarActivity implements View.OnClickListener{


    private TextView tv_register;//注册按钮
    private EditText et_username;//用户名
    private TextView et_pass_word;//密码
    private TextView tv_login;//登录
    private TextView tv_forget;//忘记密码
    private ImageView iv_weibo;//微博
    private ImageView iv_qqzone;//qq空间
    private ImageView iv_weixin;//微信
    private LoginDao ldao;
    private MemberInfo memberInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.inject(this);
        initView();
    }

    /**
     * 初始化方法
     * */
    private void initView(){
       tv_register=(TextView)findViewById(R.id.tv_register);
       et_username=(EditText)findViewById(R.id.et_username);
       et_pass_word=(TextView)findViewById(R.id.et_pass_word);
       tv_login=(TextView)findViewById(R.id.tv_login);
       tv_forget=(TextView)findViewById(R.id.tv_forget);
       iv_weibo=(ImageView)findViewById(R.id.iv_weibo);
       iv_qqzone=(ImageView)findViewById(R.id.iv_qqzone);
       iv_weixin=(ImageView)findViewById(R.id.iv_weixin);

       tv_register.setOnClickListener(this);
       tv_login.setOnClickListener(this);
       tv_forget.setOnClickListener(this);
       iv_weibo.setOnClickListener(this);
       iv_qqzone.setOnClickListener(this);
       iv_weixin.setOnClickListener(this);


        ldao=new LoginDao(this);

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==0){
            memberInfo=ldao.getMemberInfo();
            Arad.preferences.putString("memberId", memberInfo.getId());
            Arad.preferences.flush();
            MessageUtils.showShortToast(this,"登录成功");
              Intent intent02=new Intent(this,MainActivity.class);
                startActivity(intent02);
                AnimUtil.intentSlidIn(this);
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        if(errorNo.equals("002")){
            MessageUtils.showShortToast(this, "账号或密码错误");
        }

     /*   if(errorNo.equals("003")){
            MessageUtils.showShortToast(this,"登录成功");
            Intent intent02=new Intent(this,MainActivity.class);
            startActivity(intent02);
            AnimUtil.intentSlidIn(this);
        }*/


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_register :
                Intent intent01=new Intent(this,RegisterActivity.class);
                startActivity(intent01);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_login :
                if(checkInput()){
                    ldao.requestLogin(et_username.getText().toString().trim(),et_pass_word.getText().toString().trim(),"1");
                }
                break;
            case R.id.tv_forget :
                break;
            case R.id.iv_weibo :
                break;
            case R.id.iv_qqzone :
                break;
            case R.id.iv_weixin :
                break;
        }

    }

    private boolean checkInput(){
          if(TextUtils.isEmpty(et_username.getText().toString().trim())){
              MessageUtils.showShortToast(this,"用户名不可为空");
              return false;
          }
          if(TextUtils.isEmpty(et_pass_word.getText().toString().trim())){
              MessageUtils.showShortToast(this,"密码不可为空");
              return false;
          }

        return true;
    }


}
