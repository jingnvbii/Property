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

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
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
    @InjectView(R.id.tv_item_one)
    TextView tv_item_one;

    private BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_chatbox);
    private List<RimServiceCompany> rimServiceCompanies;
    private RimShopListAdapter rimShopListAdapter;
    private RimDao rimDao;
    private int PAGE_NUM=1;
    private String id,title;

    private View view;
    private PopupWindow popupWindow;
    private TextView bo_hao,call_up,cancel;
    static int position;
    private static final LatLng GEO_SHENGDONG = new LatLng(36, 117);
    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_rim_item);
        ButterKnife.inject(this);
        mMapView = (MapView) findViewById(R.id.bmapView);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        initView();
        initPop();

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
                }
                rimDao.getAroundServiceCompanyList(PAGE_NUM + "", Constant.PAGE_SIZE + "", Arad.preferences.getString("memberId"), id, "", Arad.preferences.getString("latitude"), Arad.preferences.getString("lontitude"));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (rimServiceCompanies != null) {
                    PAGE_NUM += 1;
                    rimDao.getAroundServiceCompanyList(PAGE_NUM + "", Constant.PAGE_SIZE + "", Arad.preferences.getString("memberId"), id, "", Arad.preferences.getString("latitude"), Arad.preferences.getString("lontitude"));
                } else {
                    lv_shop.onRefreshComplete();
                }
            }
        });

        lv_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (rimServiceCompanies != null) {
                    String rimServiceCompaniesId = rimServiceCompanies.get(position - 1).getId();
                    Intent intent = new Intent(getApplicationContext(), RimMapDetailActivity.class);
                    intent.putExtra("rimServiceCompaniesId", rimServiceCompaniesId);
                    intent.putExtra("name", rimServiceCompanies.get(position - 1).getName());
                    intent.putExtra("address", rimServiceCompanies.get(position - 1).getAddress());
                    intent.putExtra("telephone", rimServiceCompanies.get(position - 1).getTelephone());
                    intent.putExtra("callTimes", rimServiceCompanies.get(position - 1).getCallTimes());
                    intent.putExtra("latitude",rimServiceCompanies.get(position-1).getLatitude());
                    intent.putExtra("longitude",rimServiceCompanies.get(position-1).getLongitude());
                    startActivity(intent);
                }
            }
        });

    }

    private void initMap() {
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);

        //MapStatusUpdate u4 = MapStatusUpdateFactory.newLatLng(GEO_SHENGDONG);
        //MapStatusUpdate u41 = MapStatusUpdateFactory.zoomBy(14);
       // mBaiduMap.setMapStatus(u4);
       // mBaiduMap.setMapStatus(u41);

        initOverlay();
    }

    private void initOverlay() {

        Marker mMarker = null;
        for(int i=0;i<rimServiceCompanies.size();i++){

            Double latitude = rimServiceCompanies.get(i).getLatitude();
            Double longitude = rimServiceCompanies.get(i).getLongitude();
            MarkerOptions ooA = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(bdA).zIndex(9).draggable(false);
            //掉下动画
            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
            mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
            mBaiduMap.addOverlay(ooA);
        }

        Overlay overlay;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(mMarker.getPosition());
        mBaiduMap.setMapStatus(MapStatusUpdateFactory
                .newLatLngBounds(builder.build()));

    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mMapView.onDestroy();
        super.onDestroy();
        bdA.recycle();
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

    private void initView() {
        tv_item_one.setText(title);

        rimDao = new RimDao(this);
        rimDao.getAroundServiceCompanyList(PAGE_NUM + "", Constant.PAGE_SIZE + "", Arad.preferences.getString("memberId"), id, "", Arad.preferences.getString("latitude"), Arad.preferences.getString("lontitude"));
    }

    @Override
    public void onClick(View v) {
        Object id =v.getTag();
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
                  rimDao.addCallHistory(rimServiceCompanies.get(position).getId(), bo_hao.getText().toString(), Arad.preferences.getString("memberId"));
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
            initMap();
        }
        if (requestCode==9){
            if (rimServiceCompanies!=null){
                rimServiceCompanies.clear();
            }
            rimDao.getAroundServiceCompanyList(PAGE_NUM+ "", Constant.PAGE_SIZE + "", Arad.preferences.getString("memberId"),
                    id, "", Arad.preferences.getString("latitude"), Arad.preferences.getString("lontitude"));
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_shop.onRefreshComplete();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (rimServiceCompanies!=null){
            rimServiceCompanies.clear();
        }
        rimDao.getAroundServiceCompanyList(PAGE_NUM + "", Constant.PAGE_SIZE + "", Arad.preferences.getString("memberId"),
                id, "", Arad.preferences.getString("latitude"), Arad.preferences.getString("lontitude"));
    }
}
