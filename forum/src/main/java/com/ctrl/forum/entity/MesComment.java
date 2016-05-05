package com.ctrl.forum.entity;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MesComment {
    private String url;
    private String grad;
    private String vip_name;
    private String day;
    private String comment;
    private String reply_name;
    private String reply_content;
    public String getVip_name() {return vip_name;}
    public void setVip_name(String vip_name) {this.vip_name = vip_name;}
    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}
    public String getComment() {return comment;}
    public void setComment(String comment) {this.comment = comment;}
    public String getDay() {return day;}
    public void setDay(String day) {this.day = day;}
    public String getGrad() {return grad;}
    public void setGrad(String grad) {this.grad = grad;}
    public String getReply_content() {return reply_content;}
    public void setReply_content(String reply_content) {this.reply_content = reply_content;}
    public String getReply_name() {return reply_name;}
    public void setReply_name(String reply_name) {this.reply_name = reply_name;}

    public MesComment() {}
}
