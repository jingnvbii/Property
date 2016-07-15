package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.MallDao;
import com.ctrl.forum.entity.ShopReply;
import com.ctrl.forum.ui.activity.store.StorePingjiaDetailActivity;
import com.ctrl.forum.ui.adapter.StoreShopDetailPingLunListViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 商城评价详情列表 fragment
 * Created by Administrator on 2015/11/30.
 */
public class StorePingjiaDetailFragment extends ToolBarFragment {
    @InjectView(R.id.lv_pingjia_detail)
    PullToRefreshListView lv_pingjia_detail;

    private MallDao mdao;
    private int PAGE_NUM = 1;
    private int bol = 1;
    private boolean isFirst = true;

    private StoreShopDetailPingLunListViewAdapter mAdapter;
    private int type;
    private String id;
    private List<ShopReply> allEvaluationlist;
    private List<ShopReply> listPraise;
    private List<ShopReply> listMedium;
    private List<ShopReply> listBad;
    private String allNum;
    private String goodNum;
    private String mediumNum;
    private String badNum;

    public static StorePingjiaDetailFragment newInstance(int type) {
        StorePingjiaDetailFragment fragment = new StorePingjiaDetailFragment();
        fragment.type = type;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mdao = new MallDao(this);
        id = getActivity().getIntent().getStringExtra("id");
        mAdapter = new StoreShopDetailPingLunListViewAdapter(getActivity());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && bol == 1)
            mdao.requestCompanysEvaluate(id,String.valueOf(type),String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_pingjia_detail.onRefreshComplete();
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_pingjia_detail.onRefreshComplete();
        if (requestCode == 3) {
            bol = 0;
            allEvaluationlist = mdao.getListAllEvaluation();
            listPraise = mdao.getListPraise();
            listMedium = mdao.getListMedium();
            listBad = mdao.getListBad();
            switch (type) {
                case 0:
                    if(allEvaluationlist.size()>0)
                    mAdapter.setList(allEvaluationlist);
                    break;
                case 1:
                    if(listPraise.size()>0)
                    mAdapter.setList(listPraise);
                    break;
                case 2:
                    if(listMedium.size()>0)
                    mAdapter.setList(listMedium);
                    break;
                case 3:
                    if(listBad.size()>0)
                    mAdapter.setList(listBad);
                    break;
            }
            if(isFirst)
            setNum();
            isFirst=false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_pingjia_detail, container, false);
        ButterKnife.inject(this, view);
        lv_pingjia_detail.setMode(PullToRefreshBase.Mode.BOTH);
        lv_pingjia_detail.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                switch (type) {
                    case 0:
                        allEvaluationlist.clear();
                        break;
                    case 1:
                        listPraise.clear();
                        break;
                    case 2:
                        listMedium.clear();
                        break;
                    case 3:
                        listBad.clear();
                        break;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mdao.requestCompanysEvaluate(id,String.valueOf(type),String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                    }
                }, 500);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mdao.requestCompanysEvaluate(id,String.valueOf(type),String.valueOf(PAGE_NUM), String.valueOf(Constant.PAGE_SIZE));
                    }
                }, 500);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_pingjia_detail.setAdapter(mAdapter);
        lv_pingjia_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

    }

    private void setNum() {
        StorePingjiaDetailActivity activity=(StorePingjiaDetailActivity)getActivity();
        allNum=allEvaluationlist.size()==0?"0":allEvaluationlist.get(0).getCount();
        goodNum=listPraise.size()==0?"0":listPraise.get(0).getCount();
        mediumNum=listMedium.size()==0?"0":listMedium.get(0).getCount();
        badNum=listBad.size()==0?"0":listBad.get(0).getCount();
        activity.setNum(allNum,goodNum,mediumNum,badNum);
    }
}
