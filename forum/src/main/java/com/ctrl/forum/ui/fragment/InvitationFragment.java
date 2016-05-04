package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.GridViewForScrollView;
import com.ctrl.forum.customview.PullToRefreshListViewForScrollView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.entity.Invitation_listview;
import com.ctrl.forum.entity.Kind;
import com.ctrl.forum.entity.Notice;
import com.ctrl.forum.entity.PostKind;
import com.ctrl.forum.entity.Recommend;
import com.ctrl.forum.ui.activity.Invitation.InvitationPullDownActivity;
import com.ctrl.forum.ui.adapter.InvitationGridViewAdapter;
import com.ctrl.forum.ui.adapter.InvitationListViewAdapter;
import com.ctrl.forum.ui.viewpage.CycleViewPager;
import com.ctrl.forum.ui.viewpage.ViewFactory;
import com.ctrl.forum.utils.DemoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子 fragment
 * Created by jason on 2016/4/7.
 */
public class InvitationFragment extends ToolBarFragment {
    @InjectView(R.id.scrollView)
    HorizontalScrollView scrollView;
    @InjectView(R.id.gridView1)
    GridViewForScrollView gridView1;
    @InjectView(R.id.framelayout)
    FrameLayout framelayout;
    @InjectView(R.id.iv_recommend_1)//推荐列表图片1
            ImageView iv_recommend_1;
    @InjectView(R.id.iv_recommend_2)//推荐列表图片2
            ImageView iv_recommend_2;
    @InjectView(R.id.iv_recommend_3)//推荐列表图片3
            ImageView iv_recommend_3;
    @InjectView(R.id.iv_recommend_4)//推荐列表图片4
            ImageView iv_recommend_4;
    @InjectView(R.id.tv_change)//文字轮播
            TextView tv_change;
    @InjectView(R.id.listview)//下拉列表
            PullToRefreshListViewForScrollView listview;

    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距
    private List<Kind> kindList;
    private View vhdf;
    private CycleViewPager cycleViewPager;
    private InvitationListViewAdapter invitationListViewAdapter;
    private List<Invitation_listview> list;
    private InvitationDao idao;

    private List<Banner> listBanner;
    private List<Notice> listNotice;
    private List<Recommend> listRecommend;
    private List<PostKind> listPostKind;


    /*
    * 实现文字上下轮播
    * */
    private boolean isloop = true;
    private List<String> listNoticeString = new ArrayList<>();
    private int item = 0;
    private AnimationSet set = new AnimationSet(true);
    private AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
    private TranslateAnimation ta = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
            0, Animation.RELATIVE_TO_SELF, 2.0f,
            Animation.RELATIVE_TO_SELF, 0);

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:

                    if (isloop) {
                        tv_change.setText(listNoticeString.get(item % listNoticeString.size()));
                        tv_change.setAnimation(set);
                        tv_change.startAnimation(set);
                        item += 1;
                    }
                    break;
            }
        }

        ;
    };


    public static InvitationFragment newInstance() {
        InvitationFragment fragment = new InvitationFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation, container, false);
        ButterKnife.inject(this, view);
        scrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
        initData();
        getScreenDen();
        //调用轮播图
        setLoopView();
        //公告轮播控件初始化
        initView();

        invitationListViewAdapter = new InvitationListViewAdapter(getActivity());
        invitationListViewAdapter.setList(list);
        listview.setAdapter(invitationListViewAdapter);
        return view;
    }

    private void initView() {
        tv_change.setText("");
        set.addAnimation(animation);
        set.addAnimation(ta);
        set.setDuration(1000);
        set.setRepeatMode(Animation.REVERSE);
    }

    private void setLoopView() {
        // 三句话 调用轮播广告
        vhdf = getActivity().getLayoutInflater().inflate(R.layout.viewpage, null);
        cycleViewPager = (CycleViewPager) getActivity().getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
        ViewFactory.initialize(getActivity(), vhdf, cycleViewPager, DemoUtil.cycData());
        framelayout.addView(vhdf);

    }

    private void initData() {
        idao = new InvitationDao(this);
        idao.requestInitPostHomePage();
        idao.requestPostListByCategory("0",Arad.preferences.getString("memberId"), "", Constant.PAGE_NUM, Constant.PAGE_SIZE);
        list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Invitation_listview invitation = new Invitation_listview();
            invitation.setName("汪峰" + i + "便利店");
            list.add(invitation);
        }


       /* kindList=new ArrayList<>();
        for(int i=0;i<6;i++){
            Kind kind=new Kind();
            kind.setKindName("频道:" + i);
            kindList.add(kind);
        }*/
    }


    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 0) {
            listBanner = idao.getListBanner();
            listPostKind = idao.getListPostKind();
            listNotice = idao.getListNotice();
            listRecommend = idao.getListRecommend();
            MessageUtils.showShortToast(getActivity(), "初始化成功");
            setValue();
            initRecommend();//推荐列表初始化
            initNotice();//公告栏数据初始化
        }

        if (requestCode == 1) {
            MessageUtils.showShortToast(getActivity(), "获取帖子列表成功");
        }


    }

    private void initNotice() {

        for (int i = 0; i < listNotice.size(); i++) {
            listNoticeString.add(listNotice.get(i).getContent());
        }
        //实现自动切换界面
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isloop) {
                    //系统时钟的睡眠方法---->电量的消耗很少。
                    SystemClock.sleep(4000);
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();

    }

    private void initRecommend() {
        if (listRecommend.size() > 0) {
            Arad.imageLoader.load(listRecommend.get(0).getImgUrl()).placeholder(R.mipmap.fuzhuang).into(iv_recommend_1);
        }
        if (listRecommend.size() > 1) {
            Arad.imageLoader.load(listRecommend.get(1).getImgUrl()).placeholder(R.mipmap.fuzhuang).into(iv_recommend_2);
        }
        if (listRecommend.size() > 2) {
            Arad.imageLoader.load(listRecommend.get(2).getImgUrl()).placeholder(R.mipmap.fuzhuang).into(iv_recommend_3);
        }
        if (listRecommend.size() > 3) {
            Arad.imageLoader.load(listRecommend.get(3).getImgUrl()).placeholder(R.mipmap.fuzhuang).into(iv_recommend_4);
        }
    }

    private void setValue() {
        InvitationGridViewAdapter adapter = new InvitationGridViewAdapter(getActivity());
        adapter.setList(listPostKind);
        int count = adapter.getCount();
        int columns = (count % 2 == 0) ? count / 2 : count / 2 + 1;
        gridView1.setAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columns * dm.widthPixels / NUM,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        gridView1.setLayoutParams(params);
        gridView1.setColumnWidth(dm.widthPixels / NUM);
        gridView1.setHorizontalSpacing(hSpacing);
        gridView1.setStretchMode(GridView.NO_STRETCH);

        if (count <= 3) {
            gridView1.setNumColumns(count);
        } else {
            gridView1.setNumColumns(columns);
        }

        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InvitationPullDownActivity.class);
                intent.putExtra("channelId",listPostKind.get(position).getId());
                getActivity().startActivity(intent);
            }
        });

    }


    private void getScreenDen() {
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
