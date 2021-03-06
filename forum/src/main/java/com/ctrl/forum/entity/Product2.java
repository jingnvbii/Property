package com.ctrl.forum.entity;

/**
 * 商品实体类
 * Created by Administrator on 2016/4/8.
 */
public class Product2 {
    private String id; //商品id
    private String name;//商品名称
    private String originalPrice;//原定价格
    private String sellingPrice;//销售价格
    private String needPoint;//兑换需要积分
    private String specification;//规格
    private String stock;//库存
    private String deliveryType;//配送方式（0：自家配送、1：第三方配送）
    private String infomation;//商品详情
    private String salesVolume;//销量
    private String collectState;//收藏（0-未收藏，1-收藏）
    private String categoryName;//分类
    private String listImgUrl;//图片地址
    private String companyId;//店铺id
    private String isAdded; //是否上架

    public String getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(String isAdded) {
        this.isAdded = isAdded;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getListImgUrl() {
        return listImgUrl;
    }

    public void setListImgUrl(String listImgUrl) {
        this.listImgUrl = listImgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getNeedPoint() {
        return needPoint;
    }

    public void setNeedPoint(String needPoint) {
        this.needPoint = needPoint;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getInfomation() {
        return infomation;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getCollectState() {
        return collectState;
    }

    public void setCollectState(String collectState) {
        this.collectState = collectState;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
