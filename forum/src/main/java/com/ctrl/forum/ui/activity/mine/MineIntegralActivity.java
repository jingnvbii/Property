package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.RemarkDao;
import com.ctrl.forum.entity.IntegralProduct;
import com.ctrl.forum.ui.adapter.MineIntegralGridAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 积分商城
 */
public class MineIntegralActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.rl_last)
    RelativeLayout rl_last;
    @InjectView(R.id.rl_dui)
    RelativeLayout rl_dui;
    @InjectView(R.id.rl_fen)
    RelativeLayout rl_fen;
    @InjectView(R.id.tv_total)
    TextView tv_total;

    private PullToRefreshGridView pullToRefreshGridView;
    private MineIntegralGridAdapter mineIntegralGridAdapter;
    private List<IntegralProduct> integralProducts;
    private RemarkDao rdao;
    private int PAGE_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_integral);
        ButterKnife.inject(this);

        initView();
        mineIntegralGridAdapter = new MineIntegralGridAdapter(this);
        pullToRefreshGridView.setAdapter(mineIntegralGridAdapter);

        pullToRefreshGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),MineIntegralStoreDetailActivity.class);
                intent.putExtra("integralProductsId",integralProducts.get(position).getId());
                startActivity(intent);
            }
        });

        pullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                if (integralProducts!=null){
                    integralProducts.clear();
                }
                PAGE_NUM = 1;
                rdao.getRemarkGoods(PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                if (integralProducts!=null){
                    PAGE_NUM += 1;
                    rdao.getRemarkGoods(PAGE_NUM + "", Constant.PAGE_SIZE + "");
                }
                    pullToRefreshGridView.onRefreshComplete();
            }
        });
    }

    private void initView() {
        pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pgv_shop);
        pullToRefreshGridView.setMode(PullToRefreshBase.Mode.BOTH);

        rl_last.setOnClickListener(this);
        rl_dui.setOnClickListener(this);
        rl_fen.setOnClickListener(this);

        tv_total.setText(Arad.preferences.getString("point"));

        rdao = new RemarkDao(this);
        rdao.getRemarkGoods(PAGE_NUM + "", Constant.PAGE_SIZE + "");
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
    public String setupToolBarTitle() {return getResources().getString(R.string.integral_shop);}

    @Override
    public void onClick(View v) {
        if (v==rl_dui){ //兑换记录
            startActivity(new Intent(this,MineRemarkHistoryActivity.class));
        }
        if (v==rl_fen){ //积分记录
            startActivity(new Intent(this,MinePointHistoryActivity.class));
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        pullToRefreshGridView.onRefreshComplete();
        if (requestCode==0){
            integralProducts = rdao.getIntegralProducts();
            if (integralProducts!=null){
                mineIntegralGridAdapter.setData(integralProducts);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        pullToRefreshGridView.onRefreshComplete();
    }

    @Override
    public void onNoConnect() {
        super.onNoConnect();
        pullToRefreshGridView.onRefreshComplete();
    }
}
