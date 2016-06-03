package com.ctrl.forum.dao;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.ctrl.forum.base.Constant;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 店铺
 * Created by Administrator on 2016/5/26.
 */
public class StoreManageDao extends IDao {
    public StoreManageDao(INetResult activity) {
        super(activity);
    }

    /**
     * 开店申请
     * @param memberId 会员id
     * @param contactAddress 申请人地址
     * @param contactName 申请人姓名
     * @param contactTelephone 申请人电话
     * @param name 申请人商铺姓名
     */
    public void requestChangeBasicInfo(String memberId,String contactAddress,String contactName,String contactTelephone,String name){
        String url="companys/createCompany";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("contactAddress",contactAddress);
        map.put("contactName",contactName);
        map.put("contactTelephone",contactTelephone);
        map.put("name",contactTelephone);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }
    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {

    }
}
