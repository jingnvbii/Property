package com.ctrl.forum.ui.activity.mine;

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
import com.ctrl.forum.ui.fragment.MineCompanyCollectFragment;
import com.ctrl.forum.ui.fragment.MineInvitationCollectFragment;
import com.ctrl.forum.ui.fragment.MineProductCollectFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的收藏
 */
public class MineCollectActivity extends AppToolBarActivity {
    @InjectView(R.id.text)
            LinearLayout text;
    @InjectView(R.id.fl_content)
    ViewPager fl_content;
    @InjectView(R.id.lines)
    LinearLayout lines;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private MineInvitationCollectFragment invitationCollectFragment;
    private MineProductCollectFragment productCollectFragment;
    private MineCompanyCollectFragment companyCollectFragment;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_collect);
        ButterKnife.inject(this);
        initCtrl();
        initData();

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
    }

    private void initData() {
        fragments = new ArrayList<>();
        invitationCollectFragment = MineInvitationCollectFragment.newInstance();
        productCollectFragment = MineProductCollectFragment.newInstance();
        companyCollectFragment = MineCompanyCollectFragment.newInstance();
        fragments.add(invitationCollectFragment);
        fragments.add(companyCollectFragment);
        fragments.add(productCollectFragment);

        ((TextView) text.getChildAt(0))
                .setTextColor(getResources().getColor(R.color.red_bg));
        lines.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.red_bg));
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
    public String setupToolBarTitle() {return getResources().getString(R.string.my_collect);}

    public void onClick(View v){
        for (int i = 0; i < text.getChildCount(); i++) {
            ((TextView) text.getChildAt(i)).setTextColor(getResources().getColor(R.color.text_black1));
            lines.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.line_gray));
        }

        ((TextView) v).setTextColor(getResources().getColor(R.color.red_bg));
        (lines.getChildAt(text.indexOfChild(v))).setBackgroundColor(getResources().getColor(R.color.red_bg));


        fl_content.setCurrentItem(text.indexOfChild(v));
    }
}
