package com.ctrl.forum.entity;

/**
 * 草稿箱帖子实体类
 * Created by Administrator on 2016/4/8.
 */
public class Drafts {
    private String id;//帖子ID
    private String title;//帖子标题
    private String status;//帖子状态 2标示审核未通过
    private String publishState;//帖子状态 0标示草稿

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublishState() {
        return publishState;
    }

    public void setPublishState(String publishState) {
        this.publishState = publishState;
    }
}
