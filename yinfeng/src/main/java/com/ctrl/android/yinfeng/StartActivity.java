package com.ctrl.android.yinfeng;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.base.MyApplication;
import com.ctrl.android.yinfeng.dao.AdvertisingDao;
import com.ctrl.android.yinfeng.entity.Advertising;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;

/**
 * 快显页面
 * Created by jason on 2015/12/15.
 */
public class StartActivity extends AppToolBarActivity {
    @InjectView(R.id.iv_start)
    ImageView iv_start;
    private AdvertisingDao adao;
    private static  final String STF_TRANSITION_PAGE="STF_TRANSITION_PAGE";
    private List<Advertising> adverstingList;
    private Handler x;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.start_activity);
        adao=new AdvertisingDao(this);
        adao.requestAdvertising(STF_TRANSITION_PAGE);
        ButterKnife.inject(this);
        x = new Handler();
       /* Handler x = new Handler();
        x.postDelayed(new splashhandler(), 2000);*/


    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }

    /**
     * 请求数据成功后 调用此方法
     * */
    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        if(requestCode==666){
            adverstingList=adao.getAdvertisingList();
            Arad.imageLoader.load(adverstingList.get(0).getImgUrl()).into(iv_start);
            x.postDelayed(new splashhandler(), 2000);
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        iv_start.setImageResource(R.mipmap.yindaoye);
        x.postDelayed(new splashhandler(), 2000);
    }

    class splashhandler implements Runnable{
        public void run() {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            //Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            AnimUtil.intentSlidIn(StartActivity.this);
            finish();
        }

    }

}
