package com.ctrl.android.yinfeng.ui.job;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.dao.JobDao;
import com.ctrl.android.yinfeng.ui.adapter.JasonViewPagerAdapter;
import com.ctrl.android.yinfeng.ui.fragment.JobFragment;
import com.ctrl.android.yinfeng.utils.StrConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyJobActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.viewpager_job)//页面切换
    ViewPager viewpager_job;
    @InjectView(R.id.tv_pending)//待处理
    TextView tv_pending;
    @InjectView(R.id.tv_processing)//处理中
    TextView tv_processing;
    @InjectView(R.id.tv_pingjia)//待评价
    TextView tv_pingjia;
    @InjectView(R.id.tv_end)//已处理
    TextView tv_end;


    List<Fragment> fragments=new ArrayList<>();
    private JasonViewPagerAdapter viewPagerAdapter;

    private ImageView cursor;
    private int bmpw = 0; // 游标宽度
    private int offset = 0;// // 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private String money;
    private String repairId;
    private JobDao jdao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);
        ButterKnife.inject(this);
        init();
    }


    private void init() {
        jdao=new JobDao(this);
        tv_pending.setOnClickListener(this);
        tv_processing.setOnClickListener(this);
        tv_pingjia.setOnClickListener(this);
        tv_end.setOnClickListener(this);
        tv_pending.setSelected(true);
        JobFragment fragment01 = JobFragment.newInstance(StrConstant.JOB_PENDING);
        JobFragment fragment02 = JobFragment.newInstance(StrConstant.JOB_PROGRESSING);
        JobFragment fragment03 = JobFragment.newInstance(StrConstant.JOB_PROGRESSED);
        JobFragment fragment04 = JobFragment.newInstance(StrConstant.JOB_END);
        fragments.add(fragment01);
        fragments.add(fragment02);
        fragments.add(fragment03);
        fragments.add(fragment04);

        //初始化指示器位置
        initCursorPos();
        viewPagerAdapter = new JasonViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPagerAdapter.setOnReloadListener(new JasonViewPagerAdapter.OnReloadListener() {
            @Override
            public void onReload() {
                fragments = null;
                List<Fragment> list = new ArrayList<Fragment>();
                list.add(JobFragment.newInstance(StrConstant.JOB_PENDING));
                list.add(JobFragment.newInstance(StrConstant.JOB_PROGRESSING));
                list.add(JobFragment.newInstance(StrConstant.JOB_PROGRESSED));
                list.add(JobFragment.newInstance(StrConstant.JOB_END));
                viewPagerAdapter.setPagerItems(list);
            }
        });
        viewpager_job.setAdapter(viewPagerAdapter);
        viewpager_job.setCurrentItem(0);
        viewpager_job.setOffscreenPageLimit(4);
        viewpager_job.setOnPageChangeListener(new MyPageChangeListener());
    }

    //初始化指示器位置
    public void initCursorPos() {
        // 初始化动画
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpw = BitmapFactory.decodeResource(getResources(), R.mipmap.line)
                .getWidth();// 获取图片宽度

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / fragments.size() - bmpw) / 2;// 计算偏移量

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_pending:
                tv_pending.setSelected(true);
                tv_processing.setSelected(false);
                tv_pingjia.setSelected(false);
                tv_end.setSelected(false);
                viewpager_job.setCurrentItem(0);
                break;
            case R.id.tv_processing:
                tv_pending.setSelected(false);
                tv_processing.setSelected(true);
                tv_pingjia.setSelected(false);
                tv_end.setSelected(false);
                viewpager_job.setCurrentItem(1);
                break;
            case R.id.tv_pingjia:
                tv_pending.setSelected(false);
                tv_processing.setSelected(false);
                tv_pingjia.setSelected(true);
                tv_end.setSelected(false);
                viewpager_job.setCurrentItem(2);
                break;
            case R.id.tv_end:
                tv_pending.setSelected(false);
                tv_processing.setSelected(false);
                tv_pingjia.setSelected(false);
                tv_end.setSelected(true);
                viewpager_job.setCurrentItem(3);
                break;
        }


    }


    //页面改变监听器
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpw;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量
        int three = one * 3;// 页卡1 -> 页卡4 偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }else if(currIndex==3){
                        animation = new TranslateAnimation(three, 0, 0, 0);
                    }
                    tv_pending.setSelected(true);
                tv_processing.setSelected(false);
                tv_pingjia.setSelected(false);
                tv_end.setSelected(false);
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                    }
                tv_pending.setSelected(false);
                tv_processing.setSelected(true);
                tv_pingjia.setSelected(false);
                tv_end.setSelected(false);
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                    }
                tv_pending.setSelected(false);
                tv_processing.setSelected(false);
                tv_pingjia.setSelected(true);
                tv_end.setSelected(false);
                    break;
                case 3:
                   // changeSelectedTextColor(tv_end);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, three, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, three, 0, 0);
                    }else if(currIndex==2){
                        animation=new TranslateAnimation(two,three,0,0);
                    }

                tv_pending.setSelected(false);
                tv_processing.setSelected(false);
                tv_pingjia.setSelected(false);
                tv_end.setSelected(true);
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==33&&resultCode==RESULT_OK){
            viewPagerAdapter.reLoad();
        }
    }

    public void setMoney(String money,String id){
        this.money=money;
        this.repairId=id;
        jdao.requestPaOffLine(repairId,money);
    }


    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(4==requestCode){
            MessageUtils.showShortToast(this,"线下支付成功");
            viewPagerAdapter.reLoad();
        }
    }

    public JasonViewPagerAdapter getAdapter(){
        return viewPagerAdapter;
    }


    @Override
    public String setupToolBarTitle() {
        return "我的工单";
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
}


