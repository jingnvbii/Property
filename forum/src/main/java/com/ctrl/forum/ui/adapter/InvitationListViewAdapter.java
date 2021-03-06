package com.ctrl.forum.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AndroidUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.RecyclableImageView;
import com.ctrl.forum.customview.RoundImageView;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.utils.TimeUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子一级分类列表样式adapter  adapter
 * Created by jason on 2016/4/8.
 */
public class InvitationListViewAdapter extends BaseAdapter{
    private Activity mcontext;
    private List<Post>mPostList;
    private LayoutInflater inflter;

    public InvitationListViewAdapter(Activity context) {
               this.mcontext=context;
    }

    public void setList(List<Post> list) {
        this.mPostList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mPostList==null?0:mPostList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPostList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
       int type;
       if(mPostList.get(position).getPostImgList()!=null) {
           int size = mPostList.get(position).getPostImgList().size();
           if(size>0&&size<3){
               type=1;
           }else if(size>=3){
               type=3;
           }else {
               type=0;
           }
              /* switch (size) {
                   case 0:
                       type = 0;
                       break;
                   case 1:
                       type = 1;
                       break;
                   case 2:
                       type = 1;
                       break;
                   case 3:
                       type = 3;
                       break;
                   case 4:
                       type = 3;
                       break;
                   case 5:
                       type = 3;
                       break;
                   case 6:
                       type = 3;
                       break;
                   case 7:
                       type = 3;
                       break;
                   case 8:
                       type = 3;
                       break;
                   case 9:
                       type = 3;
                       break;
               }*/

           }else {
           type=0;
       }
        return type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1=null;
        ViewHolder2 holder2=null;
        ViewHolder3 holder3=null;

        int type=getItemViewType(position);
        if(convertView==null){
             inflter = LayoutInflater.from(mcontext);
            switch (type){
                case 0:
                    convertView= inflter.inflate(R.layout.item_invitation_listview_no_image, parent, false);
                    holder1=new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                    break;
                case 1:
                    convertView= inflter.inflate(R.layout.item_invitation_listview_one_image, parent, false);
                    holder2=new ViewHolder2(convertView);
                    convertView.setTag(holder2);
                    break;
                case 3:
                    convertView= inflter.inflate(R.layout.item_invitation_listview_three_image, parent, false);
                    holder3=new ViewHolder3(convertView);
                    convertView.setTag(holder3);
                    break;
            }
        }else {
            switch (type){
                case 0:
                    holder1=(ViewHolder1)convertView.getTag();
                    break;
                case 1:
                    holder2=(ViewHolder2)convertView.getTag();
                    break;
                case 3:
                    holder3=(ViewHolder3)convertView.getTag();
                    break;

            }
            }
        Post post=mPostList.get(position);
        switch (type){
            case 0:
               // Post mPost1 = mPostList.get(position);1
                if(post==null){break;}
                holder1.tv_titile0.setText(post.getTitle());
                if(post.getMemberName()!=null&&!post.getMemberName().equals("")) {
                    holder1.tv_name0.setText(post.getMemberName());
                }else {
                    holder1.tv_name0.setText("管理员");
                }
                if(post.getBlurbs()!=null&&!post.getBlurbs().equals("")){
                    holder1.tv_daoyu.setVisibility(View.VISIBLE);
                    holder1.tv_daoyu.setText(post.getBlurbs());
                }else {
                    holder1.tv_daoyu.setVisibility(View.GONE);
                }
                if(post.getPublishTime()!=null)
                holder1.tv_time0.setText(TimeUtils.dateTime(post.getPublishTime()));
                holder1.tv_numbers0.setText(post.getCommentNum() + "");
                Arad.imageLoader.load(post.getImgUrl()).placeholder(R.mipmap.default_error).resize(50,50)
                        .centerCrop().into(holder1.imageView1);
                break;
            case 1:
               // Post mPost2 = mPostList.get(position);
                holder2.tv_titile1.setText(post.getTitle());
                if(post.getMemberName()!=null&&!post.getMemberName().equals("")) {
                    holder2.tv_name1.setText(post.getMemberName());
                }else {
                    holder2.tv_name1.setText("管理员");
                }
              //  holder2.tv_time1.setText(TimeUtils.date(Long.parseLong(post.getPublishTime())));
                holder2.tv_time1.setText(TimeUtils.dateTime(post.getPublishTime()));
                holder2.tv_numbers1.setText(post.getCommentNum() + "");
                if(post.getBlurbs()!=null&&!post.getBlurbs().equals("")){
                    holder2.tv_daoyu.setVisibility(View.VISIBLE);
                    holder2.tv_daoyu.setText(post.getBlurbs());
                }else {
                    holder2.tv_daoyu.setVisibility(View.GONE);
                }

                int width1 = AndroidUtil.getDeviceWidth(mcontext);
                RelativeLayout.LayoutParams para1;
                para1 = (RelativeLayout.LayoutParams) holder2.iv_title_photo1.getLayoutParams();
                para1.width = (width1-AndroidUtil.dp2px(mcontext,40))/3;
                holder2.iv_title_photo1.setLayoutParams(para1);
                Arad.imageLoader.load(post.getImgUrl()).placeholder(R.mipmap.default_error).resize(50,50)
                        .centerCrop().into(holder2.imageView2);
                if(post.getPostImgList()!=null) {
                    Arad.imageLoader.load(post.getPostImgList().get(0).getImg()).placeholder(R.mipmap.default_error).resize(400,400).config(Bitmap.Config.RGB_565).centerCrop().into(holder2.iv_title_photo1);
                }
                break;
            case 3:
              // Post mPos3 = mPostList.get(position);
                if(post.getTitle()==null||post.getTitle().equals("")){
                    holder3.tv_titile3.setVisibility(View.GONE);
                }
                holder3.tv_titile3.setText(post.getTitle());
                if(post.getMemberName()!=null&&!post.getMemberName().equals("")) {
                    holder3.tv_name3.setText(post.getMemberName());
                }else {
                    holder3.tv_name3.setText("管理员");
                }
                holder3.tv_time3.setText(TimeUtils.dateTime(post.getPublishTime()));
                holder3.tv_numbers03.setText(post.getCommentNum() + "");
                if(post.getBlurbs()!=null&&!post.getBlurbs().equals("")){
                    holder3.tv_daoyu.setVisibility(View.VISIBLE);
                    holder3.tv_daoyu.setText(post.getBlurbs());
                }else {
                    holder3.tv_daoyu.setVisibility(View.GONE);
                }
              /*  int width = holder3.iv_image3_01.getWidth();
                LinearLayout.LayoutParams para;
                para = (LinearLayout.LayoutParams) holder3.iv_image3_01.getLayoutParams();


                para.height = width;
                para.width = width;
                holder3.iv_image3_01.setLayoutParams(para);
                holder3.iv_image3_02.setLayoutParams(para);
                holder3.iv_image3_03.setLayoutParams(para);*/
                Arad.imageLoader.load(post.getImgUrl()).placeholder(R.mipmap.default_error).resize(50, 50)
                        .centerCrop().into(holder3.imageView3);

                if(post.getPostImgList()!=null) {
                    Arad.imageLoader.load(post.getPostImgList().get(0).getImg()).placeholder(R.mipmap.default_error).config(Bitmap.Config.RGB_565).resize(400, 400).centerCrop().into(holder3.iv_image3_01);
                    Arad.imageLoader.load(post.getPostImgList().get(1).getImg()).placeholder(R.mipmap.default_error).config(Bitmap.Config.RGB_565).resize(400, 400).centerCrop().into(holder3.iv_image3_02);
                    Arad.imageLoader.load(post.getPostImgList().get(2).getImg()).placeholder(R.mipmap.default_error).config(Bitmap.Config.RGB_565).resize(400, 400).centerCrop().into(holder3.iv_image3_03);
                }

                break;

        }
        return convertView;
    }

    static class ViewHolder1{
        @InjectView(R.id.tv_titile0)//标题
                TextView tv_titile0;
        @InjectView(R.id.tv_name0)//昵称
                TextView tv_name0;
        @InjectView(R.id.tv_time0)//时间
                TextView tv_time0;
        @InjectView(R.id.tv_daoyu)//导语
                TextView tv_daoyu;
        @InjectView(R.id.tv_numbers0)//评论数
        TextView  tv_numbers0;
        @InjectView(R.id.imageView0)
        RoundImageView imageView1;
        ViewHolder1(View view) {
            ButterKnife.inject(this, view);
        }
    }
    static class ViewHolder2{
        @InjectView(R.id.tv_titile1)//标题
                TextView tv_titile1;
        @InjectView(R.id.tv_name1)//昵称
                TextView tv_name1;
        @InjectView(R.id.tv_time1)//时间
                TextView tv_time1;
        @InjectView(R.id.tv_daoyu)//导语
                TextView tv_daoyu;
        @InjectView(R.id.tv_numbers1)//评论数
        TextView  tv_numbers1;
        @InjectView(R.id.imageView1)
        RoundImageView imageView2;
        @InjectView(R.id.iv_title_photo1)
        RecyclableImageView iv_title_photo1;
        ViewHolder2(View view) {
            ButterKnife.inject(this, view);
        }
    }
    static class ViewHolder3{
        @InjectView(R.id.tv_titile3)//标题
                TextView tv_titile3;
        @InjectView(R.id.tv_name3)//昵称
                TextView tv_name3;
        @InjectView(R.id.tv_time3)//时间
                TextView tv_time3;
        @InjectView(R.id.tv_daoyu)//导语
                TextView tv_daoyu;
        @InjectView(R.id.tv_numbers03)//评论数
        TextView  tv_numbers03;
        @InjectView(R.id.imageView3)
        RoundImageView imageView3;
        @InjectView(R.id.iv_image3_01)
        RecyclableImageView iv_image3_01;
        @InjectView(R.id.iv_image3_02)
        RecyclableImageView iv_image3_02;
        @InjectView(R.id.iv_image3_03)
        RecyclableImageView iv_image3_03;
        ViewHolder3(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
