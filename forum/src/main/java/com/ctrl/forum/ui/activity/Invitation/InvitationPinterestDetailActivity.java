package com.ctrl.forum.ui.activity.Invitation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.ctrl.forum.customview.AudioRecordButton;
import com.ctrl.forum.customview.ImageZoomActivity;
import com.ctrl.forum.customview.ListViewForScrollView;
import com.ctrl.forum.customview.RoundImageView;
import com.ctrl.forum.customview.ShareDialog;
import com.ctrl.forum.dao.ImageDao;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.dao.SoundDao;
import com.ctrl.forum.entity.Image;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.Post2;
import com.ctrl.forum.entity.PostReply2;
import com.ctrl.forum.face.FaceRelativeLayout;
import com.ctrl.forum.manager.MediaManager;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.ui.activity.mine.MineDetailActivity;
import com.ctrl.forum.ui.adapter.FriendDetailImageAdapter;
import com.ctrl.forum.ui.adapter.InvitationPinetestDetailAdapter;
import com.ctrl.forum.utils.Base64Util;
import com.ctrl.forum.utils.InputMethodUtils;
import com.ctrl.forum.utils.TimeUtils;
import com.ctrl.forum.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 * 帖子列表  瀑布流详情页 activity
 * Created by Administrator on 2016/4/11.
 */
public class InvitationPinterestDetailActivity extends AppToolBarActivity implements View.OnClickListener ,PlatformActionListener{

  /*  @InjectView(R.id.rl_share)//分享
    RelativeLayout rl_share;
    @InjectView(R.id.rl_pinglun)//评论
    RelativeLayout rl_pinglun;
    @InjectView(R.id.rl_zan)//点赞*/
    // RelativeLayout rl_zan;

    @InjectView(R.id.iv_share)//分享
            ImageView iv_share;
    @InjectView(R.id.iv_zan)//点赞
            ImageView iv_zan;
    @InjectView(R.id.tv_pinglun)//评论
            TextView tv_pinglun;


    @InjectView(R.id.ll_pinglun)//评论布局
            LinearLayout ll_pinglun;
    @InjectView(R.id.ll_main)//主布局
            LinearLayout ll_main;
    /*  @InjectView(R.id.ll_edit)//底部编辑布局
              LinearLayout ll_edit;*/
    @InjectView(R.id.iv_input_yuyin)//语音按钮图片
            ImageView iv_input_yuyin;
    /*@InjectView(R.id.btn_yuyin)//说话按钮
            Button btn_yuyin;*/
    @InjectView(R.id.ll_bottom_edit)//照片 表情布局
            LinearLayout ll_bottom_edit;
    @InjectView(R.id.iv_input_add)//加号
            ImageView iv_input_add;
  /*  @InjectView(R.id.et_pinglun)//输入评论
            EditText et_pinglun;*/
 /*   @InjectView(R.id.tv_huitie)//回帖
            TextView tv_huitie;*/
  /*  @InjectView(R.id.ll_input_text)//文字输入布局
            LinearLayout ll_input_text;*/

    @InjectView(R.id.FaceRelativeLayout)//自定义输入编辑布局
            FaceRelativeLayout FaceRelativeLayout;
    @InjectView(R.id.btn_yuyin)//语音按钮
            AudioRecordButton btn_yuyin;
    @InjectView(R.id.ll_input_text)//文字输入布局
            LinearLayout ll_input_text;
    @InjectView(R.id.lv_reply_detail)//评论列表
            ListView lv_reply_detail;
    @InjectView(R.id.ll_facechoose)//表情布局
            RelativeLayout ll_facechoose;
    @InjectView(R.id.btn_send)//回复
            Button btn_send;
    @InjectView(R.id.et_sendmessage)//回复内容
            EditText et_sendmessage;
    @InjectView(R.id.ll_image_custom_facerelativelayout)//图片布局
            LinearLayout ll_image_custom_facerelativelayout;
  /*  @InjectView(R.id.iv01)//图片1
            ImageView iv1;
    @InjectView(R.id.iv02)//图片2
            ImageView iv2;
    @InjectView(R.id.iv03)//图片3
            ImageView iv3;*/

  /*  @InjectView(R.id.title_image)
    ImageView title_image;
    @InjectView(R.id. iv_levlel)
    ImageView  iv_levlel;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id. tv_release_time)
    TextView  tv_release_time;
    @InjectView(R.id.tv_address)
    TextView tv_address;
    @InjectView(R.id.tv_delete)
    TextView tv_delete;
    @InjectView(R.id.tv_introduction)
    TextView tv_introduction;
    @InjectView(R.id. tv_content)
    TextView  tv_content;
    @InjectView(R.id. tv_tel)
    TextView  tv_tel;
    @InjectView(R.id. ll_tel)
    LinearLayout  ll_tel;
    @InjectView(R.id. rl_detail_user)
    RelativeLayout  rl_detail_user;
    @InjectView(R.id. title_image_2)
    RoundImageView  title_image_2;
    @InjectView(R.id. tv_title2_name)
    TextView  tv_title2_name;
    @InjectView(R.id. tv_contact_address)
    TextView  tv_contact_address;
    @InjectView(R.id.lv_invitation_detail_image)
    ListViewForScrollView lv_invitation_detail_image;*/
  /*  @InjectView(R.id.main_pull_refresh_view)
    PullToRefreshView main_pull_refresh_view;*/



    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private int imageFlag = -1;
    List<Image> mImageList = new ArrayList<>();
    private List<ImageView> listImg = new ArrayList<>();
    private String[] items = new String[]{"本地图片", "拍照"};


    private ShareDialog shareDialog;
    private InvitationDao idao;
    private Post2 post;
    private PopupWindow popupWindow;
    private PopupWindow popupWindow_share;
    private int PAGE_NUM = 1;
    private List<PostReply2> listPostReply2;
    //  private InvitationDetailReplyAdapter replyAdapter;
    private InvitationPinetestDetailAdapter mInvitationCommentDetailAdapter;
    private FriendDetailImageAdapter mFriendDetailImageAdapter;

    private int TYPE = -1;
    private static final int REPLY_TYPE_TEXT = 0;//文字或表情
    private static final int REPLY_TYPE_IMAGE = 1;//图片
    private static final int REPLY_TYPE_VOICE_ = 2;//语音

    private boolean isFromPinglun;
    private int itemPosition;
    private SoundDao sdao;
    private String soundUrl;
    private View viewanim;
    private float second;//语音时长;
    private MemberInfo user;
    private LinearLayout ll_image_second;
    private LinearLayout ll_image_first;
    private LinearLayout ll_image_third;
    private ImageDao Idao;


    private int count = 0;//点击计数器
    private int count_daoxu = 0;//倒叙计数器
    private ArrayList<String> imageUrl;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private View loadMoreView;
    private int lastItem;
    private View headerView;
    private ImageView title_image;
    private TextView iv_levlel;
    private TextView tv_name;
    private TextView tv_release_time;
    private TextView tv_address;
    private TextView tv_delete;
    private TextView tv_introduction;
    private TextView tv_content;
    private TextView tv_tel;
    private TextView tv_title2_name;
    private TextView tv_contact_address;
    private LinearLayout ll_tel;
    private RelativeLayout rl_detail_user;
    private RoundImageView title_image_2;
    private ListViewForScrollView lv_invitation_detail_image;
    private RelativeLayout rl_footer;
    private ImageButton biaoqingImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_pinterest_detail);
        ButterKnife.inject(this);
        // 隐藏输入法1
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        ShareSDK.initSDK(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK();
        MediaManager.release();
    }

    private void initView() {
        btn_yuyin.setOnClickListener(this);
        et_sendmessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (InputMethodUtils.isShow(InvitationPinterestDetailActivity.this, et_sendmessage)) {
                    if (ll_bottom_edit != null && ll_bottom_edit.getVisibility() == View.VISIBLE) {
                        ll_bottom_edit.setVisibility(View.GONE);
                        ll_image_custom_facerelativelayout.setVisibility(View.GONE);
                    }
                    if (ll_facechoose != null && ll_facechoose.getVisibility() == View.VISIBLE) {
                        ll_facechoose.setVisibility(View.GONE);
                    }
                } else {
                    InputMethodUtils.show(InvitationPinterestDetailActivity.this, et_sendmessage);
                }
                return false;
            }
        });
        mFriendDetailImageAdapter=new FriendDetailImageAdapter(this);
        headerView=getLayoutInflater().inflate(R.layout.fragment_invitation_pinterest_detail,null);
        loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
        initFooterView();
        initHeaderView();

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtils.hide(InvitationPinterestDetailActivity.this);
            }
        });
        lv_reply_detail.addHeaderView(headerView);
        lv_reply_detail.addFooterView(loadMoreView);
        iv1=(ImageView)FaceRelativeLayout.findViewById(R.id.iv01);
        iv2=(ImageView)FaceRelativeLayout.findViewById(R.id.iv02);
        iv3=(ImageView)FaceRelativeLayout.findViewById(R.id.iv03);
        count = 0;
        count_daoxu=0;
        btn_yuyin.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                //  MessageUtils.showShortToast(InvitationPinterestDetailActivity.this, "语音说话");
                try {
                    second = seconds;
                    String voice = Base64Util.encodeBase64File(filePath);
                    sdao.requestSoundUpload(voice);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        listImg.add(iv1);
        listImg.add(iv2);
        listImg.add(iv3);

        iv1.setOnClickListener(FaceRelativeLayout);
        iv2.setOnClickListener(FaceRelativeLayout);
        iv3.setOnClickListener(FaceRelativeLayout);

        isFromPinglun = false;


        FaceRelativeLayout.setVisibility(View.GONE);
        //  replyAdapter = new InvitationDetailReplyAdapter(this,1);
        mInvitationCommentDetailAdapter = new InvitationPinetestDetailAdapter(this);
        tv_delete.setOnClickListener(this);
        idao = new InvitationDao(this);
        Idao = new ImageDao(this);
        sdao = new SoundDao(this);
        idao.requesPostDetail(getIntent().getStringExtra("id"), Arad.preferences.getString("memberId"));
       // idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
        idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
        iv_share.setOnClickListener(this);
        iv_zan.setOnClickListener(this);
        tv_pinglun.setOnClickListener(this);
        iv_input_yuyin.setOnClickListener(this);
        iv_input_add.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        ll_tel.setOnClickListener(this);
        rl_detail_user.setOnClickListener(this);
        title_image.setOnClickListener(this);


     /*   lv_reply_detail.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        lv_reply_detail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
            }
        });
*/

        lv_reply_detail.setAdapter(mInvitationCommentDetailAdapter);
        lv_invitation_detail_image.setAdapter(mFriendDetailImageAdapter);
        lv_invitation_detail_image.setFocusable(false);
        lv_reply_detail.setFocusable(false);
        lv_reply_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodUtils.hide(InvitationPinterestDetailActivity.this);
            }
        });
        lv_reply_detail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (lastItem-1 == mInvitationCommentDetailAdapter.getCount()) {
                        rl_footer.setVisibility(View.VISIBLE);
                        rl_footer.setPadding(0, 0, 0, 0);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PAGE_NUM += 1;
                                idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;
            }
        });

     /*   main_pull_refresh_view.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(listPostReply2!=null){
                            listPostReply2.clear();
                        }
                        main_pull_refresh_view.onHeaderRefreshComplete("更新于:"
                                + Calendar.getInstance().getTime().toLocaleString());
                        PAGE_NUM =1;
                        idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                    }
                }, 1000);
            }
        });
        main_pull_refresh_view.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PAGE_NUM += 1;
                        idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                    }
                }, 1000);

            }
        });*/



        lv_invitation_detail_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(InvitationPinterestDetailActivity.this, ImageZoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imageList",imageUrl);
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationPinterestDetailActivity.this);
            }
        });

    }

    private void initFooterView() {
        rl_footer=(RelativeLayout)loadMoreView.findViewById(R.id.rl_footer);
    }

    private void initHeaderView() {
         title_image=(ImageView)headerView.findViewById(R.id.title_image);
         iv_levlel= (TextView) headerView.findViewById(R.id.iv_levlel);
        tv_name=(TextView)headerView.findViewById(R.id.tv_name);
        tv_release_time=(TextView)headerView.findViewById(R.id.tv_release_time);
        tv_address=(TextView)headerView.findViewById(R.id.tv_address);
        tv_delete=(TextView)headerView.findViewById(R.id.tv_delete);
        tv_introduction=(TextView)headerView.findViewById(R.id.tv_introduction);
        tv_content=(TextView)headerView.findViewById(R.id.tv_content);
        tv_tel=(TextView)headerView.findViewById(R.id.tv_tel);
        tv_title2_name=(TextView)headerView.findViewById(R.id.tv_title2_name);
        tv_contact_address=(TextView)headerView.findViewById(R.id.tv_contact_address);
        ll_tel=(LinearLayout)headerView.findViewById(R.id.ll_tel);
        rl_detail_user=(RelativeLayout)headerView.findViewById(R.id.rl_detail_user);
        title_image_2=(RoundImageView)headerView.findViewById(R.id.title_image_2);
        lv_invitation_detail_image=(ListViewForScrollView)headerView.findViewById(R.id.lv_invitation_detail_image);
    }


    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        if(rl_footer.getVisibility()==View.VISIBLE){
            rl_footer.setVisibility(View.GONE);
            rl_footer.setPadding(0, -rl_footer.getHeight(), 0, 0);
        }
        if(errorNo.equals("027")){
          //  MessageUtils.showShortToast(this, "已经拉黑过，无需重复拉黑");
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==16){
            MessageUtils.showShortToast(this, "帖子收藏成功");
            popupWindow.dismiss();
        }
        if (requestCode == 888) {
            showProgress(false);
        //    MessageUtils.showShortToast(this, "图片上传成功");
            et_sendmessage.setEnabled(false);
            Image image = Idao.getImage();
            mImageList.add(image);
            imageUrl.add(image.getImgUrl());
            setBitmapImg();
            iv1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mImageList.size() >= 1) {
                        imageFlag = 1;
                        showDelDialog(1);
                    }
                    return true;
                }
            });
            iv2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mImageList.size() >= 2) {
                        imageFlag = 2;
                        showDelDialog(2);
                    }
                    return true;
                }
            });
            iv3.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mImageList.size() >= 3) {
                        imageFlag = 3;
                        showDelDialog(3);
                    }
                    return true;
                }
            });

        }

        if (requestCode == 10) {
            MessageUtils.showShortToast(this, "拉黑成功");
            popupWindow.dismiss();
        }
        if (requestCode == 11) {
            MessageUtils.showShortToast(this, "帖子举报成功");
            popupWindow.dismiss();
        }
        if (requestCode == 889) {
            MessageUtils.showShortToast(this, "语音上传成功");
            soundUrl = sdao.getSoundUrl();
            // replyAdapter.setSoundrUrl(soundUrl);
            if (soundUrl != null) {
                if (!isFromPinglun) {//无评论
                    idao.requestReplyPost(post.getId(), post.getReporterId(), "", Arad.preferences.getString("memberId"), "2", "", soundUrl, "", "", "", "");
                } else {//有评论
                    idao.requestReplyPost(post.getId(), post.getReporterId(), listPostReply2.get(itemPosition).getId(), Arad.preferences.getString("memberId"), "2", "", soundUrl, listPostReply2.get(itemPosition).getMemberId(), "", "", "");
                }


            }
        }
        if (requestCode == 15) {
            //  MessageUtils.showShortToast(this, "回复成功");
            reset();
            if (listPostReply2 != null) {
                listPostReply2.clear();
            }
            PAGE_NUM = 1;
            idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
            isFromPinglun = false;
        }
        if (requestCode == 5) {
          //  lv_reply_detail.onRefreshComplete();
          //  main_pull_refresh_view.onFooterRefreshComplete();
            //   MessageUtils.showShortToast(this, "获取评论列表成功");
            if (popupWindow != null) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }

            if(rl_footer.getVisibility()==View.VISIBLE){
                rl_footer.setVisibility(View.GONE);
                rl_footer.setPadding(0, -rl_footer.getHeight(), 0, 0);
            }

            listPostReply2 = idao.getListPostReply2();
            mInvitationCommentDetailAdapter.setList(listPostReply2);
            //  lv_reply_detail.setAdapter(replyAdapter);
            count_daoxu++;

        }
        if (requestCode == 14) {
            if (count % 2 == 0) {//奇数次点击
                if (post.getZambiastate().equals("0")) {
                  //  MessageUtils.showShortToast(this, "取消点赞成功");
                    iv_zan.setImageResource(R.mipmap.zan_blue_shixin);

                }
                if (post.getZambiastate().equals("1")) {
                    //MessageUtils.showShortToast(this, "点赞成功");
                    iv_zan.setImageResource(R.mipmap.zan_blue);

                }
            }
            if (count % 2 == 1) {//偶数次点击
                if (post.getZambiastate().equals("0")) {
                  //  MessageUtils.showShortToast(this, "点赞成功");
                    iv_zan.setImageResource(R.mipmap.zan_blue);



                }
                if (post.getZambiastate().equals("1")) {
                  //  MessageUtils.showShortToast(this, "取消点赞成功");
                    iv_zan.setImageResource(R.mipmap.zan_blue_shixin);
                }
            }
            count++;
        }
        if (requestCode == 8) {
           // MessageUtils.showShortToast(this, "删除帖子成功");
            showDelPostDialog();

        }
        if (requestCode == 3) {
            // MessageUtils.showShortToast(this, "获取帖子详情成功ghfgh");
            post = idao.getPost2();
            user = idao.getUser();
            if (user != null) {
                String levlel = idao.getUser().getMemberLevel();
                SetMemberLevel.setLevelImage(this, iv_levlel, levlel);
               /* if (levlel != null) {
                    switch (levlel) {
                        case "0":
                            iv_levlel.setImageResource(R.mipmap.vip_icon);
                            break;
                        case "1":
                            iv_levlel.setImageResource(R.mipmap.vip_icon1);
                            break;
                        case "2":
                            iv_levlel.setImageResource(R.mipmap.vip_icon2);
                            break;
                        case "3":
                            iv_levlel.setImageResource(R.mipmap.vip_icon3);
                            break;
                        case "4":
                            iv_levlel.setImageResource(R.mipmap.vip_icon4);
                            break;
                        case "5":
                            iv_levlel.setImageResource(R.mipmap.vip_icon5);
                            break;
                        case "6":
                            iv_levlel.setImageResource(R.mipmap.vip_icon6);
                            break;
                        case "7":
                            iv_levlel.setImageResource(R.mipmap.vip_icon7);
                            break;
                    }
                }else {
                    iv_levlel.setImageResource(R.mipmap.vip_icon);
                }*/
                Arad.imageLoader.load(user.getImgUrl()).placeholder(R.mipmap.round_img).resize(300,300).centerCrop().into(title_image);
                if(user.getNickName()!=null&&!user.getNickName().equals("")){
                    tv_name.setText(user.getNickName());
                }else {
                    String mobile = user.getUserName();
                    String result = mobile.substring(0,3)+"****"+mobile.substring(7,mobile.length());
                    tv_name.setText(result);
                }
                if(post.getVcardDisplay().equals("0")){
                    rl_detail_user.setVisibility(View.GONE);
                }
                if(post.getVcardDisplay().equals("1")){
                    rl_detail_user.setVisibility(View.VISIBLE);
                    Arad.imageLoader.load(user.getImgUrl()).placeholder(R.mipmap.round_img).into(title_image_2);
                    if(user.getNickName()!=null&&!user.getNickName().equals("")){
                        tv_title2_name.setText(user.getNickName());
                    }else {
                        String mobile = user.getUserName();
                        String result = mobile.substring(0,3)+"****"+mobile.substring(7,mobile.length());
                        tv_title2_name.setText(result);
                    }
                }
            }
            if(post.getLocationName()!=null)
            tv_address.setText(post.getLocationName());
            tv_introduction.setText(post.getTitle());

            if(post.getContactAddress()!=null&&!post.getContactAddress().equals("")){
                tv_contact_address.setVisibility(View.VISIBLE);
                tv_contact_address.setText("地址：" + post.getContactAddress());
            }
            if(post.getReporterId().equals(Arad.preferences.getString("memberId"))){
               // tv_delete.setVisibility(View.VISIBLE);
            }

            if ( post.getContactPhone() == null||post.getContactPhone().equals("")) {
                ll_tel.setVisibility(View.GONE);
            } else {
                ll_tel.setVisibility(View.VISIBLE);
                tv_tel.setText(post.getContactName());
            }

            if(post.getZambiastate().equals("0")){
                iv_zan.setImageResource(R.mipmap.zan_blue);
            }
            if(post.getZambiastate().equals("1")){
                iv_zan.setImageResource(R.mipmap.zan_blue_shixin);
            }
            tv_release_time.setText(TimeUtils.dateTime(post.getPublishTime()));
            tv_content.setText(post.getContent());
            mFriendDetailImageAdapter.setList(idao.getListPostImage());
            imageUrl=new ArrayList<>();
            for(int i=0;i<idao.getListPostImage().size();i++){
                imageUrl.add(idao.getListPostImage().get(i).getImg());
            }

        }


    }

    private void showDelPostDialog() {
        new AlertDialog.Builder(this)
                .setTitle("确定删除帖子吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(667);
                        finish();
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

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

    private void gotoImageZoomActivity(Activity activity,ArrayList<String>imageUrl,int pos){
        Intent intent=new Intent(activity, ImageZoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("imageList",imageUrl);
        bundle.putInt("position",pos);
        intent.putExtras(bundle);
        startActivity(intent);
        AnimUtil.intentSlidIn(activity);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()) {
            case R.id.iv01:
              gotoImageZoomActivity(InvitationPinterestDetailActivity.this,imageUrl,0);
                break;
            case R.id.iv02:
              gotoImageZoomActivity(InvitationPinterestDetailActivity.this,imageUrl,1);
                break;
            case R.id.iv03:
              gotoImageZoomActivity(InvitationPinterestDetailActivity.this,imageUrl,2);
                break;
            case R.id.iv04:
              gotoImageZoomActivity(InvitationPinterestDetailActivity.this,imageUrl,3);
                break;
            case R.id.iv05:
              gotoImageZoomActivity(InvitationPinterestDetailActivity.this,imageUrl,4);
                break;
            case R.id.iv06:
              gotoImageZoomActivity(InvitationPinterestDetailActivity.this,imageUrl,5);
                break;
            case R.id.iv07:
              gotoImageZoomActivity(InvitationPinterestDetailActivity.this,imageUrl,6);
                break;
            case R.id.iv08:
              gotoImageZoomActivity(InvitationPinterestDetailActivity.this,imageUrl,7);
                break;
            case R.id.iv09:
              gotoImageZoomActivity(InvitationPinterestDetailActivity.this,imageUrl,8);
                break;
            case R.id.title_image:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPinterestDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPinterestDetailActivity.this);
                    return;
                }
                intent = new Intent(this, MineDetailActivity.class);
                intent.putExtra("id", user.getId());
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.rl_detail_user:
                    if (Arad.preferences.getString("memberId") == null || Arad.preferences.getString("memberId").equals("")) {
                        startActivity(new Intent(InvitationPinterestDetailActivity.this, LoginActivity.class));
                        AnimUtil.intentSlidOut(InvitationPinterestDetailActivity.this);
                        return;
                    }
                    intent = new Intent(this, MineDetailActivity.class);
                    intent.putExtra("id", user.getId());
                    startActivity(intent);
                    AnimUtil.intentSlidIn(this);
                break;
            case R.id.ll_tel://一键拨打电话
                if (post.getContactPhone()!=null&&!post.getContactPhone().equals("")) {
                    AndroidUtil.dial(InvitationPinterestDetailActivity.this, post.getContactPhone());
                }
                break;
            case R.id.tv_zhikanlouzhu://收藏
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPinterestDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPinterestDetailActivity.this);
                    return;
                }
                idao.requestCollectPost(Arad.preferences.getString("memberId"),getIntent().getStringExtra("id"),"1");
                break;
            case R.id.tv_daoxu://倒叙查看
                if (listPostReply2 != null) {
                    listPostReply2.clear();
                }
                if ((count_daoxu-1) % 2 == 0) {//奇数次点击
                    idao.requesPostReplyList(getIntent().getStringExtra("id"), "0", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));

                }
                if ((count_daoxu-1) % 2 == 1) {//偶数次点击
                    idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                }

              //  idao.requesPostReplyList(getIntent().getStringExtra("id"), "0", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                break;
            case R.id.tv_pinbizuozhe://屏蔽作者
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPinterestDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPinterestDetailActivity.this);
                    return;
                }
                idao.requeMemberBlackListAdd(Arad.preferences.getString("memberId"), user.getId());
                break;
            case R.id.tv_jubao://举报
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPinterestDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPinterestDetailActivity.this);
                    return;
                }
                idao.requePostReport(post.getId(), "", user.getId(), Arad.preferences.getString("memberId"));
                break;

            case R.id.tv_delete_invitation:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPinterestDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPinterestDetailActivity.this);
                    return;
                }
                idao.requesDeltePost(getIntent().getStringExtra("id"));
                break;
            case R.id.iv_share:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPinterestDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPinterestDetailActivity.this);
                    return;
                }
               // showSharePopuwindow(iv_share);
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

                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setTitleUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");  //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(InvitationPinterestDetailActivity.this); // 设置分享事件回调
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
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");   //网友点进链接后，可以看到分享的详情

                        //3、非常重要：获取平台对象
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(InvitationPinterestDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        wechat.share(sp);
                    }
                });
                shareDialog.setSinaWeiBoButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setText("我是新浪微博分享文本，啦啦啦~http://uestcbmi.com/"); //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        //3、非常重要：获取平台对象
                        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        sinaWeibo.setPlatformActionListener(InvitationPinterestDetailActivity.this); // 设置分享事件回调
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
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                        wechatMoments.setPlatformActionListener(InvitationPinterestDetailActivity.this); // 设置分享事件回调
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
                        sp.setText("我是腾讯微博分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform tecentWeibo = ShareSDK.getPlatform(TencentWeibo.NAME);
                        tecentWeibo.setPlatformActionListener(InvitationPinterestDetailActivity.this); // 设置分享事件回调
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
                        sp.setText("我是邮件分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform emailName = ShareSDK.getPlatform(Email.NAME);
                        emailName.setPlatformActionListener(InvitationPinterestDetailActivity.this); // 设置分享事件回调
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
                        sp.setText("我是短信分享文本，啦啦啦~http://uestcbmi.com/");   //分享文本
                        sp.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul
                        sp.setUrl("http://sharesdk.cn");   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform shortMessage = ShareSDK.getPlatform(ShortMessage.NAME);
                        shortMessage.setPlatformActionListener(InvitationPinterestDetailActivity.this); // 设置分享事件回调
                        // 执行分享
                        shortMessage.share(sp);
                    }
                });
                break;
            case R.id.iv_zan:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPinterestDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPinterestDetailActivity.this);
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
            case R.id.btn_send:
                reply();
                break;
            case R.id.tv_pinglun:
                if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
                    startActivity(new Intent(InvitationPinterestDetailActivity.this, LoginActivity.class));
                    AnimUtil.intentSlidOut(InvitationPinterestDetailActivity.this);
                    return;
                }
                isFromPinglun = false;
                if(Arad.preferences.getString("isShielded").equals("1")){
                    MessageUtils.showShortToast(InvitationPinterestDetailActivity.this,"您已经被屏蔽，不能发评论");
                }
                if(Arad.preferences.getString("isShielded").equals("0")) {
                    if (ll_pinglun.getVisibility() == View.VISIBLE) {
                        ll_pinglun.setVisibility(View.GONE);
                        InputMethodUtils.toggle(InvitationPinterestDetailActivity.this);
                        FaceRelativeLayout.setVisibility(View.VISIBLE);
                        //  ll_edit.setVisibility(View.VISIBLE);
                    } else {
                        ll_pinglun.setVisibility(View.VISIBLE);
                        FaceRelativeLayout.setVisibility(View.GONE);
                        //  ll_edit.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.iv_input_yuyin:
                InputMethodUtils.hide(InvitationPinterestDetailActivity.this);
                if (ll_bottom_edit.getVisibility() == View.VISIBLE) {
                    ll_bottom_edit.setVisibility(View.GONE);
                }
                if (ll_facechoose.getVisibility() == View.VISIBLE) {
                    ll_facechoose.setVisibility(View.GONE);
                }
                if (btn_yuyin.getVisibility() == View.VISIBLE) {
                    btn_yuyin.setVisibility(View.GONE);
                    ll_input_text.setVisibility(View.VISIBLE);
                } else {
                    btn_yuyin.setVisibility(View.VISIBLE);
                    ll_input_text.setVisibility(View.GONE);

                }
                break;
            case R.id.iv_input_add:
                ll_facechoose.setVisibility(View.GONE);
                InputMethodUtils.hide(InvitationPinterestDetailActivity.this);
                if (ll_bottom_edit.getVisibility() == View.VISIBLE) {
                    ll_bottom_edit.setVisibility(View.GONE);
                    ll_image_custom_facerelativelayout.setVisibility(View.GONE);
                    if(mImageList!=null){
                        mImageList.clear();
                    }

                    et_sendmessage.setEnabled(true);

                } else {
                    ll_bottom_edit.setVisibility(View.VISIBLE);
                    Arad.preferences.putBoolean("isFromAPP", true);
                    Arad.preferences.flush();
                }
                break;

        }

    }


    public void goToAlbum() {
        Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
        intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
        AnimUtil.intentSlidIn(InvitationPinterestDetailActivity.this);
    }

    public void goToGraph() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cxh.jpg")));
        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
        AnimUtil.intentSlidIn(InvitationPinterestDetailActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //加上这个判断就好了
            if (resultCode == Activity.RESULT_CANCELED) {
                return;
            }
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    Uri uri = data.getData();
                    String thePath = Utils.getInstance().getPath(this, uri);
                    getImageToView1(thePath);
                    break;
                case CAMERA_REQUEST_CODE:
                    getImageToView1(Environment.getExternalStorageDirectory() + "/cxh.jpg");
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getImageToView1(String path) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
            byte[] buffer = out.toByteArray();
            byte[] encode = Base64.encode(buffer, Base64.DEFAULT);

            String photo = new String(encode);
            if (photo != null) {
                // Log.d("demo","上传方法2");
                /**调用后台方法  将图片上传**/
                //  String imgData = photo;
                showProgress(true);
                Idao.requestUploadImage(photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDelDialog(final int posititon) {
        new AlertDialog.Builder(this)
                .setTitle("确定删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // iDao.requestDelImg(iDao.getImg().getImgId());
                        if (imageFlag == 1) {
                            delImg(1);
                        }
                        if (imageFlag == 2) {
                            delImg(2);
                        }
                        if (imageFlag == 3) {
                            delImg(3);
                        }
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();


    }

    private void setBitmapImg() {
        if (mImageList != null) {
            if (mImageList.size() == 0) {
                ll_image_custom_facerelativelayout.setVisibility(View.GONE);
                iv1.setVisibility(View.INVISIBLE);
                iv2.setVisibility(View.INVISIBLE);
                iv3.setVisibility(View.INVISIBLE);
            }
            if (mImageList.size() == 1) {
                ll_image_custom_facerelativelayout.setVisibility(View.VISIBLE);
                ll_image_custom_facerelativelayout.clearAnimation();
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.INVISIBLE);
                iv3.setVisibility(View.INVISIBLE);
                for (int i = 0; i < mImageList.size(); i++) {
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                    listImg.get(i).clearAnimation();
                }

            }

            if (mImageList.size() == 2) {
                ll_image_custom_facerelativelayout.setVisibility(View.VISIBLE);
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.INVISIBLE);
                for (int i = 0; i < mImageList.size(); i++) {
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }

            }

            if (mImageList.size() == 3) {
                iv1.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.VISIBLE);
                for (int i = 0; i < mImageList.size(); i++) {
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }
            }

        }
    }

  private void delImg(int imgFlg) {

        if (mImageList != null) {

            //**长按 第一张图*//*
            if (imgFlg == 1) {
                if (mImageList.size() == 1) {
                    et_sendmessage.setEnabled(true);
                    mImageList.remove(0);

                    ll_image_custom_facerelativelayout.setVisibility(View.GONE);
                    iv1.setVisibility(View.INVISIBLE);
                    iv2.setVisibility(View.INVISIBLE);
                    iv3.setVisibility(View.INVISIBLE);
                    setBitmapImg();

                }

                if (mImageList.size() == 2) {
                    mImageList.remove(0);
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.INVISIBLE);
                    iv3.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }

                if (mImageList.size() == 3) {
                    mImageList.remove(0);
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
            }

            //**长按 第二张图*//*
            if (imgFlg == 2) {
                if (mImageList.size() == 1) {
                    //
                }

                if (mImageList.size() == 2) {
                    mImageList.remove(1);
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.INVISIBLE);
                    iv3.setVisibility(View.INVISIBLE);

                    setBitmapImg();
                }

                if (mImageList.size() == 3) {
                    mImageList.remove(1);


                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

            }


            //**长按 第三张图*//*
            if (imgFlg == 3) {
                if (mImageList.size() == 1) {
//
                }

                if (mImageList.size() == 2) {
                    //
                }

                if (mImageList.size() == 3) {
                    mImageList.remove(2);


                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

            }

        }
    }

    private void reply() {
        if (isFromPinglun) {
            if (et_sendmessage.getText().toString().equals("") && mImageList.size() > 0) {

                idao.requestReplyPost(post.getId(), post.getReporterId(), listPostReply2.get(itemPosition).getId(),
                        Arad.preferences.getString("memberId"), "1", et_sendmessage.getText().toString().trim(), "",
                        listPostReply2.get(itemPosition).getMemberId(), listPostReply2.get(itemPosition).getMemberFloor(), setURL(),
                        setThunbUrl());

            } else if (!TextUtils.isEmpty(et_sendmessage.getText().toString().trim()) && mImageList.size() == 0) {

                idao.requestReplyPost(post.getId(), post.getReporterId(), listPostReply2.get(itemPosition).getId(),
                        Arad.preferences.getString("memberId"), "0", et_sendmessage.getText().toString().trim(), "",
                        listPostReply2.get(itemPosition).getMemberId(), listPostReply2.get(itemPosition).getMemberFloor(), "",
                        "");

            } else {
                MessageUtils.showShortToast(this, "回复内容为空");
            }


        } else {
            if (et_sendmessage.getText().toString().equals("") && mImageList.size() > 0) {

                idao.requestReplyPost(post.getId(), post.getReporterId(), "", Arad.preferences.getString("memberId"), "1",
                        et_sendmessage.getText().toString().trim(), "", "", "",
                        setURL(), setThunbUrl());

            } else if (!TextUtils.isEmpty(et_sendmessage.getText().toString().trim()) && mImageList.size() == 0) {

                idao.requestReplyPost(post.getId(), post.getReporterId(), "", Arad.preferences.getString("memberId"), "0",
                        et_sendmessage.getText().toString().trim(), "", "", "",
                        "", "");
            } else {
                MessageUtils.showShortToast(this, "回复内容为空");
            }

        }
    }

    private String setThunbUrl() {
        String url = null;
        if (mImageList.size() == 1) {
            url = mImageList.get(0).getThumbImgUrl();
        }
        if (mImageList.size() == 2) {
            url = mImageList.get(0).getImgUrl() + "," + mImageList.get(1).getThumbImgUrl();
        }
        if (mImageList.size() == 3) {
            url = mImageList.get(0).getImgUrl() + "," + mImageList.get(1).getImgUrl() + "," + mImageList.get(2).getThumbImgUrl();
        }
        return url;
    }

    private String setURL() {
        String url = null;
        if (mImageList.size() == 1) {
            url = mImageList.get(0).getImgUrl();
        }
        if (mImageList.size() == 2) {
            url = mImageList.get(0).getImgUrl() + "," + mImageList.get(1).getImgUrl();
        }
        if (mImageList.size() == 3) {
            url = mImageList.get(0).getImgUrl() + "," + mImageList.get(1).getImgUrl() + "," + mImageList.get(2).getImgUrl();
        }
        return url;
    }


    /*
    * 输入框复位
    * */
    private void reset() {
        InputMethodUtils.hide(InvitationPinterestDetailActivity.this);
        isFromPinglun = false;
        if (!et_sendmessage.isEnabled()) {
            et_sendmessage.setEnabled(true);
        }
        if (mImageList != null) {
            mImageList.clear();
        }
        if (ll_bottom_edit.getVisibility() == View.VISIBLE) {
            ll_bottom_edit.setVisibility(View.GONE);
        }
        if (ll_image_custom_facerelativelayout.getVisibility() == View.VISIBLE) {
            ll_image_custom_facerelativelayout.setVisibility(View.GONE);
        }
        if (FaceRelativeLayout.getVisibility() == View.VISIBLE) {
            et_sendmessage.setText("");
            FaceRelativeLayout.setVisibility(View.GONE);
            ll_pinglun.setVisibility(View.VISIBLE);
        }
    }

    private void showSharePopuwindow(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.share_dialog, null);

        popupWindow_share = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置SelectPicPopupWindow的View
        popupWindow_share.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow_share.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow_share.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow_share.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popupWindow_share.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x90000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow_share.setBackgroundDrawable(dw);

        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        popupWindow_share.setTouchable(true);
         /*
        * 设置popupwindow 点击自身消失
        * */
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow_share.isShowing()) {
                    popupWindow_share.dismiss();
                }
            }
        });

        popupWindow_share.setOutsideTouchable(true);

        popupWindow_share.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 设置好参数之后再show
        popupWindow_share.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    /*
   *弹出窗口
    *  */
    private void showMorePopupwindow(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_more, null);

        TextView tv_zhikanlouzhu = (TextView) contentView.findViewById(R.id.tv_zhikanlouzhu);
        TextView tv_daoxu = (TextView) contentView.findViewById(R.id.tv_daoxu);
        TextView tv_pinbizuozhe = (TextView) contentView.findViewById(R.id.tv_pinbizuozhe);
        TextView tv_jubao = (TextView) contentView.findViewById(R.id.tv_jubao);
        TextView tv_delete_invitation = (TextView) contentView.findViewById(R.id.tv_delete_invitation);

        if(post.getReporterId().equals(Arad.preferences.getString("memberId"))){
            // tv_delete.setVisibility(View.VISIBLE);
            tv_delete_invitation.setVisibility(View.VISIBLE);
        }

        tv_zhikanlouzhu.setOnClickListener(this);
        tv_daoxu.setOnClickListener(this);
        tv_pinbizuozhe.setOnClickListener(this);
        tv_jubao.setOnClickListener(this);
        tv_delete_invitation.setOnClickListener(this);


        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置SelectPicPopupWindow的View
        popupWindow.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //   popupWindow.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x90000000);
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

    /*
    * 回复评论
    * */
    public void replyPinglun(int position) {
        if(Arad.preferences.getString("memberId")==null||Arad.preferences.getString("memberId").equals("")){
            startActivity(new Intent(InvitationPinterestDetailActivity.this, LoginActivity.class));
            AnimUtil.intentSlidOut(InvitationPinterestDetailActivity.this);
            return;
        }
        if(Arad.preferences.getString("isShielded").equals("1")){
            MessageUtils.showShortToast(InvitationPinterestDetailActivity.this,"您已经被屏蔽，不能回复评论");
        }
        if(Arad.preferences.getString("isShielded").equals("0")){
            this.itemPosition = position;
            isFromPinglun = true;
            changeSoftStatus();
        }
    }

    /*
    * 改变输入状态
    * */
    private void changeSoftStatus() {
        if (ll_pinglun.getVisibility() == View.VISIBLE) {
            ll_pinglun.setVisibility(View.GONE);
            FaceRelativeLayout.setVisibility(View.VISIBLE);
           InputMethodUtils.toggle(InvitationPinterestDetailActivity.this);
        } else {
            ll_pinglun.setVisibility(View.VISIBLE);
            FaceRelativeLayout.setVisibility(View.GONE);
        }

    }

    /*
    * 播放语音
    * */
    public void playSound(View view, String soundUrl) {
        // TODO Auto-generated method stub

        // 播放动画
        if (viewanim != null) {//让第二个播放的时候第一个停止播放
            viewanim.setBackgroundResource(R.drawable.voice_default);
            viewanim = null;
        }
        viewanim = view.findViewById(R.id.id_recorder_anim);
        viewanim.setBackgroundResource(R.drawable.play);
        AnimationDrawable drawable = (AnimationDrawable) viewanim
                .getBackground();
        drawable.start();

        // 播放音频
        MediaManager.playSoundFromUrl(InvitationPinterestDetailActivity.this, soundUrl,
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
        }else if(platform.getName().equals(SinaWeibo.NAME)){//判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(3);
        }else if(platform.getName().equals(WechatMoments.NAME)){//判断成功平台是不是微信朋友圈
            handler.sendEmptyMessage(4);
        }else if(platform.getName().equals(TencentWeibo.NAME)){//判断成功平台是不是腾讯微博
            handler.sendEmptyMessage(5);
        }else if(platform.getName().equals(Email.NAME)){//判断成功平台是不是邮件
            handler.sendEmptyMessage(6);
        }else if(platform.getName().equals(ShortMessage.NAME)){//判断成功平台是不是短信
            handler.sendEmptyMessage(7);
        }else {
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
