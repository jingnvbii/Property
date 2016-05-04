package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.ThirdKind;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子三Kind级分类  adapter
 * Created by jason on 2016/4/8.
 */
public class InvitationPullDownGridViewAdapter extends BaseAdapter{
    private Context mContext;
    private List<ThirdKind>kindList;

    public InvitationPullDownGridViewAdapter(Context context) {
               this.mContext=context;
    }

    public void setList(List<ThirdKind> list) {
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.gridview_invitation_pull_down_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        ThirdKind kind=kindList.get(position);
        holder.item_tv.setText(kind.getKindName());
        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.item_tv)//文字
                TextView  item_tv;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
