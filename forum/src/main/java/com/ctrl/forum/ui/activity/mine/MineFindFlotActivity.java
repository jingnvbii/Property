package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.MessageUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineplot);

        initData();

        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (communities!=null){
                    communities.clear();
                    PAGE_NUM = 1;
                    minePlotiAdapter = new MinePlotiAdapter(getApplicationContext());
                    lv_content.setAdapter(minePlotiAdapter);
                }
                plotDao.getPlot(Arad.preferences.getString("latitude"),Arad.preferences.getString("lontitude"),PAGE_NUM+"", Constant.PAGE_SIZE+"");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (communities!=null){
                    PAGE_NUM += 1;
                    plotDao.getPlot(Arad.preferences.getString("latitude"), Arad.preferences.getString("lontitude"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                }
               else {lv_content.onRefreshComplete();}
            }
        });

    }

    private void initData() {
        lv_content = (PullToRefreshListView) findViewById(R.id.lv_content);
        plotDao = new PlotDao(this);
        plotDao.getPlot(Arad.preferences.getString("latitude"),Arad.preferences.getString("lontitude"),PAGE_NUM+"", Constant.PAGE_SIZE+"");

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
            MessageUtils.showShortToast(this, "获取小区列表成功!");
            communities = plotDao.getCommunities();
            if (communities != null) {
                minePlotiAdapter.setMessages(communities);
            }
        }
        if (requestCode==1){
            MessageUtils.showShortToast(this, "加入小区成功!");
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
                MessageUtils.showShortToast(this,position+"");
                if (communities!=null){
                    String goodId = communities.get(position).getId();
                    plotDao.joinCel(Arad.preferences.getString("memberId"),goodId);
                }
                break;
        }
    }
}
