package com.ctrl.forum.entity;

/**
 * 商品管理--商品
 * Created by Administrator on 2016/5/18.
 */
public class CProduct {
    private String id; //商品主键id
    private String name; //商品名称
    private String listImgUrl; //商品列表展示用图片Url
    private String originalPrice;//原价
    private String sellingPrice;//卖价
    private String specification; //规格,单位
    private String stock;//库存
    private String salesVolume;//销量

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

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
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
}
