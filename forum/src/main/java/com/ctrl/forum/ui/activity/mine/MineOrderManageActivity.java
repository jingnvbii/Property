package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.MineStoreDao;
import com.ctrl.forum.entity.CompanyOrder;
import com.ctrl.forum.ui.adapter.MineOrderManagerAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 订单管理
 */
public class MineOrderManageActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.ll_text)
    LinearLayout ll_text;
    @InjectView(R.id.lv_content)
    PullToRefreshListView lv_content;
    @InjectView(R.id.lines)
    LinearLayout lines;
    @InjectView(R.id.tv_xian)
    TextView tv_xian;
    @InjectView(R.id.tv_shop_manager)
    TextView tv_shop_manager;

    private MineOrderManagerAdapter orderManagerAdapter;
    private List<CompanyOrder> companyOrders = new ArrayList<>();
    private MineStoreDao odao;
    public static int type = 0;
    private int PAGE_NUM =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_shop_manage);
        ButterKnife.inject(this);

        initView();

        orderManagerAdapter = new MineOrderManagerAdapter(this);
        lv_content.setAdapter(orderManagerAdapter);
        orderManagerAdapter.setOnButton(this);

        initData();

        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (companyOrders != null) {
                    companyOrders.clear();
                }
                PAGE_NUM = 1;
                odao.companyOrderList(Arad.preferences.getString("companyId"), type + "", Constant.PAGE_SIZE + "", PAGE_NUM + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (companyOrders != null) {
                    PAGE_NUM += 1;
                    odao.companyOrderList(Arad.preferences.getString("companyId"), type + "", Constant.PAGE_SIZE + "", PAGE_NUM + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MerchantOrderDetailActivity.class);
                intent.putExtra("id", companyOrders.get(position - 1).getId());
                intent.putExtra("deliveryNo", companyOrders.get(position - 1).getDeliveryNo());
                intent.putExtra("expressName", companyOrders.get(position - 1).getExpressName());
                intent.putExtra("receiverMobile", companyOrders.get(position - 1).getReceiverMobile());
                intent.putExtra("evaluationState", companyOrders.get(position - 1).getEvaluationState());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        odao = new MineStoreDao(this);
        odao.companyOrderList(Arad.preferences.getString("companyId"), type + "", Constant.PAGE_SIZE + "", PAGE_NUM + "");
    }

    private void initView() {
        tv_xian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//现金劵
               startActivity(new Intent(getApplicationContext(),MineMerchantCouponActivity.class));
            }
        });
        tv_shop_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//店铺管理
                startActivity(new Intent(getApplicationContext(), MineShopManagerActivity.class));
            }
        });
        ((TextView) ll_text.getChildAt(0))
                .setTextColor(getResources().getColor(R.color.red_bg));
        lines.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.red_bg));
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
    public String setupToolBarTitle() {return "订单管理";}

    public void onClick(View v) {
        Object id = v.getTag();
        switch (v.getId()){
            case R.id.bt_send://发货
                int position = (int)id;
                odao.deliverGoods(companyOrders.get(position).getId(),Arad.preferences.getString("memberId"), companyOrders.get(position).getOrderNum());
                break;
            default:
        for (int i = 0; i < ll_text.getChildCount(); i++) {
            ((TextView) ll_text.getChildAt(i)).setTextColor(getResources().getColor(R.color.text_black1));
            lines.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.line_gray));
        }
        ((TextView) v).setTextColor(getResources().getColor(R.color.red_bg));
        (lines.getChildAt(ll_text.indexOfChild(v))).setBackgroundColor(getResources().getColor(R.color.red_bg));
        type = ll_text.indexOfChild(v);
        if (companyOrders!=null){
            companyOrders.clear();
            orderManagerAdapter.setCompanyOrders(companyOrders);
            lv_content.setAdapter(orderManagerAdapter);
        }
        PAGE_NUM=1;
        odao.companyOrderList(Arad.preferences.getString("companyId"), type + "", Constant.PAGE_SIZE + "", PAGE_NUM + "");
        break;
        }

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==1){
            companyOrders  = odao.getCompanyOrders();
            if (companyOrders!=null){
                orderManagerAdapter.setCompanyOrders(companyOrders);
            }
            orderManagerAdapter.notifyDataSetChanged();
        }
        if (requestCode==8){
            MessageUtils.showShortToast(this, "发货成功");
            if (companyOrders!=null){
                companyOrders.clear();
                PAGE_NUM=1;
            }
            odao.companyOrderList(Arad.preferences.getString("companyId"), type + "", Constant.PAGE_SIZE + "", PAGE_NUM + "");
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

}
