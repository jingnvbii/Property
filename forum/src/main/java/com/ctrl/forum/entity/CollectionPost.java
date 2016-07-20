package com.ctrl.forum.entity;

/**
 * 我的收藏---帖子收藏列表
 * Created by Administrator on 2016/5/25.
 */
public class CollectionPost {
    private String postId; //帖子id
    private String postTitle; //帖子标题
    private String postCreateTime; //帖子创建时间
    private String postImgUrl; //帖子用户头像
    private String reportName; //发帖人昵称（没用用手机号）
    private String contentType; //内容类型（0：帖子、1：链接原文、2：商品详情页、3：店铺首页）
    private String articleLink; //原文链接
    private String linkItemId; //连接对象id（商品id或者店铺id）

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getLinkItemId() {
        return linkItemId;
    }

    public void setLinkItemId(String linkItemId) {
        this.linkItemId = linkItemId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPostCreateTime() {
        return postCreateTime;
    }

    public void setPostCreateTime(String postCreateTime) {
        this.postCreateTime = postCreateTime;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImgUrl() {
        return postImgUrl;
    }

    public void setPostImgUrl(String postImgUrl) {
        this.postImgUrl = postImgUrl;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public String toString() {
        return "CollectionPost{" +
                "postCreateTime='" + postCreateTime + '\'' +
                ", postId='" + postId + '\'' +
                ", postTitle='" + postTitle + '\'' +
                ", postImgUrl='" + postImgUrl + '\'' +
                ", reportName='" + reportName + '\'' +
                '}';
    }
}
