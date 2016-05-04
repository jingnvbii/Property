package com.ctrl.android.yinfeng.ui.adapter;

import android.app.Activity;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.Constant;
import com.ctrl.android.yinfeng.entity.Visit;
import com.ctrl.android.yinfeng.utils.D;
import com.ctrl.android.yinfeng.utils.S;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的到访adapter
 * Created by Administrator on 2015/10/26.
 */
public class MyVisitAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<Visit> list;

    public MyVisitAdapter(Activity activity) {
        mActivity = activity;

    }
    public void setList(List<Visit> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity)
                    .inflate(R.layout.j_my_visit_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Visit visit = list.get(position);

        holder.visit_num.setText("到访编号: " + S.getStr(visit.getVisitNum()));
        holder.visit_time.setText(D.getDateStrFromStamp(Constant.YYYY_MM_DD, visit.getCreateTime() == null ? "0000000" : visit.getCreateTime()));
        holder.visit_name.setText(visit.getBuilding()+"-"+visit.getUnit()+"-"+visit.getRoom());
               if (visit.getVisitState() .equals("0")) {
                   holder.visit_status.setText("未到访");
                 //  holder.visit_status.setBackgroundResource(R.drawable.visit_none);
               } else if (visit.getVisitState() .equals("1")) {
                   holder.visit_status.setText("已到访");
                //   holder.visit_status.setBackgroundResource(R.drawable.visit_refused);
               }

        TextPaint tp = holder.visit_name.getPaint();
        tp.setFakeBoldText(true);

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.visit_num)//到访编号
        TextView visit_num;
        @InjectView(R.id.visit_time)//到访时间
        TextView visit_time;
        @InjectView(R.id.visit_name)//到访人
        TextView visit_name;
        @InjectView(R.id.visit_status)//访问状态
        TextView visit_status;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
