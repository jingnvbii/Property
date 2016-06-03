package com.ctrl.forum.dao;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.SearchHistory;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索
 * Created by Administrator on 2016/5/24.
 */
public class SearchDao extends IDao{
   private List<SearchHistory> searchHistory = new ArrayList<>();
    private List<SearchHistory> hotSearch = new ArrayList<>();

    public SearchDao(INetResult activity) {
        super(activity);
    }

    /**
     * 获取我的搜索历史记录
     * @param memberId 会员id
     * @param searchType 搜索类型（0：帖子、1：商品、2：店铺、3：周边服务）
     * @param pageNum 页数
     * @param pageSize 每页条数
     */
    public void getSearchHistoryList(String memberId,String searchType,String pageNum,String pageSize){
        String url="searchHistory/getSearchHistoryList";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("searchType",searchType);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }
    /**
     * 清空我的搜索历史记录
     * @param memberId 会员id
     * @param searchType 搜索类型（0：帖子、1：商品、2：店铺、3：周边服务）
     */
    public void deleteSearchHistory(String memberId,String searchType){
        String url="searchHistory/deleteSearchHistory";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("searchType",searchType);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 1);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if (requestCode==0){
            searchHistory =JsonUtil.node2pojoList(result.findValue("searchHistory"),SearchHistory.class);
            hotSearch = JsonUtil.node2pojoList(result.findValue("hotSearch"),SearchHistory.class);
        }
    }

    public List<SearchHistory> getSearchHistory() {
        return searchHistory;
    }

    public List<SearchHistory> getHotSearch() {
        return hotSearch;
    }
}
