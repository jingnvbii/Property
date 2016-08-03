package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.ctrl.forum.base.Constant;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 设置
 * Created by Administrator on 2016/5/11.
 */
public class SetDao extends IDao{
    public SetDao(INetResult activity) {
        super(activity);
    }

    /**
     * 提交意见反馈
     * @param memberId 会员号
     * @param content 反馈内容
     * @param mobile 会员联系电话
     */
    public void opinionFeedback(String memberId,String content,String mobile){
        String url="memberFeedback/opinionFeedback";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("content",content);
        map.put("mobile",mobile);
        postRequest(Constant.RAW_URL + url, mapToRP(map),0);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
       if (requestCode==0){
           Log.d("demo", "dao中结果集(提交意见反馈): " + result);
       }
    }

    @Override
    public void onRequestFails(String result, int requestCode, String errorNo) {

    }

}
