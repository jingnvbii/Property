package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.utils.DataCleanUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MineSettingActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.iv_system_notification) //系统通知
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_setting);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        try {
            iv_clear.setText(DataCleanUtils.getTotalCacheSize(MineSettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        onChange(iv_system_notification,"systemNotification");
        onChange(iv_reply_comments,"replyComments");
        onChange(iv_voice,"voice");
        onChange(iv_vibration,"vibration");
        onChange(iv_night_no_message,"nightNoMessage");
        onChange(iv_notification_show,"notificationShow");
        onChange(iv_desktop_icon_hints,"desktopIconHints");

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
        if (Arad.preferences.getBoolean(name)){
            ib.setChecked(true);
            Arad.preferences.putBoolean(name,true);
        }
        else{
            ib.setChecked(false);
            Arad.preferences.putBoolean(name,false);
        }
        ib.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Arad.preferences.putBoolean(name,isChecked);
                Arad.preferences.flush();
            }
        });
    }
}
