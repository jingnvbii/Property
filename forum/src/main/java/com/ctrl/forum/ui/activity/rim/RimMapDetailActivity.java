package com.ctrl.forum.ui.activity.rim;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
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
    private String name,address,telephone,rimServiceCompaniesId;
    private int callTimes;
    private View view;
    private PopupWindow popupWindow;
    private TextView bo_hao,call_up,cancel;
    private RimDao rimDao;
    private BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_chatbox);

    /**
     * MapView 是地图主控件
     */
    private BaiduMap mBaiduMap;
    // 我的位置坐标
    double mLat1;
    double mLon1;
    // 目标位置坐标
    double mLat2;
    double mLon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_map_detail);
        ButterKnife.inject(this);

        if (Arad.preferences.getString("latitude")!=null && Arad.preferences.getString("longitude")!=null) {
            mLat1 = Double.valueOf(Arad.preferences.getString("latitude"));
            mLon1 = Double.valueOf(Arad.preferences.getString("lontitude"));
        }

        mLat2 = Double.valueOf(getIntent().getStringExtra("latitude"));
        mLat2 = Double.valueOf(getIntent().getStringExtra("longitude"));

        initData();
        initView();
        initPop();

        initMap();
    }

    private void initMap() {
        mBaiduMap = iv_map.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);

        initOverlay();
    }

    private void initOverlay() {
        Double la = getIntent().getDoubleExtra("latitude",36);
        Double lo = getIntent().getDoubleExtra("longitude", 117);

        Marker mMarker = null;
        MarkerOptions ooA = new MarkerOptions().position(new LatLng(la,lo)).icon(bdA).zIndex(9).draggable(false);
        //掉下动画
        ooA.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
        mBaiduMap.addOverlay(ooA);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(mMarker.getPosition());
        mBaiduMap.setMapStatus(MapStatusUpdateFactory
                .newLatLngBounds(builder.build()));

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
        tv_total.setText(callTimes + "");
    }

    private void initData() {
        intent = getIntent();
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        telephone = intent.getStringExtra("telephone");
        callTimes = Integer.parseInt(intent.getStringExtra("callTimes"));
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
                startActivity(intent);
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
            case R.id.tv_dh: //导航
                startNavi();
               /* Intent intent1 = new Intent(this,NaviActivity.class);
                startActivity(intent1);*/
                break;
        }
    }

    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2);
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(RimMapDetailActivity.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==9){
            tv_total.setText(""+callTimes++);
        }
    }
}
