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
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.utils.TimeUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子方块列表样式adapter  adapter
 * Created by jason on 2016/4/8.
 */
public class InvitationListViewBlockStyleAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Post> mPostList;
    private List<PostImage> mPostImageList;
    private LayoutInflater inflter;

    private List<PostImage> imageList;


    public InvitationListViewBlockStyleAdapter(Context context) {
        this.mcontext = context;
    }

    public void setList(List<Post> list) {
        this.mPostList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mPostList == null ? 0 : mPostList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        int type = getItemViewType(position);
        if (convertView == null) {
            inflter = LayoutInflater.from(mcontext);
            convertView = inflter.inflate(R.layout.item_invitation_style_3, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            Post post = mPostList.get(position);
            holder.tv_block_style_titile.setText(post.getTitle());
            if(post.getPostImgList()!=null) {
                Arad.imageLoader.load(post.getPostImgList().get(0).getImg()).placeholder(R.mipmap.default_error).resize(400,400).centerCrop().into(holder.iv_block_style_photo);
            }else {
                holder.iv_block_style_photo.setImageResource(R.mipmap.default_error);
            }
            if(post.getPublishTime()!=null)
            holder.tv_block_style_time.setText(TimeUtils.date(Long.parseLong(post.getPublishTime())));
            holder.tv_block_style_zan.setText(post.getCommentNum()+"");
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.iv_block_style_photo)//图片
                ImageView iv_block_style_photo;
        @InjectView(R.id.tv_block_style_titile)//标题
                TextView tv_block_style_titile;
        @InjectView(R.id.tv_block_style_time)//时间
                TextView tv_block_style_time;
        @InjectView(R.id.tv_block_style_zan)//评论数
                TextView tv_block_style_zan;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
