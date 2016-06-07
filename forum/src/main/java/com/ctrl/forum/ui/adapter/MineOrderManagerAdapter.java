package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.CompanyOrder;
import com.ctrl.forum.entity.OrderItem;
import com.ctrl.forum.entity.OrderState;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 订单管理
 * Created by Administrator on 2016/5/3.
 */
public class MineOrderManagerAdapter extends BaseAdapter {
    private List<CompanyOrder> companyOrders;
    private List<OrderItem> orderItems;
    private List<OrderState> orderStates;
    private Context context;
    private View.OnClickListener onButton;
    private int type;
    private int gone; //0位显示,1为隐藏

    public MineOrderManagerAdapter(Context context) {this.context = context;}

    public void setMessages(List<CompanyOrder> companyOrders) {
        this.companyOrders = companyOrders;
        notifyDataSetChanged();
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setOrderStates(List<OrderState> orderStates) {
        this.orderStates = orderStates;
    }

    public void setGone(int gone) {
        this.gone = gone;
        notifyDataSetChanged();
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setOnButton(View.OnClickListener onButton) {
        this.onButton = onButton;
    }

    @Override
    public int getCount() {return companyOrders!=null?companyOrders.size():0;}

    @Override
    public Object getItem(int position) {return companyOrders.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_order,parent,false);
            holder=new ViewHolder(convertView);
            holder.detail.setVisibility(View.GONE);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (companyOrders!=null){
            final CompanyOrder companyOrder = companyOrders.get(position);
            holder.tv_number.setText(companyOrder.getOrderNum());
            holder.tv_order_time.setText(companyOrder.getCreateTime());//时间需改格式,评分条未传值,评价内容随评分条改变
            holder.tv_order_name.setText(companyOrder.getReceiverName());
            holder.tv_order_phone.setText(companyOrder.getReceiverMobile());
            holder.tv_order_address.setText(companyOrder.getAddress());
            holder.bt_send.setOnClickListener(onButton);
            holder.bt_send.setTag(companyOrder.getId());
        }

        if (gone==0){
            holder.rl_bt.setVisibility(View.GONE);
            holder.detail.setVisibility(View.VISIBLE);
        }else{
            holder.rl_bt.setVisibility(View.VISIBLE);
            holder.detail.setVisibility(View.GONE);
        }

        switch (type){
            case 0:
                holder.tv_state.setText("已付款");
                holder.bt_send.setText("发货");
                holder.rl_bt.setVisibility(View.VISIBLE);

                if(companyOrders!=null && companyOrders.get(position).getState()!=null) {
                    CompanyOrder companyOrder = companyOrders.get(position);
                    //订单状态
                    String state = companyOrder.getState();
                    switch (state) {
                        case "1":
                            holder.tv_state.setText("订单被用户取消");
                            holder.bt_send.setVisibility(View.GONE);
                            break;
                        case "2":
                            holder.tv_state.setText("订单被系统取消");
                            holder.bt_send.setVisibility(View.GONE);
                            break;
                        case "3":
                            holder.tv_state.setText("未付款");
                            holder.bt_send.setVisibility(View.GONE);
                            break;
                        case "4":
                            holder.tv_state.setText("已付款");
                            holder.bt_send.setText("发货");
                            holder.bt_send.setVisibility(View.VISIBLE);
                            break;
                        case "5":
                            holder.tv_state.setText("待收货");
                            holder.bt_send.setVisibility(View.GONE);
                            break;
                        case "6":
                            holder.tv_state.setText("已评价");
                            holder.bt_send.setVisibility(View.GONE);
                            break;
                    }
                }
                break;
            case 1:
                holder.tv_state.setText("已付款");
                holder.bt_send.setText("发货");
                holder.bt_send.setVisibility(View.VISIBLE);
                holder.rl_bt.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.tv_state.setText("待收货");
                holder.bt_send.setVisibility(View.GONE);
                break;
            case 3:
                holder.tv_state.setText("已评价");
                holder.bt_send.setVisibility(View.GONE);
                break;
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.tv_number)
        TextView tv_number;
        @InjectView(R.id.tv_order_time)
        TextView tv_order_time;
        @InjectView(R.id.tv_order_name)
        TextView tv_order_name;
        @InjectView(R.id.tv_order_phone)
        TextView tv_order_phone;
        @InjectView(R.id.tv_state)
        TextView tv_state;
        @InjectView(R.id.tv_order_address)
        TextView tv_order_address;
        @InjectView(R.id.bt_send)
                Button bt_send;
        @InjectView(R.id.rl_bt)
        RelativeLayout rl_bt;
        @InjectView(R.id.detail)
        LinearLayout detail;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
