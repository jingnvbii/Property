package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.CouponsDao;
import com.ctrl.forum.entity.Coupon;
import com.ctrl.forum.ui.adapter.MineCouponXianListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 现金劵--过期
 */
public class MineCashCouponPastFragment extends ToolBarFragment {
    private List<Coupon> coupons ;
    private PullToRefreshListView lv_content;
    private MineCouponXianListAdapter couponXianListAdapter;
    private CouponsDao cdao;
    private int PAGE_NUM = 1;
    private int resources;

    public static MineCashCouponPastFragment newInstance() {
        MineCashCouponPastFragment fragment = new MineCashCouponPastFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MineCashCouponPastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine_list, container, false);
        lv_content = (PullToRefreshListView) v.findViewById(R.id.lv_content);
        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (coupons != null) {
                    coupons.clear();
                    PAGE_NUM = 1;
                    couponXianListAdapter = new MineCouponXianListAdapter(getActivity(),resources);
                    lv_content.setAdapter(couponXianListAdapter);
                } cdao.getMemberCoupons("1", "2", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (coupons != null) {
                    PAGE_NUM += 1;
                    cdao.getMemberCoupons("1", "2", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });
        return v;
    }

    private void initData() {
         resources = R.layout.item_mine_xianpast;
        couponXianListAdapter = new MineCouponXianListAdapter(getActivity(),resources);
        lv_content.setAdapter(couponXianListAdapter);

       cdao = new CouponsDao(this);
        cdao.getMemberCoupons("1", "2", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==0){
            coupons = cdao.getCoupons();
            if (coupons!=null) {
                couponXianListAdapter.setMessages(coupons);
            }
        }
    }

    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

}
