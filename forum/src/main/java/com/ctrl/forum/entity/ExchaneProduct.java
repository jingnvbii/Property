package com.ctrl.forum.entity;

/**
 * 兑换积分商品历史记录
 * Created by Administrator on 2016/5/11.
 */
public class ExchaneProduct {
    private String ID; //兑换ID
    private long createTime; //兑换时间
    private String  productName; //兑换商品名称
    private String point;//兑换时花费积分
    private String productImg;

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
