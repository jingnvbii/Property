package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

public class MineCreateShopThreeActivity extends AppToolBarActivity implements View.OnClickListener{
    private TextView tv_apply_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_shop_three);

        tv_apply_finish = (TextView) findViewById(R.id.tv_apply_finish);


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
        switch (v.getId()){
            case R.id.tv_apply_finish:
                break;
        }
    }
}
