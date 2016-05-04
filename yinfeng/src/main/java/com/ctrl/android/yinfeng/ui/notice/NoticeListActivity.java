package com.ctrl.android.yinfeng.ui.notice;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.dao.NoticeDao;
import com.ctrl.android.yinfeng.entity.Notice;
import com.ctrl.android.yinfeng.ui.adapter.NoticeListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 内部公告列表 activity
 * Created by Eric on 2015/10/13.
 */
public class NoticeListActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.pull_to_refresh_listView)//可刷新的列表
            PullToRefreshListView mPullToRefreshListView;


    private NoticeListAdapter noticeListAdapter;
    private ListView mListView;
    private NoticeDao dao;
    private Notice notice;

    private List<Notice> listNotice;

    private int currentPage=1;
    private int rowCountPerPage=10;
    private int tatalCountPerPage=0;

    private String communityId = Arad.preferences.getString("communityId");
    private String activityId = Arad.preferences.getString("staffId");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.j_notice_list_activity);
        ButterKnife.inject(this);
       init();
    }

    @Override
    protected void onResume() {
        super.onResume();
          //  init();
    }

    /**
     * 初始化执行的 方法
     * */
    private void init(){
        dao = new NoticeDao(this);
        dao.requestNoticeList(communityId, activityId, String.valueOf(currentPage), String.valueOf(rowCountPerPage));//请求公告列表接口
        showProgress(true);
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        mPullToRefreshListView.onRefreshComplete();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPullToRefreshListView.onRefreshComplete();
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        mPullToRefreshListView.onRefreshComplete();
        showProgress(false);
        if(0 == requestCode){
            listNotice = dao.getNoticeList();
            mListView = mPullToRefreshListView.getRefreshableView();
            noticeListAdapter = new NoticeListAdapter(this);
            noticeListAdapter.setList(listNotice);
            mListView.setAdapter(noticeListAdapter);
            mListView.setSelection(tatalCountPerPage);
            mListView.setDivider(null);
            mListView.setDividerHeight(20);

            //注册上下拉定义事件
            mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
            mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                //下拉刷新
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    currentPage=1;
                    dao.getNoticeList().clear();
                    dao.requestNoticeList(communityId, activityId, String.valueOf(currentPage), String.valueOf(rowCountPerPage));//请求公告列表接口
                    tatalCountPerPage=0;
                }

                //上拉加载
                @Override
                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    currentPage+=1;
                    dao.requestNoticeList(communityId, activityId, String.valueOf(currentPage), String.valueOf(rowCountPerPage));//请求公告列表接口
                    tatalCountPerPage+=rowCountPerPage;
                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(NoticeListActivity.this, NoticeDetailActivity.class);
                    intent.putExtra("propertyNoticeId",listNotice.get(position - 1).getId());
                    startActivityForResult(intent, 1000);
                    AnimUtil.intentSlidIn(NoticeListActivity.this);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1000:
                if(1001==resultCode){
                    currentPage=1;
                    dao.getNoticeList().clear();
                    dao.requestNoticeList(communityId, activityId, String.valueOf(currentPage), String.valueOf(rowCountPerPage));//请求公告列表接口
                    tatalCountPerPage=0;
                }
        }
    }

    @Override
    public void onClick(View v) {
        //
    }


    /**
     * header 左侧按钮
     * */
    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        return true;
    }

    /**
     * 页面标题
     * */
    @Override
    public String setupToolBarTitle() {
        return "内部通知";
    }


}
