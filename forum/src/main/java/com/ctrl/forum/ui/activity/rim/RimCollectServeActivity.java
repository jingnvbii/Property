package com.ctrl.forum.ui.activity.rim;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
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
 * 周边服务--我的收藏
 */
public class RimCollectServeActivity extends ToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.lv_collect)
    PullToRefreshListView lv_collect;
    @InjectView(R.id.iv_back)
    ImageView iv_back;

    private RimShopListAdapter rimShopListAdapter;
    private List<RimServiceCompany> rimServiceCompanies;
    private RimDao rimDao;
    private int PAGE_NUM=1;
    private View contentView;
    private PopupWindow popupWindow;
    private TextView bo_hao,call_up,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_collect_serve);
        ButterKnife.inject(this);
        iv_back.setOnClickListener(this);

        rimShopListAdapter = new RimShopListAdapter(this);
        lv_collect.setAdapter(rimShopListAdapter);
        rimShopListAdapter.setOnPhone(this);

        initData();
        initPop();

        lv_collect.setMode(PullToRefreshBase.Mode.BOTH);
        lv_collect.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (rimServiceCompanies != null) {
                    rimServiceCompanies.clear();
                    PAGE_NUM = 1;
                    rimShopListAdapter = new RimShopListAdapter(getApplication());
                    lv_collect.setAdapter(rimShopListAdapter);
                }
                rimDao.getAroundServiceCollectionList(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (rimServiceCompanies != null) {
                    PAGE_NUM += 1;
                    rimDao.getAroundServiceCollectionList(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_collect.onRefreshComplete();
                }
            }
        });

        lv_collect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (rimServiceCompanies != null) {
                    Intent intent = new Intent(getApplicationContext(), RimMapDetailActivity.class);
                    intent.putExtra("rimServiceCompaniesId", rimServiceCompanies.get(position - 1).getAroundServiceId());
                    intent.putExtra("name", rimServiceCompanies.get(position - 1).getName());
                    intent.putExtra("address", rimServiceCompanies.get(position - 1).getAddress());
                    intent.putExtra("telephone", rimServiceCompanies.get(position - 1).getTelephone());
                    intent.putExtra("callTimes", rimServiceCompanies.get(position - 1).getCallTimes());
                    intent.putExtra("latitude", rimServiceCompanies.get(position - 1).getLatitude()+"");
                    intent.putExtra("longitude", rimServiceCompanies.get(position - 1).getLongitude()+"");
                    startActivity(intent);
                }
            }
        });

    }

    //初始化弹窗
    private void initPop() {
        contentView = LayoutInflater.from(this).inflate(R.layout.call_phone,null);
        popupWindow = new PopupWindow(contentView,getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels);
        popupWindow.setFocusable(true);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.pop_bg));
        colorDrawable.setAlpha(40);
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setOutsideTouchable(true);

        bo_hao = (TextView) contentView.findViewById(R.id.bo_hao);
        call_up = (TextView) contentView.findViewById(R.id.call_up);
        cancel = (TextView) contentView.findViewById(R.id.cancel);

        bo_hao.setOnClickListener(this);
        call_up.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void initData() {
        rimDao = new RimDao(this);
        rimDao.getAroundServiceCollectionList(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
    }

    @Override
    public void onClick(View v) {
        Object id =v.getTag();
        int position = 0;
        switch (v.getId()){
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.iv_phone:
                initPop();
                position = (int)id;
                bo_hao.setText(rimServiceCompanies.get(position).getTelephone());
                if (!bo_hao.getText().equals("")){
                    popupWindow.showAtLocation(this.contentView, Gravity.BOTTOM, 0, 120);  //在底部
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
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_collect.onRefreshComplete();
        if (requestCode==8){
            rimServiceCompanies = rimDao.getRimServiceCompanies();
            if (rimServiceCompanies!=null){
                rimShopListAdapter.setRimServiceCompanies(rimServiceCompanies);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_collect.onRefreshComplete();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initPop();
        if (rimServiceCompanies != null) {
            rimServiceCompanies.clear();
            PAGE_NUM = 1;
            rimShopListAdapter = new RimShopListAdapter(getApplication());
            lv_collect.setAdapter(rimShopListAdapter);
        }
        rimDao.getAroundServiceCollectionList(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
    }
}
