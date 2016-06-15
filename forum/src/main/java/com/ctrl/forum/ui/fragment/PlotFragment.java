package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.dao.PlotDao;
import com.ctrl.forum.entity.Banner;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.loopview.HomeAutoSwitchPicHolder;
import com.ctrl.forum.ui.activity.mine.MineFindFlotActivity;
import com.ctrl.forum.ui.activity.plot.PlotAddInvitationActivity;
import com.ctrl.forum.ui.activity.plot.PlotRimServeActivity;
import com.ctrl.forum.ui.activity.plot.PlotSearchResultActivity;
import com.ctrl.forum.ui.adapter.PlotListViewFriendStyleAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 小区 fragment
 * Created by jason on 2016/4/7.
 */
public class PlotFragment extends ToolBarFragment implements View.OnClickListener{
    @InjectView(R.id.lv_content)
    PullToRefreshListView lv_content;
    @InjectView(R.id.tv_plot_name)
    TextView tv_plot_name; //小区名
    @InjectView(R.id.rim_post)
    TextView rim_post; //发帖
    @InjectView(R.id.rim_serve)
    TextView rim_serve; //周边服务
    @InjectView(R.id.iv_back)
    ImageView iv_back;
    @InjectView(R.id.rl_search)
    RelativeLayout rl_search;

    private PlotListViewFriendStyleAdapter invitationListViewFriendStyleAdapter;
    private List<Post> posts;
    private String communityId;
    private PlotDao plotDao;
    private int PAGE_NUM =1;
    private String str="";
    private FrameLayout frameLayout;
    private HomeAutoSwitchPicHolder mAutoSwitchPicHolder;

    private ArrayList<String> mData;
    private List<Banner> listBanner;
    private InvitationDao idao;

    public static PlotFragment newInstance() {
        PlotFragment fragment = new PlotFragment();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plot, container, false);
        ButterKnife.inject(this, view);

        checkActivity();

        initView();
        invitationListViewFriendStyleAdapter = new PlotListViewFriendStyleAdapter(getActivity());
        lv_content.setAdapter(invitationListViewFriendStyleAdapter);
        invitationListViewFriendStyleAdapter.setOnLove(this);

        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (posts != null) {
                    posts.clear();
                    PAGE_NUM = 1;
                }
                plotDao.queryCommunityPostList(Arad.preferences.getString("memberId"), communityId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (posts != null) {
                    PAGE_NUM += 1;
                    plotDao.queryCommunityPostList(Arad.preferences.getString("memberId"), communityId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        //listview增加头部布局
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview = getActivity().getLayoutInflater().inflate(R.layout.item_plot_header_view, lv_content, false);
        frameLayout = (FrameLayout) headview.findViewById(R.id.framelayout);
        headview.setLayoutParams(layoutParams);
        lv_content.getRefreshableView().addHeaderView(headview);

        idao = new InvitationDao(this);
        idao.postRotatingBanner("B_COMMUNITY_TOP");

        return view;
    }

    /**
     * 轮播图
     */
    private void setLoopView() {
        // 1.创建轮播的holder
        mAutoSwitchPicHolder = new HomeAutoSwitchPicHolder(getActivity());
        // 2.得到轮播图的视图view
        View autoPlayPicView = mAutoSwitchPicHolder.getRootView();
        // 把轮播图的视图添加到主界面中
        frameLayout.addView(autoPlayPicView);
        //4. 为轮播图设置数据
        mAutoSwitchPicHolder.setData(getData());
        mAutoSwitchPicHolder.setData(listBanner);
    }

    public List<String> getData() {
        mData = new ArrayList<String>();
        for(int i=0;i<listBanner.size();i++){
            mData.add(listBanner.get(i).getImgUrl());
        }
        return mData;
    }

    //检查依附的activity
    private void checkActivity() {
        Bundle bundle = getArguments();
        str = bundle.getString("str");
        if (str.equals("我")){
           iv_back.setImageDrawable(getResources().getDrawable(R.mipmap.white_arrow_left_none));
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

    }

    private void initView() {
        tv_plot_name.setText(Arad.preferences.getString("communityName"));

        tv_plot_name.setOnClickListener(this);
        rim_post.setOnClickListener(this);
        rim_serve.setOnClickListener(this);
        rl_search.setOnClickListener(this);
    }

    private void initData() {
        communityId = Arad.preferences.getString("communityId");
        plotDao = new PlotDao(this);
        plotDao.queryCommunityPostList(Arad.preferences.getString("memberId"), communityId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==6){
            posts = plotDao.getPlotPost();
            if (posts!=null){
                invitationListViewFriendStyleAdapter.setList(posts);
            }
        }
        if (requestCode==19){
           listBanner = idao.getPlotBanner();
            if (listBanner!=null){
               setLoopView();
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        Object id = v.getTag();
        switch (v.getId()){
            case R.id.tv_plot_name:
                startActivity(new Intent(getActivity(),MineFindFlotActivity.class));
                break;
            case R.id.rim_post:
                startActivity(new Intent(getActivity(),PlotAddInvitationActivity.class));
                break;
            case R.id.rim_serve:
                startActivity(new Intent(getActivity(), PlotRimServeActivity.class));
                break;
            case R.id.rl_search:
                Intent intent = new Intent(getActivity(), PlotSearchResultActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_friend_style_zan:
                int position = (int)id;
                if (posts.get(position).getTitle()==null && posts.get(position).getTitle().equals("")){
                    //标题为空时,传内容
                    //idao.requesZambia("add",posts.get(position).getId(),Arad.preferences.getString("memberId"),posts.get(position).getTitle(),"");

                }else {
                    idao.requesZambia("add",posts.get(position).getId(),Arad.preferences.getString("memberId"),posts.get(position).getTitle(),"");
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_plot_name.setText(Arad.preferences.getString("communityName"));
        plotDao.queryCommunityPostList(Arad.preferences.getString("memberId"), communityId, PAGE_NUM + "", Constant.PAGE_SIZE + "");
    }
}
