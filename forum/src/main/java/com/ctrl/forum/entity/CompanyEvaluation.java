package com.ctrl.forum.entity;

import java.util.List;

/**
 * 周边店铺评论
 * Created by Administrator on 2016/5/17.
 */
public class CompanyEvaluation {
    private String id; //评论id
    private String content; //评论内容
    private String soundUrl; //语音文件url
    private long createTime; //评论创建时间
    private String memberName; //会员姓名
    private String imgUrl; //头像url
    private String memberLevel; //等级
    private String contentType; //内容类型（0：文字或者表情、1：图片、2：语音）
    private List<CompanyEvaluationPic> companyEvaluationPicList; //

    public List<CompanyEvaluationPic> getCompanyEvaluationPicList() {
        return companyEvaluationPicList;
    }

    public void setCompanyEvaluationPicList(List<CompanyEvaluationPic> companyEvaluationPicList) {
        this.companyEvaluationPicList = companyEvaluationPicList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
