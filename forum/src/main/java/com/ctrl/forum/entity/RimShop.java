package com.ctrl.forum.entity;

/**
 * Created by Administrator on 2016/4/27.
 */
public class RimShop {
    private String name;
    private String address;
    private String phoneNum;
    private int total;
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getPhoneNum() {return phoneNum;}
    public void setPhoneNum(String phoneNum) {this.phoneNum = phoneNum;}
    public int getTotal() {return total;}
    public void setTotal(int total) {this.total = total;}
    public RimShop() {}
}
