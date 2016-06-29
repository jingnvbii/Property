package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.ListViewForScrollView;
import com.ctrl.forum.dao.OrderDao;
import com.ctrl.forum.entity.OrderItem;
import com.ctrl.forum.entity.OrderState;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;
import com.ctrl.forum.ui.adapter.StoreOrdeProductDetailAdapter;
import com.ctrl.forum.utils.TimeUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城订单详情 fragment
 * Created by Administrator on 2015/11/30.
 */
public class StoreOrderDetailFragment extends ToolBarFragment {
    @InjectView(R.id.lv_order_detail_fragment)
    ListViewForScrollView lv_order_detail_fragment;
    @InjectView(R.id.tv_shop_name)//店铺名称
    TextView tv_shop_name;
    @InjectView(R.id.tv_peisong_money)//配送费
    TextView tv_peisong_money;
    @InjectView(R.id.tv_youhuiquan_money)//优惠券金额
    TextView tv_youhuiquan_money;
    @InjectView(R.id.tv_total_money)//总价
    TextView tv_total_money;
    @InjectView(R.id.tv_order_num)//订单号
    TextView tv_order_num;
    @InjectView(R.id.tv_order_time)//下单时间
    TextView tv_order_time;
    @InjectView(R.id.tv_maijia_name)//联系人
    TextView tv_maijia_name;
    @InjectView(R.id.tv_order_address)//送货地址
    TextView tv_order_address;
    @InjectView(R.id.tv_payment)//支付方式
    TextView tv_payment;
    @InjectView(R.id.tv_once_more)//再来一单
    TextView tv_once_more;
    private OrderDao odao;
    private List<OrderItem> listOrderItem;
    private OrderState orderState;
    private StoreOrdeProductDetailAdapter mStoreOrdeProductDetailAdapter;


    public static StoreOrderDetailFragment newInstance(List<OrderItem>list,OrderState orderState) {
        StoreOrderDetailFragment fragment = new StoreOrderDetailFragment();
        fragment.listOrderItem=list;
        fragment.orderState=orderState;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_order_detail, container, false);
        ButterKnife.inject(this, view);
        if(odao==null) {
            odao = new OrderDao(this);
        }
        mStoreOrdeProductDetailAdapter=new StoreOrdeProductDetailAdapter(getActivity());
        initData();
        tv_once_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                intent.putExtra("id",orderState.getCompanyId());
                startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
            }
        });
        return view;
    }

    private void initData() {
        mStoreOrdeProductDetailAdapter.setList(listOrderItem);
        tv_shop_name.setText(orderState.getCompanyname());
        tv_youhuiquan_money.setText("-￥"+orderState.getCouponMoney()+"");
        tv_total_money.setText("￥"+orderState.getTotalCost()+"");
        tv_order_num.setText("订 单 号："+orderState.getOrderNum());
        tv_order_time.setText("下单时间："+TimeUtils.timeFormat(orderState.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        tv_maijia_name.setText("联 系 人："+orderState.getReceiverName());
        tv_order_address.setText("送货地址："+orderState.getAddress());
        tv_payment.setText("支付方式：在线支付");
        if(orderState.getPayMode()!=null) {
            if (orderState.getPayMode().equals("1")) {
                tv_payment.setText("支付方式：在线支付");
            }
            if (orderState.getPayMode().equals("2")) {
                tv_payment.setText("支付方式：货到付款");
            }
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_order_detail_fragment.setAdapter(mStoreOrdeProductDetailAdapter);
    }
}
