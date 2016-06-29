package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.OrderStatus2;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 订单详情状态 adapter
 * Created by jason on 2016/4/8.
 */
public class StoreOrderDetailStatusAdapter extends BaseAdapter{
    private Context mcontext;
    private List<OrderStatus2> list;

    public StoreOrderDetailStatusAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<OrderStatus2> orderState) {
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_store_order_detail_status,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        if(position==0){
            holder.view_line_top.setVisibility(View.INVISIBLE);
        }
        if(position==list.size()-1){
            holder.view_line_bottom.setVisibility(View.INVISIBLE);
        }
        OrderStatus2 orderState=list.get(position);
        holder.tv_status_name.setText(orderState.getStatus());
        holder.tv_status_content.setText(orderState.getTime());
        holder.iv_status_img.setImageDrawable(orderState.getDrawable());
        return convertView;
    }

    static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
        @InjectView(R.id.view_line_top)//头部线
                View  view_line_top;
        @InjectView(R.id.view_line_bottom)//底部线
                View  view_line_bottom;
        @InjectView(R.id.tv_status_name)//状态名称
                TextView  tv_status_name;
        @InjectView(R.id.tv_status_content)//状态描述
                TextView  tv_status_content;
        @InjectView(R.id.iv_status_img)//图片
        ImageView iv_status_img;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
