package com.ctrl.forum.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/4/25.
 */
public class OrderStatus2 {
    private String status;
    private String time;
    private String tel;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    private Drawable drawable;


    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
