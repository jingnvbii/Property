package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Blacklist;

import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/4/22.
 */
public class MineBlacklistAdapter extends BaseAdapter {
    private Context context;
    private List<Blacklist> blacklists;
    private View.OnClickListener onButton;

    public void setOnButton(View.OnClickListener onButton) {this.onButton = onButton;}
    public MineBlacklistAdapter(List<Blacklist> blacklists, Context context) {this.blacklists = blacklists;this.context = context;}
    @Override
    public int getCount() {return blacklists.size()==0?0:blacklists.size();}

    @Override
    public Object getItem(int position) {return blacklists.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_blacklist,parent,false);
            holder=new ViewHolder(convertView);
            holder.bt_clear.setOnClickListener(onButton);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
         holder.bt_clear.setTag(position);
        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.iv_icon)
        ImageView iv_icon;
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.iv_grade)
        ImageView iv_grade;
        @InjectView(R.id.bt_clear)
        Button bt_clear;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
