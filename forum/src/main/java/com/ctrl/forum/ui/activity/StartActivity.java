package com.ctrl.forum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.LoginDao;
import com.ctrl.forum.entity.NavigationBar;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;

/**
 * 开始页面 activity
 * Created by Eric on 2015/11/23.
 * */
public class StartActivity extends AppToolBarActivity {
    @InjectView(R.id.iv_start)
    ImageView iv_start;
    private LoginDao ldao;
    private List<NavigationBar> listNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        ButterKnife.inject(this);
        initData();

    }

    private void initData() {
        ldao=new LoginDao(this);
        ldao.requestQueryNavigationBar();
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==2){
            listNavigationBar=ldao.getListNavigationBar();
            Handler x = new Handler();
            x.postDelayed(new splashhandler(), 2000);
        }
    }

    class splashhandler implements Runnable{

        public void run() {
            SDKInitializer.initialize(getApplicationContext());
            JPushInterface.setDebugMode(true);
            JPushInterface.init(getApplicationContext());
          /*  Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);11
            AnimUtil.intentSlidIn(StartActivity.this);
            finish();*/
            Intent intent02=new Intent(StartActivity.this,MainActivity.class);
            intent02.putExtra("listNagationBar", (Serializable) ldao.getListNavigationBar());
            startActivity(intent02);
            AnimUtil.intentSlidIn(StartActivity.this);
            finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
}
