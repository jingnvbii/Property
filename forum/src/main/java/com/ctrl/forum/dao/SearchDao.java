package com.ctrl.forum.dao;

<<<<<<< HEAD
=======
import android.util.Log;

>>>>>>> origin/master
import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
<<<<<<< HEAD
=======
import com.ctrl.forum.entity.HotSearch;
>>>>>>> origin/master
import com.ctrl.forum.entity.SearchHistory;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
<<<<<<< HEAD
 * 搜索
 * Created by Administrator on 2016/5/24.
 */
public class SearchDao extends IDao{
   private List<SearchHistory> searchHistory = new ArrayList<>();
    private List<SearchHistory> hotSearch = new ArrayList<>();

    public SearchDao(INetResult activity) {
=======
 * 搜索 dao
 * Created by jason on 2015/10/28.
 */
public class SearchDao extends IDao {

    private List<SearchHistory> listSearchHistory=new ArrayList<>();//搜索历史记录列表
    private List<HotSearch> listHotSearch=new ArrayList<>();//热门搜索

    public SearchDao(INetResult activity){
>>>>>>> origin/master
        super(activity);
    }

    /**
     * 获取我的搜索历史记录
<<<<<<< HEAD
     * @param memberId 会员id
     * @param searchType 搜索类型（0：帖子、1：商品、2：店铺、3：周边服务）
     * @param pageNum 页数
     * @param pageSize 每页条数
     */
    public void getSearchHistoryList(String memberId,String searchType,String pageNum,String pageSize){
        String url="searchHistory/getSearchHistoryList";
        Map<String,String> map = new HashMap<>();
=======
     * @param memberId //会员id
     * @param searchType //搜索类型（0：帖子、1：商品、2：店铺、3：周边服务）
     * @param pageNum //第几页
     * @param pageSize //每页条数
     *
     * */
    public void requestSearchHistory(String memberId,
                                     String searchType,
                                     String pageNum,
                                     String pageSize){
        String url="searchHistory/getSearchHistoryList";
        Map<String,String> map = new HashMap<String,String>();
>>>>>>> origin/master
        map.put("memberId",memberId);
        map.put("searchType",searchType);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
<<<<<<< HEAD
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
=======
        postRequest(Constant.RAW_URL + url, mapToRP(map), 999);
    }
    /**
     * 获取位置搜索列表
     * @param memberId //会员id
     * @param keyword //关键字
     * @param latitude //经度
     * @param longitude //纬度
     *
     * */
    public void requestAddLoactionSearch(String memberId,
                                     String keyword,
                                     String latitude,
                                     String longitude){
        String url="searchHistory/addLocationSearch";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("keyword",keyword);
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 1001);
    }

    /**
     * 清空我的搜索历史记录
     * @param memberId //会员id
     * @param searchType //搜索类型（0：帖子、1：商品、2：店铺、3：周边服务）
     * */
    public void requestDeleteSearchHistory(String memberId,
                                     String searchType
                                     ){
        String url="searchHistory/deleteSearchHistory";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("searchType",searchType);
        postRequest(Constant.RAW_URL + url, mapToRP(map),1000);
>>>>>>> origin/master
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
<<<<<<< HEAD
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
=======
        if(requestCode == 999){
            Log.d("demo", "dao中结果集(我的搜索历史记录返回): " + result);
            listSearchHistory = JsonUtil.node2pojoList(result.findValue("searchHistory"), SearchHistory.class);
            listHotSearch = JsonUtil.node2pojoList(result.findValue("hotSearch"), HotSearch.class);
        }
        if(requestCode == 1001){
            Log.d("demo", "dao中结果集(位置搜索列表返回): " + result);
        }
    }

    public List<SearchHistory> getListSearchHistory() {
        return listSearchHistory;
    }

    public List<HotSearch> getListHotSearch() {
        return listHotSearch;
>>>>>>> origin/master
    }
}
