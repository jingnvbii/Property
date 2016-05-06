package com.ctrl.forum.entity;

import com.ctrl.forum.base.RimCommentListItemType;

/**
 * 评论列表三张图片,0表示三张图片的列表
 * Created by Administrator on 2016/5/6.
 */
public class CommentThree implements RimCommentListItemType {
    private String name;

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int getType() {
        return 0;
    }
}
