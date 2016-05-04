package com.ctrl.android.yinfeng.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.android.yinfeng.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 巡更巡查adapter
 * Created by Administrator on 2015/11/10.
 */
public class PatrolLineAdapter extends BaseAdapter {
    private Activity mActivity;
    //private List<String> mData;
    List<Map<String,String>> mData = new ArrayList<>();
    private Map<String, String> map;


    public PatrolLineAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setData(List<Map<String,String>> mData) {
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.j_adapter_partrol_line, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        map=mData.get(position);
        holder.tv_point_1.setText(map.get("point"));
        holder.tv_describe.setText(map.get("content"));
        holder.iv_patrol_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        holder.et_patrol_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        return convertView;
    }




    static class ViewHolder {

        @InjectView(R.id.tv_point_1)
        TextView tv_point_1;
        @InjectView(R.id.tv_describe)
        TextView tv_describe;
        @InjectView(R.id.iv_patrol_camera)//照相
        ImageView iv_patrol_camera;
        @InjectView(R.id.et_patrol_content)//备注
        EditText et_patrol_content;
        @InjectView(R.id.tv_patrol_end)//完成
        TextView tv_patrol_end;
        @InjectView(R.id.iv_patrol_disc_1)//圆圈
        ImageView iv_patrol_disc_1;
        @InjectView(R.id.ll_patrol_3)//照相布局
        LinearLayout  ll_patrol_3;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
