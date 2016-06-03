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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.AudioRecordButton;
import com.ctrl.forum.customview.ListViewForScrollView;
import com.ctrl.forum.customview.ShareDialog;
import com.ctrl.forum.dao.ImageDao;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.dao.SoundDao;
import com.ctrl.forum.entity.Image;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.Post2;
import com.ctrl.forum.entity.PostReply2;
import com.ctrl.forum.face.FaceRelativeLayout;
import com.ctrl.forum.manager.MediaManager;
import com.ctrl.forum.ui.adapter.InvitationDetailReplyAdapter;
import com.ctrl.forum.ui.adapter.InvitationListViewAdapter;
import com.ctrl.forum.utils.Base64Util;
import com.ctrl.forum.utils.TimeUtils;
import com.ctrl.forum.utils.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;

/**
 * 帖子列表  详情页(来自平台) activity
 * Created by Administrator on 2016/4/11.
 */
public class InvitationDetailFromPlatformActivity extends AppToolBarActivity implements View.OnClickListener {

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
            PullToRefreshListView lv_reply_detail;
    @InjectView(R.id.ll_facechoose)//表情布局
            RelativeLayout ll_facechoose;
    @InjectView(R.id.btn_send)//回复
            Button btn_send;
    @InjectView(R.id.et_sendmessage)//回复内容
            EditText et_sendmessage;

    @InjectView(R.id.iv01)//图片1
            ImageView iv01;
    @InjectView(R.id.iv02)//图片2
            ImageView iv02;
    @InjectView(R.id.iv03)//图片3
            ImageView iv03;
    @InjectView(R.id.ll_image_custom_facerelativelayout)//图片布局
            LinearLayout ll_image_custom_facerelativelayout;


    private ShareDialog shareDialog;
    private InvitationDao idao;
    private Post2 post;
    private PopupWindow popupWindow;
    private PopupWindow popupWindow_share;
    private int PAGE_NUM = 1;
    private List<PostReply2> listPostReply2;
    private InvitationDetailReplyAdapter replyAdapter;
    private View headview;
    private ImageView title_image;
    private TextView tv_name;
    private ImageView iv_levlel;
    /*   private ImageView iv01;
       private ImageView iv02;
       private ImageView iv03;
       private ImageView iv04;
       private ImageView iv05;
       private ImageView iv06;
       private ImageView iv07;
       private ImageView iv08;
       private ImageView iv09;*/
    private TextView tv_release_time;
    private TextView tv_address;
    private TextView tv_delete;
    private TextView tv_introduction;
    private TextView tv_tel;
    private LinearLayout ll_tel;

    private int TYPE = -1;
    private static final int REPLY_TYPE_TEXT = 0;//文字或表情
    private static final int REPLY_TYPE_IMAGE = 1;//图片
    private static final int REPLY_TYPE_VOICE_ = 2;//语音


    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private int imageFlag = -1;
    List<Image> mImageList = new ArrayList<>();
    private List<ImageView> listImg = new ArrayList<>();
    private String[] items = new String[]{"本地图片", "拍照"};

    private boolean isFromPinglun;
    private int itemPosition;
    private SoundDao sdao;
    private String soundUrl;
    private View viewanim;
    private float second;//语音时长;
    private MemberInfo user;
    private WebView webview;
    private MapView mapview_invitation;
    private InvitationListViewAdapter mInvitationListViewAdapter;
    private ListViewForScrollView lv_relevance_invitation;
    private List<Post> listRelateMap;
    private ImageDao Idao;
    private String longitude;
    private String latutude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_pinterest_detail);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        headview = getLayoutInflater().inflate(R.layout.fragment_invitation_detai_from_platform, lv_reply_detail, false);
        headview.setLayoutParams(layoutParams);
        ListView lv = lv_reply_detail.getRefreshableView();
        lv.addHeaderView(headview);
        initView();

        ShareSDK.initSDK(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview_invitation.onDestroy();
        ShareSDK.stopSDK();
        MediaManager.release();
    }

    private void initView() {
        isFromPinglun=false;
        title_image = (ImageView) headview.findViewById(R.id.title_image);//用户头像
        iv_levlel = (ImageView) headview.findViewById(R.id.iv_levlel);//等级
      /*  iv01 = (ImageView) headview.findViewById(R.id.iv01);//图片
        iv02 = (ImageView) headview.findViewById(R.id.iv02);
        iv03 = (ImageView) headview.findViewById(R.id.iv03);
        iv04 = (ImageView) headview.findViewById(R.id.iv04);
        iv05 = (ImageView) headview.findViewById(R.id.iv05);
        iv06 = (ImageView) headview.findViewById(R.id.iv06);
        iv07 = (ImageView) headview.findViewById(R.id.iv07);
        iv08 = (ImageView) headview.findViewById(R.id.iv08);
        iv09 = (ImageView) headview.findViewById(R.id.iv09);*/
        mapview_invitation = (MapView) headview.findViewById(R.id.mapview_invitation);
        lv_relevance_invitation = (ListViewForScrollView) headview.findViewById(R.id.lv_relevance_invitation);
        tv_name = (TextView) headview.findViewById(R.id.tv_name);//发帖人昵称
        tv_release_time = (TextView) headview.findViewById(R.id.tv_release_time);//发布时间
        tv_address = (TextView) headview.findViewById(R.id.tv_address);//发布地点
        tv_delete = (TextView) headview.findViewById(R.id.tv_delete);//删除本帖
        tv_introduction = (TextView) headview.findViewById(R.id.tv_introduction);//导语
        webview = (WebView) headview.findViewById(R.id.webView);//内容
        tv_tel = (TextView) headview.findViewById(R.id.tv_tel);//电话
        ll_tel = (LinearLayout) headview.findViewById(R.id.ll_tel);//拨打电话

        btn_yuyin.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                MessageUtils.showShortToast(InvitationDetailFromPlatformActivity.this, "语音说话");
                try {
                    second = seconds;
                    String voice = Base64Util.encodeBase64File(filePath);
                    sdao.requestSoundUpload(voice);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        listImg.add(iv01);
        listImg.add(iv02);
        listImg.add(iv03);


        FaceRelativeLayout.setVisibility(View.GONE);
        replyAdapter = new InvitationDetailReplyAdapter(this,2);
        tv_delete.setOnClickListener(this);
        idao = new InvitationDao(this);
        Idao = new ImageDao(this);
        sdao = new SoundDao(this);
        idao.requesPostDetail(getIntent().getStringExtra("id"),Arad.preferences.getString("zambiaID"));
        idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
        iv_share.setOnClickListener(this);
        iv_zan.setOnClickListener(this);
        tv_pinglun.setOnClickListener(this);
        iv_input_yuyin.setOnClickListener(this);
        iv_input_add.setOnClickListener(this);
        btn_send.setOnClickListener(this);


        mInvitationListViewAdapter = new InvitationListViewAdapter(this);


        lv_reply_detail.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        lv_reply_detail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
            }
        });

        lv_reply_detail.setAdapter(replyAdapter);
        lv_relevance_invitation.setAdapter(mInvitationListViewAdapter);


    }


    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        reset();
        lv_reply_detail.onRefreshComplete();
        if(errorNo.equals("001")) {
            reset();
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 888) {
            showProgress(false);
            MessageUtils.showShortToast(this, "图片上传成功");
            et_sendmessage.setEnabled(false);
            Image image = Idao.getImage();
            mImageList.add(image);
            setBitmapImg();
            iv01.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mImageList.size() >= 1) {
                        imageFlag = 1;
                        showDelDialog(1);
                    }
                    return true;
                }
            });
            iv02.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mImageList.size() >= 2) {
                        imageFlag = 2;
                        showDelDialog(2);
                    }
                    return true;
                }
            });
            iv03.setOnLongClickListener(new View.OnLongClickListener() {
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
            Log.i("tag", "soundUrl---" + soundUrl);
            if (soundUrl != null) {
                if (listPostReply2.get(itemPosition).getReplyType().equals("0")) {//无评论
                    idao.requestReplyPost(post.getId(), post.getReporterId(), "", Arad.preferences.getString("memberId"), "2", "", soundUrl, "", "", "", "");
                }
                if (listPostReply2.get(itemPosition).getReplyType().equals("1")) {//有评论
                    idao.requestReplyPost(post.getId(), post.getReporterId(), listPostReply2.get(itemPosition).getId(), Arad.preferences.getString("memberId"), "2", "", soundUrl, listPostReply2.get(itemPosition).getMemberId(), "", "", "");
                }


            }
        }
        if (requestCode == 15) {
            MessageUtils.showShortToast(this, "回复成功");
            reset();
            if (listPostReply2 != null) {
                listPostReply2.clear();
            }
            PAGE_NUM = 1;
            idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
            isFromPinglun = false;
        }
        if (requestCode == 5) {
            lv_reply_detail.onRefreshComplete();
            MessageUtils.showShortToast(this, "获取评论列表成功");
            if (popupWindow != null) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }

            listPostReply2 = idao.getListPostReply2();
            replyAdapter.setList(listPostReply2);
            //  lv_reply_detail.setAdapter(replyAdapter);

        }
        if (requestCode == 14) {
            MessageUtils.showShortToast(this, "点赞成功");
        }

        if (requestCode == 8) {
            MessageUtils.showShortToast(this, "删除帖子成功");
            finish();
        }
        if (requestCode == 3) {
            MessageUtils.showShortToast(this, "获取帖子详情成功ghfgh");
            post = idao.getPost2();
            user = idao.getUser();
            if (user != null) {
                Arad.imageLoader.load(user.getImgUrl()).placeholder(R.mipmap.round_img).into(title_image);
                tv_name.setText(user.getNickName());
                tv_address.setText(idao.getUser().getAddress());
                tv_tel.setText(idao.getUser().getMobile());

                String levlel = idao.getUser().getMemberLevel();
                if (levlel != null) {
                    switch (levlel) {
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
                }
            }
            tv_release_time.setText(TimeUtils.date(Long.parseLong(post.getPublishTime())));
            tv_introduction.setText(post.getBlurbs());
            listRelateMap = idao.getListRelateMap();
            mInvitationListViewAdapter.setList(listRelateMap);

            Log.i("tag", "listRelateMap-----" + listRelateMap.size());

            //加载富文本
            loadRichText();
            //显示地图
            showMap();
        }


    }

    /*
    * 显示地图
    * */
    private void showMap() {

        BaiduMap mBaidumap = mapview_invitation.getMap();
        //设定中心点坐标
        if(post.getLocationLatitude()!=null&&!post.getLocationLatitude().equals("")) {
           latutude = post.getLocationLatitude();
            longitude=post.getLocationLongitude();
        }else {
            latutude="118.23232";
            longitude="23.1111";
        }
            LatLng cenpt = new LatLng(Double.parseDouble(latutude), Double.parseDouble(longitude));
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaidumap.setMapStatus(mMapStatusUpdate);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_location_chatbox);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(cenpt)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaidumap.addOverlay(option);
    }

    /*
    * 加载富文本
    * */
    private void loadRichText() {
        //自适应屏幕
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setDefaultTextEncodingName("UTF -8");
        webview.loadDataWithBaseURL(null, post.getContent(), "text/html", "UTF-8", null);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_zhikanlouzhu://只看楼主
                break;
            case R.id.tv_daoxu://倒叙查看
                if (listPostReply2 != null) {
                    listPostReply2.clear();
                }
                idao.requesPostReplyList(getIntent().getStringExtra("id"), "0", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                break;
            case R.id.tv_pinbizuozhe://屏蔽作者
                idao.requeMemberBlackListAdd(Arad.preferences.getString("memberId"), user.getId());
                break;
            case R.id.tv_jubao://举报
                idao.requePostReport(post.getId(), "", user.getId(), Arad.preferences.getString("memberId"));
                break;

            case R.id.tv_delete:
                idao.requesDeltePost(getIntent().getStringExtra("id"));
                break;
            case R.id.iv_share:
                showSharePopuwindow(iv_share);
                break;
            case R.id.iv_zan:
                //  if(post.getZambiastate().equals())
                idao.requesZambia("add", post.getId(), Arad.preferences.getString("memberId"));
                break;
            case R.id.btn_send:
                reply();
                break;
            case R.id.tv_pinglun:
                isFromPinglun=false;
                if (ll_pinglun.getVisibility() == View.VISIBLE) {
                    ll_pinglun.setVisibility(View.GONE);
                    FaceRelativeLayout.setVisibility(View.VISIBLE);
                    //  ll_edit.setVisibility(View.VISIBLE);
                } else {
                    ll_pinglun.setVisibility(View.VISIBLE);
                    FaceRelativeLayout.setVisibility(View.GONE);
                    //  ll_edit.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_input_yuyin:
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
                if (ll_bottom_edit.getVisibility() == View.VISIBLE) {
                    ll_bottom_edit.setVisibility(View.GONE);
                } else {
                    ll_bottom_edit.setVisibility(View.VISIBLE);
                    Arad.preferences.putBoolean("isFromApp", false);
                    Arad.preferences.flush();
                }
                break;

        }

    }

    public void goToAlbum() {
        Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
        intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
        AnimUtil.intentSlidIn(InvitationDetailFromPlatformActivity.this);
    }

    public void goToGraph() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cxh.jpg")));
        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
        AnimUtil.intentSlidIn(InvitationDetailFromPlatformActivity.this);
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
                iv01.setVisibility(View.INVISIBLE);
                iv02.setVisibility(View.INVISIBLE);
                iv03.setVisibility(View.INVISIBLE);
            }
            if (mImageList.size() == 1) {
                ll_image_custom_facerelativelayout.setVisibility(View.VISIBLE);
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.INVISIBLE);
                iv03.setVisibility(View.INVISIBLE);
                for (int i = 0; i < mImageList.size(); i++) {
                    // Log.i("tag","mImageList----"+mImageList.size());
                    //  Log.i("tag","mImageList  url----"+mImageList.get(i).getThumbImgUrl());
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }

            }

            if (mImageList.size() == 2) {
                ll_image_custom_facerelativelayout.setVisibility(View.VISIBLE);
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv03.setVisibility(View.INVISIBLE);
                for (int i = 0; i < mImageList.size(); i++) {
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }

            }

            if (mImageList.size() == 3) {
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv03.setVisibility(View.VISIBLE);
                for (int i = 0; i < mImageList.size(); i++) {
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }
            }

        }
    }

    private void delImg(int imgFlg) {

        if (mImageList != null) {

            /**长按 第一张图*/
            if (imgFlg == 1) {
                if (mImageList.size() == 1) {
                  et_sendmessage.setEnabled(true);
                    mImageList.remove(0);

                    ll_image_custom_facerelativelayout.setVisibility(View.GONE);
                    iv01.setVisibility(View.INVISIBLE);
                    iv02.setVisibility(View.INVISIBLE);
                    iv03.setVisibility(View.INVISIBLE);
                    setBitmapImg();

                }

                if (mImageList.size() == 2) {
                    mImageList.remove(0);
                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.INVISIBLE);
                    iv03.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }

                if (mImageList.size() == 3) {
                    mImageList.remove(0);
                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
            }

            /**长按 第二张图*/
            if (imgFlg == 2) {
                if (mImageList.size() == 1) {
                    //
                }

                if (mImageList.size() == 2) {
                    mImageList.remove(1);
                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.INVISIBLE);
                    iv03.setVisibility(View.INVISIBLE);

                    setBitmapImg();
                }

                if (mImageList.size() == 3) {
                    mImageList.remove(1);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

            }


            /**长按 第三张图*/
            if (imgFlg == 3) {
                if (mImageList.size() == 1) {
//
                }

                if (mImageList.size() == 2) {
                    //
                }

                if (mImageList.size() == 3) {
                    mImageList.remove(2);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

            }

        }
    }

    private void reply() {
        if(isFromPinglun){

            if(et_sendmessage.getText().toString().equals("")&&mImageList.size()>0){

                idao.requestReplyPost(post.getId(), post.getReporterId(), listPostReply2.get(itemPosition).getId(),
                        Arad.preferences.getString("memberId"), "1", et_sendmessage.getText().toString().trim(), "",
                        listPostReply2.get(itemPosition).getMemberId(), listPostReply2.get(itemPosition).getMemberFloor(),setURL(),
                       setThunbUrl());

            }else if(!TextUtils.isEmpty(et_sendmessage.getText().toString().trim())&&mImageList.size()==0){

                idao.requestReplyPost(post.getId(), post.getReporterId(), listPostReply2.get(itemPosition).getId(),
                        Arad.preferences.getString("memberId"), "0", et_sendmessage.getText().toString().trim(), "",
                        listPostReply2.get(itemPosition).getMemberId(), listPostReply2.get(itemPosition).getMemberFloor(),"",
                        "");

            }else {
                MessageUtils.showShortToast(this,"回复内容为空");
            }



        }else {
            if(et_sendmessage.getText().toString().equals("")&&mImageList.size()>0){

                idao.requestReplyPost(post.getId(), post.getReporterId(), "", Arad.preferences.getString("memberId"), "1",
                        et_sendmessage.getText().toString().trim(), "", "", "",
                        setURL(),setThunbUrl());

            }else if(!TextUtils.isEmpty(et_sendmessage.getText().toString().trim())&&mImageList.size()==0){

                idao.requestReplyPost(post.getId(), post.getReporterId(), "", Arad.preferences.getString("memberId"), "0",
                        et_sendmessage.getText().toString().trim(), "", "", "",
                        "","");
            }else {
                MessageUtils.showShortToast(this,"回复内容为空");
            }

        }
    }

    private String setThunbUrl() {
        String url=null;
        if(mImageList.size()==1){
            url=mImageList.get(0).getThumbImgUrl();
        }
        if(mImageList.size()==2){
            url=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getThumbImgUrl();
        }
        if(mImageList.size()==3){
            url=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getImgUrl()+","+mImageList.get(2).getThumbImgUrl();
        }
        return url;
    }

    private String setURL() {
        String url=null;
        if(mImageList.size()==1){
            url=mImageList.get(0).getImgUrl();
        }
        if(mImageList.size()==2){
            url=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getImgUrl();
        }
        if(mImageList.size()==3){
            url=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getImgUrl()+","+mImageList.get(2).getImgUrl();
        }
        return url;
    }


    /*
    * 输入框复位
    * */
    private void reset() {
        isFromPinglun = false;
        if(!et_sendmessage.isEnabled()){
            et_sendmessage.setEnabled(true);
        }
        if(mImageList!=null){
            mImageList.clear();
        }
        if(ll_bottom_edit.getVisibility()==View.VISIBLE){
            ll_bottom_edit.setVisibility(View.GONE);
        }
        if(ll_image_custom_facerelativelayout.getVisibility()==View.VISIBLE){
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

        tv_zhikanlouzhu.setOnClickListener(this);
        tv_daoxu.setOnClickListener(this);
        tv_pinbizuozhe.setOnClickListener(this);
        tv_jubao.setOnClickListener(this);

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
        this.itemPosition = position;
        isFromPinglun=true;
        changeSoftStatus();
    }

    /*
    * 改变输入状态
    * */
    private void changeSoftStatus() {
        if (ll_pinglun.getVisibility() == View.VISIBLE) {
            ll_pinglun.setVisibility(View.GONE);
            FaceRelativeLayout.setVisibility(View.VISIBLE);
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
        MediaManager.playSoundFromUrl(InvitationDetailFromPlatformActivity.this, soundUrl,
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
        mapview_invitation.onResume();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mapview_invitation.onResume();
        MediaManager.resume();
    }


}