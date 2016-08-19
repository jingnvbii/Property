package com.ctrl.forum.ui.activity.Invitation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.AudioRecordButton;
import com.ctrl.forum.dao.ImageDao;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.dao.SoundDao;
import com.ctrl.forum.entity.Image;
import com.ctrl.forum.entity.PostReply2;
import com.ctrl.forum.manager.MediaManager;
import com.ctrl.forum.ui.adapter.InvitationCommentDetailAdapter;
import com.ctrl.forum.utils.Base64Util;
import com.ctrl.forum.utils.InputMethodUtils;
import com.ctrl.forum.utils.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 *帖子评论详情 activity
 * Created by jason on 2016/4/12.
 */
public class InvitationCommentDetaioActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.lv_comment_detail)//下拉列表
    PullToRefreshListView lv_comment_detail;
    @InjectView(R.id.iv_input_yuyin)//语音按钮图片
            ImageView iv_input_yuyin;
    @InjectView(R.id.ll_bottom_edit)//照片 表情布局
            LinearLayout ll_bottom_edit;
    @InjectView(R.id.ll_input_text)//文字输入布局
            LinearLayout ll_input_text;
    @InjectView(R.id.iv_input_add)//加号
            ImageView iv_input_add;
    @InjectView(R.id.ll_facechoose)//表情布局
            RelativeLayout ll_facechoose;
    @InjectView(R.id.FaceRelativeLayout)//自定义输入编辑布局
            com.ctrl.forum.face.FaceRelativeLayout FaceRelativeLayout;
    @InjectView(R.id.btn_yuyin)//语音按钮
            AudioRecordButton btn_yuyin;

    @InjectView(R.id.btn_send)//回复
            Button btn_send;
    @InjectView(R.id.et_sendmessage)//回复内容
            EditText et_sendmessage;
    @InjectView(R.id.ll_image_custom_facerelativelayout)//图片布局
            LinearLayout ll_image_custom_facerelativelayout;
    @InjectView(R.id.iv01)//图片1
            ImageView iv01;
    @InjectView(R.id.iv02)//图片2
            ImageView iv02;
    @InjectView(R.id.iv03)//图片3
            ImageView iv03;

    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private int imageFlag = -1;
    List<Image> mImageList = new ArrayList<>();
    private List<ImageView> listImg = new ArrayList<>();
    private String[] items = new String[]{"本地图片", "拍照"};

    private InvitationDao idao;
    private int PAGE_NUM=1;
    private List<PostReply2> listPostReply;
    private InvitationCommentDetailAdapter mInvitationCommentDetailAdapter;
    private ImageDao Idao;
    private boolean isFromPinglun;
    private String id;
    private String reportid;
    private int mPosition;
    private SoundDao sdao;
    private String soundUrl;
    private View viewanim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_comment_detail);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initView() {
        et_sendmessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(InputMethodUtils.isShow(InvitationCommentDetaioActivity.this,et_sendmessage)){
                    if(ll_bottom_edit!=null&&ll_bottom_edit.getVisibility()==View.VISIBLE){
                        ll_bottom_edit.setVisibility(View.GONE);
                        ll_image_custom_facerelativelayout.setVisibility(View.GONE);
                    }
                }else {
                    InputMethodUtils.show(InvitationCommentDetaioActivity.this,et_sendmessage);
                }
                return false;
            }
        });
        iv_input_add.setOnClickListener(this);
        iv_input_yuyin.setOnClickListener(this);
        btn_send.setOnClickListener(this);

        btn_yuyin.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                //     MessageUtils.showShortToast(InvitationCommentDetaioActivity.this, "语音说话");
                try {
                    //  second = seconds;
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        Arad.preferences.putBoolean("isFromCommentDetail", true);
        Arad.preferences.flush();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Arad.preferences.putBoolean("isFromCommentDetail", false);
        Arad.preferences.flush();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Arad.preferences.putBoolean("isFromCommentDetail", true);
        Arad.preferences.flush();
    }

    private void initData() {
        id=getIntent().getStringExtra("id");
        reportid=getIntent().getStringExtra("reportid");

        idao=new InvitationDao(this);
        Idao=new ImageDao(this);
        sdao=new SoundDao(this);
        idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
        mInvitationCommentDetailAdapter=new InvitationCommentDetailAdapter(this);
        lv_comment_detail.setAdapter(mInvitationCommentDetailAdapter);

        lv_comment_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodUtils.hide(InvitationCommentDetaioActivity.this);
            }
        });

        lv_comment_detail.setMode(PullToRefreshBase.Mode.BOTH);
        lv_comment_detail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (listPostReply != null) {
                    listPostReply.clear();
                }
                PAGE_NUM = 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                    }
                }, 500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        idao.requesPostReplyList(getIntent().getStringExtra("id"), "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                    }
                }, 500);
            }
        });
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_comment_detail.onRefreshComplete();
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_comment_detail.onRefreshComplete();
        if(requestCode==5){
          //  MessageUtils.showShortToast(this,"获取帖子评论成功");
            listPostReply=idao.getListPostReply2();
            mInvitationCommentDetailAdapter.setList(listPostReply);
        }

        if (requestCode == 15) {
            isFromPinglun = false;
         //   MessageUtils.showShortToast(this, "回复成功");
            reset();
            if (listPostReply != null) {
                listPostReply.clear();
            }
            PAGE_NUM = 1;
            idao.requesPostReplyList(id, "1", String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
        }

        if(requestCode==889){
           // MessageUtils.showShortToast(this, "语音上传成功");
            soundUrl=sdao.getSoundUrl();
            // replyAdapter.setSoundrUrl(soundUrl);
            Log.i("tag", "soundUrl---" + soundUrl);
            if (soundUrl != null) {
                if (!isFromPinglun) {//无评论
                    idao.requestReplyPost(id, reportid,"", Arad.preferences.getString("memberId"), "2", "", soundUrl, "", "", "", "");
                }else{//有评论
                    idao.requestReplyPost(id,reportid, listPostReply.get(mPosition).getId(), Arad.preferences.getString("memberId"), "2","" ,soundUrl,listPostReply.get(mPosition).getMemberId(), listPostReply.get(mPosition).getMemberFloor(), "", "");
                }


            }
        }

        if (requestCode == 888) {
            showProgress(false);
           // MessageUtils.showShortToast(this, "图片上传成功");
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
    }


    /*
    * 输入框复位
    **/
    private void reset() {
        InputMethodUtils.hide(InvitationCommentDetaioActivity.this);
         isFromPinglun = false;
        et_sendmessage.setText("");
        if(!et_sendmessage.isEnabled()){
            et_sendmessage.setEnabled(true);
        }
        if(mImageList!=null){
            mImageList.clear();
        }
        if(ll_bottom_edit.getVisibility()==View.VISIBLE){
            ll_bottom_edit.setVisibility(View.GONE);
        }
        if(ll_input_text.getVisibility()==View.GONE){
            ll_input_text.setVisibility(View.VISIBLE);
            btn_yuyin.setVisibility(View.GONE);
        }
        if(ll_image_custom_facerelativelayout.getVisibility()==View.VISIBLE){
            ll_image_custom_facerelativelayout.setVisibility(View.GONE);
        }
    }


    public void goToAlbum() {
        Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
        intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
        AnimUtil.intentSlidIn(InvitationCommentDetaioActivity.this);
    }

    public void goToGraph() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cxh.jpg")));
        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
        AnimUtil.intentSlidIn(InvitationCommentDetaioActivity.this);
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
                    String thePath = Utils.getInstance().getPath(InvitationCommentDetaioActivity.this, uri);
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
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 4;
            bitmap = BitmapFactory.decodeFile(path,opts);
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
        if(Arad.preferences.getString("isShielded").equals("1")){
            MessageUtils.showShortToast(InvitationCommentDetaioActivity.this,"您已经被屏蔽，不能发评论");
        }
        if(Arad.preferences.getString("isShielded").equals("0")){

        if(isFromPinglun){

            if(et_sendmessage.getText().toString().equals("")&&mImageList.size()>0){

                idao.requestReplyPost(id,reportid, listPostReply.get(mPosition).getId(),
                        Arad.preferences.getString("memberId"), "1", et_sendmessage.getText().toString().trim(), "",
                        listPostReply.get(mPosition).getMemberId(), listPostReply.get(mPosition).getMemberFloor(),setURL(),
                        setThunbUrl());

            }else if(!TextUtils.isEmpty(et_sendmessage.getText().toString().trim())&&mImageList.size()==0){

                idao.requestReplyPost(id, reportid, listPostReply.get(mPosition).getId(),
                        Arad.preferences.getString("memberId"), "0", et_sendmessage.getText().toString().trim(), "",
                        listPostReply.get(mPosition).getMemberId(), listPostReply.get(mPosition).getMemberFloor(),"",
                        "");

            }else {
                MessageUtils.showShortToast(this,"回复内容为空");
            }



        }else {
            if(et_sendmessage.getText().toString().equals("")&&mImageList.size()>0){

                idao.requestReplyPost(id, reportid, "", Arad.preferences.getString("memberId"), "1",
                        et_sendmessage.getText().toString().trim(), "", "", "",
                        setURL(),setThunbUrl());

            }else if(!TextUtils.isEmpty(et_sendmessage.getText().toString().trim())&&mImageList.size()==0){

                idao.requestReplyPost(id ,reportid, "", Arad.preferences.getString("memberId"), "0",
                        et_sendmessage.getText().toString().trim(), "", "", "",
                        "","");
            }else {
                MessageUtils.showShortToast(this,"回复内容为空");
            }

        }
        }
    }

    public void replyPinglun(int position){
        if(Arad.preferences.getString("isShielded").equals("1")){
            MessageUtils.showShortToast(InvitationCommentDetaioActivity.this,"您已经被屏蔽，不能回复评论");
        }
        reset();
        et_sendmessage.setEnabled(true);
        et_sendmessage.requestFocus();
        if(Arad.preferences.getString("isShielded").equals("0")){
        InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mPosition=position;
        isFromPinglun=true;

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

    @Override
    public String setupToolBarTitle() {
        TextView tv_title = getmTitle();
        tv_title.setTextColor(getResources().getColor(R.color.text_black));
        return "评论";
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left_black);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_input_yuyin:
                changeTextAndVoice();
                break;
            case R.id.iv_input_add:
                changeTextAndImage();
                break;
            case R.id.btn_send:
                reply();
                break;
        }

    }
/*
* 文字与图片切换
* */
    private void changeTextAndImage() {
        InputMethodUtils.hide(InvitationCommentDetaioActivity.this);
        ll_facechoose.setVisibility(View.GONE);
        if(ll_bottom_edit.getVisibility()==View.VISIBLE){
            ll_bottom_edit.setVisibility(View.GONE);
        }else {
            ll_bottom_edit.setVisibility(View.VISIBLE);
        }
    }

    /*
    * 文字与语音切换
    * */
    private void changeTextAndVoice() {
        if(ll_input_text.getVisibility()==View.VISIBLE){
            ll_input_text.setVisibility(View.GONE);
            btn_yuyin.setVisibility(View.VISIBLE);
            InputMethodUtils.hide(InvitationCommentDetaioActivity.this);
        }else {
            ll_input_text.setVisibility(View.VISIBLE);
            btn_yuyin.setVisibility(View.GONE);
        }
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
        MediaManager.playSoundFromUrl(InvitationCommentDetaioActivity.this, soundUrl,
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
    
}
