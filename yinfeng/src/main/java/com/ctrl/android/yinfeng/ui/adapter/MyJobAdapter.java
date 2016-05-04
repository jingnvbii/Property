package com.ctrl.android.yinfeng.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.customview.NoScrollGridView;
import com.ctrl.android.yinfeng.entity.Job;
import com.ctrl.android.yinfeng.entity.RepairKind;
import com.ctrl.android.yinfeng.ui.job.MyJobActivity;
import com.ctrl.android.yinfeng.utils.S;
import com.ctrl.android.yinfeng.utils.TimeUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/10/26.
 */
public class MyJobAdapter extends BaseAdapter{
    private List<Job> listMap;
    private Activity mActivity;
    private String state;
    private Job repair;
    private List<RepairKind> repairKindList;
    private List<String> kindListStr;
    private RepairKind repairKind;

    public MyJobAdapter(Activity mActivity, String state){
        this.mActivity=mActivity;
        this.state=state;
    }
    public void setList(List<Job> list) {
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
            convertView= LayoutInflater.from(mActivity).inflate(R.layout.j_fragment_my_repairs_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        repair=listMap.get(position);
        holder.tv_content.setText(repair.getContent());

        /*
        * 屏蔽gridview焦点
        * */
        holder.listview_item_gridview.setClickable(false);
        holder.listview_item_gridview.setPressed(false);
        holder.listview_item_gridview.setEnabled(false);



        if(null!=repair.getCreateTime()) {
            holder.tv_time.setText(TimeUtil.date(Long.parseLong(repair.getCreateTime())) + "     " +
                    repair.getCommunityName()+" "+repair.getBuilding()+"-"+repair.getUnit()+"-"+repair.getRoom());
        }
        if(holder.listview_item_gridview!=null) {
            MyJobGridViewAdapter adapter = new MyJobGridViewAdapter(mActivity);
            adapter.setList(repair.getrepairKindList());
            holder.listview_item_gridview.setAdapter(adapter);
        }
        if(repair.getOrderType().equals("0")&&state.equals("0")){
            holder.tv_accept.setVisibility(View.VISIBLE);
            holder.tv_accept.setText("抢单");
            holder.tv_accept.setBackgroundResource(R.mipmap.da);

            if(Arad.preferences.getString("grade").equals("0")||
                    (Arad.preferences.getString("grade").equals("1")&&Arad.preferences.getString("jobType").equals("1"))
                    ||(Arad.preferences.getString("grade").equals("1")&&Arad.preferences.getString("jobType").equals("2"))
                    ){

                holder.tv_accept.setText("指派");
            }

        }else if(repair.getOrderType().equals("1")&&state.equals("0")){
            if(Arad.preferences.getString("grade").equals("2")) {
                holder.tv_accept.setVisibility(View.VISIBLE);
                holder.tv_accept.setText("接单");
                holder.tv_accept.setBackgroundResource(R.mipmap.da);
            }else {
                holder.tv_accept.setVisibility(View.GONE);
            }
        }else if(repair.getIsFeedback().equals("0")&&state.equals("1")){
            holder.tv_accept.setVisibility(View.VISIBLE);
            holder.tv_accept.setText("未反馈");
            holder.tv_accept.setTextSize(15);
            holder.tv_accept.setBackgroundResource(R.mipmap.da);
            holder.tv_accept.setTextColor(Color.RED);
        }else if(repair.getIsFeedback().equals("1")&&state.equals("1")){
            if(repair.getPayState().equals("0"))
            holder.tv_accept.setVisibility(View.VISIBLE);
            holder.tv_accept.setText("未付款");
            holder.tv_accept.setTextSize(15);
            holder.tv_accept.setBackgroundResource(R.mipmap.da);
            holder.tv_accept.setTextColor(Color.RED);

            if(Arad.preferences.getString("grade").equals("2")) {
                holder.tv_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = mActivity.getLayoutInflater();
                        final View layout = inflater.inflate(R.layout.dialog,
                                (ViewGroup) v.findViewById(R.id.dialog));
                        new AlertDialog.Builder(mActivity).setTitle("请输入金额").setView(layout)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EditText et_report = (EditText) layout.findViewById(R.id.et_job);
                                        MyJobActivity activity = (MyJobActivity) mActivity;
                                        if (!S.isNull(et_report.getText().toString())) {
                                            activity.setMoney(et_report.getText().toString(), repair.getId());
                                        } else {
                                            new AlertDialog.Builder(mActivity).setMessage("金额为空")
                                                    .setPositiveButton("返回", null).show();
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });
            }
        }else if(state.equals("3")){
            holder.tv_money.setVisibility(View.VISIBLE);
            holder.tv_money.setText(repair.getMaintenanceCosts());
            holder.tv_yuan.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


    static class ViewHolder{
        @InjectView(R.id.tv_content)//报修内容
        TextView tv_content;
        @InjectView(R.id.tv_time)//报修时间
        TextView  tv_time;
        @InjectView(R.id.tv_money)//金额
        TextView tv_money;
        @InjectView(R.id.view)//线
        View view;
        @InjectView(R.id.tv_yuan)//元

        TextView tv_yuan;

        @InjectView(R.id.tv_accept)//抢单
        TextView  tv_accept;
        @InjectView(R.id.listview_item_gridview)//报修类型gridView
        NoScrollGridView listview_item_gridview;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }


    }
}
