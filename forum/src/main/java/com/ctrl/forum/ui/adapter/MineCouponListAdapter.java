package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Coupon;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 现金劵
 * Created by Administrator on 2016/5/3.
 */
public class MineCouponListAdapter extends BaseAdapter{
    private List<Coupon> messages;
    private Context context;
    private View view;

    public MineCouponListAdapter(Context context, List<Coupon> messages, View view) {
        this.context = context;
        this.messages = messages;
        this.view = view;
    }

    public MineCouponListAdapter(Context context, List<Coupon> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {return messages.size()!= 0?messages.size():0;}

    @Override
    public Object getItem(int position) {return messages.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_huipast,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.iv_icon)
        ImageView iv_icon;
        @InjectView(R.id.tv_money)
        TextView tv_money;
        @InjectView(R.id.tv_man)
        TextView tv_man;
        @InjectView(R.id.tv_time)
        TextView tv_time;
        @InjectView(R.id.tv_zhi)
        TextView tv_zhi;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
