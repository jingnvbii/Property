package com.ctrl.forum.ui.activity.plot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AndroidUtil;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.ImageDao;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.dao.KeyDao;
import com.ctrl.forum.entity.Image;
import com.ctrl.forum.entity.ItemValues;
import com.ctrl.forum.entity.Post2;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.ui.activity.Invitation.AddContactPhoneActivity;
import com.ctrl.forum.ui.activity.Invitation.CallingCardActivity;
import com.ctrl.forum.ui.activity.Invitation.LocationActivity;
import com.ctrl.forum.ui.activity.WebViewActivity;
import com.ctrl.forum.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 小区发布帖子
 */
public class PlotAddInvitationActivity extends AppToolBarActivity implements View.OnClickListener {
    private KeyDao kdao;
    private ItemValues itemValues;

    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private TextView tv_tel;//联系电话
    private TextView tv_location;//位置
    private TextView tv_name;//名片

    private String channelId;;
    private String name;
    private String adress;
    private String tel;

    private String tv_location_name;//位置标识

    @InjectView(R.id.iv01)
    ImageView iv01;
    @InjectView(R.id.iv02)
    ImageView iv02;
    @InjectView(R.id.iv03)
    ImageView iv03;
    @InjectView(R.id.iv04)
    ImageView iv04;
    @InjectView(R.id.iv05)
    ImageView iv05;
    @InjectView(R.id.iv06)
    ImageView iv06;
    @InjectView(R.id.iv07)
    ImageView iv07;
    @InjectView(R.id.iv08)
    ImageView iv08;
    @InjectView(R.id.iv09)
    ImageView iv09;

    @InjectView(R.id.ll_image_one)//图片布局1
    LinearLayout ll_image_one;
    @InjectView(R.id.ll_image_second)//图片布局2
            LinearLayout ll_image_second;
    @InjectView(R.id.ll_image_third)//图片布局3
            LinearLayout ll_image_third;

    @InjectView(R.id.tv_release)//发布
            TextView tv_release;

    @InjectView(R.id.et_content)//内容
            EditText et_content;

    @InjectView(R.id.tv_release_back)//取消
            TextView tv_release_back;
    @InjectView(R.id.tv_release_save)//存草稿
            TextView tv_release_save;
    @InjectView(R.id.tougao)//投稿协议
            TextView tougao;
    @InjectView(R.id.tv_plot_name)
    TextView tv_plot_name;//小区名称
    @InjectView(R.id.ll_all)
    LinearLayout ll_all;

    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private int imageFlag=-1;
    List<Image>mImageList=new ArrayList<>();
    private List<ImageView> listImg=new ArrayList<>();
    private ImageDao Idao;
    private String locationLongitude;
    private String locationLatitude;
    private String vcardDisplay;
    private InvitationDao idao;
    private String edit = ""; //是否是编辑状态
    private String id; //编辑状态时的帖子id

    private Map<String,String> delIds = new HashMap<>();
    private List<Image> delImages = new ArrayList<>(); //删除的图片
    private List<Image> addImages = new ArrayList<>(); //添加的图片

    private boolean isSave;//是否存草稿
    private String delId = "";
    private List<Bitmap> newBmp = new ArrayList<>();//新加载的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_add_invitation);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.inject(this);

        edit = getIntent().getStringExtra("edit");

        Init();
        initView();

        checkActivity();
    }

    private void checkActivity() {
        id = getIntent().getStringExtra("id");
        if (id!=null && !id.equals("")){
            idao = new InvitationDao(this);
            idao.requesPostDetail(id, Arad.preferences.getString("memberId"));
        }
    }

    private void initView() {
        idao=new InvitationDao(this);
        idao.requesItemCategory2(channelId,"1");
        kdao = new KeyDao(this);

        tv_tel=(TextView)findViewById(R.id.tv_tel);
        tv_location=(TextView)findViewById(R.id.tv_location);
        tv_name=(TextView)findViewById(R.id.tv_name);

        tv_tel.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        tv_release_back.setOnClickListener(this);
        tv_release.setOnClickListener(this);
        tv_release_save.setOnClickListener(this);
        tougao.setOnClickListener(this);
        ll_all.setOnClickListener(this);

        //初始化控件宽高
        setImageViewWidth(iv01);
        listImg.add(iv01);
        listImg.add(iv02);
        listImg.add(iv03);
        listImg.add(iv04);
        listImg.add(iv05);
        listImg.add(iv06);
        listImg.add(iv07);
        listImg.add(iv08);
        listImg.add(iv09);
        iv01.setOnClickListener(this);
        iv02.setOnClickListener(this);
        iv03.setOnClickListener(this);
        iv04.setOnClickListener(this);
        iv05.setOnClickListener(this);
        iv06.setOnClickListener(this);
        iv07.setOnClickListener(this);
        iv08.setOnClickListener(this);
        iv09.setOnClickListener(this);
    }

    private void showDelDialog(final int posititon) {
        new AlertDialog.Builder(this)
                .setTitle("确定删除吗？")
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // iDao.requestDelImg(iDao.getImg().getImgId());
                        delImages.add(mImageList.get(imageFlag-1)); //删除的图片
                        delImg(imageFlag);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void Init() {
        tv_plot_name.setText(Arad.preferences.getString("communityName"));

        channelId=getIntent().getStringExtra("channelId");
        Idao=new ImageDao(this);

        if (Arad.preferences.getBoolean("isCallingChecked")){
            vcardDisplay = "1";
        }else{
            vcardDisplay = "0";
        }

    }
    public void showDialog(){

        pop = new PopupWindow(PlotAddInvitationActivity.this);

        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cxh.jpg")));
                startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                AnimUtil.intentSlidIn(PlotAddInvitationActivity.this);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
                intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                AnimUtil.intentSlidIn(PlotAddInvitationActivity.this);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        pop.showAtLocation(ll_popup, Gravity.BOTTOM,0,0);

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 3){
            Post2 post2 = idao.getPost2(); //帖子内容
            name = post2.getContactName();
            adress = post2.getContactAddress();
            tel = post2.getContactPhone();
            locationLongitude = post2.getLocationLongitude();
            locationLatitude = post2.getLocationLatitude();
            tv_location_name = post2.getLocationName();
            et_content.setText(post2.getContent());

            //名片
            vcardDisplay = post2.getVcardDisplay();
            if (vcardDisplay.equals("0")){
                Arad.preferences.putBoolean("isCallingChecked",false);
            }else{
                Arad.preferences.putBoolean("isCallingChecked",true);
            }
            Arad.preferences.flush();

            List<PostImage> listPostImage = idao.getListPostImage();//图片
            for (int i=0;i<listPostImage.size();i++){
                PostImage post = listPostImage.get(i);
                Image image=new Image();
                image.setImgUrl(post.getImg());
                image.setThumbImgUrl(post.getThumbImg());
                mImageList.add(image);
                delIds.put(mImageList.get(i).getImgUrl(),listPostImage.get(i).getId());
            }
            if (mImageList.size()>3){
                ll_image_second.setVisibility(View.VISIBLE);
            }
            if (mImageList.size()>7){
                ll_image_third.setVisibility(View.VISIBLE);
            }
            setBitmapImg();
            bitmapClick();
        }

        if(requestCode==7){
            if(isSave){
                MessageUtils.showShortToast(this, "存草稿成功");
            }else {
                MessageUtils.showShortToast(this, "帖子发布成功");
            }
            isSave=false;
            finish();
        }

        if(requestCode==888){
            showProgress(false);
            MessageUtils.showShortToast(this, "图片上传成功");
            Image image=Idao.getImage();
            mImageList.add(image);
            addImages.add(image); //添加的图片
            setBitmapImg();
            bitmapClick();
        }
        if(requestCode==6){
            if(isSave){
                MessageUtils.showShortToast(this, "存草稿成功");
            }else {
                MessageUtils.showShortToast(this, "帖子发布成功");
            }
            isSave=false;
            finish();
        }
    }

    private void bitmapClick() {

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
        iv04.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mImageList.size() >= 4) {
                    imageFlag = 4;
                    showDelDialog(4);
                }
                return true;
            }
        });
        iv05.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mImageList.size() >= 5) {
                    imageFlag = 5;
                    showDelDialog(5);
                }
                return true;
            }
        });
        iv06.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mImageList.size() >= 6) {
                    imageFlag = 6;
                    showDelDialog(6);
                }
                return true;
            }
        });
        iv07.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mImageList.size() >= 7) {
                    imageFlag = 7;
                    showDelDialog(7);
                }
                return true;
            }
        });
        iv08.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mImageList.size() >= 8) {
                    imageFlag = 8;
                    showDelDialog(8);
                }
                return true;
            }
        });
        iv09.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mImageList.size() >= 9) {
                    imageFlag = 9;
                    showDelDialog(9);
                }
                return true;
            }
        });

    }

    private boolean checkInput(){
        if (Arad.preferences.getBoolean("isCallingChecked")){
            if(TextUtils.isEmpty(name)){
                MessageUtils.showShortToast(PlotAddInvitationActivity.this,"联系人名称为空");
                return false;
            }
            if(TextUtils.isEmpty(adress)){
                MessageUtils.showShortToast(PlotAddInvitationActivity.this, "联系人地址为空");
                return false;
            }
            if(TextUtils.isEmpty(tel)){
                MessageUtils.showShortToast(PlotAddInvitationActivity.this, "联系人电话为空");
                return false;
            }
            return true;}
        else {
            return true;
        }
    }
    /*
    * 原图url串
    * */
    public String getImagesUrl(List<Image>mImageList){
        String imagesUrl=null;
        switch (mImageList.size()){
            case 0:
                imagesUrl=null;
                break;
            case 1:
                imagesUrl=mImageList.get(0).getImgUrl();
                break;
            case 2:
                imagesUrl=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getImgUrl();
                break;
            case 3:
                imagesUrl=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getImgUrl()+","+mImageList.get(2).getImgUrl();
                break;
            case 4:
                imagesUrl=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getImgUrl()+","+mImageList.get(2).getImgUrl()+","+mImageList.get(3).getImgUrl();
                break;
            case 5:
                imagesUrl=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getImgUrl()+","+mImageList.get(2).getImgUrl()
                        +","+mImageList.get(3).getImgUrl()+","+mImageList.get(4).getImgUrl();
                break;
            case 6:
                imagesUrl=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getImgUrl()+","+mImageList.get(2).getImgUrl()
                        +","+mImageList.get(3).getImgUrl()+","+mImageList.get(4).getImgUrl()+","+mImageList.get(5).getImgUrl();
                break;
            case 7:
                imagesUrl=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getImgUrl()+","+mImageList.get(2).getImgUrl()
                        +","+mImageList.get(3).getImgUrl()+","+mImageList.get(4).getImgUrl()+","+mImageList.get(5).getImgUrl()+","+
                        mImageList.get(6).getImgUrl();
                break;
            case 8:
                imagesUrl=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getImgUrl()+","+mImageList.get(2).getImgUrl()
                        +","+mImageList.get(3).getImgUrl()+","+mImageList.get(4).getImgUrl()+","+mImageList.get(5).getImgUrl()+","+
                        mImageList.get(6).getImgUrl()+","+mImageList.get(7).getImgUrl();
                break;
            case 9:
                imagesUrl=mImageList.get(0).getImgUrl()+","+mImageList.get(1).getImgUrl()+","+mImageList.get(2).getImgUrl()
                        +","+mImageList.get(3).getImgUrl()+","+mImageList.get(4).getImgUrl()+","+mImageList.get(5).getImgUrl()+","+
                        mImageList.get(6).getImgUrl()+","+mImageList.get(7).getImgUrl()+","+mImageList.get(8).getImgUrl();
                break;
        }
        return imagesUrl;
    }
    /*
    * 缩略图url串
    * */
    public String getThumbImagesUrl(List<Image>mImageList){
        String thumbImagesUrl=null;
        switch (mImageList.size()){
            case 0:
                thumbImagesUrl=null;
                break;
            case 1:
                thumbImagesUrl=mImageList.get(0).getThumbImgUrl();
                break;
            case 2:
                thumbImagesUrl=mImageList.get(0).getThumbImgUrl()+","+mImageList.get(1).getThumbImgUrl();
                break;
            case 3:
                thumbImagesUrl=mImageList.get(0).getThumbImgUrl()+","+mImageList.get(1).getThumbImgUrl()+","+mImageList.get(2).getThumbImgUrl();
                break;
            case 4:
                thumbImagesUrl=mImageList.get(0).getThumbImgUrl()+","+mImageList.get(1).getThumbImgUrl()+","+mImageList.get(2).getThumbImgUrl()+","+mImageList.get(3).getThumbImgUrl();
                break;
            case 5:
                thumbImagesUrl=mImageList.get(0).getThumbImgUrl()+","+mImageList.get(1).getThumbImgUrl()+","+mImageList.get(2).getThumbImgUrl()
                        +","+mImageList.get(3).getThumbImgUrl()+","+mImageList.get(4).getThumbImgUrl();
                break;
            case 6:
                thumbImagesUrl=mImageList.get(0).getThumbImgUrl()+","+mImageList.get(1).getThumbImgUrl()+","+mImageList.get(2).getThumbImgUrl()
                        +","+mImageList.get(3).getThumbImgUrl()+","+mImageList.get(4).getThumbImgUrl()+","+mImageList.get(5).getThumbImgUrl();
                break;
            case 7:
                thumbImagesUrl=mImageList.get(0).getThumbImgUrl()+","+mImageList.get(1).getThumbImgUrl()+","+mImageList.get(2).getThumbImgUrl()
                        +","+mImageList.get(3).getThumbImgUrl()+","+mImageList.get(4).getThumbImgUrl()+","+mImageList.get(5).getThumbImgUrl()+","+
                        mImageList.get(6).getThumbImgUrl();
                break;
            case 8:
                thumbImagesUrl=mImageList.get(0).getThumbImgUrl()+","+mImageList.get(1).getThumbImgUrl()+","+mImageList.get(2).getThumbImgUrl()
                        +","+mImageList.get(3).getThumbImgUrl()+","+mImageList.get(4).getThumbImgUrl()+","+mImageList.get(5).getThumbImgUrl()+","+
                        mImageList.get(6).getThumbImgUrl()+","+mImageList.get(7).getThumbImgUrl();
                break;
            case 9:
                thumbImagesUrl=mImageList.get(0).getThumbImgUrl()+","+mImageList.get(1).getThumbImgUrl()+","+mImageList.get(2).getThumbImgUrl()
                        +","+mImageList.get(3).getThumbImgUrl()+","+mImageList.get(4).getThumbImgUrl()+","+mImageList.get(5).getThumbImgUrl()+","+
                        mImageList.get(6).getThumbImgUrl()+","+mImageList.get(7).getThumbImgUrl()+","+mImageList.get(8).getThumbImgUrl();
                break;
        }
        return thumbImagesUrl;
    }

    //删除的图片的url转成id形式的String
    public String getDelImageId(List<Image> imageId){
        if (imageId.size()!=0) {
            List<String> delImageIds  = new ArrayList<>();
            for (int i=0;i< imageId.size();i++){
                delImageIds.add(delIds.get(imageId.get(i).getImgUrl()));
            }
            for (int i = 0; i < delImageIds.size(); i++) {
                //转换成字符串
                delId = delId + delImageIds.get(i) + ",";
            }
            return delId.substring(0, delId.length() - 1);
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.tv_release_save: //存草稿
                //遍历比价两个集合，若是有相同的，则为删除的图片的url,不同的，增加的集合里面是新增加的图片的url,删除的集合里是删除的图片的url
                for (int i=0;i<addImages.size();i++){
                    String addUrl = addImages.get(i).getImgUrl();
                    for (int j=0;j<delImages.size();j++){
                        String delurl = delImages.get(j).getImgUrl();
                        if (delurl.equals(addUrl)){
                            addImages.remove(i);
                            delImages.remove(j);
                        }
                    }
                }
                isSave=true;
                String imagesUrl1=getImagesUrl(mImageList);
                String thumbImagesUrl1= getThumbImagesUrl(mImageList);

                if (!et_content.getText().toString().equals("") || imagesUrl1!=null){
                    if (edit == null || edit.equals("")) {
                        idao.requesReleasePost(
                                Arad.preferences.getString("memberId"),
                                Arad.preferences.getString("communityId"),
                                "1",
                                "0",
                                "0",
                                "",
                                et_content.getText().toString().trim(),
                                vcardDisplay,
                                name,
                                adress,
                                tel,
                                locationLongitude,
                                locationLatitude,
                                tv_location_name,
                                imagesUrl1,
                                thumbImagesUrl1
                        );
                    } else {//编辑状态下存草稿
                        // getImagesUrl(imageId);
                        idao.requesPlotPostEditor(
                                id,
                                "1",
                                Arad.preferences.getString("communityId"),
                                "0",
                                "0",
                                "",
                                et_content.getText().toString().trim(),
                                vcardDisplay,
                                name,
                                adress,
                                tel,
                                locationLongitude,
                                locationLatitude,
                                tv_location_name,
                                getDelImageId(delImages),  //删除图片的id的字符串
                                getImagesUrl(addImages),
                                getThumbImagesUrl(addImages)); //不对
                    }
                }else {
                    MessageUtils.showShortToast(this,"内容不能为空!");
                }
                break;
            case R.id.tv_release: //发布

                //遍历比价两个集合，若是有相同的，则为删除的图片的url,不同的，增加的集合里面是新增加的图片的url,删除的集合里是删除的图片的url
                for (int i=0;i<addImages.size();i++){
                    String addUrl = addImages.get(i).getImgUrl();
                    for (int j=0;j<delImages.size();j++){
                        String delurl = delImages.get(j).getImgUrl();
                        if (delurl.equals(addUrl)){
                            addImages.remove(i);
                            delImages.remove(j);
                        }
                    }
                }

                String imagesUrl=getImagesUrl(mImageList);
                String thumbImagesUrl= getThumbImagesUrl(mImageList);
               /* if(et_content.getText().toString().trim().length()<20){
                    MessageUtils.showShortToast(this,"帖子内容少于20个字符");
                    return;
                }*/
                if (!et_content.getText().toString().equals("") || imagesUrl!=null) {
                    if (checkInput()) {
                        if (edit == null || edit.equals("")) {
                            idao.requesReleasePost(
                                    Arad.preferences.getString("memberId"),
                                    Arad.preferences.getString("communityId"),
                                    "1",
                                    "1",
                                    "0",
                                    "",
                                    et_content.getText().toString().trim(),
                                    vcardDisplay,
                                    name,
                                    adress,
                                    tel,
                                    locationLongitude,
                                    locationLatitude,
                                    tv_location_name,
                                    imagesUrl,
                                    thumbImagesUrl
                            );
                        } else {//编辑状态下发布帖子
                            idao.requesPlotPostEditor(
                                    id,
                                    "1",
                                    Arad.preferences.getString("communityId"),
                                    "0",
                                    "1",
                                    "",
                                    et_content.getText().toString().trim(),
                                    vcardDisplay,
                                    name,
                                    adress,
                                    tel,
                                    locationLongitude,
                                    locationLatitude,
                                    tv_location_name,
                                    getDelImageId(delImages),  //删除图片的id的字符串
                                    getImagesUrl(addImages),
                                    getThumbImagesUrl(addImages)); //不对
                        }
                    }
                }else{
                    MessageUtils.showShortToast(this,"内容不能为空!");
                }
                break;
            case R.id.tv_tel:
                intent=new Intent(this,AddContactPhoneActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("adress",adress);
                intent.putExtra("tel",tel);
                startActivityForResult(intent, 100);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_location:
                intent=new Intent(this,LocationActivity.class);
                intent.putExtra("locationLongitude",locationLongitude);
                intent.putExtra("locationLatitude",locationLatitude);
                intent.putExtra("tv_location_name",tv_location_name);
                startActivityForResult(intent, 101);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_name:
                intent=new Intent(this,CallingCardActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_release_back:
                onBackPressed();
                break;
            case R.id.iv01:
                if(mImageList.size() >= 1){
                    //
                } else {
                    showDialog();
                }

                //setBitmapImg();
                break;
            case R.id.iv02:
                if(mImageList.size() >= 2){
                    //
                } else {
                    showDialog();
                }

                //setBitmapImg();
                break;
            case R.id.iv03:
                if(mImageList.size() >= 3){
                    //
                } else {
                    showDialog();
                }

                //setBitmapImg();
                break;
            case R.id.iv04:
                if(mImageList.size() >= 4){
                    //
                } else {
                    showDialog();
                }

                //setBitmapImg();
                break;
            case R.id.iv05:
                if(mImageList.size() >= 5){
                    //
                } else {
                    showDialog();
                }

                //setBitmapImg();
                break;
            case R.id.iv06:
                if(mImageList.size() >= 6){
                    //
                } else {
                    showDialog();
                }

                //setBitmapImg();
                break;
            case R.id.iv07:
                if(mImageList.size() >= 7){
                    //
                } else {
                    showDialog();
                }

                //setBitmapImg();
                break;
            case R.id.iv08:
                if(mImageList.size() >= 8){
                    //
                } else {
                    showDialog();
                }

                //setBitmapImg();
                break;
            case R.id.iv09:
                if(mImageList.size() >=9){
                    //
                } else {
                    showDialog();
                }
                //setBitmapImg();
                break;
            case R.id.tougao:
                Intent intent1 = new Intent(this,WebViewActivity.class);
                intent1.putExtra("title","投稿协议");
                startActivity(intent1);
                break;
            case R.id.ll_all:
                InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen=imm.isActive();
                //isOpen若返回true，则表示输入法打开
                if(isOpen){
                    imm.hideSoftInputFromWindow(PlotAddInvitationActivity.this.getCurrentFocus().getWindowToken()
                            ,InputMethodManager.HIDE_NOT_ALWAYS);
                    //接受软键盘输入的编辑文本或其它视图
                    //imm.showSoftInput(et_content,InputMethodManager.SHOW_FORCED);
                }
                break;
        }
    }

    private void getImageToView1(String path) {
        Bitmap bitmap ;
        try{
            bitmap = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
            byte[] buffer = out.toByteArray();
            byte[] encode = Base64.encode(buffer, Base64.DEFAULT);

            String photo = new String(encode);
            if (photo != null){
                // Log.d("demo","上传方法2");
                /**调用后台方法  将图片上传**/
                //  String imgData = photo;
                //showProgress(true);

                Idao.requestUploadImage(photo);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            //加上这个判断就好了
            if(resultCode== Activity.RESULT_CANCELED)
            {
                return;
            }
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    Uri uri = data.getData();
                    String thePath = Utils.getInstance().getPath(this, uri);
                    getImageToView1(thePath);
                    break;
                case CAMERA_REQUEST_CODE:
                    getImageToView1(Environment.getExternalStorageDirectory()+"/cxh.jpg");
                    break;

                case 100:
                    if(resultCode==RESULT_OK){

                        name=data.getStringExtra("name");
                        adress=data.getStringExtra("adress");
                        tel=data.getStringExtra("tel");

                    }
                    break;
                case 101:
                    if(resultCode==RESULT_CANCELED){
                        tv_location_name="";
                        locationLongitude="";//经度
                        locationLatitude="";//纬度
                    }
                    if(resultCode==RESULT_OK){
                        tv_location_name=data.getStringExtra("location");
                        locationLongitude=data.getStringExtra("locationLongitude");
                        locationLatitude=data.getStringExtra("locationLatitude");
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImageViewWidth(ImageView imageView){
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int w= (AndroidUtil.getDeviceWidth(this)-20)/4;
        android.util.Log.d("demo", "width : " + w);
        params.height=w;
        android.util.Log.d("demo", "height : " + params.height);
        imageView.setLayoutParams(params);

    }

    private void setBitmapImg(){

        setImageViewWidth(iv01);
        setImageViewWidth(iv02);
        setImageViewWidth(iv03);
        setImageViewWidth(iv04);
        setImageViewWidth(iv05);
        setImageViewWidth(iv06);
        setImageViewWidth(iv07);
        setImageViewWidth(iv08);
        setImageViewWidth(iv09);

        if (mImageList != null){

            if(mImageList.size()==0){
                iv01.setVisibility(View.VISIBLE);
                iv01.setImageResource(R.mipmap.add_pic);
                iv02.setVisibility(View.INVISIBLE);
                iv03.setVisibility(View.INVISIBLE);
                iv04.setVisibility(View.INVISIBLE);
                iv05.setVisibility(View.INVISIBLE);
                iv06.setVisibility(View.INVISIBLE);
                iv07.setVisibility(View.INVISIBLE);
                iv08.setVisibility(View.INVISIBLE);
                iv09.setVisibility(View.INVISIBLE);
            }
            if(mImageList.size() == 1) {
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv02.setImageResource(R.mipmap.add_pic);
                iv03.setVisibility(View.INVISIBLE);
                iv04.setVisibility(View.INVISIBLE);
                iv06.setVisibility(View.INVISIBLE);
                iv07.setVisibility(View.INVISIBLE);
                iv08.setVisibility(View.INVISIBLE);
                iv09.setVisibility(View.INVISIBLE);
                iv05.setVisibility(View.INVISIBLE);


                for(int i = 0 ; i < mImageList.size() ; i++){
                     Log.i("tag","mImageList----"+mImageList.size());
                      Log.i("tag","mImageList  url----"+mImageList.get(i).getImgUrl());
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }

            }

            if(mImageList.size() == 2){
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv03.setVisibility(View.VISIBLE);
                iv03.setImageResource(R.mipmap.add_pic);
                iv04.setVisibility(View.INVISIBLE);
                iv05.setVisibility(View.INVISIBLE);
                iv06.setVisibility(View.INVISIBLE);
                iv07.setVisibility(View.INVISIBLE);
                iv08.setVisibility(View.INVISIBLE);
                iv09.setVisibility(View.INVISIBLE);


                for(int i = 0 ; i < mImageList.size() ; i ++){
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }

            }

            if(mImageList.size() == 3){
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv03.setVisibility(View.VISIBLE);
                iv04.setVisibility(View.VISIBLE);
                iv04.setImageResource(R.mipmap.add_pic);
                iv05.setVisibility(View.INVISIBLE);
                iv06.setVisibility(View.INVISIBLE);
                iv07.setVisibility(View.INVISIBLE);
                iv08.setVisibility(View.INVISIBLE);
                iv09.setVisibility(View.INVISIBLE);


                for(int i = 0 ; i < mImageList.size() ; i ++){
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }
            }

            if (mImageList.size() == 4){
                ll_image_second.setVisibility(View.VISIBLE);


                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv03.setVisibility(View.VISIBLE);
                iv04.setVisibility(View.VISIBLE);
                iv05.setVisibility(View.VISIBLE);
                iv05.setImageResource(R.mipmap.add_pic);
                iv06.setVisibility(View.INVISIBLE);
                iv07.setVisibility(View.INVISIBLE);
                iv08.setVisibility(View.INVISIBLE);
                iv09.setVisibility(View.INVISIBLE);

                for(int i = 0 ; i < mImageList.size() ; i ++){
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }
            }
            if (mImageList.size() ==5){
                //  ll_image_second.setVisibility(View.VISIBLE);
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv03.setVisibility(View.VISIBLE);
                iv04.setVisibility(View.VISIBLE);
                iv05.setVisibility(View.VISIBLE);
                iv06.setVisibility(View.VISIBLE);
                iv06.setImageResource(R.mipmap.add_pic);
                iv07.setVisibility(View.INVISIBLE);
                iv08.setVisibility(View.INVISIBLE);
                iv09.setVisibility(View.INVISIBLE);

                for(int i = 0 ; i < mImageList.size() ; i ++){
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }
            }
            if (mImageList.size() == 6){
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv03.setVisibility(View.VISIBLE);
                iv04.setVisibility(View.VISIBLE);
                iv05.setVisibility(View.VISIBLE);
                iv06.setVisibility(View.VISIBLE);
                iv07.setVisibility(View.VISIBLE);
                iv07.setImageResource(R.mipmap.add_pic);
                iv08.setVisibility(View.INVISIBLE);
                iv09.setVisibility(View.INVISIBLE);

                for(int i = 0 ; i < mImageList.size() ; i ++){
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }
            }
            if (mImageList.size() == 7){
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv03.setVisibility(View.VISIBLE);
                iv04.setVisibility(View.VISIBLE);
                iv05.setVisibility(View.VISIBLE);
                iv06.setVisibility(View.VISIBLE);
                iv07.setVisibility(View.VISIBLE);
                iv08.setVisibility(View.VISIBLE);
                iv08.setImageResource(R.mipmap.add_pic);
                iv09.setVisibility(View.INVISIBLE);

                for(int i = 0 ; i < mImageList.size() ; i ++){
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }
            }
            if (mImageList.size() == 8){
                ll_image_third.setVisibility(View.VISIBLE);
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv03.setVisibility(View.VISIBLE);
                iv04.setVisibility(View.VISIBLE);
                iv05.setVisibility(View.VISIBLE);
                iv06.setVisibility(View.VISIBLE);
                iv07.setVisibility(View.VISIBLE);
                iv08.setVisibility(View.VISIBLE);
                iv09.setVisibility(View.VISIBLE);
                iv09.setImageResource(R.mipmap.add_pic);

                for(int i = 0 ; i < mImageList.size() ; i ++){
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }
            }
            if (mImageList.size() == 9){
                //  ll_image_third.setVisibility(View.VISIBLE);
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.VISIBLE);
                iv03.setVisibility(View.VISIBLE);
                iv04.setVisibility(View.VISIBLE);
                iv05.setVisibility(View.VISIBLE);
                iv06.setVisibility(View.VISIBLE);
                iv07.setVisibility(View.VISIBLE);
                iv08.setVisibility(View.VISIBLE);
                iv09.setVisibility(View.VISIBLE);

                for(int i = 0 ; i < mImageList.size() ; i ++){
                    Arad.imageLoader.load(mImageList.get(i).getThumbImgUrl()).into(listImg.get(i));
                }
            }
        }
    }

    private void delImg(int imgFlg)  {
        if(mImageList != null){

            /**长按 第一张图*/
            if(imgFlg == 1){
                if(mImageList.size() == 1){

                    mImageList.remove(0);

                    iv01.setVisibility(View.VISIBLE);
                    iv01.setImageResource(R.mipmap.add_pic);
                    iv02.setVisibility(View.INVISIBLE);
                    iv03.setVisibility(View.INVISIBLE);
                    iv04.setVisibility(View.INVISIBLE);
                    iv05.setVisibility(View.INVISIBLE);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();

                }

                if(mImageList.size() == 2){
                    mImageList.remove(0);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv02.setImageResource(R.mipmap.add_pic);
                    iv03.setVisibility(View.INVISIBLE);
                    iv04.setVisibility(View.INVISIBLE);
                    iv05.setVisibility(View.INVISIBLE);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

                if(mImageList.size() == 3){
                    mImageList.remove(0);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv03.setImageResource(R.mipmap.add_pic);
                    iv04.setVisibility(View.INVISIBLE);
                    iv05.setVisibility(View.INVISIBLE);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }
                if(mImageList.size() == 4){
                    mImageList.remove(0);
                    ll_image_second.setVisibility(View.GONE);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv04.setImageResource(R.mipmap.add_pic);
                    iv05.setVisibility(View.INVISIBLE);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if (mImageList.size() == 5){
                    mImageList.remove(0);

                    //  ll_image_second.setVisibility(View.GONE);
                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv05.setImageResource(R.mipmap.add_pic);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 6){
                    mImageList.remove(0);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv06.setImageResource(R.mipmap.add_pic);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 7){
                    mImageList.remove(0);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv07.setImageResource(R.mipmap.add_pic);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 8){
                    mImageList.remove(0);
                    ll_image_third.setVisibility(View.GONE);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv08.setImageResource(R.mipmap.add_pic);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() ==9){
                    mImageList.remove(0);
                    //  ll_image_third.setVisibility(View.GONE);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv09.setVisibility(View.VISIBLE);
                    iv09.setImageResource(R.mipmap.add_pic);
                    setBitmapImg();
                }
            }

            /**长按 第二张图*/
            if(imgFlg == 2){
                if(mImageList.size() == 1){
                    //
                }

                if(mImageList.size() == 2){
                    mImageList.remove(1);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv02.setImageResource(R.mipmap.add_pic);
                    iv03.setVisibility(View.INVISIBLE);
                    iv04.setVisibility(View.INVISIBLE);
                    iv05.setVisibility(View.INVISIBLE);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);

                    setBitmapImg();
                }

                if(mImageList.size() == 3){
                    mImageList.remove(1);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv03.setImageResource(R.mipmap.add_pic);
                    iv04.setVisibility(View.INVISIBLE);
                    iv05.setVisibility(View.INVISIBLE);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);



                    setBitmapImg();
                }

                if(mImageList.size() == 4){
                    mImageList.remove(1);
                    ll_image_second.setVisibility(View.GONE);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv04.setImageResource(R.mipmap.add_pic);
                    iv05.setVisibility(View.INVISIBLE);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

                if(mImageList.size() == 5){
                    mImageList.remove(1);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv05.setImageResource(R.mipmap.add_pic);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 6){
                    mImageList.remove(1);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv06.setImageResource(R.mipmap.add_pic);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 7){
                    mImageList.remove(1);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv07.setImageResource(R.mipmap.add_pic);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 8){
                    mImageList.remove(1);
                    ll_image_third.setVisibility(View.GONE);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv08.setImageResource(R.mipmap.add_pic);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() ==9){
                    mImageList.remove(1);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv09.setVisibility(View.VISIBLE);
                    iv09.setImageResource(R.mipmap.add_pic);
                    setBitmapImg();
                }
            }


            /**长按 第三张图*/
            if(imgFlg == 3){
                if(mImageList.size() == 1){
//
                }

                if(mImageList.size() == 2){
                    //
                }

                if(mImageList.size() == 3){
                    mImageList.remove(2);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv03.setImageResource(R.mipmap.add_pic);
                    iv04.setVisibility(View.INVISIBLE);
                    iv05.setVisibility(View.INVISIBLE);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);



                    setBitmapImg();
                }

                if(mImageList.size() == 4){
                    mImageList.remove(2);
                    ll_image_second.setVisibility(View.GONE);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv04.setImageResource(R.mipmap.add_pic);
                    iv05.setVisibility(View.INVISIBLE);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

                if(mImageList.size() == 5){
                    mImageList.remove(2);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv05.setImageResource(R.mipmap.add_pic);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 6){
                    mImageList.remove(2);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv06.setImageResource(R.mipmap.add_pic);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 7){
                    mImageList.remove(2);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv07.setImageResource(R.mipmap.add_pic);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 8){
                    mImageList.remove(2);
                    ll_image_third.setVisibility(View.GONE);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv08.setImageResource(R.mipmap.add_pic);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() ==9){
                    mImageList.remove(2);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv09.setVisibility(View.VISIBLE);
                    iv09.setImageResource(R.mipmap.add_pic);
                    setBitmapImg();
                }
            }
            /**长按 第四张图*/
            if(imgFlg == 4){
                if(mImageList.size() == 1){
//
                }

                if(mImageList.size() == 2){
                    //
                }

                if(mImageList.size() == 3){

                }

                if(mImageList.size() == 4){
                    mImageList.remove(3);
                    ll_image_second.setVisibility(View.GONE);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv04.setImageResource(R.mipmap.add_pic);
                    iv05.setVisibility(View.INVISIBLE);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

                if(mImageList.size() == 5){
                    mImageList.remove(3);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv05.setImageResource(R.mipmap.add_pic);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 6){
                    mImageList.remove(3);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv06.setImageResource(R.mipmap.add_pic);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 7){
                    mImageList.remove(3);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv07.setImageResource(R.mipmap.add_pic);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 8){
                    mImageList.remove(3);
                    ll_image_third.setVisibility(View.GONE);



                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv08.setImageResource(R.mipmap.add_pic);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() ==9){
                    mImageList.remove(3);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv09.setVisibility(View.VISIBLE);
                    iv09.setImageResource(R.mipmap.add_pic);
                    setBitmapImg();
                }
            }
            /**长按 第五张图*/
            if(imgFlg == 5){
                if(mImageList.size() == 1){
//
                }

                if(mImageList.size() == 2){
                    //
                }

                if(mImageList.size() == 3){

                }

                if(mImageList.size() == 4){
                    //
                }

                if(mImageList.size() == 5){
                    mImageList.remove(4);

                    // ll_image_second.setVisibility(View.GONE);
                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv05.setImageResource(R.mipmap.add_pic);
                    iv06.setVisibility(View.INVISIBLE);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 6){
                    mImageList.remove(4);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv06.setImageResource(R.mipmap.add_pic);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 7){
                    mImageList.remove(4);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv07.setImageResource(R.mipmap.add_pic);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 8){
                    mImageList.remove(4);
                    ll_image_third.setVisibility(View.GONE);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv08.setImageResource(R.mipmap.add_pic);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() ==9){
                    mImageList.remove(4);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv09.setVisibility(View.VISIBLE);
                    iv09.setImageResource(R.mipmap.add_pic);
                    setBitmapImg();
                }
            }
            /**长按 第六张图*/
            if(imgFlg == 6){
                if(mImageList.size() == 1){
//
                }

                if(mImageList.size() == 2){
                    //
                }

                if(mImageList.size() == 3){

                }

                if(mImageList.size() == 4){
                    //
                }

                if(mImageList.size() == 5){
                    //
                }
                if(mImageList.size() == 6){
                    mImageList.remove(5);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv06.setImageResource(R.mipmap.add_pic);
                    iv07.setVisibility(View.INVISIBLE);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 7){
                    mImageList.remove(5);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv07.setImageResource(R.mipmap.add_pic);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 8){
                    mImageList.remove(5);
                    ll_image_third.setVisibility(View.GONE);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv08.setImageResource(R.mipmap.add_pic);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() ==9){
                    mImageList.remove(5);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv09.setVisibility(View.VISIBLE);
                    iv09.setImageResource(R.mipmap.add_pic);
                    setBitmapImg();
                }
            }
            /**长按 第七张图*/
            if(imgFlg == 7){
                if(mImageList.size() == 1){
//
                }

                if(mImageList.size() == 2){
                    //
                }

                if(mImageList.size() == 3){

                }

                if(mImageList.size() == 4){
                    //
                }

                if(mImageList.size() == 5){
                    //
                }
                if(mImageList.size() == 6){
                    //
                }
                if(mImageList.size() == 7){
                    mImageList.remove(6);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv07.setImageResource(R.mipmap.add_pic);
                    iv08.setVisibility(View.INVISIBLE);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() == 8){
                    mImageList.remove(6);
                    ll_image_third.setVisibility(View.GONE);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv08.setImageResource(R.mipmap.add_pic);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() ==9){
                    mImageList.remove(6);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv09.setVisibility(View.VISIBLE);
                    iv09.setImageResource(R.mipmap.add_pic);
                    setBitmapImg();
                }
            }
            /**长按 第八张图*/
            if(imgFlg == 8){
                if(mImageList.size() == 1){
//
                }

                if(mImageList.size() == 2){
                    //
                }

                if(mImageList.size() == 3){

                }

                if(mImageList.size() == 4){
                    //
                }

                if(mImageList.size() == 5){
                    //
                }
                if(mImageList.size() == 6){
                    //
                }
                if(mImageList.size() == 7){
                    //
                }
                if(mImageList.size() == 8){
                    mImageList.remove(7);
                    ll_image_third.setVisibility(View.GONE);


                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv08.setImageResource(R.mipmap.add_pic);
                    iv09.setVisibility(View.INVISIBLE);
                    setBitmapImg();
                }
                if(mImageList.size() ==9){
                    mImageList.remove(7);

                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv09.setVisibility(View.VISIBLE);
                    iv09.setImageResource(R.mipmap.add_pic);
                    setBitmapImg();
                }
            }
            /**长按 第九张图*/
            if(imgFlg == 9){
                if(mImageList.size() == 1){
//
                }

                if(mImageList.size() == 2){
                    //
                }

                if(mImageList.size() == 3){

                }

                if(mImageList.size() == 4){
                    //
                }

                if(mImageList.size() == 5){
                    //
                }
                if(mImageList.size() == 6){
                    //
                }
                if(mImageList.size() == 7){
                    //
                }
                if(mImageList.size() == 8){
                    //
                }
                if(mImageList.size() ==9){
                    mImageList.remove(8);

                    //  ll_image_third.setVisibility(View.GONE);
                    iv01.setVisibility(View.VISIBLE);
                    iv02.setVisibility(View.VISIBLE);
                    iv03.setVisibility(View.VISIBLE);
                    iv04.setVisibility(View.VISIBLE);
                    iv05.setVisibility(View.VISIBLE);
                    iv06.setVisibility(View.VISIBLE);
                    iv07.setVisibility(View.VISIBLE);
                    iv08.setVisibility(View.VISIBLE);
                    iv09.setVisibility(View.VISIBLE);
                    iv09.setImageResource(R.mipmap.add_pic);
                    setBitmapImg();
                }
            }

        }
    }

}
