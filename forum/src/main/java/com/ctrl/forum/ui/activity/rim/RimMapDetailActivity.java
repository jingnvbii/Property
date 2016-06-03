package com.ctrl.forum.ui.activity.rim;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.widget.SlidingUpPanelLayout;
import com.ctrl.forum.R;
import com.ctrl.forum.dao.RimDao;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RimMapDetailActivity extends ToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.iv_back)
    ImageView iv_back;
    @InjectView(R.id.iv_map)
    MapView iv_map;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id.tv_address)
    TextView tv_address;
    @InjectView(R.id.tv_number)
    TextView tv_number;
    @InjectView(R.id.tv_total)
    TextView tv_total;
    @InjectView(R.id.tv_dh)
    TextView tv_dh;
    @InjectView(R.id.rl_item)
    RelativeLayout rl_item;
    @InjectView(R.id.iv_phone)
    ImageView iv_phone;

    private Intent intent;
    private String name,address,telephone,callTimes,latitude,longitude,rimServiceCompaniesId;
    private View view;
    private PopupWindow popupWindow;
    private TextView bo_hao,call_up,cancel;
    private RimDao rimDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_map_detail);
        ButterKnife.inject(this);

        initData();
        initView();
        initPop();
    }

    private void initView() {
        rimDao = new RimDao(this);

        iv_back.setOnClickListener(this);
        tv_dh.setOnClickListener(this);
        rl_item.setOnClickListener(this);
        iv_phone.setOnClickListener(this);

        tv_name.setText(name);
        tv_address.setText(address);
        tv_number.setText(telephone);
        tv_total.setText(callTimes);
    }

    private void initData() {
        intent = getIntent();
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        telephone = intent.getStringExtra("telephone");
        callTimes = intent.getStringExtra("callTimes");
        latitude = Arad.preferences.getString("latitude");
        longitude = Arad.preferences.getString("longitude");
        rimServiceCompaniesId = intent.getStringExtra("rimServiceCompaniesId");
    }

    //初始化弹窗
    private void initPop() {
        view = LayoutInflater.from(this).inflate(R.layout.call_phone,null);
        popupWindow = new PopupWindow(view, SlidingUpPanelLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.pop_bg));
        colorDrawable.setAlpha(40);
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setOutsideTouchable(true);

        bo_hao = (TextView) view.findViewById(R.id.bo_hao);
        call_up = (TextView) view.findViewById(R.id.call_up);
        cancel = (TextView) view.findViewById(R.id.cancel);

        bo_hao.setOnClickListener(this);
        call_up.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.rl_item:
                Intent intent = new Intent(this,RimShopDetailActivity.class);
                intent.putExtra("rimServiceCompaniesId",rimServiceCompaniesId);
                intent.putExtra("name",name);
                intent.putExtra("address",address);
                intent.putExtra("telephone",telephone);
                intent.putExtra("callTimes", callTimes);
                startActivity(intent);
                this.finish();
                break;
            case R.id.iv_phone:
                bo_hao.setText(telephone);
                if (!bo_hao.getText().equals("")){
                    popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 0);  //在底部
                    popupWindow.update();
                }
                break;
            case R.id.call_up: //打电话
                if (!bo_hao.getText().equals("")){
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bo_hao.getText())));
                    rimDao.addCallHistory(rimServiceCompaniesId, bo_hao.getText().toString(), Arad.preferences.getString("memberId"));
                    popupWindow.dismiss();}
                break;
            case R.id.cancel: //取消
                popupWindow.dismiss();
                break;
        }
    }

}
