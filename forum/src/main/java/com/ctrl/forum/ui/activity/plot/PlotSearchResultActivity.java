package com.ctrl.forum.ui.activity.plot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.PlotDao;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.ui.adapter.InvitationListViewFriendStyleAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 小区--搜索
 */
public class PlotSearchResultActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.iv_back)
    ImageView iv_back;
    @InjectView(R.id.lv_content)
    PullToRefreshListView lv_content; //周边服务

    private InvitationListViewFriendStyleAdapter invitationListViewFriendStyleAdapter;
    private List<Post> posts;
    private String keyWord;
    private PlotDao plotDao;
    private int PAGE_NUM =1;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_search_result);
        ButterKnife.inject(this);
        initView();

        invitationListViewFriendStyleAdapter = new InvitationListViewFriendStyleAdapter(this);
        lv_content.setAdapter(invitationListViewFriendStyleAdapter);

        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (posts != null) {
                    posts.clear();
                    PAGE_NUM = 1;
                    invitationListViewFriendStyleAdapter = new InvitationListViewFriendStyleAdapter(activity);
                    lv_content.setAdapter(invitationListViewFriendStyleAdapter);
                }
                plotDao.queryPostList(Arad.preferences.getString("memberId"), "1", keyWord,
                        Arad.preferences.getString("communityId"),
                        PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (posts != null) {
                    PAGE_NUM += 1;
                    plotDao.queryPostList(Arad.preferences.getString("memberId"), "1", keyWord,
                            Arad.preferences.getString("communityId"),
                            PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

    }

    private void initData() {
        plotDao = new PlotDao(this);
        keyWord = getIntent().getStringExtra("keyWord");
        plotDao.queryPostList(Arad.preferences.getString("memberId"),"1",keyWord,
                Arad.preferences.getString("communityId"),
                PAGE_NUM+"",Constant.PAGE_SIZE+"");
    }

    private void initView() {
        activity = this;
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                this.finish();
                break;
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==3){
            posts = plotDao.getSearchPost();
            if (posts!=null){
                invitationListViewFriendStyleAdapter.setList(posts);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }
}
