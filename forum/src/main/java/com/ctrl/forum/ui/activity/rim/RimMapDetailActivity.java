package com.ctrl.forum.ui.activity.rim;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;

import com.ctrl.forum.R;

public class RimMapDetailActivity extends ActionBarActivity {
    private ImageView iv_map;//地图
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_map_detail);
        iv_map = (ImageView) findViewById(R.id.iv_map);
    }

    public void onClick(View view){
        this.finish();
    }

}
