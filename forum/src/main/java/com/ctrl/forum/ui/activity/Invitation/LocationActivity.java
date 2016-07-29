package com.ctrl.forum.ui.activity.Invitation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.MyApplication;
import com.ctrl.forum.service.LocationService;
import com.ctrl.forum.ui.adapter.StoreSearchAddressListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 所在位置 activity
 * Created by jason on 2016/4/12.
 */
public class LocationActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.lv_locate)//下拉列表
            PullToRefreshListView lv_locate;
    @InjectView(R.id.rl_loact_search)//不显示位置
            RelativeLayout rl_loact_search;
    @InjectView(R.id.et_search)//搜索
            EditText et_search;


    private LocationService locationService;
    private String city;//城市名
    private PoiSearch mPoiSearch;
    private List<String> mPoiInfoListStr=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int totalPage;

    private static int PAGE_NUM=1;
    private PoiCitySearchOption option;
    private CharSequence et_s;
    private LatLng latLng;
    private boolean isRefresh=false;

    private List<PoiInfo> poiInfoList;
    private String keyWord;
    private StoreSearchAddressListAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initData() {
        adapter2 = new StoreSearchAddressListAdapter(this);
        lv_locate.setAdapter(adapter2);
    }


    /**
     * 初始化组件
     */
    private void initView() {
        rl_loact_search.setOnClickListener(this);
        et_search.addTextChangedListener(watcher);
        lv_locate.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        lv_locate.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                    PAGE_NUM += 1;
                    //第四步，发起检索请求；
                    if(!et_search.getText().toString().equals("")){
                        keyWord=et_search.getText().toString();
                    }
                Log.i("tag","keyword===="+keyWord);
                Log.i("tag","city===="+city);
                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .pageNum(PAGE_NUM)
                            .city(city)
                            .keyword(keyWord)
                            .pageCapacity(10));
            }

        });
        lv_locate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("locationLongitude",poiInfoList.get(position-1).location.latitude+"" );
                intent.putExtra("locationLatitude",poiInfoList.get(position-1).location.longitude+"");
                intent.putExtra("location", mPoiInfoListStr.get(position - 1));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //第一步，创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();

        // 第二步，创建POI检索监听者；
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            public void onGetPoiResult(PoiResult result) {
                lv_locate.onRefreshComplete();
                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结
                    MessageUtils.showShortToast(LocationActivity.this, "抱歉，未找到结果");
                    mPoiInfoListStr.clear();
                    adapter2.notifyDataSetChanged();
                    PAGE_NUM=1;
                    return;
                }

                if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                   // MessageUtils.showShortToast(LocationActivity.this, "检索结果正常返回");
                    poiInfoList=result.getAllPoi();
                    adapter2.setList(poiInfoList);
                }

            }

            public void onGetPoiDetailResult(PoiDetailResult result) {
                //获取Place详情页检索结果
            }
        };
        // 第三步，设置POI检索监听者；
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

    }


    private TextWatcher watcher = new TextWatcher() {
        //文字变化时
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if(mPoiInfoListStr!=null)
            mPoiInfoListStr.clear();
            if (s.toString() == null) {
                adapter2.notifyDataSetChanged();
                return;
            }
            //第四步，发起检索请求；
            mPoiSearch.searchInCity((new PoiCitySearchOption())
                    .pageNum(PAGE_NUM)
                    .city(city)
                    .keyword(s.toString())
                    .pageCapacity(10));



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
            if (s.length() == 0) {
                mPoiInfoListStr.clear();
                adapter2.notifyDataSetChanged();
            }
            et_s=s.toString();

        }
    };

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        locationService = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();
        showProgress(true);


    }



    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            showProgress(false);
            isRefresh=false;
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                latLng=new LatLng(location.getLatitude(),location.getLongitude());
                city = location.getCity();
                Address s = location.getAddress();
                keyWord=s.streetNumber;
              /*  Log.i("tag", "address=vvfvv==" + s.address);
                Log.i("tag", "street=vvfvv==" + s.street);
                Log.i("tag", "streetNumber=vvfvv==" + s.streetNumber);
                Log.i("tag", "cityCode=vvfvv==" + s.cityCode);
                Log.i("tag", "district=vvfvv==" + s.district);
                Log.i("tag", "address=vvfvv==" + s.address);
                Log.i("tag", "latidute=vvfvv==" + location.getLatitude());
                Log.i("tag", "longtidu=vvfvv==" + location.getLongitude());*/
                if(mPoiSearch!=null) {
                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .pageNum(PAGE_NUM)
                            .city(city)
                            .keyword(keyWord)
                            .pageCapacity(10))
                    ;
                }
           /*     adapter = new ArrayAdapter<String>(LocationActivity.this, android.R.layout.simple_list_item_1, mPoiInfoListStr);
                ListView lv = lv_locate.getRefreshableView();
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra("locationLongitude", location.getLongitude());
                        intent.putExtra("locationLatitude", location.getLatitude());
                        intent.putExtra("location", mPoiInfoListStr.get(position-1));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });*/
            }
        }

    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 第五步，释放POI检索实例；
        mPoiSearch.destroy();
    }

    @Override
    public boolean setupToolBarLeftText(TextView mLeftText) {
        mLeftText.setText("取消");
        mLeftText.setTextColor(getResources().getColor(R.color.text_blue));
        mLeftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {
        TextView tv_title = getmTitle();
        tv_title.setTextColor(getResources().getColor(R.color.text_black));
        return "所在位置";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_loact_search:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }


    }
}
