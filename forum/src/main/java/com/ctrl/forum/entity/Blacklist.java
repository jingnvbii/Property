package com.ctrl.forum.entity;

/**
 * 黑名单
 * Created by Administrator on 2016/4/22.
 */
public class Blacklist {
    private String id; //用户id
    private String memberId; //被拉黑用户id
    private String username; //被拉黑用户昵称
    private String userlevel; //被拉黑用户等级
    private String userimg; //被拉黑用户图片

    public Blacklist() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getUserlevel() {
        return userlevel;
    }

    public void setUserlevel(String userlevel) {
        this.userlevel = userlevel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
