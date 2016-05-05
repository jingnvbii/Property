package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

public class MineSettingActivity extends AppToolBarActivity implements View.OnClickListener{
    private RelativeLayout feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_setting);
        init();
    }

    private void init() {
        feedback = (RelativeLayout) findViewById(R.id.feedback);
        feedback.setOnClickListener(this);
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
        }
    }
}
