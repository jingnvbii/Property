package com.ctrl.android.yinfeng.ui.cpatrol;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppHolder;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.customview.ListViewForScrollView;
import com.ctrl.android.yinfeng.dao.ImgDao;
import com.ctrl.android.yinfeng.dao.PatrolDao;
import com.ctrl.android.yinfeng.entity.Point;
import com.ctrl.android.yinfeng.utils.StrConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CPatrolLineActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.lv_patrol)//下拉列表
            ListViewForScrollView lv_patrol;
    @InjectView(R.id.sc_patrol)
    ScrollView sc_patrol;

    @InjectView(R.id.tv_patrol_time)//任务时间
    TextView tv_patrol_time;
    @InjectView(R.id.tv_patrol_load)//巡更路线
    TextView tv_patrol_load;
    @InjectView(R.id.tv_patrol_name)//执行人
    TextView tv_patrol_name;

    private PatrolDao pdao;
    private String nextPoint;
    private static final int CAMERA_REQUEST_CODE = 1;

    private int mPosition;
    private List<Point> mData;
    private int num;
    private ImgDao iDao;
    private String PATROL_LINE_ACTIVITY="PATROL_LINE_ACTIVITY";
    private String url;
    private String zipUrl;
    private PatrolLineAdapter1 adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_activity_patrol_line);
        ButterKnife.inject(this);
        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
    }

    private void init() {
        iDao=new ImgDao(this);
        pdao=new PatrolDao(this);
        pdao.requestPatrolDetail(getIntent().getStringExtra("patrolRouteStaffId"));

    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        showProgress(false


        );
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        if(101==requestCode){
            MessageUtils.showShortToast(this, "图片上传成功");
            url=iDao.getImg().getImgUrl();
            zipUrl=iDao.getImg().getZipImgUrl();
            adapter.setNum(num,zipUrl);

        }
        if(2==requestCode){
            MessageUtils.showShortToast(this, "此巡查点完成");
            num++;
            adapter.setNum(num,null);
            if(num>pdao.getPointList().size()){

                setResult(201);
                finish();
            }

        }
        if(1==requestCode){
           tv_patrol_name.setText(pdao.getPatrolRoute().getStaffName());
           tv_patrol_time.setText(pdao.getPatrolRoute().getCreateTime());
           tv_patrol_load.setText(pdao.getPatrolRoute().getRouteName());
            nextPoint=pdao.getNextPoint();

            mData=pdao.getPointList();
            if(nextPoint==null||nextPoint.equals("")){
                //
            }else {
                for (int i=0;i< mData.size();i++){
                    String currentPoint = mData.get(i).getPatrolRoutePointId();
                    if(nextPoint.equals(currentPoint)){
                        num = mData.get(i).getSortNum();
                    }
                }
                }

            adapter = new PatrolLineAdapter1(this);
            adapter.setData(mData);
            adapter.setNum(num,null);
            lv_patrol.setAdapter(adapter);
            sc_patrol.smoothScrollTo(0, 0);
            lv_patrol.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(resultCode==RESULT_CANCELED){
                return;
            }
            if (requestCode == CAMERA_REQUEST_CODE) {
                getImageToView1(Environment.getExternalStorageDirectory() + "/cxh.jpg");

            }
        }catch (Exception e){
            e.printStackTrace();
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
                String activityId = "";
                String imgData = photo;
                String typeKey = "AC";//活动配图
                String optMode = "0";//0：添加、1:编辑
                showProgress(true);
                iDao.requestUploadImg(PATROL_LINE_ACTIVITY, photo, StrConstant.PATROL_RESULT, "1");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        }
    /**
     * 保存裁剪之后的图片数据
     *
     * @param uri
     */
    private void getImageToView(Uri uri) {
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri,
                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);  //获取照片路径
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);//压缩比例50%
            out.flush();
            out.close();
            byte[] buffer = out.toByteArray();
            byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
            String photo = new String(encode);
            Drawable drawable = new BitmapDrawable(bitmap);


            if (photo != null){
                iDao.requestUploadImg(PATROL_LINE_ACTIVITY, photo, StrConstant.PATROL_RESULT, "1");
                showProgress(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
        return "巡查";
    }


    /**
     * 巡更巡查adapter
     * Created by Administrator on 2015/11/10.
     */
     class PatrolLineAdapter1 extends BaseAdapter {
        private Activity mActivity;
        List<Point> mData = new ArrayList<>();
        private int num;
        private String url=null;


        public PatrolLineAdapter1(Activity activity) {
            mActivity = activity;
        }
        public void setData(List<Point> mData) {
            this.mData = mData;
            notifyDataSetChanged();
        }
        public void setNum(int num,String url) {
            this.num = num;
            this.url=url;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }


        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.j_adapter_partrol_line, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Point point = mData.get(position);
            holder.tv_point_1.setText(point.getPointName());
            holder.tv_describe.setText(point.getKeyMessage());
            holder.setPosition(position);
            if(position==0){
                holder.tv_line01.setVisibility(View.INVISIBLE);
            }else {
                holder.tv_line01.setVisibility(View.VISIBLE);
            }
            /*if(position==mData.size()-1){
                holder.tv_line02.setVisibility(View.INVISIBLE);
            }else {
                holder.tv_line02.setVisibility(View.VISIBLE);
            }*/
            if(position<num-1||num==0){
                holder.iv_patrol_disc_1.setImageResource(R.mipmap.ring_red);
            }

            if(position==num-1){
                holder.ll_patrol_3.setVisibility(View.VISIBLE);
                holder.tv_patrol_end.setVisibility(View.VISIBLE);
            }else{
                holder.ll_patrol_3.setVisibility(View.GONE);
                holder.tv_patrol_end.setVisibility(View.GONE);
            }

            if(url!=null){
                Arad.imageLoader.load(zipUrl).placeholder(R.mipmap.default_image).into(holder.iv_patrol_camera);
                holder.iv_patrol_camera.setScaleType(ImageView.ScaleType.FIT_XY);
            }else {
                holder.iv_patrol_camera.setImageResource(R.mipmap.camera);
                holder.iv_patrol_camera.setScaleType(ImageView.ScaleType.CENTER);
            }

            return convertView;
        }

        class ViewHolder implements View.OnClickListener{


            @InjectView(R.id.tv_point_1)
            TextView tv_point_1;
            @InjectView(R.id.tv_describe)
            TextView tv_describe;
            @InjectView(R.id.tv_line01)//圆圈上方竖线
            TextView tv_line01;
            @InjectView(R.id.tv_line02)//圆圈下方竖线
            TextView tv_line02;
            @InjectView(R.id.iv_patrol_camera)//照相
                    ImageView iv_patrol_camera;
            @InjectView(R.id.et_patrol_content)//备注
                    EditText et_patrol_content;
            @InjectView(R.id.tv_patrol_end)//完成
                    TextView tv_patrol_end;
            @InjectView(R.id.iv_patrol_disc_1)//圆圈
                    ImageView iv_patrol_disc_1;
            @InjectView(R.id.ll_patrol_3)//照相布局
                    LinearLayout  ll_patrol_3;

            private int position;


            ViewHolder(View view) {
                ButterKnife.inject(this, view);
                iv_patrol_camera.setOnClickListener(this);
                tv_patrol_end.setOnClickListener(this);
            }


            public void setPosition(int position){
                this.position = position;
            }

            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.iv_patrol_camera:
                        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "cxh.jpg")));
                        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                        break;
                    case R.id.tv_patrol_end:
                        if(url==null||url.equals("")){
                            MessageUtils.showShortToast(CPatrolLineActivity.this, "请拍照");

                        }else {
                            pdao.requestPatrolRoutePointAdd(AppHolder.getInstance().getStaffInfo().getCommunityId(),
                                    pdao.getPatrolRoute().getPatrolRouteStaffId(),
                                    pdao.getPatrolRoute().getStaffId(),
                                    pdao.getPatrolRoute().getRouteId(),
                                    mData.get(position).getPointId(),
                                    et_patrol_content.getText().toString(),
                                    url+","+zipUrl
                            );
                            showProgress(true);
                        }
                        break;

                }

            }

        }
    }
}
