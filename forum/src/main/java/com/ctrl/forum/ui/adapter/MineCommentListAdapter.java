package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.ReplyForMe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的--我的评论
 * Created by Administrator on 2016/4/22.
 */
public class MineCommentListAdapter extends BaseAdapter {
    private List<ReplyForMe> comments;
    private Context context;

    public MineCommentListAdapter(List<ReplyForMe> comments, Context context) {this.comments = comments;this.context = context;}

    public void setComments(List<ReplyForMe> comments) {
        this.comments = comments;
       // notifyDataSetChanged();
    }

    @Override
    public int getCount() {return comments.size()==0?0:comments.size();}
    @Override
    public Object getItem(int position) {return comments.get(position);}
    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_comment,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.tv_comment)
        TextView tv_comment;
        @InjectView(R.id.tv_year)
        TextView tv_year;
        @InjectView(R.id.tv_time)
        TextView tv_time;
        @InjectView(R.id.tv_name)
        TextView tv_name;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
