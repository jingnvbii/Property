package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.PostImage;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子一级分类  adapter
 * Created by jason on 2016/4/8.
 */
public class InvitationListViewAdapter extends BaseAdapter{
    private Context mcontext;
    private List<Post>mPostList;
    private List<PostImage>mPostImageList;
    private LayoutInflater inflter;

    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    final int TYPE_3 = 2;
    private List<PostImage> imageList;

    public InvitationListViewAdapter(Context context) {
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

    // 每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position) {
        Post mpost = mPostList.get(position);
        imageList=mpost.getPostImgList();
        if (imageList.size() == 0)
            return TYPE_1;
        else if (imageList.size() == 1)
            return TYPE_2;
        else if(imageList.size()==3)
            return TYPE_3;
        else return 111;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1=null;
        ViewHolder2 holder2=null;
        ViewHolder3 holder3=null;
        int type = getItemViewType(position);
        if(convertView==null){
             inflter = LayoutInflater.from(mcontext);
            switch (type){
                case TYPE_1:
                    convertView= inflter.inflate(R.layout.item_invitation_listview_no_image, parent, false);
                    holder1=new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                    break;
                case TYPE_2:
                    convertView= inflter.inflate(R.layout.item_invitation_listview_one_image, parent, false);
                    holder2=new ViewHolder2(convertView);
                    convertView.setTag(holder2);
                    break;
                case TYPE_3:
                    convertView=inflter.inflate(R.layout.item_invitation_listview_three_image, parent, false);
                    holder3=new ViewHolder3(convertView);
                    convertView.setTag(holder3);
                    break;
                case 111:
                    MessageUtils.showShortToast(mcontext,"cuole ");
                    break;
            }
        }else {
            switch (type){
                case TYPE_1:
                    holder1=(ViewHolder1)convertView.getTag();
                    break;
                case TYPE_2:
                    holder2=(ViewHolder2)convertView.getTag();
                    break;
                case TYPE_3:
                    holder3=(ViewHolder3)convertView.getTag();
                    break;
            }

        }

       // Post mPost = mPostList.get(position);
        switch (type){
            case TYPE_1:
                Post mPost1 = mPostList.get(position);
                holder1.tv_titile0.setText(mPost1.getTitle());
                holder1.tv_name0.setText(mPost1.getMemberName());
                holder1.tv_time0.setText(mPost1.getPublishTime());
                holder1.tv_numbers0.setText(mPost1.getCommentNum());
                Arad.imageLoader.load(mPost1.getImgUrl()).into(holder1.imageView0);
                break;
            case TYPE_2:
                Post mPost2 = mPostList.get(position);
                holder2.tv_titile1.setText(mPost2.getTitle());
                holder2.tv_name1.setText(mPost2.getMemberName());
                holder2.tv_time1.setText(mPost2.getPublishTime());
                holder2.tv_numbers1.setText(mPost2.getCommentNum());
                Arad.imageLoader.load(mPost2.getImgUrl()).into(holder2.imageView1);
                Arad.imageLoader.load(mPostImageList.get(0).getImg()).into(holder2.iv_title_photo1);
                break;
            case TYPE_3:
                Post mPost3 = mPostList.get(position);
                holder3.tv_titile3.setText(mPost3.getTitle());
                holder3.tv_name3.setText(mPost3.getMemberName());
                holder3.tv_time3.setText(mPost3.getPublishTime());
             //   holder3.tv_numbers03.setText(mPost3.getCommentNum());
                Arad.imageLoader.load(mPost3.getImgUrl()).into(holder3.imageView3);
             //   Arad.imageLoader.load(mPostImageList.get(0).getImg()).into(holder3.iv_image3_01);
            //    Arad.imageLoader.load(mPostImageList.get(1).getImg()).into(holder3.iv_image3_02);
           //     Arad.imageLoader.load(mPostImageList.get(2).getImg()).into(holder3.iv_image3_03);
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
        @InjectView(R.id.tv_numbers0)//评论数
        TextView  tv_numbers0;
        @InjectView(R.id.imageView0)
        ImageView imageView0;
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
        @InjectView(R.id.tv_numbers1)//评论数
                TextView  tv_numbers1;
        @InjectView(R.id.imageView1)
        ImageView imageView1;
        @InjectView(R.id.iv_title_photo1)//图片
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
        @InjectView(R.id.tv_numbers03)//评论数
                TextView  tv_numbers03;
        @InjectView(R.id.imageView3)
        ImageView imageView3;
        @InjectView(R.id.iv_image3_01)//图片
                ImageView iv_image3_01;
        @InjectView(R.id.iv_image3_02)//图片
                ImageView iv_image3_02;
        @InjectView(R.id.iv_image3_03)//图片
                ImageView iv_image3_03;
        ViewHolder3(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
