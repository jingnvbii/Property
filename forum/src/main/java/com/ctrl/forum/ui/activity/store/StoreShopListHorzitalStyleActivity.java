package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.ui.fragment.StoreShopListHorzitalStyleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城定位 activity
* */

public class StoreShopListHorzitalStyleActivity extends AppToolBarActivity implements View.OnClickListener{

    private RadioGroup myRadioGroup;
    @InjectView(R.id.lay_shop_horzital_style)
    LinearLayout layout;
    @InjectView(R.id.horizontalScrollView_shop_horzital_style)
    HorizontalScrollView mHorizontalScrollView;

    @InjectView(R.id.m_list_submit)//马上结算按钮
    Button m_list_submit;

    @InjectView(R.id.viewpager_shop)
    ViewPager mViewpager;

    SparseArray<Fragment> fragments = new SparseArray<Fragment>();

    private int width;
    private float mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离
    private List<Merchant> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_shop_list_horzital_style);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initData();
        initView();
    }

    private void initData() {
       list=new ArrayList<>();
        for(int i=0;i<10;i++){
            Merchant merchant=new Merchant();
            merchant.setName("小贝便利"+i);
            list.add(merchant);
        }

    }

    private void initView() {
        m_list_submit.setOnClickListener(this);
        width = getResources().getDisplayMetrics().widthPixels;
        myRadioGroup = new RadioGroup(this);
        myRadioGroup.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(myRadioGroup);
        for (int i = 0; i <10; i++) {
            RadioButton radio = new RadioButton(this);
            radio.setBackgroundResource(R.drawable.top_category_selector);
            LinearLayout.LayoutParams l;
           /* if(dao.getCompanyCategoryList().size()==1){
                l = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else if(dao.getCompanyCategoryList().size()==2){
                l = new LinearLayout.LayoutParams(width/2, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else if(dao.getCompanyCategoryList().size()==3){
                l = new LinearLayout.LayoutParams(width/3, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }else {*/
            l = new LinearLayout.LayoutParams(width/5, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            // }
            radio.setLayoutParams(l);
            radio.setGravity(Gravity.CENTER);
            //  radio.setPadding(20, 20, 20, 20);
            radio.setId(i);
            radio.setButtonDrawable(getResources().getDrawable(R.color.white));
            radio.setTextSize(14.0f);
            radio.setTextColor(getResources().getColor(R.color.text_gray));//选择器
            radio.setText("分类");//动态设置
            radio.setSingleLine(true);
            radio.setEllipsize(TextUtils.TruncateAt.END);
            radio.setTag(i);
            if (i == 0) {
                radio.setChecked(true);
                radio.setTextColor(getResources().getColor(R.color.text_red));
            }
            radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        buttonView.setTextColor(getResources().getColor(R.color.text_red));
                    }else {
                        buttonView.setTextColor(getResources().getColor(R.color.text_gray));
                    }
                }
            });

            View view = new View(this);
            LinearLayout.LayoutParams v = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(v);
              view.setBackgroundColor(Color.parseColor("#cccccc"));
            myRadioGroup.addView(radio);
             myRadioGroup.addView(view);
            StoreShopListHorzitalStyleFragment fragment = StoreShopListHorzitalStyleFragment.newInstance(list);
            fragments.put(i, fragment);
        }
        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Map<String, Object> map = (Map<String, Object>) group.getChildAt(checkedId).getTag();
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                 mViewpager.setCurrentItem(radioButtonId);//让下方ViewPager跟随上面的HorizontalScrollView切换
                mCurrentCheckedRadioLeft = rb.getLeft();
                mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (width * 2) / 5, 0);
            }
        });



        mViewpager.setAdapter(new com.ctrl.forum.ui.adapter.ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mViewpager.setOnPageChangeListener(new FragmentOnPageChangeListener());
        mViewpager.setCurrentItem(0);

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
            case R.id.m_list_submit:
                Intent intent=new Intent(this,StoreOrderDetailActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
        }


    }


    @Override
    public String setupToolBarTitle() {
        return "小贝商品";
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
        mRightText.setText("店铺详情");
        mRightText.setTextColor(Color.WHITE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StoreShopListHorzitalStyleActivity.this,StoreShopDetailActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(StoreShopListHorzitalStyleActivity.this);
            }
        });
        return true;
    }
}
