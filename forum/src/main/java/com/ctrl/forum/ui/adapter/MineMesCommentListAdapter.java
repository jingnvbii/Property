package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.ReplyForMe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MineMesCommentListAdapter extends BaseAdapter {

    private List<ReplyForMe> replyForMes;
    private Context context;

    public MineMesCommentListAdapter(Context context, List<ReplyForMe> messages) {this.context = context;this.replyForMes = messages;}

    @Override
    public int getCount() {return replyForMes.size()!= 0?replyForMes.size():0;}

    @Override
    public Object getItem(int position) {return replyForMes.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.iconfont_head)
        ImageView iconfont_head;
        @InjectView(R.id.grad)
        ImageView grad;
        @InjectView(R.id.comment_vip_name)
        TextView comment_vip_name;
        @InjectView(R.id.tv_day)
        TextView tv_day;
        @InjectView(R.id.tv_vip_name)
        TextView tv_vip_name;
        @InjectView(R.id.tv_comment)
        TextView tv_comment;
        @InjectView(R.id.tv_reply_name)
        TextView tv_reply_name;
        @InjectView(R.id.tv_reply_content)
        TextView tv_reply_content;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
