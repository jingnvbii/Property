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
 * 商城首页列表 adapter
 * Created by jason on 2016/4/8.
 */
public class StoreFragmentAdapter extends BaseAdapter{
    private Context mcontext;
    private List<Merchant> list;

    public StoreFragmentAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<Merchant> list) {
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_listview_store_fragment,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
       Merchant merchant=list.get(position);
        holder.tv_name.setText(merchant.getName());
        return convertView;
    }

    static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
        @InjectView(R.id.tv_name)//文字
                TextView  tv_name;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
