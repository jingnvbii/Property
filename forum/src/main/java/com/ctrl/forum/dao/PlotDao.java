package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.Category2;
import com.ctrl.forum.entity.Communitys;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.RimServiceCompany;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小区
 * Created by Administrator on 2016/5/13.
 */
public class PlotDao extends IDao {
    private List<Communitys> communities = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();
    private List<Post> searchPost = new ArrayList<>();
    private List<Category2> category2 = new ArrayList<>(); //小区_周边服务
    private List<RimServiceCompany> rimServiceCompanies = new ArrayList<>();
    private List<Post> plotPost = new ArrayList<>();

    public PlotDao(INetResult activity) {
        super(activity);
    }

    /**
     * 获取小区列表
     * @param longitude 经度
     * @param latitude 纬度
     * @param pageNum 当前页
     * @param pageSize 每页条数
     */
    public void getPlot(String longitude,String latitude,String pageNum,String pageSize){
        String url="community/queryCommunityList";
        Map<String,String> map = new HashMap<>();
        map.put("longitude",longitude);
        map.put("latitude",latitude);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }

    /**
     * 加入小区
     * @param memberId 经度
     * @param communityId 纬度
     */
    public void joinCel(String memberId,String communityId){
        String url="member/JoinCell";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("communityId",communityId);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 1);
    }

    /**
     * 小区首页初始化
     * @param memberId 会员id
     * @param id 小区id
     */
    public void initCommunity(String memberId,String id,String pageNum,String pageSize){
        String url="community/initCommunity";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("id",id);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 2);
    }

    /**
     * 获取小区帖子列表
     * @param reporterId 会员id,发帖人id
     * @param communityId 小区id
     */
    public void queryCommunityPostList(String reporterId,String communityId,String pageNum,String pageSize){
        String url="post/queryCommunityPostList";
        Map<String,String> map = new HashMap<>();
        map.put("reporterId",reporterId);
        map.put("communityId",communityId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 6);
    }

    /**
     * 根据分类获取小区帖子列表
     * @param reporterId 会员id
     * @param categoryType 分类类型（0：广场帖子、1：小区帖子）
     * @param communityId 当分类类型为1时, 小区id需要传
     * @param keyword 搜索关键字
     * @param pageNum 当前页
     * @param pageSize 每页条数
     */
    public void queryPostList(String reporterId,String categoryType,String keyword,String communityId,String pageNum,String pageSize){
        String url="post/queryPostListByCategory";
        Map<String,String> map = new HashMap<>();
        map.put("reporterId",reporterId);
        map.put("categoryType",categoryType);
        map.put("keyword",keyword);
        map.put("communityId",communityId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map),3);
    }

    /**
     * 小区周边服务分类列表
     * @param communityId 小区id
     * @param categoryType 分类类型（0：广场帖子分类、1：小区帖子分类、2：小区周边服务分类、3：周边服务分类）
     */
    public void getPostCategory(String communityId,String categoryType){
        String url="post/getPostCategory";
        Map<String,String> map = new HashMap<>();
        map.put("communityId",communityId);
        map.put("categoryType",categoryType);
        postRequest(Constant.RAW_URL + url, mapToRP(map),4);
    }


    /**
     * 获取周边商家列表
     * @param pageNum 第几页
     * @param pagesize 一页几条
     * @param memberId 当前用户登录id
     * @param companyCategoryId 周边商家分类id
     * @param latitude 经度
     * @param longitude 纬度
     */
    public void getAroundServiceCompanyList(String pageNum,String pagesize,String memberId,String companyCategoryId,String latitude,String longitude){
        String url="aroundService/getAroundServiceCompanyList";
        Map<String,String> map = new HashMap<>();
        map.put("pageNum",pageNum);
        map.put("pagesize",pagesize);
        map.put("memberId",memberId);
        map.put("companyCategoryId",companyCategoryId);
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        postRequest(Constant.RAW_URL + url, mapToRP(map),5);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if (requestCode==0){
            Log.d("demo", "dao中结果集(获取小区列表): " + result);
            List<Communitys> co= JsonUtil.node2pojoList(result.findValue("communityList"), Communitys.class);
            communities.addAll(co);
        }
        if (requestCode==1){
            Log.d("demo", "dao中结果集(加入小区):" + result);
        }
        if (requestCode==2){
            Log.d("demo", "dao中结果集(小区首页初始化):" + result);
            postList = JsonUtil.node2pojoList(result.findValue("postList"),Post.class);
        }
        if (requestCode==3){
            Log.d("demo", "dao中结果集(搜索栏目内容):" + result);
            searchPost = JsonUtil.node2pojoList(result.findValue("postList"),Post.class);
        }
        if (requestCode==4){
            Log.d("demo", "dao中结果集(小区周边服务分类):" + result);
            category2 = JsonUtil.node2pojoList(result.findValue("category"),Category2.class);
        }
        if (requestCode==5){
            Log.d("demo", "dao中结果集(小区周边服务分类列表):" + result);
            List<RimServiceCompany> list = JsonUtil.node2pojoList(result.findValue("aroundServiceCompany"),RimServiceCompany.class);
            rimServiceCompanies.addAll(list);
        }
        if (requestCode==6){
            List<Post> data = JsonUtil.node2pojo(result.findValue("postList"), new TypeReference<List<Post>>() {
            });
            plotPost.addAll(data);
        }
    }

    public List<Communitys> getCommunities() {
        return communities;
    }
    public List<Post> getPostList() {
        return postList;
    }
    public List<Post> getSearchPost() {
        return searchPost;
    }
    public List<Category2> getCategory2() {
        return category2;
    }

    public List<RimServiceCompany> getRimServiceCompanies() {
        return rimServiceCompanies;
    }

    public List<Post> getPlotPost() {
        return plotPost;
    }
}
