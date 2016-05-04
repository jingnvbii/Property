package com.ctrl.forum.ui.activity.store;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

/*
* 商城定位 activity
* */

public class StoreLocateActivity extends AppToolBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locate);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }


    }


    @Override
    public String setupToolBarTitle() {
        TextView tv = getmTitle();
        Drawable drawable = getResources().getDrawable(R.mipmap.locate_img);
        drawable.setBounds(0,0,32,32);
        tv.setCompoundDrawables(drawable, null, null, null);
        tv.setCompoundDrawablePadding(10);
        tv.setTextColor(Color.WHITE);
        tv.setText("中润世纪广场");
        return tv.getText().toString();
    } 

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left_white);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("地图");
        mRightText.setTextColor(Color.WHITE);
        return true;
    }
}
