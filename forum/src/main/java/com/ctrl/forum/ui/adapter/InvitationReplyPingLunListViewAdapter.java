package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.PostImage;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子详情评论回复列表  adapter
 * Created by jason on 2016/4/8.
 */
public class InvitationReplyPingLunListViewAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Post> mPostList;
    private List<PostImage> mPostImageList;
    private LayoutInflater inflter;

    private List<PostImage> imageList;


    public InvitationReplyPingLunListViewAdapter(Context context) {
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
            convertView = inflter.inflate(R.layout.item_invitation_reply_pinglun, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Post post = mPostList.get(position);
        holder.tv_pinglun_title.setText(post.getTitle());
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_pinglun_title)//标题
                TextView tv_pinglun_title;
        @InjectView(R.id.tv_pinglun_content)//内容
                TextView tv_pinglun_content;
        @InjectView(R.id.tv_pinglun_floor)//楼层
                TextView tv_pinglun_floor;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}

