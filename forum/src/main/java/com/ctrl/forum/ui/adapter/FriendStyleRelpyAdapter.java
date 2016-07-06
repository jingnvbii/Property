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
import com.ctrl.forum.entity.PostReply;
import com.ctrl.forum.face.FaceConversionUtil;
import com.ctrl.forum.manager.MediaManager;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城首页列表 adapter
 * Created by jason on 2016/4/8.
 */
public class FriendStyleRelpyAdapter extends BaseAdapter{
    private Context mcontext;
    private List<PostReply> list;
    private View viewanim;

    public FriendStyleRelpyAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<PostReply> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_friend_style_reply,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        final PostReply merchant=list.get(position);
        holder.tv_reply_name.setText(merchant.getMemberName()+":");
        if(merchant.getContentType().equals("0")) {
            SpannableString spannableString2 = FaceConversionUtil.getInstace().getExpressionString(mcontext, merchant.getReplyContent());
            holder.tv_reply_content.setText(spannableString2);
        }

        if (merchant.getContentType().equals("1")) {
           holder.tv_reply_content.setText(" [图片]");
        }
        if (merchant.getContentType().equals("2")) {
           holder.rl_reply_voice.setVisibility(View.VISIBLE);
         /*  holder.rl_reply_voice.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   playSound(v,merchant.getSoundUrl());
               }
           });*/
        }
        return convertView;
    }



    static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
        @InjectView(R.id.tv_reply_name)//文字
                TextView  tv_reply_name;
        @InjectView(R.id.tv_reply_content)//文字
                TextView  tv_reply_content;
        @InjectView(R.id.rl_reply_voice)
        RelativeLayout rl_reply_voice;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    /*
* 播放语音
* */
    public void playSound(View view, String soundUrl) {
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
        MediaManager.playSoundFromUrl(mcontext, soundUrl,
                new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        viewanim.setBackgroundResource(R.drawable.voice_default);
                    }
                });

    }

}
