package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.MineStoreDao;
import com.ctrl.forum.dao.OrderDao;
import com.ctrl.forum.entity.CompanyOrder;
import com.ctrl.forum.ui.activity.mine.MerchantOrderDetailActivity;
import com.ctrl.forum.ui.adapter.MineOrderManagerAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 订单管理
 */
public class MineOrderManagerFragment extends ToolBarFragment implements View.OnClickListener{
    private PullToRefreshListView lv_content;
    private MineOrderManagerAdapter orderManagerAdapter;
    private List<CompanyOrder> companyOrders;
    private MineStoreDao odao;
    public static int type = 0;
    private int PAGE_NUM =1;
    private OrderDao orderDao;

    public static MineOrderManagerFragment newInstance() {
        MineOrderManagerFragment fragment = new MineOrderManagerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_mine_list, container, false);
        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        Bundle args = getArguments();
        type = args.getInt("position");

        orderManagerAdapter = new MineOrderManagerAdapter(getActivity());
        lv_content.setAdapter(orderManagerAdapter);
        //orderManagerAdapter.setOnButton(this);

        initData();

        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (companyOrders != null) {
                    companyOrders.clear();
                }
                PAGE_NUM = 1;
                Log.e("typr===================", type + "");
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
                Intent intent = new Intent(getActivity(), MerchantOrderDetailActivity.class);
                intent.putExtra("id",companyOrders.get(position-1).getId());
                intent.putExtra("deliveryNo",companyOrders.get(position-1).getDeliveryNo());
                intent.putExtra("expressName",companyOrders.get(position-1).getExpressName());
                startActivity(intent);
            }
        });

        return view;
    }

    private void initData() {
        odao = new MineStoreDao(this);
        odao.companyOrderList(Arad.preferences.getString("companyId"), type + "", Constant.PAGE_SIZE + "", PAGE_NUM + "");
        orderDao = new OrderDao(this);
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
        }
        if (requestCode==8){
            MessageUtils.showShortToast(getActivity(),"发货成功");
            if (companyOrders!=null){
                companyOrders.clear();
            }
            odao.companyOrderList(Arad.preferences.getString("companyId"), type + "", Constant.PAGE_SIZE + "", PAGE_NUM + "");
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
           /* case R.id.bt_send:
                int position = (int)id;
                odao.deliverGoods(companyOrders.get(position).getId(),Arad.preferences.getString("memberId"), companyOrders.get(position).getOrderNum());
                break;*/
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        PAGE_NUM=1;
    }
}
