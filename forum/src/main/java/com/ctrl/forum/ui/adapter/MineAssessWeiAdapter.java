package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Assess;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 待评价订单
 * Created by Administrator on 2016/5/3.
 */
public class MineAssessWeiAdapter extends BaseAdapter {
    private List<Assess> messages;
    private Context context;

    public MineAssessWeiAdapter(Context context) {this.context = context;}

    public void setMessages(List<Assess> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {return messages!=null?messages.size():0;}

    @Override
    public Object getItem(int position) {return messages.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_assess_wei,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (messages!=null){
            holder.tv_name.setText(messages.get(position).getCompanyname());
            holder.tv_total.setText(messages.get(position).getTotalCost());
            holder.tv_data.setText(messages.get(position).getCreateTime());
            //时间需改格式,评分条未传值,评价内容随评分条改变
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.iv_head)
        ImageView iv_head;
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_total)
        TextView tv_total;
        @InjectView(R.id.tv_data)
        TextView tv_data;
        @InjectView(R.id.assess_go)
        Button assess_go;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
