package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Coupon;

import java.util.List;

/**
 * 现金劵
 * Created by Administrator on 2016/5/3.
 */
public class MineCouponXianListAdapter extends BaseAdapter{
    private List<Coupon> messages;
    private Context context;
    private int resources;

    public MineCouponXianListAdapter(Context context, int resources) {
        this.context = context;
        this.resources = resources;
    }

    public void setMessages(List<Coupon> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessages(List<Coupon> messages){
        messages.addAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages==null?0:messages.size();
    }

    @Override
    public Object getItem(int position) {return messages.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resources,null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.tv_limit_goods = (TextView) convertView.findViewById(R.id.tv_limit_goods);
            holder.tv_use_man = (TextView) convertView.findViewById(R.id.tv_use_man);
            holder.time_limit = (TextView) convertView.findViewById(R.id.time_limit);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (messages!=null){
             holder.tv_name.setText(messages.get(position).getName());
            holder.tv_money.setText(messages.get(position).getAmount());
        }

        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
        TextView time_limit;
        TextView tv_money;
        TextView tv_limit_goods;
        TextView tv_use_man;
    }
}
