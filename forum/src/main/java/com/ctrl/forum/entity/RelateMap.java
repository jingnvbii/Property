package com.ctrl.forum.entity;

import java.util.List;

/**
 * 关联帖子实体
 * Created by Administrator on 2016/4/8.
 */
public class RelateMap {
    private Post rtuRelatedPost;
    private MemberInfo relateduser;
    private List<PostImage> relatedpostImgList;

    public Post getRtuRelatedPost() {
        return rtuRelatedPost;
    }

    public void setRtuRelatedPost(Post rtuRelatedPost) {
        this.rtuRelatedPost = rtuRelatedPost;
    }

    public MemberInfo getRelateduser() {
        return relateduser;
    }

    public void setRelateduser(MemberInfo relateduser) {
        this.relateduser = relateduser;
    }

    public List<PostImage> getRelatedpostImgList() {
        return relatedpostImgList;
    }

    public void setRelatedpostImgList(List<PostImage> relatedpostImgList) {
        this.relatedpostImgList = relatedpostImgList;
    }
}
