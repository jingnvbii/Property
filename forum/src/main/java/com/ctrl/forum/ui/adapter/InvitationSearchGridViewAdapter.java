package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.HotSearch;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子热门搜索 adapter
 * Created by jason on 2016/4/8.
 */
public class InvitationSearchGridViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<HotSearch> list;

    public InvitationSearchGridViewAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<HotSearch> list) {
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_invitation_search_grid_view,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        HotSearch merchant=list.get(position);
        holder.tv_hot_search_item.setText(merchant.getKeyword());
        return convertView;
    }

    static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
        @InjectView(R.id.tv_hot_search_item)//文字
                TextView  tv_hot_search_item;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
