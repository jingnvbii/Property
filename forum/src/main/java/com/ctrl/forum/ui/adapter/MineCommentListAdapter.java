package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.ObtainMyReply;
import com.ctrl.forum.face.FaceConversionUtil;
import com.ctrl.forum.manager.MediaManager;
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
    private View viewanim;

    public MineCommentListAdapter(Context context) {
        this.context = context;
        obtainMyReplies = new ArrayList<>();
    }

    public void setObtainMyReplies(List<ObtainMyReply> obtainMyReplies) {
        this.obtainMyReplies = obtainMyReplies;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_comment,parent,false);
            holder=new ViewHolder(convertView);
            holder.rl_reply_voice.setVisibility(View.GONE);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (obtainMyReplies!=null && obtainMyReplies.get(position)!=null){
            String contentType = obtainMyReplies.get(position).getContentType();
            switch (contentType){
                case "0"://文字或表情
                    holder.rl_reply_voice.setVisibility(View.GONE);
                    holder.tv_comment.setVisibility(View.VISIBLE);
                    //holder.tv_comment.setText(obtainMyReplies.get(position).getReplyContent());
                    SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context,
                            obtainMyReplies.get(position).getReplyContent());
                    holder.tv_comment.setText(spannableString);
                    break;
                case "1"://图片
                    holder.rl_reply_voice.setVisibility(View.GONE);
                    holder.tv_comment.setVisibility(View.VISIBLE);
                    holder.tv_comment.setText("[图片]");
                    break;
                case "2": //语音
                    holder.tv_comment.setVisibility(View.GONE);
                    holder.rl_reply_voice.setVisibility(View.VISIBLE);
                    holder.rl_reply_voice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = "2";
                            playSound(v, id, obtainMyReplies.get(position).getSoundUrl());
                        }
                    });
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
        @InjectView(R.id.rl_reply_voice)
        RelativeLayout rl_reply_voice;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    /**
     * 播放语音
     * @param view
     * @param soundUrl
     */
    public void playSound(View view,String id,String soundUrl) {
        // TODO Auto-generated method stub

        // 播放动画
        if (viewanim != null) {//让第二个播放的时候第一个停止播放
            viewanim.setBackgroundResource(R.drawable.voice_default);
            viewanim = null;
        }
        viewanim = view.findViewById(R.id.id_recorder_anim);
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
