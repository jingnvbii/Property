package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.ctrl.forum.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城地址搜索展示列表 adapter
 * Created by jason on 2016/4/8.
 */
public class StoreSearchAddressListAdapter extends BaseAdapter{
    private Context mcontext;
    private List<PoiInfo> list;

    public StoreSearchAddressListAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<PoiInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_store_search_address_list,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        PoiInfo merchant=list.get(position);
        holder.tv_1.setText(merchant.name);
        holder.tv_2.setText(merchant.address);
        return convertView;
    }

    static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
        @InjectView(R.id.tv_1)//地址名称
                TextView  tv_1;
        @InjectView(R.id.tv_2)//地址
                TextView  tv_2;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
