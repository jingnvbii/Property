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
import com.ctrl.forum.entity.ExchaneProduct;
import com.ctrl.forum.ui.adapter.MineIntegralRemarkListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 兑换积分商品记录
 */
public class MineRemarkHistoryActivity extends ToolBarActivity {
    private PullToRefreshListView lv_content;
    private MineIntegralRemarkListAdapter mineIntegralRemarkListAdapter;
    private List<ExchaneProduct> exchaneProducts;
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
                if (exchaneProducts!=null){
                    exchaneProducts.clear();
                    PAGE_NUM = 1;
                    rdao.convertRemarkHistory(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                }else{
                    lv_content.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (exchaneProducts!=null){
                    PAGE_NUM += 1;
                    rdao.convertRemarkHistory(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                }else{
                    lv_content.onRefreshComplete();
                }
            }
        });
    }

    private void initData() {
        rdao = new RemarkDao(this);
        rdao.convertRemarkHistory(Arad.preferences.getString("memberId"),PAGE_NUM+"", Constant.PAGE_SIZE+"");

        mineIntegralRemarkListAdapter = new MineIntegralRemarkListAdapter(this);
        lv_content.setAdapter(mineIntegralRemarkListAdapter);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.remark_history);}

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==2){
            exchaneProducts = rdao.gettRedeemHistory();
            if (exchaneProducts!=null){
                mineIntegralRemarkListAdapter.setExchaneProducts(exchaneProducts);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }
}
