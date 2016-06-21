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
import com.ctrl.forum.utils.DateUtil;

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
    ImageView iv_grade;
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
               idao.requeMemberBlackListAdd(Arad.preferences.getString("memberId"),id);
               break;
           case R.id.iv_phone:
               if (tel!=null && !tel.equals("")){
                   Intent intent1 = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+tel));
                   intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent1);
               }
               break;
       }
    }

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
            tv_name.setText(memberInfo.getNickName());
            SetMemberLevel.setLevelImage(this, iv_grade, memberInfo.getMemberLevel());
            Arad.imageLoader.load(memberInfo.getImgUrl()).placeholder(getResources().getDrawable(R.mipmap.iconfont_head)).into(iv_head);
        }
        if (companyInfo!=null){
            Arad.imageLoader.load(companyInfo.getImg()).placeholder(getResources().getDrawable(R.mipmap.iconfont_head)).into(shop_head);
            shop_name.setText(companyInfo.getCompanyName());
            shop_detail.setText(companyInfo.getCompanyKind());
            tv_time.setText(DateUtil.getStringByFormat(companyInfo.getCompanyStartTime(),"hh:mm")+"--"
                    +DateUtil.getStringByFormat(companyInfo.getCompanyEndTime(),"hh:mm"));
            if (companyInfo.getCouponEnable().equals("0")){tv_juan.setVisibility(View.GONE);}else {tv_juan.setVisibility(View.VISIBLE);}
            if (companyInfo.getPacketEnable().equals("0")){tv_hui.setVisibility(View.GONE);}else {tv_hui.setVisibility(View.VISIBLE);}
            tel = companyInfo.getMobile();
        }
    }
}
