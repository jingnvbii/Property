package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.AnimUtil;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.adapter.StoreShopListHorzitalStyleGridViewAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城商品列表 fragment
 * Created by Administrator on 2015/11/30.
 */
public class StoreShopListHorzitalStyleFragment extends ToolBarFragment {
    @InjectView(R.id.gridview_shore_horzital_style)
    GridView gridView;
    private List<Merchant> aroundCompanies ;
    private StoreShopListHorzitalStyleGridViewAdapter mAdapter;

    public static StoreShopListHorzitalStyleFragment newInstance(List<Merchant>aroundCompanies) {
        StoreShopListHorzitalStyleFragment fragment = new StoreShopListHorzitalStyleFragment();
        fragment.aroundCompanies = aroundCompanies;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_shop_list_horzital_style, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new StoreShopListHorzitalStyleGridViewAdapter(getActivity());
        mAdapter.setList(aroundCompanies);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(getActivity(), StoreCommodityDetailActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(getActivity());
            }
        });
    }
}
