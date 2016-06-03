package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.RimServeCategorySecond;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周边服务--gridView
 * Created by Administrator on 2016/4/26.
 */
public class RimGridViewAdapter extends BaseAdapter{
    private List<RimServeCategorySecond> data;
    private Context context;
    private View.OnClickListener onButton;
    private String title;

    public RimGridViewAdapter(Context context){
        this.context = context;
    }

    public void setData(List<RimServeCategorySecond> data) {
        this.data = data;
    }

    public void setOnButton(View.OnClickListener onButton) {
        this.onButton = onButton;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_rim,parent,false);
            holder=new ViewHolder(convertView);
            holder.bt_rim.setOnClickListener(onButton);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.bt_rim.setText(data.get(position).getName());
        holder.bt_rim.setTag(data.get(position).getId());
        title = holder.bt_rim.getText().toString();
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
