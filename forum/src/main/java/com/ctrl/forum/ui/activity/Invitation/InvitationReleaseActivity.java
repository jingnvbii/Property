package com.ctrl.forum.ui.activity.Invitation;

import android.app.Activity;
import android.content.Context;
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
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AndroidUtil;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.customview.GridViewForScrollView;
import com.ctrl.forum.dao.ImageDao;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.CategoryItem;
import com.ctrl.forum.entity.Image;
import com.ctrl.forum.entity.List2;
import com.ctrl.forum.entity.Post2;
import com.ctrl.forum.entity.PostImage;
import com.ctrl.forum.photo.activity.AlbumActivity;
import com.ctrl.forum.photo.activity.GalleryActivity;
import com.ctrl.forum.photo.util.Bimp;
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
import com.ctrl.forum.ui.activity.WebViewActivity;
import com.ctrl.forum.utils.BitmapUtils;
import com.ctrl.forum.utils.InputMethodUtils;
import com.ctrl.forum.utils.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
 * 帖子发布 activity
 * Created by Administrator on 2016/4/11.
 */
public class InvitationReleaseActivity extends AppToolBarActivity implements View.OnClickListener{
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap ;
    private TextView tv_tel;//联系电话
    private TextView tv_location;//位置
    private TextView tv_name;//名片
    private Spinner spinner_second_kind;
    private Spinner spinner_third_kind;
    private InvitationDao idao;
    private String channelId;
    private List<CategoryItem> listItemCategroy;
    private List<String> secondCategroyStr=new ArrayList<>();
    private List<String> thirdCategroyStr=new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private String categroyId;
    private String checkType;
    private String name;
    private String adress;
    private String tel;
    private GridAdapter adapter;
    private String tv_location_name;//位置标识
    @InjectView(R.id.tv_release)//发布
    TextView tv_release;
    @InjectView(R.id.noScrollgridview)
    GridViewForScrollView noScrollgridview;
   @InjectView(R.id.et_tittle)//标题
    EditText et_tittle;
    @InjectView(R.id.et_content)//内容
    EditText et_content;
    @InjectView(R.id.tv_release_back)//取消
    TextView tv_release_back;
    @InjectView(R.id.tv_release_save)//存草稿
    TextView tv_release_save;
    @InjectView(R.id.tougao)//存草稿
    TextView tougao;
    @InjectView(R.id.ll_gen)
    LinearLayout ll_gen;


    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private int imageFlag=-1;
    List<Image>mImageList=new ArrayList<>();
    private List<ImageView> listImg=new ArrayList<>();
    private ImageDao Idao;
    private String thirdKindId;//三级分类id
    private String secondKindId;//二级分类id
    private String locationLongitude;
    private String locationLatitude;
    private String checkType3;
    private String checkType2;
    private List<CategoryItem> listItemCategroy3;
    private boolean isSave;//是否存草稿
    private String vcardDisplay;
    private List<List2> list;
    private String categoryTree; //分类
    private String[] eid;
    private Map<String,String> delIds = new HashMap<>();
    private List<Image> delImages = new ArrayList<>(); //删除的图片
    private List<Image> addImages = new ArrayList<>(); //添加的图片
    //private String edit = ""; //是否是编辑状态
    private String id; //编辑状态时的帖子id
    private String delId = "";
    private String qiniuKey;
    private List<String>keyList=new ArrayList<>();
    private List<String>urlList=new ArrayList<>();
    private String TYPE;
    private List<PostImage> listPostImage = new ArrayList<>();

    private int count=0;
    private String categoryName;
    private String categroyId2;
    private String styleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_release);
        // 隐藏输入法1
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.inject(this);
        id = getIntent().getStringExtra("id");
        if (id==null || id.equals("")){
            Arad.preferences.putBoolean("isCallingChecked", false);
            Arad.preferences.flush();
        }

        initBuildToken();
        Res.init(this);
        initView();
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.icon_addpic_unfocused);
        PublicWay.activityList.add(this);

        Bimp.max=0;
        if (checkActivity()){
            showProgress(true);
            idao.requesPostDetail(id, Arad.preferences.getString("memberId"));
        }else{
            idao.requesItemCategory2(channelId, "1");
        }
        Init();
    }

    private Boolean checkActivity() {
        if (id != null && !id.equals("")) {
            return true;
        }else{
            return false;
        }
    }

    private void initView() {
        channelId=getIntent().getStringExtra("channelId");
        categroyId2=getIntent().getStringExtra("categoryId");
        categoryName=getIntent().getStringExtra("categoryName");
        styleType  = getIntent().getStringExtra("styleType");
        idao = new InvitationDao(this);

        tv_tel=(TextView)findViewById(R.id.tv_tel);
        tv_location=(TextView)findViewById(R.id.tv_location);
        tv_name=(TextView)findViewById(R.id.tv_name);

        spinner_second_kind=(Spinner)findViewById(R.id.spinner_second_kind);
        spinner_third_kind=(Spinner)findViewById(R.id.spinner_third_kind);

        tv_tel.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        tv_release_back.setOnClickListener(this);
        tv_release.setOnClickListener(this);
        tv_release_save.setOnClickListener(this);
        tougao.setOnClickListener(this);
        ll_gen.setOnClickListener(this);

    }

    public void Init() {
        Idao=new ImageDao(this);
        pop = new PopupWindow(InvitationReleaseActivity.this);
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
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(InvitationReleaseActivity.this,
                        AlbumActivity.class);
                intent.putExtra("activity","InvitationReleaseActivity");
                startActivity(intent);
               // overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                AnimUtil.intentSlidIn(InvitationReleaseActivity.this);
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
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    InputMethodUtils.hide(InvitationReleaseActivity.this);
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(InvitationReleaseActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(noScrollgridview, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(InvitationReleaseActivity.this, GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                    AnimUtil.intentSlidIn(InvitationReleaseActivity.this);
                }
            }
        });
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
            if(Bimp.tempSelectBitmap.size() == 9){
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

        public View getView(int position, View convertView, ViewGroup parent) {
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

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        super.onRestart();
        adapter.update();
        Bimp.max=Bimp.tempSelectBitmap.size();
        if (Arad.preferences.getBoolean("isCallingChecked")) {
            vcardDisplay = "1";
        } else {
            vcardDisplay = "0";
        }
    }

    private static final int TAKE_PICTURE = 0x000001;

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
      /*  Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "lzr.jpg")));
        startActivityForResult(intentFromCapture, TAKE_PICTURE);*/
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        if(errorNo.equals("006")){
            spinner_third_kind.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==7){
            showProgress(false);
            if(isSave){
                MessageUtils.showShortToast(this,"存草稿成功");
            }else {
                MessageUtils.showShortToast(this, "帖子发布成功");
            }
            isSave=false;
            setResult(RESULT_OK);
            finish();
        }

        if (requestCode == 6) {
            showProgress(false);
            if (isSave) {
                MessageUtils.showShortToast(this, "存草稿成功");
            } else {
                MessageUtils.showShortToast(this, "帖子发布成功");
            }
            isSave = false;
            setResult(RESULT_OK);
            this.finish();
        }

        /*if(requestCode==888){
            showProgress(false);
          //  MessageUtils.showShortToast(this, "图片上传成功");
            Image image=Idao.getImage();
            mImageList.add(image);
            addImages.add(image); //添加的图片
          //  setBitmapImg();
        }*/

        if (requestCode == 3){
            Post2 post2 = idao.getPost2(); //帖子内容
            if (post2!=null) {
                name = post2.getContactName();
                adress = post2.getContactAddress();
                tel = post2.getContactPhone();
                locationLongitude = post2.getLocationLongitude();
                locationLatitude = post2.getLocationLatitude();
                tv_location_name = post2.getLocationName();
                et_content.setText(post2.getContent());
                et_tittle.setText(post2.getTitle());
                categoryTree = post2.getCategoryTree();
                eid = categoryTree.split(",", categoryTree.length());
                //名片
                vcardDisplay = post2.getVcardDisplay();
                if (vcardDisplay.equals("0")){
                    Arad.preferences.putBoolean("isCallingChecked",false);
                }else{
                    Arad.preferences.putBoolean("isCallingChecked",true);
                }
                Arad.preferences.flush();
            }

            //二级菜单列表
            listItemCategroy = idao.getList2s();
            for(int i=0;i<listItemCategroy.size();i++){
                secondCategroyStr.add(listItemCategroy.get(i).getName());
            }
            setSecondSpinner2();

            //三级菜单列表
            listItemCategroy3 = idao.getList3();
            if (listItemCategroy3!=null) {
                spinner_third_kind.setVisibility(View.VISIBLE);
                for (int i = 0; i < listItemCategroy3.size(); i++) {
                    thirdCategroyStr.add(listItemCategroy3.get(i).getName());
                }
                setSecondSpinner3();
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
                    showProgress(false);
                }
            }).start();
        }

        if (requestCode == 12) {
            if(listItemCategroy!=null){
                listItemCategroy.clear();
            }
            listItemCategroy=idao.getListCategroyItem();
            for(int i=0;i<listItemCategroy.size();i++){
                secondCategroyStr.add(listItemCategroy.get(i).getName());
            }
            setSecondSpinner2();
        }

        if(requestCode==13){
            if(listItemCategroy3!=null){
                listItemCategroy3.clear();
            }
            if (thirdCategroyStr!=null){
                thirdCategroyStr.clear();
            }
            spinner_third_kind.setVisibility(View.VISIBLE);
            listItemCategroy3=idao.getListCategroyItem();
            for(int i=0;i<listItemCategroy3.size();i++){
                thirdCategroyStr.add(listItemCategroy3.get(i).getName());
            }

           setSecondSpinner3();
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

    private void setSecondSpinner3() {
        arrayAdapter = new ArrayAdapter<String>(InvitationReleaseActivity.this, R.layout.simple_spinner_item, thirdCategroyStr);
        //设置下拉列表的风格
        // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner_third_kind.setAdapter(arrayAdapter);
        if (listItemCategroy3!=null && categoryTree!=null) {
            for (int i = 0; i < listItemCategroy3.size(); i++) {
                if (eid.length>3) {
                    if (listItemCategroy3.get(i).getId().equals(eid[3])) {
                        spinner_third_kind.setSelection(i);
                    }
                }
            }
        }
        spinner_third_kind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categroyId = listItemCategroy3.get(position).getId();
                thirdKindId = categroyId;
                checkType3 = listItemCategroy3.get(position).getCheckType();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSecondSpinner2() {
        arrayAdapter = new ArrayAdapter<String>(InvitationReleaseActivity.this, R.layout.simple_spinner_item, secondCategroyStr);
        //设置下拉列表的风格
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner_second_kind.setAdapter(arrayAdapter);
        if (listItemCategroy!=null && categoryTree!=null){
            for(int i=0;i<listItemCategroy.size();i++){
                if (eid.length>2) {
                    if (listItemCategroy.get(i).getId().equals(eid[2])) {
                        spinner_second_kind.setSelection(i);
                        styleType = listItemCategroy.get(i).getStyleType();
                    }
                }
            }
        }else {
            for (int i=0;i<listItemCategroy.size();i++){
                if(listItemCategroy.get(i).getId().equals(categroyId2)){
                    spinner_second_kind.setSelection(i);
                }
            }
        }
        spinner_second_kind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idao.requesItemCategory3(listItemCategroy.get(position).getId(), "2");
                secondKindId = listItemCategroy.get(position).getId();
                checkType2 = listItemCategroy.get(position).getCheckType();
                styleType = listItemCategroy.get(position).getStyleType();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean checkInput() {
        if (Arad.preferences.getBoolean("isCallingChecked")) {
            if (TextUtils.isEmpty(name)) {
                MessageUtils.showShortToast(InvitationReleaseActivity.this, "联系人名称为空");
                return false;
            }
            if (TextUtils.isEmpty(adress)) {
                MessageUtils.showShortToast(InvitationReleaseActivity.this, "联系人地址为空");
                return false;
            }
            if (TextUtils.isEmpty(tel)) {
                MessageUtils.showShortToast(InvitationReleaseActivity.this, "联系人电话为空");
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    public String getImageUrl(List<String>mImageList){
        String imagesUrl=null;
        switch (mImageList.size()){
            case 0:
                imagesUrl=null;
                break;
            case 1:
                imagesUrl=mImageList.get(0);
                break;
            case 2:
                imagesUrl=mImageList.get(0)+","+mImageList.get(1);
                break;
            case 3:
                imagesUrl=mImageList.get(0)+","+mImageList.get(1)+","+mImageList.get(2);
                break;
            case 4:
                imagesUrl=mImageList.get(0)+","+mImageList.get(1)+","+mImageList.get(2)+","+mImageList.get(3);
                break;
            case 5:
                imagesUrl=mImageList.get(0)+","+mImageList.get(1)+","+mImageList.get(2)
                        +","+mImageList.get(3)+","+mImageList.get(4);
                break;
            case 6:
                imagesUrl=mImageList.get(0)+","+mImageList.get(1)+","+mImageList.get(2)
                        +","+mImageList.get(3)+","+mImageList.get(4)+","+mImageList.get(5);
                break;
            case 7:
                imagesUrl=mImageList.get(0)+","+mImageList.get(1)+","+mImageList.get(2)
                        +","+mImageList.get(3)+","+mImageList.get(4)+","+mImageList.get(5)+","+
                        mImageList.get(6);
                break;
            case 8:
                imagesUrl=mImageList.get(0)+","+mImageList.get(1)+","+mImageList.get(2)
                        +","+mImageList.get(3)+","+mImageList.get(4)+","+mImageList.get(5)+","+
                        mImageList.get(6)+","+mImageList.get(7);
                break;
            case 9:
                imagesUrl=mImageList.get(0)+","+mImageList.get(1)+","+mImageList.get(2)
                        +","+mImageList.get(3)+","+mImageList.get(4)+","+mImageList.get(5)+","+
                        mImageList.get(6)+","+mImageList.get(7)+","+mImageList.get(8);
                break;
        }
        return imagesUrl;
    }
    
/*
* 原图url串
* */
    public String getImagesUrl(List<String> mImageList){
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
    public String getDelImageId(List<ImageItem> imageId) {
        if (imageId.size() != 0 ) {
            List<String> delImageIds = new ArrayList<>();
            for (int i = 0; i < imageId.size(); i++) {
                if (imageId.get(i).getImageUrl()!=null) {
                    delImageIds.add(delIds.get(imageId.get(i).getImageUrl()));
                }
            }
            if (delImageIds.size()!=0) {
                for (int i = 0; i < delImageIds.size(); i++) {
                    //转换成字符串
                    delId = delId + delImageIds.get(i) + ",";
                }
                return delId.substring(0, delId.length() - 1);
            }else{return "";}
        }
        return "";
    }

    public boolean checkStyle(){
        if (styleType.equals("3")){
            if (Bimp.tempSelectBitmap.size()>0){
                return true;
            }else{
                MessageUtils.showShortToast(this,"瀑布流样式必须有图片");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
       Intent intent=null;
        switch (v.getId()){
            case R.id.ll_gen:
                InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen=imm.isActive();
                //isOpen若返回true，则表示输入法打开
                if(isOpen){
                    imm.hideSoftInputFromWindow(InvitationReleaseActivity.this.getCurrentFocus().getWindowToken()
                            ,InputMethodManager.HIDE_NOT_ALWAYS);
                    //接受软键盘输入的编辑文本或其它视图
                  //  imm.showSoftInput(et_content,InputMethodManager.SHOW_FORCED);
                }
                break;
            case R.id.tougao:
                intent=new Intent(InvitationReleaseActivity.this, WebViewActivity.class);
                intent.putExtra("title","投稿协议");
                startActivity(intent);
                AnimUtil.intentSlidIn(InvitationReleaseActivity.this);
                break;
            case R.id.tv_release_save: //存草稿
                TYPE="0";
                isSave=true;
                //checkContent();
                if (checkStyle()) {
                    if (checkContent()) {
                        if (checkActivity()) {
                            if (!et_content.getText().toString().equals("")) {
                                saveInvitation();
                                return;
                            }
                            if (listPostImage != null) {
                                saveInvitation();
                                return;
                            }
                        }
                        if (!et_content.getText().toString().equals("")) {
                            saveInvitation();
                            return;
                        }
                    }
                }
                break;
            case R.id.tv_release:
                TYPE="1";
                //checkContent();
                if (checkStyle()) {
                    if (checkContent()) {
                        showProgress(true);
                        //发布帖子
                        releaseInvitation();
                    }
                }
                break;
            case R.id.tv_tel:
                intent=new Intent(this,AddContactPhoneActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("adress", adress);
                intent.putExtra("tel", tel);
                startActivityForResult(intent, 100);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_location:
                intent=new Intent(this,LocationActivity.class);
                intent.putExtra("locationLongitude", locationLongitude);
                intent.putExtra("locationLatitude", locationLatitude);
                intent.putExtra("tv_location_name", tv_location_name);
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
        }
    }

    //检查输入框总是否有内容,从哪个界面进来的
    public Boolean checkContent(){
        if(et_content.getText().toString().equals("")&&Bimp.tempSelectBitmap.size()==0){
            MessageUtils.showShortToast(this,"帖子内容不可为空");
            return false;
        }
        if (checkActivity()) {
            showProgress(true);
            if (AlbumActivity.addList.size() > 0) {
                Uri uri = null;
                for (int i = 0; i < AlbumActivity.addList.size(); i++) {
                    uri= Utils.getUriFromPath(this, AlbumActivity.addList.get(i).getImagePath());
                    //uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), AlbumActivity.addList.get(i).getBitmap(), null, null));
                    preUpload(uri);
                }
                doUpload();
                return false;
            }else {
                return true;
            }
           // return false;
        }else{
            showProgress(true);
            if(Bimp.tempSelectBitmap.size()>0) {
                Uri uri = null;
                for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
                 //   uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), Bimp.tempSelectBitmap.get(i).getBitmap(), null, null));
                    Log.i("tag","path2123123===="+Bimp.tempSelectBitmap.get(i).getImagePath());
                    uri= Utils.getUriFromPath(this,Bimp.tempSelectBitmap.get(i).getImagePath());
                    Log.i("tag", "uri===" + uri);
                    preUpload(uri);
                }
                doUpload();
                return false;
            }else {
                if (!et_content.getText().toString().equals("")) {
                    return true;
                }
                return false;
            }
        }
    }

    //保存到草稿箱
    public void saveInvitation(){
        String imagesUrl=getImageUrl(urlList);
        String thumbImagesUrl= getImageUrl(urlList);
        if (spinner_third_kind.getVisibility() == View.VISIBLE) {
            if(!checkActivity()) {
                idao.requesInvitationPost(
                        Arad.preferences.getString("memberId"),
                        thirdKindId,
                        "0",
                        "0",
                        checkType3,
                        et_tittle.getText().toString().trim(),
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
            } else { //编辑状态存草稿
                idao.requesPostEditor(
                        id,
                        thirdKindId,
                        "0",
                        "0",
                        checkType3,
                        et_tittle.getText().toString().trim(),
                        et_content.getText().toString().trim(),
                        vcardDisplay,
                        name,
                        adress,
                        tel,
                        locationLongitude,
                        locationLatitude,
                        tv_location_name,
                        getDelImageId(GalleryActivity.delList),  //删除图片的id的字符串
                        imagesUrl,
                        thumbImagesUrl);
            }
        }else {
            if(!checkActivity()) {
                idao.requesInvitationPost(
                        Arad.preferences.getString("memberId"),
                        secondKindId,
                        "0",
                        "0",
                        checkType2,
                        et_tittle.getText().toString().trim(),
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
            }else{
                idao.requesPostEditor(
                        id,
                        secondKindId,
                        "0",
                        "0",
                        checkType2,
                        et_tittle.getText().toString().trim(),
                        et_content.getText().toString().trim(),
                        vcardDisplay,
                        name,
                        adress,
                        tel,
                        locationLongitude,
                        locationLatitude,
                        tv_location_name,
                        getDelImageId(GalleryActivity.delList),  //删除图片的id的字符串
                        imagesUrl,
                        thumbImagesUrl);
            }
        }
    }

    //发布帖子
    private void releaseInvitation() {
        String imagesUrl=getImageUrl(urlList);
        String thumbImagesUrl= getImageUrl(urlList);

        if(checkInput()){
            if(spinner_third_kind.getVisibility()==View.VISIBLE){
                if(!checkActivity()) {
                    idao.requesInvitationPost(
                            Arad.preferences.getString("memberId"),
                            thirdKindId,
                            "0",
                            "1",
                            checkType3,
                            et_tittle.getText().toString().trim(),
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
                }else{
                    idao.requesPostEditor(
                            id,
                            thirdKindId,
                            "0",
                            "1",
                            checkType3,
                            et_tittle.getText().toString().trim(),
                            et_content.getText().toString().trim(),
                            vcardDisplay,
                            name,
                            adress,
                            tel,
                            locationLongitude,
                            locationLatitude,
                            tv_location_name,
                            getDelImageId(GalleryActivity.delList),  //删除图片的id的字符串
                            imagesUrl,
                            imagesUrl);
                }
            }else {
                if (!checkActivity()) {
                    idao.requesInvitationPost(
                            Arad.preferences.getString("memberId"),
                            secondKindId,
                            "0",
                            "1",
                            checkType2,
                            et_tittle.getText().toString().trim(),
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
                }else{
                    idao.requesPostEditor(
                            id,
                            secondKindId,
                            "0",
                            "1",
                            checkType2,
                            et_tittle.getText().toString().trim(),
                            et_content.getText().toString().trim(),
                            vcardDisplay,
                            name,
                            adress,
                            tel,
                            locationLongitude,
                            locationLatitude,
                            tv_location_name,
                            getDelImageId(GalleryActivity.delList),  //删除图片的id的字符串
                            imagesUrl,
                            imagesUrl);
                }
            }
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
               // showProgress(true);
                //Idao.requestUploadImage(photo);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void resetBimp(){
        cancel();
        Bimp.tempSelectBitmap.clear();
        for(int i=0;i<PublicWay.activityList.size();i++){
            if (null != PublicWay.activityList.get(i)) {
                PublicWay.activityList.get(i).finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetBimp();
        GalleryActivity.delList.clear();
        AlbumActivity.addList.clear();
       // System.exit(0);
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
                case TAKE_PICTURE:
                    if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
                            Bitmap bitmap =(Bitmap)data.getExtras().get("data");
                             String fileName = String.valueOf(System.currentTimeMillis());
                            // String path = FileUtils.saveBitmap(bm, fileName).getPath();
                            BitmapUtils.saveImageToGallery(this, bitmap, "laizhouren", fileName);
                            ImageItem takePhoto = new ImageItem();
                            takePhoto.setBitmap(bitmap);
                            takePhoto.setImagePath(Environment.getExternalStorageDirectory()
                                    + "/laizhouren/" + fileName + ".jpg");
                            Bimp.tempSelectBitmap.add(takePhoto);
                    }
                    break;

                case IMAGE_REQUEST_CODE:
                    Uri uri = data.getData();
                    String thePath = Utils.getInstance().getPath(this, uri);
                    getImageToView1(thePath);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return false;
    }

    @Override
    public boolean setupToolBarLeftText(TextView mLeftText) {
        mLeftText.setText("取消");
        mLeftText.setTextColor(getResources().getColor(R.color.text_blue));
        return true;
    }

    @Override
    public String setupToolBarTitle() {
        TextView tv_title = getmTitle();
        tv_title.setTextColor(getResources().getColor(R.color.text_black));
        return "帖子发布";
    }

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("存草稿");
        mRightText.setTextColor(getResources().getColor(R.color.text_blue));
        return true;
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

            //Toast.makeText(InvitationReleaseActivity.this, "上传成功!", Toast.LENGTH_LONG).show();
          /*  String o = textViewCurrent.getText() == null ? "" : textViewCurrent.getText().toString();
            // o;
            textViewCurrent.setText("");
            textViewCurrent.setText("\n" + ret.getStatusCode() + " " + ret.getResponse());
            Log.d("handler", textViewCurrent.getText().toString());*/
            Log.e("0","1");
            Log.e("ups.size()",ups.size()+"");
            count++;
            if (checkActivity()){
                if (count == AlbumActivity.addList.size()) {
                    Log.e("1", "1");
                    for (int i = 0; i < count; i++) {
                        String url = QiNiuConfig.BASE_URL + keyList.get(i);
                        Log.e("url===========", url.toString());
                        urlList.add(url);
                        Log.i("tag", "url==i==" + url);
                    }
                    if (urlList.size() == AlbumActivity.addList.size()) {
                        if (TYPE.equals("0")) {
                            saveInvitation();
                        }
                        if (TYPE.equals("1")) {
                            releaseInvitation();
                        }
                    }
                }
            }else{
                if (count == Bimp.tempSelectBitmap.size()){
                    Log.e("1","1");
                    for(int i=0;i<count;i++){
                        String url=QiNiuConfig.BASE_URL+keyList.get(i);
                        Log.e("url===========",url.toString());
                        urlList.add(url);
                        Log.i("tag","url==i=="+url);
                    }Log.e("1","2");
                    if(urlList.size()==Bimp.tempSelectBitmap.size()) {
                        Log.e("1","3");
                        Log.i("tag", "check type=11=="+checkType2);
                        Log.i("tag","check type=22=="+checkType3);
                        if (TYPE.equals("0")){
                            saveInvitation();
                        }
                        if (TYPE.equals("1")){
                            Log.e("1", "4");
                            releaseInvitation();
                        }
                    }
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
        keyList.add(qiniuKey);
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
