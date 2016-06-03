package com.ctrl.forum.ui.activity.rim;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.RimDao;
import com.ctrl.forum.entity.CompanyEvaluation;
import com.ctrl.forum.ui.adapter.RimCommentListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周边服务----店铺评论页面
 */
public class RimStoreCommentActivity extends ToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.lv_content)
    PullToRefreshListView lv_content;
    @InjectView(R.id.iv_voice) //发语音按钮
    ImageView iv_voice;
    @InjectView(R.id.iv_add)  //增加按钮
    ImageView iv_add;
    @InjectView(R.id.et_content) //输入要发送的内容
    EditText et_content;
    @InjectView(R.id.tv_huitie)  //发帖按钮
    TextView tv_huitie;
    @InjectView(R.id.rl_add_phone)  //点击增加按钮显示的布局
    RelativeLayout rl_add_phone;
    @InjectView(R.id.jiabiaoqing)  //表情
    ImageView jiabiaoqing;
    @InjectView(R.id.picture)  //照片
    ImageView picture;
    @InjectView(R.id.shoot)  //拍摄
            ImageView shoot;


    private RimDao rimDao;
    private RimCommentListAdapter rimCommentListAdapter;
    private Intent intent;
    private String rimServiceCompaniesId;
    private List<CompanyEvaluation> companyEvaluationList;
    private int PAGE_NUM=1;
    private CompanyEvaluation companyEvaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rim_store_comment);
        ButterKnife.inject(this);

        initData();
        initView();

        //rimCommentListAdapter.setList(list);
        //lv_content.setAdapter(rimCommentListAdapter);
    }

    private void initData() {
        rimDao = new RimDao(this);
        intent = getIntent();
        rimServiceCompaniesId = intent.getStringExtra("rimServiceCompaniesId");
        rimDao.getcollectAroundCompany(rimServiceCompaniesId, PAGE_NUM + "", Constant.PAGE_SIZE + "");

        companyEvaluationList = new ArrayList<>();
        companyEvaluation = new CompanyEvaluation();
        companyEvaluation.setThumbImg("jijijiji");
        companyEvaluationList.add(companyEvaluation);

        companyEvaluation = new CompanyEvaluation();
        companyEvaluation.setSoundUrl("dfdfdfdf");
        companyEvaluationList.add(companyEvaluation);

        companyEvaluation = new CompanyEvaluation();
        companyEvaluation.setContent("dfdfdfdf");
        companyEvaluationList.add(companyEvaluation);

        companyEvaluation = new CompanyEvaluation();
        companyEvaluation.setImg("dfdfdfdf");
        companyEvaluationList.add(companyEvaluation);
    }

    private void initView() {
        iv_voice.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        tv_huitie.setOnClickListener(this);
        jiabiaoqing.setOnClickListener(this);
        picture.setOnClickListener(this);
        shoot.setOnClickListener(this);
        et_content.setOnClickListener(this);
        //默认增加照片,拍摄的布局为隐藏
        rl_add_phone.setVisibility(View.GONE);

        rimCommentListAdapter = new RimCommentListAdapter(this);
        rimCommentListAdapter.setList(companyEvaluationList);
        lv_content.setAdapter(rimCommentListAdapter);

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.store_comment);}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.iv_voice:
             break;
         case R.id.iv_add:
             rl_add_phone.setVisibility(View.VISIBLE);
             //et_content.clearFocus();
             et_content.setFocusable(false);
             et_content.setFocusableInTouchMode(false);
             break;
         case R.id.tv_huitie:
             if (!et_content.getText().toString().equals("")){
                 //会员id,商家id,类型0：文字或者表情、1：图片、2：语音）,内容,语音,原图,缩略图
                 rimDao.evaluateAroundCompany(Arad.preferences.getString("memberId"),rimServiceCompaniesId,"0",et_content.getText().toString(),"","","");
             }else{
                 MessageUtils.showShortToast(this,"评价内容不能为空!");
             }
             break;
         case R.id.jiabiaoqing:

             break;
         case R.id.picture:
             break;
         case R.id.shoot:
             break;
         case R.id.et_content:
             //et_content.requestFocus();
             et_content.setFocusableInTouchMode(true);
             et_content.setFocusable(true);
             et_content.requestFocus();
             rl_add_phone.setVisibility(View.GONE);
             break;
         default:
             break;
     }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==2){

        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
    }
}
