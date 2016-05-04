package com.ctrl.android.yinfeng.ui.ereport;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.dao.ClassDao;
import com.ctrl.android.yinfeng.dao.ImgDao;
import com.ctrl.android.yinfeng.dao.ReportDao;
import com.ctrl.android.yinfeng.entity.Img2;
import com.ctrl.android.yinfeng.entity.Kind;
import com.ctrl.android.yinfeng.utils.StrConstant;
import com.ctrl.android.yinfeng.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EReportAddActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.et_title)//标题
            EditText et_title;
    @InjectView(R.id.spinner_type)//事件类型
    Spinner spinner_type;
    @InjectView(R.id.et_content)//内容
    EditText et_content;

    @InjectView(R.id.iv01_ereport_add)//图片1
    ImageView iv01_ereport_add;
    @InjectView(R.id.iv02_ereport_add)//图片2
    ImageView iv02_ereport_add;
    @InjectView(R.id.iv03_ereport_add)//图片3
    ImageView iv03_ereport_add;
    @InjectView(R.id.iv04_ereport_add)//图片4
    ImageView iv04_ereport_add;


    private List<String>list=new ArrayList<>();
    private ClassDao cdao;
    private ReportDao rdao;

    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;

    List<Img2>mGoodPicList=new ArrayList<>();
    private List<ImageView> listImg=new ArrayList<>();
    private String[] items = new String[]{"本地图片", "拍照"};
    private int imageFlag=-1;
    private ImgDao iDao;
    private String EREPORT_ADD_ACTIVITYID="EREPORT_ADD_ACTIVITYID";
    private String kindId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ereport_add);
        ButterKnife.inject(this);
        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();

    }

    private void init() {
        iDao=new ImgDao(this);
        rdao=new ReportDao(this);
        cdao=new ClassDao(this);
        cdao.requestData(StrConstant.EVT_RPT);
        listImg.add(iv01_ereport_add);
        listImg.add(iv02_ereport_add);
        listImg.add(iv03_ereport_add);
        listImg.add(iv04_ereport_add);
        iv01_ereport_add.setOnClickListener(this);
        iv02_ereport_add.setOnClickListener(this);
        iv03_ereport_add.setOnClickListener(this);
        iv04_ereport_add.setOnClickListener(this);


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
    public void onRequestSuccess(int requestCode) {

        if(2==requestCode){
            MessageUtils.showShortToast(EReportAddActivity.this,"事件上报成功");
            setResult(667);
            finish();
        }
        if(999==requestCode){
            final List<Kind>list=cdao.getData();
            ArrayList listKindName=new ArrayList();
            for(int i=0;i<list.size();i++){
                listKindName.add(list.get(i).getKindName());
            }
            ArrayAdapter<String>adapter=new ArrayAdapter<String>(EReportAddActivity.this,R.layout.simple_spinner_item,listKindName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_type.setAdapter(adapter);
            spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    kindId = list.get(position).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


        if(requestCode==101){
            showProgress(false);
            MessageUtils.showShortToast(this, "图片上传成功");
            Img2 img2=iDao.getImg();
            mGoodPicList.add(img2);
            setBitmapImg();
            iv01_ereport_add.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mGoodPicList.size() >= 1) {
                        imageFlag = 1;
                        showDelDialog(1);
                    }
                    return true;
                }
            });
            iv02_ereport_add.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mGoodPicList.size() >= 2) {
                        imageFlag = 2;
                        showDelDialog(2);
                    }
                    return true;
                }
            });
            iv03_ereport_add.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mGoodPicList.size() >= 3) {
                        imageFlag = 3;
                        showDelDialog(3);
                    }
                    return true;
                }
            });
            iv04_ereport_add.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mGoodPicList.size() >= 4) {
                        imageFlag = 4;
                        showDelDialog(4);
                    }
                    return true;
                }
            });

        }
        if(requestCode==102){
            MessageUtils.showShortToast(this, "图片删除成功");
          /*  if(imageFlag==1){
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
            }*/

        }
    }

    @Override
    public String setupToolBarTitle() {
        return "事件上报";
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
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("完成");
        mRightText.setTextColor(Color.WHITE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(et_title.getText().toString())) {
                    MessageUtils.showShortToast(EReportAddActivity.this, "标题为空");
                } else if (TextUtils.isEmpty(et_content.getText().toString())) {
                    MessageUtils.showShortToast(EReportAddActivity.this, "内容为空");

                } else if (mGoodPicList.size() == 1) {

                    Log.i("imgurl", mGoodPicList.get(0).getZipImgUrl());
                    Log.i("zipimgurl", mGoodPicList.get(0).getZipImgUrl());
                    rdao.requestReportAdd(Arad.preferences.getString("communityId"),
                            Arad.preferences.getString("staffId"),
                            et_title.getText().toString(),
                            kindId,
                            et_content.getText().toString(),
                            mGoodPicList.get(0).getImgUrl() + "," + mGoodPicList.get(0).getZipImgUrl(),
                            "",
                            "",
                            "");
                } else if (mGoodPicList.size() == 2) {
                    rdao.requestReportAdd(Arad.preferences.getString("communityId"),
                            Arad.preferences.getString("staffId"),
                            et_title.getText().toString(),
                            kindId,
                            et_content.getText().toString(),
                            mGoodPicList.get(0).getImgUrl() + "," + mGoodPicList.get(0).getZipImgUrl(),
                            mGoodPicList.get(1).getImgUrl() + "," + mGoodPicList.get(1).getZipImgUrl(),
                            "",
                            "");
                } else if (mGoodPicList.size() == 3) {
                    rdao.requestReportAdd(Arad.preferences.getString("communityId"),
                            Arad.preferences.getString("staffId"),
                            et_title.getText().toString(),
                            kindId,
                            et_content.getText().toString(),
                            mGoodPicList.get(0).getImgUrl() + "," + mGoodPicList.get(0).getZipImgUrl(),
                            mGoodPicList.get(1).getImgUrl() + "," + mGoodPicList.get(1).getZipImgUrl(),
                            mGoodPicList.get(2).getImgUrl() + "," + mGoodPicList.get(2).getZipImgUrl(),
                            "");
                } else if (mGoodPicList.size() == 4) {
                    rdao.requestReportAdd(Arad.preferences.getString("communityId"),
                            Arad.preferences.getString("staffId"),
                            et_title.getText().toString(),
                            kindId,
                            et_content.getText().toString(),
                            mGoodPicList.get(0).getImgUrl() + "," + mGoodPicList.get(0).getZipImgUrl(),
                            mGoodPicList.get(1).getImgUrl() + "," + mGoodPicList.get(1).getZipImgUrl(),
                            mGoodPicList.get(2).getImgUrl() + "," + mGoodPicList.get(2).getZipImgUrl(),
                            mGoodPicList.get(3).getImgUrl() + "," + mGoodPicList.get(3).getZipImgUrl()
                    );
                } else {
                    rdao.requestReportAdd(Arad.preferences.getString("communityId"),
                            Arad.preferences.getString("staffId"),
                            et_title.getText().toString(),
                            kindId,
                            et_content.getText().toString(),
                            "",
                            "",
                            "",
                            "");
                }


            }
        });


        return true;
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
                String activityId = "";
                String imgData = photo;
                String typeKey = "AC";//活动配图
                String optMode = "0";//0：添加、1:编辑
                showProgress(true);
                iDao.requestUploadImg(EREPORT_ADD_ACTIVITYID, photo, StrConstant.EVT_RPT, "1");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == iv01_ereport_add){
            if(mGoodPicList.size() >= 1){
                //
            } else {
                showDialog();
            }

            //setBitmapImg();
        }

        if(v == iv02_ereport_add){
            if(mGoodPicList.size() >= 2||mGoodPicList.size()==0){
                //
            } else {
                showDialog();
            }
            //setBitmapImg();
        }
        if(v == iv03_ereport_add){
            if(mGoodPicList.size() >= 3||mGoodPicList.size()==0){
                //
            } else {
                showDialog();
            }
            //setBitmapImg();
        }
        if(v == iv04_ereport_add){
            if(mGoodPicList.size() >= 4||mGoodPicList.size()==0){
                //
            } else {
                showDialog();
            }
            //setBitmapImg();
        }

    }

    /**
     * 显示选择对话框
     */
    private void showDialog() {

        new AlertDialog.Builder(this)
                .setTitle("添加图片")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case IMAGE_REQUEST_CODE:
                                Intent intentFromGallery = new Intent(Intent.ACTION_PICK, null);
                                intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                                break;
                            case CAMERA_REQUEST_CODE:
                                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cxh.jpg")));
                                startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                                break;
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
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void setImageViewWidth(ImageView imageView){

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int w=imageView.getWidth();
        android.util.Log.d("demo", "width : " + w);
        params.height=w;
        android.util.Log.d("demo", "height : " + params.height);
        imageView.setLayoutParams(params);

    }


    private void setBitmapImg(){

/*        setImageViewWidth(iv01_ereport_add);
        setImageViewWidth(iv02_ereport_add);
        setImageViewWidth(iv03_ereport_add);
        setImageViewWidth(iv04_ereport_add);*/

        if (mGoodPicList != null){

            if(mGoodPicList.size()==0){
                iv01_ereport_add.setVisibility(View.VISIBLE);
                iv01_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);
                iv02_ereport_add.setVisibility(View.INVISIBLE);
                iv03_ereport_add.setVisibility(View.INVISIBLE);
                iv04_ereport_add.setVisibility(View.INVISIBLE);
            }
            if(mGoodPicList.size() == 1) {
                iv01_ereport_add.setVisibility(View.VISIBLE);
                iv02_ereport_add.setVisibility(View.VISIBLE);
                iv02_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);
                iv03_ereport_add.setVisibility(View.INVISIBLE);
                iv04_ereport_add.setVisibility(View.INVISIBLE);


                for(int i = 0 ; i < mGoodPicList.size() ; i ++){
                    Arad.imageLoader.load(mGoodPicList.get(i).getZipImgUrl()).into(listImg.get(i));
                }

            }

            if(mGoodPicList.size() == 2){
                iv01_ereport_add.setVisibility(View.VISIBLE);
                iv02_ereport_add.setVisibility(View.VISIBLE);
                iv03_ereport_add.setVisibility(View.VISIBLE);
                iv03_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);
                iv04_ereport_add.setVisibility(View.INVISIBLE);


                for(int i = 0 ; i < mGoodPicList.size() ; i ++){
                    Arad.imageLoader.load(mGoodPicList.get(i).getZipImgUrl()).into(listImg.get(i));
                }

            }

            if(mGoodPicList.size() == 3){
                iv01_ereport_add.setVisibility(View.VISIBLE);
                iv02_ereport_add.setVisibility(View.VISIBLE);
                iv03_ereport_add.setVisibility(View.VISIBLE);
                iv04_ereport_add.setVisibility(View.VISIBLE);
                iv04_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);


                for(int i = 0 ; i < mGoodPicList.size() ; i ++){
                    Arad.imageLoader.load(mGoodPicList.get(i).getZipImgUrl()).into(listImg.get(i));
                }
            }

           if (mGoodPicList.size() == 4){
               iv01_ereport_add.setVisibility(View.VISIBLE);
               iv02_ereport_add.setVisibility(View.VISIBLE);
               iv03_ereport_add.setVisibility(View.VISIBLE);
               iv04_ereport_add.setVisibility(View.VISIBLE);



               for(int i = 0 ; i < mGoodPicList.size() ; i ++){
                   Arad.imageLoader.load(mGoodPicList.get(i).getZipImgUrl()).into(listImg.get(i));
               }
           }
        }
    }

    private void delImg(int imgFlg) {
        if(mGoodPicList != null){

            /**长按 第一张图*/
            if(imgFlg == 1){
                if(mGoodPicList.size() == 1){

                    mGoodPicList.remove(0);

                    iv01_ereport_add.setVisibility(View.VISIBLE);
                    iv01_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);
                    iv02_ereport_add.setVisibility(View.INVISIBLE);
                    iv03_ereport_add.setVisibility(View.INVISIBLE);
                    iv04_ereport_add.setVisibility(View.INVISIBLE);
                    setBitmapImg();

                }

                if(mGoodPicList.size() == 2){
                    mGoodPicList.remove(0);

                    iv01_ereport_add.setVisibility(View.VISIBLE);
                    iv02_ereport_add.setVisibility(View.VISIBLE);
                    iv02_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);
                    iv03_ereport_add.setVisibility(View.INVISIBLE);
                    iv04_ereport_add.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

                if(mGoodPicList.size() == 3){
                    mGoodPicList.remove(0);


                    iv01_ereport_add.setVisibility(View.VISIBLE);
                    iv02_ereport_add.setVisibility(View.VISIBLE);
                    iv03_ereport_add.setVisibility(View.VISIBLE);
                    iv03_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);
                    iv04_ereport_add.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }
                if(mGoodPicList.size() == 4){
                    mGoodPicList.remove(0);


                    iv01_ereport_add.setVisibility(View.VISIBLE);
                    iv02_ereport_add.setVisibility(View.VISIBLE);
                    iv03_ereport_add.setVisibility(View.VISIBLE);
                    iv04_ereport_add.setVisibility(View.VISIBLE);
                    iv04_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);


                    setBitmapImg();
                }
            }

            /**长按 第二张图*/
            if(imgFlg == 2){
                if(mGoodPicList.size() == 1){
                    //
                }

                if(mGoodPicList.size() == 2){
                   mGoodPicList.remove(1);

                    iv01_ereport_add.setVisibility(View.VISIBLE);
                    iv02_ereport_add.setVisibility(View.VISIBLE);
                    iv02_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);
                    iv03_ereport_add.setVisibility(View.INVISIBLE);
                    iv04_ereport_add.setVisibility(View.INVISIBLE);

                    setBitmapImg();
                }

                if(mGoodPicList.size() == 3){
                    mGoodPicList.remove(1);


                    iv01_ereport_add.setVisibility(View.VISIBLE);
                    iv02_ereport_add.setVisibility(View.VISIBLE);
                    iv03_ereport_add.setVisibility(View.VISIBLE);
                    iv03_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);
                    iv04_ereport_add.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

                if(mGoodPicList.size() == 4){
                    mGoodPicList.remove(1);


                    iv01_ereport_add.setVisibility(View.VISIBLE);
                    iv02_ereport_add.setVisibility(View.VISIBLE);
                    iv03_ereport_add.setVisibility(View.VISIBLE);
                    iv04_ereport_add.setVisibility(View.VISIBLE);
                    iv04_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);


                    setBitmapImg();
                }
            }


            /**长按 第三张图*/
            if(imgFlg == 3){
                if(mGoodPicList.size() == 1){
//
                }

                if(mGoodPicList.size() == 2){
                    //
                }

                if(mGoodPicList.size() == 3){
                    mGoodPicList.remove(2);


                    iv01_ereport_add.setVisibility(View.VISIBLE);
                    iv02_ereport_add.setVisibility(View.VISIBLE);
                    iv03_ereport_add.setVisibility(View.VISIBLE);
                    iv03_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);
                    iv04_ereport_add.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

                if(mGoodPicList.size() == 4){
                    mGoodPicList.remove(1);


                    iv01_ereport_add.setVisibility(View.VISIBLE);
                    iv02_ereport_add.setVisibility(View.VISIBLE);
                    iv03_ereport_add.setVisibility(View.VISIBLE);
                    iv03_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);
                    iv04_ereport_add.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }
            }
            /**长按 第四张图*/
            if(imgFlg == 4){
                if(mGoodPicList.size() == 1){
//
                }

                if(mGoodPicList.size() == 2){
                    //
                }

                if(mGoodPicList.size() == 3){

                }

                if(mGoodPicList.size()==4){
                    mGoodPicList.remove(3);
                    iv01_ereport_add.setVisibility(View.VISIBLE);
                    iv02_ereport_add.setVisibility(View.VISIBLE);
                    iv03_ereport_add.setVisibility(View.VISIBLE);
                    iv04_ereport_add.setVisibility(View.VISIBLE);
                    iv04_ereport_add.setImageResource(R.mipmap.iconfont_jiahao);


                    setBitmapImg();
                }
            }

        }
    }


}
