package com.ctrl.android.property.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.property.staff.base.AppHolder;
import com.ctrl.android.property.staff.base.AppToolBarActivity;
import com.ctrl.android.property.staff.base.MyApplication;
import com.ctrl.android.property.staff.ui.device.DeviceActivity;
import com.ctrl.android.property.staff.ui.express.ExpressListActivity;
import com.ctrl.android.property.staff.ui.forum.ForumActivity;
import com.ctrl.android.property.staff.ui.my.MyActivity;
import com.ctrl.android.property.staff.ui.notice.NoticeListActivity;
import com.ctrl.android.property.staff.ui.patrol.PatrolActivity;
import com.ctrl.android.property.staff.ui.repair.RepairsActivity;
import com.ctrl.android.property.staff.ui.task.TaskListActivity;
import com.ctrl.android.property.staff.ui.visit.VisitHandleActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 主页面 activity
 * Created by Eric on 2015/11/23.
 * */
public class MainActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.main_notice_btn)//内部通知
    LinearLayout main_notice_btn;
    @InjectView(R.id.main_forum_btn)//内部论坛
    LinearLayout main_forum_btn;
    @InjectView(R.id.main_repair_btn)//报修
    LinearLayout main_repair_btn;
    @InjectView(R.id.main_visit_btn)//预约访客
    LinearLayout main_visit_btn;
    @InjectView(R.id.main_express_btn)//代收快递
    LinearLayout main_express_btn;
    @InjectView(R.id.main_partrol_btn)//巡更巡查
    LinearLayout main_partrol_btn;
    @InjectView(R.id.main_equipment_btn)//设备养护
    LinearLayout main_equipment_btn;
    @InjectView(R.id.main_task_btn)//任务指派
    LinearLayout main_task_btn;
    @InjectView(R.id.main_custom_btn)//自定义模块
    LinearLayout main_custom_btn;

    private String TITLE = AppHolder.getInstance().getStaffInfo().getCityName() + "   " + AppHolder.getInstance().getStaffInfo().getCommunityName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.main_activity);
        ButterKnife.inject(this);
        init();
    }

    /**
     * 初始化方法
     * */
    private void init(){
        main_notice_btn.setOnClickListener(this);
        main_forum_btn.setOnClickListener(this);
        main_repair_btn.setOnClickListener(this);
        main_visit_btn.setOnClickListener(this);
        main_express_btn.setOnClickListener(this);
        main_partrol_btn.setOnClickListener(this);
        main_equipment_btn.setOnClickListener(this);
        main_task_btn.setOnClickListener(this);
        main_custom_btn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        TITLE = AppHolder.getInstance().getStaffInfo().getCityName() + "   " + AppHolder.getInstance().getStaffInfo().getCommunityName();
    }

    @Override
    public void onClick(View v) {

        if(v == main_notice_btn){
            //MessageUtils.showShortToast(this,"内部通知");
            startActivity(new Intent(MainActivity.this, NoticeListActivity.class));
        }

        if(v == main_forum_btn){
            //MessageUtils.showShortToast(this,"内部论坛");
            Intent intent = new Intent(MainActivity.this, ForumActivity.class);
            startActivity(intent);
            AnimUtil.intentSlidIn(MainActivity.this);
        }

        if(v == main_repair_btn){
            //MessageUtils.showShortToast(this,"报修");
            startActivity(new Intent(MainActivity.this, RepairsActivity.class));
        }

        if(v == main_visit_btn){
            //MessageUtils.showShortToast(this,"到访");
            startActivity(new Intent(MainActivity.this, VisitHandleActivity.class));
        }

        if(v == main_express_btn){
            //MessageUtils.showShortToast(this,"代收快递");
            startActivity(new Intent(MainActivity.this, ExpressListActivity.class));
        }

        if(v == main_partrol_btn){
            //MessageUtils.showShortToast(this,"巡更巡查");
            startActivity(new Intent(MainActivity.this, PatrolActivity.class));

        }

        if(v == main_equipment_btn){
            //MessageUtils.showShortToast(this,"设备维护");
            Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
            startActivity(intent);
            AnimUtil.intentSlidIn(MainActivity.this);
        }

        if(v == main_task_btn){
            //MessageUtils.showShortToast(this,"任务指派");
            Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
            startActivity(intent);
            AnimUtil.intentSlidIn(MainActivity.this);
        }

        if(v == main_custom_btn){
            MessageUtils.showShortToast(this,"自定义模块");
        }

    }

    /**
     * header 左侧按钮
     * */
//    @Override
//    public boolean setupToolBarLeftButton(ImageView leftButton) {
//        leftButton.setImageResource(R.drawable.white_arrow_left_none);
//        leftButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //MessageUtils.showShortToast(MallShopActivity.this, "AA");
//                onBackPressed();
//            }
//        });
//        return true;
//    }

    /**
     * 页面标题
     * */
    @Override
    public String setupToolBarTitle() {
        return TITLE;
    }

    /**
     * header 右侧按钮
     * */
//    @Override
//    public boolean setupToolBarRightButton(ImageView rightButton) {
//        rightButton.setImageResource(R.drawable.white_cross_icon);
//        rightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MessageUtils.showShortToast(EasyActivity.this, "MORE");
//                //showProStyleListPop();
//            }
//        });
//        return true;
//    }

    /**
     * header 右侧文本
     * */
    public boolean setupToolBarRightText(TextView mRightText) {

        mRightText.setText("个人中心");
        mRightText.setTextColor(getResources().getColor(R.color.text_white));
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(MainActivity.this);
            }
        });

        return true;
    }


}
