package com.ctrl.forum.dao;

import android.util.Log;
import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.Blacklist;
import com.ctrl.forum.entity.DraftsPostList;
import com.ctrl.forum.entity.MemberInfo;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑个人资料
 * Created by Administrator on 2016/5/6.
 */
public class EditDao extends IDao {
    /**
     * 用户信息
     * */
    private MemberInfo memberInfo=new MemberInfo();
    private List<Blacklist> blacklists = new ArrayList<>();
    private List<DraftsPostList> draftsPostLists = new ArrayList<>();//草稿箱

    public EditDao(INetResult activity) {super(activity);}

    /**
     * 修改个人信息
     * @param nickName 昵称
     * @param remark 个人简介
     * @param mobile 手机号
     * @param memberId 会员id
     */
    public void requestChangeBasicInfo(String nickName,String remark,String mobile,String memberId){
        String url="member/modifyMyDate";
        Map<String,String> map = new HashMap<>();
        map.put("nickName",nickName);
        map.put("remark",remark);
        map.put("mobile",mobile);
        map.put("memberId",memberId);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }

    /**
     * 获取会员基本信息
     * @param id 会员号
     */
    public void getVipInfo(String id){
        String url="member/getMemeberDetails";
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 1);
    }

    /**
     * 获取用户黑名单列表
     * @param memberId 会员号
     * @param pageNum 当前页号
     * @param pageSize 每页几条
     */
    public void getBlackList(String memberId,String pageNum,String pageSize){
        String url="memberBlackList/getMemberBlackList";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("pageNum",pageNum+"");
        map.put("pageSize",pageSize+"");
        postRequest(Constant.RAW_URL + url, mapToRP(map), 2);
    }

    /**
     * 把指定会员从黑名单除去
     * @param memberId 用户id
     * @param personId 被拉黑用户id
     */
    public void cancelBlack(String memberId,String personId){
        String url="memberBlackList/memberBlackListDelete";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("personId",personId);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 3);
    }

    /**
     * 获取草稿箱帖子列表接口
     * @param reporterId 用户id/发帖人id
     */
    public void getDraftsList(String reporterId){
        String url="post/getDraftsList";
        Map<String,String> map = new HashMap<>();
        map.put("reporterId",reporterId);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 4);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
       if (requestCode==0){
           Log.d("demo", "dao中结果集(修改基本信息): " + result);
       }
        if(requestCode==1){
            Log.d("demo","dao中结果集(获得基本信息): " + result);
            memberInfo = JsonUtil.node2pojo(result.findValue("memberInfo"), MemberInfo.class);
        }
        if(requestCode==2){
            Log.d("demo","dao中结果集(获取黑名单列表): " + result);
            blacklists = JsonUtil.node2pojoList(result.findValue("memberBlacklist"), Blacklist.class);
        }
        if(requestCode==3){
            Log.d("demo","dao中结果集(会员取消屏蔽): " + result);
        }
        if(requestCode==4){
            Log.d("demo","dao中结果集(获取草稿箱帖子列表接口): " + result);
            draftsPostLists = JsonUtil.node2pojoList(result.findValue("postList"), DraftsPostList.class);
        }
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }
    public List<Blacklist> getBlacklists() {
        return blacklists;
    }
    public List<DraftsPostList> getDraftsPostLists() {
        return draftsPostLists;
    }
}
