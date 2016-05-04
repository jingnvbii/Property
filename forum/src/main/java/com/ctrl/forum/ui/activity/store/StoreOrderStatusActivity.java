package com.ctrl.forum.ui.activity.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.ui.adapter.ViewPagerAdapter;
import com.ctrl.forum.ui.fragment.StoreOrderDetailFragment;
import com.ctrl.forum.ui.fragment.StoreOrderStatusFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城订单状态 activity
* */

public class StoreOrderStatusActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.iv_order_status_back)//返回按钮
            ImageView iv_order_status_back;
    @InjectView(R.id.iv_order_status_share)//分享按钮
            ImageView iv_order_status_share;
    @InjectView(R.id.iv_order_status_tel)//电话按钮
            ImageView iv_order_status_tel;

    @InjectView(R.id.rb1)//顶部导航1
            RadioButton rb1;

    @InjectView(R.id.rb2)//顶部导航2
            RadioButton rb2;

    @InjectView(R.id.view01)//红色下标
            View view01;
    @InjectView(R.id.view02)//红色下标
            View view02;

    @InjectView(R.id.viewPager_order_status)
    ViewPager viewPager_order_status;

    private SparseArray<Fragment>fragments=new SparseArray<>();
    private ViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_status);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {
        iv_order_status_back.setOnClickListener(this);
        iv_order_status_share.setOnClickListener(this);
        iv_order_status_tel.setOnClickListener(this);
        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);
        rb1.setChecked(true);

        StoreOrderStatusFragment mStoreOrderStatusFragment=StoreOrderStatusFragment.newInstance();
        StoreOrderDetailFragment mStoreOrderDetailFragment= StoreOrderDetailFragment.newInstance();

        fragments.put(0,mStoreOrderStatusFragment);
        fragments.put(1,mStoreOrderDetailFragment);

        mViewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager_order_status.setAdapter(mViewPagerAdapter);
        viewPager_order_status.setCurrentItem(0);
        viewPager_order_status.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        rb1.setChecked(true);
                        view01.setVisibility(View.VISIBLE);
                        view02.setVisibility(View.INVISIBLE);
                        viewPager_order_status.setCurrentItem(0);

                        break;
                    case 1:
                        rb2.setChecked(true);
                        view01.setVisibility(View.INVISIBLE);
                        view02.setVisibility(View.VISIBLE);
                        viewPager_order_status.setCurrentItem(1);

                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_order_status_back:
                onBackPressed();
                break;
            case R.id.rb1:
                view01.setVisibility(View.VISIBLE);
                view02.setVisibility(View.INVISIBLE);
                break;
            case R.id.rb2:
                view01.setVisibility(View.INVISIBLE);
                view02.setVisibility(View.VISIBLE);
                break;


        }


    }


}
