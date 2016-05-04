package com.ctrl.android.yinfeng.dao;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.android.yinfeng.base.Constant;
import com.ctrl.android.yinfeng.entity.Img;
import com.ctrl.android.yinfeng.entity.Job;
import com.ctrl.android.yinfeng.entity.StaffListInfo;
import com.ctrl.android.yinfeng.http.AopUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报修
 * Created by Administrator on 2015/11/2.
 */
public class JobDao extends IDao {
    private List<Job> repairList = new ArrayList<>();
    private List<StaffListInfo> staffInfoList = new ArrayList<>();
    private Job job;
    private List<Img> repairPicList = new ArrayList<>();
    private List<Img> repairResultPicList = new ArrayList<>();
    private StaffListInfo staffInfo;

    public List<Job> getRepairList() {
        return repairList;
    }
    public List<StaffListInfo> getStaffInfoList() {
        return staffInfoList;
    }

    public Job getRepair() {
        return job;
    }

    public List<Img> getRepairPicList() {
        return repairPicList;
    }

    public List<Img> getRepairResultPicList() {
        return repairResultPicList;
    }

    public JobDao(INetResult activity) {
        super(activity);
    }
    /**
     * 获取报修信息列表
     * @param communityId 社区ID
     * @param staffId 员工ID
     * @param handleStatus 处理状态（0：待处理、1：处理中、2：待评价、3：已结束）
     * @param currentPage
     */
    public void requestRepairList(String communityId,String staffId,String handleStatus,String currentPage) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        params.put(Constant.SECRET, Constant.SECRET_VALUE);
        params.put(Constant.VERSION, Constant.VERSION_VALUE);
        params.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        params.put(Constant.TYPE, Constant.TYPE_VALUE);
        params.put("method","pm.stf.RepairDemand.list");
        params.put("communityId", communityId);
        params.put("staffId", staffId);
        params.put("handleStatus", handleStatus);
        params.put("currentPage", currentPage);
        params.put("rowCountPerPage", "20");
        String sign = AopUtils.sign(params, Constant.SECRET_VALUE);
        params.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(params), 0);
    }

    /**
     * 获取报修信息详情
     * @param repairDemandId 我的工单id
     */
    public void requestRepair(String repairDemandId) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        params.put(Constant.SECRET, Constant.SECRET_VALUE);
        params.put(Constant.VERSION, Constant.VERSION_VALUE);
        params.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        params.put(Constant.TYPE, Constant.TYPE_VALUE);
        params.put("method","pm.stf.RepairDemand.selContent");
        params.put("repairDemandId", repairDemandId);
        String sign = AopUtils.sign(params, Constant.SECRET_VALUE);
        params.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(params), 1);
    }

    /**
     * 员工抢单
     * @param repairDemandId 报修ID
     * @param staffId 维修人员ID
     * @param orderType 报修是否已分配（0：未分配、1：已分配）
     */
    public void requestRepairScrambleBill(String repairDemandId,String staffId,String orderType) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        params.put(Constant.SECRET, Constant.SECRET_VALUE);
        params.put(Constant.VERSION, Constant.VERSION_VALUE);
        params.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        params.put(Constant.TYPE, Constant.TYPE_VALUE);
        params.put("method","pm.stf.RepairDemand.acceptOrder");
        params.put("repairDemandId", repairDemandId);
        params.put("staffId", staffId);
        params.put("orderType", orderType);
        String sign = AopUtils.sign(params, Constant.SECRET_VALUE);
        params.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(params), 2);
    }

    /**
     * 填写物业处理意见
     * @param repairDemandId 报修id
     * @param result 物业处理结果
     * @param feedbackPicStr1 物业处理结果图片string串1
     * @param feedbackPicStr2 物业处理结果图片string串2
     * @param feedbackPicStr3 物业处理结果图片string串3
     */
    public void requestRepairFillResult(String repairDemandId,String result,String feedbackPicStr1,String feedbackPicStr2,String feedbackPicStr3) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        params.put(Constant.SECRET, Constant.SECRET_VALUE);
        params.put(Constant.VERSION, Constant.VERSION_VALUE);
        params.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        params.put(Constant.TYPE, Constant.TYPE_VALUE);
        params.put("method","pm.stf.RepairDemand.feedback");
        params.put("repairDemandId", repairDemandId);
        params.put("result", result);
        params.put("feedbackPicStr1", feedbackPicStr1);
        params.put("feedbackPicStr2", feedbackPicStr2);
        params.put("feedbackPicStr3", feedbackPicStr3);
        String sign = AopUtils.sign(params, Constant.SECRET_VALUE);
        params.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(params), 3);
    }

    /**
     * 对报修进行线性支付
     * @param repairDemandId 报修id
     * @param dealFee 支付金额
     */
    public void requestPaOffLine(String repairDemandId,String dealFee) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        params.put(Constant.SECRET, Constant.SECRET_VALUE);
        params.put(Constant.VERSION, Constant.VERSION_VALUE);
        params.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        params.put(Constant.TYPE, Constant.TYPE_VALUE);
        params.put("method","pm.stf.RepairDemand.cashPayment");
        params.put("repairDemandId", repairDemandId);
        params.put("dealFee", dealFee);
        String sign = AopUtils.sign(params, Constant.SECRET_VALUE);
        params.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(params),4);
    }
    /**
     * 查修员工信息列表哦
     * @param communityId 社区id
     * @param jobType 岗位类别
     * @param currentPage 当前页码
     * @param rowCountPerPage 每页条数
     */
    public void requestStaffInfoList(String communityId,String jobType,String currentPage,String rowCountPerPage) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        params.put(Constant.SECRET, Constant.SECRET_VALUE);
        params.put(Constant.VERSION, Constant.VERSION_VALUE);
        params.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        params.put(Constant.TYPE, Constant.TYPE_VALUE);
        params.put("method","pm.ppt.staff.queryStaffList");
        params.put("communityId", communityId);
        params.put("jobType", jobType);
        params.put("currentPage", currentPage);
        params.put("rowCountPerPage", rowCountPerPage);
        String sign = AopUtils.sign(params, Constant.SECRET_VALUE);
        params.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(params),5);
    }
    /**
     * 对报修进行指派
     * @param repairDemandId 报修id
     * @param staffId 维修人员id
     * @param assignStaffId 员工id
     */
    public void requestAssignOrder(String repairDemandId,String staffId,String assignStaffId) {
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constant.APPKEY, Constant.APPKEY_VALUE);
        params.put(Constant.SECRET, Constant.SECRET_VALUE);
        params.put(Constant.VERSION, Constant.VERSION_VALUE);
        params.put(Constant.FORMAT, Constant.FORMAT_VALUE);
        params.put(Constant.TYPE, Constant.TYPE_VALUE);
        params.put("method","pm.stf.RepairDemand.assignOrder");
        params.put("repairDemandId", repairDemandId);
        params.put("staffId", staffId);
        params.put("assignStaffId", assignStaffId);
        String sign = AopUtils.sign(params, Constant.SECRET_VALUE);
        params.put("sign",sign);
        postRequest(Constant.RAW_URL, mapToRP(params),6);
    }



    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            /*List<Job> data = JsonUtil.node2pojoList(result.findValue("repairDemandList"), Job.class);

            List<RepairKind> repairKind = JsonUtil.node2pojoList(result.findValue("repairKindList"), RepairKind.class);
             repairKindList.addAll(repairKind);*/
            List<Job> data = JsonUtil.node2pojo(result.findValue("repairDemandList"),new TypeReference<List<Job>>(){});
            if(data!=null) {
                repairList.addAll(data);
            }
        }
        if(requestCode == 1){
            job = JsonUtil.node2pojo(result.findValue("repairDemandInfo"), Job.class);
            List<Img> repairPics = JsonUtil.node2pojoList(result.findValue("repairDemandPicList"), Img.class);
           if(null!=repairPics)
            repairPicList.addAll(repairPics);
            List<Img> repairResultPics = JsonUtil.node2pojoList(result.findValue("feedbackPicList"), Img.class);
            if(null!=repairResultPics) {
                repairResultPicList.addAll(repairResultPics);
            }
        }

        if(requestCode==5){
          List<StaffListInfo> staffInfo=JsonUtil.node2pojoList(result.findValue("staffList"), StaffListInfo.class);
          staffInfoList.addAll(staffInfo);
        }

    }
}
