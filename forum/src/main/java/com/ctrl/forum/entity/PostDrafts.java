package com.ctrl.forum.entity;

import java.util.List;

/**
 * 草稿箱帖子列表
 *
 * Created by Administrator on 2016/4/8.
 */
public class PostDrafts {
    private String reporterId;//会员id（发帖人ID）
    private String count;//固定日期帖子数量
    private String time;//固定日期
    private List<Drafts>draftsList;//固定日期帖子

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Drafts> getDraftsList() {
        return draftsList;
    }

    public void setDraftsList(List<Drafts> draftsList) {
        this.draftsList = draftsList;
    }
}
