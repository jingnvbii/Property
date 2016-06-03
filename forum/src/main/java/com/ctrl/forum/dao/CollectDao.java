package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.CollectionPost;
import com.ctrl.forum.entity.CompanyCollect;
import com.ctrl.forum.entity.ProductCollect;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏
 * Created by Administrator on 2016/5/13.
 */
public class CollectDao extends IDao {
    private List<ProductCollect> productCollects = new ArrayList<>();
    private List<CompanyCollect> companyCollects = new ArrayList<>();
    private List<CollectionPost> collectionPost = new ArrayList<>();
    public CollectDao(INetResult activity) {
        super(activity);
    }

    /**
     * 获取我收藏的商品列表
     * @param memberId 会员id
     * @param pageNum  当前页码
     * @param pageSize  每页条数
     */
    public void productsCollection(String memberId,String pageNum,String pageSize){
        String url="memberCollection/productsCollection";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }

    /**
     * 获取我收藏的店铺列表
     * @param memberId 会员id
     * @param latitude 纬度
     * @param longitude 经度
     * @param pageNum  当前页码
     * @param pageSize  每页条数
     */
    public void companysCollection(String memberId,String latitude,String longitude,String pageNum,String pageSize){
        String url="memberCollection/companysCollection";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("pageNum",pageNum);
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 1);
    }

    /**
     * 获取用户收藏的帖子列表
     * @param memberId 会员id
     * @param pageNum  当前页码
     * @param pageSize  每页条数
     */
    public void getCollectPostList(String memberId,String pageNum,String pageSize){
        String url="memberCollection/getCollectPostList";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 2);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
       if (requestCode==0){
           Log.d("demo", "dao中结果集(获取商品收藏): " + result);
           productCollects = JsonUtil.node2pojoList(result.findValue("productsList"), ProductCollect.class);       }
       if (requestCode==1){
           Log.d("demo", "dao中结果集(获取店铺收藏): " + result);
           companyCollects = JsonUtil.node2pojoList(result.findValue("companysCollectionLis"), ProductCollect.class);
       }
        if (requestCode==2){
            Log.d("demo", "dao中结果集(获取帖子收藏): " + result);
            collectionPost = JsonUtil.node2pojoList(result.findValue("collectionPost"), CollectionPost.class);
        }
    }

    public List<CompanyCollect> getCompanyCollects() {
        return companyCollects;
    }

    public List<ProductCollect> getProductCollects() {
        return productCollects;
    }

    public List<CollectionPost> getCollectionPost() {
        return collectionPost;
    }
}
