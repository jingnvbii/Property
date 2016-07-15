package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.PlotDao;
import com.ctrl.forum.entity.Communitys;
import com.ctrl.forum.ui.adapter.MinePlotiAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 我的小区--发现小区
 */
public class MineFindFlotActivity extends ToolBarActivity implements View.OnClickListener{
    private PullToRefreshListView lv_content;
    private int PAGE_NUM = 1;
    private List<Communitys> communities;
    private MinePlotiAdapter minePlotiAdapter;
    private PlotDao plotDao;
    private String communityName;
    private String goodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineplot);

        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (communities != null) {
                    communities.clear();
                    PAGE_NUM = 1;
                }
                //plotDao.getPlot(Arad.preferences.getString("latitude"),Arad.preferences.getString("lontitude"),PAGE_NUM+"", Constant.PAGE_SIZE+"");
                plotDao.getPlot("117.027055", "36.626039", PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (communities != null) {
                    PAGE_NUM += 1;
                    //plotDao.getPlot(Arad.preferences.getString("latitude"),Arad.preferences.getString("lontitude"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                    plotDao.getPlot("117.027055", "36.626039", PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

    }

    private void initData() {
        lv_content = (PullToRefreshListView) findViewById(R.id.lv_content);
        plotDao = new PlotDao(this);
       // plotDao.getPlot(Arad.preferences.getString("latitude"),Arad.preferences.getString("lontitude"),PAGE_NUM+"", Constant.PAGE_SIZE+"");
        plotDao.getPlot("117.027055","36.626039",PAGE_NUM+"", Constant.PAGE_SIZE+"");

        minePlotiAdapter = new MinePlotiAdapter(this);
        lv_content.setAdapter(minePlotiAdapter);
        minePlotiAdapter.setOnButton(this);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.find_plot);}

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==0) {
            communities = plotDao.getCommunities();
            if (communities != null) {
                minePlotiAdapter.setMessages(communities);
            }
        }
        if (requestCode==1){
            Arad.preferences.putString("communityId", goodId);
            Arad.preferences.putString("communityName", communityName);
            Arad.preferences.flush();
            setResult(RESULT_OK);
            this.finish();
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        Object id = v.getTag();
        switch (v.getId()){
            case R.id.tv_join:
                int position = (int) id;
                if (communities!=null){
                    goodId = communities.get(position).getId();
                    communityName = communities.get(position).getCommunityName();
                    plotDao.joinCel(Arad.preferences.getString("memberId"),goodId);
                }
                break;
        }
    }
}
