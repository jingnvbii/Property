package com.ctrl.android.yinfeng.ui.job;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.customview.TestanroidpicActivity;
import com.ctrl.android.yinfeng.dao.JobDao;
import com.ctrl.android.yinfeng.entity.Img;
import com.ctrl.android.yinfeng.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*我的工单（处理中）activity*/

public class JobProcessedActivity extends AppToolBarActivity implements View.OnClickListener {
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
    @InjectView(R.id.tv_repairs_result)//处理结果
    TextView tv_repairs_result;
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
    @InjectView(R.id.tv_wuye_image)//反馈照片
    TextView tv_wuye_image;
    private JobDao dao;
    List<Img>mGoodPicList=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_activity_repairs_processed);
        ButterKnife.inject(this);
        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
    }

    private void init() {
        iv01_repairs.setOnClickListener(this);
        iv02_repairs.setOnClickListener(this);
        iv03_repairs.setOnClickListener(this);
        iv04_repairs.setOnClickListener(this);
        iv05_repairs.setOnClickListener(this);
        iv06_repairs.setOnClickListener(this);
        dao=new JobDao(this);
        if(getIntent().getFlags()==999){
            dao.requestRepair(getIntent().getStringExtra("repairDemandId1"));
        }else {
            dao.requestRepair(getIntent().getStringExtra("repairDemandId"));
        }


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
            tv_repairs_result.setText(dao.getRepair().getResult());
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

            if(dao.getRepairResultPicList().size()<1){
                tv_wuye_image.setVisibility(View.GONE);
                iv04_repairs.setVisibility(View.GONE);
                iv05_repairs.setVisibility(View.GONE);
                iv06_repairs.setVisibility(View.GONE);
            }

            if(dao.getRepairResultPicList().size()>=1){
                Arad.imageLoader.load(dao.getRepairResultPicList().get(0).getZipImg()).placeholder(R.mipmap.default_image).into(iv04_repairs);
            }
            if(dao.getRepairResultPicList().size()>=2){
                Arad.imageLoader.load(dao.getRepairResultPicList().get(1).getZipImg()).placeholder(R.mipmap.default_image).into(iv05_repairs);
            }
            if(dao.getRepairResultPicList().size()>=3){
                Arad.imageLoader.load(dao.getRepairResultPicList().get(2).getZipImg()).placeholder(R.mipmap.default_image).into(iv06_repairs);
            }

        }

       if(3==requestCode){
           MessageUtils.showShortToast(this, "反馈成功");
           setResult(333);
           finish();
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



    @Override
    public void onClick(View v) {
        if (v == iv01_repairs && dao.getRepairPicList().size() >= 1) {
            scaleImage(dao.getRepairPicList().get(0).getOriginalImg(), iv01_repairs, this);
        }
        if (v == iv02_repairs && dao.getRepairPicList().size() >= 2) {
            scaleImage(dao.getRepairPicList().get(1).getOriginalImg(), iv02_repairs, this);
        }
        if (v == iv03_repairs && dao.getRepairPicList().size() >= 3) {
            scaleImage(dao.getRepairPicList().get(2).getOriginalImg(), iv03_repairs, this);
        }
        if (v == iv04_repairs && dao.getRepairResultPicList().size() >= 1) {
            scaleImage(dao.getRepairResultPicList().get(0).getOriginalImg(), iv04_repairs, this);
            Log.i("imgurl","imgurl "+dao.getRepairResultPicList().get(0).getOriginalImg());
        }
        if (v == iv05_repairs && dao.getRepairResultPicList().size() >= 2) {
            scaleImage(dao.getRepairResultPicList().get(1).getOriginalImg(), iv05_repairs, this);
        }
        if (v == iv06_repairs && dao.getRepairResultPicList().size() >= 3) {
            scaleImage(dao.getRepairResultPicList().get(2).getOriginalImg(), iv06_repairs, this);
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
