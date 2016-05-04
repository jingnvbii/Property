package com.ctrl.android.yinfeng.ui.cpatrol;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.ui.adapter.JasonViewPagerAdapter;
import com.ctrl.android.yinfeng.ui.fragment.CPatrolFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *   巡查 列表 Created by jason on 2015/11/10.
 */
public class CPatrolActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.radio_one)
    RadioButton mRadioOne;
    @InjectView(R.id.radio_two)
    RadioButton mRadioTwo;
    @InjectView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @InjectView(R.id.viewpager)
    ViewPager mViewpager;

    List<Fragment> fragments = new ArrayList<>();
    private JasonViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_activity_patrol);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        CPatrolFragment CPatrolFragment1 = CPatrolFragment.newInstance("a");
        CPatrolFragment CPatrolFragment2 = CPatrolFragment.newInstance("b");
        fragments.add(0, CPatrolFragment1);
        fragments.add(1, CPatrolFragment2);
        adapter = new JasonViewPagerAdapter(getSupportFragmentManager(), fragments);
        adapter.setOnReloadListener(new JasonViewPagerAdapter.OnReloadListener() {
            @Override
            public void onReload() {
                fragments = null;
                List<Fragment> list = new ArrayList<Fragment>();
                list.add(CPatrolFragment.newInstance("a"));
                list.add(CPatrolFragment.newInstance("b"));
                adapter.setPagerItems(list);
            }
        });
        mViewpager.setAdapter(adapter);
        mViewpager.setOnPageChangeListener(new FragmentOnPageChangeListener());
        mViewpager.setOffscreenPageLimit(2);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_one:
                        mRadioOne.setBackgroundResource(R.drawable.button_left_shap_checked);
                        mRadioTwo.setBackgroundResource(R.drawable.button_right_shap);
                        mRadioOne.setTextColor(Color.WHITE);
                        mRadioTwo.setTextColor(Color.GRAY);
                        mViewpager.setCurrentItem(0);
                        break;
                    case R.id.radio_two:
                        mRadioGroup.check(R.id.radio_two);
                        mRadioOne.setBackgroundResource(R.drawable.button_left_shap);
                        mRadioTwo.setBackgroundResource(R.drawable.button_right_shap_checked);
                        mRadioTwo.setTextColor(Color.WHITE);
                        mRadioOne.setTextColor(Color.GRAY);
                        mViewpager.setCurrentItem(1);
                        break;
                }
            }
        });
        mRadioOne.setText("应巡查");
        mRadioTwo.setText("已巡查");
        mRadioOne.setChecked(true);
    }

    @Override
    public void onClick(View v) {


    }

    public class FragmentOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int index) {
            switch (index) {
                case 0:
                    mRadioGroup.check(R.id.radio_one);
                    mRadioOne.setBackgroundResource(R.drawable.button_left_shap_checked);
                    mRadioTwo.setBackgroundResource(R.drawable.button_right_shap);
                    mRadioOne.setTextColor(Color.WHITE);
                    mRadioTwo.setTextColor(Color.GRAY);
                    break;
                case 1:
                    mRadioGroup.check(R.id.radio_two);
                    mRadioOne.setBackgroundResource(R.drawable.button_left_shap);
                    mRadioTwo.setBackgroundResource(R.drawable.button_right_shap_checked);
                    mRadioTwo.setTextColor(Color.WHITE);
                    mRadioOne.setTextColor(Color.GRAY);
                    break;
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }


    public JasonViewPagerAdapter getAdapter(){
        return adapter;
    }

    @Override
    public String setupToolBarTitle() {
        return "巡查";
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
