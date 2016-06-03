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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
 * 周边---搜索结果
 */
public class RimSearchReaultActivity extends ToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.lv_result)
    PullToRefreshListView lv_result;
    @InjectView(R.id.iv_back)
    ImageView iv_back;

    private String memberId,latitude,longitude,keyWord;
    private RimShopListAdapter rimShopListAdapter;
    private int PAGE_NUM=1;
    private List<RimServiceCompany> result;
    private RimDao rimDao;
    private Intent intent;
    private View view;
    private PopupWindow popupWindow;
    private TextView bo_hao,call_up,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_search_reault);
        ButterKnife.inject(this);
        intent = getIntent();
        keyWord = intent.getStringExtra("keyWord");

        initData();
        initPop();

        lv_result.setMode(PullToRefreshBase.Mode.BOTH);
        lv_result.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (result != null) {
                    result.clear();
                    PAGE_NUM = 1;
                    rimShopListAdapter = new RimShopListAdapter(getApplication());
                    lv_result.setAdapter(rimShopListAdapter);
                }
                rimDao.getAroundServiceCompanyList(PAGE_NUM + "", Constant.PAGE_SIZE + "", memberId, "", keyWord, latitude, longitude);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (result != null) {
                    PAGE_NUM += 1;
                    rimDao.getAroundServiceCompanyList(PAGE_NUM + "", Constant.PAGE_SIZE + "", memberId, "", keyWord, latitude, longitude);
                } else {
                    lv_result.onRefreshComplete();
                }
            }
        });
        lv_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (result != null) {
                    Intent intent = new Intent(getApplicationContext(), RimMapDetailActivity.class);
                    intent.putExtra("rimServiceCompaniesId", result.get(position - 1).getAroundServiceId());
                    intent.putExtra("name", result.get(position - 1).getName());
                    intent.putExtra("address", result.get(position - 1).getAddress());
                    intent.putExtra("telephone", result.get(position - 1).getTelephone());
                    intent.putExtra("callTimes",result.get(position-1).getCallTimes());
                    startActivity(intent);
                }
            }
        });
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

    private void initData() {
        iv_back.setOnClickListener(this);

        memberId = Arad.preferences.getString("memberId");
        latitude = Arad.preferences.getString("latitude");
        longitude = Arad.preferences.getString("lontitude");

        rimDao = new RimDao(this);
        rimDao.getAroundServiceCompanyList(PAGE_NUM+"",Constant.PAGE_SIZE+"",memberId,"",keyWord,latitude,longitude);

        rimShopListAdapter = new RimShopListAdapter(this);
        lv_result.setAdapter(rimShopListAdapter);
        rimShopListAdapter.setOnPhone(this);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_result.onRefreshComplete();
        if (requestCode==4){
            result = rimDao.getRimServiceCompanies();
            if (result!=null){
                rimShopListAdapter.setRimServiceCompanies(result);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_result.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        Object id = v.getTag();
        int position = 0;
        switch (v.getId()){
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.iv_phone:
                position = (int)id;
                bo_hao.setText(result.get(position).getTelephone());
                if (!bo_hao.getText().equals("")){
                    popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 0);  //在底部
                    popupWindow.update();
                }
                break;
            case R.id.call_up: //打电话
                if (!bo_hao.getText().equals("")){
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bo_hao.getText())));
                    rimDao.addCallHistory(result.get(position).getAroundServiceId(), bo_hao.getText().toString(), Arad.preferences.getString("memberId"));
                    popupWindow.dismiss();}
                break;
            case R.id.cancel: //取消
                popupWindow.dismiss();
                break;
            default:
                break;
        }
    }
}
