package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.ObtainMyReply;
import com.ctrl.forum.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 个人中心--我的评论
 * Created by Administrator on 2016/5/3.
 */
public class MineCommentListAdapter extends BaseAdapter {
    private List<ObtainMyReply> obtainMyReplies;
    private Context context;

    public MineCommentListAdapter(Context context) {
        this.context = context;
        obtainMyReplies = new ArrayList<>();
    }

    public void setObtainMyReplies(List<ObtainMyReply> obtainMyReplies) {
        this.obtainMyReplies.addAll(obtainMyReplies);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return obtainMyReplies.size();
      }

    @Override
    public Object getItem(int position) {return obtainMyReplies.get(position);}

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

        if (obtainMyReplies!=null && obtainMyReplies.get(position)!=null){
            String contentType = obtainMyReplies.get(position).getContentType();
            switch (contentType){
                case "0"://文字或表情
                    holder.tv_comment.setText(obtainMyReplies.get(position).getReplyContent());
                    break;
                case "1"://图片
                    holder.tv_comment.setText("[图片]");
                    break;
                case "2": //语音
                    holder.tv_comment.setText("[语音]");
                    break;
                default:
                    break;
            }
            holder.tv_name.setText(obtainMyReplies.get(position).getReceiverName());
            if (obtainMyReplies.get(position).getReplyType().equals("0")){
                holder.tv_append.setText("的帖子");
            }else{
                holder.tv_append.setText("的评论");
            }

            Long time = obtainMyReplies.get(position).getCreateTime();
            String time1 = DateUtil.getStringByFormat(time, "yyyy-MM-dd HH:mm:ss");
            holder.tv_year.setText(time1);
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.tv_comment)
        TextView tv_comment;
        @InjectView(R.id.tv_year)
        TextView tv_year;
        @InjectView(R.id.tv_name)
        TextView tv_name;
        @InjectView(R.id.tv_append)
        TextView tv_append;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
