package com.ctrl.forum.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AndroidUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.PostImage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Transformation;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城首页列表 adapter
 * Created by jason on 2016/4/8.
 */
public class FriendDetailImageAdapter extends BaseAdapter{
    private Activity mcontext;
    private List<PostImage> list;

    public FriendDetailImageAdapter(Activity context) {
               this.mcontext=context;
    }

    public void setList(List<PostImage> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_friend_detail_image,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        PostImage merchant=list.get(position);
       // final int width = holder.iv_friend_detail.getWidth();
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = (AndroidUtil.getDeviceWidth(mcontext))/2;
                int targetHeight = 500;

                if (source.getWidth() == 0 || source.getHeight() == 0) {
                    return source;
                }

                if (source.getWidth() > source.getHeight()) {//横向长图
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
        Arad.imageLoader.load(merchant.getImg()).placeholder(R.mipmap.default_error).transform(transformation).into(holder.iv_friend_detail, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
            }
        });
        return convertView;
    }

    static class ViewHolder{
       /* @InjectView(R.id.iv_grid_item)//图片
                ImageView iv_grid_item;*/
        @InjectView(R.id.iv_friend_detail)//文字
              ImageView iv_friend_detail;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
