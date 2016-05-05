package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.RimShop;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/4/27.
 */
public class RimShopListAdapter extends BaseAdapter {
    private List<RimShop> rimShops;
    private Context context;

    public RimShopListAdapter(Context context, List<RimShop> rimShops) {this.context = context;this.rimShops = rimShops;}

    @Override
    public int getCount() {return rimShops.size()!=0?rimShops.size():0;}

    @Override
    public Object getItem(int position) {return rimShops.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_rim_shop,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.tv_name.setText(rimShops.get(position).getName());
//        holder.tv_address.setText(rimShops.get(position).getAddress());
//        holder.tv_number.setText(rimShops.get(position).getPhoneNum());
//        holder.tv_total.setText(rimShops.get(position).getTotal());
        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_address)
        TextView tv_address;
        @InjectView(R.id.tv_number)
        TextView tv_number;
        @InjectView(R.id.tv_total)
        TextView tv_total;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
