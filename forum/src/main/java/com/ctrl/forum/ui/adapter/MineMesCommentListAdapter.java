package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.SetMemberLevel;
import com.ctrl.forum.entity.ReplyForMe;
import com.ctrl.forum.face.FaceConversionUtil;
import com.ctrl.forum.manager.MediaManager;
import com.ctrl.forum.utils.TimeUtils;

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
    private View viewanim;

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
            holder.rl_reply_voice1.setVisibility(View.GONE);
            holder.rl_reply_voice2.setVisibility(View.GONE);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (replyForMes!=null && replyForMes.get(position)!=null){
            Arad.imageLoader.load(replyForMes.get(position).getImgUrl()).placeholder(context.getResources().getDrawable(R.mipmap.my_gray))
                    .into(holder.iconfont_head);
            SetMemberLevel.setLevelImage(context, holder.grad, replyForMes.get(position).getMemberLevel());
            holder.tv_day.setText(TimeUtils.dateTime1(replyForMes.get(position).getCreateTime()+""));
            holder.comment_vip_name.setText(replyForMes.get(position).getMemberName());

            String replyType = replyForMes.get(position).getReplyType();
            String isReplied = replyForMes.get(position).getIsReplied();

            if (replyType.equals("0")) { //对帖子的评论
                holder.tv_reply_name.setText("回复我的帖子：");
            }else{ //对评论的回复
                holder.tv_reply_name.setText("回复我的评论：");
            }
            setType(replyForMes.get(position).getContentType(),holder.tv_reply_content,position,"1",holder);//ReplyContent

            if (isReplied.equals("0")){
                holder.ll_replay.setVisibility(View.GONE);
            }else{
                holder.ll_replay.setVisibility(View.VISIBLE);
                holder.tv_vip_name.setText(replyForMes.get(position).getMemberName() + ":");
                setType(replyForMes.get(position).getMyContentType(),holder.tv_comment,position,"2",holder); //myReplyContent
            }
        }

        return convertView;
    }

    public void setType(String type, final TextView view, final int position,String item,ViewHolder holder){
        if (type!=null && !type.equals("")) {
            switch (type) {
                case "0":
                    holder.rl_reply_voice1.setVisibility(View.GONE);
                    holder.rl_reply_voice2.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                    if (item.equals("1")){
                        //view.setText(replyForMes.get(position).getReplyContent());
                        SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context,
                                replyForMes.get(position).getReplyContent());
                        view.setText(spannableString);
                    }else{
                        //view.setText(replyForMes.get(position).getMyReplyContent());
                        SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context,
                                replyForMes.get(position).getMyReplyContent());
                        view.setText(spannableString);
                    }
                    break;
                case "1":
                    holder.rl_reply_voice1.setVisibility(View.GONE);
                    holder.rl_reply_voice2.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                    view.setText("[图片]");
                    break;
                case "2":
                    //view.setText("[语音]");
                    view.setVisibility(View.GONE);
                    if (item.equals("1")){
                        //view.setText(replyForMes.get(position).getReplyContent());
                        holder.rl_reply_voice2.setVisibility(View.VISIBLE);
                        holder.rl_reply_voice2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = "2";
                                playSound(v,id,replyForMes.get(position).getSoundUrl());
                            }
                        });
                    }else{
                        holder.rl_reply_voice1.setVisibility(View.VISIBLE);
                        holder.rl_reply_voice1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = "1";
                                playSound(v,id, replyForMes.get(position).getMySoundUrl());
                            }
                        });
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
        TextView grad;
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
        @InjectView(R.id.ll_replay)
        LinearLayout ll_replay;
        @InjectView(R.id.rl_reply_voice1)
        RelativeLayout rl_reply_voice1;
        @InjectView(R.id.rl_reply_voice2)
        RelativeLayout rl_reply_voice2;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    /*
* 播放语音
* */
    public void playSound(View view,String id,String soundUrl) {
        // TODO Auto-generated method stub

        // 播放动画
        if (viewanim != null) {//让第二个播放的时候第一个停止播放
            viewanim.setBackgroundResource(R.drawable.voice_default);
            viewanim = null;
        }
        if (id.equals("1")){
            viewanim = view.findViewById(R.id.id_recorder_anim1);
        }else{
            viewanim = view.findViewById(R.id.id_recorder_anim2);
        }
        viewanim.setBackgroundResource(R.drawable.play);
        AnimationDrawable drawable = (AnimationDrawable) viewanim
                .getBackground();
        drawable.start();

        // 播放音频
        MediaManager.playSoundFromUrl(context, soundUrl,
                new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        viewanim.setBackgroundResource(R.drawable.voice_default);
                    }
                });

    }
}
