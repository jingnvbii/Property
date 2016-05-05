package com.ctrl.forum.ui.activity.Invitation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.ctrl.forum.photo.util.PublicWay;
import com.ctrl.forum.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子发布 activity
 * Created by Administrator on 2016/4/11.
 */
public class InvitationReleaseActivity extends AppToolBarActivity implements View.OnClickListener{
    private GridViewForScrollView noScrollgridview;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap ;
    private TextView tv_tel;//联系电话
    private TextView tv_location;//位置
    private TextView tv_name;//名片

    private TextView tv_release_back;
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

    @InjectView(R.id.ll_image_second)//图片布局2
     LinearLayout ll_image_second;
    @InjectView(R.id.ll_image_third)//图片布局3
     LinearLayout ll_image_third;


    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private int imageFlag=-1;
    List<Image>mImageList=new ArrayList<>();
    private List<ImageView> listImg=new ArrayList<>();
    private ImageDao Idao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_release);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.inject(this);
       Init();
       initView();

    }

    private void initView() {
        idao=new InvitationDao(this);
        idao.requesItemCategory2(channelId,"1");

        tv_tel=(TextView)findViewById(R.id.tv_tel);
        tv_location=(TextView)findViewById(R.id.tv_location);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_release_back=(TextView)findViewById(R.id.tv_release_back);

        spinner_second_kind=(Spinner)findViewById(R.id.spinner_second_kind);
        spinner_third_kind=(Spinner)findViewById(R.id.spinner_third_kind);


        tv_tel.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        tv_release_back.setOnClickListener(this);
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
                        if(imageFlag==1){
                            delImg(1);
                        }
                        if(imageFlag==2){
                            delImg(2);
                        }
                        if (imageFlag==3){
                            delImg(3);
                        }
                        if (imageFlag==4){
                            delImg(4);
                        }
                        if (imageFlag==5){
                            delImg(5);
                        }
                        if (imageFlag==6){
                            delImg(6);
                        }
                        if (imageFlag==7){
                            delImg(7);
                        }
                        if (imageFlag==8){
                            delImg(8);
                        }
                        if (imageFlag==9){
                            delImg(9);
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

    public void Init() {
        channelId=getIntent().getStringExtra("channelId");
        Idao=new ImageDao(this);

    }
    public void showDialog(){

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
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cxh.jpg")));
                startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
                intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
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

        pop.showAtLocation(ll_popup, Gravity.BOTTOM,0,0);

    }


    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==888){
            showProgress(false);
            MessageUtils.showShortToast(this,"图片上传成功");
            Image image=Idao.getImage();
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

        if(requestCode==12){
            MessageUtils.showShortToast(this,"获取二级分类成功");
            listItemCategroy=idao.getListCategroyItem();
            for(int i=0;i<listItemCategroy.size();i++){
                secondCategroyStr.add(listItemCategroy.get(i).getName());
            }

            arrayAdapter = new ArrayAdapter<String>(InvitationReleaseActivity.this, R.layout.simple_spinner_item, secondCategroyStr);
            //设置下拉列表的风格
            //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            arrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
            spinner_second_kind.setAdapter(arrayAdapter);
            spinner_second_kind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   idao.requesItemCategory3(listItemCategroy.get(position).getId(),"2");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        if(requestCode==13){
            MessageUtils.showShortToast(this,"获取三级分类成功");
            listItemCategroy=idao.getListCategroyItem();
            for(int i=0;i<listItemCategroy.size();i++){
                thirdCategroyStr.add(listItemCategroy.get(i).getName());
            }

            arrayAdapter = new ArrayAdapter<String>(InvitationReleaseActivity.this, R.layout.simple_spinner_item, thirdCategroyStr);
            //设置下拉列表的风格
           // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            arrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
            spinner_third_kind.setAdapter(arrayAdapter);

            spinner_third_kind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    categroyId=listItemCategroy.get(position).getId();
                    checkType=listItemCategroy.get(position).getCheckType();
                    
                    
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            
            
        }
    }

    @Override
    public void onClick(View v) {
       Intent intent=null;
        switch (v.getId()){
            case R.id.tv_tel:
                intent=new Intent(this,AddContactPhoneActivity.class);
                startActivityForResult(intent, 100);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_location:
                intent=new Intent(this,LocationActivity.class);
                startActivityForResult(intent, 101);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_name:
                intent=new Intent(this,CallingCardActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
            case R.id.tv_release_back:
                for(int i=0;i<PublicWay.activityList.size();i++){
                    if (null != PublicWay.activityList.get(i)) {
                        PublicWay.activityList.get(i).finish();
                    }
                }
              //  System.exit(0);
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
                String imgData = photo;
                showProgress(true);
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
                        name=getIntent().getStringExtra("name");
                        adress=getIntent().getStringExtra("adress");
                        tel=getIntent().getStringExtra("tel");
                    }
                    break;
                case 101:
                    if(resultCode==RESULT_CANCELED){
                        tv_location_name="";
                    }
                    if(resultCode==RESULT_OK){
                        tv_location_name=getIntent().getStringExtra("location");
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
                   // Log.i("tag","mImageList----"+mImageList.size());
                  //  Log.i("tag","mImageList  url----"+mImageList.get(i).getThumbImgUrl());
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

    private void delImg(int imgFlg) {
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
}
