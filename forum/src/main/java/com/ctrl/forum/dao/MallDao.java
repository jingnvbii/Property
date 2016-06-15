package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.entity.CashCoupons;
import com.ctrl.forum.entity.Company;
import com.ctrl.forum.entity.CompanyUnion;
import com.ctrl.forum.entity.Image2;
import com.ctrl.forum.entity.Mall;
import com.ctrl.forum.entity.MallKind;
import com.ctrl.forum.entity.MallRecommend;
import com.ctrl.forum.entity.Notice;
import com.ctrl.forum.entity.NoticeImage;
import com.ctrl.forum.entity.Product2;
import com.ctrl.forum.entity.ProductCategroy;
import com.ctrl.forum.entity.Qualification2;
import com.ctrl.forum.entity.ShopReply;
import com.ctrl.forum.entity.TRedeemHistory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城 dao
 * Created by jason on 2016/4/19.
 */
public class MallDao extends IDao {




    private List<Banner> listMallBanner = new ArrayList<>();//商家首页轮播图列表
    private List<MallKind> listMallKind= new ArrayList<>();//商家首页频道列表
    private List<MallRecommend> listMallRecommend = new ArrayList<>();//商家首页推荐列表
    private List<Notice> listMallNotice = new ArrayList<>();//商家首页公告列表
    private List<Mall> listMall = new ArrayList<>();//商家推荐列表
    private List<Qualification2> listQualification=new ArrayList<>();//店铺资质列表
    private List<CompanyUnion> listCompanyUnion=new ArrayList<>();//店铺联合商家列表
    private List<CashCoupons> listCashCoupons=new ArrayList<>();//店铺现金券列表
    private List<ShopReply> listShopReply=new ArrayList<>();//店铺回复列表
    private Company company;//店铺
    private String remark;//现金券说明信息
    private List<ShopReply> listAllEvaluation=new ArrayList<>();//所有店铺评价列表
    private List<ShopReply> listPraise=new ArrayList<>();//店铺好评列表
    private List<ShopReply> listMedium=new ArrayList<>();//店铺中评列表
    private List<ShopReply> listBad=new ArrayList<>();//店铺差评列表
    private List<Product2> listProduct=new ArrayList<>();//商品列表
    private List<Image2> listProductImg=new ArrayList<>();//商品图片列表
    private List<TRedeemHistory> listTRedeemHistory=new ArrayList<>();//积分兑换商品历史记录列表
    private List<ProductCategroy>listProductCategroy=new ArrayList();//商品分类列表
    private Product2 product2;
    private List<NoticeImage> listNoticeImage=new ArrayList<>();//公告图片
    private List<Product2> listProducts=new ArrayList<>();


    public MallDao(INetResult activity){
        super(activity);
    }

    /**
     * 商城首页初始化
     * */
    public void requestInitMall(){
        String url="product/initMall";
        Map<String,String> map = new HashMap<String,String>();
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }
    /**
     * 商城首页初始化2  获取推荐商家列表
     * @param latitude //纬度
     * @param longitude //经度
     * @param pageSize //每页几条
     * @param pageNum //第几页
     *
     * */
    public void requestInitMallRecommendCompany(String latitude,String longitude,String pageSize,String pageNum){
        String url="product/initMallRecommendCompany";
        Map<String,String> map = new HashMap<String,String>();
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 1);
    }
    /**
     * 获取店铺详情接口
     * @param memberId //当前用户登录id
     * @param id //店铺id
     *
     * */
    public void requestCompanysDetails(String memberId,String id){
        String url="companys/getCompanysDeatils";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("id",id);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 002);
    }
    /**
     * 获取店铺评价列表接口
     * @param companyId //商铺id
     * @param pageNum //第几页
     * @param pageSize //每页几条
     *
     * */
    public void requestCompanysEvaluate(String companyId,String pageNum,String pageSize){
        String url="companys/getEvaluate";
        Map<String,String> map = new HashMap<String,String>();
        map.put("companyId",companyId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 3);
    }
    /**
     * 获取商品/积分商品接口
     * @param id //商品id
     * @param memberId //用户id
     * @param productKind //获取类别（0：商城商品、1：积分兑换商品）
     *
     * */
    public void requestProduct(String id,String memberId,String productKind){
        String url="product/getProduct";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        map.put("memberId",memberId);
        map.put("productKind",productKind);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 4);
    }
    /**
     * 根据店铺分类（频道）获取店铺列表接口
     * @param sortType //排序类型:默认排序:0   评价最高:1   销量最高:2 距离最近:3
     * @param latitude //纬度
     * @param longitude //经度
     * @param companyKindId //频道id,  全部时为空
     * @param pageNum //当前页码
     * @param pageSize //每页条数
     *
     * */
    public void requestCompanyByKind(String sortType,
                               String latitude,
                               String longitude,
                               String companyKindId,
                               String pageNum,
                               String pageSize){
        String url="companys/getCompanysByKind";
        Map<String,String> map = new HashMap<String,String>();
        map.put("sortType",sortType);
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("companyKindId",companyKindId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 5);
    }

    /**
     * 根据关键字搜索商家或商品列表接口
     * @param sortType //排序类型:默认排序:0   评价最高:1   销量最高:2 距离最近:3
     * @param latitude //纬度
     * @param longitude //经度
     * @param type //0:查询商家    1:查询商品
     * @param keyword //关键字
     * @param memberId //会员id ( 有就传, 没有就不传)
     * @param pageNum //当前页码
     * @param pageSize //每页条数
     *
     * */
    public void requestSearchCompanysOrProductByKeyword(String sortType,
                               String latitude,
                               String longitude,
                               String type,
                               String keyword,
                               String memberId,
                               String pageNum,
                               String pageSize){
        String url="companys/searchCompanysOrProductByKeyword";
        Map<String,String> map = new HashMap<String,String>();
        map.put("sortType",sortType);
        map.put("latitude",latitude);
        map.put("longitude",longitude);
        map.put("type",type);
        map.put("keyword",keyword);
        map.put("memberId",memberId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map),6);
    }
    /**
     * 兑换积分商品
     * @param memberId //用户id
     * @param productId //商品id
     *
     * */
    public void requestExchangeIntegralProduct(String memberId,
                               String productId){
        String url="MemberIntegral/exchangeIntegralProduct";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("productId",productId);
        postRequest(Constant.RAW_URL + url, mapToRP(map),7);
    }
    /**
     * 兑换积分商品历史记录
     * @param pageNum //用户id
     * @param pageSize //商品id
     * @param memberId //商品id
     *
     * */
    public void requestExchangeIntegralProductHistory(String pageNum,
                                               String pageSize,
                               String memberId){
        String url="memberIntegral/exchangeIntegralProductHistory";
        Map<String,String> map = new HashMap<String,String>();
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        map.put("memberId",memberId);
        postRequest(Constant.RAW_URL + url, mapToRP(map),8);
    }
    /**
     * 获取店铺商品分类以及分类下的商品列表接口
     * @param companyId //商家id
     *
     * */
    public void requestProductCategroy(String companyId
                             ){
        String url="productCategory/getProductCategory";
        Map<String,String> map = new HashMap<String,String>();
        map.put("companyId",companyId);
        postRequest(Constant.RAW_URL + url, mapToRP(map),9);
    }
    /**
     * 搜索店内商品
     * @param companyId //商家id
     * @param keyword //关键字
     * @param pageNum //页数
     * @param pageSize //每页条数
     *
     * */
    public void requestQueryCompanyProducts(String companyId,String keyword,String pageNum,String pageSize
                             ){
        String url="product/queryCompanyProducts";
        Map<String,String> map = new HashMap<String,String>();
        map.put("companyId",companyId);
        map.put("keyword",keyword);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        postRequest(Constant.RAW_URL + url, mapToRP(map),10);
    }





    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            Log.d("demo", "dao中结果集(商城首页初始化返回): " + result);
            listMallBanner = JsonUtil.node2pojoList(result.findValue("bannerList"), Banner.class);
            listMallKind = JsonUtil.node2pojoList(result.findValue("companyCategoryList"), MallKind.class);
            listMallRecommend = JsonUtil.node2pojoList(result.findValue("recommendItemList"), MallRecommend.class);
            listMallNotice = JsonUtil.node2pojoList(result.findValue("noticeList"), Notice.class);
            listNoticeImage = JsonUtil.node2pojoList(result.findValue("noticeImgList"), NoticeImage.class);
        }
        if(requestCode == 1){
            Log.d("demo", "dao中结果集(商城首页商家推荐返回): " + result);
            listMall = JsonUtil.node2pojoList(result.findValue("companysList"), Mall.class);
        }
        if(requestCode == 002){
            Log.d("demo", "dao中结果集(店铺详情返回): " + result);
           listQualification = JsonUtil.node2pojoList(result.findValue("qualification"), Qualification2.class);
         //   listCompanyUnion = JsonUtil.node2pojoList(result.findValue("tCompanyUnion"), CompanyUnion.class);
           listShopReply = JsonUtil.node2pojoList(result.findValue("tOrderEvaluationlist"), ShopReply.class);
           listCashCoupons = JsonUtil.node2pojoList(result.findValue("tCashCouponslist"), CashCoupons.class);
            company=JsonUtil.node2pojo(result.findValue("company"), Company.class);
            remark=result.findValue("remark").asText();
            List<CompanyUnion> data = JsonUtil.node2pojo(result.findValue("tCompanyUnion"), new TypeReference<List<CompanyUnion>>() {
            });
            listCompanyUnion.addAll(data);
        }
        if(requestCode == 3){
            Log.d("demo", "dao中结果集(店铺评价列表返回): " + result);
            listAllEvaluation = JsonUtil.node2pojoList(result.findValue("allEvaluationlist"), ShopReply.class);
            listPraise = JsonUtil.node2pojoList(result.findValue("praiseList"), ShopReply.class);
            listMedium = JsonUtil.node2pojoList(result.findValue("mediumList"), ShopReply.class);
            listBad = JsonUtil.node2pojoList(result.findValue("badList"), ShopReply.class);

        }
        if(requestCode == 4){
            Log.d("demo", "dao中结果集(商品返回): " + result);
            product2 = JsonUtil.node2pojo(result.findValue("product"), Product2.class);
            listProductImg = JsonUtil.node2pojoList(result.findValue("productImgList"), Image2.class);
        }
        if(requestCode == 5){
            Log.d("demo", "dao中结果集(店铺列表返回): " + result);
            listMall = JsonUtil.node2pojoList(result.findValue("companysList"), Mall.class);
        }
        if(requestCode == 6){
            Log.d("demo", "dao中结果集(关键字查询店铺/商家列表返回): " + result);
            listMall = JsonUtil.node2pojoList(result.findValue("companysList"), Mall.class);
            listProduct = JsonUtil.node2pojoList(result.findValue("productList"), Product2.class);
        }
        if(requestCode == 8){
            Log.d("demo", "dao中结果集(积分兑换历史记录返回): " + result);
            listTRedeemHistory = JsonUtil.node2pojoList(result.findValue("tRedeemHistoryList"), TRedeemHistory.class);
        }
        if(requestCode == 9){
            Log.d("demo", "dao中结果集(获取店铺商品分类以及分类下的商品列表返回): " + result);
            List<ProductCategroy> data = JsonUtil.node2pojo(result.findValue("productCategoryList"), new TypeReference<List<ProductCategroy>>() {
            });
            listProductCategroy.addAll(data);
        }

        if(requestCode == 10){
            Log.d("demo", "dao中结果集(店铺内商品返回): " + result);
            listProduct = JsonUtil.node2pojoList(result.findValue("productsList"), Product2.class);
        }


    }

    public List<Banner> getListMallBanner() {
        return listMallBanner;
    }

    public List<Notice> getListMallNotice() {
        return listMallNotice;
    }

    public List<MallRecommend>getListMallRecommend(){
        return listMallRecommend;
    }

    public List<MallKind> getListMallKind() {
        return listMallKind;
    }

    public List<Mall> getListMall() {
        return listMall;
    }

    public List<CashCoupons> getListCashCoupons() {
        return listCashCoupons;
    }

    public List<CompanyUnion> getListCompanyUnion() {
        return listCompanyUnion;
    }

    public List<Qualification2> getListQualification() {
        return listQualification;
    }

    public List<ShopReply> getListShopReply() {
        return listShopReply;
    }

    public Company getCompany() {
        return company;
    }

    public String getRemark() {
        return remark;
    }

    public List<ShopReply> getListAllEvaluation() {
        return listAllEvaluation;
    }

    public List<ShopReply> getListPraise() {
        return listPraise;
    }

    public List<ShopReply> getListMedium() {
        return listMedium;
    }

    public List<ShopReply> getListBad() {
        return listBad;
    }

    public List<Image2> getListProductImg() {
        return listProductImg;
    }

    public List<Product2> getListProduct() {
        return listProduct;
    }

    public List<TRedeemHistory> getListTRedeemHistory() {
        return listTRedeemHistory;
    }

    public List<ProductCategroy> getListProductCategroy() {
        return listProductCategroy;
    }

    public Product2 getProduct2() {
        return product2;
    }

    public List<NoticeImage> getListNoticeImage() {
        return listNoticeImage;
    }

    public List<Product2> getListProducts() {
        return listProducts;
    }
}
