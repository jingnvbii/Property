package com.ctrl.android.yinfeng.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.entity.StaffListInfo;
import com.ctrl.android.yinfeng.ui.job.RepairStaffListActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/10/26.
 */
public class RepairStaffListAdapter extends BaseAdapter{
    private List<StaffListInfo> listMap;
    private Activity mActivity;
    private int mposition;

    public RepairStaffListAdapter(Activity mActivity){
        this.mActivity=mActivity;
    }
    public void setList(List<StaffListInfo> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mActivity).inflate(R.layout.repair_staff_list_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        final  StaffListInfo staffInfo = listMap.get(position);
        if(staffInfo.getCountOnRepairing()==0){
           holder.iv_repair_staff_status.setImageResource(R.drawable.img_idle);
        }else {
            holder.iv_repair_staff_status.setImageResource(R.drawable.img_busy);
        }
        holder.tv_repair_name.setText(staffInfo.getName());
        holder.tv_repair_tel.setText("现有  " + staffInfo.getCountOnRepairing() + "  个工单");
        holder.tv_repair_detail.setText(staffInfo.getSpecialty());
        Arad.imageLoader.load(staffInfo.getImgUrl()).placeholder(R.mipmap.img_repair_personal).into(holder.iv_repair_staff_pic);
        holder.tv_zhipai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (staffInfo.getCountOnRepairing() == 0) {
                    RepairStaffListActivity activity = (RepairStaffListActivity) mActivity;
                    activity.setAssignOrder(staffInfo.getId());
                } else {
                    new AlertDialog.Builder(mActivity)
                            .setMessage("您指定的维修人员目前处于忙碌中，可能需要等待，是否确认选定该人员？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RepairStaffListActivity activity = (RepairStaffListActivity) mActivity;
                                    activity.setAssignOrder(staffInfo.getId());
                                }
                            })
                            .setNegativeButton("否", null)
                            .show();
                }
            }
        });
        return convertView;
    }

    static class ViewHolder{
        @InjectView(R.id.iv_repair_staff_status)//人员状态
        ImageView iv_repair_staff_status;
        @InjectView(R.id.iv_repair_staff_pic)//头像
        ImageView iv_repair_staff_pic;
        @InjectView(R.id.tv_repair_name)//维修人员姓名
        TextView tv_repair_name;
        @InjectView(R.id.tv_repair_tel)//维修人员电话
        TextView tv_repair_tel;
        @InjectView(R.id.tv_repair_detail)//维修人员业务描述
        TextView tv_repair_detail;
        @InjectView(R.id.tv_zhipai)//我要指定
        TextView  tv_zhipai;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
