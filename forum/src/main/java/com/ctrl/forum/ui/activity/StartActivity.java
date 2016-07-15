package com.ctrl.forum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.dao.LoginDao;
import com.ctrl.forum.entity.Advertising;
import com.ctrl.forum.entity.NavigationBar;
import com.ctrl.forum.entity.StartAds;
import com.ctrl.forum.ui.adapter.StartImageViewPagerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;

/**
 * 开始页面 activity
 * Created by Eric on 2015/11/23.
 * */
public class StartActivity extends ToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.iv_start)
    ImageView iv_start;
    @InjectView(R.id.viewPager_start)
    ViewPager viewPager_start;
    @InjectView(R.id.ll_icons)
    LinearLayout ll_icons;
    @InjectView(R.id.tv_enter)
    TextView tv_enter;
    private List<View> views = new ArrayList<View>();
    private LoginDao ldao;
    private List<NavigationBar> listNavigationBar;
    private LayoutInflater inflater;
    private int currentItem;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int position = (int) msg.obj + 1;
                    break;
                case 2:
                    //设置当前页面
                    viewPager_start.setCurrentItem(currentItem);

                    break;
            }

            return false;
        }
    });
    private List<Advertising> listAdvertising;
    private StartAds startAds;
    private int len;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initView() {
        tv_enter.setOnClickListener(this);
    }

    private void initData() {
        inflater = getLayoutInflater();
        ldao=new LoginDao(this);
        ldao.requestQueryNavigationBar();
        /////////////////////主要配置//////////////////////////////////////
       /* container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager_start.dispatchTouchEvent(event);
            }
        });*/
        viewPager_start.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (viewPager_start != null) {
                    viewPager_start.invalidate();
                }
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < listAdvertising.size(); i++) {
                    ll_icons.getChildAt(i).setEnabled(false);
                }
                ll_icons.getChildAt(position).setEnabled(true);
                if(position==listAdvertising.size()-1){
                    tv_enter.setVisibility(View.VISIBLE);
                }else {
                    tv_enter.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });// 设置监听器
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
       if(requestCode==2){
           listNavigationBar=ldao.getListNavigationBar();
           listAdvertising=ldao.getListAdvertising();
           startAds=ldao.getStartAds();
           Boolean isFirstIn= Arad.preferences.getBoolean("isFirstIn",true);
         //  Log.i("tag", "isFirstin====" + isFirstIn);
           if(isFirstIn){//第一次进入
               Arad.preferences.putBoolean("isFirstIn", false);
               Arad.preferences.flush();
               iv_start.setVisibility(View.GONE);
               viewPager_start.setVisibility(View.VISIBLE);
               for (int i = 0; i < listAdvertising.size(); i++) {
                   View view = inflater.inflate(R.layout.commdity_image_item, null);
                   views.add(view);
               }
               //设置小圆点
               if (listAdvertising.size()>1) {
                   for (int i = 0; i < listAdvertising.size(); i++) {
                       ImageView imageView = new ImageView(this);
                       imageView.setImageResource(R.drawable.red_round_select);
                       imageView.setEnabled(false);
                       LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(15,15);
                       params.leftMargin=15;
                       ll_icons.addView(imageView,params);
                       ll_icons.getChildAt(0).setEnabled(true);//默认第一个为选中的情况
                   }
               }
               // 1.设置幕后item的缓存数目
               viewPager_start.setOffscreenPageLimit(listAdvertising.size());
               // 2.设置页与页之间的间距
               viewPager_start.setPageMargin(10);
               // 3.将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
               ////////////////////////////////////////////////////////////////
               viewPager_start.setAdapter(new StartImageViewPagerAdapter(views, listAdvertising)); // 为viewpager设置adapter


           }else {//不是第一次进入
              Arad.imageLoader.load(startAds.getKindIcon()).placeholder(R.mipmap.default_error).into(iv_start);
               Handler x = new Handler();
               x.postDelayed(new splashhandler(), 2000);
           }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_enter:
                Intent intent02=new Intent(StartActivity.this,MainActivity.class);
                intent02.putExtra("listNagationBar", (Serializable) ldao.getListNavigationBar());
                startActivity(intent02);
                AnimUtil.intentSlidIn(StartActivity.this);
                finish();
                break;
        }
    }

    //切换图片
    private class ViewPagerTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            currentItem = (currentItem + 1) % listAdvertising.size();
            //更新界面
        }

    }

    class splashhandler implements Runnable{
        public void run() {
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
