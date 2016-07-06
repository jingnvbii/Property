package com.ctrl.forum.ui.activity.rim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.AudioRecordButton;
import com.ctrl.forum.dao.ImageDao;
import com.ctrl.forum.dao.RimDao;
import com.ctrl.forum.dao.SoundDao;
import com.ctrl.forum.entity.CompanyEvaluation;
import com.ctrl.forum.entity.Image;
import com.ctrl.forum.manager.MediaManager;
import com.ctrl.forum.ui.adapter.RimCommentListAdapter;
import com.ctrl.forum.utils.Base64Util;
import com.ctrl.forum.utils.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周边服务----店铺评论页面
 */
public class RimStoreCommentActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.iv_input_add)
    ImageView iv_input_add;  //加号按钮
    @InjectView(R.id.iv_input_yuyin)//语音按钮图片
            ImageView iv_input_yuyin;
    @InjectView(R.id.ll_bottom_edit)//照片 表情布局
            LinearLayout ll_bottom_edit;
    @InjectView(R.id.ll_input_text)//文字输入布局
            LinearLayout ll_input_text;
    @InjectView(R.id.btn_yuyin)//语音按钮
            AudioRecordButton btn_yuyin;
    @InjectView(R.id.ll_facechoose)//文字输入布局
            RelativeLayout ll_facechoose;

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
    @InjectView(R.id.lv_content)//回复
    com.handmark.pulltorefresh.library.PullToRefreshListView lv_content;

    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private int imageFlag = -1;
    List<Image> mImageList = new ArrayList<>();
    private List<ImageView> listImg = new ArrayList<>();
    private String[] items = new String[]{"本地图片", "拍照"};

    private ImageDao Idao;
    private SoundDao sdao;
    private String soundUrl;
    private View viewanim;

    private RimDao rimDao;
    private RimCommentListAdapter rimCommentListAdapter;
    private Intent intent;
    private String rimServiceCompaniesId;
    private List<CompanyEvaluation> companyEvaluationList;
    private int PAGE_NUM=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_store_comment);
        ButterKnife.inject(this);

        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();

        rimCommentListAdapter = new RimCommentListAdapter(this);
        lv_content.setAdapter(rimCommentListAdapter);
        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (companyEvaluationList != null) {
                    companyEvaluationList.clear();
                    PAGE_NUM = 1;
                }
                rimDao.getcollectAroundCompany(rimServiceCompaniesId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (companyEvaluationList != null) {
                    PAGE_NUM += 1;
                    rimDao.getcollectAroundCompany(rimServiceCompaniesId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });
    }

    private void initData() {
        Idao=new ImageDao(this);
        sdao=new SoundDao(this);

        rimDao = new RimDao(this);
        intent = getIntent();
        rimServiceCompaniesId = intent.getStringExtra("rimServiceCompaniesId");
        rimDao.getcollectAroundCompany(rimServiceCompaniesId, PAGE_NUM + "", Constant.PAGE_SIZE + "");

    }

    private void initView() {
        iv_input_add.setOnClickListener(this);
        iv_input_yuyin.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        et_sendmessage.setOnClickListener(this);

        btn_yuyin.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                MessageUtils.showShortToast(RimStoreCommentActivity.this, "语音说话");
                try {
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
        Arad.preferences.putBoolean("isFromStoreComment", true);
        Arad.preferences.flush();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Arad.preferences.putBoolean("isFromStoreComment", false);
        Arad.preferences.flush();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Arad.preferences.putBoolean("isFromStoreComment", true);
        Arad.preferences.flush();
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {return getResources().getString(R.string.store_comment);}


    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==2){
            companyEvaluationList = rimDao.getEvaluations();
            if (companyEvaluationList!=null){
                rimCommentListAdapter.setList(companyEvaluationList);
            }

        }

        if(requestCode==889){
            MessageUtils.showShortToast(this, "语音上传成功");
            soundUrl=sdao.getSoundUrl();
            // replyAdapter.setSoundrUrl(soundUrl);
            Log.i("tag", "soundUrl---" + soundUrl);
            if (soundUrl != null) {
                rimDao.evaluateAroundCompany(Arad.preferences.getString("memberId"),
                        rimServiceCompaniesId,
                        "2",
                        "",
                        soundUrl,
                        "","");
            }

        }

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

        if (requestCode==6){
            reset();
            // 隐藏输入法
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            MessageUtils.showShortToast(this,"评论成功,已提交审核,请等待...");
            if (companyEvaluationList!=null) {
                companyEvaluationList.clear();
            }
            rimDao.getcollectAroundCompany(rimServiceCompaniesId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
        }
    }

    /*
   * 输入框复位
   * */
    private void reset() {
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
        if(ll_image_custom_facerelativelayout.getVisibility()==View.VISIBLE){
            ll_image_custom_facerelativelayout.setVisibility(View.GONE);
        }
    }

    public void goToAlbum() {
        Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
        intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
        AnimUtil.intentSlidIn(RimStoreCommentActivity.this);
    }

    public void goToGraph() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cxh.jpg")));
        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
        AnimUtil.intentSlidIn(RimStoreCommentActivity.this);
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
                    String thePath = Utils.getInstance().getPath(RimStoreCommentActivity.this, uri);
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
            if(et_sendmessage.getText().toString().equals("")&&mImageList.size()>0){
                rimDao.evaluateAroundCompany(Arad.preferences.getString("memberId"),
                        rimServiceCompaniesId,
                        "1",
                        "",
                        "",
                        setURL(),setThunbUrl());

            }else if(!TextUtils.isEmpty(et_sendmessage.getText().toString().trim())&&mImageList.size()==0){
                rimDao.evaluateAroundCompany(Arad.preferences.getString("memberId"),
                        rimServiceCompaniesId,
                        "0",
                        et_sendmessage.getText().toString().trim(),
                        "",
                        "", "");
            }else {
                MessageUtils.showShortToast(this,"回复内容为空");
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
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.iv_input_add:
              changeTextAndImage();
              if (ll_facechoose.getVisibility()==View.VISIBLE){
                  ll_facechoose.setVisibility(View.GONE);
              }
              break;
          case R.id.iv_input_yuyin:
              changeTextAndVoice();
              if(ll_bottom_edit.getVisibility()==View.VISIBLE){
                  ll_bottom_edit.setVisibility(View.GONE);
              }
              if (ll_facechoose.getVisibility()==View.VISIBLE){
                  ll_facechoose.setVisibility(View.GONE);
              }
              break;
          case R.id.btn_send:
              reply();
          case R.id.et_sendmessage:
              if (ll_bottom_edit.getVisibility()==View.VISIBLE){
                  ll_bottom_edit.setVisibility(View.GONE);
                  ll_image_custom_facerelativelayout.setVisibility(View.GONE);
              }
              break;
      }
    }

    /**
     * 文字与图片切换
     */
    private void changeTextAndImage() {
        if(ll_bottom_edit.getVisibility()==View.VISIBLE){
            ll_bottom_edit.setVisibility(View.GONE);
        }else {
            ll_bottom_edit.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 文字与语音切换
     */
    private void changeTextAndVoice() {
        if(ll_input_text.getVisibility()==View.VISIBLE){
            ll_input_text.setVisibility(View.GONE);
            btn_yuyin.setVisibility(View.VISIBLE);
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
        MediaManager.playSoundFromUrl(RimStoreCommentActivity.this, soundUrl,
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
