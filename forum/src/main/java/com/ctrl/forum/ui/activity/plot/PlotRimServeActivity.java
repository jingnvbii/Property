package com.ctrl.forum.ui.activity.plot;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.widget.SlidingUpPanelLayout;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.PlotDao;
import com.ctrl.forum.dao.RimDao;
import com.ctrl.forum.entity.Category2;
import com.ctrl.forum.entity.RimServiceCompany;
import com.ctrl.forum.ui.adapter.PlotRimServeAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 小区--周边服务
 */
public class PlotRimServeActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.ll_category)
    LinearLayout ll_category;
    @InjectView(R.id.lv_content)
    PullToRefreshListView lv_content;

    private PlotDao plotDao;
    private RimDao rimDao;
    private List<RimServiceCompany> rimServiceCompanies;
    private List<Category2> category2s;
    private int PAGE_NUM=1;
    private PlotRimServeAdapter plotRimServeAdapter;
    private String CompantyCategyId;

    //弹窗
    private View view;
    private PopupWindow popupWindow;
    private TextView bo_hao,call_up,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_rim_serve);
        ButterKnife.inject(this);

        plotRimServeAdapter = new PlotRimServeAdapter(this);
        lv_content.setAdapter(plotRimServeAdapter);
        plotRimServeAdapter.setOnButton(this);

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (rimServiceCompanies != null) {
                    rimServiceCompanies.clear();
                    PAGE_NUM = 1;
                    plotRimServeAdapter = new PlotRimServeAdapter(getApplicationContext());
                    lv_content.setAdapter(plotRimServeAdapter);
                }
                plotDao.getAroundServiceCompanyList(PAGE_NUM + "", Constant.PAGE_SIZE + "",
                        Arad.preferences.getString("memberId"), CompantyCategyId,
                        Arad.preferences.getString("latitude"), Arad.preferences.getString("longitude"));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (rimServiceCompanies != null) {
                    PAGE_NUM += 1;
                    plotDao.getAroundServiceCompanyList(PAGE_NUM + "", Constant.PAGE_SIZE + "",
                            Arad.preferences.getString("memberId"), CompantyCategyId,
                            Arad.preferences.getString("latitude"), Arad.preferences.getString("longitude"));
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        initData();
        initPop();
    }

    //初始化分类列表
    private void initView() {
        int count = ll_category.getChildCount();
       for (int i=0;i<count;i++){
           TextView v = (TextView) ll_category.getChildAt(i);
           v.setText(category2s.get(i).getName());
           final int finalI = i;
           v.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   TextView tv =(TextView)v;
                   tv.setTextColor(getResources().getColor(R.color.red_bg));
                   CompantyCategyId = category2s.get(finalI).getId();
                   plotDao.getAroundServiceCompanyList(PAGE_NUM + "", Constant.PAGE_SIZE + "",
                           Arad.preferences.getString("memberId"), CompantyCategyId,
                           Arad.preferences.getString("latitude"), Arad.preferences.getString("longitude"));
               }
           });
       }
        plotDao.getAroundServiceCompanyList(PAGE_NUM + "", Constant.PAGE_SIZE + "",
                Arad.preferences.getString("memberId"), category2s.get(0).getId(),
                Arad.preferences.getString("latitude"), Arad.preferences.getString("longitude"));
    }

    private void initData() {
        plotDao = new PlotDao(this);
        rimDao = new RimDao(this);
        plotDao.getPostCategory(Arad.preferences.getString("communityId"), "2");

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

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {return getResources().getString(R.string.rim);}

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==4){
            category2s = plotDao.getCategory2();
            if (category2s!=null){
               //initView();
            }
        }
        if (requestCode==5){
            rimServiceCompanies = plotDao.getRimServiceCompanies();
            if (rimServiceCompanies!=null){
                plotRimServeAdapter.setRimServiceCompanies(rimServiceCompanies);
            }
        }//
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        Object id = v.getTag();
        int position = 0;
        switch (v.getId()){
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
}
