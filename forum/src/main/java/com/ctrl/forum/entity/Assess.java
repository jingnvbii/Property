package com.ctrl.forum.entity;

/**
 * 我的评价
 * Created by Administrator on 2016/5/3.
 */
public class Assess {
    private String id;  //订单id
    private String createTime; //创建时间
    private String totalCost;  //商品总价
    private String companyname; //商家名称
    private String img; //商家logo

    public Assess() {
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }
}
