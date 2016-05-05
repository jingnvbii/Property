package com.ctrl.forum.entity;

/**
 * 消息֪
 * Created by Administrator on 2016/4/21.
 */
public class Message {
    private String url;    //图片的地址
    private String name;
    private String data;
    private String content;

    public Message() {}
    public Message(String content, String name, String data, String url) {this.content = content;this.name = name;this.data = data;this.url = url;}
    public String getContent() {return content;}
    public String getData() {return data;}
    public String getName() {return name;}
    public String getUrl() {return url;}
    public void setContent(String content) {this.content = content;}
    public void setData(String data) {this.data = data;}
    public void setUrl(String url) {this.url = url;}
    public void setName(String name) {this.name = name;}
}
