package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.CompanyEvaluation;

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
       // Log.e("list.get(position).getSoundUrl()setList=====",list.toString());
        notifyDataSetChanged();
    }

    //返回item类型的个数
    @Override
    public int getViewTypeCount() {return 4;}

    // 获取到具体的list的每一个成员的类型号
    @Override
    public int getItemViewType(int position) {
        if (list!=null){
            if (list.get(position).getSoundUrl()!=null){
                type=0;
            }
            if (list.get(position).getImg()!=null){
                type=1;
            }
            if (list.get(position).getThumbImg()!=null){
                type=2;
            }
            if (list.get(position).getContent()!=null){
                type=3;
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
        ViewHolder1 viewHolder1 = null;
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
                case 1: //返回值为1,表示一张图片
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.item_rimcomment_onepic, parent, false);
                    viewHolder1 = new ViewHolder1(convertView);
                    convertView.setTag(viewHolder1);
                    break;
                case 2:
                    //评论列表三张图片,返回值为0
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.item_rimcomment_threepic, parent, false);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                    break;
                case 3: //直接文字评论店铺 返回3
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
                    viewHolder1 = (ViewHolder1) convertView.getTag();
                    break;
                case 2:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;
                case 3:
                    viewHolder3 = (ViewHolder3) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        // 设置数据
        switch (type) {
            case 0:
                CompanyEvaluation companyEvaluation = (CompanyEvaluation) list.get(position);
                break;
            case 1:
                CompanyEvaluation companyEvaluation1 = (CompanyEvaluation) list.get(position);
                break;
            case 2:
                CompanyEvaluation companyEvaluation2 = (CompanyEvaluation) list.get(position);
                break;
            case 3:
                CompanyEvaluation companyEvaluation3 = (CompanyEvaluation) list.get(position);
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
    //返回值为1,表示一张图片
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
    }
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
