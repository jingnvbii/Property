package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MineJuanActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.rl_hui)
    RelativeLayout rl_hui;
    @InjectView(R.id.rl_xian)
    RelativeLayout rl_xian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_juan);
        ButterKnife.inject(this);

        rl_hui.setOnClickListener(this);
        rl_xian.setOnClickListener(this);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.my_juan);}

    @Override
    public void onClick(View v) {
        //优惠劵
        if (v==rl_hui){startActivity(new Intent(this,MineYouJuanActivity.class));}
        //现金劵
        if (v==rl_xian){startActivity(new Intent(this,MineXianJuanActivity.class));}
    }
}
