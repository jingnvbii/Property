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
import com.ctrl.forum.dao.CouponsDao;
import com.ctrl.forum.dao.ReplyCommentDao;
import com.ctrl.forum.entity.Count;
import com.ctrl.forum.ui.fragment.MineCashCouponFragment;
import com.ctrl.forum.ui.fragment.MineCashCouponPastFragment;
import com.ctrl.forum.ui.fragment.MineCashCouponUseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 现金劵
 */
public class MineXianJuanActivity extends ToolBarActivity {
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
    private MineCashCouponFragment cashCouponFragment;//未使用
    private MineCashCouponUseFragment cashCouponUseFragment; //已使用
    private MineCashCouponPastFragment cashCouponPastFragment;//已过期
    private List<Fragment> fragments;
    private CouponsDao cdao;
    private String wei,yi,past;

    //手指上下滑动时的最小速度
    private static final int YSPEED_MIN = 1000;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 50;

    //手指向上滑或下滑时的最小距离
    private static final int YDISTANCE_MIN = 100;

    //记录手指按下时的横坐标。
    private float xDown;

    //记录手指按下时的纵坐标。
    private float yDown;

    //记录手指移动时的横坐标。
    private float xMove;

    //记录手指移动时的纵坐标。
    private float yMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;
    private ReplyCommentDao rdao;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_you_juan);
        ButterKnife.inject(this);
        modityMessageRead();

       /* Intent itt = new Intent();
        itt.setAction("com.message");
        itt.putExtra("num", "cashCoupon");
        this.sendBroadcast(itt, null);*/

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

    private void modityMessageRead() {
        rdao = new ReplyCommentDao(this);
        String msgId = getIntent().getStringExtra("msgId");
        if (msgId!=null){
            rdao.modifyOneReadState(msgId);
        }
    }

    private void initData() {
        fragments = new ArrayList<>();
        cashCouponFragment = MineCashCouponFragment.newInstance();
        cashCouponUseFragment = MineCashCouponUseFragment.newInstance();
        cashCouponPastFragment = MineCashCouponPastFragment.newInstance();
        fragments.add(cashCouponFragment);
        fragments.add(cashCouponUseFragment);
        fragments.add(cashCouponPastFragment);

        //设置下划线默认的为灰色,第一条默认为红色
        ((TextView) text.getChildAt(0))
                .setTextColor(getResources().getColor(R.color.red_bg));
        lines.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.red_bg));

        //获取网络数据.给textView赋值
        cdao = new CouponsDao(this);
        cdao.getMemberCoupons("0","0", Arad.preferences.getString("memberId"), "", "");
        cdao.getMemberCoupons("0","1", Arad.preferences.getString("memberId"), "", "");
        cdao.getMemberCoupons("0","2", Arad.preferences.getString("memberId"), "", "");

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
    public String setupToolBarTitle() {return getResources().getString(R.string.xian_juan);}

    public void onClick(View v){
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
        if (requestCode==0){
            Count count = cdao.getCount();
            wei = count.getNotUsed();
            yi = count.getUsed();
            past = count.getExpired();

            tv_wei.setText("未使用("+wei+")");
            tv_yi.setText("已使用("+yi+")");
            tv_past.setText("已过期("+past+")");
        }
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

    /**
     * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
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
