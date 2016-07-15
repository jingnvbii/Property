package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.ctrl.forum.R;
import com.ctrl.forum.dao.ReplyCommentDao;
import com.ctrl.forum.ui.fragment.MineCommentFragment;
import com.ctrl.forum.ui.fragment.MineMessageFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 消息通知
 */
public class MineMessageActivity extends ToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.text)//文本
    LinearLayout text;
    @InjectView(R.id.lines)//下划线
    LinearLayout lines;
    @InjectView(R.id.fl_content)//内容
            ViewPager fl_content;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private MineMessageFragment messageFragment; //全部
    private MineCommentFragment commentFragment; //我的评论
    private List<Fragment> fragments;
    private ReplyCommentDao rdao;

    //记录手指按下时的横坐标。
    private float xDown;
    //记录手指按下时的纵坐标。
    private float yDown;
    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;
    //记录手指移动时的横坐标。
    private float xMove;
    //记录手指移动时的纵坐标。
    private float yMove;
    //手指上下滑动时的最小速度
    private static final int YSPEED_MIN = 1000;
    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 50;
    //手指向上滑或下滑时的最小距离
    private static final int YDISTANCE_MIN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_notification);
        ButterKnife.inject(this);
        rdao = new ReplyCommentDao(this);
        rdao.modifyReadState(Arad.preferences.getString("memberId"));

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
    }

    private void initData() {
        fragments = new ArrayList<>();
        messageFragment = MineMessageFragment.newInstance();
        commentFragment = MineCommentFragment.newInstance();
        fragments.add(messageFragment);
        fragments.add(commentFragment);

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
    public String setupToolBarTitle() {return getResources().getString(R.string.message_notification);}

    @Override
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (fl_content.getCurrentItem()==0) {
                    xMove = event.getRawX();
                    yMove = event.getRawY();
                    //滑动的距离
                    int distanceX = (int) (xMove - xDown);
                    int distanceY = (int) (yMove - yDown);
                    //获取顺时速度
                    int ySpeed = getScrollVelocity();
                    //关闭Activity需满足以下条件：
                    //1.x轴滑动的距离>XDISTANCE_MIN
                    //2.y轴滑动的距离在YDISTANCE_MIN范围内
                    //3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
                    if (distanceX > XDISTANCE_MIN && (distanceY < YDISTANCE_MIN && distanceY > -YDISTANCE_MIN) && ySpeed < YSPEED_MIN) {
                        finish();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }
}


