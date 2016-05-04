package com.ctrl.android.yinfeng.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.entity.RepairKind;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/10/26.
 */
public class MyJobGridViewAdapter extends BaseAdapter{
    private Activity mActivity;
    private List<RepairKind> repairKindList;
    private RepairKind repairKind;

    public MyJobGridViewAdapter(Activity mActivity){
        this.mActivity=mActivity;
    }
    public void setList(List<RepairKind>repairKindList) {
        this.repairKindList=repairKindList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return repairKindList==null?0:repairKindList.size();
    }

    @Override
    public Object getItem(int position) {
        return repairKindList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mActivity).inflate(R.layout.job_gridview_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        repairKind=repairKindList.get(position);
        holder.tv_type.setText(repairKind.getKindName());
        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.tv_type)//报修类型
        TextView tv_type;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }


    }
}
