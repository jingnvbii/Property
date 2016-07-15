package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Post;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子列表瀑布流样式adapter  adapter
 * Created by jason on 2016/4/8.
 */
public class InvitationListViewPinterestStyleAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Post> mPostList;
    private LayoutInflater inflter;


    public InvitationListViewPinterestStyleAdapter(Context context) {
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
        if (convertView == null) {
            inflter = LayoutInflater.from(mcontext);
            convertView = inflter.inflate(R.layout.item_pinterest, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Post post = mPostList.get(position);
        holder.tv_pinerest_style_title.setText(post.getTitle());
        if (post.getPostImgList() != null) {
            Arad.imageLoader.load(post.getPostImgList().get(0).getImg()).placeholder(R.mipmap.default_error).config(Bitmap.Config.RGB_565).into(holder.iv_pinerest_style_image);
        }
        holder.tv_pinerest_style_zan.setText(post.getPraiseNum() + "");
        if(post.getPostImgList()!=null)
        holder.tv_pinerest_style_imagenum.setText(post.getPostImgList().size() + " 图");
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.iv_pinerest_style_image)//图片
                ImageView iv_pinerest_style_image;
        @InjectView(R.id.tv_pinerest_style_title)//标题
                TextView tv_pinerest_style_title;
        @InjectView(R.id.tv_pinerest_style_imagenum)//图片数
                TextView tv_pinerest_style_imagenum;
        @InjectView(R.id.tv_pinerest_style_zan)//点赞数
                TextView tv_pinerest_style_zan;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
