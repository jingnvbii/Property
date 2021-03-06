package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.SetMemberLevel;
import com.ctrl.forum.customview.MineHeadView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.dao.MemberDao;
import com.ctrl.forum.entity.CompanyInfo;
import com.ctrl.forum.entity.MemberInfo;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 个人详情
 */
public class MineDetailActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.iv_head)
    MineHeadView iv_head;
    @InjectView(R.id.shop_head)
    MineHeadView shop_head;
    @InjectView(R.id.iv_grade)
    TextView iv_grade;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id.shop_name)
    TextView shop_name;
    @InjectView(R.id.shop_detail)
    TextView shop_detail;
    @InjectView(R.id.tv_time)
    TextView tv_time;
    @InjectView(R.id.iv_phone)
    ImageView iv_phone;
    @InjectView(R.id.rl_post)
    RelativeLayout rl_post;
    @InjectView(R.id.rl_comment)
    RelativeLayout rl_comment;
    @InjectView(R.id.tv_blacklist)
    TextView tv_blacklist;
    @InjectView(R.id.tv_juan)
    TextView tv_juan;
    @InjectView(R.id.tv_hui)
    TextView tv_hui;
    @InjectView(R.id.shop_information)
    RelativeLayout shop_information;
    @InjectView(R.id.shop_information_name)
    TextView shop_information_name;

    private String id;
    private MemberDao mdao;
    private MemberInfo memberInfo;
    private CompanyInfo companyInfo;
    private String tel;
    private InvitationDao idao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_detail);
        ButterKnife.inject(this);

        initView();
        initData();
    }

    private void initData() {
        id = getIntent().getStringExtra("id");
        idao = new InvitationDao(this);
        mdao = new MemberDao(this);
        mdao.queryCompanyByMemberId(id);
    }

    private void initView() {
        rl_post.setOnClickListener(this);
        rl_comment.setOnClickListener(this);
        tv_blacklist.setOnClickListener(this);
        iv_phone.setOnClickListener(this);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.detail);}

    @Override
    public void onClick(View v) {
        Intent intent = null;
       switch (v.getId()){
           case R.id.rl_post:
               intent = new Intent(this,MineQueryPostActivity.class);
               intent.putExtra("id",id);
               startActivity(intent);
               break;
           case R.id.rl_comment:
               intent = new Intent(this,MineCommentActivity.class);
               intent.putExtra("id",id);
               startActivity(intent);
               break;
           case R.id.tv_blacklist:
               if (Arad.preferences.getString("memberId").equals(id)){
                   MessageUtils.showShortToast(this,"自己不能对自己拉黑!");
               }else{
                   idao.requeMemberBlackListAdd(Arad.preferences.getString("memberId"),id);
               }
               break;
           case R.id.iv_phone:
               if (tel!=null && !tel.equals("")){
                   Intent intent1 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+tel));
                   intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent1);
               }else{MessageUtils.showShortToast(this,"该店铺暂无电话!");}
               break;
       }
    }
    //http://115.28.243.3:8008/ctrl-api/companys/queryCompanyByMemberId?memberId=8e64b73b1bc741b5b09610140b0a9710
    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==2){
            memberInfo = mdao.getMemberInfo();
            companyInfo = mdao.getCompanyInfo();
            putValue();
        }
        if (requestCode == 10) {
            MessageUtils.showShortToast(this, "拉黑成功");
        }
    }

    private void putValue() {
        if (memberInfo!=null){
            if (memberInfo.getNickName()==null || memberInfo.getNickName().equals("")){
                String str = memberInfo.getUserName();
                tv_name.setText(str.substring(0,3)+"****"+str.substring(7,11));
            }else{
                tv_name.setText(memberInfo.getNickName());
            }
            SetMemberLevel.setLevelImage(this, iv_grade, memberInfo.getMemberLevel());
            shop_detail.setText(memberInfo.getCompanyKind());
            Arad.imageLoader.load(memberInfo.getImgUrl()).placeholder(getResources().getDrawable(R.mipmap.my_gray)).into(iv_head);
        }
        if (companyInfo!=null){
            shop_information_name.setVisibility(View.VISIBLE);
            shop_information.setVisibility(View.VISIBLE);
            Arad.imageLoader.load(companyInfo.getImg()).placeholder(getResources().getDrawable(R.mipmap.my_gray)).into(shop_head);
            shop_name.setText(companyInfo.getName());
            tv_time.setText(companyInfo.getWorkStartTime()+"--"
                    +companyInfo.getWorkEndTime());
            if(companyInfo.getCouponEnable()!=null) {
                if (companyInfo.getCouponEnable().equals("0")) {
                    tv_juan.setVisibility(View.GONE);
                } else {
                    tv_juan.setVisibility(View.VISIBLE);
                }
            }
            if(companyInfo.getPacketEnable()!=null) {
            if (companyInfo.getPacketEnable().equals("0")){tv_hui.setVisibility(View.GONE);}else {tv_hui.setVisibility(View.VISIBLE);}
            tel = companyInfo.getMobile();}
        }
    }
}
