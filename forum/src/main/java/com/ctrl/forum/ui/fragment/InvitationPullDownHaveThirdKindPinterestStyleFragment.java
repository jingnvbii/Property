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
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.customview.PLA_AdapterView;
import com.ctrl.forum.customview.XListView;
import com.ctrl.forum.dao.InvitationDao;
import com.ctrl.forum.entity.Category;
import com.ctrl.forum.entity.Post;
import com.ctrl.forum.loopview.HomeAutoSwitchPicHolder;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinerestGalleyActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationPullDownActivity;
import com.ctrl.forum.ui.activity.Invitation.InvitationSearchActivity;
import com.ctrl.forum.ui.adapter.InvitationListViewPinterestStyleAdapter;
import com.ctrl.forum.ui.adapter.InvitationPullDownGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子下拉列表有三级分类 fragment
 * Created by Administrator on 2015/11/30.
 */
public class InvitationPullDownHaveThirdKindPinterestStyleFragment extends ToolBarFragment implements XListView.IXListViewListener {
    @InjectView(R.id.xlv_pinerest_style)
    XListView xlv_pinerest_style;
    private GridView gridView1;

   /* @InjectView(R.id.framelayout)//
    FrameLayout framelayout;*/

    private int width;
    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距
    private String id;
    private View headview;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout ll;
    private FrameLayout framelayout;
    private InvitationDao idao;
    private int bol = 1;
    private int PAGE_NUM = 1;
    private List<Post> listPost;
    private List<Category> listCategroy3;
    private InvitationListViewPinterestStyleAdapter mInvitationListViewPinterestStyleAdapter;
    private InvitationPullDownGridViewAdapter adapter;
    private String thirdKindId = null;
    private List<Post> listPost2;
    private int Position;
    private HomeAutoSwitchPicHolder mAutoSwitchPicHolder;
    private ArrayList<String> mData;
    private TextView tv_search;
    private boolean isFirst=true;


    public static InvitationPullDownHaveThirdKindPinterestStyleFragment newInstance(String id,String thirdKindId) {
        InvitationPullDownHaveThirdKindPinterestStyleFragment fragment = new InvitationPullDownHaveThirdKindPinterestStyleFragment();
        fragment.id = id;
        fragment.thirdKindId = thirdKindId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInvitationListViewPinterestStyleAdapter = new InvitationListViewPinterestStyleAdapter(getActivity());
        width = getResources().getDisplayMetrics().widthPixels;
        idao = new InvitationDao(this);
        adapter = new InvitationPullDownGridViewAdapter(getActivity());
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!InvitationPullDownActivity.isFromSelcet){
            thirdKindId=null;
            Log.i("tag", "dsfsdfsdf1122");
        }
        if (isVisibleToUser&&bol==1 ) {
           if(listCategroy3!=null)listCategroy3.clear();
            if(thirdKindId==null) {
                 idao.requesPostCategory(id, "2", "0");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), id, "0", "", PAGE_NUM, Constant.PAGE_SIZE);
            }else {
               idao.requesPostCategory(id, "2", "0");
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", "", PAGE_NUM, Constant.PAGE_SIZE);
            }
       }

    }

    public void request(String id) {
        bol = 1;
        thirdKindId = id;
        idao.requestPostListByCategory(Arad.preferences.getString("memberId"), thirdKindId, "0", "", PAGE_NUM, Constant.PAGE_SIZE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation_pull_down_have_third_kind_pinterest_style, container, false);
        ButterKnife.inject(this, view);
        headview = getActivity().getLayoutInflater().inflate(R.layout.fragment_invitation_header, null);
        xlv_pinerest_style.addHeaderView(headview);
        xlv_pinerest_style.setFocusable(false);
        xlv_pinerest_style.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                int nowPos = position - xlv_pinerest_style.getHeaderViewsCount();
                Intent intent = new Intent(getActivity(), InvitationPinerestGalleyActivity.class);
                intent.putExtra("id", listPost.get(nowPos).getId());
                startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
            }
        });
        initData();
        getScreenDen();
        xlv_pinerest_style.setPullLoadEnable(true);
        xlv_pinerest_style.setXListViewListener(this);
        // setValue();


        return view;
    }

    private void initData() {
        gridView1 = (GridView) headview.findViewById(R.id.gridView_pull_down);
        //   horizontalScrollView = (HorizontalScrollView) headview.findViewById(R.id.scrollView);
        ll = (LinearLayout) headview.findViewById(R.id.ll);
        gridView1 = (GridView) headview.findViewById(R.id.gridView_pull_down);
       framelayout = (FrameLayout) headview.findViewById(R.id.framelayout);
        tv_search = (TextView) headview.findViewById(R.id.tv_search);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bol = 1;
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(position).getId(), "0", "", PAGE_NUM, Constant.PAGE_SIZE);
            }
        });

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        xlv_pinerest_style.stopLoadMore();
        xlv_pinerest_style.stopRefresh();
        if (requestCode == 1) {
            bol = 0;
            listPost = idao.getListPost();
            if(listCategroy3==null){
                framelayout.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
                // horizontalScrollView.setVisibility(View.VISIBLE);
                gridView1.setVisibility(View.GONE);
                setLoopView();
            }else {
                framelayout.setVisibility(View.GONE);
                ll.setVisibility(View.VISIBLE);
                // horizontalScrollView.setVisibility(View.VISIBLE);
                gridView1.setVisibility(View.VISIBLE);
            }
            mInvitationListViewPinterestStyleAdapter.setList(listPost);
            thirdKindId=null;
            InvitationPullDownActivity.isFromSelcet=false;
        }
        if (requestCode == 2) {
            bol = 0;
            framelayout.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
            // horizontalScrollView.setVisibility(View.VISIBLE);
            gridView1.setVisibility(View.VISIBLE);
            // horizontalScrollView.setVisibility(View.VISIBLE);
            listCategroy3 = idao.getListCategory();
            adapter.setList(listCategroy3);
            if(isFirst) {
              //  idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(0).getId(), "0", "", PAGE_NUM, Constant.PAGE_SIZE);
            }
            isFirst=false;

            tv_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), InvitationSearchActivity.class);
                   startActivityForResult(intent,1112);
                    AnimUtil.intentSlidIn(getActivity());
                }
            });

            thirdKindId=null;
            // setValue();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1112&&resultCode==2222){
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
        xlv_pinerest_style.stopLoadMore();
        xlv_pinerest_style.stopRefresh();
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
                Position = position;
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(position).getId(), "0", "", PAGE_NUM, Constant.PAGE_SIZE);
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
        xlv_pinerest_style.setAdapter(mInvitationListViewPinterestStyleAdapter);
        gridView1.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        listPost.clear();
        PAGE_NUM = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listCategroy3 != null) {
                    idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(Position).getId(), "0", "", PAGE_NUM, Constant.PAGE_SIZE);
                }
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), id, "0", "", PAGE_NUM, Constant.PAGE_SIZE);
            }
        }, 500);

    }

    @Override
    public void onLoadMore() {
        PAGE_NUM += 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listCategroy3 != null) {
                    idao.requestPostListByCategory(Arad.preferences.getString("memberId"), listCategroy3.get(Position).getId(), "0", "", PAGE_NUM, Constant.PAGE_SIZE);
                }
                idao.requestPostListByCategory(Arad.preferences.getString("memberId"), id, "0", "", PAGE_NUM, Constant.PAGE_SIZE);
            }
        }, 500);
    }
}
