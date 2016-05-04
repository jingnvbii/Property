package com.ctrl.forum.ui.activity.Invitation;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

/**
 *
 * 所在位置 activity
 * Created by jason on 2016/4/12.
 */
public class LocationActivity extends AppToolBarActivity{
    private GeoCoder mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);
        // 隐藏输入法
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();
        initSearch();
    }

    private void initSearch() {
         mSearch = GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                }
                //获取地理编码结果
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                }
                //获取反向地理编码结果
            }
        };
        mSearch.setOnGetGeoCodeResultListener(listener);
        //发起地理编码检索
        mSearch.geocode(new GeoCodeOption());
        //释放地理编码检索实例
        mSearch.destroy();
    }

    /**
     * 初始化组件
     */
    private void initView() {
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

}
