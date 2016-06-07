package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.MineStoreDao;
import com.ctrl.forum.dao.OrderDao;
import com.ctrl.forum.entity.CompanyOrder;
import com.ctrl.forum.entity.OrderItem;
import com.ctrl.forum.entity.OrderState;
import com.ctrl.forum.ui.adapter.MineOrderManagerAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单管理
 */
public class MineOrderManagerFragment extends ToolBarFragment implements View.OnClickListener{
    private PullToRefreshListView lv_content;
    private MineOrderManagerAdapter orderManagerAdapter;
    private List<CompanyOrder> companyOrders;
    private List<OrderItem> orderItems;
    private List<OrderState> orderStates;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_mine_list, container, false);
        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        Bundle args = getArguments();
        type = args.getInt("position");

        orderManagerAdapter = new MineOrderManagerAdapter(getActivity());
        lv_content.setAdapter(orderManagerAdapter);
        orderManagerAdapter.setOnButton(this);

        //initView();
        initData();

        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (companyOrders != null) {
                    companyOrders.clear();
                    PAGE_NUM = 1;
                    orderManagerAdapter = new MineOrderManagerAdapter(getActivity());
                    lv_content.setAdapter(orderManagerAdapter);
                }
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

        return view;
    }

    private void initView() {
        companyOrders = new ArrayList<>();
        for (int i=0;i<8;i++){
            CompanyOrder companyOrder = new CompanyOrder();
            companyOrder.setAddress(i+"");
            companyOrder.setId(i + "");
            companyOrders.add(companyOrder);
        }
        orderManagerAdapter.setMessages(companyOrders);
        orderManagerAdapter.setType(type);

        orderDao = new OrderDao(this);
    }

    private void initData() {
        odao = new MineStoreDao(this);
        odao.companyOrderList(Arad.preferences.getString("companyId"),type+"", Constant.PAGE_SIZE+"",PAGE_NUM+"");

        orderDao = new OrderDao(this);
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
        if (requestCode==6){
            orderItems = orderDao.getListOrderItem();
            orderStates = orderDao.getListOrderState();
            if (orderItems!=null&&orderStates!=null){
                orderManagerAdapter.setOrderItems(orderItems);
                orderManagerAdapter.setOrderStates(orderStates);
            }
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
            case R.id.bt_send:
                String position = (String)id;
                orderManagerAdapter.setGone(0);
                orderDao.requesOrderDetail(position);
                break;
        }
    }


}
