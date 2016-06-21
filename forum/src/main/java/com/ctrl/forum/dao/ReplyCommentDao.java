package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.Assess;
import com.ctrl.forum.entity.Message;
import com.ctrl.forum.entity.ObtainMyReply;
import com.ctrl.forum.entity.ReplyForMe;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论,评价
 * Created by Administrator on 2016/5/9.
 */
public class ReplyCommentDao extends IDao{
    private List<ReplyForMe> replyForMes = new ArrayList<>();//收到的评论列表
    private List<Assess> assesses = new ArrayList<>();  //我的评价
    private List<ObtainMyReply> obtainMyReplies = new ArrayList<>(); //我收到的评论
    private List<Message> messages = new ArrayList<>(); //消息通知

    public ReplyCommentDao(INetResult activity) {
        super(activity);
    }

    /**
     * 获取我收到的评论列表--消息通知
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
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }

    /**
     * 获取消息列表接口
     * @param receiverId  消息接收者id
     * @param pageNum  当前页码
     * @param pageSize  每页条数
     */
    public void queryMessageList(String receiverId,int pageNum,int pageSize){
        String url="message/queryMessageList";
        Map<String,String> map = new HashMap<>();
        map.put("receiverId",receiverId);
        map.put("pageNum",pageNum+"");
        map.put("pageSize",pageSize+"");
        postRequest(Constant.RAW_URL + url, mapToRP(map), 4);
    }


    /**
     * 获取待评价或者已评价订单列表接口
     * @param memberId  会员id
     * @param evaluationState  评价状态（0：未评价、1：已评价）
     */
    public void queryOrderEvaluation(String memberId,String evaluationState){
        String url="order/queryOrderEvaluationList";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("evaluationState",evaluationState);
        postRequest(Constant.RAW_URL+url, mapToRP(map), 1);
    }

    /**
     * 获取周边商家评论列表
     * @param companyId  会员id
     * @param pageNum  当前页码
     * @param pageSize  每页条数
     */
    public void queryAroundCompanyEvaluation(String companyId,String pageNum,String pageSize){
        String url="companyEvaluation/queryAroundCompanyEvaluation";
        Map<String,String> map = new HashMap<>();
        map.put("companyId",companyId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL+url, mapToRP(map), 2);
    }

    /**
     * 获取个人中心我的评论列表--个人中心
     * @param memberId  会员id
     * @param pageNum  当前页码
     * @param pageSize  每页条数
     */
    public void obtainMyReply(String memberId,int pageNum,int pageSize){
        String url="postReply/obtainMyReplyList";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("pageNum",pageNum+"");
        map.put("pageSize",pageSize+"");
        postRequest(Constant.RAW_URL + url, mapToRP(map), 3);
    }

    /**
     * 更改消息通知为已读
     * @param id  消息的主键id
     */
    public void modifyReadState(String id){
        String url="message/modifyReadState";
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 5);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            Log.d("demo", "dao中结果集(我的评论): " + result);
            List<ReplyForMe> list = JsonUtil.node2pojoList(result.findValue("replyForMeList"),ReplyForMe.class);
            replyForMes.addAll(list);
        }
        if (requestCode==1){
            Log.d("demo", "dao中结果集(我的评价): " + result);
            assesses = JsonUtil.node2pojoList(result.findValue("orderEvaluationList"),Assess.class);
        }
        if (requestCode==3){
            Log.d("demo", "dao中结果集(个人中心====我的评论): " + result);
            List<ObtainMyReply> list = JsonUtil.node2pojoList(result.findValue("postReplyList"),ObtainMyReply.class);
            obtainMyReplies.addAll(list);
        }
        if (requestCode==4){
            messages = JsonUtil.node2pojoList(result.findValue("messageList"),Message.class);
        }
    }

    public List<ReplyForMe> getReplyForMes() {
        return replyForMes;
    }
    public List<Assess> getAssesses() {
        return assesses;
    }
    public List<ObtainMyReply> getObtainMyReplies() {
        return obtainMyReplies;
    }
    public List<Message> getMessages() {
        return messages;
    }
}
