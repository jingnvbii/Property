package com.ctrl.forum.entity;

import com.ctrl.forum.base.RimCommentListItemType;

/**
 * 直接回复店铺,返回3
 * Created by Administrator on 2016/5/6.
 */
public class CommentShop implements RimCommentListItemType {
    private String name;

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int getType() {
        return 3;
    }
}
