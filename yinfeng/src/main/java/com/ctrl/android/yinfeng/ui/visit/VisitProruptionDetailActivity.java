package com.ctrl.android.yinfeng.ui.visit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.AnimUtil;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VisitProruptionDetailActivity extends AppToolBarActivity implements View.OnClickListener {

    @InjectView(R.id.et_visit_visitingpeople)//到访人姓名
            TextView et_visit_visitingpeople;
    @InjectView(R.id.et_visit_name)//拜访人姓名
            TextView et_visit_name;
    @InjectView(R.id.et_visit_room)//拜访房间
            TextView et_visit_room;
    @InjectView(R.id.et_visit_stop)//车牌号
            TextView et_visit_stop;
    @InjectView(R.id.et_visit_car)//到访人数
            TextView et_visit_car;
    @InjectView(R.id.et_visit_stf2op)//预计停留时间
            TextView et_visit_stf2op;
    @InjectView(R.id.et_visit_count)//性别
            TextView et_visit_count;
    @InjectView(R.id.et_visit_tel)//是否驾车
            TextView et_visit_tel;
    @InjectView(R.id.tv_notice_detail_confirm)//查看通行证
            TextView tv_notice_detail_confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_activity_visit_proruption_detail);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
        tv_notice_detail_confirm.setOnClickListener(this);
        et_visit_visitingpeople.setText(getIntent().getStringExtra("visitorname"));
        et_visit_name.setText(getIntent().getStringExtra("membername"));
        et_visit_room.setText(getIntent().getStringExtra("building")
                + "-" + getIntent().getStringExtra("unit") + "-" + getIntent().getStringExtra("room"));
        if (getIntent().getStringExtra("gender").equals("0")) {
            et_visit_count.setText("男");
        } else {
            et_visit_count.setText("女");

        }
        et_visit_car.setText(getIntent().getStringExtra("peoplenumber"));
        if (getIntent().getStringExtra("drovevisit").equals("0")) {
            et_visit_tel.setText("否");
        } else {
            et_visit_tel.setText("是");
        }
        et_visit_stop.setText(getIntent().getStringExtra("numberplates"));
        et_visit_stf2op.setText(getIntent().getStringExtra("residenceTime"));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_notice_detail_confirm:
                Intent intent = new Intent(VisitProruptionDetailActivity.this, VisitOrderSucessActivity.class);
                intent.putExtra("visitorname", getIntent().getStringExtra("visitorname"));
                intent.putExtra("gender", getIntent().getStringExtra("gender"));
                intent.putExtra("qrImgUrl", getIntent().getStringExtra("qrImgUrl"));
                intent.putExtra("arriveTime", getIntent().getStringExtra("arriveTime"));
                intent.putExtra("visitNum", getIntent().getStringExtra("visitNum"));
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
        }

    }


    @Override
    public String setupToolBarTitle() {
        return "访客详情";
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

}
