package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.GridViewForScrollView;
import com.ctrl.forum.customview.XListView;
import com.ctrl.forum.entity.Category2;
import com.ctrl.forum.entity.Invitation_listview;
import com.ctrl.forum.entity.ThirdKind;
import com.ctrl.forum.ui.adapter.InvitationPullDownGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子下拉列表有三级分类 fragment
 * Created by Administrator on 2015/11/30.
 */
public class InvitationPullDownHaveThirdKindPinterestStyleFragment extends ToolBarFragment {
    @InjectView(R.id.invitation_list)
    XListView invitation_list;
    private List<Invitation_listview> list;
    private List<ThirdKind> kindList;
    private GridViewForScrollView gridView1;

    private int width;
    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距
    private List<Category2> mCategory2List;


    public static InvitationPullDownHaveThirdKindPinterestStyleFragment newInstance(List< Category2>list) {
        InvitationPullDownHaveThirdKindPinterestStyleFragment fragment = new InvitationPullDownHaveThirdKindPinterestStyleFragment();
        fragment.mCategory2List=list;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation_pull_down_have_third_kind_pinterest_style, container, false);
        ButterKnife.inject(this, view);
        width = getResources().getDisplayMetrics().widthPixels;
       // AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview =getActivity().getLayoutInflater().inflate(R.layout.fragment_invitation_header,null);
       // headview.setLayoutParams(layoutParams);
       // ListView lv = invitation_list.getRefreshableView();
        gridView1=(GridViewForScrollView)headview.findViewById(R.id.gridView1);
        invitation_list.addHeaderView(headview);
        initData();
        getScreenDen();
        setValue();
        return view;
    }

    private void initData() {
        list=new ArrayList<>();

        for(int i=0;i<20;i++){
            Invitation_listview invitation=new Invitation_listview();
            invitation.setName("汪峰"+i+"便利店");
            list.add(invitation);
        }


        kindList=new ArrayList<>();
        for(int i=0;i<8;i++){
            ThirdKind kind=new ThirdKind();
            kind.setKindName("频道:"+i);
            kindList.add(kind);
        }
    }

    private void setValue() {
        InvitationPullDownGridViewAdapter adapter = new InvitationPullDownGridViewAdapter(getActivity());
        adapter.setList(mCategory2List);
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
      /*  mAdapter = new StoreShopDetailPingLunListViewAdapter(getActivity());
        mAdapter.setList(aroundCompanies);
        lv_pingjia_detail.setAdapter(mAdapter);*/
    }
}
