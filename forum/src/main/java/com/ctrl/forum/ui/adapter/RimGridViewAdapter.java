package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.ctrl.forum.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/4/26.
 */
public class RimGridViewAdapter extends BaseAdapter{
    private List<String> data;
    private Context context;
    public RimGridViewAdapter(List<String> data,Context context){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_rim,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.bt_rim.setText(data.get(position));
        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.bt_rim)
        Button bt_rim;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
