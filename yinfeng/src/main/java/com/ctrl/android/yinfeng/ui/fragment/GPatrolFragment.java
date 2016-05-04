package com.ctrl.android.yinfeng.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.dao.PatrolDao;
import com.ctrl.android.yinfeng.ui.adapter.GPatrolAdapter;
import com.ctrl.android.yinfeng.ui.gpatrol.GPatrolActivity;
import com.ctrl.android.yinfeng.ui.gpatrol.GPatrolLineActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 巡更巡查fragment
 * Created by Administrator on 2015/11/10.
 */
public class GPatrolFragment extends ToolBarFragment {
    @InjectView(R.id.pull_refresh_grid)
    PullToRefreshListView mPullRefreshGridView;
    private ListView mGridView;
    private GPatrolAdapter mAdapter;
    private String mType;
    private int mPage = 1;
    private int bol = 1;
    private PatrolDao pdao;

    public static GPatrolFragment newInstance(String type) {
        GPatrolFragment fragment = new GPatrolFragment();
        fragment.mType = type;
        return fragment;
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        mPullRefreshGridView.onRefreshComplete();
        showProgress(false);
        bol=0;
        mAdapter = new GPatrolAdapter(getActivity(),this);
        mGridView = mPullRefreshGridView.getRefreshableView();
        mAdapter.setData(pdao.getPatrolRouteList());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent(getActivity(), GPatrolLineActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
            }
        });
        mPullRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage = 1;
               pdao.getPatrolRouteList().clear();
               pdao.requestPatrolList(Arad.preferences.getString("staffId"),"0", mType, String.valueOf(mPage), "");


            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPage+=1;
                pdao.requestPatrolList(Arad.preferences.getString("staffId"),"0", mType, String.valueOf(mPage), "");


            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),GPatrolLineActivity.class);
                intent.putExtra("patrolRouteStaffId",pdao.getPatrolRouteList().get(position-1).getPatrolRouteStaffId());
                startActivityForResult(intent, 200);
                AnimUtil.intentSlidIn(getActivity());
            }
        });
//        mAdapter.setData(dao.getSendOrders());


    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        mPullRefreshGridView.onRefreshComplete();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = 1;
        pdao=new PatrolDao(this);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&bol==1) {
            pdao.requestPatrolList(Arad.preferences.getString("staffId"),"0", mType, String.valueOf(mPage), "");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.j_fragment_patrol, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200 && resultCode==201){
            GPatrolActivity activity = (GPatrolActivity) getActivity();
            activity.getAdapter().reLoad();
            //  activity.getmViewpager().setCurrentItem(1);
        }
    }



}
