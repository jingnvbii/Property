package com.ctrl.forum.entity;

/**
 * 订单评价
 * Created by Administrator on 2016/4/8.
 */
public class OrderEvaluation {
    private String id;//订单主键id
    private String createTime;//创建时间
    private String totalCost;//商品总价
    private String companyname;//商家名称
    private String img;//商家logo

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}