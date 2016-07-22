package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.dao.RimDao;
import com.ctrl.forum.entity.RimServeCategory;
import com.ctrl.forum.ui.activity.LoginActivity;
import com.ctrl.forum.ui.activity.rim.RimCollectServeActivity;
import com.ctrl.forum.ui.activity.rim.RimSearchActivity;
import com.ctrl.forum.ui.adapter.RimGridViewAdapter;
import com.ctrl.forum.ui.adapter.RimListViewAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周边fragment
 * Created by jason on 2016/4/7.
 */
public class  RimFragment extends ToolBarFragment implements View.OnClickListener,AbsListView.OnScrollListener {

    @InjectView(R.id.lv_content)
    ListView lv_content; //分类的内容

    private RimGridViewAdapter rimGridViewAdapter;
    private RimListViewAdapter rimListViewAdapter;
    private List<RimServeCategory> category;
    private RimDao rdao;

    private int scrollState;//当前滚动状态
    private int firstVisibleItem;//当前可见的第一个item
    private int startY;//刚开始触摸屏幕时的Y值
    private int endY;
    private RelativeLayout rl_search;
    private RelativeLayout rl_collect;

    public static RimFragment newInstance() {
        RimFragment fragment = new RimFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rim, container, false);
        ButterKnife.inject(this, view);

        initView();

        View v = new TextView(getActivity());
        v.setBackgroundColor(getResources().getColor(R.color.main_bg));
        lv_content.addFooterView(v);

        //listview增加头部布局
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        View headview = getActivity().getLayoutInflater().inflate(R.layout.item_rim_list_collect, lv_content, false);
        rl_search = (RelativeLayout) headview.findViewById(R.id.rl_search);
        rl_search.setOnClickListener(this);
        rl_collect = (RelativeLayout) headview.findViewById(R.id.rl_collect);
        rl_collect.setOnClickListener(this);
        headview.setLayoutParams(layoutParams);
        lv_content.addHeaderView(headview);

        //lv_content.setOnTouchListener(toun);
        rimListViewAdapter.setRimGridViewAdapter(rimGridViewAdapter);
        lv_content.setAdapter(rimListViewAdapter);

        return view;
    }

    private void initView() {
        rimGridViewAdapter = new RimGridViewAdapter(getActivity());
        rimListViewAdapter = new RimListViewAdapter(getActivity());

        rdao = new RimDao(this);
        rdao.getAroundServiceCategory();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_collect:
                if (Arad.preferences.getString("memberId").equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), RimCollectServeActivity.class));
                }
                break;
            case R.id.rl_search:
                if (Arad.preferences.getString("memberId").equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), RimSearchActivity.class));
                }
                break;
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 1) {
            category = rdao.getRimServeCategory();
            if (category != null) {
                rimListViewAdapter.setData(category);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        MessageUtils.showShortToast(getActivity(), errorMessage);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //记录当前的滚动状态
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    private View.OnTouchListener toun = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()){
                //手指落到屏幕上时
                case MotionEvent.ACTION_DOWN:
                    //如果当前可见的第一个item为第0号，说明ListView位于顶端，可以执行下拉刷新
                    if(firstVisibleItem == 0){
                        startY = (int) event.getY();
                        MessageUtils.showShortToast(getActivity(),"第一个item为第0");
                    }
                    break;
                 //手指在屏幕上拖动时
           /* case MotionEvent.ACTION_MOVE:
                if(firstVisibleItem == 0 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    endY = (int) event.getY();
                    int move = endY - startY;
                    if (move > 30) {
                        tv.setVisibility(View.VISIBLE);
                        rl_search.setVisibility(View.VISIBLE);
                    }
                }
                break;*/
                //手指离开屏幕时
                case MotionEvent.ACTION_UP:
                    if(firstVisibleItem == 0&& scrollState == SCROLL_STATE_IDLE) {
                        endY = (int) event.getY();
                        int move = endY - startY;
                        if (move > 30) {
                            rl_search.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
            }
            return false;
        }
    };


}
