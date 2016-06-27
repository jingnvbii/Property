package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.customview.MineHeadView;
import com.ctrl.forum.dao.EditDao;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.utils.DataCleanUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 编辑界面
 */
public class MineEditActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.update_head)
    RelativeLayout update_head;  //修改头像
    @InjectView(R.id.rl_ni)
    RelativeLayout rl_ni;  //修改昵称
    @InjectView(R.id.rl_jianjie)
    RelativeLayout rl_jianjie;  //修改简介
    @InjectView(R.id.rl_phone)
    RelativeLayout rl_phone;  //修改电话号码
    @InjectView(R.id.rl_xiaoqu)
    RelativeLayout rl_xiaoqu;  //修改小区
    @InjectView(R.id.rl_pwd)
    RelativeLayout rl_pwd;  //修改密码
    @InjectView(R.id.tv_ni)
    TextView tv_ni;    //昵称
    @InjectView(R.id.tv_jianjie)
    TextView tv_jianjie;    //简介
    @InjectView(R.id.tv_phone)
    TextView tv_phone;    //电话
    @InjectView(R.id.tv_xiaoqu)
    TextView tv_xiaoqu;    //小区
    @InjectView(R.id.iv_head)
    MineHeadView iv_head; //头像
    @InjectView(R.id.tv_tuichu)
    TextView tv_tuichu;    //昵称

    private EditDao edao;
    private MemberInfo memberInfo;//会员基本信息
    private TextView take_picture,choose_phone,cancel;
    private View view;
    private PopupWindow popupWindow;

    //查看相册Intent  key
    public static final int LOOK_ALBUM_INTENT = 10009;
    //访问相机Intent  key
    public static final int LOOK_CAMERA_INTENT=10010;
    //裁剪照片Intent  key
    public static final int CROP_PHOTO_INTENT = 10011;

    private String encodeToString;//base64图片
    private ImageView sv_loans_certificate;//上传头像
    private ImageView image1;//加号
    private Bitmap bitmap;

    private File file = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());

    // 使用系统当前日期加以调整作为照片的名称
    /*@SuppressLint("SimpleDateFormat")*/
    private String getPhotoFileName() {
        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        return name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.inject(this);
        init();
        initPop();
        putData();
    }

    //为控件赋值
    private void putData() {
        String id = Arad.preferences.getString("memberId");
        edao.getVipInfo(id);
        tv_ni.setText(Arad.preferences.getString("nickName"));
        tv_jianjie.setText(Arad.preferences.getString("remark"));
        tv_phone.setText(Arad.preferences.getString("mobile"));
        tv_xiaoqu.setText(Arad.preferences.getString("communityName"));

        String imgUrl = Arad.preferences.getString("imgUrl");
        if (imgUrl!=null&&!imgUrl.equals(""))
            Arad.imageLoader.load(imgUrl).placeholder(getResources().getDrawable(R.mipmap.iconfont_head)).into(iv_head);//设置头像
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==1){
            String url = edao.getImageUrl();
            if (url!=null && url.equals("")){
                Arad.preferences.putString("imgUrl",url);
                Arad.preferences.flush();
                String imgUrl = Arad.preferences.getString("imgUrl");
                if (imgUrl!=null&&!imgUrl.equals(""))
                    Arad.imageLoader.load(imgUrl).placeholder(getResources().getDrawable(R.mipmap.iconfont_head)).into(iv_head);//设置头像
            }
        }
    }

    //弹窗
    private void initPop() {
        view = LayoutInflater.from(this).inflate(R.layout.update_head,null);
        popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
        popupWindow.setFocusable(true);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.pop_bg));
        colorDrawable.setAlpha(40);
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setOutsideTouchable(true);

        take_picture = (TextView) view.findViewById(R.id.take_picture);
        choose_phone = (TextView) view.findViewById(R.id.choose_phone);
        cancel = (TextView) view.findViewById(R.id.cancel);
        take_picture.setOnClickListener(this);
        choose_phone.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
    //控件的初始化
    private void init() {
        edao = new EditDao(this);

        update_head.setOnClickListener(this);
        rl_ni.setOnClickListener(this);
        rl_jianjie.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_xiaoqu.setOnClickListener(this);
        rl_pwd.setOnClickListener(this);
        tv_tuichu.setOnClickListener(this);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.bianji);}

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent =null;
        switch (id){
            //修改头像
            case R.id.update_head:
                popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 0);  //弹出
                popupWindow.update();
                break;
            case R.id.rl_ni://昵称
                intent= new Intent(getApplicationContext(), MineNickNameActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_jianjie://签名
                intent = new Intent(getApplicationContext(),MineSignActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_phone://
                break;
            case R.id.rl_xiaoqu://小区
                startActivity(new Intent(this,MineFindFlotActivity.class));
                break;
            case R.id.rl_pwd://修改密码
                intent = new Intent(getApplicationContext(),MineUpdatepwdActivity.class);
                startActivity(intent);
                break;
            case R.id.take_picture: //拍照片
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, LOOK_CAMERA_INTENT);
                //关闭弹窗
                popupWindow.dismiss();
                break;
            case R.id.choose_phone: //选择照片
                Intent intent1;
                intent1 = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                intent1.setType("image*//*");
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1,LOOK_ALBUM_INTENT);
                //关闭弹窗
                popupWindow.dismiss();
                break;
            case R.id.cancel: //取消
                popupWindow.dismiss();
                break;
            case R.id.tv_tuichu:
                DataCleanUtils.clearAllCache(this.getApplicationContext());
                this.finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        putData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LOOK_ALBUM_INTENT://相册
                    if (data != null) {
                        startPhotoZoom(data.getData());
                    }
                    break;
                case LOOK_CAMERA_INTENT://相机
                    startPhotoZoom(Uri.fromFile(file));
                    break;
                case CROP_PHOTO_INTENT: // 图片缩放完成后
                    if (null != data) {
                        Log.e("", "-------------------" + data);
                        setPicToView(data);
                    }
                    break;
            }

        }
    }

    /**
     * 调用系统裁剪
     */
    private void startPhotoZoom(Uri uri) {
        // TODO Auto-generated method stub
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        // 剪切比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        int size = 360;
        // 输出大小
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PHOTO_INTENT);
    }

    /**
     * 设置图片并将图片转成base64
     */
    private void setPicToView(Intent arg2) {
        Bundle bundle = arg2.getExtras();
        bitmap = bundle.getParcelable("data");// 获取相机返回的数据，并转换为Bitmap图片格式
        iv_head.setImageBitmap(bitmap);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 把数据写入文件
        byte[] bytes = baos.toByteArray();
        //转成base64
        encodeToString = Base64.encodeToString(bytes, Base64.DEFAULT);

        //设置头像
        //-----------------------------------------------------------
        //调用接口上传服务器。。。encodeToString是String形式

         edao.modifyImgUrl(Arad.preferences.getString("memberId"),encodeToString);

        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
