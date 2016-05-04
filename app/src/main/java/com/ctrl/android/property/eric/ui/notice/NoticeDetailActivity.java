package com.ctrl.android.property.eric.ui.notice;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.property.R;
import com.ctrl.android.property.base.AppHolder;
import com.ctrl.android.property.base.AppToolBarActivity;
import com.ctrl.android.property.base.Constant;
import com.ctrl.android.property.eric.dao.NoticeDao;
import com.ctrl.android.property.eric.entity.Img;
import com.ctrl.android.property.eric.entity.Notice;
import com.ctrl.android.property.eric.ui.widget.ImageZoomActivity;
import com.ctrl.android.property.eric.util.D;
import com.ctrl.android.property.eric.util.S;
import com.ctrl.android.property.eric.util.StrConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 公告列表 activity
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
    @InjectView(R.id.tv_notice_detail_confirm)
    TextView tv_notice_detail_confirm;

    private String TITLE = StrConstant.COMMUNITY_NOTICE_DETAIL;
    private NoticeDao dao;
    private Notice notice;

    private String propertyNoticeId;
    private String activityId;
    private String title = "";

    private List<Img> listImg = new ArrayList<>();
    private ArrayList<String> imagelist;//传入到图片放大类 用
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.notice_detail_activity);
        ButterKnife.inject(this);
        init();
    }

    /**
     * 初始化执行的 方法
     * */
    private void init(){

        tv_notice_detail_confirm.setOnClickListener(this);

        dao = new NoticeDao(this);
        propertyNoticeId = getIntent().getStringExtra("propertNoticeId");
        title = getIntent().getStringExtra("title");
        activityId = AppHolder.getInstance().getProprietor().getProprietorId();
        showProgress(true);
        dao.requestNotice(propertyNoticeId,activityId);
    }

    @Override
    public void onClick(View v) {
        if(v == tv_notice_detail_confirm){
            //MessageUtils.showShortToast(this,"签收");
            showProgress(true);
            dao.requestNoticeModify(propertyNoticeId,AppHolder.getInstance().getProprietor().getProprietorId());
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        if(requestCode == 1){
            notice = dao.getNotice();

            listImg = dao.getListImg();

            tv_notice_detail_title.setText(title);
            //tv_notice_detail_title.setText(notice.getNoticeTitle());
            tv_notice_detail_releaser.setText(notice.getUserName() + "   " + D.getDateStrFromStamp(Constant.YYYY_MM_DD, notice.getCreateTime()));
            tv_notice_detail_content.setText(S.getStr(notice.getContent()));

            if(listImg == null || listImg.size() < 1){
                iv_notice_detail_content.setVisibility(View.GONE);
            } else {
                Arad.imageLoader
                        .load(listImg.get(0).getOriginalImg())
                        .placeholder(R.drawable.default_image)
                        .into(iv_notice_detail_content);
                iv_notice_detail_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagelist = new ArrayList<String>();
                        imagelist.add(listImg.get(0).getOriginalImg());
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
                tv_notice_detail_confirm.setText("签收");
                tv_notice_detail_confirm.setBackgroundResource(R.drawable.green_bg_shap);
                tv_notice_detail_confirm.setClickable(true);
            } else {
                tv_notice_detail_confirm.setText("已签收");
                tv_notice_detail_confirm.setBackgroundResource(R.drawable.gray_bg_shap);
                tv_notice_detail_confirm.setClickable(false);
            }
        }

        if(2 == requestCode){
            MessageUtils.showShortToast(this,"签收成功");
            tv_notice_detail_confirm.setText("已签收");
            tv_notice_detail_confirm.setBackgroundResource(R.drawable.gray_bg_shap);
            tv_notice_detail_confirm.setClickable(false);
        }
    }

    /**
     * header 左侧按钮
     * */
    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.drawable.white_arrow_left_none);
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
        return TITLE;
    }


    /**
     * header 右侧按钮
     * */
//    @Override
//    public boolean setupToolBarRightButton(ImageView rightButton) {
//        rightButton.setImageResource(R.drawable.more_info_icon);
//        rightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //MessageUtils.showShortToast(MallShopMainActivity.this, "MORE");
//                showProStyleListPop();
//            }
//        });
//        return true;
//    }





}
