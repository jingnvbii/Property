package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.LevelInfo;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员信息
 * Created by Administrator on 2016/5/16.
 */
public class MemberDao extends IDao {
    private LevelInfo levelInfos = new LevelInfo();

    public MemberDao(INetResult activity) {
        super(activity);
    }

    /**
     * 会员签到
     * @param memberId 会员id
     */
    public void sign(String memberId){
      String url = "signHistory/signHistory";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }

    /**
     * 会员等级信息
     * @param userId 会员id
     */
    public void getLevelInfo(String userId){
        String url = "member/lookLevelInfo";
        Map<String,String> map = new HashMap<>();
        map.put("userId",userId);
        postRequest(Constant.RAW_URL+url,mapToRP(map),1);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
       if (requestCode==0){
          Log.d("demo", "dao中结果集(签到): " + result);
       }
        if (requestCode==1){
            Log.d("demo", "dao中结果集(会员等级信息): " + result);
            levelInfos = JsonUtil.node2pojo(result.findValue("data"), LevelInfo.class);
        }
    }

    public LevelInfo getLevelInfos() {
        return levelInfos;
    }
}
