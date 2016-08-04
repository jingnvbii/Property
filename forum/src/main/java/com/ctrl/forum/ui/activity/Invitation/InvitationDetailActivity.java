package com.ctrl.forum.ui.activity.Invitation;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AndroidUtil;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.base.SetMemberLevel;
import com.ctrl.forum.customview.ImageZoomActivity;
import com.ctrl.forum.customview.ListViewForScrollView;
import com.ctrl.forum.customview.RoundImageView;
import com.ctrl.forum.customview.ShareDialog;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.Post2;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.entity.PostReply2;
import com.ctrl.forum.manager.MediaManager;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.ui.activity.mine.MineDetailActivity;
import com.ctrl.forum.ui.adapter.FriendDetailImageAdapter;
import com.ctrl.forum.ui.adapter.InvitationDetailReply2Adapter;
import com.ctrl.forum.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 *
 * 帖子详情 activity
 * Created by jason on 2016/4/12.
 */
public class InvitationDetailActivity extends AppToolBarActivity implements View.OnClickListener,PlatformActionListener{
    private PopupWindow popupWindow;
    private RelativeLayout rl;
    @InjectView(R.id.title_image)//头像
    RoundImageView title_image;
    @InjectView(R.id.tv_name)//发帖人
    TextView tv_name;
    @InjectView(R.id.tv_detail_title)//发帖标题
    TextView tv_detail_title;
    @InjectView(R.id.tv_detail_time)//发布时间
    TextView tv_detail_time;
    @InjectView(R.id.tv_detail_tel)//发布电话
    TextView tv_detail_tel;
    @InjectView(R.id.tv_contact_address)//发布电话
    TextView tv_contact_address;
    @InjectView(R.id.tv_address)//发布电话
    TextView tv_address;
    @InjectView(R.id.tv_detail_content)//发布内容
    TextView tv_detail_content;
    @InjectView(R.id.tv_friend_style_zan_num)//点赞数量
    TextView tv_friend_style_zan_num;
    @InjectView(R.id.tv_chakan_more)//查看更多
    TextView tv_chakan_more;
    @InjectView(R.id.iv_detail_levlel)//等级
    TextView iv_detail_levlel;
    @InjectView(R.id.iv_friend_style_zan_num)//点赞图片
    ImageView iv_friend_style_zan_num;
    @InjectView(R.id.lv_detail_image)//图片列表
    ListViewForScrollView lv_detail_image;
    @InjectView(R.id.lv_detail_reply)//回复列表
    ListViewForScrollView lv_detail_reply;
    @InjectView(R.id.rl_friend_style_share)//分享11
    RelativeLayout rl_friend_style_share;
    @InjectView(R.id.rl_friend_style_pinglun)//评论
    RelativeLayout rl_friend_style_pinglun;
    @InjectView(R.id.rl_friend_style_more)//更多
    RelativeLayout rl_friend_style_more;
    @InjectView(R.id.rl_friend_style_zan)//点赞
    RelativeLayout rl_friend_style_zan;
    @InjectView(R.id.rl_user)//用户信息
    RelativeLayout rl_user;
    @InjectView(R.id.ll_tel)//电话布局
            LinearLayout ll_tel;
    private InvitationDao idao;
    private Post2 post;
    private MemberInfo user;
    private List<PostImage> listPostImage;
    private FriendDetailImageAdapter mFriendDetailImageAdapter;
    private InvitationDetailReply2Adapter mFriendDetailReplyAdapter;
    private List<PostReply2> listPostReply2;
    private int PAGE_NUM=1;
    private int count=0;

    private Map<Integer,Boolean> isAdd = new HashMap<>();
    private Map<Integer,Integer> text = new HashMap<>();
    private int pariseNum;

    final ArrayList<String>imageUrl=new ArrayList<>();
    private View viewanim;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_detail);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();

        lv_detail_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), ImageZoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imageList",imageUrl);
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                startActivity(intent);
                //AnimUtil.intentSlidIn(this);
            }
        });
        lv_detail_reply.setFocusable(false);
    }

    private void initData() {
        count = 0;
        idao=new InvitationDao(this);
        idao.requesPostDetail(getIntent().getStringExtra("id"), Arad.preferences.getString("memberId"));
        idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", "", "");
        mFriendDetailImageAdapter=new FriendDetailImageAdapter(this);
       // mFriendDetailReplyAdapter=new FriendDetailReplyAdapter(this);
        mFriendDetailReplyAdapter=new InvitationDetailReply2Adapter(this);
        lv_detail_image.setAdapter(mFriendDetailImageAdapter);
        lv_detail_reply.setAdapter(mFriendDetailReplyAdapter);
        lv_detail_reply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Arad.preferences.getString("memberId") == null || Arad.preferences.getString("memberId").equals("")) {
                    startActivity(new Intent(InvitationDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationDetailActivity.this);
                    return;
                }
                Intent intent = new Intent(InvitationDetailActivity.this, InvitationCommentDetaioActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.putExtra("reportid", getIntent().getStringExtra("reportid"));
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationDetailActivity.this);
            }
        });
    }

    private void initView() {
        rl=(RelativeLayout)findViewById(R.id.rl);
        rl_friend_style_zan.setOnClickListener(this);
        rl_friend_style_pinglun.setOnClickListener(this);
        rl_friend_style_share.setOnClickListener(this);
        rl_friend_style_more.setOnClickListener(this);
        tv_chakan_more.setOnClickListener(this);
        rl_user.setOnClickListener(this);
        ll_tel.setOnClickListener(this);

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 14) {
            if (count % 2 == 0) {//奇数次点击1
                if (post.getZambiastate().equals("0")) {
                    //  MessageUtils.showShortToast(this, "取消点赞成功");
                    iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue_shixin);
                    tv_friend_style_zan_num.setText((pariseNum + 1) + "");

                }
                if (post.getZambiastate().equals("1")) {
                    //MessageUtils.showShortToast(this, "点赞成功");
                    iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue);
                    tv_friend_style_zan_num.setText((pariseNum - 1) + "");

                }
            }
            if (count % 2 == 1) {//偶数次点击
                if (post.getZambiastate().equals("0")) {
                    //  MessageUtils.showShortToast(this, "点赞成功");
                    iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue);
                    tv_friend_style_zan_num.setText(pariseNum  + "");



                }
                if (post.getZambiastate().equals("1")) {
                    //  MessageUtils.showShortToast(this, "取消点赞成功");
                    iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue_shixin);
                    tv_friend_style_zan_num.setText(pariseNum+"");
                }
            }
            count++;
        }
        if(requestCode==16){
            MessageUtils.showShortToast(this, "帖子收藏成功");
            popupWindow.dismiss();
        }
        if (requestCode == 10) {
            MessageUtils.showShortToast(this, "拉黑成功");
            popupWindow.dismiss();
        }
        if (requestCode == 11) {
            MessageUtils.showShortToast(this, "帖子举报成功");
            popupWindow.dismiss();
        }
        if(requestCode==5){
            //   MessageUtils.showShortToast(this, "获取评论列表成功");
            if (popupWindow != null) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
            listPostReply2=new ArrayList<>();
            if(idao.getListPostReply2().size()>3){
                tv_chakan_more.setVisibility(View.VISIBLE);
                for(int i=0;i<3;i++){
                    listPostReply2.add(idao.getListPostReply2().get(i));
                }
            }else {
                for(int i=0;i<idao.getListPostReply2().size();i++){
                    listPostReply2.add(idao.getListPostReply2().get(i));
                }
            }
            mFriendDetailReplyAdapter.setList(listPostReply2);
        }
        if(requestCode==3){
            post = idao.getPost2();
            user = idao.getUser();
            listPostImage=idao.getListPostImage();
            if(listPostImage!=null) {
                mFriendDetailImageAdapter.setList(listPostImage);
                for (int i = 0; i < listPostImage.size(); i++) {
                    imageUrl.add(listPostImage.get(i).getImg());
                }
            }
            if (user!= null) {
                if (user.getImgUrl() != null && !user.getImgUrl().equals(""))
                    Arad.imageLoader.load(user.getImgUrl()).placeholder(R.mipmap.default_error).resize(300,300).centerCrop().into(title_image);
                if (user.getNickName() != null) {
                    tv_name.setText(user.getNickName());
                } else {
                    String mobile = user.getUserName();
                    String result = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
                    tv_name.setText(result);
                }
            }else{
                title_image.setImageResource(R.mipmap.image_default);
                tv_name.setText("管理员");
            }

            if(post.getZambiastate().equals("0")){
                iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue);
            }
            if(post.getZambiastate().equals("1")){
                iv_friend_style_zan_num.setImageResource(R.mipmap.zan_blue_shixin);
            }

            tv_detail_time.setText("发布时间：" + TimeUtils.dateTime(post.getPublishTime()));
            tv_detail_title.setText(post.getTitle());
            tv_detail_content.setText(post.getContent());
            tv_detail_tel.setText(post.getContactPhone());

            if(post.getContactAddress()!=null&&!post.getContactAddress().equals("")){
                tv_contact_address.setVisibility(View.VISIBLE);
                tv_contact_address.setText("地址："+post.getContactAddress());
            }

            if(post.getLocationName()!=null){
                tv_address.setText(post.getLocationName());
            }


            if(post.getContactPhone()==null||post.getContactPhone().equals("")){
                ll_tel.setVisibility(View.GONE);
            }else {
                ll_tel.setVisibility(View.VISIBLE);
                tv_detail_tel.setText(post.getContactName());

            }

            pariseNum=post.getPraiseNum();
            tv_friend_style_zan_num.setText(pariseNum+"");
            if (idao.getUser()!=null) {
                String levlel = idao.getUser().getMemberLevel();
                SetMemberLevel.setLevelImage(this,iv_detail_levlel,levlel);
                /*if (levlel != null) {
                    switch (levlel) {
                        case "0":
                            iv_detail_levlel.setImageResource(R.mipmap.vip_icon);
                            break;
                        case "1":
                            iv_detail_levlel.setImageResource(R.mipmap.vip_icon1);
                            break;
                        case "2":
                            iv_detail_levlel.setImageResource(R.mipmap.vip_icon2);
                            break;
                        case "3":
                            iv_detail_levlel.setImageResource(R.mipmap.vip_icon3);
                            break;
                        case "4":
                            iv_detail_levlel.setImageResource(R.mipmap.vip_icon4);
                            break;
                        case "5":
                            iv_detail_levlel.setImageResource(R.mipmap.vip_icon5);
                            break;
                        case "6":
                            iv_detail_levlel.setImageResource(R.mipmap.vip_icon6);
                            break;
                        case "7":
                            iv_detail_levlel.setImageResource(R.mipmap.vip_icon7);
                            break;
                    }
                } else {
                    iv_detail_levlel.setImageResource(R.mipmap.vip_icon);
                }*/
            }else{
                iv_detail_levlel.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {
        TextView tv_title = getmTitle();
        tv_title.setTextColor(getResources().getColor(R.color.text_black));
        return "帖子详情";
    }

    @Override
    public boolean setupToolBarRightButton(ImageView rightButton) {
        rightButton.setImageResource(R.mipmap.more_shixin);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMorePopupwindow(v);
            }
        });
        
        return true;
    }
    /*
     *弹出窗口
      *  */
    private void showMorePopupwindow(View v) {
        View contentView= LayoutInflater.from(this).inflate(R.layout.popupwindow_more,null);

        TextView tv_zhikanlouzhu = (TextView) contentView.findViewById(R.id.tv_zhikanlouzhu);
        TextView tv_daoxu = (TextView) contentView.findViewById(R.id.tv_daoxu);
        TextView tv_pinbizuozhe = (TextView) contentView.findViewById(R.id.tv_pinbizuozhe);
        TextView tv_jubao = (TextView) contentView.findViewById(R.id.tv_jubao);

        tv_zhikanlouzhu.setOnClickListener(this);
        tv_daoxu.setOnClickListener(this);
        tv_pinbizuozhe.setOnClickListener(this);
        tv_jubao.setOnClickListener(this);


        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
      //  popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(dw);

        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        popupWindow.setTouchable(true);
         /*
        * 设置popupwindow 点击自身消失
        * */
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow.setOutsideTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 设置好参数之后再show
       popupWindow.showAsDropDown(v);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.ll_tel:
                if(post.getContactPhone()!=null){
                    AndroidUtil.dial(this,post.getContactPhone());
                }
                break;
            case R.id.rl_user:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationDetailActivity.this);
                    return;
                }
                intent = new Intent(this, MineDetailActivity.class);
                intent.putExtra("id", user.getId());
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_zhikanlouzhu://收藏
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationDetailActivity.this);
                    return;
                }
                idao.requestCollectPost(Arad.preferences.getString("memberId"),getIntent().getStringExtra("id"),"1");
                break;
            case R.id.tv_daoxu://倒叙查看
                if (idao.getListPostReply2() != null) {
                    idao.getListPostReply2().clear();
                }
                idao.requesPostReplyList(getIntent().getStringExtra("id"), "0", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                break;
            case R.id.tv_pinbizuozhe://屏蔽作者
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationDetailActivity.this);
                    return;
                }
                idao.requeMemberBlackListAdd(Arad.preferences.getString("memberId"), user.getId());
                break;
            case R.id.tv_jubao://举报
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationDetailActivity.this);
                    return;
                }
                idao.requePostReport(post.getId(), "", user.getId(), Arad.preferences.getString("memberId"));
                break;
            case R.id.rl_friend_style_zan:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationDetailActivity.this);
                    return;
                }
                if (count % 2 == 0) {//奇数次点击
                    if (post.getZambiastate().equals("0")) {
                        idao.requesZambia("add", post.getId(), Arad.preferences.getString("memberId"), "", "");

                    }
                    if (post.getZambiastate().equals("1")) {
                        idao.requesZambia("reduce", post.getId(), Arad.preferences.getString("memberId"), "", "");
                    }
                }
                if (count % 2 == 1) {//偶数次点击
                    if (post.getZambiastate().equals("0")) {
                        idao.requesZambia("reduce", post.getId(), Arad.preferences.getString("memberId"), "", "");
                    }
                    if (post.getZambiastate().equals("1")) {
                        idao.requesZambia("add", post.getId(), Arad.preferences.getString("memberId"), "", "");
                    }
                }
                break;
            case R.id.rl_friend_style_pinglun:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationDetailActivity.this);
                    return;
                }
                intent=new Intent(InvitationDetailActivity.this,InvitationCommentDetaioActivity.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                intent.putExtra("reportid",getIntent().getStringExtra("reportid"));
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationDetailActivity.this);
                break;
            case R.id.rl_friend_style_share:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationDetailActivity.this);
                    return;
                }

                shareDialog = new ShareDialog(this);
                shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareDialog.dismiss();
                    }
                });
                shareDialog.setQQButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("烟台项目");
                        sp.setText("欢迎加入");

                        sp.setImageUrl(getIntent().getStringExtra("qrImgUrl"));//网络图片rul
                        sp.setTitleUrl(Constant.SHARE_INVITION_URL+post.getId());  //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(InvitationDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        qq.share(sp);
                    }

                });

                shareDialog.setWeixinButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                        sp.setTitle("烟台项目");  //分享标题
                        sp.setText("欢迎加入");   //分享文本
                        sp.setImageUrl(getIntent().getStringExtra("qrImgUrl"));//网络图片rul
                        sp.setUrl(Constant.SHARE_INVITION_URL+post.getId());   //网友点进链接后，可以看到分享的详情

                        //3、非常重要：获取平台对象
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(InvitationDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        wechat.share(sp);
                    }
                });
                shareDialog.setSinaWeiBoButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setText("我是新浪微博分享文本，啦啦啦~"+Constant.SHARE_INVITION_URL+post.getId()); //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl(Constant.SHARE_INVITION_URL+post.getId());
                        //3、非常重要：获取平台对象
                        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        sinaWeibo.setPlatformActionListener(InvitationDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        sinaWeibo.share(sp);
                    }
                });

                shareDialog.setPengYouQuanButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                        sp.setTitle("我是朋友圈分享标题");  //分享标题
                        sp.setText("我是朋友圈分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl(Constant.SHARE_INVITION_URL+post.getId());   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                        wechatMoments.setPlatformActionListener(InvitationDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        wechatMoments.share(sp);
                    }
                });
                shareDialog.setTecentWeiBoButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("我是腾讯微博分享标题");  //分享标题
                        sp.setText("我是腾讯微博分享文本，啦啦啦~"+Constant.SHARE_INVITION_URL+post.getId());   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl(Constant.SHARE_INVITION_URL+post.getId());   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform tecentWeibo = ShareSDK.getPlatform(TencentWeibo.NAME);
                        tecentWeibo.setPlatformActionListener(InvitationDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        tecentWeibo.share(sp);
                    }
                });

                shareDialog.setEmailButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("我是邮件分享标题");  //分享标题
                        sp.setText("我是邮件分享文本，啦啦啦~"+Constant.SHARE_INVITION_URL+post.getId());   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl(Constant.SHARE_INVITION_URL+post.getId());   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform emailName = ShareSDK.getPlatform(Email.NAME);
                        emailName.setPlatformActionListener(InvitationDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        emailName.share(sp);
                    }
                });
                shareDialog.setDuanXinButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle("我是短信分享标题");  //分享标题
                        sp.setText("我是短信分享文本，啦啦啦~"+Constant.SHARE_INVITION_URL+post.getId());   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl(Constant.SHARE_INVITION_URL+post.getId());   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform shortMessage = ShareSDK.getPlatform(ShortMessage.NAME);
                        shortMessage.setPlatformActionListener(InvitationDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        shortMessage.share(sp);
                    }
                });
                break;
            case R.id.rl_friend_style_more:
                showButoomMoreDialog();
                break;
            case R.id.tv_chakan_more:
                intent=new Intent(InvitationDetailActivity.this,InvitationCommentDetaioActivity.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                intent.putExtra("reportid",getIntent().getStringExtra("reportid"));
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationDetailActivity.this);
                break;
        }
    }

    private void showButoomMoreDialog() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_friend_style_more, null);
        final TextView tv_friend_style_jubao = (TextView)contentView.findViewById(R.id.tv_friend_style_jubao);//举报
        TextView tv_friend_style_pinbi_zuozhe = (TextView)contentView.findViewById(R.id.tv_friend_style_pinbi_zuozhe);//屏蔽作者
        TextView tv_friend_style_cancel = (TextView)contentView.findViewById(R.id.tv_friend_style_cancel);//取消
        tv_friend_style_pinbi_zuozhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationDetailActivity.this);
                    return;
                }
                idao.requeMemberBlackListAdd(Arad.preferences.getString("memberId"), user.getId());

            }
        });
        tv_friend_style_jubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationDetailActivity.this);
                    return;
                }
                idao.requePostReport(post.getId(), "", user.getId(), Arad.preferences.getString("memberId"));
            }
        });

        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(dw);

        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        popupWindow.setTouchable(true);
         /*
        * 设置popupwindow 点击自身消失
        * */
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow.setOutsideTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 设置好参数之后再show
        popupWindow.showAtLocation(rl_friend_style_more, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    /*
* 播放语音
* */
    public void playSound(View view,String soundUrl) {
        // TODO Auto-generated method stub

        // 播放动画
        if (viewanim!=null) {//让第二个播放的时候第一个停止播放
            viewanim.setBackgroundResource(R.drawable.voice_default);
            viewanim=null;
        }
        viewanim = view.findViewById(R.id.id_recorder_anim);
        viewanim.setBackgroundResource(R.drawable.play);
        AnimationDrawable drawable = (AnimationDrawable) viewanim
                .getBackground();
        drawable.start();

        // 播放音频
        MediaManager.playSoundFromUrl(InvitationDetailActivity.this, soundUrl,
                new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        viewanim.setBackgroundResource(R.drawable.voice_default);

                    }
                });

    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MediaManager.resume();
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(QQ.NAME)) {// 判断成功的平台是不是QQ
            handler.sendEmptyMessage(1);
        } else if (platform.getName().equals(Wechat.NAME)) {//判断成功的平台是不是微信
            handler.sendEmptyMessage(2);
        } else if (platform.getName().equals(SinaWeibo.NAME)) {//判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(3);
        } else if (platform.getName().equals(WechatMoments.NAME)) {//判断成功平台是不是微信朋友圈
            handler.sendEmptyMessage(4);
        } else if (platform.getName().equals(TencentWeibo.NAME)) {//判断成功平台是不是腾讯微博
            handler.sendEmptyMessage(5);
        } else if (platform.getName().equals(Email.NAME)) {//判断成功平台是不是邮件
            handler.sendEmptyMessage(6);
        } else if (platform.getName().equals(ShortMessage.NAME)) {//判断成功平台是不是短信
            handler.sendEmptyMessage(7);
        } else {
            //
        }

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        Message msg = new Message();
        msg.what = 8;
        msg.obj = throwable.getMessage();
        handler.sendMessage(msg);

    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(9);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //  Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 7:
                    //Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 8:
                    Toast.makeText(getApplicationContext(), "分享失败啊", Toast.LENGTH_LONG).show();
                    break;
                case 9:
                    Toast.makeText(getApplicationContext(), "已取消", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };
}
