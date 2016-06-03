package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.MineStoreDao;
import com.ctrl.forum.entity.CompanyOrder;
import com.ctrl.forum.ui.adapter.MineOrderManagerAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 订单管理
 */
public class MineOrderManagerFragment extends ToolBarFragment {
    private PullToRefreshListView lv_content;
    private MineOrderManagerAdapter orderManagerAdapter;
    private List<CompanyOrder> companyOrders;
    private MineStoreDao odao;
    public static int type = 0;
    private int PAGE_NUM =1;


    public static MineOrderManagerFragment newInstance() {
        MineOrderManagerFragment fragment = new MineOrderManagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_mine_list, container, false);
        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        Bundle args = getArguments();
        type = args.getInt("position");
        //initView();
       initData();
        orderManagerAdapter = new MineOrderManagerAdapter(getActivity());
        lv_content.setAdapter(orderManagerAdapter);

        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (companyOrders != null) {
                    companyOrders.clear();
                    odao.companyOrderList(Arad.preferences.getString("companyId"), type + "", Constant.PAGE_SIZE + "", PAGE_NUM + "");
                } else {
                    lv_content.onRefreshComplete();
                }
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

        return view;
    }

    private void initView() {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_mine_order,null);
        TextView tv_state = (TextView) v.findViewById(R.id.tv_stage);
        Button bt_send = (Button) v.findViewById(R.id.bt_send);
        RelativeLayout rl_bt = (RelativeLayout) v.findViewById(R.id.rl_bt);
        switch (type){
            case 0:
                tv_state.setText("已付款");
                bt_send.setText("发货");
                rl_bt.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_state.setText("已付款");
                bt_send.setText("发货");
                rl_bt.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_state.setText("已发货");
                bt_send.setVisibility(View.GONE);
                rl_bt.setVisibility(View.GONE);
                break;
            case 3:
                tv_state.setText("已收货");
                bt_send.setVisibility(View.GONE);
                rl_bt.setVisibility(View.GONE);
                break;
        }
    }

    private void initData() {
        odao = new MineStoreDao(this);
        odao.companyOrderList(Arad.preferences.getString("companyId"),type+"", Constant.PAGE_SIZE+"",PAGE_NUM+"");
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==1){
            MessageUtils.showShortToast(getActivity(), "商家获取订单列表成功");
            companyOrders  = odao.getCompanyOrders();
            if (companyOrders!=null){
               orderManagerAdapter.setMessages(companyOrders);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
        MessageUtils.showShortToast(getActivity(), "商家获取订单列表失败!");
    }
}
