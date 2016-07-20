package com.ctrl.forum.ui.activity.plot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.ctrl.forum.customview.GridViewForScrollView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Post2;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.photo.activity.AlbumActivity;
import com.ctrl.forum.photo.activity.GalleryActivity;
import com.ctrl.forum.photo.util.Bimp;
import com.ctrl.forum.photo.util.FileUtils;
import com.ctrl.forum.photo.util.ImageItem;
import com.ctrl.forum.photo.util.PublicWay;
import com.ctrl.forum.photo.util.Res;
import com.ctrl.forum.qiniu.QiNiuConfig;
import com.ctrl.forum.qiniu_main.up.UpApi;
import com.ctrl.forum.qiniu_main.up.UpParam;
import com.ctrl.forum.qiniu_main.up.Upload;
import com.ctrl.forum.qiniu_main.up.UploadHandler;
import com.ctrl.forum.qiniu_main.up.auth.Authorizer;
import com.ctrl.forum.qiniu_main.up.rs.PutExtra;
import com.ctrl.forum.qiniu_main.up.rs.UploadResultCallRet;
import com.ctrl.forum.qiniu_main.up.slice.Block;
import com.ctrl.forum.qiniu_main.util.Util;
import com.ctrl.forum.ui.activity.Invitation.AddContactPhoneActivity;
import com.ctrl.forum.ui.activity.Invitation.CallingCardActivity;
import com.ctrl.forum.ui.activity.Invitation.LocationActivity;
import com.ctrl.forum.ui.activity.WebViewActivity;
import com.ctrl.forum.utils.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 小区发布帖子
 */
public class PlotAddInvitationActivity extends AppToolBarActivity implements View.OnClickListener {

    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private TextView tv_tel;//联系电话
    private TextView tv_location;//位置
    private TextView tv_name;//名片

    private String channelId;
    private String name;
    private String adress;
    private String tel;

    private String tv_location_name;//位置标识

    @InjectView(R.id.noScrollgridview)
    GridViewForScrollView noScrollgridview;

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
    private String locationLongitude;
    private String locationLatitude;
    private String vcardDisplay;
    private InvitationDao idao;
     //private String edit = ""; //是否是编辑状态
    private String id; //编辑状态时的帖子id

    private Map<String, String> delIds = new HashMap<>();
    //private List<ImageItem> delImages = new ArrayList<>(); //删除的图片

    private boolean isSave;//是否存草稿
    private String delId = "";
    private GridAdapter adapter;
    public static Bitmap bimap ;
    private String qiniuKey;
    private List<String>urlList=new ArrayList<>();
    private List<String>keyList=new ArrayList<>();
    private String qiNiuImgUrl;//添加图片的url
    private String TYPE;
    private List<PostImage> listPostImage = new ArrayList<>();
    //private List<ImageNew> primary = new ArrayList<>(); //从草稿箱进来时获取到的imageList的url集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_add_invitation);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.inject(this);
        Res.init(this);

        initView();
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.icon_addpic_unfocused);

        //edit = getIntent().getStringExtra("edit");
        initBuildToken();
        idao = new InvitationDao(this);

        if (checkActivity()){
            idao.requesPostDetail(id, Arad.preferences.getString("memberId"));
        }

        Init();
        Bimp.max=0;
    }

    private Boolean checkActivity() {
        id = getIntent().getStringExtra("id");
        if (id != null && !id.equals("")) {
            return true;
        }else{
            return false;
        }
    }

    private void initView() {
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_name = (TextView) findViewById(R.id.tv_name);

        tv_tel.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        tv_release_back.setOnClickListener(this);
        tv_release.setOnClickListener(this);
        tv_release_save.setOnClickListener(this);
        tougao.setOnClickListener(this);
        ll_all.setOnClickListener(this);

    }

    private void showDelDialog(final int posititon) {
        new AlertDialog.Builder(this)
                .setTitle("确定删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImageItem ii = new ImageItem();
                        ii.setImageUrl(Bimp.tempSelectBitmap.get(posititon).getImageUrl());

                        for (int i =0;i< AlbumActivity.addList.size();i++){
                            if (Bimp.tempSelectBitmap.get(posititon).getImageId().
                                    equals(AlbumActivity.addList.get(posititon).getImageId())){
                                AlbumActivity.addList.remove(i);
                            }
                        }

                        Bimp.tempSelectBitmap.remove(posititon);
                        Bimp.max = Bimp.tempSelectBitmap.size();
                        //delImages.add(ii); //删除的图片
                        adapter.update();
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

        channelId = getIntent().getStringExtra("channelId");

        if (Arad.preferences.getBoolean("isCallingChecked")) {
            vcardDisplay = "1";
        } else {
            vcardDisplay = "0";
        }

        channelId = getIntent().getStringExtra("channelId");
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
                .findViewById(R.id.item_popupwindows_camera);//拍照
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo); //相册中选取
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel); //取消
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//拍照
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//相册中选取
                Intent intent = new Intent(PlotAddInvitationActivity.this,
                        AlbumActivity.class);
                intent.putExtra("activity","PlotAddInvitationActivity");
                startActivity(intent);
                // overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
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

        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);

        if (!checkActivity()) {
            adapter.update();
        }
        noScrollgridview.setAdapter(adapter);

        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(PlotAddInvitationActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(noScrollgridview, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(PlotAddInvitationActivity.this, GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                    AnimUtil.intentSlidIn(PlotAddInvitationActivity.this);
                }
            }
        });
    }

    private static final int TAKE_PICTURE = 0x000001;

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 3) {
            Post2 post2 = idao.getPost2(); //帖子内容
            if (post2!=null) {
                et_content.setText(post2.getContent());
                //名片
                vcardDisplay = post2.getVcardDisplay();
                name = post2.getContactName();
                adress = post2.getContactAddress();
                tel = post2.getContactPhone();
                locationLongitude = post2.getLocationLongitude();
                locationLatitude = post2.getLocationLatitude();
                tv_location_name = post2.getLocationName();
                if (vcardDisplay.equals("0")) {
                    Arad.preferences.putBoolean("isCallingChecked", false);
                } else {
                    Arad.preferences.putBoolean("isCallingChecked", true);
                }
                Arad.preferences.flush();
            }

            listPostImage = idao.getListPostImage();//图片
            for (int i = 0; i < listPostImage.size(); i++) {
                final PostImage post = listPostImage.get(i);
                delIds.put(post.getImg(), listPostImage.get(i).getId());//用来存放获取到的图片的url对应的id
            }

            new Thread(new Runnable() {
                @Override
                public void run() {

                    List<PostImage> listPostImage = idao.getListPostImage();//图片
                    for (int i=0;i<listPostImage.size();i++) {
                        Bitmap bmp = getBitmap(listPostImage.get(i).getImg());
                        ImageItem ii = new ImageItem();
                        ii.setBitmap(bmp);
                        ii.setImageUrl(listPostImage.get(i).getImg());
                        Bimp.tempSelectBitmap.add(ii); //网络中获取
                    }
                    adapter.update();
                }
            }).start();
        }

        if (requestCode == 7) {
            if (isSave) {
                MessageUtils.showShortToast(this, "存草稿成功");
            } else {
                MessageUtils.showShortToast(this, "帖子发布成功");
            }
            isSave = false;
            setResult(RESULT_OK);
            this.finish();
        }
        if (requestCode == 6) {
            if (isSave) {
                MessageUtils.showShortToast(this, "存草稿成功");
            } else {
                MessageUtils.showShortToast(this, "帖子发布成功");
            }
            isSave = false;
            setResult(RESULT_OK);
            this.finish();
        }
    }

    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 获取图片
     * @param path 图片路径
     * @return
     */
    public static Bitmap getImageBitmap(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inStream);
            return bitmap;
        }
        return null;
    }

    private boolean checkInput() {
        if (Arad.preferences.getBoolean("isCallingChecked")) {
            if (TextUtils.isEmpty(name)) {
                MessageUtils.showShortToast(PlotAddInvitationActivity.this, "联系人名称为空");
                return false;
            }
            if (TextUtils.isEmpty(adress)) {
                MessageUtils.showShortToast(PlotAddInvitationActivity.this, "联系人地址为空");
                return false;
            }
            if (TextUtils.isEmpty(tel)) {
                MessageUtils.showShortToast(PlotAddInvitationActivity.this, "联系人电话为空");
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    /*
    * 原图url串
    * */
    public String getImagesUrl(List<String> mImageList) {
        String urlStr = "";
        if (mImageList.size() != 0) {
            for (int i = 0; i < mImageList.size(); i++) {
                //转换成字符串
                urlStr = urlStr + mImageList.get(i) + ",";
            }
            return urlStr.substring(0, urlStr.length() - 1);
        }
        return "";
    }

    //删除的图片的url转成id形式的String
    public String getDelImageId(List<ImageItem> imageId) {
        if (imageId.size() != 0 ) {
            List<String> delImageIds = new ArrayList<>();
            for (int i = 0; i < imageId.size(); i++) {
                if (imageId.get(i).getImageUrl()!=null) {
                    delImageIds.add(delIds.get(imageId.get(i).getImageUrl()));
                }
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
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_release_save: //存草稿0
                TYPE="0";
                upImgPass();
                break;
            case R.id.tv_release: //发布1
                TYPE="1";
                upImgPass();
                break;
            case R.id.tv_tel:
                intent = new Intent(this, AddContactPhoneActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("adress", adress);
                intent.putExtra("tel", tel);
                startActivityForResult(intent, 100);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_location:
                intent = new Intent(this, LocationActivity.class);
                intent.putExtra("locationLongitude", locationLongitude);
                intent.putExtra("locationLatitude", locationLatitude);
                intent.putExtra("tv_location_name", tv_location_name);
                startActivityForResult(intent, 101);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_name:
                intent = new Intent(this, CallingCardActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_release_back:
                onBackPressed();
                break;
            case R.id.tougao:
                Intent intent1 = new Intent(this, WebViewActivity.class);
                intent1.putExtra("title", "投稿协议");
                startActivity(intent1);
                break;
            case R.id.ll_all:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();
                //isOpen若返回true，则表示输入法打开
                if (isOpen) {
                    imm.hideSoftInputFromWindow(PlotAddInvitationActivity.this.getCurrentFocus().getWindowToken()
                            , InputMethodManager.HIDE_NOT_ALWAYS);
                    //接受软键盘输入的编辑文本或其它视图
                    //imm.showSoftInput(et_content,InputMethodManager.SHOW_FORCED);
                }
                break;
        }
    }

    //上传图片，发布帖子
    private void upImgPass() {
        if (checkActivity()) {
            if (AlbumActivity.addList.size() > 0) {
                Uri uri = null;
                for (int i = 0; i < AlbumActivity.addList.size(); i++) {
                    uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), AlbumActivity.addList.get(i).getBitmap(), null, null));
                    preUpload(uri);
                    doUpload();
                }
            }
        }else{
            if (Bimp.tempSelectBitmap.size() > 0) {
                Uri uri = null;
                for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
                    uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), Bimp.tempSelectBitmap.get(i).getBitmap(), null, null));
                    preUpload(uri);
                    doUpload();
                }
            }
        }
        if(!et_content.getText().toString().equals("")) {
            //发布帖子
            releaseInvitation(TYPE);
        }
    }

    //发布帖子或者存草稿
    private void releaseInvitation(String type) {

        String imagesUrl1 = getImagesUrl(urlList);
        if (!et_content.getText().toString().equals("") || imagesUrl1 != null) {
            switch (type){
                case "0"://存草稿
                    isSave = true;
                    if (!checkActivity()) {
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
                                imagesUrl1
                        );
                    } else {//编辑状态下存草稿
                        //getImagesUrl(imageId);
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
                                getDelImageId(GalleryActivity.delList),  //删除图片的id的字符串
                                imagesUrl1,
                                imagesUrl1);//不对
                    }
                    break;
                case "1"://发布帖子
                    if (checkInput()) {
                        if (!checkActivity()) {
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
                                    imagesUrl1,
                                    imagesUrl1
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
                        getDelImageId(GalleryActivity.delList),  //删除图片的id的字符串
                           imagesUrl1,
                           imagesUrl1); //不对
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            MessageUtils.showShortToast(this, "内容不能为空!");
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
            if (resultCode == Activity.RESULT_CANCELED) {
                return;
            }
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    Uri uri = data.getData();
                    String thePath = Utils.getInstance().getPath(this, uri);
                    ImageItem ii = new ImageItem();
                    ii.setBitmap(BitmapFactory.decodeFile(thePath));
                    Bimp.tempSelectBitmap.add(Bimp.tempSelectBitmap.size(), ii);
                    break;
                case TAKE_PICTURE:
                    if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
                        String fileName = String.valueOf(System.currentTimeMillis());
                        Bitmap bm = (Bitmap) data.getExtras().get("data");
                        FileUtils.saveBitmap(bm, fileName);
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setBitmap(bm);
                        Bimp.tempSelectBitmap.add(takePhoto);
                    }
                    break;
                case 100:
                    if (resultCode == RESULT_OK) {
                        name = data.getStringExtra("name");
                        adress = data.getStringExtra("adress");
                        tel = data.getStringExtra("tel");
                    }
                    break;
                case 101:
                    if (resultCode == RESULT_CANCELED) {
                        tv_location_name = "";
                        locationLongitude = "";//经度
                        locationLatitude = "";//纬度
                    }
                    if (resultCode == RESULT_OK) {
                        tv_location_name = data.getStringExtra("location");
                        locationLongitude = data.getStringExtra("locationLongitude");
                        locationLatitude = data.getStringExtra("locationLatitude");
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImageViewWidth(ImageView imageView) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int w = (AndroidUtil.getDeviceWidth(this) - 20) / 4;
        android.util.Log.d("demo", "width : " + w);
        params.height = w;
        android.util.Log.d("demo", "height : " + params.height);
        imageView.setLayoutParams(params);

    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 9) {
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    protected void onRestart() {
        adapter.update();
        Bimp.max = Bimp.tempSelectBitmap.size();
        super.onRestart();
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();
        GalleryActivity.delList.clear();
        AlbumActivity.addList.clear();
        for(int i=0;i< PublicWay.activityList.size();i++){
            if (null != PublicWay.activityList.get(i)) {
                PublicWay.activityList.get(i).finish();
            }
        }
    }


    // ********* 以下为七牛sdk相关代码 *********

    long start = 0;
    private static Authorizer authorizer = new Authorizer();

    private static Date buildTokenDate;
    private static ScheduledExecutorService replenishTimer = Executors.newScheduledThreadPool(1, new UpApi.DaemonThreadFactory());
    private static ReadWriteLock rw = new ReentrantReadWriteLock();
    private void initBuildToken() {
        replenishTimer.scheduleAtFixedRate(new Runnable() {
            private long gap = 1000 * 60 * 40; // 40分钟
            public void run() {
                if (getBuildTokenDate() == null || (new Date().getTime() - getBuildTokenDate().getTime() > gap)) {
                    buildToken();
                }
            }

        }, 0, 10, TimeUnit.MINUTES);

        authorizer.setUploadToken(QiNiuConfig.token);
        buildTokenDate = new Date();
    }

    private Random r = new Random();
    private void buildToken() {
        try {
            rw.writeLock().lock();
            if (r.nextBoolean()) {// 模拟
                throw new RuntimeException("  获取token失败。  ");
            }
            authorizer.setUploadToken(QiNiuConfig.token);
            buildTokenDate = new Date();
        } catch (Exception e) {

        } finally {
            rw.writeLock().unlock();
        }
    }

    private Date getBuildTokenDate() {
        try {
            rw.readLock().lock();
            return buildTokenDate;
        } finally {
            rw.readLock().unlock();
        }
    }

    public Authorizer getAuthorizer() {
        try {
            rw.readLock().lock();
            return authorizer;
        } finally {
            rw.readLock().unlock();
        }
    }


    // *************************

    private UploadHandler uploadHandler = new UploadHandler() {
        @Override
        protected void onProcess(long contentLength, long currentUploadLength, long lastUploadLength, UpParam p, Object passParam) {
            long now = System.currentTimeMillis();
            long time = (now - start) / 1000;
            long v = currentUploadLength / 1000 / (time + 1);
           /* String o = textViewCurrent.getText() == null ? "" : textViewCurrent.getText().toString();
            String m = passParam + "  : " + p.getName();
            String txt = o + "\n1" + m + "\n共: " + contentLength / 1024 + "KB, 历史已上传: " + lastUploadLength / 1024 + "KB, 本次已上传: "
                    + currentUploadLength / 1024 + "KB, 耗时: " + time + "秒, 速度: " + v + "KB/s";
            txt = txt.substring(Math.max(0, txt.length() - 1300));
            textViewCurrent.setText(txt);
            Log.d("handler", textViewCurrent.getText().toString());*/
        }

        @Override
        protected void onSuccess(UploadResultCallRet ret, UpParam p, Object passParam) {

             //Toast.makeText(PlotAddInvitationActivity.this, "上传成功!", Toast.LENGTH_LONG).show();
          /*  String o = textViewCurrent.getText() == null ? "" : textViewCurrent.getText().toString();
            // o;
            textViewCurrent.setText("");
            textViewCurrent.setText("\n" + ret.getStatusCode() + " " + ret.getResponse());
            Log.d("handler", textViewCurrent.getText().toString());*/
            //qiNiuImgUrl=QiNiuConfig.BASE_URL+qiniuKey;
            qiNiuImgUrl=QiNiuConfig.BASE_URL+keyList.get(urlList.size());
            urlList.add(urlList.size(), qiNiuImgUrl);
            if (checkActivity()){
                if (urlList.size() == AlbumActivity.addList.size()){
                    releaseInvitation(TYPE);
                }
            }else{
                if (urlList.size() == Bimp.tempSelectBitmap.size()){
                    releaseInvitation(TYPE);
                }
            }
            try {
                String sourceId = generateSourceId(p, passParam);
                clean(sourceId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        @Override
        protected void onFailure(UploadResultCallRet ret, UpParam p, Object passParam) {
           /* String o = textViewCurrent.getText() == null ? "" : textViewCurrent.getText().toString();
            textViewCurrent.setText(o + "\n" + ret.getStatusCode() + " " + ret.getResponse() + ", X-Reqid: " + ret.getReqId()
                    + (ret.getException() == null ? "" : " e:" + ret.getException() + " -- " + ret.getException().getMessage()));
            Log.d("handler", textViewCurrent.getText().toString());*/
            Log.d("tag", ret.getException().getMessage());
            if (ret.getException() != null) {
                ret.getException().printStackTrace();
            }
        }

        @Override
        protected void onBlockSuccess(List<Block> uploadedBlocks, Block block, UpParam p, Object passParam) {
            try {
                String sourceId = generateSourceId(p, passParam);
                addBlock(sourceId, block);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    };


    private List<Upload> ups = new LinkedList<Upload>();

    private void preUpload(Uri uri) {
        // 此参数会传递到回调
        String passObject = "test: " + uri.getEncodedPath() + "passObject";

        qiniuKey =UUID.randomUUID().toString();
        keyList.add(keyList.size(),qiniuKey);
        PutExtra extra = null;
        Upload up = UpApi.build(getAuthorizer(), qiniuKey, uri, this, extra, passObject, uploadHandler);
        addUp(up);
    }

    private synchronized void addUp(Upload up) {
        if (!contains(up)) {
            ups.add(up);
            showUps();
        }
    }

    private synchronized boolean contains(Upload up) {
        for (Upload u : ups) {
            if (up.getPassParam() != null && up.getPassParam().equals(u.getPassParam())) {
                return true;
            }
        }
        return false;
    }

    private List<UpApi.Executor> executors = new ArrayList<UpApi.Executor>();

    private synchronized void doUpload() {
        System.out.println("doup: 启动上传任务");
        for (Upload up : ups) {
            if (UpApi.isSliceUpload(up)) {
                String sourceId = generateSourceId(up.getUpParam(), up.getPassParam());
                List<Block> bls = null;
                try {
                    bls = load(sourceId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 设置以前上传的断点记录。 直传会忽略此参数
                up.setLastUploadBlocks(bls);
            }
            // UpApi.execute(up, bls);
            UpApi.Executor executor =  UpApi.execute(up);
            executors.add(executor);
        }
        System.out.println("doup: 启动上传任务完毕");
        start = System.currentTimeMillis();
    }

    // 取消上传 **************************
    private void cancel() {
        try {
            for (UpApi.Executor executor : executors) {
                executor.cancel();
            }
            showUps();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cancel0();
    }

    // 上传成功、失败、取消后等，也应将对应的UpLoad、Executor 取消，避免一直被引用，不能回收
    private synchronized void cancel0() {
        try {
            for (int l = ups.size(); l > 0; l--) {
                ups.remove(ups.get(0));
            }
            showUps();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showUps() {
        StringBuilder sb = new StringBuilder();
        for (Upload up : ups) {
            sb.append(up.getUpParam().getName()).append(", ");
        }
        //  title.setText(sb);
    }

    // 断点记录 记录到文件示例 ******************************
    public static String RESUME_DIR;
    private File getDir() throws IOException {
        String dir = RESUME_DIR;
        String qinuDir = ".qiniu_up";
        if (dir == null) {
            // dir = System.getProperties().getProperty("user.home");
            File exdir = Environment.getExternalStorageDirectory();
            dir = exdir.getCanonicalPath();
            return new File(exdir, qinuDir);
        } else {
            return new File(dir, qinuDir);
        }
    }

    private File initFile(File dir, String sourceId) throws IOException {
        dir.mkdirs();
        File file = new File(dir, sourceId);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    private String generateSourceId(UpParam p, Object passParam) {
        String s = p.getName() + "-" + p.getSize() + "-" + passParam;
        String ss = Util.urlsafeBase64(s);
        return ss;
    }

    private List<Block> load(String sourceId) throws IOException {
        File file = new File(getDir(), sourceId);
        if (!file.exists()) {
            return null;
        }
        List<Block> bls = null;
        FileReader freader = null;
        BufferedReader reader = null;
        try {
            bls = new ArrayList<Block>();
            freader = new FileReader(file);
            reader = new BufferedReader(freader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                Block b = analyLine(line);
                if (b != null) {
                    bls.add(b);
                }
            }
            Collections.reverse(bls);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (freader != null) {
                try {
                    freader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bls;
    }

    private void addBlock(String sourceId, Block block) throws IOException {
        File file = initFile(getDir(), sourceId);
        String l = sync(block);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.newLine();
            writer.write(l);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private void clean(String sourceId) throws IOException {
        File file = new File(getDir(), sourceId);
        file.delete();
    }

    private Block analyLine(String line) {
        String[] s = line.split(",");
        if (s.length >= 4) {
            int idx = Integer.parseInt(s[0]);
            String ctx = s[1];
            long length = Long.parseLong(s[2]);
            String host = s[3];
            Block block = new Block(idx, ctx, length, host);
            return block;
        } else {
            return null;
        }
    }

    private String sync(Block b) {
        return b.getIdx() + "," + b.getCtx() + "," + b.getLength() + "," + b.getHost();
    }
}

