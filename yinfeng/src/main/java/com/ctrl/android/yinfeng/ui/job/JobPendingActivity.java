package com.ctrl.android.yinfeng.ui.job;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

/*我的工单（待处理）activity*/

public class JobPendingActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.tv_my_repairs_pending_progress)//报修进度
    TextView tv_my_repairs_pending_progress;
    @InjectView(R.id.tv_my_repairs_pending_time)//报修时间
    TextView tv_my_repairs_pending_time;
    @InjectView(R.id.tv_repairs_type)//报修类型
    TextView tv_repairs_type;
    @InjectView(R.id.tv_my_repairs_pending_money)//付款金额
    TextView tv_my_repairs_pending_money;
    @InjectView(R.id.tv_my_repairs_pending_content)//报修内容
    TextView tv_my_repairs_pending_content;
    @InjectView(R.id.iv01_my_repairs_pending)//图片1
    ImageView iv01_my_repairs_pending;
    @InjectView(R.id.iv02_my_repairs_pending)//图片2
    ImageView iv02_my_repairs_pending;
    @InjectView(R.id.iv03_my_repairs_pending)//图片3
    ImageView iv03_my_repairs_pending;
    @InjectView(R.id.tv_repairs_accept)//抢单
    TextView tv_repairs_accept;
    @InjectView(R.id.tv_baoxiu_image)//实景照片
    TextView tv_baoxiu_image;
    private JobDao dao;
    private List<ImageView> listImg;
    List<Img>mGoodPicList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_activity_repairs_pending);
        ButterKnife.inject(this);
        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
    }

    private void init() {
        iv01_my_repairs_pending.setOnClickListener(this);
        iv02_my_repairs_pending.setOnClickListener(this);
        iv03_my_repairs_pending.setOnClickListener(this);
        tv_repairs_accept.setOnClickListener(this);

        listImg=new ArrayList<>();
        listImg.add(iv01_my_repairs_pending);
        listImg.add(iv02_my_repairs_pending);
        listImg.add(iv03_my_repairs_pending);
        dao=new JobDao(this);
        dao.requestRepair(getIntent().getStringExtra("repairDemandId"));
       if(Arad.preferences.getString("grade").equals("2")) {
           if (getIntent().getStringExtra("orderType").equals("0")) {
               tv_repairs_accept.setText("抢单");
           } else if (getIntent().getStringExtra("orderType").equals("1")) {
               tv_repairs_accept.setText("接单");
           }
       }else if(Arad.preferences.getString("grade").equals("0")||
               (Arad.preferences.getString("grade").equals("1")&&Arad.preferences.getString("jobType").equals("1"))
               ||(Arad.preferences.getString("grade").equals("1")&&Arad.preferences.getString("jobType").equals("2"))){
           if(getIntent().getStringExtra("orderType").equals("0")){
               tv_repairs_accept.setText("指派");
           }else {
               tv_repairs_accept.setVisibility(View.GONE);
           }
       }else {

       }

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if(requestCode==1){
            tv_my_repairs_pending_progress.setText("报修进度：待处理");
            tv_my_repairs_pending_time.setText("报修时间："+ TimeUtil.date(Long.parseLong(dao.getRepair().getCreateTime())));
            if(dao.getRepair().getrepairKindList()!=null) {
                tv_repairs_type.setText("报修类型：" + dao.getRepair().getrepairKindList().get(0).getKindName());
            }
            tv_my_repairs_pending_money.setText("付款金额："+dao.getRepair().getMaintenanceCosts()+"元");
            tv_my_repairs_pending_content.setText(dao.getRepair().getContent());
            if(dao.getRepairPicList().size()<1){
                    tv_baoxiu_image.setVisibility(View.GONE);
                    iv01_my_repairs_pending.setVisibility(View.GONE);
                    iv02_my_repairs_pending.setVisibility(View.GONE);
                    iv03_my_repairs_pending.setVisibility(View.GONE);
            }
            if(dao.getRepairPicList().size()>=1){
                Arad.imageLoader.load(dao.getRepairPicList().get(0).getZipImg()).placeholder(R.mipmap.default_image).into(iv01_my_repairs_pending);
            }
            if(dao.getRepairPicList().size()>=2){
                Arad.imageLoader.load(dao.getRepairPicList().get(1).getZipImg()).placeholder(R.mipmap.default_image).into(iv02_my_repairs_pending);
            }
            if(dao.getRepairPicList().size()>=3){
                Arad.imageLoader.load(dao.getRepairPicList().get(2).getZipImg()).placeholder(R.mipmap.default_image).into(iv03_my_repairs_pending);
            }
        }

       if(2==requestCode){

           if(getIntent().getStringExtra("orderType").equals("0")){
               MessageUtils.showShortToast(this, "抢单成功");
           }else if(getIntent().getStringExtra("orderType").equals("1")){
               MessageUtils.showShortToast(this, "接单成功");

           }
           setResult(667);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==55&&resultCode==RESULT_OK){
            setResult(667);
            finish();
        }
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

        switch (v.getId()){
            case R.id.tv_repairs_accept:

                if(Arad.preferences.getString("grade").equals("2")) {
                    dao.requestRepairScrambleBill(
                            getIntent().getStringExtra("repairDemandId"),
                            Arad.preferences.getString("staffId"),
                            getIntent().getStringExtra("orderType")
                    );
                }else if(Arad.preferences.getString("grade").equals("0")||
                        (Arad.preferences.getString("grade").equals("1")&&Arad.preferences.getString("jobType").equals("1"))
                        ||(Arad.preferences.getString("grade").equals("1")&&Arad.preferences.getString("jobType").equals("2"))){

                    Intent intent =new Intent(this, RepairStaffListActivity.class);
                    intent.putExtra("repairDemandId", getIntent().getStringExtra("repairDemandId")
                    );
                    startActivityForResult(intent, 55);
                }else {

                }

                break;
            case R.id.iv01_my_repairs_pending:
                if(dao.getRepairPicList().size()>=1) {
                    scaleImage(dao.getRepairPicList().get(0).getOriginalImg(), iv01_my_repairs_pending, this);
                }
                break;
            case R.id.iv02_my_repairs_pending:
                if(dao.getRepairPicList().size()>=2) {
                    scaleImage(dao.getRepairPicList().get(1).getOriginalImg(), iv02_my_repairs_pending, this);
                }
                break;
            case R.id.iv03_my_repairs_pending:
                if(dao.getRepairPicList().size()>=3) {
                    scaleImage(dao.getRepairPicList().get(2).getOriginalImg(), iv03_my_repairs_pending, this);
                }
                break;
        }


    }
}
