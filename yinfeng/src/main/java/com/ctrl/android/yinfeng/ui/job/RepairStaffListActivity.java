package com.ctrl.android.yinfeng.ui.job;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.dao.JobDao;
import com.ctrl.android.yinfeng.ui.adapter.RepairStaffListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RepairStaffListActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.lv_repair_staff_list)//维修人员展示列表
    PullToRefreshListView lv_repair_staff_list;
    private JobDao jdao;
    private RepairStaffListAdapter repairStaffListAdapter;
    private String staffId;
    private int currentPage = 1;
    private int rowCountPerPage = 10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_staff_list);
        ButterKnife.inject(this);
        init();
    }

    private void init() {
       jdao=new JobDao(this);
       jdao.requestStaffInfoList(Arad.preferences.getString("communityId"),"2", String.valueOf(currentPage), String.valueOf(rowCountPerPage));
        repairStaffListAdapter=new RepairStaffListAdapter(this);
        lv_repair_staff_list.setAdapter(repairStaffListAdapter);
        lv_repair_staff_list.setMode(PullToRefreshBase.Mode.BOTH);
        lv_repair_staff_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage=1;
                jdao.getStaffInfoList().clear();
                jdao.requestStaffInfoList(Arad.preferences.getString("communityId"), "2", String.valueOf(currentPage), String.valueOf(rowCountPerPage));

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage+=1;
                jdao.requestStaffInfoList(Arad.preferences.getString("communityId"), "2", String.valueOf(currentPage), String.valueOf(rowCountPerPage));
            }
        });
    }

    public void  setAssignOrder(String staffId){
        this.staffId=staffId;
        jdao.requestAssignOrder(getIntent().getStringExtra("repairDemandId"),
                staffId,
                Arad.preferences.getString("staffId"));
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_repair_staff_list.onRefreshComplete();
        if (5 == requestCode){
            repairStaffListAdapter.setList(jdao.getStaffInfoList());
        }
        if(6==requestCode){
            MessageUtils.showShortToast(this, "指派成功");
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_repair_staff_list.onRefreshComplete();
    }

    @Override
    public String setupToolBarTitle() {
        return "维修人员";
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
    public void onClick(View v) {

    }
}


