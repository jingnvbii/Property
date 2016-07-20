package com.ctrl.forum.entity;

import android.graphics.Bitmap;

/**
 * 发布帖子界面,用于展示图片
 * Created by Administrator on 2016/7/19.
 */
public class ImageNew {
    private String imgUrl;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
