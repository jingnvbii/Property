package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.CompanyOrder;
import com.ctrl.forum.utils.DateUtil;

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
    private View.OnClickListener onButton;

    public MineOrderManagerAdapter(Context context) {this.context = context;}

    public void setCompanyOrders(List<CompanyOrder> companyOrders) {
        this.companyOrders = companyOrders;
        notifyDataSetChanged();
    }

    public void setOnButton(View.OnClickListener onButton) {
        this.onButton = onButton;
    }

    @Override
    public int getCount() {
        return companyOrders!=null?companyOrders.size():0;
    }

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

            holder.bt_send.setOnClickListener(onButton);
            holder.bt_send.setTag(position);

            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (companyOrders!=null){
            final CompanyOrder companyOrder = companyOrders.get(position);
            holder.tv_number.setText(companyOrder.getOrderNum());
            holder.tv_order_time.setText(DateUtil.getStringByFormat(companyOrder.getCreateTime(), "yyyy-MM-dd   hh:mm:ss"));//时间需改格式,评分条未传值,评价内容随评分条改变
            holder.tv_order_name.setText(companyOrder.getReceiverName());
            holder.tv_order_phone.setText(companyOrder.getReceiverMobile());
            holder.tv_order_address.setText(companyOrder.getAddress());

            String type = companyOrders.get(position).getState();
            switch (type){
                case "1": //1-订单被用户取消
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
                    holder.bt_send.setVisibility(View.VISIBLE);
                    holder.bt_send.setText("发货");
                    holder.bt_send.setOnClickListener(onButton);
                    holder.bt_send.setTag(position);
                    break;
                case "5":
                    holder.tv_state.setText("待签收");
                    holder.bt_send.setVisibility(View.GONE);
                    break;
                case "6":
                    String evaluationState = companyOrders.get(position).getEvaluationState();
                    if (evaluationState.equals("0")){
                        holder.tv_state.setText("未评价");
                    }else{
                        holder.tv_state.setText("已评价");
                    }
                    holder.bt_send.setVisibility(View.GONE);
                    break;
                default:
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

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
