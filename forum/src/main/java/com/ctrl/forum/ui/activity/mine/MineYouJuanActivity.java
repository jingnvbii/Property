package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.CouponsDao;
import com.ctrl.forum.entity.Count;
import com.ctrl.forum.ui.fragment.MineCouponFragment;
import com.ctrl.forum.ui.fragment.MineCouponPastFragment;
import com.ctrl.forum.ui.fragment.MineCouponUseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 优惠劵
 */
public class MineYouJuanActivity extends AppToolBarActivity {
    @InjectView(R.id.text)
            LinearLayout text;
    @InjectView(R.id.fl_content)
            ViewPager fl_content;
    @InjectView(R.id.lines)
            LinearLayout lines;
    @InjectView(R.id.tv_wei)
    TextView tv_wei;
    @InjectView(R.id.tv_yi)
    TextView tv_yi;
    @InjectView(R.id.tv_past)
    TextView tv_past;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private MineCouponFragment couponFragment;
    private MineCouponUseFragment couponUseFragment;
    private MineCouponPastFragment couponPastFragment;
    private List<Fragment> fragments;
    private CouponsDao cdao;
    private String wei,yi,past;
    private String amount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_youjuan);
        ButterKnife.inject(this);
        initData();
        initCtrl();
        fl_content.setAdapter(fragmentPagerAdapter);

        fl_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < text.getChildCount(); i++) {
                    ((TextView) text.getChildAt(i))
                            .setTextColor(getResources().getColor(R.color.text_black1));
                    lines.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.line_gray));
                }
                ((TextView) text.getChildAt(arg0))
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
        amount = getIntent().getStringExtra("amount");
    }
    private void initData() {
        fragments = new ArrayList<>();
        couponFragment = MineCouponFragment.newInstance();

        couponUseFragment = MineCouponUseFragment.newInstance();
        couponPastFragment = MineCouponPastFragment.newInstance();
        fragments.add(couponFragment);
        fragments.add(couponUseFragment);
        fragments.add(couponPastFragment);

        ((TextView) text.getChildAt(0))
                .setTextColor(getResources().getColor(R.color.red_bg));
        lines.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.red_bg));

        cdao = new CouponsDao(this);
        cdao.getMemberRedenvelope("0","0",Arad.preferences.getString("memberId"),"","");
        cdao.getMemberRedenvelope("0", "1", Arad.preferences.getString("memberId"), "", "");
        cdao.getMemberRedenvelope("0","2",Arad.preferences.getString("memberId"),"","");
    }

    private void initCtrl() {
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return text.getChildCount();
            }
        };
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
    public String setupToolBarTitle() {return getResources().getString(R.string.you_hui);}

    public void onClick(View v) {
        for (int i = 0; i < text.getChildCount(); i++) {
            ((TextView) text.getChildAt(i)).setTextColor(getResources().getColor(R.color.text_black1));
            lines.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.line_gray));
        }

        ((TextView) v).setTextColor(getResources().getColor(R.color.red_bg));
        (lines.getChildAt(text.indexOfChild(v))).setBackgroundColor(getResources().getColor(R.color.red_bg));

        fl_content.setCurrentItem(text.indexOfChild(v));
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==1){
            Count count = cdao.getCount1();
            wei = count.getNotUsed();
            yi = count.getUsed();
            past = count.getExpired();

            tv_wei.setText("未使用("+wei+")");
            tv_yi.setText("已使用("+yi+")");
            tv_past.setText("已过期("+past+")");
        }
    }
}
