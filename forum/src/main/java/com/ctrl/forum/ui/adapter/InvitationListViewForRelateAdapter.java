package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.RoundImageView;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.entity.RelateMap;
import com.ctrl.forum.utils.TimeUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子一级分类列表样式adapter  adapter
 * Created by jason on 2016/4/8.
 */
public class InvitationListViewForRelateAdapter extends BaseAdapter{
    private Context mcontext;
    private LayoutInflater inflter;
    private List<RelateMap> mListRelateMap;

  /*  private List<Post> mPostList;
    private List<PostImage> mListPostImage;
    private List<MemberInfo> mListUser;*/


    public InvitationListViewForRelateAdapter(Context context) {
               this.mcontext=context;
    }

    public void setList(List<RelateMap> list) {
        this.mListRelateMap = list;
        notifyDataSetChanged();
    }
/*    public void setList(List<Post> list,List<MemberInfo>listUser,List<PostImage>listPostImage) {
        this.mPostList = list;
        this.mListUser = listUser;
        this.mListPostImage = listPostImage;
        notifyDataSetChanged();
    }*/


    @Override
    public int getCount() {
        return mListRelateMap==null?0:mListRelateMap.size();
    }

    @Override
    public Object getItem(int position) {
        return mListRelateMap.get(position);
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
       int type=0;
       if(mListRelateMap.get(position).getRelatedpostImgList()!=null) {
           int size =mListRelateMap.get(position).getRelatedpostImgList().size();
           switch (size) {
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
           }
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
                case -1:
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
                case -1:
                    break;
            }
            }
        Post post=mListRelateMap.get(position).getRtuRelatedPost();
        MemberInfo user=mListRelateMap.get(position).getRelateduser();
         List<PostImage> postImageList=mListRelateMap.get(position).getRelatedpostImgList();
        switch (type){
            case 0:
                if(post==null){break;}
               // Post mPost1 = mPostList.get(position);
               holder1.tv_titile0.setText(post.getTitle());
                holder1.tv_name0.setText(post.getMemberName());
                if(post.getBlurbs()!=null&&!post.getBlurbs().equals("")){
                    holder1.tv_daoyu.setVisibility(View.VISIBLE);
                    holder1.tv_daoyu.setText(post.getBlurbs());
                }else {
                    holder1.tv_daoyu.setVisibility(View.GONE);
                }
                if(post.getPublishTime()!=null)
                holder1.tv_time0.setText(TimeUtils.dateTime(post.getPublishTime()));
                holder1.tv_numbers0.setText(post.getCommentNum() + "");
                if(user!=null&&user.getImgUrl()!=null&&!user.getImgUrl().equals(""))
                Arad.imageLoader.load(user.getImgUrl()).placeholder(R.mipmap.default_error).resize(300,300)
                        .centerCrop().into(holder1.imageView1);
                break;
            case 1:
               // Post mPost2 = mPostList.get(position);
                holder2.tv_titile1.setText(post.getTitle());
                holder2.tv_name1.setText(post.getMemberName());
              //  holder2.tv_time1.setText(TimeUtils.date(Long.parseLong(post.getPublishTime())));
                if(post.getPublishTime()!=null)
                holder2.tv_time1.setText(TimeUtils.dateTime(post.getPublishTime()));
                holder2.tv_numbers1.setText(post.getCommentNum() + "");
                if(post.getBlurbs()!=null&&!post.getBlurbs().equals("")){
                    holder2.tv_daoyu.setVisibility(View.VISIBLE);
                    holder2.tv_daoyu.setText(post.getBlurbs());
                }else {
                    holder2.tv_daoyu.setVisibility(View.GONE);
                }
                if(user!=null&&user.getImgUrl()!=null&&!user.getImgUrl().equals(""))
                Arad.imageLoader.load(user.getImgUrl()).placeholder(R.mipmap.default_error).resize(300,300)
                        .centerCrop().into(holder2.imageView2);
                if(postImageList!=null) {
                    Arad.imageLoader.load(postImageList.get(0).getImg()).placeholder(R.mipmap.default_error).resize(150,150).into(holder2.iv_title_photo1);
                }
                break;
            case 3:
              // Post mPos3 = mPostList.get(position);
                if(post.getTitle()==null||post.getTitle().equals("")){
                    holder3.tv_titile3.setVisibility(View.GONE);
                }
                holder3.tv_titile3.setText(post.getTitle());
                holder3.tv_name3.setText(post.getMemberName());
                if(post.getPublishTime()!=null)
                holder3.tv_time3.setText(TimeUtils.dateTime(post.getPublishTime()));
                holder3.tv_numbers03.setText(post.getCommentNum() + "");
                if(post.getBlurbs()!=null&&!post.getBlurbs().equals("")){
                    holder3.tv_daoyu.setVisibility(View.VISIBLE);
                    holder3.tv_daoyu.setText(post.getBlurbs());
                }else {
                    holder3.tv_daoyu.setVisibility(View.GONE);
                }
                if(user!=null&&user.getImgUrl()!=null&&!user.getImgUrl().equals("")) {
                    Arad.imageLoader.load(user.getImgUrl()).placeholder(R.mipmap.default_error).resize(300, 300)
                            .centerCrop().into(holder3.imageView3);
                }
                if(postImageList!=null) {
                    Arad.imageLoader.load(postImageList.get(0).getImg()).placeholder(R.mipmap.default_error).resize(150,150).into(holder3.iv_image3_01);
                    Arad.imageLoader.load(postImageList.get(1).getImg()).placeholder(R.mipmap.default_error).resize(150,150).into(holder3.iv_image3_02);
                    Arad.imageLoader.load(postImageList.get(2).getImg()).placeholder(R.mipmap.default_error).resize(150,150).into(holder3.iv_image3_03);
                }
                break;
            case -1:
                break;
        }
        return convertView;
    }

    static class ViewHolder1{
        @InjectView(R.id.tv_titile0)//标题
                TextView tv_titile0;
        @InjectView(R.id.tv_daoyu)//导语
                TextView tv_daoyu;
        @InjectView(R.id.tv_name0)//昵称
                TextView tv_name0;
        @InjectView(R.id.tv_time0)//时间
                TextView tv_time0;
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
        @InjectView(R.id.tv_daoyu)//导语
                TextView tv_daoyu;
        @InjectView(R.id.tv_time1)//时间
                TextView tv_time1;
        @InjectView(R.id.tv_numbers1)//评论数
        TextView  tv_numbers1;
        @InjectView(R.id.imageView1)
        RoundImageView imageView2;
        @InjectView(R.id.iv_title_photo1)
        ImageView iv_title_photo1;
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
        @InjectView(R.id.tv_daoyu)//时间
                TextView tv_daoyu;
        @InjectView(R.id.tv_numbers03)//评论数
        TextView  tv_numbers03;
        @InjectView(R.id.imageView3)
        RoundImageView imageView3;
        @InjectView(R.id.iv_image3_01)
        ImageView iv_image3_01;
        @InjectView(R.id.iv_image3_02)
        ImageView iv_image3_02;
        @InjectView(R.id.iv_image3_03)
        ImageView iv_image3_03;
        ViewHolder3(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
