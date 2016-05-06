package com.ctrl.forum.ui.activity.Invitation;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 * 名片 activity
 * Created by jason on 2016/4/12.
 */
public class CallingCardActivity extends AppToolBarActivity{
    @InjectView(R.id.cb_calling)//单选按钮
    CheckBox cb_calling;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calling_card_activity);
        ButterKnife.inject(this);
        if(Arad.preferences.getBoolean("isCallingChecked")){
            cb_calling.setChecked(true);
        }else {
            cb_calling.setChecked(false);
        }
        cb_calling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Arad.preferences.putBoolean("isCallingChecked",isChecked);
                Arad.preferences.flush();
            }
        });


    }

    @Override
    public boolean setupToolBarLeftText(TextView mLeftText) {
        mLeftText.setText("取消");
        mLeftText.setTextColor(getResources().getColor(R.color.text_blue));
        mLeftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {
        TextView tv_title = getmTitle();
        tv_title.setTextColor(getResources().getColor(R.color.text_black));
        return "所在位置";
    }

}
