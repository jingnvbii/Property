package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.ShopReply;
import com.ctrl.forum.utils.TimeUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 店铺详情 评论详情  adapter
 * Created by jason on 2016/4/8.
 */
public class StoreShopDetailPingLunListViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<ShopReply>kindList;

    public StoreShopDetailPingLunListViewAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<ShopReply> list) {
        this.kindList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return kindList==null?0:kindList.size();
    }

    @Override
    public Object getItem(int position) {
        return kindList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_store_shop_detail_ping_lun_listview,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        ShopReply mShopReply=kindList.get(position);
        holder.tv_ping_name.setText(mShopReply.getReportName());
        holder.id_pingjia_content.setText(mShopReply.getContent());
        holder.ratingBar_pinglun.setNumStars(Integer.parseInt(mShopReply.getLevel()));
        holder.tv_pinlun_time.setText(TimeUtils.date(Long.parseLong(mShopReply.getCreateTime())));
        if(mShopReply.getKfReplay()!=null) {
            holder.rl_kf.setVisibility(View.VISIBLE);
            holder.tv_huifu_content.setText(mShopReply.getKfReplay());
        }else {
            holder.rl_kf.setVisibility(View.GONE);
        }
        holder.ratingBar_pinglun.setNumStars(5);
        if(mShopReply.getLevel()!=null){
            holder.ratingBar_pinglun.setRating(Float.parseFloat(mShopReply.getLevel())/2);
        }

        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.tv_ping_name)//评价人姓名
                TextView tv_ping_name;
        @InjectView(R.id.id_pingjia_content)//评价内容
                TextView id_pingjia_content;
        @InjectView(R.id.tv_pinlun_time)//评价时间
                TextView tv_pinlun_time;
        @InjectView(R.id.tv_huifu_content)//回复内容
                TextView tv_huifu_content;
        @InjectView(R.id.tv_huifu_time)//回复时间
                TextView tv_huifu_time;
        @InjectView(R.id.ratingBar_pinglun)//评价等级
                RatingBar ratingBar_pinglun;
        @InjectView(R.id.rl_kf)//客服回复布局
        RelativeLayout rl_kf;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
