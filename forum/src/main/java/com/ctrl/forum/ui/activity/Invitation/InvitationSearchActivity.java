package com.ctrl.forum.ui.activity.Invitation;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

/**
 *
 *帖子搜索 activity
 * Created by jason on 2016/4/12.
 */
public class InvitationSearchActivity extends AppToolBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_search);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
