package com.ctrl.android.yinfeng.ui.visit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.dao.VisitDao;
import com.ctrl.android.yinfeng.entity.Visit;
import com.ctrl.android.yinfeng.ui.adapter.MyVisitAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 突发到访  列表  activity
* */

public class VisitProruptionActivity extends AppToolBarActivity {

    @InjectView(R.id.lv_visit_proruption)
    PullToRefreshListView lv_visit_proruption;

    private ListView mListView;
    private MyVisitAdapter adapter;

    private VisitDao visitDao;
    private String communityId = Arad.preferences.getString("communityId");
    private int currentPage = 1;
    //private int rowCountPerPage = Constant.PAGE_CAPACITY;
    private int rowCountPerPage = 10;
    private int totalCountPerPage = 0;

    private List<Visit> listVisit;
    private String visitType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_activity_visit_proruption);
        ButterKnife.inject(this);
        //init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {

        visitDao = new VisitDao(this);
        showProgress(true);
        visitDao.requestVisitList(communityId, String.valueOf(currentPage), String.valueOf(rowCountPerPage));
        lv_visit_proruption.setMode(PullToRefreshBase.Mode.BOTH);
        mListView = lv_visit_proruption.getRefreshableView();
        adapter = new MyVisitAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
        mListView.setDividerHeight(10);
        lv_visit_proruption.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;
                visitDao.getListVisit().clear();
                visitDao.requestVisitList(communityId, String.valueOf(currentPage), String.valueOf(rowCountPerPage));
                totalCountPerPage = 0;
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                ++currentPage;
                visitDao.requestVisitList(communityId, String.valueOf(currentPage), String.valueOf(rowCountPerPage));
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(VisitProruptionActivity.this,VisitProruptionDetailActivity.class);
                intent.putExtra("visitorname",listVisit.get(position-1).getVisitorName());
                intent.putExtra("membername",listVisit.get(position-1).getMemberName());
                intent.putExtra("building",listVisit.get(position-1).getBuilding());
                intent.putExtra("unit",listVisit.get(position-1).getUnit());
                intent.putExtra("room",listVisit.get(position-1).getRoom());
                intent.putExtra("gender",listVisit.get(position-1).getGender());
                intent.putExtra("peoplenumber",listVisit.get(position-1).getPeopleNum());
                intent.putExtra("drovevisit",listVisit.get(position-1).getDroveVisit());
                intent.putExtra("numberplates",listVisit.get(position-1).getNumberPlates());
                intent.putExtra("residenceTime",listVisit.get(position-1).getResidenceTime());
                intent.putExtra("qrImgUrl",listVisit.get(position-1).getQrImgUrl());
                intent.putExtra("arriveTime",listVisit.get(position-1).getArriveTime());
                intent.putExtra("visitNum",listVisit.get(position-1).getVisitNum());
                startActivity(intent);
                AnimUtil.intentSlidIn(VisitProruptionActivity.this);
            }
        });



    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        lv_visit_proruption.onRefreshComplete();

        if(0 == requestCode){
            listVisit = visitDao.getListVisit();
            adapter.setList(listVisit);
        }

    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_visit_proruption.onRefreshComplete();
    }

    @Override
    public String setupToolBarTitle() {
        return "突发到访";
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
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("添加");
        mRightText.setTextColor(getResources().getColor(R.color.text_white));
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisitProruptionActivity.this, VisitProruptionReleaseActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(VisitProruptionActivity.this);
            }
        });
        return true;
    }
}
