package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.ReplyForMe;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心--我收到的评论列表
 * Created by Administrator on 2016/5/9.
 */
public class ReplyCommentDao extends IDao{
    private List<ReplyForMe> replyForMes = new ArrayList<>();//收到的评论列表

    public ReplyCommentDao(INetResult activity) {
        super(activity);
    }

    /**
     *获取我收到的评论列表
     * @param reporterId  会员id
     * @param pageNum  当前页码
     * @param pageSize  每页条数
     */
    public void obtainReply(String reporterId,int pageNum,int pageSize){
        String url="postReply/obtainReplyListForMe";
        Map<String,String> map = new HashMap<>();
        map.put("reporterId",reporterId);
        map.put("pageNum",pageNum+"");
        map.put("pageSize",pageSize+"");
        postRequest(Constant.RAW_URL+url, mapToRP(map), 0);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            Log.d("demo", "dao中结果集(我的评论): " + result);
            replyForMes = JsonUtil.node2pojoList(result.findValue("replyForMeList"),ReplyForMe.class);
        }
    }

    public List<ReplyForMe> getReplyForMes() {
        return replyForMes;
    }
}
