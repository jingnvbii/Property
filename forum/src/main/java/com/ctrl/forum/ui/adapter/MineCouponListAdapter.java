package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ctrl.forum.entity.Coupon;
import com.ctrl.forum.entity.Redenvelope;

import java.util.List;

/**
 * 优惠劵
 * Created by Administrator on 2016/5/3.
 */
public class MineCouponListAdapter extends BaseAdapter{
    private List<Redenvelope> redenvelopes;
    private Context context;
    private int resources;

    public MineCouponListAdapter(Context context,int resources) {
        this.context = context;
        this.resources = resources;
    }

    public void setMessages(List<Redenvelope> redenvelopes) {
        this.redenvelopes = redenvelopes;
        notifyDataSetChanged();
    }

    public void addMessages(List<Coupon> messages){
        messages.addAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return redenvelopes!= null?redenvelopes.size():0;
    }

    @Override
    public Object getItem(int position) {return redenvelopes.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            //convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_huipast,null);
            //convertView = view;
            convertView = LayoutInflater.from(context).inflate(resources,null);
            holder=new ViewHolder();
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (redenvelopes!=null){
            //为控件赋值
        }

        return convertView;
    }

    class ViewHolder{
        /*@InjectView(R.id.iv_icon)
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
        }*/
    }
}
