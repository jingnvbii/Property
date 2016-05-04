package com.ctrl.android.yinfeng.dao;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.android.yinfeng.base.Constant;
import com.ctrl.android.yinfeng.entity.Img;
import com.ctrl.android.yinfeng.entity.Report;
import com.ctrl.android.yinfeng.http.AopUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件上报
 * Created by jaosn on 2015/12/28.
 */
public class ReportDao extends IDao {
    private List<Report> reportList = new ArrayList<>();
    private Report Report;
    private List<Img> reportPicList = new ArrayList<>();

    public List<Report> getReportList() {
        return reportList;
    }

    public Report getReport() {
        return Report;
    }

    public List<Img> getReportPicList() {
        return reportPicList;
    }


    public ReportDao(INetResult activity) {
        super(activity);
    }
    /**
     * 获取事件上报列表
     * @param communityId 社区ID
     * @param staffId 员工ID
     * @param handleStatus 处理状态（0：待处理、1：处理中、2：已结束）
     * @param currentPage
     * @param eventKindId 事件分类id（传空值时查询全部类型）
     */
    public void requestReportList(String communityId,String staffId,String handleStatus,String currentPage,String eventKindId) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        params.put(Constant.SECRET, Constant.SECRET_VALUE);
        params.put(Constant.VERSION, Constant.VERSION_VALUE);
        params.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        params.put(Constant.TYPE, Constant.TYPE_VALUE);
        params.put("method","pm.stf.EventReport.list");
        params.put("communityId", communityId);
        params.put("staffId", staffId);
        params.put("handleStatus", handleStatus);
        params.put("currentPage", currentPage);
        params.put("rowCountPerPage", "20");
        params.put("eventKindId", eventKindId);
        String sign = AopUtils.sign(params, Constant.SECRET_VALUE);
        params.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(params), 0);
    }

    /**
     * 获取事件上报详情
     * @param eventReportId 事件上报id
     */
    public void requestReportDetail(String eventReportId) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        params.put(Constant.SECRET, Constant.SECRET_VALUE);
        params.put(Constant.VERSION, Constant.VERSION_VALUE);
        params.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        params.put(Constant.TYPE, Constant.TYPE_VALUE);
        params.put("method","pm.stf.EventReport.selContent");
        params.put("eventReportId", eventReportId);
        String sign = AopUtils.sign(params, Constant.SECRET_VALUE);
        params.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(params), 1);
    }


    /**
     * 事件上报添加
     * @param communityId 社区id
     * @param staffId 员工id
     * @param title 事件标题
     * @param eventKindId 事件类型id
     * @param content 事件内容
     * @param eventReportPicUrl1 事件上报图片Url1（原图Url + “,”+ 缩略图Url）
     * @param eventReportPicUrl2 事件上报图片Url2（原图Url + “,”+ 缩略图Url）
     * @param eventReportPicUrl3 事件上报图片Url3（原图Url + “,”+ 缩略图Url）
     * @param eventReportPicUrl4 事件上报图片Url4（原图Url + “,”+ 缩略图Url）
     */
    public void requestReportAdd(String communityId,String staffId,String title,String eventKindId,
                                 String content,
                                 String eventReportPicUrl1,
                                 String eventReportPicUrl2,
                                 String eventReportPicUrl3,
                                 String eventReportPicUrl4
    ) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        params.put(Constant.SECRET, Constant.SECRET_VALUE);
        params.put(Constant.VERSION, Constant.VERSION_VALUE);
        params.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        params.put(Constant.TYPE, Constant.TYPE_VALUE);
        params.put("method","pm.stf.EventReport.insert");
        params.put("communityId", communityId);
        params.put("staffId", staffId);
        params.put("title", title);
        params.put("eventKindId", eventKindId);
        params.put("content", content);
        params.put("eventReportPicUrl1", eventReportPicUrl1);
        params.put("eventReportPicUrl2", eventReportPicUrl2);
        params.put("eventReportPicUrl3", eventReportPicUrl3);
        params.put("eventReportPicUrl4", eventReportPicUrl4);
        String sign = AopUtils.sign(params, Constant.SECRET_VALUE);
        params.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(params), 2);
    }



    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            List<Report> data = JsonUtil.node2pojoList(result.findValue("eventReportList"), Report.class);
            reportList.addAll(data);
        }

        if(requestCode == 1){
            Report = JsonUtil.node2pojo(result.findValue("eventReportContent"), Report.class);
            reportPicList = JsonUtil.node2pojoList(result.findValue("eventReportPicList"), Img.class);
        }
    }
}
