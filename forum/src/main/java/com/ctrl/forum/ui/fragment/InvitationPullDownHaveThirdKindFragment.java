package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Category;
import com.ctrl.forum.entity.Category2;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.entity.ThirdKind;
import com.ctrl.forum.loopview.HomeAutoSwitchPicHolder;
import com.ctrl.forum.ui.activity.Invitation.InvitationDetailFromPlatformActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinterestDetailActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPullDownActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationSearchActivity;
import com.ctrl.forum.ui.adapter.InvitationListViewAdapter;
import com.ctrl.forum.ui.adapter.InvitationListViewBlockStyleAdapter;
import com.ctrl.forum.ui.adapter.InvitationListViewFriendStyleAdapter;
import com.ctrl.forum.ui.adapter.InvitationPullDownGridViewAdapter;
import com.ctrl.forum.ui.adapter.testAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子下拉列表有三级分类 fragment
 * Created by Administrator on 2015/11/30.
 */
public class InvitationPullDownHaveThirdKindFragment extends ToolBarFragment {
    @InjectView(R.id.lv_invitation_pull_down_have_third_kind)
    PullToRefreshListView lv_invitation_pull_down_have_third_kind;
  /*  @InjectView(R.id.framelayout)
    FrameLayout framelayout;*/


    private List<Merchant> list;
    private List<ThirdKind> kindList;
    private GridView gridView1;

    private int width;
    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距
    private testAdapter mAdapter;
    private List<Category2> mCategory2List;
    private InvitationListViewAdapter invitationListViewAdapter;
    private InvitationDao idao;
    private String channelId;
    private int PAGE_NUM = 1;
    private List<Post> listPost;
    private List<Category> listCategroy3;
    private FrameLayout framelayout;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout ll;
    private View headview;
    private int bol = 1;
    private boolean  isFirst=true;
    private String styleType;
    private InvitationListViewBlockStyleAdapter mInvitationListViewBlockStyleAdapter;
    private InvitationListViewFriendStyleAdapter mInvitationListViewFriendStyleAdapter;
    private InvitationPullDownGridViewAdapter gridViewAdapter;
    private String thirdKindId = null;
    private List<Post> listPost2;
    private int Position;
    private HomeAutoSwitchPicHolder mAutoSwitchPicHolder;
    private ArrayList<String> mData;
    private TextView tv_search;
    private int newPosition;


    public static InvitationPullDownHaveThirdKindFragment newInstance(String channelId,String styleType,String thirdKindId) {
        InvitationPullDownHaveThirdKindFragment fragment = new InvitationPullDownHaveThirdKindFragment();
        fragment.channelId = channelId;
        fragment.styleType = styleType;
        fragment.thirdKindId = thirdKindId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idao = new InvitationDao(this);
        invitationListViewAdapter = new InvitationListViewAdapter(getActivity());
        mInvitationListViewBlockStyleAdapter = new InvitationListViewBlockStyleAdapter(getActivity());
        mInvitationListViewFriendStyleAdapter = new InvitationListViewFriendStyleAdapter(getActivity());
        width = getResources().getDisplayMetrics().widthPixels;
        gridViewAdapter = new InvitationPullDownGridViewAdapter(getActivity());


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!InvitationPullDownActivity.isFromSelcet){
            thirdKindId=null;
            Log.i("tag","dsfsdfsdf11");
        }
        if (isVisibleToUser&&bol==1 ) {
            showProgress(true);
            if(listCategroy3!=null)listCategroy3.clear();
            if(thirdKindId==null) {
               idao.requesPostCategory(channelId, "2", "0");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", "", PAGE_NUM, Constant.PAGE_SIZE);
            }else {
                idao.requesPostCategory(channelId, "2", "0");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", "", PAGE_NUM, Constant.PAGE_SIZE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation_pull_down_have_third_kind, container, false);
        ButterKnife.inject(this, view);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        headview = getActivity().getLayoutInflater().inflate(R.layout.fragment_invitation_header, lv_invitation_pull_down_have_third_kind, false);
        headview.setLayoutParams(layoutParams);
       final ListView lv = lv_invitation_pull_down_have_third_kind.getRefreshableView();
        initData();
        getScreenDen();
        lv.addHeaderView(headview);
        lv.setFocusable(false);

        lv_invitation_pull_down_have_third_kind.setMode(PullToRefreshBase.Mode.BOTH);
        lv_invitation_pull_down_have_third_kind.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                listPost.clear();
                PAGE_NUM = 1;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (listCategroy3 != null) {
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(Position).getId(), "0", "", PAGE_NUM, Constant.PAGE_SIZE);
                        } else {
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", "", PAGE_NUM, Constant.PAGE_SIZE);

                        }
                    }

                }, 500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM += 1;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (listCategroy3 != null) {
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(Position).getId(), "0", "", PAGE_NUM, Constant.PAGE_SIZE);
                        } else {
                            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), channelId, "0", "", PAGE_NUM, Constant.PAGE_SIZE);

                        }
                    }
                }, 500);

            }
        });
        lv_invitation_pull_down_have_third_kind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newPosition=position-lv.getHeaderViewsCount();
                Intent intent=null;
                String type = listPost.get(newPosition).getSourceType();
                switch (type) {
                    case "0"://平台
                        intent = new Intent(getActivity(), InvitationDetailFromPlatformActivity.class);
                        intent.putExtra("id", listPost.get(newPosition).getId());
                        startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());

                        break;
                    case "1"://app
                        intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                        intent.putExtra("id", listPost.get(newPosition).getId());
                        startActivity(intent);
                        AnimUtil.intentSlidIn(getActivity());
                        break;
                }

            }
        });



        return view;
    }

    private void initData() {
        //  horizontalScrollView = (HorizontalScrollView) headview.findViewById(R.id.scrollView);
        ll = (LinearLayout) headview.findViewById(R.id.ll);
        gridView1 = (GridView) headview.findViewById(R.id.gridView_pull_down);
        framelayout = (FrameLayout) headview.findViewById(R.id.framelayout);
        tv_search = (TextView) headview.findViewById(R.id.tv_search);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bol = 1;
                Position = position;
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(position).getId(), "0", "", PAGE_NUM, Constant.PAGE_SIZE);
            }
        });





    }

    private void setValue() {
        InvitationPullDownGridViewAdapter adapter = new InvitationPullDownGridViewAdapter(getActivity());
        adapter.setList(listCategroy3);
        int count = adapter.getCount();
        int columns = (count % 2 == 0) ? count / 2 : count / 2 + 1;
        gridView1.setAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columns * dm.widthPixels / NUM,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        gridView1.setLayoutParams(params);
        gridView1.setColumnWidth(dm.widthPixels / NUM);
        // gridView.setHorizontalSpacing(hSpacing);
        gridView1.setStretchMode(GridView.NO_STRETCH);
        if (count <= 3) {
            gridView1.setNumColumns(count);
        } else {
            gridView1.setNumColumns(columns);
        }
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bol = 1;
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(position).getId(), "0", "", PAGE_NUM, Constant.PAGE_SIZE);
            }
        });


    }




    @Override
    public void onResume() {
        super.onResume();
    }

    private void getScreenDen() {
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_invitation_pull_down_have_third_kind.onRefreshComplete();
        showProgress(false);
        if (requestCode == 1) {
            bol = 0;
            if(listCategroy3==null) {
                framelayout.setVisibility(View.VISIBLE);
                //设置轮播图
                setLoopView();
                ll.setVisibility(View.GONE);
                // horizontalScrollView.setVisibility(View.VISIBLE);
                gridView1.setVisibility(View.GONE);
            }else {
                framelayout.setVisibility(View.GONE);
                ll.setVisibility(View.VISIBLE);
                // horizontalScrollView.setVisibility(View.VISIBLE);
                gridView1.setVisibility(View.VISIBLE);
            }
            listPost = idao.getListPost();
            switch (styleType) {
                case "1":
                    invitationListViewAdapter.setList(listPost);
                    break;
                case "2":
                    mInvitationListViewBlockStyleAdapter.setList(listPost);
                    break;
                case "4":
                    mInvitationListViewFriendStyleAdapter.setList(listPost);
                    break;
            }
            thirdKindId=null;
            InvitationPullDownActivity.isFromSelcet=false;

        }
        if (requestCode == 2) {
            bol = 0;
            framelayout.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
            // horizontalScrollView.setVisibility(View.VISIBLE);
            gridView1.setVisibility(View.VISIBLE);

            listCategroy3 = idao.getListCategory();
            gridViewAdapter.setList(listCategroy3);
            if(isFirst) {
              //  idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(0).getId(), "0", "", PAGE_NUM, Constant.PAGE_SIZE);
            }
            isFirst=false;
            tv_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), InvitationSearchActivity.class);
                    startActivityForResult(intent, 1111);
                    AnimUtil.intentSlidIn(getActivity());
                }
            });
            thirdKindId=null;
            //  setValue();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1111&&resultCode==2222){
            String keyword = data.getStringExtra("keyword");
            idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(Position).getId(), "0", keyword, PAGE_NUM, Constant.PAGE_SIZE);
        }
    }

    /*
    * 轮播图
    * */
    private void setLoopView() {
        // 1.创建轮播的holder
        mAutoSwitchPicHolder = new HomeAutoSwitchPicHolder(getActivity());
        // 2.得到轮播图的视图view
        View autoPlayPicView = mAutoSwitchPicHolder.getRootView();
        // 把轮播图的视图添加到主界面中
        framelayout.addView(autoPlayPicView);
        //4. 为轮播图设置数据
        mAutoSwitchPicHolder.setData(getData());
    }

    public List<String> getData() {
        mData = new ArrayList<String>();
        mData.add("http://pic.qqmail.com/imagecache/20101016/1287208885.png");
        mData.add("http://v1.qzone.cc/pic/201308/01/16/44/51fa1fd3d9f0d545.jpg!600x600.jpg");
        mData.add("http://img.blog.cctv.com/attachments/2009/02/810583_200902231053501.jpg");
        return mData;
    }


    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_invitation_pull_down_have_third_kind.onRefreshComplete();
        showProgress(false);
        if(errorNo.equals("006")){
            if(listPost2!=null)
            listPost2.clear();
            switch (styleType) {
                case "1":
                    invitationListViewAdapter.setList(listPost2);
                    break;
                case "2":
                    mInvitationListViewBlockStyleAdapter.setList(listPost2);
                    break;
                case "4":
                    mInvitationListViewFriendStyleAdapter.setList(listPost2);
                    break;
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView1.setAdapter(gridViewAdapter);
        if(styleType!=null) {
            switch (styleType) {
                case "1":
                    lv_invitation_pull_down_have_third_kind.setAdapter(invitationListViewAdapter);
                    break;
                case "2":
                    lv_invitation_pull_down_have_third_kind.setAdapter(mInvitationListViewBlockStyleAdapter);
                    break;
                case "4":
                    lv_invitation_pull_down_have_third_kind.setAdapter(mInvitationListViewFriendStyleAdapter);
                    break;
            }
        }


    }



}
