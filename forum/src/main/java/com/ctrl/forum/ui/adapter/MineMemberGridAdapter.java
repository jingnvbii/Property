package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Member;

import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的-griview
 * Created by Administrator on 2016/4/12.
 */
public class MineMemberGridAdapter extends BaseAdapter {
    private List<Member> data;
    private Context context;
    public MineMemberGridAdapter(List<Member> data, Context context){
        this.context = context;
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.gridview_invitation_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.tv_grid_item.setText(data.get(position).getName());
        holder.iv_grid_item.setImageResource(R.mipmap.ic_launcher);
        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.iv_grid_item)
                ImageView iv_grid_item;
        @InjectView(R.id.tv_grid_item)
                TextView tv_grid_item;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
