package com.ctrl.forum.dao;

import android.util.Log;

import com.beanu.arad.http.IDao;
import com.beanu.arad.http.INetResult;
import com.beanu.arad.utils.JsonUtil;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.entity.Category;
import com.ctrl.forum.entity.CategoryItem;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.Notice;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.Post2;
import com.ctrl.forum.entity.PostDrafts;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.entity.PostKind;
import com.ctrl.forum.entity.PostReply;
import com.ctrl.forum.entity.PostReply2;
import com.ctrl.forum.entity.Recommend;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帖子 dao
 * Created by jason on 2016/4/19.
 */
public class InvitationDao extends IDao {

    private Post post;
    private MemberInfo userInfo;



    private List<Banner> listBanner = new ArrayList<>();//帖子首页轮播图列表
    private List<PostKind> listPostKind= new ArrayList<>();//帖子首页频道列表
    private List<Recommend> listRecommend = new ArrayList<>();//帖子首页推荐列表
    private List<Notice> listNotice = new ArrayList<>();//帖子首页公告列表
    private List<Post> listPost = new ArrayList<>();//帖子列表
    private List<Post> listPost2 = new ArrayList<>();//帖子列表
    private List<PostImage> listPostImage = new ArrayList<>();//帖子图片列表
    private List<PostReply> listPostReply = new ArrayList<>();//帖子回复列表
    private List<Category> listCategory = new ArrayList<>();//帖子分类列表
    private List<MemberInfo> listMemberInfo = new ArrayList<>();//关联帖子用户列表
    private List<PostImage> listRelatePostImage = new ArrayList<>();//关联帖子图片列表
    private List<PostReply2> listPostReply2 = new ArrayList<>();//评论列表
    private List<PostDrafts> listPostDrafts = new ArrayList<>();//草稿箱帖子列表
    private List<CategoryItem> listCategroyItem = new ArrayList<>();//帖子分类级联列表
    private Post2 post2;
    private List<Post> minePost = new ArrayList<>(); //我发布的帖子列表
    private List<PostImage> minePostImage = new ArrayList<>(); //我发布的帖子列表
    private List<PostReply> minePostReply = new ArrayList<>(); //我发布的帖子回复列表


    public InvitationDao(INetResult activity){
        super(activity);
    }

    /**
     * 帖子首页初始化
     * */
    public void requestInitPostHomePage(){
        String url="post/initPostHomePage";
        Map<String,String> map = new HashMap<String,String>();
        postRequest(Constant.RAW_URL + url, mapToRP(map), 0);
    }
    /**
     * 根据分类获取帖子列表
     * @param reporterId //发帖人id（会员id）
     * @param categoryId //帖子分类id
     * @param categoryType //分类类型（0：广场帖子、1：小区帖子）
     * @param pageNum   //当前页码
     * @param pageSize  //每页条数
     * */
    public void requestPostListByCategory(String reporterId,String categoryId,String categoryType,int pageNum,int pageSize){
        String url="post/queryPostListByCategory";
        Map<String,String> map = new HashMap<String,String>();
        map.put("reporterId",reporterId);
        map.put("categoryId",categoryId);
        map.put("categoryType",categoryType);
        map.put("pageNum",String.valueOf(pageNum));
        map.put("pageSize",String.valueOf(pageSize));

        postRequest(Constant.RAW_URL+url, mapToRP(map), 1);
    }
    /**
     * 获取帖子分类列表
     * @param id //当前目录id
     * @param grade //该目录为几级目录（1,2，3）
     * @param categoryType //分类类型（0：广场帖子分类、1：小区帖子分类、2：小区周边服务分类、3：周边服务分类）
     * */
    public void requesPostCategory(String id,String grade,String categoryType){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.METHOD,"post/queryPostListByCategory");//方法名称
        map.put("id",id);
        map.put("grade",grade);
        map.put("categoryType",categoryType);

        postRequest(Constant.RAW_URL, mapToRP(map), 2);
    }
    /**
     * 获取帖子详情
     * @param id //当前目录id
     * */
    public void requesPostDetail(String id){
        String url="/post/getPort";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        postRequest(Constant.RAW_URL + url, mapToRP(map), 3);
    }
    /**
     * 获取当前频道下所有帖子列表分类
     * @param id //帖子id（唯一id标示）
     * */
    public void requesAllPostCategory(String id){
        String url="post/getAllPostCategory";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);

        postRequest(Constant.RAW_URL+url, mapToRP(map),4);
    }
    /**
     * 获取帖子评论列表
     * @param postId //帖子id（唯一id标示）
     * @param timeSortType //评论排序类型（0：时间升序、1：时间降序）注：默认降序排列
     * @param pageNum //当前页码
     * @param pageSize //每页条数
     * */
    public void requesPostReplyList(String postId,String timeSortType,String pageNum,String pageSize){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.METHOD,"postReply/obtainPostReplyList");//方法名称
        map.put("postId",postId);
        map.put("timeSortType",timeSortType);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);

        postRequest(Constant.RAW_URL, mapToRP(map),5);
    }
    /**
     * 编辑帖子
     * @param id //帖子id（唯一id标示）
     * @param categoryId //分类id
     * @param categoryType //分类类型（0：广场帖子、1：小区帖子）
     * @param publishState //发布状态（0：存为草稿、1：已发布）
     * @param checkType //审核类型（0：无需审核、1：需审核)   (当分类id修改后, 该参数必须传 )
     * @param title //标题  (朋友圈样式时不填写)
     * @param content //帖子内容 (帖子标题和内容不能同时为空)
     * @param vcardDisplay //是否显示名片（0：不显示、1：显示）
     * @param contactName //联系人姓名 ( 是否显示名片状态为1时必须传)
     * @param contactAddress //联系地址   ( 是否显示名片状态为1时必须传)
     * @param contactPhone //联系电话   ( 是否显示名片状态为1时必须传)
     * @param locationLongitude //发帖位置_经度
     * @param locationLatitude //发帖位置_纬度
     * @param locationName //位置名称
     * @param delPostPicStr //要删除的图片该参数里面放的是要删除图片的id(多张图片id之间用逗号分隔)
     * @param UploadPostPicStr //要上传的图片该参数里面放的是要上传图片的 url :(多个图片 url之间用逗号分隔)
     * */
    public void requesPostEditor(String id,
                                 String categoryId,
                                 String categoryType,
                                 String publishState,
                                 String checkType,
                                 String title,
                                 String content,
                                 String vcardDisplay,
                                 String contactName,
                                 String contactAddress,
                                 String contactPhone,
                                 String locationLongitude,
                                 String locationLatitude,
                                 String locationName,
                                 String delPostPicStr,
                                 String UploadPostPicStr
    ){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.METHOD,"post/editorPost");//方法名称
        map.put("id",id);
        map.put("categoryId",categoryId);
        map.put("categoryType",categoryType);
        map.put("publishState",publishState);
        map.put("checkType",checkType);
        map.put("title",title);
        map.put("content",content);
        map.put("vcardDisplay",vcardDisplay);
        map.put("contactName",contactName);
        map.put("contactAddress",contactAddress);
        map.put("contactPhone",contactPhone);
        map.put("locationLongitude",locationLongitude);
        map.put("locationLatitude",locationLatitude);
        map.put("locationName",locationName);
        map.put("delPostPicStr",delPostPicStr);
        map.put("UploadPostPicStr",UploadPostPicStr);

        postRequest(Constant.RAW_URL, mapToRP(map),6);
    }

    /**
     * 发布帖子
     * @param reporterId //发帖人id（会员id)
     * @param categoryId //分类id
     * @param categoryType //分类类型（0：广场帖子、1：小区帖子）
     * @param publishState //发布状态（0：存为草稿、1：已发布）
     * @param checkType //审核类型（0：无需审核、1：需审核)   (当分类id修改后, 该参数必须传 )
     * @param title //标题  (朋友圈样式时不填写)
     * @param content //帖子内容 (帖子标题和内容不能同时为空)
     * @param vcardDisplay //是否显示名片（0：不显示、1：显示）
     * @param contactName //联系人姓名 ( 是否显示名片状态为1时必须传)
     * @param contactAddress //联系地址   ( 是否显示名片状态为1时必须传)
     * @param contactPhone //联系电话   ( 是否显示名片状态为1时必须传)
     * @param locationLongitude //发帖位置_经度
     * @param locationLatitude //发帖位置_纬度
     * @param locationName //位置名称
     * @param postImgStr //帖子图片串:(跟帖子图片一一对应,之间用逗号分隔)
     * */
    public void requesReleasePost(String reporterId,
                                 String categoryId,
                                 String categoryType,
                                 String publishState,
                                 String checkType,
                                 String title,
                                 String content,
                                 String vcardDisplay,
                                 String contactName,
                                 String contactAddress,
                                 String contactPhone,
                                 String locationLongitude,
                                 String locationLatitude,
                                 String locationName,
                                 String postImgStr
    ){
        String url="post/reportPost";
        Map<String,String> map = new HashMap<String,String>();
        map.put("reporterId",reporterId);
        map.put("categoryId",categoryId);
        map.put("categoryType",categoryType);
        map.put("publishState",publishState);
        map.put("checkType",checkType);
        map.put("title",title);
        map.put("content",content);
        map.put("vcardDisplay",vcardDisplay);
        map.put("contactName",contactName);
        map.put("contactAddress",contactAddress);
        map.put("contactPhone",contactPhone);
        map.put("locationLongitude",locationLongitude);
        map.put("locationLatitude",locationLatitude);
        map.put("locationName",locationName);
        map.put("postImgStr",postImgStr);

        postRequest(Constant.RAW_URL+url, mapToRP(map),7);
    }



    /**
     * 删除帖子
     * @param id //帖子id（唯一id标示）
     * */
    public void requesDeltePost(String id){
        String url="post/deletePort";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);

        postRequest(Constant.RAW_URL+url, mapToRP(map),8);
    }
    /**
     * 获取草稿箱帖子列表接口
     * @param reporterId //帖子id（唯一id标示）
     * */
    public void requeDraftsList(String reporterId){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.METHOD,"post/getDraftsList");//方法名称
        map.put("reporterId",reporterId);

        postRequest(Constant.RAW_URL, mapToRP(map),9);
    }
    /**
     *把发帖人加入黑名单接口
     * @param memberId //用户ID
     * @param personId //被拉黑用户ID
     * */
    public void requeMemberBlackListAdd(String memberId,String personId){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.METHOD,"MemberBlackList/MemberBlackListAdd");//方法名称
        map.put("memberId",memberId);
        map.put("personId",personId);

        postRequest(Constant.RAW_URL, mapToRP(map),10);
    }
    /**
     *对帖子进行举报接口
     * @param postId //帖子ID
     * @param remark //举报内容
     * @param reporterId //发帖人ID
     * @param createBy //举报人ID
     * */
    public void requePostReport(String postId,String remark,String reporterId,String createBy){
        Map<String,String> map = new HashMap<String,String>();
        map.put(Constant.METHOD,"postReport/postReportAdd");//方法名称
        map.put("postId",postId);
        map.put("remark",remark);
        map.put("reporterId",reporterId);
        map.put("createBy",createBy);

        postRequest(Constant.RAW_URL, mapToRP(map),11);
    }


    /**
     * 获取帖子二级分类级联列表
     * @param id //帖子分类id
     * @param grade //分类级别
     * */
    public void requesItemCategory2(String id,String grade){
        String url="itemCategory/obtainPostCategoryCascadeList";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        map.put("grade",grade);

        postRequest(Constant.RAW_URL+url, mapToRP(map),12);
    }
    /**
     * 获取帖子三级分类级联列表
     * @param id //帖子分类id
     * @param grade //分类级别
     * */
    public void requesItemCategory3(String id,String grade){
        String url="itemCategory/obtainPostCategoryCascadeList";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        map.put("grade",grade);

        postRequest(Constant.RAW_URL+url, mapToRP(map),13);
    }

    /**
     * 获取我发布的帖子列表
     * @param reporterId //发帖人id（会员id）
     * @param pageNum //当前页码
     * @param pageSize //每页条数
     * */
    public void queryMyPostList(String reporterId,String pageNum,String pageSize){
        String url="post/queryMyPostList";
        Map<String,String> map = new HashMap<String,String>();
        map.put("reporterId",reporterId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);

        postRequest(Constant.RAW_URL+url, mapToRP(map),14);
    }


    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException {
        if(requestCode == 0){
            Log.d("demo", "dao中结果集(帖子初始化返回): " + result);
            listBanner = JsonUtil.node2pojoList(result.findValue("rotatingBannerList"), Banner.class);
            listPostKind = JsonUtil.node2pojoList(result.findValue("postCategoryList"), PostKind.class);
            listRecommend = JsonUtil.node2pojoList(result.findValue("recommendItemList"), Recommend.class);
            listNotice = JsonUtil.node2pojoList(result.findValue("noticeList"), Notice.class);
        }

        if(requestCode == 1){
            Log.d("demo","dao中结果集(根据分类获取帖子列表): " + result);
            List<Post> data = JsonUtil.node2pojo(result.findValue("postList"), new TypeReference<List<Post>>() {
            });
            listPost.addAll(data);

           /* listPost = JsonUtil.node2pojoList(result.findValue("postList"), Post.class);
            listPostImage = JsonUtil.node2pojoList(result.findValue("postImgList"), PostImage.class);
            listPostReply = JsonUtil.node2pojoList(result.findValue("postReplyList"), PostReply.class);*/
        }
        if(requestCode == 2){
            Log.d("demo","dao中结果集(获取帖子分类列表): " + result);
            listCategory = JsonUtil.node2pojoList(result.findValue("category"), Category.class);
        }
        if(requestCode == 3){
            Log.d("demo","dao中结果集(获取帖子详情): " + result);
           // post=JsonUtil.node2pojo(result.findValue("post"),Post.class);
            userInfo=JsonUtil.node2pojo(result.findValue("user"), MemberInfo.class);

            //listPostImage = JsonUtil.node2pojoList(result.findValue("postImgList"), PostImage.class);
            listPost = JsonUtil.node2pojoList(result.findValue("rtuRelatedPost"), Post.class);
            listMemberInfo = JsonUtil.node2pojoList(result.findValue("Relateduser"), MemberInfo.class);
            listRelatePostImage = JsonUtil.node2pojoList(result.findValue("RelatedpostImgList"), PostImage.class);
           post2 = JsonUtil.node2pojo(result.findValue("post"),new TypeReference<Post2>(){});

        }

        if(requestCode == 4){
            Log.d("demo","dao中结果集(获取当前频道下所有帖子列表分类): " + result);
           // listCategory = JsonUtil.node2pojoList(result.findValue("category"), Category.class);//可能有问题

            List<Category> data = JsonUtil.node2pojo(result.findValue("category"),new TypeReference<List<Category>>(){});
            listCategory.addAll(data);
        }
        if(requestCode == 5){
            Log.d("demo","dao中结果集(获取帖子评论列表): " + result);
            listPostReply2 = JsonUtil.node2pojoList(result.findValue("postReplyList"), PostReply2.class);
            listPostImage = JsonUtil.node2pojoList(result.findValue("postReplyImgList"), PostImage.class);
        }
        if(requestCode == 9){
            Log.d("demo","dao中结果集(获取草稿箱帖子列表接口): " + result);
            listPostDrafts = JsonUtil.node2pojoList(result.findValue("postList"), PostDrafts.class);
        }
        if(requestCode == 12){
            Log.d("demo","dao中结果集(获取帖子分类级联列表接口): " + result);
            listCategroyItem = JsonUtil.node2pojoList(result.findValue("itemCategoryList"), CategoryItem.class);
        }
        if(requestCode == 13){
            Log.d("demo","dao中结果集(获取帖子分类级联列表接口): " + result);
            listCategroyItem = JsonUtil.node2pojoList(result.findValue("itemCategoryList"), CategoryItem.class);
        }

        if (requestCode==14){
            Log.d("demo","dao中结果集(获取我发布的帖子列表接口): " + result);
            minePost = JsonUtil.node2pojoList(result.findValue("postList"), Post.class);
            minePostImage = JsonUtil.node2pojoList(result.findValue("postImgList"),PostImage.class);
            minePostReply = JsonUtil.node2pojoList(result.findValue("postReplyList"),PostReply.class);
        }

    }



    public List<Banner> getListBanner() {
        return listBanner;
    }
    public List<PostKind> getListPostKind() {
        return listPostKind;
    }
    public List<Recommend> getListRecommend() {
        return listRecommend;
    }
    public List<Notice> getListNotice() {
        return listNotice;
    }
    public List<Post> getListPost() {
        return listPost;
    }
    public List<PostImage> getListPostImage() {
        return listPostImage;
    }
    public List<PostImage> getListRelatePostImage() {
        return listRelatePostImage;
    }
    public List<PostReply> getListPostReply() {
        return listPostReply;
    }
    public List<PostReply2> getListPostReply2() {
        return listPostReply2;
    }
    public List<Category> getListCategory() {
        return listCategory;
    }
    public List<MemberInfo> getListMemberInfo() {
        return listMemberInfo;
    }
    public List<PostDrafts> getListPostDrafts() {
        return listPostDrafts;
    }
    public List<CategoryItem> getListCategroyItem() {
        return listCategroyItem;
    }
    public List<Post> getMinePost() {
        return minePost;
    }
    public List<PostImage> getMinePostImage() {
        return minePostImage;
    }
    public List<PostReply> getMinePostReply() {
        return minePostReply;
    }

    public Post2 getPost2(){
        return post2;
    }
    public MemberInfo getUser(){
        return userInfo;
    }

}
