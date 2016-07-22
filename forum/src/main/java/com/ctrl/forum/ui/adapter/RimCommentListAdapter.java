package com.ctrl.forum.ui.adapter;

import android.content.Context;
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
import com.ctrl.forum.entity.CompanyEvaluation;
import com.ctrl.forum.face.FaceConversionUtil;
import com.ctrl.forum.ui.activity.rim.RimStoreCommentActivity;
import com.ctrl.forum.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周边服务---评论
 * Created by Administrator on 2016/5/6.
 */
public class RimCommentListAdapter extends BaseAdapter {
    private Context context;
    private List<CompanyEvaluation> list;
    private int type;

    public RimCommentListAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<CompanyEvaluation> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //返回item类型的个数
    @Override
    public int getViewTypeCount() {return 3;}

    // 获取到具体的list的每一个成员的类型号
    @Override
    public int getItemViewType(int position) {
        if (list!=null){
            if (list.get(position).getContentType().equals("2")){
                type=0;  //语音
            }
            if (list.get(position).getContentType().equals("1")){
                type=1; //图片
            }
            if (list.get(position).getContentType().equals("0")){
                type=2; //文字或表情
            }
        }
        return type;
    }

    @Override
    public int getCount() {return list!=null?list.size():0;}

    @Override
    public Object getItem(int position) {return list.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        ViewHolder2 viewHolder2 = null;
        ViewHolder3 viewHolder3 = null;
        if (convertView == null) {
            switch (type) {
                case 0:
                    //语音
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.item_rimcomment_voice, parent, false);
                    viewHolder2 = new ViewHolder2(convertView);
                    convertView.setTag(viewHolder2);
                    break;
                case 1: //图片
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.item_rimcomment_threepic, parent, false);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                    break;
                case 2:
                    ////直接文字评论店铺
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.item_rimcomment_comment, parent, false);
                    viewHolder3 = new ViewHolder3(convertView);
                    convertView.setTag(viewHolder3);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case 0:
                    viewHolder2 = (ViewHolder2) convertView.getTag();
                    break;
                case 1:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;
                case 2:
                    viewHolder3 = (ViewHolder3) convertView.getTag();
                    break;
                default:
                    break;
            }
        }


        // 设置数据
        switch (type) {
            case 0:
                final CompanyEvaluation companyEvaluation =list.get(position);
                viewHolder2.tv_data.setText(DateUtil.getStringByFormat(companyEvaluation.getCreateTime(),"yyyy-MM-dd"));
                viewHolder2.tv_name.setText(companyEvaluation.getMemberName());
                Arad.imageLoader.load(companyEvaluation.getImgUrl()).placeholder(R.mipmap.default_error).into(viewHolder2.iv_head);
                SetMemberLevel.setLevelImage(context, viewHolder2.iv_grade, companyEvaluation.getMemberLevel());

                viewHolder2.rl_reply_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RimStoreCommentActivity activity = (RimStoreCommentActivity)context;
                        activity.playSound(v, companyEvaluation.getSoundUrl());
                    }
                });

                break;
            case 1:
                CompanyEvaluation companyEvaluation1 = list.get(position);
                viewHolder.tv_data.setText(DateUtil.getStringByFormat(companyEvaluation1.getCreateTime(), "yyyy-MM-dd"));
                viewHolder.tv_name.setText(companyEvaluation1.getMemberName());
                Arad.imageLoader.load(companyEvaluation1.getImgUrl()).into(viewHolder.iv_head);
                SetMemberLevel.setLevelImage(context,viewHolder.iv_grade,companyEvaluation1.getMemberLevel());
                for (int i=0;i<companyEvaluation1.getCompanyEvaluationPicList().size();i++){
                    String imageUrl = companyEvaluation1.getCompanyEvaluationPicList().get(i).getThumbImg();
                    ImageView iv_pic = (ImageView) viewHolder.ll_threePic.getChildAt(i);
                    Arad.imageLoader.load(imageUrl).into(iv_pic);
                }
                break;
            case 2:
                CompanyEvaluation companyEvaluation2 = list.get(position);
                viewHolder3.tv_data.setText(DateUtil.getStringByFormat(companyEvaluation2.getCreateTime(),"yyyy-MM-dd"));
                viewHolder3.tv_name.setText(companyEvaluation2.getMemberName());
                Arad.imageLoader.load(companyEvaluation2.getImgUrl()).into(viewHolder3.iv_head);
                SetMemberLevel.setLevelImage(context, viewHolder3.iv_grade, companyEvaluation2.getMemberLevel());
                SpannableString spannableString2 = FaceConversionUtil.getInstace().getExpressionString(context, companyEvaluation2.getContent());
                viewHolder3.tv_replay.setText(spannableString2);
                break;
            default:
                break;
        }
        return convertView;
    }

    //评论列表三张图片,返回值为0
    class ViewHolder {
        @InjectView(R.id.ll_threePic)
        LinearLayout ll_threePic;//存放三张图片的LinearLayout
        @InjectView(R.id.iv_head)
        ImageView iv_head;//头像
        @InjectView(R.id.tv_name)
        TextView tv_name; //
        @InjectView(R.id.iv_grade)
        ImageView iv_grade; //等级
        @InjectView(R.id.tv_data)
        TextView tv_data;//时间
        ViewHolder(View view) {
            ButterKnife.inject(this, view);}
    }
   /* //返回值为1,表示一张图片
    class ViewHolder1 {
        @InjectView(R.id.iv_head)
        ImageView iv_head;//头像
        @InjectView(R.id.tv_name)
        TextView tv_name; //本人名
        @InjectView(R.id.iv_grade)
        ImageView iv_grade; //等级
        @InjectView(R.id.tv_data)
        TextView tv_data;//时间
        @InjectView(R.id.iv_pic1)
        ImageView iv_pic1;//回复的名字
        ViewHolder1(View view) {ButterKnife.inject(this, view);}
    }*/
    //语音
    class ViewHolder2{
        @InjectView(R.id.iv_head)
          ImageView iv_head;//头像
        @InjectView(R.id.tv_name)
          TextView tv_name; //本人名
        @InjectView(R.id.iv_grade)
          ImageView iv_grade; //等级
        @InjectView(R.id.tv_data)
       TextView tv_data;//时间
       @InjectView(R.id.rl_reply_voice)
       RelativeLayout rl_reply_voice;//语音

        ViewHolder2(View view) {ButterKnife.inject(this, view);}
    }
    //直接文字回复
    class ViewHolder3{
        @InjectView(R.id.iv_head)
        ImageView iv_head;//头像
        @InjectView(R.id.tv_name)
        TextView tv_name; //本人名
        @InjectView(R.id.iv_grade)
        ImageView iv_grade; //等级
        @InjectView(R.id.tv_data)
        TextView tv_data;//时间
        @InjectView(R.id.tv_replay)
        TextView tv_replay;
        ViewHolder3(View view) {ButterKnife.inject(this, view);}
    }

}
