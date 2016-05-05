package com.ctrl.forum.entity;

/**
 * 我的评论
 * Created by Administrator on 2016/4/22.
 */
public class Comment {
    private String comment;
    private String year;
    private String time;
    private String name;

    public Comment() {}
    public Comment(String comment, String name, String time, String year) {this.comment = comment;this.name = name;this.time = time;this.year = year;}
    public String getComment() {return comment;}
    public void setComment(String comment) {this.comment = comment;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}
    public String getYear() {return year;}
    public void setYear(String year) {this.year = year;}
}
