package com.ctrl.android.yinfeng.ui.job;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.customview.TestanroidpicActivity;
import com.ctrl.android.yinfeng.dao.ImgDao;
import com.ctrl.android.yinfeng.dao.JobDao;
import com.ctrl.android.yinfeng.entity.Img2;
import com.ctrl.android.yinfeng.utils.StrConstant;
import com.ctrl.android.yinfeng.utils.TimeUtil;
import com.ctrl.android.yinfeng.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*我的工单（处理中）activity*/

public class JobProcessingActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.tv_repairs_progress)//报修进度
    TextView tv_repairs_progress;
    @InjectView(R.id.tv_repairs_time)//报修时间
    TextView tv_repairs_time;
    @InjectView(R.id.tv_repairs_type)//报修类型
    TextView tv_repairs_type;
    @InjectView(R.id.tv_repairs_money)//付款金额
    TextView tv_repairs_money;
    @InjectView(R.id.tv_repairs_content)//报修内容
    TextView tv_repairs_content;
    @InjectView(R.id.tv_repairs_name)//处理人员
    TextView tv_repairs_name;
    @InjectView(R.id.tv_repairs_telphone)//联系电话
    TextView tv_repairs_telphone;
    @InjectView(R.id.et_repairs_content)//处理结果
    TextView et_repairs_content;
    @InjectView(R.id.iv01_repairs)//图片1
    ImageView iv01_repairs;
    @InjectView(R.id.iv02_repairs)//图片2
    ImageView iv02_repairs;
    @InjectView(R.id.iv03_repairs)//图片3
    ImageView iv03_repairs;
    @InjectView(R.id.iv04_repairs)//图片4
    ImageView iv04_repairs;
    @InjectView(R.id.iv05_repairs)//图片5
    ImageView iv05_repairs;
    @InjectView(R.id.iv06_repairs)//图片6
    ImageView iv06_repairs;
    @InjectView(R.id.tv_baoxiu_image)//实景照片
    TextView tv_baoxiu_image;
    private JobDao dao;
    List<Img2>mGoodPicList=new ArrayList<>();


    /* 请求码*/
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;

    private List<ImageView> listImg=new ArrayList<>();
    private String[] items = new String[]{"本地图片", "拍照"};
    private int imageFlag=-1;
    private ImgDao iDao;
    private String REPAIRS_ADD_ACTIVITYID="REPAIRS_ADD_ACTIVITYID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_activity_repairs_processing);
        ButterKnife.inject(this);
        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
    }

    private void init() {
        iDao=new ImgDao(this);
        iv01_repairs.setOnClickListener(this);
        iv02_repairs.setOnClickListener(this);
        iv03_repairs.setOnClickListener(this);
        iv04_repairs.setOnClickListener(this);
        iv05_repairs.setOnClickListener(this);
        iv06_repairs.setOnClickListener(this);

        listImg.add(iv04_repairs);
        listImg.add(iv05_repairs);
        listImg.add(iv06_repairs);
        dao=new JobDao(this);
        dao.requestRepair(getIntent().getStringExtra("repairDemandId"));



    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==1){
            tv_repairs_progress.setText("报修进度：处理中");
            tv_repairs_time.setText("报修时间："+ TimeUtil.date(Long.parseLong(dao.getRepair().getCreateTime())));
            if(dao.getRepair().getrepairKindList()!=null) {
                tv_repairs_type.setText("报修类型：" + dao.getRepair().getrepairKindList().get(0).getKindName());
            }
            tv_repairs_money.setText("付款金额："+dao.getRepair().getMaintenanceCosts()+"元");
            tv_repairs_content.setText(dao.getRepair().getContent());
            tv_repairs_name.setText("处理人员:"+dao.getRepair().getStaffName());
            tv_repairs_telphone.setText("联系电话："+dao.getRepair().getStaffMobile());
            if(dao.getRepairPicList().size()<1){
                tv_baoxiu_image.setVisibility(View.GONE);
                iv01_repairs.setVisibility(View.GONE);
                iv02_repairs.setVisibility(View.GONE);
                iv03_repairs.setVisibility(View.GONE);
            }
            if(dao.getRepairPicList().size()>=1){
                Arad.imageLoader.load(dao.getRepairPicList().get(0).getZipImg()).placeholder(R.mipmap.default_image).into(iv01_repairs);
            }
            if(dao.getRepairPicList().size()>=2){
                Arad.imageLoader.load(dao.getRepairPicList().get(1).getZipImg()).placeholder(R.mipmap.default_image).into(iv02_repairs);
            }
            if(dao.getRepairPicList().size()>=3){
                Arad.imageLoader.load(dao.getRepairPicList().get(2).getZipImg()).placeholder(R.mipmap.default_image).into(iv03_repairs);
            }
        }

       if(3==requestCode){
           MessageUtils.showShortToast(this, "反馈成功");
           setResult(333);
           finish();
       }

        if(requestCode==101){
            showProgress(false);
            MessageUtils.showShortToast(this, "图片上传成功");
            Img2 img2=iDao.getImg();
            mGoodPicList.add(img2);
            setBitmapImg();
            iv04_repairs.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mGoodPicList.size() >= 1) {
                        imageFlag = 1;
                        showDelDialog(1);
                    }
                    return true;
                }
            });
            iv05_repairs.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mGoodPicList.size() >= 2) {
                        imageFlag = 2;
                        showDelDialog(2);
                    }
                    return true;
                }
            });
            iv06_repairs.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mGoodPicList.size() >= 3) {
                        imageFlag = 3;
                        showDelDialog(3);
                    }
                    return true;
                }
            });

        }

    }

    /**
     * 保存裁剪之后的图片数据
     * @param uri
     */
    private void getImageToView(Uri uri) {
        //Log.d("demo", "上传方法");
        Bitmap bitmap ;
        //Bundle extras = data.getExtras();
        try{
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor =getContentResolver().query(uri, filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);  //获取照片路径
            cursor.close();
            bitmap = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
            byte[] buffer = out.toByteArray();
            byte[] encode = Base64.encode(buffer, Base64.DEFAULT);

            String photo = new String(encode);

            Drawable drawable = new BitmapDrawable(bitmap);
            if (photo != null){
              //  Log.d("demo","上传方法2");
                /**调用后台方法  将图片上传**/
                String activityId = "";
                String imgData = photo;
                String typeKey = "AC";//活动配图
                String optMode = "0";//0：添加、1:编辑
                showProgress(true);
                iDao.requestUploadImg(REPAIRS_ADD_ACTIVITYID, photo, StrConstant.REPAIRS_PRETREAMENT_IMAGE, "1");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

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
                iDao.requestUploadImg(REPAIRS_ADD_ACTIVITYID, photo, StrConstant.REPAIRS_PRETREAMENT_IMAGE, "1");
            }
        } catch (Exception e){
            e.printStackTrace();
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


    private void setImageViewWidth(ImageView imageView){

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int w=imageView.getWidth();
        android.util.Log.d("demo", "width : " + w);
        params.height=w;
        android.util.Log.d("demo", "height : " + params.height);
        imageView.setLayoutParams(params);

    }


    private void setBitmapImg(){

        setImageViewWidth(iv04_repairs);
        setImageViewWidth(iv05_repairs);
        setImageViewWidth(iv06_repairs);

        if (mGoodPicList != null){

            if(mGoodPicList.size()==0){
                iv04_repairs.setVisibility(View.VISIBLE);
                iv04_repairs.setImageResource(R.mipmap.iconfont_jiahao);
                iv05_repairs.setVisibility(View.INVISIBLE);
                iv06_repairs.setVisibility(View.INVISIBLE);
            }
            if(mGoodPicList.size() == 1) {
                iv04_repairs.setVisibility(View.VISIBLE);
                iv05_repairs.setVisibility(View.VISIBLE);
                iv05_repairs.setImageResource(R.mipmap.iconfont_jiahao);
                iv06_repairs.setVisibility(View.INVISIBLE);


                for(int i = 0 ; i < mGoodPicList.size() ; i ++){
                    Arad.imageLoader.load(mGoodPicList.get(i).getZipImgUrl()).into(listImg.get(i));
                }

            }

            if(mGoodPicList.size() == 2){
                iv04_repairs.setVisibility(View.VISIBLE);
                iv05_repairs.setVisibility(View.VISIBLE);
                iv06_repairs.setVisibility(View.VISIBLE);
                iv06_repairs.setImageResource(R.mipmap.iconfont_jiahao);


                for(int i = 0 ; i < mGoodPicList.size() ; i ++){
                    Arad.imageLoader.load(mGoodPicList.get(i).getZipImgUrl()).into(listImg.get(i));
                }

            }

            if(mGoodPicList.size() == 3){
                iv04_repairs.setVisibility(View.VISIBLE);
                iv05_repairs.setVisibility(View.VISIBLE);
                iv06_repairs.setVisibility(View.VISIBLE);


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

                    iv04_repairs.setVisibility(View.VISIBLE);
                    iv04_repairs.setImageResource(R.mipmap.iconfont_jiahao);
                    iv05_repairs.setVisibility(View.INVISIBLE);
                    iv06_repairs.setVisibility(View.INVISIBLE);
                    setBitmapImg();

                }

                if(mGoodPicList.size() == 2){
                    mGoodPicList.remove(0);

                    iv04_repairs.setVisibility(View.VISIBLE);
                    iv05_repairs.setVisibility(View.VISIBLE);
                    iv05_repairs.setImageResource(R.mipmap.iconfont_jiahao);
                    iv06_repairs.setVisibility(View.INVISIBLE);


                    setBitmapImg();
                }

                if(mGoodPicList.size() == 3){
                    mGoodPicList.remove(0);


                    iv04_repairs.setVisibility(View.VISIBLE);
                    iv05_repairs.setVisibility(View.VISIBLE);
                    iv06_repairs.setVisibility(View.VISIBLE);
                    iv06_repairs.setImageResource(R.mipmap.iconfont_jiahao);


                    setBitmapImg();
                }
                if(mGoodPicList.size() == 4){
                    mGoodPicList.remove(0);


                    iv04_repairs.setVisibility(View.VISIBLE);
                    iv05_repairs.setVisibility(View.VISIBLE);
                    iv06_repairs.setVisibility(View.VISIBLE);


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

                    iv04_repairs.setVisibility(View.VISIBLE);
                    iv05_repairs.setImageResource(R.mipmap.iconfont_jiahao);
                    iv06_repairs.setVisibility(View.INVISIBLE);

                    setBitmapImg();
                }

                if(mGoodPicList.size() == 3){
                    mGoodPicList.remove(1);


                    iv04_repairs.setVisibility(View.VISIBLE);
                    iv05_repairs.setVisibility(View.VISIBLE);
                    iv06_repairs.setVisibility(View.VISIBLE);
                    iv06_repairs.setImageResource(R.mipmap.iconfont_jiahao);


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


                    iv04_repairs.setVisibility(View.VISIBLE);
                    iv05_repairs.setVisibility(View.VISIBLE);
                    iv06_repairs.setVisibility(View.VISIBLE);
                    iv06_repairs.setImageResource(R.mipmap.iconfont_jiahao);


                    setBitmapImg();
                }

            }

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
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    /**
     * header 左侧按钮
     * */
    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MessageUtils.showShortToast(MallShopActivity.this, "AA");
                onBackPressed();
            }
        });
        return true;
    }

    /**
     * 页面标题
     * */
    @Override
    public String setupToolBarTitle() {
        return "报修详情";
    }


    /*
    * */

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("完成");
        mRightText.setTextColor(Color.WHITE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(TextUtils.isEmpty(et_repairs_content.getText().toString())){
                        MessageUtils.showShortToast(JobProcessingActivity.this,"处理结果未填写");
                    }else if(mGoodPicList.size()==1){
                        dao.requestRepairFillResult(getIntent().getStringExtra("repairDemandId"),
                                    et_repairs_content.getText().toString(),
                                    mGoodPicList.get(0).getImgUrl()+","+mGoodPicList.get(0).getZipImgUrl(),
                                    "",
                                    "");
                    }else if(mGoodPicList.size()==2){
                        dao.requestRepairFillResult(getIntent().getStringExtra("repairDemandId"),
                                    et_repairs_content.getText().toString(),
                                    mGoodPicList.get(0).getImgUrl()+","+mGoodPicList.get(0).getZipImgUrl(),
                                    mGoodPicList.get(1).getImgUrl()+","+mGoodPicList.get(1).getZipImgUrl(),
                                    "");
                    }else if(mGoodPicList.size()==3){
                        dao.requestRepairFillResult(getIntent().getStringExtra("repairDemandId"),
                                    et_repairs_content.getText().toString(),
                                    mGoodPicList.get(0).getImgUrl()+","+mGoodPicList.get(0).getZipImgUrl(),
                                    mGoodPicList.get(1).getImgUrl()+","+mGoodPicList.get(1).getZipImgUrl(),
                                    mGoodPicList.get(2).getImgUrl()+","+mGoodPicList.get(2).getZipImgUrl()
                                    );
                    }else{
                        dao.requestRepairFillResult(getIntent().getStringExtra("repairDemandId"),
                        et_repairs_content.getText().toString(),
                        "",
                       "",
                       ""
                        );
                    }


            }
        });

        return true;
    }



    @Override
    public void onClick(View v) {

        if(v==iv01_repairs&&dao.getRepairPicList().size()>=1){
            scaleImage(dao.getRepairPicList().get(0).getOriginalImg(),iv01_repairs,this);
        }
        if(v==iv02_repairs&&dao.getRepairPicList().size()>=2){
            scaleImage(dao.getRepairPicList().get(1).getOriginalImg(),iv02_repairs,this);
        }
        if(v==iv03_repairs&&dao.getRepairPicList().size()>=3){
            scaleImage(dao.getRepairPicList().get(2).getOriginalImg(),iv03_repairs,this);
        }
        if(v == iv04_repairs){
            if(mGoodPicList.size() >= 1){
                //
            } else{
                showDialog();
            }

            //setBitmapImg();
        }

        if(v == iv05_repairs){
            if(mGoodPicList.size() >= 2){
                //
            } else {
                showDialog();
            }
            //setBitmapImg();
        }
        if(v == iv06_repairs){
            if(mGoodPicList.size() >= 3){
                //
            } else {
                showDialog();
            }
            //setBitmapImg();
        }


        }

    private void scaleImage(String url,ImageView iv,Activity activity){
        Intent intent=new Intent(activity, TestanroidpicActivity.class);
        intent.putExtra("imageurl", url);
        int[] location = new int[2];
        iv.getLocationOnScreen(location);
        intent.putExtra("locationX", location[0]);
        intent.putExtra("locationY", location[1]);
        intent.putExtra("width", iv.getWidth());
        intent.putExtra("height", iv.getHeight());
        startActivity(intent);
        overridePendingTransition(0, 0);
    }



}
