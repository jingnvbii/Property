package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.customview.PullToRefreshListViewForScrollView;
import com.ctrl.forum.entity.Kind;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.ui.activity.store.StoreLocateActivity;
import com.ctrl.forum.ui.activity.store.StoreScreenActivity;
import com.ctrl.forum.ui.adapter.StoreFragmentAdapter;
import com.ctrl.forum.ui.adapter.StoreGridViewAdapter;
import com.ctrl.forum.ui.viewpage.CycleViewPager;
import com.ctrl.forum.ui.viewpage.ViewFactory;
import com.ctrl.forum.utils.DemoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城 fragment
 * Created by jason on 2016/4/7.
 */
public class StroeFragment extends ToolBarFragment implements View.OnClickListener {
    @InjectView(R.id.scrollView)
    HorizontalScrollView scrollView;
    @InjectView(R.id.gridView1)
    GridView gridView1;
    @InjectView(R.id.framelayout)
    FrameLayout framelayout;
    @InjectView(R.id.tv_toolbar)//定位标
            TextView tv_toolbar;

    @InjectView(R.id.listview)//下拉列表
    PullToRefreshListViewForScrollView listview;


    DisplayMetrics dm;
    private int NUM = 4; // 每行显示个数
    private int hSpacing = 20;// 水平间距
    private List<Kind> kindList;
    private View vhdf;
    private CycleViewPager cycleViewPager;
    private StoreFragmentAdapter listviewAdapter;
    private List<Merchant> list;


    public static StroeFragment newInstance() {
        StroeFragment fragment = new StroeFragment();
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
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        ButterKnife.inject(this, view);
        scrollView.setHorizontalScrollBarEnabled(false);// 隐藏滚动条
        initData();
        getScreenDen();
        setValue();
        // 三句话 调用轮播广告
        vhdf = getActivity().getLayoutInflater().inflate(R.layout.viewpage2, null);
        cycleViewPager = (CycleViewPager) getActivity().getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content_2);
        ViewFactory.initialize(getActivity(), vhdf, cycleViewPager, DemoUtil.cycData());
        framelayout.addView(vhdf);
        initView();

        listviewAdapter=new StoreFragmentAdapter(getActivity());
        listviewAdapter.setList(list);
        listview.setAdapter(listviewAdapter);
        return view;
    }

    private void initView() {
        tv_toolbar.setOnClickListener(this);

    }

    private void initData() {
       list=new ArrayList<>();

        for(int i=0;i<20;i++){
            Merchant manchant = new Merchant();
            manchant.setName("章子怡"+i+"便利店");
            list.add(manchant);
        }


        kindList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Kind kind = new Kind();
            kind.setKindName("频道:" + i);
            kindList.add(kind);
        }
    }

    private void setValue() {
        StoreGridViewAdapter adapter = new StoreGridViewAdapter(getActivity());
        adapter.setList(kindList);
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
                Intent intent = new Intent(getActivity(), StoreScreenActivity.class);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_toolbar:
                Intent intent_toolbar = new Intent(getActivity(), StoreLocateActivity.class);
                startActivity(intent_toolbar);
                AnimUtil.intentSlidIn(getActivity());
                break;
        }


    }
}
