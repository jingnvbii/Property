package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Merchant;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城搜索  adapter
 * Created by jason on 2016/4/8.
 */
public class StoreSearchGridViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<Merchant>kindList;

    public StoreSearchGridViewAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<Merchant> list) {
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_store_search,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        Merchant kind=kindList.get(position);
        holder.tv_store_name.setText(kind.getName());
        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.tv_store_name)//文字
                TextView  tv_store_name;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
