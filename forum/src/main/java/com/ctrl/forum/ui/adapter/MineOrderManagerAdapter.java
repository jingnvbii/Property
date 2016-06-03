package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.CompanyOrder;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 订单管理
 * Created by Administrator on 2016/5/3.
 */
public class MineOrderManagerAdapter extends BaseAdapter {
    private List<CompanyOrder> companyOrders;
    private Context context;

    public MineOrderManagerAdapter(Context context) {this.context = context;}

    public void setMessages(List<CompanyOrder> companyOrders) {
        this.companyOrders = companyOrders;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {return companyOrders!=null?companyOrders.size():0;}

    @Override
    public Object getItem(int position) {return companyOrders.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_order,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (companyOrders!=null){
            CompanyOrder companyOrder = companyOrders.get(position);
            holder.tv_number.setText(companyOrder.getOrderNum());
            holder.tv_order_time.setText(companyOrder.getCreateTime());//时间需改格式,评分条未传值,评价内容随评分条改变
            holder.tv_order_name.setText(companyOrder.getReceiverName());
            holder.tv_order_phone.setText(companyOrder.getReceiverMobile());
            holder.tv_order_address.setText(companyOrder.getAddress());

            String state = companyOrder.getState();
            switch (state){
                case "4":
                    holder.tv_state.setText("发货");
                    holder.bt_send.setVisibility(View.GONE);
                    holder.rl_bt.setVisibility(View.GONE);
                    break;
                case "6":
                    holder.tv_state.setText("已签收");
                    holder.bt_send.setVisibility(View.GONE);
                    holder.rl_bt.setVisibility(View.GONE);
                    break;
            }

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
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
