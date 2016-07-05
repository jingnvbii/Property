package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Address;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城首页列表 adapter
 * Created by jason on 2016/4/8.
 */
public class StoreSearchAddressAdapter extends BaseAdapter{
    private Context mcontext;
    private List<Address> list;

    public StoreSearchAddressAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<Address> list) {
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_store_search_address,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        Address merchant=list.get(position);
        holder.tv_address_name.setText(merchant.getReceiveName());
        holder.tv_address_tel.setText(merchant.getMobile());
        holder.tv_address_detail.setText(merchant.getAddressBase()+merchant.getAddressDetail());
        return convertView;
    }

    static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
        @InjectView(R.id.tv_address_name)//文字
                TextView  tv_address_name;
        @InjectView(R.id.tv_address_detail)//文字
                TextView  tv_address_detail;
        @InjectView(R.id.tv_address_tel)//文字
                TextView  tv_address_tel;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
