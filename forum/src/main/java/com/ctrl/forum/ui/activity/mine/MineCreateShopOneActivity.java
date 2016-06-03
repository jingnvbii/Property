package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

/**
 * 我的店铺_没有店铺
 */
public class MineCreateShopOneActivity extends AppToolBarActivity implements View.OnClickListener{
    private TextView tv_apply_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_shop_one);

        init();
    }

    private void init() {
        tv_apply_start = (TextView) findViewById(R.id.tv_apply_start);

        tv_apply_start.setOnClickListener(this);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.apply_shop);}

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_apply_start:
                Intent intent = new Intent(getApplicationContext(),MineCreateShopTwoActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }
}
