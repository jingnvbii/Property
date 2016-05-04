package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城订单状态 fragment
 * Created by Administrator on 2015/11/30.
 */
public class StoreOrderStatusFragment extends ToolBarFragment {
    @InjectView(R.id.lv_order_status_fragment)
    PullToRefreshListView lv_order_status_fragment;


    public static StoreOrderStatusFragment newInstance() {
        StoreOrderStatusFragment fragment = new StoreOrderStatusFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_order_status, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      /*  mAdapter = new StoreShopDetailPingLunListViewAdapter(getActivity());
        mAdapter.setList(aroundCompanies);
        lv_pingjia_detail.setAdapter(mAdapter);*/
    }
}
