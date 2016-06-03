package com.ctrl.forum.entity;

import com.ctrl.forum.base.ListItemTypeInterf;

/**
 * 返回值为1,表示一张图片
 * Created by Administrator on 2016/5/6.
 */
public class CommentOne implements ListItemTypeInterf {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return 1;
    }
}
