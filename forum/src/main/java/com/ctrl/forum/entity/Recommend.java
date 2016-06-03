package com.ctrl.forum.entity;

/**
 * 推荐实体类
 * Created by Administrator on 2016/4/8.
 */
public class Recommend {
    private String targetId;//推荐id（商品、店铺或者帖子id）
    private String type;//推荐类型（0：商家、1：商品、2：帖子、3：外部链接）
    private String imgUrl;//推荐图片Url
    private String targetUrl;//推荐图片Url

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
