package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.MallKind;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城一级分类  adapter
 * Created by jason on 2016/4/8.
 */
public class StoreGridViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<MallKind>kindList;

    public StoreGridViewAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<MallKind> list) {
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.gridview_store_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        MallKind kind=kindList.get(position);
        holder.tv_grid_item.setText(kind.getName());
        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;
        @InjectView(R.id.tv_grid_item)//文字
                TextView  tv_grid_item;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
