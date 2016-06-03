package com.ctrl.forum.entity;

/**
 * 我的小区列表
 * Created by Administrator on 2016/5/13.
 */
public class Communitys {
    private String communityName; //社区名称
    private String postCount;//发布数量
    private String companyCount;//商家数量
    private String id;//小区id

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCompanyCount() {
        return companyCount;
    }

    public void setCompanyCount(String companyCount) {
        this.companyCount = companyCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostCount() {
        return postCount;
    }

    public void setPostCount(String postCount) {
        this.postCount = postCount;
    }
}
