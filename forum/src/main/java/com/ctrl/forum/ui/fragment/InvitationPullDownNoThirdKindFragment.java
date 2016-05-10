package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.ui.viewpage.CycleViewPager;
import com.ctrl.forum.ui.viewpage.ViewFactory;
import com.ctrl.forum.utils.DemoUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 帖子下拉列表无三级分类 fragment
 * Created by Administrator on 2015/11/30.
 */
public class InvitationPullDownNoThirdKindFragment extends ToolBarFragment {
    @InjectView(R.id.listview_invitation_pull_down_no_third_kind)
    PullToRefreshListView listview_invitation_pull_down_no_third_kind;

    private View vhdf;
    private CycleViewPager cycleViewPager;
    private FrameLayout framelayout;


    public static InvitationPullDownNoThirdKindFragment newInstance() {
        InvitationPullDownNoThirdKindFragment fragment = new InvitationPullDownNoThirdKindFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation_pull_down_no_third_kind, container, false);
        ButterKnife.inject(this, view);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview =getActivity().getLayoutInflater().inflate(R.layout.fragment_invitation_header_second, listview_invitation_pull_down_no_third_kind, false);
        headview.setLayoutParams(layoutParams);
        framelayout=(FrameLayout)headview.findViewById(R.id.framelayout);
        ListView lv = listview_invitation_pull_down_no_third_kind.getRefreshableView();
        lv.addHeaderView(headview);
        //调用轮播图
        setLoopView();
        return view;
    }

    private void setLoopView() {
        // 三句话 调用轮播广告
        vhdf = getActivity().getLayoutInflater().inflate(R.layout.viewpage, null);
        cycleViewPager = (CycleViewPager) getActivity().getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
        ViewFactory.initialize(getActivity(), vhdf, cycleViewPager, DemoUtil.cycData());
        framelayout.addView(vhdf);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
