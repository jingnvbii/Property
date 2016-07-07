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
    /**
     * 获取共通分类列表（或者下拉列表）用户获取店铺分类以及类似的下拉列表
     * @param memberId //当前用户登录id
     * @param type //收藏类型（0：商品、1：店铺、2：帖子、3：周边服务）
     * @param targerId //目标id（帖子id、商品id、店铺id或者周边服务id）
     * @param collectType //收藏状态（0-收藏 1-取消收藏）
     *
     * */
    public void requestMemberCollect(String memberId,String type,String targerId,String collectType){
        String url="memberCollection/memberCollect";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("type",type);
        map.put("targerId",targerId);
        map.put("collectType",collectType);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 666);
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
           companyCollects = JsonUtil.node2pojoList(result.findValue("companysCollectionList"), CompanyCollect.class);
       }
        if (requestCode==2){
            Log.d("demo", "dao中结果集(获取帖子收藏): " + result);
            collectionPost = JsonUtil.node2pojoList(result.findValue("collectionPost"), CollectionPost.class);
        }

        if(requestCode == 666){
            Log.d("demo","dao中结果集(收藏返回): " + result);
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

