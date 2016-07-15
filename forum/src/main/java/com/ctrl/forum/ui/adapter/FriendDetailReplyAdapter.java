package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.PostReply2;
import com.ctrl.forum.face.FaceConversionUtil;
import com.ctrl.forum.ui.activity.Invitation.InvitationDetailActivity;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.ui.activity.mine.MineDetailActivity;
import com.ctrl.forum.utils.TimeUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城首页列表 adapter
 * Created by jason on 2016/4/8.
 */
public class FriendDetailReplyAdapter extends BaseAdapter{
    private Context mcontext;
    private List<PostReply2> list;

    public FriendDetailReplyAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<PostReply2> list) {
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_invitation_detail,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        final PostReply2 merchant=list.get(position);
        holder.tv_reply_name.setText(merchant.getMemberName());
        holder.tv_comment_detail_floor.setText(merchant.getMemberFloor()+"楼");
        holder.tv_reply_time.setText(TimeUtils.timeFormat(Long.parseLong(merchant.getCreateTime()), "yyyy-MM-dd"));
        if(merchant.getContentType().equals("0")) {
            holder.tv_reply_content.setVisibility(View.VISIBLE);
            holder.rl_reply_voice.setVisibility(View.GONE);
            SpannableString spannableString2 = FaceConversionUtil.getInstace().getExpressionString(mcontext, merchant.getReplyContent());
            holder.tv_reply_content.setText(spannableString2);
        }
        if(merchant.getContentType().equals("1")) {
            holder.tv_reply_content.setText(" [图片]");
        }
        if(merchant.getContentType().equals("2")) {
            holder.tv_reply_content.setVisibility(View.GONE);
            holder.rl_reply_voice.setVisibility(View.VISIBLE);
        }
        Arad.imageLoader.load(merchant.getImgUrl()).placeholder(R.mipmap.default_error).into(holder.iv_reply_photo);
        String levlel = merchant.getMemberLevel();
        if (levlel != null) {
            switch (levlel) {
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

        holder.iv_reply_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    mcontext.startActivity(new Intent(mcontext, LoginActivity.class));
                    AnimUtil.intentSlidOut((InvitationDetailActivity)mcontext);
                    return;
                }
                Intent intent = new Intent(mcontext, MineDetailActivity.class);
                intent.putExtra("id", merchant.getMemberId());
                mcontext.startActivity(intent);
                AnimUtil.intentSlidIn((InvitationDetailActivity)mcontext);
            }
        });

        return convertView;
    }

    static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
        @InjectView(R.id.iv_reply_photo)//文字
               ImageView iv_reply_photo;
        @InjectView(R.id.iv_reply_level)//文字
               ImageView iv_reply_level;
        @InjectView(R.id.tv_reply_name)
                TextView tv_reply_name;
        @InjectView(R.id.tv_reply_time)
                TextView tv_reply_time;
        @InjectView(R.id.tv_comment_detail_floor)
                TextView tv_comment_detail_floor;
        @InjectView(R.id.tv_reply_content)
                TextView tv_reply_content;
        @InjectView(R.id.rl_reply_voice)
        RelativeLayout rl_reply_voice;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
