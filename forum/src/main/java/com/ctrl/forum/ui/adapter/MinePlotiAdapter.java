package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Communitys;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的小区
 * Created by Administrator on 2016/5/3.
 */
public class MinePlotiAdapter extends BaseAdapter {
    private List<Communitys> communities;
    private Context context;
    public View.OnClickListener onButton;

    public MinePlotiAdapter(Context context) {
        this.context = context;
        communities = new ArrayList<>();
    }

    public void setOnButton(View.OnClickListener onButton) {
        this.onButton = onButton;
    }

    public void setMessages(List<Communitys> communities) {
        this.communities.addAll(communities);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {return communities!=null?communities.size():0;}

    @Override
    public Object getItem(int position) {return communities.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_plot_activity,parent,false);
            holder=new ViewHolder(convertView);
            holder.tv_join.setOnClickListener(onButton);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        holder.tv_join.setTag(position);

        if (communities!=null && communities.get(position)!=null){
            holder.tv_name.setText(communities.get(position).getCommunityName());
            holder.tv_f_num.setText(communities.get(position).getPostCount());
            holder.tv_good_num.setText(communities.get(position).getCompanyCount());
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_f_num)
        TextView tv_f_num;
        @InjectView(R.id.tv_good_num)
        TextView tv_good_num;
        @InjectView(R.id.tv_join)
        TextView tv_join;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
