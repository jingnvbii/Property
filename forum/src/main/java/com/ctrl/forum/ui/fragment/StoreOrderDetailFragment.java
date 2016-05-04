package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城订单详情 fragment
 * Created by Administrator on 2015/11/30.
 */
public class StoreOrderDetailFragment extends ToolBarFragment {
    @InjectView(R.id.lv_order_detail_fragment)
    ListView lv_order_detail_fragment;


    public static StoreOrderDetailFragment newInstance() {
        StoreOrderDetailFragment fragment = new StoreOrderDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_order_detail, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
