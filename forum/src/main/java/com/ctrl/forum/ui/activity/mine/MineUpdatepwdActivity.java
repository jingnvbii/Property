package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

/**
 * 修改密码
 */
public class MineUpdatepwdActivity extends AppToolBarActivity implements View.OnClickListener{
    private TextView tv_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_updatepwd);
        init();

    }

    private void init() {
        tv_next = (TextView) findViewById(R.id.tv_next);

        tv_next.setOnClickListener(this);
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
        return getResources().getString(R.string.updatepwd);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id){
            case R.id.tv_next:
                intent = new Intent(getApplicationContext(),MineNewpwdActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
