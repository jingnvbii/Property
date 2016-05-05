package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Order;

import java.util.List;

/**
 * 我的订单
 * Created by Administrator on 2016/4/25.
 */
public class MineOrderListAdapter extends BaseAdapter{
    private List<Order> orders;
    private Context context;
    private int type;

    public MineOrderListAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    public void setType(int type) {this.type = type;}
    @Override
    public int getCount() {
        return orders.size()==0?0:orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            switch (type){
                case 0://已付款
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_payment,parent,false);
                    holder=new ViewHolder();
                    holder.ctrl = (TextView) convertView.findViewById(R.id.ctrl);
                    holder.tv_total = (TextView) convertView.findViewById(R.id.tv_total);
                    holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                    holder.time = (TextView) convertView.findViewById(R.id.textView24);
                    holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
                    convertView.setTag(holder);
                    break;
                case 1://未付款
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_nopayment,parent,false);
                    holder=new ViewHolder();
                    holder.ctrl = (TextView) convertView.findViewById(R.id.ctrl);
                    holder.tv_total = (TextView) convertView.findViewById(R.id.tv_total);
                    holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                    holder.time = (TextView) convertView.findViewById(R.id.textView24);
                    holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
                    convertView.setTag(holder);
                    break;
            }
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
          holder.tv_total.setText(orders.get(position).getTotal());
        return convertView;
    }

    class ViewHolder{
      TextView ctrl;
        TextView tv_total; //总价
          TextView time;//时间
         TextView tv_content;//由谁提供
        ImageView iv_head;
    }
}
