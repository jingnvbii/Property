package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.dao.OrderDao;
import com.ctrl.forum.entity.OrderState;
import com.ctrl.forum.entity.OrderStatus2;
import com.ctrl.forum.ui.activity.mine.OrderPingjiaActivity;
import com.ctrl.forum.ui.activity.store.StoreShopListVerticalStyleActivity;
import com.ctrl.forum.ui.adapter.StoreOrderDetailStatusAdapter;
import com.ctrl.forum.utils.TimeUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城订单状态 fragment
 * Created by Administrator on 2015/11/30.
 */
public class StoreOrderStatusFragment extends ToolBarFragment implements View.OnClickListener{
    @InjectView(R.id.lv_order_status_fragment)
    PullToRefreshListView lv_order_status_fragment;
    @InjectView(R.id.tv_dianping_go)
    TextView tv_dianping_go;
    @InjectView(R.id.tv_once_more)
    TextView tv_once_more;
    @InjectView(R.id.iv_hongbao)
    ImageView iv_hongbao;
    private OrderDao odao;
    private StoreOrderDetailStatusAdapter mStoreOrderDetailStatusAdapter;
    private OrderState orderState;


    public static StoreOrderStatusFragment newInstance(OrderState orderState) {
        StoreOrderStatusFragment fragment = new StoreOrderStatusFragment();
        fragment.orderState=orderState;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_order_status, container, false);
        ButterKnife.inject(this, view);
        if(odao==null) {
            odao = new OrderDao(this);
        }
        tv_dianping_go.setOnClickListener(this);
        tv_once_more.setOnClickListener(this);
        mStoreOrderDetailStatusAdapter=new StoreOrderDetailStatusAdapter(getActivity());
        initData();
        lv_order_status_fragment.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        lv_order_status_fragment.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        odao.requesOrderDetail(getActivity().getIntent().getStringExtra("id"));
                    }
                },500);
            }
        });
        return view;
    }

    private void initData() {
        List<OrderStatus2>list=new ArrayList<>();
        if(orderState.getState().equals("1")){
            iv_hongbao.setVisibility(View.GONE);
            OrderStatus2 orderStatus=new OrderStatus2();
            orderStatus.setStatus("订单被用户取消");
            orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_red));
            if(orderState.getSellCancelTime()!=null)
                orderStatus.setTime(TimeUtils.timeFormat(Long.parseLong(orderState.getSellCancelTime()), "yyyy-MM-dd HH:mm:ss"));
            list.add(orderStatus);
        }
        if(orderState.getState().equals("2")){
            iv_hongbao.setVisibility(View.GONE);
            OrderStatus2 orderStatus=new OrderStatus2();
            orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_red));
            orderStatus.setStatus("订单被系统取消");
            list.add(orderStatus);
        }
        if(orderState.getState().equals("3")){
            iv_hongbao.setVisibility(View.GONE);
            OrderStatus2 orderStatus=new OrderStatus2();
            orderStatus.setStatus("订单提交成功，等待付款");
            orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_red));
            if(String.valueOf(orderState.getCreateTime())!=null)
                orderStatus.setTime(TimeUtils.timeFormat(orderState.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            list.add(orderStatus);
        }
        if(orderState.getState().equals("4")){
            iv_hongbao.setVisibility(View.VISIBLE);
            OrderStatus2 orderStatus=new OrderStatus2();
            OrderStatus2 orderStatus2=new OrderStatus2();
            orderStatus.setStatus("订单提交成功，等待商家接单");
            orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_gray));
            if(String.valueOf(orderState.getCreateTime())!=null)
                orderStatus.setTime(TimeUtils.timeFormat(orderState.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            orderStatus2.setStatus("商家已接单");
            orderStatus2.setDrawable(getResources().getDrawable(R.mipmap.yijiedan_red));
            if(orderState.getAcceptTime()!=null)
                orderStatus2.setTime(TimeUtils.timeFormat(Long.parseLong(orderState.getAcceptTime()), "yyyy-MM-dd HH:mm:ss"));
            list.add(orderStatus);
            list.add(orderStatus2);
        }
        if(orderState.getState().equals("5")){
            iv_hongbao.setVisibility(View.VISIBLE);
            OrderStatus2 orderStatus=new OrderStatus2();
            OrderStatus2 orderStatus2=new OrderStatus2();
            OrderStatus2 orderStatus3=new OrderStatus2();
            orderStatus.setStatus("订单提交成功，等待商家接单");
            orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_gray));
            if(String.valueOf(orderState.getCreateTime())!=null)
                orderStatus.setTime(TimeUtils.timeFormat(orderState.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            orderStatus2.setStatus("商家已接单");
            orderStatus2.setDrawable(getResources().getDrawable(R.mipmap.yijiedan_gray));
            if(orderState.getAcceptTime()!=null)
                orderStatus2.setTime(TimeUtils.timeFormat(Long.parseLong(orderState.getAcceptTime()), "yyyy-MM-dd HH:mm:ss"));
            orderStatus3.setStatus("配送中");
            orderStatus3.setDrawable(getResources().getDrawable(R.mipmap.yijiedan_red));
            orderStatus3.setTime(orderState.getCompanyPhone());
            list.add(orderStatus);
            list.add(orderStatus2);
            list.add(orderStatus3);
        }
        if(orderState.getState().equals("6")){
            iv_hongbao.setVisibility(View.VISIBLE);
            OrderStatus2 orderStatus=new OrderStatus2();
            OrderStatus2 orderStatus2=new OrderStatus2();
            OrderStatus2 orderStatus3=new OrderStatus2();
            OrderStatus2 orderStatus4=new OrderStatus2();
            orderStatus.setStatus("订单提交成功，等待商家接单");
            orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_gray));
            if(String.valueOf(orderState.getCreateTime())!=null)
                orderStatus.setTime(TimeUtils.timeFormat(orderState.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            orderStatus2.setStatus("商家已接单");
            orderStatus2.setDrawable(getResources().getDrawable(R.mipmap.yijiedan_gray));
            if(orderState.getAcceptTime()!=null)
                orderStatus2.setTime(TimeUtils.timeFormat(Long.parseLong(orderState.getAcceptTime()), "yyyy-MM-dd HH:mm:ss"));
            orderStatus3.setStatus("配送中");
            orderStatus3.setDrawable(getResources().getDrawable(R.mipmap.yijiedan_gray));
            orderStatus3.setTime(orderState.getCompanyPhone());
            orderStatus4.setStatus("已确认收货");
            if(orderState.getSignTime()!=null)
                orderStatus4.setTime(TimeUtils.timeFormat(Long.parseLong(orderState.getSignTime()), "yyyy-MM-dd HH:mm:ss"));
            orderStatus4.setDrawable(getResources().getDrawable(R.mipmap.yishouhuo_red));

            list.add(orderStatus);
            list.add(orderStatus2);
            list.add(orderStatus3);
            list.add(orderStatus4);
        }
        mStoreOrderDetailStatusAdapter.setList(list);
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_order_status_fragment.onRefreshComplete();
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_order_status_fragment.onRefreshComplete();
        if(requestCode==66){
          //  listOrderItem=odao.getListOrderItem();
            orderState=odao.getOrderState();
            List<OrderStatus2>list=new ArrayList<>();
            if(orderState.getState().equals("1")){
                iv_hongbao.setVisibility(View.GONE);
                OrderStatus2 orderStatus=new OrderStatus2();
                orderStatus.setStatus("订单被用户取消");
                orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_red));
                if(orderState.getSellCancelTime()!=null)
                orderStatus.setTime(TimeUtils.timeFormat(Long.parseLong(orderState.getSellCancelTime()), "yyyy-MM-dd HH:mm:ss"));
                list.add(orderStatus);
            }
            if(orderState.getState().equals("2")){
                iv_hongbao.setVisibility(View.GONE);
                OrderStatus2 orderStatus=new OrderStatus2();
                orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_red));
                orderStatus.setStatus("订单被系统取消");
                list.add(orderStatus);
            }
            if(orderState.getState().equals("3")){
                iv_hongbao.setVisibility(View.GONE);
                OrderStatus2 orderStatus=new OrderStatus2();
                orderStatus.setStatus("订单提交成功，等待付款");
                orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_red));
                if(String.valueOf(orderState.getCreateTime())!=null)
                    orderStatus.setTime(TimeUtils.timeFormat(orderState.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                list.add(orderStatus);
            }
            if(orderState.getState().equals("4")){
                iv_hongbao.setVisibility(View.VISIBLE);
                OrderStatus2 orderStatus=new OrderStatus2();
                OrderStatus2 orderStatus2=new OrderStatus2();
                orderStatus.setStatus("订单提交成功，等待商家接单");
                orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_gray));
                if(String.valueOf(orderState.getCreateTime())!=null)
                    orderStatus.setTime(TimeUtils.timeFormat(orderState.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                orderStatus2.setStatus("商家已接单");
                orderStatus2.setDrawable(getResources().getDrawable(R.mipmap.yijiedan_red));
                if(orderState.getAcceptTime()!=null)
                orderStatus2.setTime(TimeUtils.timeFormat(Long.parseLong(orderState.getAcceptTime()), "yyyy-MM-dd HH:mm:ss"));
                list.add(orderStatus);
                list.add(orderStatus2);
            }
            if(orderState.getState().equals("5")){
                iv_hongbao.setVisibility(View.VISIBLE);
                OrderStatus2 orderStatus=new OrderStatus2();
                OrderStatus2 orderStatus2=new OrderStatus2();
                OrderStatus2 orderStatus3=new OrderStatus2();
                orderStatus.setStatus("订单提交成功，等待商家接单");
                orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_gray));
                if(String.valueOf(orderState.getCreateTime())!=null)
                    orderStatus.setTime(TimeUtils.timeFormat(orderState.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                orderStatus2.setStatus("商家已接单");
                orderStatus2.setDrawable(getResources().getDrawable(R.mipmap.yijiedan_gray));
                if(orderState.getAcceptTime()!=null)
                orderStatus2.setTime(TimeUtils.timeFormat(Long.parseLong(orderState.getAcceptTime()), "yyyy-MM-dd HH:mm:ss"));
                orderStatus3.setStatus("配送中");
                orderStatus3.setDrawable(getResources().getDrawable(R.mipmap.yijiedan_red));
                orderStatus3.setTime(orderState.getCompanyPhone());
                list.add(orderStatus);
                list.add(orderStatus2);
                list.add(orderStatus3);
            }
            if(orderState.getState().equals("6")){
                iv_hongbao.setVisibility(View.VISIBLE);
                OrderStatus2 orderStatus=new OrderStatus2();
                OrderStatus2 orderStatus2=new OrderStatus2();
                OrderStatus2 orderStatus3=new OrderStatus2();
                OrderStatus2 orderStatus4=new OrderStatus2();
                orderStatus.setStatus("订单提交成功，等待商家接单");
                orderStatus.setDrawable(getResources().getDrawable(R.mipmap.weifukuan_gray));
                if(String.valueOf(orderState.getCreateTime())!=null)
                    orderStatus.setTime(TimeUtils.timeFormat(orderState.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                orderStatus2.setStatus("商家已接单");
                orderStatus2.setDrawable(getResources().getDrawable(R.mipmap.yijiedan_gray));
                if(orderState.getAcceptTime()!=null)
                orderStatus2.setTime(TimeUtils.timeFormat(Long.parseLong(orderState.getAcceptTime()), "yyyy-MM-dd HH:mm:ss"));
                orderStatus3.setStatus("配送中");
                orderStatus3.setDrawable(getResources().getDrawable(R.mipmap.yijiedan_gray));
                orderStatus3.setTime(orderState.getCompanyPhone());
                orderStatus4.setStatus("已确认收货");
                if(orderState.getSignTime()!=null)
                orderStatus4.setTime(TimeUtils.timeFormat(Long.parseLong(orderState.getSignTime()), "yyyy-MM-dd HH:mm:ss"));
                orderStatus4.setDrawable(getResources().getDrawable(R.mipmap.yishouhuo_red));

                list.add(orderStatus);
                list.add(orderStatus2);
                list.add(orderStatus3);
                list.add(orderStatus4);
            }
            mStoreOrderDetailStatusAdapter.setList(list);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_order_status_fragment.setAdapter(mStoreOrderDetailStatusAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.tv_dianping_go:
                intent=new Intent(getActivity(), OrderPingjiaActivity.class);
                intent.putExtra("id",orderState.getId());
                intent.putExtra("companyId",orderState.getCompanyId());
                startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
                break;
            case R.id.tv_once_more:
                intent=new Intent(getActivity(), StoreShopListVerticalStyleActivity.class);
                intent.putExtra("id",orderState.getCompanyId());
                startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
                break;
        }
    }
}
