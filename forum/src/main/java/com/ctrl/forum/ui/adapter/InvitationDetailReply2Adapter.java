package com.ctrl.forum.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.ImageZoomActivity;
import com.ctrl.forum.customview.RoundImageView;
import com.ctrl.forum.entity.PostReply2;
import com.ctrl.forum.face.FaceConversionUtil;
import com.ctrl.forum.ui.activity.Invitation.InvitationDetailActivity;
import com.ctrl.forum.ui.activity.mine.MineDetailActivity;
import com.ctrl.forum.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *帖子评论列表详情 adapter
 * Created by jason on 2016/4/8.
 */
public class InvitationDetailReply2Adapter extends BaseAdapter {
    private Context mcontext;
    private List<PostReply2> list;
    private View viewanim;


    //  private String soundUrl;

    public InvitationDetailReply2Adapter(Context context) {
        this.mcontext = context;
    }

    public void setList(List<PostReply2> list) {
        this.list = list;
        notifyDataSetChanged();
    }
   /* public void setSoundrUrl(String soundUrl) {
        this.soundUrl = soundUrl;
        notifyDataSetChanged();
    }*/


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_invitation_detail, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PostReply2 mPostReply2 = list.get(position);
       // holder.tv_reply_time.setText(TimeUtils.timeFormat(Long.parseLong(mPostReply2.getCreateTime()),"yyyy-MM-dd"));
        holder.tv_reply_time.setText(TimeUtils.dateTime(mPostReply2.getCreateTime()));
        holder.tv_comment_detail_floor.setText(mPostReply2.getMemberFloor() + " 楼");
        if(mPostReply2.getMemberLevel()!=null){
            switch (mPostReply2.getMemberLevel()){
                case "0":
                    holder.iv_reply_level.setImageResource(R.mipmap.vip_icon);
                    break;
                case "1":
                    holder.iv_reply_level.setImageResource(R.mipmap.vip_icon1);
                    break;
                case "2":
                    holder.iv_reply_level.setImageResource(R.mipmap.vip_icon2);
                    break;
                case "3":
                    holder.iv_reply_level.setImageResource(R.mipmap.vip_icon3);
                    break;
                case "4":
                    holder.iv_reply_level.setImageResource(R.mipmap.vip_icon4);
                    break;
                case "5":
                    holder.iv_reply_level.setImageResource(R.mipmap.vip_icon5);
                    break;
                case "6":
                    holder.iv_reply_level.setImageResource(R.mipmap.vip_icon6);
                    break;
                case "7":
                    holder.iv_reply_level.setImageResource(R.mipmap.vip_icon7);
                    break;
            }
        }else {
            holder.iv_reply_level.setImageResource(R.mipmap.vip_icon);
        }
        Arad.imageLoader.load(mPostReply2.getImgUrl()).placeholder(R.mipmap.baby_large).resize(300,300)
                .centerCrop().into(holder.iv_reply_photo);
        if (mPostReply2.getReplyType().equals("0")) {//无评论
            holder.rl_pinglun.setVisibility(View.GONE);//评论布局隐藏
            if (mPostReply2.getContentType().equals("0")) {//回复文字
                holder.ll_reply_invitation_image.setVisibility(View.GONE);
                holder.rl_reply_voice.setVisibility(View.GONE);
                holder.tv_reply_content.setVisibility(View.VISIBLE);
                holder.tv_reply_content.setText(mPostReply2.getReplyContent());
                SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(mcontext, mPostReply2.getReplyContent());
                holder.tv_reply_content.setText(spannableString);
                holder.tv_reply_name.setText(mPostReply2.getMemberName());

            } else if (mPostReply2.getContentType().equals("1")) {//回复图片
                holder.ll_reply_invitation_image.setVisibility(View.VISIBLE);
                holder.tv_reply_content.setVisibility(View.GONE);
                holder.rl_reply_voice.setVisibility(View.GONE);
                holder.tv_reply_name.setText(mPostReply2.getMemberName());
                if(mPostReply2.getPostReplyImgList()!=null) {
                    if (mPostReply2.getPostReplyImgList().size() >= 1) {
                        Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(0).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image1);
                    }
                    if (mPostReply2.getPostReplyImgList().size() >= 2) {
                        Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(1).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image2);
                    }
                    if (mPostReply2.getPostReplyImgList().size() >= 3) {
                        Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(2).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image3);
                    }
                }

            } else if (mPostReply2.getContentType().equals("2")) {//回复语音
                holder.ll_reply_invitation_image.setVisibility(View.GONE);
                holder.rl_reply_voice.setVisibility(View.VISIBLE);
                holder.tv_reply_content.setVisibility(View.GONE);
                holder.tv_reply_name.setText(mPostReply2.getMemberName());
                holder.rl_reply_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InvitationDetailActivity activity = (InvitationDetailActivity) mcontext;
                        activity.playSound(v, mPostReply2.getSoundUrl());
                    }
                });
            } else {
                //
            }

        }
        if (mPostReply2.getReplyType().equals("1")) {//有评论
            holder.rl_pinglun.setVisibility(View.VISIBLE);//评论布局显示
            if (mPostReply2.getContentType().equals("0") && mPostReply2.getPreContentType().equals("0")) {//回复与评论都是文字
                holder.tv_reply_content.setVisibility(View.VISIBLE);
                holder.rl_reply_voice.setVisibility(View.GONE);
                holder.rl_pinglun.setVisibility(View.VISIBLE);
                SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(mcontext, mPostReply2.getReplyContent());
                holder.tv_reply_content.setText(spannableString);
                holder.ll_reply_invitation_image.setVisibility(View.GONE);
                holder.tv_reply_content.setText(mPostReply2.getReplyContent());
                SpannableString spannableString2 = FaceConversionUtil.getInstace().getExpressionString(mcontext, mPostReply2.getPreContent());
                holder.tv_pinglun_content.setText(spannableString2);
                holder.tv_reply_name.setText(mPostReply2.getMemberName());
                holder.tv_pinglun_title.setText("引用  " + mPostReply2.getReceiverName() + "  的回复");
                holder.tv_pinglun_floor.setText(mPostReply2.getReceiverFloor() + "楼");
            } else if (mPostReply2.getContentType().equals("0") && mPostReply2.getPreContentType().equals("1")) {//评论文字 回复 图片
                holder.tv_reply_content.setVisibility(View.VISIBLE);
                holder.rl_reply_voice.setVisibility(View.GONE);
                holder.rl_pinglun.setVisibility(View.VISIBLE);
                SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(mcontext, mPostReply2.getReplyContent());
                holder.tv_reply_content.setText(spannableString);
                holder.ll_reply_invitation_image.setVisibility(View.GONE);
                holder.tv_reply_content.setText(mPostReply2.getReplyContent());
                holder.tv_reply_name.setText(mPostReply2.getMemberName());
                holder.tv_pinglun_content.setText("[图片]");
                holder.tv_pinglun_title.setText("引用  " + mPostReply2.getReceiverName() + "  的回复");
                holder.tv_pinglun_floor.setText(mPostReply2.getReceiverFloor() + "楼");

            } else if (mPostReply2.getContentType().equals("0") && mPostReply2.getPreContentType().equals("2")) {//评论文字 回复 语音
                holder.tv_reply_content.setVisibility(View.VISIBLE);
                holder.rl_reply_voice.setVisibility(View.GONE);
                holder.rl_pinglun.setVisibility(View.VISIBLE);
                holder.tv_reply_content.setText(mPostReply2.getReplyContent());
                holder.tv_reply_name.setText(mPostReply2.getMemberName());
                SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(mcontext, mPostReply2.getReplyContent());
                holder.tv_reply_content.setText(spannableString);
                holder.tv_pinglun_content.setText("[语音]");
                holder.tv_pinglun_title.setText("引用  " + mPostReply2.getReceiverName() + "  的回复");
                holder.tv_pinglun_floor.setText(mPostReply2.getReceiverFloor() + "楼");

            } else if (mPostReply2.getContentType().equals("1") && mPostReply2.getPreContentType().equals("0")) {//评论图片  回复 文字
                holder.tv_reply_content.setVisibility(View.GONE);
                holder.rl_reply_voice.setVisibility(View.GONE);
                holder.ll_reply_invitation_image.setVisibility(View.VISIBLE);
                holder.tv_reply_name.setText(mPostReply2.getMemberName());
                holder.rl_pinglun.setVisibility(View.VISIBLE);
                if(mPostReply2.getPostReplyImgList()!=null) {
                    if (mPostReply2.getPostReplyImgList().size() >= 1) {
                        Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(0).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image1);
                    }
                    if (mPostReply2.getPostReplyImgList().size() >= 2) {
                        Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(1).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image2);
                    }
                    if (mPostReply2.getPostReplyImgList().size() >= 3) {
                        Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(2).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image3);
                    }
                }
                //TODO 加载图片

                SpannableString spannableString2 = FaceConversionUtil.getInstace().getExpressionString(mcontext, mPostReply2.getPreContent());
                holder.tv_pinglun_content.setText(spannableString2);
                holder.tv_pinglun_title.setText("引用  " + mPostReply2.getReceiverName() + "  的回复");
                holder.tv_pinglun_floor.setText(mPostReply2.getReceiverFloor() + "楼");

            } else if (mPostReply2.getContentType().equals("1") && mPostReply2.getPreContentType().equals("1")) {//评论图片  回复 图片
                holder.tv_reply_content.setVisibility(View.GONE);
                holder.rl_reply_voice.setVisibility(View.GONE);
                holder.ll_reply_invitation_image.setVisibility(View.VISIBLE);
                holder.rl_pinglun.setVisibility(View.VISIBLE);
                holder.tv_reply_name.setText(mPostReply2.getMemberName());
                if(mPostReply2.getPostReplyImgList()!=null) {
                    if (mPostReply2.getPostReplyImgList().size() >= 1) {
                        Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(0).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image1);
                    }
                    if (mPostReply2.getPostReplyImgList().size() >= 2) {
                        Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(1).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image2);
                    }
                    if (mPostReply2.getPostReplyImgList().size() >= 3) {
                        Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(2).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image3);
                    }
                }

                //TODO 加载图片

                holder.tv_pinglun_content.setText("[图片]");
                holder.tv_pinglun_title.setText("引用  " + mPostReply2.getReceiverName() + "  的回复");
                holder.tv_pinglun_floor.setText(mPostReply2.getReceiverFloor() + "楼");

            } else if (mPostReply2.getContentType().equals("1") && mPostReply2.getPreContentType().equals("2")) {//评论图片  回复 语音
                holder.tv_reply_content.setVisibility(View.GONE);
                holder.rl_reply_voice.setVisibility(View.GONE);
                holder.ll_reply_invitation_image.setVisibility(View.VISIBLE);
                holder.rl_pinglun.setVisibility(View.VISIBLE);
                holder.tv_reply_name.setText(mPostReply2.getMemberName());
                 if(mPostReply2.getPostReplyImgList()!=null) {
                     if (mPostReply2.getPostReplyImgList().size() >= 1) {
                         Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(0).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image1);
                     }
                     if (mPostReply2.getPostReplyImgList().size() >= 2) {
                         Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(1).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image2);
                     }
                     if (mPostReply2.getPostReplyImgList().size() >= 3) {
                         Arad.imageLoader.load(mPostReply2.getPostReplyImgList().get(2).getImg()).placeholder(R.mipmap.default_error).into(holder.iv_reply_invitation_image3);
                     }
                 }

                //TODO 加载图片

                holder.tv_pinglun_content.setText("[语音]");

            } else if (mPostReply2.getContentType().equals("2") && mPostReply2.getPreContentType().equals("0")) {//评论 语音  回复 文字
                holder.tv_reply_content.setVisibility(View.GONE);
                holder.ll_reply_invitation_image.setVisibility(View.GONE);
                holder.rl_reply_voice.setVisibility(View.VISIBLE);
                holder.rl_reply_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InvitationDetailActivity activity = (InvitationDetailActivity) mcontext;
                        activity.playSound(v, mPostReply2.getSoundUrl());
                    }
                });
                holder.rl_pinglun.setVisibility(View.VISIBLE);
                holder.tv_reply_name.setText(mPostReply2.getMemberName());
                SpannableString spannableString2 = FaceConversionUtil.getInstace().getExpressionString(mcontext, mPostReply2.getPreContent());
                holder.tv_pinglun_content.setText(spannableString2);
                holder.tv_pinglun_title.setText("引用  " + mPostReply2.getReceiverName() + "  的回复");
                holder.tv_pinglun_floor.setText(mPostReply2.getReceiverFloor() + "楼");
                holder.tv_pinglun_content.setText(mPostReply2.getPreContent());

            } else if (mPostReply2.getContentType().equals("2") && mPostReply2.getPreContentType().equals("1")) {//评论 语音  回复论 图片
                holder.tv_reply_content.setVisibility(View.GONE);
                holder.ll_reply_invitation_image.setVisibility(View.GONE);
                holder.rl_reply_voice.setVisibility(View.VISIBLE);
                holder.rl_pinglun.setVisibility(View.VISIBLE);
                holder.tv_pinglun_content.setText("[图片]");
                holder.tv_reply_name.setText(mPostReply2.getMemberName());
                holder.rl_reply_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InvitationDetailActivity activity = (InvitationDetailActivity) mcontext;
                        activity.playSound(v, mPostReply2.getSoundUrl());
                    }
                });
                holder.tv_pinglun_title.setText("引用  " + mPostReply2.getReceiverName() + "  的回复");
                holder.tv_pinglun_floor.setText(mPostReply2.getMemberFloor() + "楼");

            } else if (mPostReply2.getContentType().equals("2") && mPostReply2.getPreContentType().equals("2")) {//评论 语音  回复 语音
                holder.tv_reply_content.setVisibility(View.GONE);
                holder.ll_reply_invitation_image.setVisibility(View.GONE);
                holder.rl_reply_voice.setVisibility(View.VISIBLE);
                holder.rl_reply_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InvitationDetailActivity activity = (InvitationDetailActivity) mcontext;
                        activity.playSound(v, mPostReply2.getSoundUrl());
                    }
                });
                holder.rl_pinglun.setVisibility(View.VISIBLE);
                holder.tv_pinglun_content.setText("[语音]");
                holder.tv_reply_name.setText(mPostReply2.getMemberName());
                holder.tv_pinglun_title.setText("引用  " + mPostReply2.getReceiverName() + "  的回复");
                holder.tv_pinglun_floor.setText(mPostReply2.getReceiverFloor() + "楼");
            } else {
                //
            }
        }
        holder.iv_reply_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MineDetailActivity.class);
                intent.putExtra("id", mPostReply2.getMemberId());
                mcontext.startActivity(intent);
                AnimUtil.intentSlidIn((Activity)mcontext);
            }
        });

        if(mPostReply2.getPostReplyImgList()!=null){
            final ArrayList<String> list1=new ArrayList<>();
            for(int i=0;i<mPostReply2.getPostReplyImgList().size();i++){
                list1.add(mPostReply2.getPostReplyImgList().get(i).getImg());
            }
            holder.iv_reply_invitation_image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToImageZoomActivity(mcontext,list1,0);
                }
            });
            holder.iv_reply_invitation_image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToImageZoomActivity(mcontext,list1,1);
                }
            });
            holder.iv_reply_invitation_image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToImageZoomActivity(mcontext,list1,2);
                }
            });

        }
        return convertView;
    }

    private void goToImageZoomActivity(Context context,ArrayList<String>list,int pos){
        Intent intent=new Intent(context, ImageZoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("imageList", list);
        bundle.putInt("position", pos);
        intent.putExtras(bundle);
        context.startActivity(intent);
        AnimUtil.intentSlidIn((Activity)context);
    }

    static class ViewHolder {
        @InjectView(R.id.tv_reply_name)//发帖人
                TextView tv_reply_name;
        @InjectView(R.id.iv_reply_level)//等级
                ImageView iv_reply_level;
        @InjectView(R.id.iv_reply_photo)//头像
                RoundImageView iv_reply_photo;
        @InjectView(R.id.tv_reply_time)//时间
                TextView tv_reply_time;
        @InjectView(R.id.tv_comment_detail_floor)//楼层
                TextView tv_comment_detail_floor;
        @InjectView(R.id.tv_comment_detail_reply)//回复
                TextView tv_comment_detail_reply;
        @InjectView(R.id.tv_reply_address)//地址
                TextView tv_reply_address;
        @InjectView(R.id.tv_reply_content)//内容
                TextView tv_reply_content;
        @InjectView(R.id.rl_pinglun)//回复评论布局
                RelativeLayout rl_pinglun;
        @InjectView(R.id.tv_pinglun_title)//回复评论标题
                TextView tv_pinglun_title;
        @InjectView(R.id.tv_pinglun_content)//回复评论内容
                TextView tv_pinglun_content;
        @InjectView(R.id.tv_pinglun_floor)//回复评论楼层
                TextView tv_pinglun_floor;
        @InjectView(R.id.rl_reply_voice)//帖子回复语音布局
                RelativeLayout rl_reply_voice;
        @InjectView(R.id.id_recorder_anim)//帖子回复语音图片
                View id_recorder_anim;
        @InjectView(R.id.tv_reply_voice_time)//帖子回复语音时间
                TextView tv_reply_voice_time;
        /* @InjectView(R.id.rl_reply_pinglun_voice)//帖子评论语音布局
                 RelativeLayout rl_reply_pinglun_voice;
         @InjectView(R.id.id_recorder_pinglun_anim)//帖子评论语音图片
                 View id_recorder_pinglun_anim;
         @InjectView(R.id.tv_reply_pinglun_voice_time)//帖子评论语音时间
                 TextView tv_reply_pinglun_voice_time;*/
        @InjectView(R.id.ll_reply_invitation_image)//帖子回复图片布局
                LinearLayout ll_reply_invitation_image;
        @InjectView(R.id.iv_reply_invitation_image1)//帖子回复图片1
                ImageView iv_reply_invitation_image1;
        @InjectView(R.id.iv_reply_invitation_image2)//帖子回复图片2
                ImageView iv_reply_invitation_image2;
        @InjectView(R.id.iv_reply_invitation_image3)//帖子回复图片3
                ImageView iv_reply_invitation_image3;
        /*   @InjectView(R.id.ll_reply_pinglun_image)//帖子评论图片布局
                   LinearLayout ll_reply_pinglun_image;
           @InjectView(R.id.iv_reply_pinglun_image1)//帖子评论图片1
                   ImageView iv_reply_pinglun_image1;
           @InjectView(R.id.iv_reply_pinglun_image2)//帖子评论图片2
                   ImageView iv_reply_pinglun_image2;
           @InjectView(R.id.iv_reply_pinglun_image3)//帖子评论图片3
                   ImageView iv_reply_pinglun_image3;
   */
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
