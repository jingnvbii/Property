package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.CollectDao;
import com.ctrl.forum.entity.CompanyCollect;
import com.ctrl.forum.ui.activity.store.StoreShopDetailActivity;
import com.ctrl.forum.ui.adapter.CompanyCollectFragmentAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 我的收藏-店铺收藏
 * Created by Administrator on 2016/5/13.
 */
public class MineCompanyCollectFragment extends ToolBarFragment{
    private List<CompanyCollect> companyCollects;
    private CompanyCollectFragmentAdapter companyCollectFragmentAdapter;
    private PullToRefreshListView lv_content;
    private CollectDao cdao;
    private int PAGE_NUM=1;
    private String couponEnable,packetEnable;

    public static MineCompanyCollectFragment newInstance() {
        MineCompanyCollectFragment fragment = new MineCompanyCollectFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine_list, container, false);

        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        companyCollectFragmentAdapter = new CompanyCollectFragmentAdapter(getActivity());
        lv_content.setAdapter(companyCollectFragmentAdapter);
        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (companyCollects != null) {
                    companyCollects.clear();
                    PAGE_NUM = 1;
                    companyCollectFragmentAdapter = new CompanyCollectFragmentAdapter(getActivity());
                    lv_content.setAdapter(companyCollectFragmentAdapter);
                }
                cdao.companysCollection(Arad.preferences.getString("memberId"), "100", "100", PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (companyCollects != null) {
                    PAGE_NUM += 1;
                    cdao.companysCollection(Arad.preferences.getString("memberId"), "100", "100", PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (companyCollects!=null) {
                    Intent intent = new Intent(getActivity(), StoreShopDetailActivity.class);
                    intent.putExtra("id", companyCollects.get(position-1).getId());
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private void initData() {
        cdao = new CollectDao(this);
        //cdao.companysCollection(会员id,纬度,经度,当前页号,每页的数目);
       String latitude =  Arad.preferences.getString("latitude");
        String lontitude =  Arad.preferences.getString("lontitude");
        cdao.companysCollection(Arad.preferences.getString("memberId"), latitude,lontitude, PAGE_NUM + "", Constant.PAGE_SIZE + "");
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if(requestCode == 1){
            companyCollects = cdao.getCompanyCollects();
            if (companyCollects!=null){
                companyCollectFragmentAdapter.setList(companyCollects);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }


}
