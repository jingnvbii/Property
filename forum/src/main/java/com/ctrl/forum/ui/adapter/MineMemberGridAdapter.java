package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Plugin;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的-griview
 * Created by Administrator on 2016/4/12.
 */
public class MineMemberGridAdapter extends BaseAdapter {
    private List<Plugin> data;
    private Context context;
    private View.OnClickListener onImage;
    private int type;

    public MineMemberGridAdapter( Context context){
        this.context = context;
    }

    public void setData(List<Plugin> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void setOnImage(View.OnClickListener onImage) {
        this.onImage = onImage;
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
            holder.iv_grid_item.setOnClickListener(onImage);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        holder.iv_grid_item.setTag(type*10+position);

        if (data!=null && data.get(position)!=null){
            if (data.get(position).getIconUrl()!=null && !data.get(position).getIconUrl().equals("")) {
                Arad.imageLoader.load(data.get(position).getIconUrl()).placeholder(context.getResources().
                        getDrawable(R.mipmap.image_default)).into(holder.iv_grid_item);
            }
            holder.tv_grid_item.setText(data.get(position).getName());
        }

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
