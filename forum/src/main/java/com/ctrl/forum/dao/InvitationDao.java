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
import com.ctrl.forum.entity.NoticeImage;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private List <Post>listRelateMap=new ArrayList<>();//关联帖子列表
    private List <NoticeImage>listNoticeImage=new ArrayList<>();//帖子公告图片
    private NoticeImage noticeImage;
    //  private ArrayList<PostImage> list=new ArrayList<>();
    private List<Post> minePost = new ArrayList<>();


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
     * @param keyword //搜索关键字
     * @param communityId //搜索关键字
     * @param pageNum   //当前页码
     * @param pageSize  //每页条数
     * */
    public void requestPostListByCategory(String reporterId,String categoryId,String categoryType,String keyword,String communityId,int pageNum,int pageSize){
        String url="post/queryPostListByCategory";
        Map<String,String> map = new HashMap<String,String>();
        map.put("reporterId",reporterId);
        map.put("categoryId",categoryId);
        map.put("categoryType",categoryType);
        map.put("keyword",keyword);
        map.put("communityId",communityId);
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
        String url="post/getPostCategory";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        map.put("grade",grade);
        map.put("categoryType",categoryType);

        postRequest(Constant.RAW_URL+url, mapToRP(map), 2);
    }
    /**
     * 获取帖子详情
    * @param id //当前目录id
    * @param zambiaID //用户id
    * */
    public void requesPostDetail(String id,String zambiaID){
        String url="/post/getPort";
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",id);
        map.put("zambiaID",zambiaID);
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
        String url="postReply/obtainPostReplyList";
        Map<String,String> map = new HashMap<String,String>();
        map.put("postId",postId);
        map.put("timeSortType",timeSortType);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);

        postRequest(Constant.RAW_URL+url, mapToRP(map),5);
    }

    /**
     * 编辑帖子(小区)
     * @param id //帖子id（唯一id标示）
     * @param categoryType //分类类型（0：广场帖子、1：小区帖子）
     * @param communityId  //小区id  (分类为小区帖子时传入)
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
    public void requesPlotPostEditor(String id,
                                     String categoryType,
                                     String communityId,
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
        String url = "post/editorPost";
        map.put("id",id);
        map.put("categoryType",categoryType);
        map.put("communityId",communityId);
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

        postRequest(Constant.RAW_URL+url, mapToRP(map),6);
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
     * @param communityId //分类id
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
     * @param thumbImgPostPicStr //帖子缩略图片串:(url之间用逗号分隔)
     * */
    public void requesReleasePost(String reporterId,
                                 String categoryId,
                                  String communityId,
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
                                 String postImgStr,
                                 String thumbImgPostPicStr
    ){
        String url="post/reportPost";
        Map<String,String> map = new HashMap<String,String>();
        map.put("reporterId",reporterId);
        map.put("categoryId",categoryId);
        map.put("communityId",communityId);
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
        map.put("thumbImgPostPicStr",thumbImgPostPicStr);

        postRequest(Constant.RAW_URL+url, mapToRP(map),7);
    }

    /**
     * 发布帖子(小区发布帖子)
     * @param reporterId //发帖人id（会员id)
     * @param categoryType //分类类型（0：广场帖子、1：小区帖子）
     * @param communityId //小区id (当发小区帖子时,该参数需要)
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
     * @param thumbImgPostPicStr //帖子缩略图片串:(url之间用逗号分隔)
     * */
    public void requesReleasePost(String reporterId,
                                  String communityId,
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
                                  String postImgStr,
                                  String thumbImgPostPicStr
    ){
        String url="post/reportPost";
        Map<String,String> map = new HashMap<String,String>();
        map.put("reporterId",reporterId);
        map.put("communityId",communityId);
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
        map.put("thumbImgPostPicStr",thumbImgPostPicStr);

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
        String url="memberBlackList/memberBlackListAdd";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("personId",personId);

        postRequest(Constant.RAW_URL+url, mapToRP(map),10);
    }
    /**
     *对帖子进行举报接口
     * @param postId //帖子ID
     * @param remark //举报内容
     * @param reporterId //发帖人ID
     * @param createBy //举报人ID
     * */
    public void requePostReport(String postId,String remark,String reporterId,String createBy){
        String url="postReport/postReportAdd";
        Map<String,String> map = new HashMap<String,String>();
        map.put("postId",postId);
        map.put("remark",remark);
        map.put("reporterId",reporterId);
        map.put("createBy",createBy);

        postRequest(Constant.RAW_URL+url, mapToRP(map),11);
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
     *设置点赞/取消点赞
     * @param zambiaType //add是点赞reduce是取消赞
     * @param id //帖子id
     * @param zambiaID//当前操作用户id
     * */
    public void requesZambia(String zambiaType,String id,String zambiaID){
        String url="post/Zambia";
        Map<String,String> map = new HashMap<String,String>();
        map.put("zambiaType",zambiaType);
        map.put("id",id);
        map.put("zambiaID",zambiaID
        );

        postRequest(Constant.RAW_URL+url, mapToRP(map),14);
    }

    /**
     *设置点赞/取消点赞
     * @param zambiaType //add是点赞reduce是取消赞
     * @param id //帖子id
     * @param zambiaID//当前操作用户id
     * @param title//帖子标题
     * @param content//帖子内容(没标题时传入)
     * */
    public void requesZambia(String zambiaType,String id,String zambiaID,String title,String content){
        String url="post/Zambia";
        Map<String,String> map = new HashMap<String,String>();
        map.put("zambiaType",zambiaType);
        map.put("id",id);
        map.put("zambiaID",zambiaID);
        map.put("title",title);
        map.put("content",content);
        postRequest(Constant.RAW_URL + url, mapToRP(map),14);
    }

    /**
     *对帖子进行回复
     注：1、内容类型为“0：文字或者表情”时，帖子内容不能为空。为“1：图片”时，
     回复原图Url串和回复缩略图Url串均不能为空。为“2：语音”时，语音文件Url不能为空。
     2、直接对帖子回复时，帖子id、回复者id和回复内容类型为必须项。
     对评论进行回复时候，评论id、被回复者id、被回复者所在楼层均不可为空。
     *
     * @param postId //帖子id
     * @param reporterId //帖子发布者id
     * @param pid //评论id
     * @param memberId //回复者id
     * @param contentType //内容类型（0：文字或者表情、1：图片、2：语音）
     * @param replyContent //回复内容（文本和表情）
     * @param soundUrl //语音文件Url
     * @param receiverId //被回复者id
     * @param receiverFloor //被回复者所在楼层
     * @param replyImgStr //回复原图Url（多个Url逗号分隔存放）
     * @param replyThumbImgStr //回复缩略图Url（多个Url逗号分隔存放）
     * */
    public void requestReplyPost(String postId,
                                String reporterId,
                                String pid,
                                String memberId,
                                String contentType,
                                String replyContent,
                                String soundUrl,
                                String receiverId,
                                String receiverFloor,
                                String replyImgStr,
                                String replyThumbImgStr){
        String url="postReply/replyPost";
        Map<String,String> map = new HashMap<String,String>();
        map.put("postId",postId);
        map.put("reporterId",reporterId);
        map.put("pid",pid);
        map.put("memberId",memberId);
        map.put("contentType",contentType);
        map.put("replyContent",replyContent);
        map.put("soundUrl",soundUrl);
        map.put("receiverId",receiverId);
        map.put("receiverFloor",receiverFloor);
        map.put("replyImgStr",replyImgStr);
        map.put("replyThumbImgStr",replyThumbImgStr);

        postRequest(Constant.RAW_URL+url, mapToRP(map),15);
    }

    /**
     *收藏帖子
     * @param memberId //会员id
     * @param targerId //帖子id
     * @param osType //收藏来源（0：未知、1：Android、2：IOS、3：WEB）
     * */
    public void requestCollectPost(String memberId,String targerId,String osType){
        String url="memberCollection/collectPost";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("targerId",targerId);
        map.put("osType",osType);

        postRequest(Constant.RAW_URL+url, mapToRP(map),16);
    }
    /**
     *取消收藏帖子
     * @param memberId //会员id
     * @param targerId //帖子id
     * */
    public void requestDeleteCollectPost(String memberId,String targerId){
        String url="memberCollection/deleteCollectionPost";
        Map<String,String> map = new HashMap<String,String>();
        map.put("memberId",memberId);
        map.put("targerId",targerId);

        postRequest(Constant.RAW_URL+url, mapToRP(map),17);
    }


    /**
     * 获取我发布的帖子
     * @param reporterId //会员id/发帖人id
     * @param pageNum //当前页码
     * @param pageSize //每页条数
     * */
    public void queryMyPostList(String reporterId,String pageNum,String pageSize){
        String url="post/queryMyPostList";
        Map<String,String> map = new HashMap<String,String>();
        map.put("reporterId",reporterId);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);

        postRequest(Constant.RAW_URL+url, mapToRP(map),18);
    }


    /**
     * 获取我发布的帖子
     * @param location //轮播图位置KEY(固定：B_POST_MIDDLE)
     * */
    public void requestPostRotaingBanner(String location){
        String url="rotatingBanner/getPostRotatingBanner";
        Map<String,String> map = new HashMap<String,String>();
        map.put("location",location);
        postRequest(Constant.RAW_URL+url, mapToRP(map),19);
    }






    @Override
    public void onRequestSuccess(JsonNode result, int requestCode) throws IOException{
        if(requestCode == 0){
            Log.d("demo", "dao中结果集(帖子初始化返回): " + result);
            listBanner = JsonUtil.node2pojoList(result.findValue("rotatingBannerList"), Banner.class);
            listPostKind = JsonUtil.node2pojoList(result.findValue("postCategoryList"), PostKind.class);
            listRecommend = JsonUtil.node2pojoList(result.findValue("recommendItemList"), Recommend.class);
            listNotice = JsonUtil.node2pojoList(result.findValue("noticeList"), Notice.class);
            listNoticeImage=JsonUtil.node2pojoList(result.findValue("noticeImgList"), NoticeImage.class);
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
            List<Category> data = JsonUtil.node2pojo(result.findValue("category"), new TypeReference<List<Category>>() {
            });
            listCategory.addAll(data);
        }
        if(requestCode == 3){
            String json = JsonUtil.node2json(result);
            JSONObject object= null;
            try {
                object = new JSONObject(json);
                JSONObject personObject = object.getJSONObject("data");

            // 返回json的数组
            JSONArray jsonArray = personObject.getJSONArray("postImgList");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                PostImage person = new PostImage();
                person.setId(jsonObject2.getString("id"));
                person.setImg(jsonObject2.getString("img"));
                person.setThumbImg(jsonObject2.getString("thumbImg"));
                person.setTargetId(jsonObject2.getString("targetId"));

                listPostImage.add(person);
            }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            userInfo=JsonUtil.node2pojo(result.findValue("user"), MemberInfo.class);
            List<Post> data = JsonUtil.node2pojo(result.findValue("relatedMap"), new TypeReference<List<Post>>() {
            });
            listRelateMap.addAll(data);
            post2=JsonUtil.node2pojo(result.findValue("post"), Post2.class);

        }

        if(requestCode == 4){
            Log.d("demo","dao中结果集(获取当前频道下所有帖子列表分类): " + result);
            List<Category> data = JsonUtil.node2pojo(result.findValue("category"),new TypeReference<List<Category>>(){});
            listCategory.addAll(data);
        }
        if(requestCode == 5){
            Log.d("demo","dao中结果集(获取帖子评论列表): " + result);
            List<PostReply2> data = JsonUtil.node2pojo(result.findValue("postReplyList"), new TypeReference<List<PostReply2>>() {
            });
            listPostReply2.addAll(data);
           // listPostImage = JsonUtil.node2pojoList(result.findValue("postReplyImgList"), PostImage.class);
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
        if (requestCode == 18){
            Log.d("demo","dao中结果集(获取我发布的帖子接口): " + result);
            minePost = JsonUtil.node2pojoList(result.findValue("postList"),Post.class);
        }

        if (requestCode == 19){
            Log.d("demo","dao中结果集(获取我帖子轮播图接口): " + result);
            listBanner = JsonUtil.node2pojoList(result.findValue("rotatingBannerList"),Banner.class);
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


    public Post2 getPost2(){
        return post2;
    }
    public MemberInfo getUser(){
        return userInfo;
    }

    public List<Post> getListRelateMap() {
        return listRelateMap;
    }

    public NoticeImage getNoticeImage() {
        return noticeImage;
    }

    public List<NoticeImage> getListNoticeImage() {
        return listNoticeImage;
    }

    public List<Post> getMinePost() {
        return minePost;
    }
}
