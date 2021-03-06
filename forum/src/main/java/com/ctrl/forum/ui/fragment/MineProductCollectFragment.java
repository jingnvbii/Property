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
import com.ctrl.forum.entity.ProductCollect;
import com.ctrl.forum.ui.activity.store.StoreCommodityDetailActivity;
import com.ctrl.forum.ui.adapter.ProductCollectFragmentAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 我的收藏-商品收藏
 * Created by Administrator on 2016/5/13.
 */
public class MineProductCollectFragment extends ToolBarFragment{
    private List<ProductCollect> productCollects;
    private ProductCollectFragmentAdapter productCollectFragmentAdapter;
    private PullToRefreshListView lv_content;
    private CollectDao cdao;
    private int PAGE_NUM=1;

    public static MineProductCollectFragment newInstance() {
        MineProductCollectFragment fragment = new MineProductCollectFragment();
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
        View view = inflater.inflate(R.layout.fragment_mine_list, container, false);
        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);

        productCollectFragmentAdapter = new ProductCollectFragmentAdapter(getActivity());
        lv_content.setAdapter(productCollectFragmentAdapter);

        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (productCollects != null) {
                    productCollects.clear();
                    PAGE_NUM = 1;
                    productCollectFragmentAdapter = new ProductCollectFragmentAdapter(getActivity());
                    lv_content.setAdapter(productCollectFragmentAdapter);
                }
                cdao.productsCollection(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (productCollects != null) {
                    PAGE_NUM += 1;
                    cdao.productsCollection(Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (productCollects!=null){
                    String productId = productCollects.get(position-1).getId();//点击的商品id
                    Intent intent = new Intent(getActivity(), StoreCommodityDetailActivity.class);
                    intent.putExtra("id",productId);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private void initData() {
        cdao = new CollectDao(this);
        cdao.productsCollection(Arad.preferences.getString("memberId"), PAGE_NUM+"",Constant.PAGE_SIZE+"");
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if(requestCode == 0){
            productCollects = cdao.getProductCollects();
            if (productCollects!=null){
                productCollectFragmentAdapter.setList(productCollects);
            }
        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }
}
