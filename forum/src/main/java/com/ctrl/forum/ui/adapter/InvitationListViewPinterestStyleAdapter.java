package com.ctrl.forum.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Post;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Transformation;

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
        final int width = holder.iv_pinerest_style_image.getWidth();

        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {

                int targetWidth = width/2;

                int targetHeight = 500;

                if (source.getWidth() == 0 || source.getHeight() == 0) {
                    return source;
                }

                if (source.getWidth() > source.getHeight()) {//横向长图1
                    if (source.getHeight() < targetHeight && source.getWidth() <= 400) {
                        return source;
                    } else {
                        //如果图片大小大于等于设置的高度，则按照设置的高度比例来缩放
                        double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                        int width = (int) (targetHeight * aspectRatio);
                        if (width > 400) { //对横向长图的宽度 进行二次限制
                            width = 400;
                            targetHeight = (int) (width / aspectRatio);// 根据二次限制的宽度，计算最终高度
                        }
                        if (width != 0 && targetHeight != 0) {
                            Bitmap result = Bitmap.createScaledBitmap(source, width, targetHeight, false);
                            if (result != source) {
                                // Same bitmap is returned if sizes are the same
                                source.recycle();
                            }
                            return result;
                        } else {
                            return source;
                        }
                    }
                } else {//竖向长图
                    //如果图片小于设置的宽度，则返回原图
                    if (source.getWidth() < targetWidth && source.getHeight() <= 600) {
                        return source;
                    } else {
                        //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                        int height = (int) (targetWidth * aspectRatio);
                        if (height > 600) {//对横向长图的高度进行二次限制
                            height = 600;
                            targetWidth = (int) (height / aspectRatio);//根据二次限制的高度，计算最终宽度
                        }
                        if (height != 0 && targetWidth != 0) {
                            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, height, false);
                            if (result != source) {
                                // Same bitmap is returned if sizes are the same
                                source.recycle();
                            }
                            return result;
                        } else {
                            return source;
                        }
                    }
                }

            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };
        if (post.getPostImgList() != null) {
            holder.iv_pinerest_style_image.setVisibility(View.VISIBLE);
            holder.rl_content.setVisibility(View.GONE);
            final ViewHolder finalHolder = holder;
            Arad.imageLoader.load(post.getPostImgList().get(0).getImg()).transform(transformation).into(holder.iv_pinerest_style_image, new Callback() {
                @Override
                public void onSuccess() {
                    finalHolder.rl_content.setVisibility(View.VISIBLE);
                    ((ViewGroup.MarginLayoutParams)finalHolder.iv_pinerest_style_image.getLayoutParams()).setMargins(10,10,10,10);
                    ((ViewGroup.MarginLayoutParams)finalHolder.rl_content.getLayoutParams()).setMargins(10, 10, 10, 10);
                }

                @Override
                public void onError() {

                }
            });
        }else {
            holder.iv_pinerest_style_image.setVisibility(View.GONE);
            holder.rl_content.setVisibility(View.GONE);
        }

        holder.tv_pinerest_style_zan.setText(post.getPraiseNum() + "");
        if(post.getPostImgList()!=null)
        holder.tv_pinerest_style_imagenum.setText(post.getPostImgList().size() + " 图");
        return convertView;
    }


    static class ViewHolder {
        @InjectView(R.id.iv_pinerest_style_image)//图片
                ImageView iv_pinerest_style_image;
        @InjectView(R.id.tv_pinerest_style_title)//标题1
                TextView tv_pinerest_style_title;
        @InjectView(R.id.tv_pinerest_style_imagenum)//图片数
                TextView tv_pinerest_style_imagenum;
        @InjectView(R.id.tv_pinerest_style_zan)//点赞数
                TextView tv_pinerest_style_zan;
        @InjectView(R.id.rl_content)//点赞数
                RelativeLayout rl_content;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
