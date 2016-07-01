package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.OrderItem;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 订单状态商品详情 adapter
 * Created by jason on 2016/4/8.
 */
public class StoreOrdeProductDetailAdapter extends BaseAdapter{
    private Context mcontext;
    private List<OrderItem> list;

    public StoreOrdeProductDetailAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<OrderItem> orderState) {
        this.list = orderState;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_store_order_detail,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        OrderItem orderItem=list.get(position);
        holder.tv_yellow.setVisibility(View.GONE);
        holder.tv_item_order_detail_name.setText(orderItem.getProductname());
        holder.tv_item_order_detail_num.setText("x  "+orderItem.getNums());
        holder.tv_item_order_detail_price.setText(orderItem.getAmount()+"元");
        return convertView;
    }

    static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
       @InjectView(R.id.tv_item_order_detail_name)//名称
               TextView  tv_item_order_detail_name;
        @InjectView(R.id.tv_item_order_detail_num)//数量
                TextView  tv_item_order_detail_num;
        @InjectView(R.id.tv_item_order_detail_price)//价格
                TextView  tv_item_order_detail_price;
        @InjectView(R.id.tv_yellow)//价格
                TextView  tv_yellow;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
