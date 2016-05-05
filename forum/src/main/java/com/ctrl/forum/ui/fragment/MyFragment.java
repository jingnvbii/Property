package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Member;
import com.ctrl.forum.ui.activity.mine.MineAssessActivity;
import com.ctrl.forum.ui.activity.mine.MineBlacklistActivity;
import com.ctrl.forum.ui.activity.mine.MineCollectActivity;
import com.ctrl.forum.ui.activity.mine.MineCommentActivity;
import com.ctrl.forum.ui.activity.mine.MineCreateShopOneActivity;
import com.ctrl.forum.ui.activity.mine.MineEditActivity;
import com.ctrl.forum.ui.activity.mine.MineGradeActivity;
import com.ctrl.forum.ui.activity.mine.MineIntegralActivity;
import com.ctrl.forum.ui.activity.mine.MineJuanActivity;
import com.ctrl.forum.ui.activity.mine.MineMessageActivity;
import com.ctrl.forum.ui.activity.mine.MineOrderActivity;
import com.ctrl.forum.ui.activity.mine.MineSettingActivity;
import com.ctrl.forum.ui.activity.mine.MineShopManageActivity;
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
    @InjectView(R.id.membergridView)
    GridView memberGridView;
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
    ImageView iv_message; //消息
    @InjectView(R.id.iv_grade)
    ImageView iv_grade; //等级
    @InjectView(R.id.bt_integral)
    Button bt_integral; //积分商城

    private List<Member> datas;
    private Boolean isShop = false;//判断我是否已经有店铺,有了则显示,没有则创建

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initData(){
        datas = new ArrayList<>();
        datas.add(new Member("美食"));
        datas.add(new Member("电影"));
        datas.add(new Member("酒店"));
        datas.add(new Member("休闲娱乐"));
        datas.add(new Member("优惠劵"));
        datas.add(new Member("拼车一族"));
        datas.add(new Member("家居广场"));
        datas.add(new Member("同城有约"));
        datas.add(new Member("二手市场"));
        datas.add(new Member("宠物吧"));
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
        memberGridView.setAdapter(new MineMemberGridAdapter(datas, getActivity()));
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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onClick(View view){
        Intent intent;
        //我的评论
        if (view == my_comment){intent = new Intent(getActivity(), MineCommentActivity.class);startActivity(intent);}
        //编辑我的资料
        if (view == iv_bianji){intent = new Intent(getActivity(), MineEditActivity.class);startActivity(intent);}
        //设置
        if (view == iv_set){intent = new Intent(getActivity(), MineSettingActivity.class);startActivity(intent);}
        //消息
        if (view==iv_message){intent = new Intent(getActivity(), MineMessageActivity.class);startActivity(intent);}
        //抢红包
        //if (view==iv_message){intent = new Intent(getActivity(), RedPacketActivity.class);startActivity(intent);}
        //我的店铺
        if (view==my_shop){
            //已经有店铺,跳转到显示店铺界面
            if (isShop){intent = new Intent(getActivity(), MineShopManageActivity.class);startActivity(intent);}
            //没有店铺,创建店铺,默认为false
            else{intent = new Intent(getActivity(), MineCreateShopOneActivity.class);startActivity(intent);}
        }
        //黑名单
        if (view==my_blacklist){intent = new Intent(getActivity(), MineBlacklistActivity.class);startActivity(intent);}
        //等级
        if (view==iv_grade){intent = new Intent(getActivity(), MineGradeActivity.class);startActivity(intent);}
        //我的订单
        if (view==my_order){intent = new Intent(getActivity(), MineOrderActivity.class);startActivity(intent);}
        //我的劵
        if (view==my_juan){intent = new Intent(getActivity(), MineJuanActivity.class);startActivity(intent);}
        //积分商城
        if (view==bt_integral){intent = new Intent(getActivity(), MineIntegralActivity.class);startActivity(intent);}
        //我的评价
        if (view==my_pingjia){intent = new Intent(getActivity(), MineAssessActivity.class);startActivity(intent);}
        //我的收藏
        if (view==my_collect){startActivity(new Intent(getActivity(), MineCollectActivity.class));}
    }
}
