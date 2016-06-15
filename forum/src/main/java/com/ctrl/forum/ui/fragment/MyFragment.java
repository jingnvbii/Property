package com.ctrl.forum.ui.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.SetMemberLevel;
import com.ctrl.forum.customview.MineHeadView;
import com.ctrl.forum.customview.NoScrollGridView;
import com.ctrl.forum.customview.NumView;
import com.ctrl.forum.dao.EditDao;
import com.ctrl.forum.dao.MemberDao;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.entity.Plugin;
import com.ctrl.forum.ui.activity.mine.MineAssessActivity;
import com.ctrl.forum.ui.activity.mine.MineBlacklistActivity;
import com.ctrl.forum.ui.activity.mine.MineCollectActivity;
import com.ctrl.forum.ui.activity.mine.MineCommentActivity;
import com.ctrl.forum.ui.activity.mine.MineCreateShopOneActivity;
import com.ctrl.forum.ui.activity.mine.MineCreateShopThreeActivity;
import com.ctrl.forum.ui.activity.mine.MineDraftActivity;
import com.ctrl.forum.ui.activity.mine.MineEditActivity;
import com.ctrl.forum.ui.activity.mine.MineGradeActivity;
import com.ctrl.forum.ui.activity.mine.MineIntegralActivity;
import com.ctrl.forum.ui.activity.mine.MineJuanActivity;
import com.ctrl.forum.ui.activity.mine.MineMessageActivity;
import com.ctrl.forum.ui.activity.mine.MineOrderActivity;
import com.ctrl.forum.ui.activity.mine.MineOrderManageActivity;
import com.ctrl.forum.ui.activity.mine.MinePlotActivity;
import com.ctrl.forum.ui.activity.mine.MineQueryPostActivity;
import com.ctrl.forum.ui.activity.mine.MineSettingActivity;
import com.ctrl.forum.ui.activity.store.StoreManageAddressActivity;
import com.ctrl.forum.ui.adapter.MineMemberGridAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我  fragment
 * Created by jaosn on 2016/4/7.
 */
public class MyFragment extends ToolBarFragment implements View.OnClickListener{
    @InjectView(R.id.my_comment)
    RelativeLayout my_comment; //我的评论
    @InjectView(R.id.my_collect)
    RelativeLayout my_collect; //我的收藏
    @InjectView(R.id.my_post)
    RelativeLayout my_post; //我的发帖
    @InjectView(R.id.my_juan)
    RelativeLayout my_juan; //我的劵
    @InjectView(R.id.my_order)
    RelativeLayout my_order; //我的订单
    @InjectView(R.id.my_address)
    RelativeLayout my_address; //收货地址
    @InjectView(R.id.my_pingjia)
    RelativeLayout my_pingjia; //我的评价
    @InjectView(R.id.my_xiaoqu)
    RelativeLayout my_xiaoqu;//我的小区
    @InjectView(R.id.my_shop)
    RelativeLayout my_shop; //我的店铺
    @InjectView(R.id.my_drafts)
    RelativeLayout my_drafts; //草稿箱
    @InjectView(R.id.my_blacklist)
    RelativeLayout my_blacklist; //黑名单
    @InjectView(R.id.iv_bianji)
    ImageView iv_bianji;
    @InjectView(R.id.iv_set)
    ImageView iv_set; //设置
    @InjectView(R.id.iv_message)
    NumView iv_message; //消息
    @InjectView(R.id.iv_grade)
    ImageView iv_grade; //等级
    @InjectView(R.id.bt_integral)
    Button bt_integral; //积分
    @InjectView(R.id.tv_nickName)
    TextView tv_nickName; //昵称
    @InjectView(R.id.iv_head)
    MineHeadView iv_head; //头像
    @InjectView(R.id.bt_sign)
    Button bt_sign;
    @InjectView(R.id.num_juan)
    NumView num_juan;
    @InjectView(R.id.vp_plug)
    ViewPager vp_plug;
    @InjectView(R.id.ll_icons)
    LinearLayout ll_icons;

    private MemberDao mdao;
    private EditDao editDao;
    private MemberInfo memberInfo;//会员基本信息
    private Dialog alertDialog;
    private List<Plugin> pluginList; //用户插件
    private MyPagerAdapter pagerAdapter;
    private List<Plugin> plugins = new ArrayList<>();
    private int len;  //viewPager的页数
    private NoScrollGridView gridView;
    private MineMemberGridAdapter gridListAdapter;

    private View[] views;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void initData(){
        mdao = new MemberDao(this);
        editDao = new EditDao(this);

        String id = Arad.preferences.getString("memberId");
        editDao.getVipInfo(id);
        editDao.getPlugins();

        bt_integral.setText("积分:" + Arad.preferences.getString("point"));
        tv_nickName.setText("昵称:" + Arad.preferences.getString("nickName"));
        String grad = Arad.preferences.getString("memberLevel"); //等级
        String imgUrl = Arad.preferences.getString("imgUrl");

        if (imgUrl!=null&&!imgUrl.equals(""))
        Arad.imageLoader.load(imgUrl).placeholder(getResources().getDrawable(R.mipmap.iconfont_head)).into(iv_head);//设置头像
        SetMemberLevel.setLevelImage(getActivity(), iv_grade, grad);//设置等级

        alertDialog = new AlertDialog.Builder(getActivity()).
                setTitle("审核未通过请重新申请!").
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), MineCreateShopOneActivity.class));
                        alertDialog.dismiss();
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                }).
                create();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.inject(this, view);
        initData();
        init();

        return view;
    }
    //注册点击监听事件
    private void init() {
      my_comment.setOnClickListener(this);
        my_collect.setOnClickListener(this);
        my_post.setOnClickListener(this);
        my_juan.setOnClickListener(this);
        my_order.setOnClickListener(this);
        my_address.setOnClickListener(this);
        my_pingjia.setOnClickListener(this);
        my_xiaoqu.setOnClickListener(this);
        my_shop.setOnClickListener(this);
        my_drafts.setOnClickListener(this);
        my_blacklist.setOnClickListener(this);
        iv_bianji.setOnClickListener(this);
        iv_set.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        iv_grade.setOnClickListener(this);
        bt_integral.setOnClickListener(this);
        bt_sign.setOnClickListener(this);
    }

    //用户插件
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setPlugins(){
        pagerAdapter = new MyPagerAdapter();
        gridListAdapter = new MineMemberGridAdapter(getActivity());

        final int len=plugins.size()%10==0?plugins.size()/10:plugins.size()/10+1;
        this.views=new View[len];
        for(int i=0;i<len;i++){
            NoScrollGridView gridView = new NoScrollGridView(getActivity());
            gridView.setNumColumns(5);
            gridView.setAdapter(gridListAdapter);
            gridListAdapter.setOnImage(this);
            this.views[i]=gridView;
        }

        //设置小圆点
        if (len>1) {
            for (int i = 0; i < len; i++) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setBackground(getResources().getDrawable(R.drawable.red_round_select));
                imageView.setEnabled(false);
                ll_icons.addView(imageView);
                ll_icons.getChildAt(0).setEnabled(true);//默认第一个为选中的情况
            }
        }

        this.pagerAdapter=new MyPagerAdapter();
        this.vp_plug.setAdapter(pagerAdapter);
        vp_plug.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < len; i++) {
                    ll_icons.getChildAt(i).setEnabled(false);
                }
                ll_icons.getChildAt(position).setEnabled(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onClick(View view) {
        //我的评论
        if (view == my_comment) {startActivity(new Intent(getActivity(), MineCommentActivity.class));}
        //编辑我的资料
        if (view == iv_bianji) {startActivity(new Intent(getActivity(), MineEditActivity.class));}
        //设置
        if (view == iv_set) {startActivity(new Intent(getActivity(), MineSettingActivity.class));}
        //消息
        if (view == iv_message) {startActivity(new Intent(getActivity(), MineMessageActivity.class));}
        //抢红包
        //if (view==iv_message){intent = new Intent(getActivity(), RedPacketActivity.class);startActivity(intent);}
        //我的店铺
        if (view == my_shop) {
            String state  = Arad.preferences.getString("companyState");
            switch (state){
                case "3": //没有店铺
                    startActivity(new Intent(getActivity(), MineCreateShopOneActivity.class));
                    break;
                case "0": //待审核
                    startActivity(new Intent(getActivity(), MineCreateShopThreeActivity.class));
                    break;
                case "1": //已通过
                    startActivity(new Intent(getActivity(), MineOrderManageActivity.class));
                    break;
                case "2": //未通过
                    alertDialog.show();
                    break;
            }
        }
        //黑名单
        if (view == my_blacklist) {startActivity(new Intent(getActivity(), MineBlacklistActivity.class));}
        //等级
        if (view == iv_grade) {startActivity(new Intent(getActivity(), MineGradeActivity.class));}
        //我的订单
        if (view == my_order) {startActivity(new Intent(getActivity(), MineOrderActivity.class));}
        //我的劵
        if (view == my_juan) {startActivity(new Intent(getActivity(), MineJuanActivity.class));}
        //积分商城
        if (view == bt_integral) {startActivity(new Intent(getActivity(), MineIntegralActivity.class));}
        //我的评价
        if (view == my_pingjia) {startActivity(new Intent(getActivity(), MineAssessActivity.class));}
        //我的收藏
        if (view == my_collect) {startActivity(new Intent(getActivity(), MineCollectActivity.class));}
        //我的小区
        if (view == my_xiaoqu) {startActivity(new Intent(getActivity(), MinePlotActivity.class));}
        //签到
        if (view == bt_sign){mdao.sign(Arad.preferences.getString("memberId"));}
        //我的发帖
        if (view == my_post){startActivity(new Intent(getActivity(), MineQueryPostActivity.class));}
        //我的草稿箱
        if (view==my_drafts){startActivity(new Intent(getActivity(), MineDraftActivity.class));}
        //收货地址
        if (view==my_address){startActivity(new Intent(getActivity(), StoreManageAddressActivity.class));
        }

        Object uriId = view.getTag();
        switch(view.getId()){
            case R.id.iv_grid_item:
                int position = (int)uriId;
                String uri = plugins.get(position).getLinkUrl();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);//没有指明调用哪个浏览器
                //intent.setData(Uri.parse("http://" + uri));   //网址不全
                intent.setData(Uri.parse(uri));   //网址不对
                //intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        bt_integral.setText("积分:" + Arad.preferences.getString("point"));
        tv_nickName.setText("昵称:" + Arad.preferences.getString("nickName"));

        editDao.getVipInfo(Arad.preferences.getString("memberId"));
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==0){
            MessageUtils.showShortToast(getActivity(), "签到成功!连续签到"+Arad.preferences.getString("signTimes")+"天");
            editDao.getVipInfo(Arad.preferences.getString("memberId"));
            bt_sign.setText("已签到");
        }
        if (requestCode==1) {
            memberInfo = editDao.getMemberInfo();
            Arad.preferences.putString("nickName", memberInfo.getNickName());//昵称
            Arad.preferences.putString("mobile", memberInfo.getMobile()); //手机号
            Arad.preferences.putString("point", memberInfo.getPoint()); //积分
            Arad.preferences.putString("remark", memberInfo.getRemark());//简介
            Arad.preferences.putString("memberLevel", memberInfo.getMemberLevel());//等级
            Arad.preferences.putString("imgUrl", memberInfo.getImgUrl()); //头像
            Arad.preferences.putString("companyId", memberInfo.getCompanyId());//店铺id
            Arad.preferences.putString("companyState", memberInfo.getCompanyState());//是否有店铺3:没有, 0：待审核、1：已通过 2:未通过
            Arad.preferences.putString("isShielded", memberInfo.getIsShielded());//是否被屏蔽（0：否、1：是）
            Arad.preferences.putString("couponsNum", memberInfo.getCouponsNum()); //现金券数量
            Arad.preferences.putString("redenvelopeNum", memberInfo.getRedenvelopeNum());//优惠券数量
            Arad.preferences.putString("signTimes", memberInfo.getSignTimes());//连续签到次数
            Arad.preferences.putString("messageCount", memberInfo.getMessageCount());//通知消息数量
            Arad.preferences.putString("signState", memberInfo.getSignState());//是否签到; 0:没签到   1:已签到

            Arad.preferences.flush();
            if (memberInfo.getSignState().equals("0")) {
                bt_sign.setText("签到");
            } else {
                bt_sign.setText("已签到");
            }

            if (memberInfo.getMessageCount() != null) {
                iv_message.setShowNumMode(2);
                int num = Integer.parseInt(memberInfo.getMessageCount());
                iv_message.setNum(num);
            }

            int redenvelopeNum = 0;
            int couponsNum = 0;
            if (memberInfo.getRedenvelopeNum() != null) {
                redenvelopeNum = Integer.parseInt(memberInfo.getRedenvelopeNum());
            }
            if (memberInfo.getCouponsNum() != null) {
                couponsNum = Integer.parseInt(memberInfo.getCouponsNum());
            }
            num_juan.setShowNumMode(1);
            num_juan.setNum(redenvelopeNum + couponsNum);

            bt_integral.setText("积分:" + Arad.preferences.getString("point"));
        }
        if(requestCode==6){
            pluginList = editDao.getPluginList();
            if (pluginList!=null){
                //往gridView里添值。10个为一页
                // %取模  \取整
                this.plugins = pluginList;
                setPlugins();
                gridListAdapter.setData(pluginList);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        if (errorNo.equals("022")){
            MessageUtils.showShortToast(getActivity(), "已签到,无需重复签到!");
        }
    }

    //viewPager的适配器
    private final class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return views.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view=views[position];
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view=views[position];
            container.removeView(view);
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            // TODO Auto-generated method stub
            return view==obj;
        }

    }
}
