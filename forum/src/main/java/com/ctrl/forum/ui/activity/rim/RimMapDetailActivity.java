package com.ctrl.forum.ui.activity.rim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.ctrl.forum.R;
import com.ctrl.forum.dao.RimDao;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 地图详情
 */
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


    private String mSDCardPath = null;
    private static final String APP_FOLDER_NAME = "forum";
    String authinfo = null;
    public static List<Activity> activityList = new LinkedList<Activity>();
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";

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
        mLon2 = Double.valueOf(getIntent().getStringExtra("longitude"));

        initData();
        initView();
        initPop();

        initMap();

        if (initDirs()) {
            initNavi();
        }
    }

    private void initMap() {
        mBaiduMap = iv_map.getMap();
        /*MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18);
        mBaiduMap.setMapStatus(msu);*/

        LatLng cenpt = new LatLng(mLat2,mLon2);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_location_chatbox);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(cenpt)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        //initOverlay();
    }

    private void initOverlay() {

        Double la =Double.valueOf(getIntent().getStringExtra("latitude"));
        Double lo = Double.valueOf(getIntent().getStringExtra("longitude"));

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
        popupWindow = new PopupWindow(view, getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels);
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
                    popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 120);  //在底部
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
            case R.id.tv_dh: //开启导航
                if (BaiduNaviManager.isNaviInited()) {
                    routeplanToNavi(CoordinateType.BD09LL);
                }
                break;
        }
    }

    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
               /* if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }*/
                RimMapDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(RimMapDetailActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                //Toast.makeText(RimMapDetailActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();
            }

            public void initStart() {
                //Toast.makeText(RimMapDetailActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                //Toast.makeText(RimMapDetailActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }


        }, null, ttsHandler, null);

    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
    //设置导航模式
    private void initSetting(){
        //BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        //BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
       // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        //setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    //showToastMsg("Handler : TTS play start");
                    //showToastMsg("导航开始");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    //showToastMsg("Handler : TTS play end");
                    //showToastMsg("导航结束");
                    break;
                }
                default :
                    break;
            }
        }
    };
    public void showToastMsg(final String msg) {
        RimMapDetailActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(RimMapDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void routeplanToNavi(CoordinateType coType) {
       /* BNRoutePlanNode sNode = new BNRoutePlanNode(Double.valueOf(Arad.preferences.getString("lontitude")),
                Double.valueOf(Arad.preferences.getString("latitude")),
                "我的位置", null, coType);

        BNRoutePlanNode eNode =new BNRoutePlanNode(mLon2,mLat2,name, null, coType);*/

        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        sNode = new BNRoutePlanNode(Double.valueOf(Arad.preferences.getString("lontitude")),
                Double.valueOf(Arad.preferences.getString("latitude")), "我的位置", null, coType);
        eNode = new BNRoutePlanNode(mLon2, mLat2, name, null, coType);
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */
            for (Activity ac : activityList) {
                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
                    return;
                }
            }
            Intent intent = new Intent(RimMapDetailActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(RimMapDetailActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==9){
            tv_total.setText(""+callTimes++);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initPop();
    }
}
