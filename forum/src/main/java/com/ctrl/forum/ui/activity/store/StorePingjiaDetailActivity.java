package com.ctrl.forum.ui.activity.store;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.ui.fragment.StorePingjiaDetailFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城评价详情 activity
* */

public class StorePingjiaDetailActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.iv_back)//返回键
    ImageView iv_back;
    @InjectView(R.id.viewpager_pingjia_detail)//viewpager
    ViewPager viewpager_pingjia_detail;
    @InjectView(R.id.radiogroup_detail)
    RadioGroup radiogroup_detail;

    @InjectView(R.id.rb_all)//全部评价
    RadioButton   rb_all;
    @InjectView(R.id.rb_good)//全部评价
    RadioButton   rb_good;
    @InjectView(R.id.rb_medium)//全部评价
    RadioButton   rb_medium;
    @InjectView(R.id.rb_bad)//全部评价
    RadioButton   rb_bad;

   SparseArray<Fragment>fragments=new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_pingjia_detail);
        // 隐藏输入法
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.inject(this);
        initView();
    }


    public void setNum(String allNum,String goodNum,String mediumNum,String badNum){
        rb_all.setText("全部"+"("+allNum+")");
        rb_good.setText("好评"+"("+goodNum+")");
        rb_medium.setText("中评"+"("+mediumNum+")");
        rb_bad.setText("差评"+"("+badNum+")");
    }


    private void initView() {
        rb_all.setOnClickListener(this);
        rb_good.setOnClickListener(this);
        rb_medium.setOnClickListener(this);
        rb_bad.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        for(int i=0;i<4;i++){
            StorePingjiaDetailFragment mStorePingjiaDetailFragment=StorePingjiaDetailFragment.newInstance(i);
            fragments.put(i, mStorePingjiaDetailFragment);
            }
        rb_all.setTextColor(getResources().getColor(R.color.text_red));

        viewpager_pingjia_detail.setAdapter(new com.ctrl.forum.ui.adapter.ViewPagerAdapter(getSupportFragmentManager(), fragments));
      //  viewpager_pingjia_detail.setOnPageChangeListener(new FragmentOnPageChangeListener());
        viewpager_pingjia_detail.setCurrentItem(0);
        viewpager_pingjia_detail.setOffscreenPageLimit(4);
        viewpager_pingjia_detail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               switch (position){
                   case 0:
                       rb_all.setTextColor(getResources().getColor(R.color.text_red));
                       rb_good.setTextColor(getResources().getColor(R.color.text_black));
                       rb_medium.setTextColor(getResources().getColor(R.color.text_black));
                       rb_bad.setTextColor(getResources().getColor(R.color.text_black));
                       break;
                   case 1:
                       rb_all.setTextColor(getResources().getColor(R.color.text_black));
                       rb_good.setTextColor(getResources().getColor(R.color.text_red));
                       rb_medium.setTextColor(getResources().getColor(R.color.text_black));
                       rb_bad.setTextColor(getResources().getColor(R.color.text_black));
                       break;
                   case 2:
                       rb_all.setTextColor(getResources().getColor(R.color.text_black));
                       rb_good.setTextColor(getResources().getColor(R.color.text_black));
                       rb_medium.setTextColor(getResources().getColor(R.color.text_red));
                       rb_bad.setTextColor(getResources().getColor(R.color.text_black));
                       break;
                   case 3:
                       rb_all.setTextColor(getResources().getColor(R.color.text_black));
                       rb_good.setTextColor(getResources().getColor(R.color.text_black));
                       rb_medium.setTextColor(getResources().getColor(R.color.text_black));
                       rb_bad.setTextColor(getResources().getColor(R.color.text_red));
                       break;
               }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }

    /**
     * ViewPager的PageChangeListener(页面改变的监听器)
     */
    private class FragmentOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }
        /**
         * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
         */
        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            RadioButton radioButton = (RadioButton) findViewById(position);
            radioButton.performClick();

        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.rb_all:
                viewpager_pingjia_detail.setCurrentItem(0);
                rb_all.setTextColor(getResources().getColor(R.color.text_red));
                rb_good.setTextColor(getResources().getColor(R.color.text_black));
                rb_medium.setTextColor(getResources().getColor(R.color.text_black));
                rb_bad.setTextColor(getResources().getColor(R.color.text_black));
                break;
            case R.id.rb_good:
                rb_all.setTextColor(getResources().getColor(R.color.text_black));
                viewpager_pingjia_detail.setCurrentItem(1);
                rb_good.setTextColor(getResources().getColor(R.color.text_red));
                rb_medium.setTextColor(getResources().getColor(R.color.text_black));
                rb_bad.setTextColor(getResources().getColor(R.color.text_black));
                break;
            case R.id.rb_medium:
                rb_all.setTextColor(getResources().getColor(R.color.text_black));
                viewpager_pingjia_detail.setCurrentItem(2);
                rb_good.setTextColor(getResources().getColor(R.color.text_black));
                rb_medium.setTextColor(getResources().getColor(R.color.text_red));
                rb_bad.setTextColor(getResources().getColor(R.color.text_black));
                break;
            case R.id.rb_bad:
                rb_all.setTextColor(getResources().getColor(R.color.text_black));
                viewpager_pingjia_detail.setCurrentItem(3);
                rb_good.setTextColor(getResources().getColor(R.color.text_black));
                rb_medium.setTextColor(getResources().getColor(R.color.text_black));
                rb_bad.setTextColor(getResources().getColor(R.color.text_red));
                break;
        }


    }


    @Override
    public String setupToolBarTitle() {
        TextView tv = getmTitle();
        Drawable drawable = getResources().getDrawable(R.mipmap.locate_img);
        drawable.setBounds(0,0,32,32);
        tv.setCompoundDrawables(drawable, null, null, null);
        tv.setCompoundDrawablePadding(10);
        tv.setTextColor(Color.WHITE);
        tv.setText("中润世纪广场");
        return tv.getText().toString();
    } 

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left_white);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("地图");
        mRightText.setTextColor(Color.WHITE);
        return true;
    }
}
