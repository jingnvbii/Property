package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.SetMemberLevel;
import com.ctrl.forum.entity.Blacklist;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单
 * Created by Administrator on 2016/4/22.
 */
public class MineBlacklistAdapter extends BaseAdapter {
    private Context context;
    private List<Blacklist> blacklists = new ArrayList<>();
    private View.OnClickListener onButton;

    public void setOnButton(View.OnClickListener onButton) {this.onButton = onButton;}
    public MineBlacklistAdapter( Context context) {this.context = context;}

    public void setBlacklists(List<Blacklist> blacklists) {
        this.blacklists.addAll(blacklists);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {return blacklists!=null?blacklists.size():0;}//blacklists.size();

    @Override
    public Object getItem(int position) {return blacklists.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_blacklist,parent,false);
            holder=new ViewHolder();
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.iv_grade = (TextView) convertView.findViewById(R.id.iv_grade);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.bt_clear = (Button) convertView.findViewById(R.id.bt_clear);
            holder.bt_clear.setOnClickListener(onButton);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (blacklists!=null && blacklists.get(position)!=null){
            holder.tv_name.setText(blacklists.get(position).getUsername());
            Arad.imageLoader.load(blacklists.get(position).getUserimg()).into(holder.iv_icon);
            SetMemberLevel.setLevelImage(context,holder.iv_grade,blacklists.get(position).getUserlevel());
        }

         holder.bt_clear.setTag(position);
        return convertView;
    }

    class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView iv_grade;
        Button bt_clear;
    }
}
