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
import com.ctrl.forum.entity.CollectionPost;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的收藏---帖子收藏
 * Created by Administrator on 2016/5/3.
 */
public class MinePostCollectListAdapter extends BaseAdapter {

    private List<CollectionPost> collectionPosts;
    private Context context;

    public MinePostCollectListAdapter(Context context) {this.context = context;
    collectionPosts = new ArrayList<>();}

    public void setCollectionPosts(List<CollectionPost> collectionPosts) {
        this.collectionPosts = collectionPosts;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {return collectionPosts!=null?collectionPosts.size():0;}

    @Override
    public Object getItem(int position) {return collectionPosts.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mine_collect_post,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        if (collectionPosts!=null && collectionPosts.get(position)!=null){
            holder.post_title.setText(collectionPosts.get(position).getPostTitle());

            String soureType = collectionPosts.get(position).getSourceType();
            switch (soureType){
                case "0"://后台发的
                    holder.tv_post_name.setText("管理员");
                    break;
                case "1":
                    holder.tv_post_name.setText(collectionPosts.get(position).getReportName());
                    break;
            }

            holder.tv_post_time.setText(collectionPosts.get(position).getPostCreateTime());
            Arad.imageLoader.load(collectionPosts.get(position).getPostImgUrl()).
                    placeholder(context.getResources().getDrawable(R.mipmap.my_gray)).
                    into(holder.iconfont_head);
        }

        return convertView;
    }

    class ViewHolder{
        @InjectView(R.id.iconfont_head)
        ImageView iconfont_head;
        @InjectView(R.id.post_title)
        TextView post_title;
        @InjectView(R.id.tv_post_name)
        TextView tv_post_name;
        @InjectView(R.id.tv_post_time)
        TextView tv_post_time;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
