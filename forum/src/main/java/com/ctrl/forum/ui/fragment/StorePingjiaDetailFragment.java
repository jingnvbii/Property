package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.entity.Merchant;
import com.ctrl.forum.ui.adapter.StoreShopDetailPingLunListViewAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城评价详情列表 fragment
 * Created by Administrator on 2015/11/30.
 */
public class StorePingjiaDetailFragment extends ToolBarFragment {
    @InjectView(R.id.lv_pingjia_detail)
    ListView lv_pingjia_detail;

    private List<Merchant> aroundCompanies ;
    private StoreShopDetailPingLunListViewAdapter mAdapter;

    public static StorePingjiaDetailFragment newInstance(List<Merchant>aroundCompanies) {
        StorePingjiaDetailFragment fragment = new StorePingjiaDetailFragment();
        fragment.aroundCompanies = aroundCompanies;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_pingjia_detail, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new StoreShopDetailPingLunListViewAdapter(getActivity());
        mAdapter.setList(aroundCompanies);
        lv_pingjia_detail.setAdapter(mAdapter);
        lv_pingjia_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* if (!TextUtils.isEmpty(aroundCompanies.get(position).getMobile())) {
                    AndroidUtil.dial(getActivity(), aroundCompanies.get(position).getMobile());
                } else {
                    MessageUtils.showShortToast(getActivity(), "该服务暂无联系电话");
                }*/
            }
        });
    }
}
