package com.ctrl.android.yinfeng.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.entity.Report;
import com.ctrl.android.yinfeng.utils.TimeUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/10/26.
 */
public class ReportAdapter extends BaseAdapter{
    private List<Report> listMap;
    private Activity mActivity;
    private String state;
    private Report report;

    public ReportAdapter(Activity mActivity, String state){
        this.mActivity=mActivity;
        this.state=state;
    }
    public void setList(List<Report> list) {
        this.listMap = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listMap==null?0:listMap.size();
    }

    @Override
    public Object getItem(int position) {
        return listMap.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mActivity).inflate(R.layout.report_fragment_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        report=listMap.get(position);
        holder.tv_content.setText("上报类型："+report.getKindName());
        if(null!=report.getCreateTime()) {
            holder.tv_time.setText("上报时间：" + TimeUtil.date(Long.parseLong(report.getCreateTime())));
        }
        if(state.equals("0")){
            holder.tv_status.setText("待处理");
            //holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_red);
        }
        if(state.equals("1")){
            holder.tv_status.setText("处理中");
           // holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_yellow);
        }
        if(state.equals("2")){
            holder.tv_status.setText("已结束");
          //  holder.tv_my_repairs_status.setBackgroundResource(R.drawable.tv_shape_gray);

        }

        if(state.equals("")){

            if(report.getHandleStatus().equals("0")){
                holder.tv_status.setText("待处理");
            }else if(report.getHandleStatus().equals("1")){
                holder.tv_status.setText("处理中");
            }else if(report.getHandleStatus().equals("2")){
                holder.tv_status.setText("已结束");
            }else {
                holder.tv_status.setText("");

            }


        }


        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.tv_content)//报修类型
        TextView tv_content;
        @InjectView(R.id.tv_time)//报修时间
        TextView  tv_time;
        @InjectView(R.id.tv_status)//金额
        TextView tv_status;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }


    }
}
