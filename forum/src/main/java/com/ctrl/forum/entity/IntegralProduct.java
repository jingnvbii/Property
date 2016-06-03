package com.ctrl.forum.entity;

/**
 * 积分商品
 * Created by Administrator on 2016/5/9.
 */
public class IntegralProduct {
    private String name; //商品名
    private String listImgUrl;//展示图片的url
    private String needPoint; //兑换所需积分
    private String id; //积分商品id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getListImgUrl() {
        return listImgUrl;
    }

    public void setListImgUrl(String listImgUrl) {
        this.listImgUrl = listImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeedPoint() {
        return needPoint;
    }

    public void setNeedPoint(String needPoint) {
        this.needPoint = needPoint;
    }
}
