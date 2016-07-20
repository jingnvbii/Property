package com.ctrl.forum.entity;

import java.util.List;

/**
 * 帖子实体类
 * Created by Administrator on 2016/4/8.
 */
public class Post {
    private String id; //帖子id
    private String reporterId;//发帖人id
    private String memberName;//发帖人昵称
    private String imgUrl;//发帖人头像Url
    private String memberLevel;//发帖人等级（会员等级）
    private String title;//帖子标题
    private String blurbs;//帖子导语
    private String sourceType;//发布来源类型（0：平台、1：App）
    private int commentNum;//评论数
    private int praiseNum;//赞数
    private int shareNum;//分享数
    private String publishTime;//帖子发布时间
    private String locationLongitude;//发帖位置经度
    private String locationLatitude;//发帖位置纬度
    private String locationName;//位置名称
    private String picturesStyle;//列表中是否总以多图方式显示（0：否、1：是） 注：只针对列表样式
    private String topShow;//是否置顶（0：否、1：是）
    private String contactPhone;//联系电话
    private String contactAddress;//联系地址
    private List<PostImage> postImgList;//图片列表
    private List<PostReply> postReplyList;//回复列表
    private String content;//帖子标题

    private String contentType;//内容类型（0：帖子、1：链接原文、2：商品详情页、3：店铺首页）
    private String articleLink;//原文链接
    private String linkItemId;//连接对象id（商品id或者店铺id）

    private String praiseState; //点赞的状态

    private String zambiastate;//是否被当前用户点赞
    private String collectstate;//是否被当前用户收藏

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

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

    public String getPraiseState() {
        return praiseState;
    }

    public void setPraiseState(String praiseState) {
        this.praiseState = praiseState;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getZambiastate() {
        return zambiastate;
    }

    public void setZambiastate(String zambiastate) {
        this.zambiastate = zambiastate;
    }

    public String getCollectstate() {
        return collectstate;
    }

    public void setCollectstate(String collectstate) {
        this.collectstate = collectstate;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public List<PostReply> getPostReplyList() {
        return postReplyList;
    }


    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public void setPostReplyList(List<PostReply> postReplyList) {
        this.postReplyList = postReplyList;
    }

    public List<PostImage> getPostImgList() {
        return postImgList;
    }

    public void setPostImgList(List<PostImage> postImgList) {
        this.postImgList = postImgList;
    }


    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBlurbs() {
        return blurbs;
    }

    public void setBlurbs(String blurbs) {
        this.blurbs = blurbs;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getPicturesStyle() {
        return picturesStyle;
    }

    public void setPicturesStyle(String picturesStyle) {
        this.picturesStyle = picturesStyle;
    }

    public String getTopShow() {
        return topShow;
    }

    public void setTopShow(String topShow) {
        this.topShow = topShow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
