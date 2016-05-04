package com.ctrl.android.yinfeng.ui.notice;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.base.Constant;
import com.ctrl.android.yinfeng.base.MyApplication;
import com.ctrl.android.yinfeng.customview.ImageZoomActivity;
import com.ctrl.android.yinfeng.dao.NoticeDao;
import com.ctrl.android.yinfeng.entity.Img;
import com.ctrl.android.yinfeng.entity.Notice;
import com.ctrl.android.yinfeng.utils.D;
import com.ctrl.android.yinfeng.utils.S;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 内部公告详情 activity
 * Created by Eric on 2015/10/13.
 */
public class NoticeDetailActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.tv_notice_detail_title)
    TextView tv_notice_detail_title;
    @InjectView(R.id.tv_notice_detail_releaser)//作者 时间
    TextView tv_notice_detail_releaser;
    @InjectView(R.id.tv_notice_detail_content)
    TextView tv_notice_detail_content;
    @InjectView(R.id.iv_notice_detail_content)//图片
    ImageView iv_notice_detail_content;
    @InjectView(R.id.tv_notice_detail_confirm)//签收
    TextView tv_notice_detail_confirm;

    private NoticeDao dao;
    private Notice notice;

    private String propertNoticeId;
    private String activityId;

    private ArrayList<String> imagelist;//传入到图片放大类 用
    private int position = 0;
    private List<Img> imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.j_notice_detail_activity);
        ButterKnife.inject(this);
        init();
    }

    /**
     * 初始化执行的 方法
     * */
    private void init(){

        tv_notice_detail_confirm.setOnClickListener(this);
        dao = new NoticeDao(this);
        propertNoticeId = getIntent().getStringExtra("propertyNoticeId");
        Log.i("TAG","TAG"+propertNoticeId);
        activityId =Arad.preferences.getString("staffId");
        dao.requestNotice(propertNoticeId, activityId);
        showProgress(true);
    }

    @Override
    public void onClick(View v) {
        if(v == tv_notice_detail_confirm){
            //MessageUtils.showShortToast(this,"签收");
            dao.requestNoticeModify(activityId,propertNoticeId);
            showProgress(true);
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        if(requestCode == 1){
            notice = dao.getNotice();
            imgList=dao.getPropertyNoticePicList();
            tv_notice_detail_title.setText(notice.getNoticeTitle());
            tv_notice_detail_releaser.setText( D.getDateStrFromStamp(Constant.YYYY_MM_DD_HH_MM, notice.getCreateTime()));
            tv_notice_detail_content.setText(S.getStr(notice.getContent()));

            if(imgList==null){
                iv_notice_detail_content.setVisibility(View.INVISIBLE);
            } else {
                Arad.imageLoader
                        .load(imgList.get(0).getOriginalImg())
                        .placeholder(R.mipmap.default_image)
                        .into(iv_notice_detail_content);
                iv_notice_detail_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagelist = new ArrayList<String>();
                        imagelist.add(imgList.get(0).getOriginalImg());
                        Intent intent = new Intent(NoticeDetailActivity.this, ImageZoomActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("imageList", imagelist);
                        bundle.putInt("position", position);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            //状态（0：未读、1：已读）
            if(notice.getStatus().equals("0")){
                tv_notice_detail_confirm.setText("我知道了");
               tv_notice_detail_confirm.setBackgroundResource(R.drawable.red_border_register);
                tv_notice_detail_confirm.setClickable(true);
            } else {
                tv_notice_detail_confirm.setText("我知道了");
                tv_notice_detail_confirm.setBackgroundResource(R.drawable.gray_border_register);
                tv_notice_detail_confirm.setClickable(false);
            }
        }

        if(2 == requestCode){
            MessageUtils.showShortToast(this, "签收成功");
            tv_notice_detail_confirm.setText("我知道了");
            tv_notice_detail_confirm.setBackgroundResource(R.drawable.gray_border_register);
            tv_notice_detail_confirm.setClickable(false);
            Intent intent=new Intent();
            setResult(1001,intent);
            MyApplication.isNotceActivity=true;
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
        return "内部通知";
    }



}
