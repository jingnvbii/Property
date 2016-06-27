package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.RemarkDao;
import com.ctrl.forum.entity.RedeemHistory;
import com.ctrl.forum.ui.adapter.MineIntegralPointListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 积分记录
 */
public class MinePointHistoryActivity extends ToolBarActivity {
    private PullToRefreshListView lv_content;
    private MineIntegralPointListAdapter mineIntegralPointListAdapter;
    private List<RedeemHistory> redeemHistories;
    private int PAGE_NUM=1;
    private RemarkDao rdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_remark_history);
        initView();
        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (redeemHistories != null) {
                    redeemHistories.clear();
                    PAGE_NUM = 1;
                    rdao.getPointHistory(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (redeemHistories != null) {
                    PAGE_NUM += 1;
                    rdao.getPointHistory(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });
    }
    private void initData() {
        rdao = new RemarkDao(this);
        rdao.getPointHistory(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");

        mineIntegralPointListAdapter = new MineIntegralPointListAdapter(this);
        lv_content.setAdapter(mineIntegralPointListAdapter);
    }

    private void initView() {
        lv_content = (PullToRefreshListView) findViewById(R.id.lv_content);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.point_history);}

   /* @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText(getResources().getString(R.string.clear_all));//清空
        mRightText.setTextColor(getResources().getColor(R.color.text_white));
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (redeemHistories!=null){
                    redeemHistories.clear();
                    mineIntegralPointListAdapter.setExchaneProducts(redeemHistories);
                }
            }
        });
        return true;
    }*/

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==3){
            redeemHistories = rdao.getRedeemHistories();
            if (redeemHistories!=null){
                mineIntegralPointListAdapter.setExchaneProducts(redeemHistories);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

}
