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
 * 周围
 * Created by Administrator on 2016/5/9.
 */
public class RimDao extends IDao {

    public RimDao(INetResult activity) {
        super(activity);
    }

    /**
     * 获取周围商家
     * @param memberId 会员id
     */
    public void getRimStore(String memberId){
        String url="aroundService/getAroundServiceCompany";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if (requestCode==0){
            Log.d("demo", "dao中结果集(周围商家): " + result);
        }
    }
}
