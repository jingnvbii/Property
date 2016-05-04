package com.ctrl.android.yinfeng.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.android.yinfeng.base.Constant;
import com.ctrl.android.yinfeng.entity.Img;
import com.ctrl.android.yinfeng.entity.Notice;
import com.ctrl.android.yinfeng.http.AopUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知公告
 * Created by Administrator on 2015/10/28.
 */
public class NoticeDao extends IDao {
    private List<Notice> noticeList= new ArrayList<>();
    private List<Img> imgList= new ArrayList<>();
    private Notice notice;
    private int count;
    private List<Img> propertyNoticePicList;

    public List<Notice> getNoticeList(){
        return noticeList;
    }
    public Notice getNotice(){
        return notice;
    }
    public List<Img> getPropertyNoticePicList() {
        return propertyNoticePicList;
    }
    public NoticeDao(INetResult activity) {
        super(activity);
    }

    public int getCount() {
        return count;
    }


    /**
     * 获取内部通知公告列表
     * @param communityId 社区ID
     * @param activityId 员工id
     */
    public void requestNoticeList(String communityId,String activityId,String currentPage,String rowCountPerPage) {
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        map.put(Constant.SECRET, Constant.SECRET_VALUE);
        map.put(Constant.VERSION, Constant.VERSION_VALUE);
        map.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.propertyNotice.list");
        map.put("communityId", communityId);
        map.put("activityId", activityId);
        map.put("currentPage", currentPage);
        map.put("rowCountPerPage", rowCountPerPage);

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 0);
    }

    /**
     * 获取内部通知公告详情
     * @param propertyNoticeId 通知公告ID
     * */
    public void requestNotice(String propertyNoticeId,String activityId) {
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        map.put(Constant.SECRET, Constant.SECRET_VALUE);
        map.put(Constant.VERSION, Constant.VERSION_VALUE);
        map.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.propertyNotice.propertyNoticeInfo");

        map.put("propertyNoticeId", propertyNoticeId);
        map.put("activityId", activityId);

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);

        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 1);
    }

    /**
     * 更改公告状态为已读
     * @param activityId 员工id
     * @param noticeId 通知公告ID
     */
    public void requestNoticeModify(String activityId,String noticeId) {
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        map.put(Constant.SECRET, Constant.SECRET_VALUE);
        map.put(Constant.VERSION, Constant.VERSION_VALUE);
        map.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.memberNoticeRelation.insert");

        map.put("activityId", activityId);
        map.put("noticeId", noticeId);

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 2);
    }
    /**
     * 查询未读公告数量
     * @param communityId 社区ID
     * @param activityId 员工id
     */
    public void requestNoticeUnreadCount(String communityId,String activityId) {
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        map.put(Constant.SECRET, Constant.SECRET_VALUE);
        map.put(Constant.VERSION, Constant.VERSION_VALUE);
        map.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        map.put(Constant.TYPE, Constant.TYPE_VALUE);
        map.put(Constant.METHOD,"pm.stf.propertyNotice.queryPropertyNoticeNum");

        map.put("communityId", communityId);
        map.put("activityId", activityId);

        String sign = AopUtils.sign(map, Constant.SECRET_VALUE);
        map.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(map), 3);
    }




    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            Log.d("demo", "dao中结果集(公告列表): " + result);
            List<Notice> data = JsonUtil.node2pojoList(result.findValue("rtnPropertyNoticeList"), Notice.class);
            noticeList.addAll(data);
        }
        if(requestCode == 1){
            Log.d("demo", "dao中结果集(公告详细): " + result);
            notice = JsonUtil.node2pojo(result.findValue("rtnPropertyNoticeInfo"), Notice.class);
            propertyNoticePicList = JsonUtil.node2pojoList(result.findValue("propertyNoticePicList"), Img.class);
        }
        if(requestCode == 2){
            Log.d("demo", "dao中结果集(签收公告): " + result);
            //notice = JsonUtil.node2pojo(result.findValue("propertyNoticeInfo"), Notice.class);
        }
        if(requestCode == 3){
            Log.d("demo", "dao中结果集(公告详细): " + result);
            String str = result.findValue("propertyNoticeNum").asText();
            count=Integer.parseInt(str);

        }
    }
}
