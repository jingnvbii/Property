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
 * 收藏共通接口 dao
 * Created by jason on 2015/10/28.
 */
public class CollectDao extends IDao {


    public CollectDao(INetResult activity){
        super(activity);
    }

    /**
     * 获取共通分类列表（或者下拉列表）用户获取店铺分类以及类似的下拉列表
     * @param memberId //当前用户登录id
     * @param type //收藏类型（0：商品、1：店铺、2：帖子、3：周边服务）
     * @param companyId //商铺id(当收藏商品是填写，商品所属店铺Id)
     * @param targerId //目标id（帖子id、商品id、店铺id或者周边服务id）
     * @param collectType //收藏状态（0-收藏 1-取消收藏）
     *
     * */
    public void requestMemberCollect(String memberId,String type,String companyId,String targerId,String collectType){
        String url="memberCollection/memberCollect";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("type",type);
        map.put("companyId",companyId);
        map.put("targerId",targerId);
        map.put("collectType",collectType);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 666);
    }


    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 666){
            Log.d("demo","dao中结果集(收藏返回): " + result);
        }




    }

}
