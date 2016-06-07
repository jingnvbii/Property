package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.ui.fragment.MineOrderManagerFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 订单管理
 */
public class MineOrderManageActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.ll_text)
    LinearLayout ll_text;
    @InjectView(R.id.fl_content)
    ViewPager fl_content;
    @InjectView(R.id.lines)
    LinearLayout lines;
    @InjectView(R.id.tv_xian)
    TextView tv_xian;
    @InjectView(R.id.tv_shop_manager)
    TextView tv_shop_manager;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private MineOrderManagerFragment shopManagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_shop_manage);
        ButterKnife.inject(this);

        initView();
        initCtrl();

        fl_content.setAdapter(fragmentPagerAdapter);

        fl_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < ll_text.getChildCount(); i++) {
                    ((TextView) ll_text.getChildAt(i))
                            .setTextColor(getResources().getColor(R.color.text_black1));
                    lines.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.line_gray));
                }
                ((TextView) ll_text.getChildAt(arg0))
                        .setTextColor(getResources().getColor(R.color.red_bg));
                lines.getChildAt(arg0).setBackgroundColor(getResources().getColor(R.color.red_bg));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        ((TextView) ll_text.getChildAt(0))
                .setTextColor(getResources().getColor(R.color.red_bg));
        lines.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.red_bg));
    }

    private void initView() {
        tv_xian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//现金劵
               startActivity(new Intent(getApplicationContext(),MineMerchantCouponActivity.class));
            }
        });
        tv_shop_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//店铺管理
                startActivity(new Intent(getApplicationContext(),MineShopManagerActivity.class));
            }
        });
    }

    private void initCtrl() {
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                shopManagerFragment = MineOrderManagerFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                shopManagerFragment.setArguments(bundle);
                return shopManagerFragment;
            }

            @Override
            public int getCount() {
                return ll_text.getChildCount();
            }
        };
    }

    public void onClick(View v) {
        for (int i = 0; i < ll_text.getChildCount(); i++) {
            ((TextView) ll_text.getChildAt(i)).setTextColor(getResources().getColor(R.color.text_black1));
            lines.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.line_gray));
        }

        ((TextView) v).setTextColor(getResources().getColor(R.color.red_bg));
        (lines.getChildAt(ll_text.indexOfChild(v))).setBackgroundColor(getResources().getColor(R.color.red_bg));

        fl_content.setCurrentItem(ll_text.indexOfChild(v));
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
    public String setupToolBarTitle() {return "订单管理";}

}
