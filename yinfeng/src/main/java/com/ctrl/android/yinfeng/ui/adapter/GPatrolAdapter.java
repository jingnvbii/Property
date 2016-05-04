package com.ctrl.android.yinfeng.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.entity.PatrolRoute;
import com.ctrl.android.yinfeng.ui.fragment.GPatrolFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 巡更巡查adapter
 * Created by Administrator on 2015/11/10.
 */
public class GPatrolAdapter extends BaseAdapter {
    private Activity mActivity;
    private GPatrolFragment GPatrolFragment;
    private List<PatrolRoute> mData;

    public GPatrolAdapter(Activity activity, GPatrolFragment fragment) {
        mActivity = activity;
        this.GPatrolFragment = fragment;
    }
    public void setData(List<PatrolRoute> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.j_adapter_partrol, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PatrolRoute data = mData.get(position);
        holder.mPatrolTime.setText("任务时间："+data.getCreateTime());
        holder.mPatrolLoad.setText("巡更路线："+data.getRouteName());
        holder.mPatrolName.setText("    执行人："+data.getStaffName());
        if(data.getStatus().equals("0")){holder.mPatrolType.setText("未巡查");}
        if(data.getStatus().equals("1")){holder.mPatrolType.setText("巡查中");}
        if(data.getStatus().equals("2")){holder.mPatrolType.setText("巡查结束");}
        if(data.getStatus().equals("3")){holder.mPatrolType.setText("巡查超时");}
        if(data.getStatus().equals("4")){holder.mPatrolType.setText("未完成");}
        return convertView;
    }
    static class ViewHolder {

        @InjectView(R.id.tv_patrol_time)
        TextView mPatrolTime;
        @InjectView(R.id.tv_patrol_load)
        TextView mPatrolLoad;
        @InjectView(R.id.tv_patrol_name)
        TextView mPatrolName;
        @InjectView(R.id.tv_patrol_type)//巡更巡查状态（0：未巡查、1：巡查中、2：巡查结束 3 :巡查超时 4：未完成）
        TextView mPatrolType;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
