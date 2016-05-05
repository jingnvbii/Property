package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

/**
 * 我的等级
 */
public class MineGradeActivity extends AppToolBarActivity implements View.OnClickListener{
    private TextView tv_stage;
    private TextView now_stage;
    private TextView tv_end;
    private TextView tv_num;
    private RelativeLayout fast_stage;
    private RelativeLayout use_grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_grade);
        initView();
    }

    private void initView() {
        tv_stage = (TextView) findViewById(R.id.tv_stage);
        now_stage = (TextView) findViewById(R.id.now_stage);
        tv_end = (TextView) findViewById(R.id.tv_end);
        tv_num = (TextView) findViewById(R.id.tv_num);
        fast_stage = (RelativeLayout) findViewById(R.id.fast_stage);
        use_grade = (RelativeLayout) findViewById(R.id.use_grade);

        fast_stage.setOnClickListener(this);
        use_grade.setOnClickListener(this);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.vip_grade);}

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fast_stage:
                break;
            case R.id.use_grade:
                break;
        }
    }
}
