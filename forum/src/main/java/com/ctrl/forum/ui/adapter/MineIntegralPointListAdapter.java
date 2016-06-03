package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.RedeemHistory;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 兑换商品积分记录
 * Created by Administrator on 2016/5/12.
 */
public class MineIntegralPointListAdapter extends BaseAdapter{
    private List<RedeemHistory> redeemHistories;
    private Context context;

    public MineIntegralPointListAdapter(Context context) {
        this.context = context;
    }

    public void setExchaneProducts(List<RedeemHistory> redeemHistories) {
        this.redeemHistories = redeemHistories;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return redeemHistories!=null?redeemHistories.size():0;
    }

    @Override
    public Object getItem(int position) {
        return redeemHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mine_remark_history,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (redeemHistories.size()>0){
            String type = redeemHistories.get(position).getPointType(); //类型（0：会员注册、1：商城消费、2：积分兑换...后续逐渐追加
            String time = redeemHistories.get(position).getCreateTime();

            viewHolder.tv_fen.setText(redeemHistories.get(position).getPoint());
            //String id = exchaneProducts.get(position).getID();//根据兑换的商品的id,找到该id的图片url
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.tv_content)
        TextView tv_content;
        @InjectView(R.id.tv_day)
        TextView tv_day;
        @InjectView(R.id.tv_fen)
        TextView tv_fen;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
