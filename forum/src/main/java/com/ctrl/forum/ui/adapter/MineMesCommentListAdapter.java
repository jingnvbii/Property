package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.SetMemberLevel;
import com.ctrl.forum.entity.ReplyForMe;
import com.ctrl.forum.utils.DateUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 消息通知--收到的评论
 * Created by Administrator on 2016/5/3.
 */
public class MineMesCommentListAdapter extends BaseAdapter {

    private List<ReplyForMe> replyForMes;
    private Context context;
    String myContentType = "";
    String contentType= "";

    public MineMesCommentListAdapter(Context context) {this.context = context;}

    public void setReplyForMes(List<ReplyForMe> replyForMes) {
        this.replyForMes = replyForMes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {return replyForMes!=null?replyForMes.size():0;}

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

        if (replyForMes!=null && replyForMes.get(position)!=null){
            Arad.imageLoader.load(replyForMes.get(position).getImgUrl()).placeholder(context.getResources().getDrawable(R.mipmap.iconfont_head))
                    .into(holder.iconfont_head);
            SetMemberLevel.setLevelImage(context, holder.grad, replyForMes.get(position).getMemberLevel());
            holder.tv_day.setText(DateUtil.getStringByFormat(replyForMes.get(position).getCreateTime(), "yyyy-MM-dd"));
            holder.comment_vip_name.setText(replyForMes.get(position).getMemberName());

            String replyType = replyForMes.get(position).getReplyType();
            if (replyType.equals("0")) { //对帖子的评论
                contentType = replyForMes.get(position).getContentType();
                setType(contentType,holder.tv_comment,position,"0","t");
                holder.ll_me.setVisibility(View.GONE);
                holder.textView43.setVisibility(View.GONE);
                holder.tv_vip_name.setVisibility(View.GONE);
            }else{ //对评论的回复
                contentType = replyForMes.get(position).getContentType();
                myContentType = replyForMes.get(position).getMyContentType();
                setType(myContentType,holder.tv_reply_content,position,"1","p");
                setType(contentType,holder.tv_comment,position,"0","p");
                holder.textView43.setVisibility(View.VISIBLE);
                holder.tv_vip_name.setVisibility(View.VISIBLE);
                holder.tv_vip_name.setText(replyForMes.get(position).getMemberName()+":");
                holder.tv_reply_name.setVisibility(View.VISIBLE);
                holder.tv_reply_content.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    public void setType(String type,TextView view,int position,String item,String tp){
        if (type!=null && !type.equals("")) {
            switch (type) {
                case "0":
                    if (item.equals("1")){
                        view.setText(replyForMes.get(position).getMyReplyContent());
                    }else{
                        view.setText(replyForMes.get(position).getReplyContent());
                    }
                    break;
                case "1":
                    if (tp.equals("t")){
                        view.setText("评论:"+"[图片]");
                    }else {
                        view.setText("[图片]");
                    }
                    break;
                case "2":
                    if (tp.equals("t")){
                        view.setText("评论:"+"[语音]");
                    }else {
                        view.setText("[语音]");
                    }
                    break;
                default:
                    break;
            }
        }
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
        @InjectView(R.id.textView43)
        TextView textView43;
        @InjectView(R.id.tv_reply_content)
        TextView tv_reply_content;
        @InjectView(R.id.ll_me)
        LinearLayout ll_me;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
