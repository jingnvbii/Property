package com.ctrl.forum.cart.datasave;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by fml on 2015/12/17 0017.
 */
@Table(name = "goodes")
public class GoodsBean extends GoodsBase implements Serializable{
    @Column(column = "menupos")
    private int menupos;
    @Column(column = "goodsid")
    private String goodsid;
    @Column(column = "goodsnum")
    private String goodsnum;
    @Column(column = "goodsprice")
    private float goodsprice;
    @Column(column="goodsname")
    private String goodsname;
    @Column(column="stock")
    private String stock;
    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public int getMenupos() {
        return menupos;
    }

    public void setMenupos(int menupos) {
        this.menupos = menupos;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsnum() {
        return goodsnum;
    }

    public void setGoodsnum(String goodsnum) {
        this.goodsnum = goodsnum;
    }

    public float getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(float goodsprice) {
        this.goodsprice = goodsprice;
    }

    @Override
    public String toString() {
        return "GoodsBean{" +
                "menupos='" + menupos + '\'' +
                ", goodsid='" + goodsid + '\'' +
                ", goodsnum='" + goodsnum + '\'' +
                ", goodsprice='" + goodsprice + '\'' +
                ", names='" + goodsname + '\'' +
                ", stock='" + stock + '\'' +
                '}';
    }
}
