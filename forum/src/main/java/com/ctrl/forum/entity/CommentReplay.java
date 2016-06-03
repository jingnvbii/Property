package com.ctrl.forum.entity;

import com.ctrl.forum.base.ListItemTypeInterf;

/**
 * 评论..回复某人 返回2
 * Created by Administrator on 2016/5/6.
 */
public class CommentReplay implements ListItemTypeInterf {
    private String name;

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int getType() {
        return 2;
    }
}
