package com.ctrl.forum.entity;

/**
 * �Ż݄�
 * Created by Administrator on 2016/5/3.
 */
public class Coupon {
    private String name;//�Ż݄����̵���
    private String price;//�۸�
    private String limit;//ʹ������
    private String time;//ʱ��
    private String man;//����ٿ�ʹ��

    public Coupon() {
    }

    public String getMan() {
        return man;
    }

    public void setMan(String man) {
        this.man = man;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
