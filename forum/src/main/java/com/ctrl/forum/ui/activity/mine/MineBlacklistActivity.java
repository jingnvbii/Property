package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.EditDao;
import com.ctrl.forum.entity.Blacklist;
import com.ctrl.forum.ui.adapter.MineBlacklistAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

public class MineBlacklistActivity extends AppToolBarActivity implements View.OnClickListener{
    private List<Blacklist> blacklists;
    private EditDao edao;
    private MineBlacklistAdapter blacklistAdapter;
    private PullToRefreshListView lv_blacklist;
    private int PAGE_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_blacklist);
        lv_blacklist = (PullToRefreshListView) findViewById(R.id.lv_blacklist);

        blacklistAdapter = new MineBlacklistAdapter(this);
        lv_blacklist.setAdapter(blacklistAdapter);
        initData();
        blacklistAdapter.setOnButton(this);

        lv_blacklist.setMode(PullToRefreshBase.Mode.BOTH);
        lv_blacklist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (blacklists != null) {
                    blacklists.clear();
                    PAGE_NUM = 1;
                    blacklistAdapter = new MineBlacklistAdapter(getApplicationContext());
                    lv_blacklist.setAdapter(blacklistAdapter);
                }
                edao.getBlackList(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (blacklists != null) {
                    PAGE_NUM += 1;
                    edao.getBlackList(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_blacklist.onRefreshComplete();
                }
            }
        });
    }

    private void initData() {
        edao = new EditDao(this);
        edao.getBlackList(Arad.preferences.getString("memberId"),PAGE_NUM+"",Constant.PAGE_SIZE+"");
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_blacklist.onRefreshComplete();
        if (requestCode==2){
            MessageUtils.showShortToast(this, "获取成功");
            blacklists = edao.getBlacklists();
            if (blacklists!=null){
                blacklistAdapter.setBlacklists(blacklists);
            }
        }
        if (requestCode==3){
            MessageUtils.showShortToast(this,"去除成功");
            blacklists = edao.getBlacklists();
            blacklistAdapter.setBlacklists(blacklists);
        }
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
    public String setupToolBarTitle() {return getResources().getString(R.string.my_blacklist);}

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        switch (v.getId()){
            case R.id.bt_clear: //取消屏蔽
                int position = (int) tag;
                if (blacklists!=null){
                    String id = blacklists.get(position).getId();
                    edao.cancelBlack(Arad.preferences.getString("memberId"),id);}
                break;
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_blacklist.onRefreshComplete();
    }
}
