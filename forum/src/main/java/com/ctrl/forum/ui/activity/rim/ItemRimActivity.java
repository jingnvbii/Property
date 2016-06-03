package com.ctrl.forum.ui.activity.rim;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.widget.SlidingUpPanelLayout;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.RimDao;
import com.ctrl.forum.entity.RimServiceCompany;
import com.ctrl.forum.ui.adapter.RimShopListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 项目
 */
public class ItemRimActivity extends ToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.lv_shop)
    PullToRefreshListView lv_shop;
    @InjectView(R.id.bmapView)
    MapView bmapView;
    @InjectView(R.id.tv_item_one)
    TextView tv_item_one;

    private List<RimServiceCompany> rimServiceCompanies;
    private RimShopListAdapter rimShopListAdapter;
    private RimDao rimDao;
    private int PAGE_NUM=1;
    private String id,title;
    private BaiduMap mBaiduMap;

    private View view;
    private PopupWindow popupWindow;
    private TextView bo_hao,call_up,cancel;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_rim_item);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        initView();
        initPop();
        //initData();

        bmapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        //initMap();
        mLocationClient = new LocationClient(getApplicationContext()); //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();

        rimShopListAdapter = new RimShopListAdapter(this);
        lv_shop.setAdapter(rimShopListAdapter);
        rimShopListAdapter.setOnPhone(this);

        lv_shop.setMode(PullToRefreshBase.Mode.BOTH);
        lv_shop.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (rimServiceCompanies != null) {
                    rimServiceCompanies.clear();
                    PAGE_NUM = 1;
                    rimShopListAdapter = new RimShopListAdapter(getApplication());
                    lv_shop.setAdapter(rimShopListAdapter);
                }
                rimDao.getAroundServiceCompanyList(PAGE_NUM+ "", Constant.PAGE_SIZE + "", Arad.preferences.getString("memberId"), id, "", Arad.preferences.getString("latitude"), Arad.preferences.getString("lontitude"));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (rimServiceCompanies != null) {
                    PAGE_NUM += 1;
                    rimDao.getAroundServiceCompanyList(PAGE_NUM+ "", Constant.PAGE_SIZE + "", Arad.preferences.getString("memberId"), id, "", Arad.preferences.getString("latitude"), Arad.preferences.getString("lontitude"));
                }else {
                    lv_shop.onRefreshComplete();
                }
            }
        });

        lv_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (rimServiceCompanies!=null){
                    String rimServiceCompaniesId = rimServiceCompanies.get(position-1).getId();
                    Intent intent = new Intent(getApplicationContext(), RimMapDetailActivity .class);
                    intent.putExtra("rimServiceCompaniesId",rimServiceCompaniesId);
                    intent.putExtra("name",rimServiceCompanies.get(position-1).getName());
                    intent.putExtra("address",rimServiceCompanies.get(position-1).getAddress());
                    intent.putExtra("telephone",rimServiceCompanies.get(position-1).getTelephone());
                    intent.putExtra("callTimes",rimServiceCompanies.get(position-1).getCallTimes());
                    startActivity(intent);
                }
            }
        });

        mLocationClient.start();
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

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Arad.preferences.putString("latitude", location.getLatitude() + "");
            Arad.preferences.putString("lontitude",location.getLongitude()+"");
        }
    }
    /*private void initMap() {
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                //获取POI检索结果
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                //获取Place详情页检索结果
            }
        });
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("济南")
                .keyword("美食")
                .pageNum(10));
        //mPoiSearch.destroy();
    }*/

    private void initView() {
        tv_item_one.setText(title);

        rimDao = new RimDao(this);
        rimDao.getAroundServiceCompanyList(PAGE_NUM+ "", Constant.PAGE_SIZE + "", Arad.preferences.getString("memberId"), id, "", Arad.preferences.getString("latitude"), Arad.preferences.getString("lontitude"));
    }

    @Override
    public void onClick(View v) {
        Object id =v.getTag();
        int position = 0;
        switch (v.getId()){
          case R.id.iv_map://地图
              Intent intent = new Intent(this,RimMapDetailActivity.class);
              startActivity(intent);
              break;
          case R.id.iv_back:
              this.finish();
              break;
          case R.id.iv_phone:
             position = (int)id;
              bo_hao.setText(rimServiceCompanies.get(position).getTelephone());
              if (!bo_hao.getText().equals("")){
                  popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 0);  //在底部
                  popupWindow.update();
              }
              break;
          case R.id.call_up: //打电话
              if (!bo_hao.getText().equals("")){
                  startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bo_hao.getText())));
                  rimDao.addCallHistory(rimServiceCompanies.get(position).getAroundServiceId(), bo_hao.getText().toString(), Arad.preferences.getString("memberId"));
                  popupWindow.dismiss();}
              break;
          case R.id.cancel: //取消
              popupWindow.dismiss();
              break;
          default:
              break;
      }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_shop.onRefreshComplete();
        if (requestCode==4){
            rimServiceCompanies = rimDao.getRimServiceCompanies();
            if (rimServiceCompanies!=null){
                rimShopListAdapter.setRimServiceCompanies(rimServiceCompanies);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_shop.onRefreshComplete();
    }

}
