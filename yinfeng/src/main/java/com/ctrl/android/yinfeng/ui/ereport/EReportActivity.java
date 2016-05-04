package com.ctrl.android.yinfeng.ui.ereport;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.customview.ScrollerNumberPicker;
import com.ctrl.android.yinfeng.customview.SelectPicPopupWindow;
import com.ctrl.android.yinfeng.dao.ClassDao;
import com.ctrl.android.yinfeng.entity.Kind;
import com.ctrl.android.yinfeng.ui.adapter.JasonViewPagerAdapter;
import com.ctrl.android.yinfeng.ui.fragment.ReportFragment;
import com.ctrl.android.yinfeng.utils.StrConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class EReportActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.viewpager_report)//页面切换
            ViewPager viewpager_report;
    @InjectView(R.id.tv_all)//全部
            TextView tv_all;

    @InjectView(R.id.tv_processing)//处理中
            TextView tv_processing;

    @InjectView(R.id.tv_pending)//未处理
            TextView tv_pending;

    @InjectView(R.id.tv_end)//已处理
            TextView tv_end;





    List<Fragment> fragments=new ArrayList<>();
    List<Kind> eventKindIdList=new ArrayList<>();
    private JasonViewPagerAdapter viewPagerAdapter;

    private ImageView cursor;
    private int bmpw = 0; // 游标宽度
    private int offset = 0;// // 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private TextView mTitle;
    private SelectPicPopupWindow menuWindow;
    private ScrollerNumberPicker pv_report;
    private ClassDao cdao;
    private String kindId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ereport);
        ButterKnife.inject(this);
        init();
        


    }

    private void init() {
        cdao=new ClassDao(this);
        tv_pending.setOnClickListener(this);
        tv_processing.setOnClickListener(this);
        tv_all.setOnClickListener(this);
        tv_end.setOnClickListener(this);
        ReportFragment fragment01 = ReportFragment.newInstance(StrConstant.REPORT_ALL,kindId);
        ReportFragment fragment02 = ReportFragment.newInstance(StrConstant.REPORT_PENDING,kindId);
        ReportFragment fragment03 = ReportFragment.newInstance(StrConstant.REPORT_PROCESSING,kindId);
        ReportFragment fragment04 = ReportFragment.newInstance(StrConstant.REPORT_END,kindId);
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
                list.add(ReportFragment.newInstance(StrConstant.REPORT_ALL,kindId));
                list.add(ReportFragment.newInstance(StrConstant.REPORT_PENDING,kindId));
                list.add(ReportFragment.newInstance(StrConstant.REPORT_PROCESSING,kindId));
                list.add(ReportFragment.newInstance(StrConstant.REPORT_END,kindId));
                viewPagerAdapter.setPagerItems(list);
            }
        });
        viewpager_report.setAdapter(viewPagerAdapter);
        viewpager_report.setCurrentItem(0);
        viewpager_report.setOffscreenPageLimit(4);
        viewpager_report.setOnPageChangeListener(new MyPageChangeListener());


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
            case R.id.tv_all:
                tv_all.setTextColor (getResources().getColor(R.color.text_job_red));
                tv_pending.setTextColor(getResources().getColor(R.color.text_job_gray));
                tv_processing.setTextColor(getResources().getColor(R.color.text_job_gray));
                tv_end.setTextColor(getResources().getColor(R.color.text_job_gray));
                viewpager_report.setCurrentItem(0);
                break;
            case R.id.tv_pending:
                tv_all.setTextColor (getResources().getColor(R.color.text_job_gray));
                tv_pending.setTextColor(getResources().getColor(R.color.text_job_red));
                tv_processing.setTextColor(getResources().getColor(R.color.text_job_gray));
                tv_end.setTextColor(getResources().getColor(R.color.text_job_gray));
                viewpager_report.setCurrentItem(1);
                break;
            case R.id.tv_processing:
                tv_all.setTextColor (getResources().getColor(R.color.text_job_gray));
                tv_pending.setTextColor(getResources().getColor(R.color.text_job_gray));
                tv_processing.setTextColor(getResources().getColor(R.color.text_job_red));
                tv_end.setTextColor(getResources().getColor(R.color.text_job_gray));
                viewpager_report.setCurrentItem(2);
                break;
            case R.id.tv_end:
                tv_all.setTextColor (getResources().getColor(R.color.text_job_gray));
                tv_pending.setTextColor(getResources().getColor(R.color.text_job_gray));
                tv_processing.setTextColor(getResources().getColor(R.color.text_job_gray));
                tv_end.setTextColor(getResources().getColor(R.color.text_job_red));
                viewpager_report.setCurrentItem(3);
                break;
        }


    }

    public JasonViewPagerAdapter getAdapter(){
        return viewPagerAdapter;
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
                    tv_all.setTextColor (getResources().getColor(R.color.text_job_red));
                    tv_pending.setTextColor(getResources().getColor(R.color.text_job_gray));
                    tv_processing.setTextColor(getResources().getColor(R.color.text_job_gray));
                    tv_end.setTextColor(getResources().getColor(R.color.text_job_gray));
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                    }
                    tv_all.setTextColor (getResources().getColor(R.color.text_job_gray));
                    tv_pending.setTextColor(getResources().getColor(R.color.text_job_red));
                    tv_processing.setTextColor(getResources().getColor(R.color.text_job_gray));
                    tv_end.setTextColor(getResources().getColor(R.color.text_job_gray));
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                    }
                    tv_all.setTextColor (getResources().getColor(R.color.text_job_gray));
                    tv_pending.setTextColor(getResources().getColor(R.color.text_job_gray));
                    tv_processing.setTextColor(getResources().getColor(R.color.text_job_red));
                    tv_end.setTextColor(getResources().getColor(R.color.text_job_gray));
                    break;
                case 3:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, three, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, three, 0, 0);
                    }else if(currIndex==2){
                        animation=new TranslateAnimation(two,three,0,0);
                    }
                    tv_all.setTextColor (getResources().getColor(R.color.text_job_gray));
                    tv_pending.setTextColor(getResources().getColor(R.color.text_job_gray));
                    tv_processing.setTextColor(getResources().getColor(R.color.text_job_gray));
                    tv_end.setTextColor(getResources().getColor(R.color.text_job_red));
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
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(999==requestCode){
            eventKindIdList=cdao.getData();
            showType();
        }
    }

    private void showType() {
        //实例化SelectPicPopupWindow
        menuWindow = new SelectPicPopupWindow(EReportActivity.this, itemsOnClick);
        menuWindow.setData(eventKindIdList);
        //显示窗口
        menuWindow.showAtLocation(EReportActivity.this.findViewById(R.id.main),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置


    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                default:
                    break;
            }


        }

    };

    public void setKindId(String kindId){
        this.kindId=kindId;
        viewPagerAdapter.reLoad();
    }

    @Override
    public String setupToolBarTitle() {
        if(Arad.preferences.getString("grade").equals("2")||Arad.preferences.getString("grade").equals("1")){
//

        }else{
            mTitle = (TextView) findViewById(com.beanu.arad.R.id.toolbar_title);
            Drawable drawable= getResources().getDrawable(R.mipmap.xiala);
/// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTitle.setCompoundDrawables(null, null, drawable, null);
            mTitle.setCompoundDrawablePadding(10);
            mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(eventKindIdList.size()==0) {
                        cdao.requestData(StrConstant.EVT_RPT);
                    }else {
                        showType();
                    }


                }


            });
        }


        return "事件上报";
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
    public boolean setupToolBarRightButton(ImageView rightButton) {
        rightButton.setImageResource(R.mipmap.iconfont_tianjia);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EReportActivity.this,EReportAddActivity.class);
                startActivityForResult(intent, 555);
                AnimUtil.intentSlidIn(EReportActivity.this);
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==555&&resultCode==667){
            viewPagerAdapter.reLoad();
        }
    }
}
