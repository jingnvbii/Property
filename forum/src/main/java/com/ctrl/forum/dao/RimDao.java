package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.CompanyEvaluation;
import com.ctrl.forum.entity.Data;
import com.ctrl.forum.entity.RimServeCategory;
import com.ctrl.forum.entity.RimServiceCompany;
import com.ctrl.forum.entity.RimSeverCompanyDetail;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 周围
 * Created by Administrator on 2016/5/9.
 */
public class RimDao extends IDao {
    private List<CompanyEvaluation> evaluations = new ArrayList<>(); //周边店铺评论列表
    private List<RimServeCategory> rimServeCategory = new ArrayList<>(); //周边服务所有分类
    private List<RimServiceCompany> rimServiceCompanies = new ArrayList<>(); //根据id获取周边商家
    private List<RimSeverCompanyDetail> rimSeverCompanyDetails = new ArrayList<>(); //周边商家详情
    private Data data = new Data();

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

    /**
     * 获取周边服务所有分类
     */
    public void getAroundServiceCategory(){
        String url="aroundService/getAroundServiceCategory";
        Map<String,String> map = new HashMap<>();
        postRequest(Constant.RAW_URL + url, mapToRP(map), 1);
    }

    /**
     * 获取周边商家评论列表
     * @param companyId 商家id
     * @param pageNum 当前页码
     * @param pageSize 每页条数
     */
    public void getcollectAroundCompany(String companyId,String pageNum,String pageSize){
        String url="companyEvaluation/queryAroundCompanyEvaluation";
        Map<String,String> map = new HashMap<>();
        map.put("companyId",companyId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 2);
    }

    /**
     * 对周边服务进行评价
     * @param memberId 会员id
     * @param companyId 周边商家id
     * @param content 评价内容(contentType为0时)
     * @param contentType 内容类型（0：文字或者表情、1：图片、2：语音）
     * @param soundUrl 语音文件URL(contentType为2时)
     * @param imgStr 评价原图url串,多个之间用逗号隔开(contentType为1时)
     * @param thumbImgStr 评价缩略图url串,多个之间用逗号隔开(contentType为1时)
     */
    public void evaluateAroundCompany(String memberId,String companyId,String contentType,String content,String soundUrl,String imgStr,String thumbImgStr){
        String url="companyEvaluation/evaluateAroundCompany";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("companyId",companyId);
        map.put("content",content);
        map.put("contentType",contentType);
        map.put("soundUrl",soundUrl);
        map.put("imgStr",imgStr);
        map.put("thumbImgStr",thumbImgStr);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 6);
    }

    /**
     * 获取帖子、周边服务、小区周边服务分类列表
     * @param categoryType 分类类型（0：广场帖子分类、1：小区帖子分类、2：小区周边服务分类、3：周边服务分类）
     */
    public void getPostCategory(String categoryType){
        String url="post/getPostCategory";
        Map<String,String> map = new HashMap<>();
        map.put("categoryType",categoryType);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 3);
    }

    /**
     * 根据分类id或者关键字获取周边商家列表列表
     * @param pageNum 第几页
     * @param pagesize 一页几条
     * @param memberId 当前用户登录id
     * @param keyword  搜索关键字
     * @param companyCategoryId 周边商家分类id
     * @param latitude 经度
     * @param longitude 纬度
     */
    public void getAroundServiceCompanyList(String pageNum,String pagesize,String memberId,String companyCategoryId,String keyword,String latitude,String longitude){
        String url="aroundService/getAroundServiceCompanyList";
        Map<String,String> map = new HashMap<>();
        map.put("pageNum",pageNum);
        map.put("pagesize",pagesize);
        map.put("memberId",memberId);
        map.put("keyword",keyword);
        map.put("companyCategoryId",companyCategoryId);
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 4);
    }

    /**
     * 获取周边商家详情
     * @param id 周边商家id
     * @param memberId 当前登录id
     */
    public void getAroundServiceCompany(String id,String memberId){
        String url="aroundService/getAroundServiceCompany";
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("memberId",memberId);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 5);
    }

    /**
     * 收藏周边商家
     * @param osType 收藏来源（0：未知、1：Android、2：IOS、3：WEB）
     * @param memberId 会员id
     * @param targerId 周边服务id
     */
   /* public void collectAroundCompany(String osType,String memberId,String targerId){
        String url="memberCollection/collectAroundCompanys";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("targerId",targerId);
        map.put("osType",osType);
        postRequest(Constant.RAW_URL + url, mapToRP(map),6);
    }*/

    /**
     * 取消收藏周边商家
     * @param memberId 会员id
     * @param targerId 周边服务id
     */
    /*public void cancelCollectAroundCompanys(String memberId,String targerId){
        String url="memberCollection/cancelCollectAroundCompanys";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("targerId",targerId);
        postRequest(Constant.RAW_URL + url, mapToRP(map),7);
    }*/


    /**
     * 用户收藏综合接口3[周边服务]（0：商品、1：店铺、2：帖子、3：周边服务）
     * @param memberId 会员id
     * @param type 收藏类型（0：商品、1：店铺、2：帖子、3：周边服务）
     * @param targerId 目标id（帖子id、商品id、店铺id或者周边服务id）
     * @param collectType 收藏状态（0-收藏 1-取消收藏）
     */
    public void memberCollect(String memberId,String type,String targerId,String collectType){
        String url="memberCollection/memberCollect";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("type",type);
        map.put("targerId",targerId);
        map.put("collectType",collectType);
        postRequest(Constant.RAW_URL + url, mapToRP(map),7);
    }

    /**
     * 获取收藏周边商家收藏列表
     * @param memberId 会员id
     * @param pageNum 页数
     * @param pageSize 每页条数
     */
    public void getAroundServiceCollectionList(String memberId,String pageNum,String pageSize){
        String url="memberCollection/getAroundServiceCollectionList";
        Map<String,String> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map),8);
    }

    /**
     * 增加周边商家拨打记录
     * @param id 周边服务id
     * @param telephone 联系电话
     * @param memberId 会员id
     */
    public void addCallHistory(String id,String telephone,String memberId){
        String url="aroundService/addCallHistory";
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("telephone",telephone);
        map.put("memberId",memberId);
        postRequest(Constant.RAW_URL + url, mapToRP(map),9);
    }

    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if (requestCode==0){
            Log.d("demo", "dao中结果集(周围商家): " + result);
        }
        if (requestCode==1){
            Log.d("demo", "dao中结果集(获取周边服务所有分类): " + result);
            rimServeCategory = JsonUtil.node2pojoList(result.findValue("category"), RimServeCategory.class);

        }
        if (requestCode==2){
            Log.d("demo", "dao中结果集(获取周边商家评论列表): " + result);
            List<CompanyEvaluation> list = JsonUtil.node2pojo(result.findValue("companyEvaluationList"), new TypeReference<List<CompanyEvaluation>>(){});
            evaluations.addAll(list);
        }
        if (requestCode==4){
            List<RimServiceCompany> list = JsonUtil.node2pojoList(result.findValue("aroundServiceCompany"), RimServiceCompany.class);
            rimServiceCompanies.addAll(list);
        }
        if (requestCode==5){
            Log.d("demo", "dao中结果集(获取周边商家详情): " + result);
            data = JsonUtil.node2pojo(result.findValue("data"), Data.class);
            List<RimSeverCompanyDetail> list =JsonUtil.node2pojo(result.findValue("aroundServiceCompany"), new TypeReference<List<RimSeverCompanyDetail>>(){});
            rimSeverCompanyDetails.addAll(list);
        }
        if (requestCode==8){
            List<RimServiceCompany> list = JsonUtil.node2pojoList(result.findValue("aroundServiceCollectionList"), RimServiceCompany.class);
            rimServiceCompanies.addAll(list);
        }
    }

    public List<CompanyEvaluation> getEvaluations() {
        return evaluations;
    }

    public List<RimServeCategory> getRimServeCategory() {
        return rimServeCategory;
    }

    public List<RimServiceCompany> getRimServiceCompanies() {
        return rimServiceCompanies;
    }

    public List<RimSeverCompanyDetail> getRimSeverCompanyDetails() {
        return rimSeverCompanyDetails;
    }

    public Data getData() {
        return data;
    }
}
