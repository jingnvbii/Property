package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 消息
 * Created by Administrator on 2016/4/21.
 */
public class MineMessageListAdapter extends BaseAdapter{
    private List<Message> messages;
    private Context context;

    public MineMessageListAdapter(Context context) {this.context = context;this.messages = new ArrayList<>();}

    public void setMessages(List<Message> messages) {
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {return messages.size()!= 0?messages.size():0;}

    @Override
    public Object getItem(int position) {return messages.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_content,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        holder.tv_content.setText(messages.get(position).getContent());

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.iv_icon)
        ImageView iv_icon;
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_data)
        TextView tv_data;
        @InjectView(R.id.tv_content)
        TextView tv_content;
        ViewHolder(View view) {ButterKnife.inject(this, view);}
    }
}
