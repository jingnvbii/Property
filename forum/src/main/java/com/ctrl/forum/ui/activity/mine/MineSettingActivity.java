package com.ctrl.forum.ui.activity.mine;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.ctrl.forum.R;
import com.ctrl.forum.base.MyBroadcastReceiver;
import com.ctrl.forum.utils.DataCleanUtils;

import java.util.HashSet;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * 系统设置
 */
public class MineSettingActivity extends ToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.iv_system_notification) //是否推送
    CheckBox iv_system_notification;
    @InjectView(R.id.iv_reply_comments)  //评论回复
    CheckBox iv_reply_comments;
    @InjectView(R.id.iv_voice)   //声音
    CheckBox iv_voice;
    @InjectView(R.id.iv_vibration)  //振动
    CheckBox iv_vibration;
    @InjectView(R.id.iv_night_no_message)  //夜间免打捞
    CheckBox iv_night_no_message;
    @InjectView(R.id.iv_notification_show)  //通知显示消息详情
    CheckBox iv_notification_show;
    @InjectView(R.id.iv_desktop_icon_hints)  //桌面图标提示
    CheckBox iv_desktop_icon_hints;
    @InjectView(R.id.rl_cancle)
    RelativeLayout rl_cancle;   //清理缓存
    @InjectView(R.id. feedback)
    RelativeLayout  feedback;   //意见反馈
    @InjectView(R.id.iv_clear)
    TextView iv_clear;
    MyBroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_setting);
        ButterKnife.inject(this);
        init();
        receiver = new MyBroadcastReceiver();
        initJpush();
        //setJPush();
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void initJpush(){
        JPushInterface.init(getApplicationContext());
    }

    private void setJPush() {
        //设置夜间免大佬模式
        if (Arad.preferences.getBoolean("nightNoMessage")){//夜间免打捞
            Set<Integer> days = new HashSet<>();
            days.add(0);
            days.add(1);
            days.add(2);
            days.add(3);
            days.add(4);
            days.add(5);
            days.add(6);
            JPushInterface.setPushTime(getApplicationContext(), days, 8, 22);
        }else {
            Set<Integer> days = new HashSet<>();
            days.add(0);
            days.add(1);
            days.add(2);
            days.add(3);
            days.add(4);
            days.add(5);
            days.add(6);
            JPushInterface.setPushTime(getApplicationContext(), days, 0, 23);
        }

        if (Arad.preferences.getBoolean("desktopIconHints")){ //桌面图标提示
        }else {}

        //设置极光推送通知的声音与震动
        JPushInterface.setSilenceTime(getApplicationContext(), 0, 0, 0, 0);
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MineSettingActivity.this);
        if (!Arad.preferences.getBoolean("isSet")) {
            Log.e("isSet==========", Arad.preferences.getBoolean("isSet") + "");
            builder.notificationDefaults = Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE;  // 设置为铃声、震动都要
            JPushInterface.setPushNotificationBuilder(1, builder);
        }else{
            if (Arad.preferences.getBoolean("voice")&&Arad.preferences.getBoolean("vibration") ){
                builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;  // 设置为铃声
                JPushInterface.setPushNotificationBuilder(1, builder);
            }else{
                if(Arad.preferences.getBoolean("voice")){
                    Log.e("voice==========",Arad.preferences.getBoolean("voice")+"");
                    builder.notificationDefaults = Notification.DEFAULT_SOUND;  // 设置为铃声
                    JPushInterface.setPushNotificationBuilder(1, builder);
                }
                if(Arad.preferences.getBoolean("vibration")){
                    Log.e("vibration==========",Arad.preferences.getBoolean("vibration")+"");
                    builder.notificationDefaults = Notification.DEFAULT_VIBRATE;  // 设置为震动
                    JPushInterface.setPushNotificationBuilder(1, builder);
                }
                if (!Arad.preferences.getBoolean("voice")&&!Arad.preferences.getBoolean("voice")){
                    Log.e("vibration==========","wu");
                    JPushInterface.setSilenceTime(getApplicationContext(), 0, 0, 23, 59);
                }
            }
        }

        //设置停止推送，恢复推送
        if(Arad.preferences.getBoolean("systemNotification")){
            if (JPushInterface.isPushStopped(MineSettingActivity.this)){
                JPushInterface.resumePush(getApplicationContext());
            }
        }else{
            JPushInterface.stopPush(MineSettingActivity.this);
        }
    }

    private void init() {
        try {
            iv_clear.setText(DataCleanUtils.getTotalCacheSize(MineSettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

            onChange(iv_system_notification, "systemNotification");
            onChange(iv_reply_comments, "replyComments");
            onChange(iv_voice, "voice");
            onChange(iv_vibration, "vibration");
            onChange(iv_night_no_message, "nightNoMessage");
            onChange(iv_notification_show, "notificationShow");
            onChange(iv_desktop_icon_hints, "desktopIconHints");

        feedback.setOnClickListener(this);
        rl_cancle.setOnClickListener(this);
    }

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
    public String setupToolBarTitle() {
        return getResources().getString(R.string.set_title);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intetnt;
        switch (id){
            case R.id.feedback:
                intetnt = new Intent(getApplication(),MineFeedBackActivity.class);
                startActivity(intetnt);
                break;
            case R.id.rl_cancle:
                //清理缓存
                DataCleanUtils.clearAllCache(this.getApplicationContext());
                try {
                    iv_clear.setText(DataCleanUtils.getTotalCacheSize(MineSettingActivity.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void onChange(CheckBox ib, final String name){
        if (!Arad.preferences.getBoolean("isSet")) {
            if (ib.isChecked()){
                Arad.preferences.putBoolean(name, true);
            }else{
                Arad.preferences.putBoolean(name, false);
            }
        }else {
            if (Arad.preferences.getBoolean(name)) {
                ib.setChecked(true);
                Arad.preferences.putBoolean(name, true);
                Arad.preferences.flush();
            } else {
                ib.setChecked(false);
                Arad.preferences.putBoolean(name, false);
                Arad.preferences.flush();
            }
        }

        ib.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Arad.preferences.putBoolean(name, isChecked);
                Arad.preferences.flush();
                setJPush();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Arad.preferences.putBoolean("isSet", true);
        Arad.preferences.flush();
        //unregisterReceiver(receiver);
    }

}
