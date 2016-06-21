package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.RimServiceCompany;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 小区_周边服务
 * Created by Administrator on 2016/4/22.
 */
public class PlotRimServeAdapter extends BaseAdapter {
    private Context context;
    private List<RimServiceCompany> rimServiceCompanies;
    private View.OnClickListener onButton;

    public void setOnButton(View.OnClickListener onButton) {this.onButton = onButton;}
    public PlotRimServeAdapter(Context context) {this.context = context;
        rimServiceCompanies = new ArrayList<>();
    }

    public void setRimServiceCompanies(List<RimServiceCompany> rimServiceCompanies) {
        this.rimServiceCompanies= rimServiceCompanies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {return rimServiceCompanies!=null?rimServiceCompanies.size():0;}//blacklists.size();

    @Override
    public Object getItem(int position) {return rimServiceCompanies.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_rim_shop,parent,false);
            holder=new ViewHolder(convertView);
            holder.iv_phone.setOnClickListener(onButton);
            holder.iv_address.setVisibility(View.GONE);
            holder.tv_number.setTextColor(context.getResources().getColor(R.color.rim_gray));
            holder.tv_take_phone.setTextColor(context.getResources().getColor(R.color.red_bg));
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (rimServiceCompanies!=null && rimServiceCompanies.get(position)!=null){
            holder.tv_name.setText(rimServiceCompanies.get(position).getName());
            holder.tv_address.setText(rimServiceCompanies.get(position).getAddress());
            holder.tv_number.setText(rimServiceCompanies.get(position).getTelephone());
            holder.tv_take_phone.setText("已拨打"+rimServiceCompanies.get(position).getCallTimes()+"次");
        }

         holder.iv_phone.setTag(position);
        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_address)
        TextView tv_address;
        @InjectView(R.id.tv_number)
        TextView tv_number;
        @InjectView(R.id.tv_take_phone)
        TextView tv_take_phone;
        @InjectView(R.id.iv_phone)
        ImageView iv_phone;
        @InjectView(R.id.iv_address)
        ImageView iv_address;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
