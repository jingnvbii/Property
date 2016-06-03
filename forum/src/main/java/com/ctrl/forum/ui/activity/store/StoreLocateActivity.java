package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.AddressDao;
import com.ctrl.forum.dao.SearchDao;
import com.ctrl.forum.entity.Address;
import com.ctrl.forum.entity.SearchHistory;
import com.ctrl.forum.ui.adapter.StoreSearchAddressListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城定位 activity
* */

public class StoreLocateActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.tv_locate_address)//暂无地址
            TextView tv_locate_address;
    @InjectView(R.id.tv_locate_history)//暂无历史记录
            TextView tv_locate_history;
    @InjectView(R.id.lv_locate_address)//地址列表
            ListView lv_locate_address;
    @InjectView(R.id.lv_locate_history)//历史记录列表
            ListView lv_locate_history;
    @InjectView(R.id.tv_locate_now)//定位当前
            TextView tv_locate_now;
    @InjectView(R.id.tv_delete_store_history)//清空历史记录
            TextView tv_delete_store_history;
    @InjectView(R.id.tv_seach)//搜索
            TextView tv_seach;
    @InjectView(R.id.et_locate_search)//搜索输入
            EditText et_locate_search;
    @InjectView(R.id.lv_search_address)//搜索地址列表
            ListView lv_search_address;
    @InjectView(R.id.ll_store_locate)//整体布局
            LinearLayout ll_store_locate;
    private AddressDao adao;
    private SearchDao sdao;
    private LocationClient mLocationClient;
    private MyLocationListener myListener;
    private GeoCoder mGeoCoder;
    private TextView tv;//标题地址栏
    private String address;
    private String latitude;
    private String longitude;
    private String province;
    private String city;
    private String area;
    private List<SearchHistory> listLocateSearch;
    private ArrayList<String> listHistoryStr;
    private ArrayAdapter historyAdapter;
    private ArrayList<String> listAddressStr;
    private List<Address> listAddress;
    private LatLng latlng;
    private Intent intent = new Intent();
    private PoiSearch mPoiSearch;
    private List<PoiInfo> poiInfoList;
    private static int PAGE_NUM = 1;
    private StoreSearchAddressListAdapter adapter;
    private double latitude1;
    private double longitude1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locate);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initLocation();
        initPoiSearch();
        initData();
    }

    private void initPoiSearch() {
        //第一步，创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();

        // 第二步，创建POI检索监听者；
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            public void onGetPoiResult(PoiResult result) {
                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结
                    MessageUtils.showShortToast(StoreLocateActivity.this, "抱歉，未找到结果");
                    return;
                }

                if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                    poiInfoList = result.getAllPoi();
                    adapter.setList(poiInfoList);
                }
            }

            public void onGetPoiDetailResult(PoiDetailResult result) {
                //获取Place详情页检索结果
            }
        };
        // 第三步，设置POI检索监听者；
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
    }


    private void initView() {
        et_locate_search.addTextChangedListener(watcher);
        tv_seach.setOnClickListener(this);
        tv_locate_now.setOnClickListener(this);
        tv_delete_store_history.setOnClickListener(this);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        lv_locate_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                latitude = listAddress.get(position).getLatitude();
                longitude = listAddress.get(position).getLongitude();
                Intent intent = new Intent();
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("address", listAddress.get(position).getAddressBase() + listAddress.get(position).getAddressDetail());
                setResult(557, intent);
                finish();
            }
        });
        lv_locate_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_locate_search.setText(listHistoryStr.get(position));
                // latitude = listLocateSearch.get(position).g();
                //  longitude = listLocateSearch.get(position).getLongitude();
            }
        });
    }

    private void initData() {
        showProgress(true);
        adao = new AddressDao(this);
        sdao = new SearchDao(this);
        adao.requestGetAddressList(Arad.preferences.getString("memberId"));
        sdao.requestSearchHistory(Arad.preferences.getString("memberId"), "4", "", "");

        adapter = new StoreSearchAddressListAdapter(StoreLocateActivity.this);
        lv_search_address.setAdapter(adapter);

        lv_search_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent=new Intent();
                intent.putExtra("address",poiInfoList.get(position).address);
                intent.putExtra("latitude",poiInfoList.get(position).location.latitude);
                intent.putExtra("longitude",poiInfoList.get(position).location.longitude);
                setResult(888,intent);
                finish();
            }
        });

        latitude1 = getIntent().getDoubleExtra("latitude1",0.000);
        longitude1 = getIntent().getDoubleExtra("longitude1", 0.00);


    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);

        // 地理编码
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new MyGetGeoCoderResultListener());
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            latlng = new LatLng(location.getLatitude(), location.getLongitude());
            //发起反地理编码检索
            mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption()).location(latlng));
        }
    }

    // 地理编码监听器
    private class MyGetGeoCoderResultListener implements OnGetGeoCoderResultListener {

        @Override
        public void onGetReverseGeoCodeResult(final  ReverseGeoCodeResult result) {
            //TODO 地理编码
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有找到检索结果
            }
            // 获取反向地理编码结果
            else {
                showProgress(false);
                // 当前位置信息
                tv_locate_now.setText(result.getAddress());
                latitude = String.valueOf(result.getLocation().latitude);
                longitude = String.valueOf(result.getLocation().longitude);

                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("address", result.getAddress());
                setResult(556, intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);


            }
        }


        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            //TODO 地理编码
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检索到结果
            }
            // 获取地理编码结果
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        //文字变化时
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (poiInfoList != null)
                poiInfoList.clear();
            if (s.toString() == null) {
                lv_search_address.setVisibility(View.GONE);
                ll_store_locate.setVisibility(View.VISIBLE);
                return;
            }
            ll_store_locate.setVisibility(View.GONE);
            lv_search_address.setVisibility(View.VISIBLE);
            //第四步，发起检索请求；
            mPoiSearch.searchNearby((new PoiNearbySearchOption())
                    .pageNum(PAGE_NUM)
                    .location(new LatLng(latitude1,longitude1))
                    .radius(500)
                    .keyword(s.toString())
                    .pageCapacity(10))
            ;
        }
        //文字变化前

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub


        }

        //文字变化后
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (s.toString().equals("")) {
                lv_search_address.setVisibility(View.GONE);
                ll_store_locate.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        if (requestCode == 0) {
            //   MessageUtils.showShortToast(this, "获取收货地址列表成功");
            listAddress = adao.getListAddress();
            if (listAddress.size() > 0) {
                tv_locate_address.setVisibility(View.GONE);
                lv_locate_address.setVisibility(View.VISIBLE);
            }
            listAddressStr = new ArrayList<String>();
            for (int i = 0; i < listAddress.size(); i++) {
                listAddressStr.add(listAddress.get(i).getAddressBase() + listAddress.get(i).getAddressDetail());
            }
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_layout, listAddressStr);
            lv_locate_address.setAdapter(adapter);
        }
        if (requestCode == 999) {
            MessageUtils.showShortToast(this, "获取搜索历史记录列表成功");
            listLocateSearch = sdao.getListSearchHistory();
            if (listLocateSearch.size() > 0) {
                tv_locate_history.setVisibility(View.GONE);
                lv_locate_history.setVisibility(View.VISIBLE);
            }
            listHistoryStr = new ArrayList<String>();
            for (int i = 0; i < listLocateSearch.size(); i++) {
                listHistoryStr.add(listLocateSearch.get(i).getKeyword());
            }
            historyAdapter = new ArrayAdapter(this, R.layout.spinner_layout, listHistoryStr);
            lv_locate_history.setAdapter(historyAdapter);
        }

        if (requestCode == 1000) {
            MessageUtils.showShortToast(this, "清空搜索历史记录列表成功");
            if (listHistoryStr != null) listLocateSearch.clear();
            historyAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_locate_now:
                showProgress(true);
                mLocationClient.start();
                break;
            case R.id.tv_delete_store_history:
                sdao.requestDeleteSearchHistory(Arad.preferences.getString("memberId"), "4");
                break;
            case R.id.tv_seach:
                if (TextUtils.isEmpty(et_locate_search.getText().toString().trim())) {
                    MessageUtils.showShortToast(this, "搜索关键字为空");

                } else {
                    sdao.requestSearchHistory(Arad.preferences.getString("memberId"), et_locate_search.getText().toString().trim(), latitude, longitude);
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        if (mGeoCoder != null)
            mGeoCoder.destroy();
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient = null;
        // 第五步，释放POI检索实例；
        mPoiSearch.destroy();
    }

    @Override
    public String setupToolBarTitle() {
        tv = getmTitle();
        Drawable drawable = getResources().getDrawable(R.mipmap.locate_img);
        drawable.setBounds(0, 0, 32, 32);
        tv.setCompoundDrawables(drawable, null, null, null);
        tv.setCompoundDrawablePadding(10);
        tv.setTextColor(Color.WHITE);
        tv.setWidth(200);
        tv.setText(getIntent().getStringExtra("address"));
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setFocusableInTouchMode(true);
        tv.setMarqueeRepeatLimit(100000000);
        tv.setHorizontallyScrolling(true);
        tv.setFocusable(true);
        tv.requestFocus();
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
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreLocateActivity.this, StoreLocateListMapActivity.class);
                startActivityForResult(intent, 333);
                AnimUtil.intentSlidIn(StoreLocateActivity.this);
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 333 && resultCode == RESULT_OK) {
            address = data.getStringExtra("address");
            latitude = data.getStringExtra("latitude");
            longitude = data.getStringExtra("longitude");
            province = data.getStringExtra("province");
            city = data.getStringExtra("city");
            area = data.getStringExtra("area");
            intent=new Intent();
            intent.putExtra("address",address);
            intent.putExtra("latitude",latitude);
            intent.putExtra("longitude",longitude);
            setResult(666, intent);
            finish();
        }
    }
}
