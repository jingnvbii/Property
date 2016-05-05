package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.AssessYi;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 已评价订单
 * Created by Administrator on 2016/5/3.
 */
public class MineAssessYiAdapter extends BaseAdapter {
    private List<AssessYi> messages;
    private Context context;

    public MineAssessYiAdapter(Context context) {this.context = context;}

    public void setMessages(List<AssessYi> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessages(List<AssessYi> messages) {
        this.messages.addAll(messages);
        notifyDataSetChanged();
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_assess_yi,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.tv_name)
        TextView tv_name;   //商店名
        @InjectView(R.id.tv_time)
        TextView tv_time;   //时间
        @InjectView(R.id.rb)
        RatingBar rb;    //评分条
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
