package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.RimServeCategory;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周边服务-listview
 * Created by Administrator on 2016/5/16.
 */
public class RimListViewAdapter extends BaseAdapter{
    private List<RimServeCategory> data;
    private Context context;
    private RimGridViewAdapter rimGridViewAdapter;
    private View.OnClickListener onSearch;
    private View.OnClickListener onCollect;
    private int type;

    public RimListViewAdapter(Context context){
        this.context = context;
    }

    public void setOnCollect(View.OnClickListener onCollect) {
        this.onCollect = onCollect;
    }

    public void setOnSearch(View.OnClickListener onSearch) {
        this.onSearch = onSearch;
    }

    public void setRimGridViewAdapter(RimGridViewAdapter rimGridViewAdapter) {
        this.rimGridViewAdapter = rimGridViewAdapter;
    }

    public void setData(List<RimServeCategory> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                type = 0;
                break;
            case 1:
                type = 1;
                break;
            default:
                type = 2;
                break;
        }
        return type;
    }

    @Override
    public int getCount() {
        return data==null?2:data.size()+2;
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
        ViewHolder holder = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;
        if(convertView==null) {
            switch (type) {
                case 0:
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_rim_list_search,parent,false);
                    holder=new ViewHolder(convertView);
                    holder.rl_search.setOnClickListener(onSearch);
                    convertView.setTag(holder);
                    break;
                case 1:
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_rim_list_collect,parent,false);
                    holder2=new ViewHolder2(convertView);
                    holder2.rl_collect.setOnClickListener(onCollect);
                    convertView.setTag(holder2);
                    break;
                case 2:
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_rim_list,parent,false);
                    holder3=new ViewHolder3(convertView);
                    convertView.setTag(holder3);
                    break;
            }
        }else {
            switch (type) {
                case 0:
                    holder=(ViewHolder)convertView.getTag();
                    break;
                case 1:
                    holder2=(ViewHolder2)convertView.getTag();
                    break;
                case 2:
                    holder3=(ViewHolder3)convertView.getTag();
                    break;
            }
        }

        switch (type){
            case 0:
                holder.rl_search.setTag(position);
                break;
            case 1:
                holder2.rl_collect.setTag(position);
                break;
            case 2:
                if (data!=null){
                Arad.imageLoader.load(data.get(position-2).getCategory_icon()).into(holder3.iv_pic);
                holder3.tv_title.setText(data.get(position - 2).getName());
                holder3.gv_hot.setAdapter(rimGridViewAdapter);
                rimGridViewAdapter.setData(data.get(position - 2).getAroundservicecategorylist());}
                break;
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.rl_search)
        RelativeLayout rl_search;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    class ViewHolder2{
        @InjectView(R.id.rl_collect)
        RelativeLayout rl_collect;
        ViewHolder2(View view) {
            ButterKnife.inject(this, view);
        }
    }
    class ViewHolder3{
        @InjectView(R.id.iv_pic)
        ImageView iv_pic;
        @InjectView(R.id.tv_title)
        TextView tv_title;
        @InjectView(R.id.gv_hot)
        GridView gv_hot;
        ViewHolder3(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
