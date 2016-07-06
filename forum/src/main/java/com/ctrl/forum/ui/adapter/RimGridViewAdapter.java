package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.RimServeCategorySecond;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.ui.activity.rim.ItemRimActivity;

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

    public RimGridViewAdapter(Context context){
        this.context = context;
    }

    public void setData(List<RimServeCategorySecond> data) {
        this.data = data;
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
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        final String title = data.get(position).getName();
        final String id = data.get(position).getId();
        holder.bt_rim.setText(data.get(position).getName());

        if (data.get(position).getId() != null) {
            holder.bt_rim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Arad.preferences.getString("memberId") != null && !Arad.preferences.getString("memberId").equals("")){
                        Intent intent = new Intent(context, ItemRimActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("title", title);
                        context.startActivity(intent);
                    }else{
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                }
            });
        }

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
