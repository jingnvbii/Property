package com.ctrl.forum.entity;

/**
 * Created by Administrator on 2016/4/22.
 */
public class Blacklist {
    private String icon_url;
    private String grade_url;
    private String name;

    public String getGrade_url() {return grade_url;}
    public void setGrade_url(String grade_url) {this.grade_url = grade_url;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getIcon_url() {return icon_url;}
    public void setIcon_url(String icon_url) {this.icon_url = icon_url;}
    public Blacklist() {}
}
