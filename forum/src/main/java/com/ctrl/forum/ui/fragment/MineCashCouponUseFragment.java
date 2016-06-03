package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.CouponsDao;
import com.ctrl.forum.entity.Coupon;
import com.ctrl.forum.ui.adapter.MineCouponXianListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 现金劵--已用
 */
public class MineCashCouponUseFragment extends ToolBarFragment {
    private List<Coupon> coupons = new ArrayList<>();
    private PullToRefreshListView lv_content;
    private MineCouponXianListAdapter CouponXianListAdapter;
    private CouponsDao cdao;
    private int PAGE_NUM=1;
    public static MineCashCouponUseFragment newInstance() {
        MineCashCouponUseFragment fragment = new MineCashCouponUseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MineCashCouponUseFragment() {
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

        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (coupons.size() > 0) {
                    coupons.clear();
                    PAGE_NUM = 1;
                    cdao.getMemberCoupons("1", "1", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (coupons.size() > 0) {
                    PAGE_NUM += 1;
                    cdao.getMemberCoupons("1", "1", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });
        return v;
    }

    private void initData() {
        cdao = new CouponsDao(this);
        cdao.getMemberCoupons("1", "1", Arad.preferences.getString("memberId"), Constant.PAGE_NUM + "", Constant.PAGE_SIZE + "");
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==0){
            MessageUtils.showShortToast(getActivity(), "获取现金劵成功");
            coupons = cdao.getCoupons();
            if (coupons!=null) {
                int resources = R.layout.item_mine_xianuse;
                CouponXianListAdapter = new MineCouponXianListAdapter(getActivity(), resources);
                CouponXianListAdapter.setMessages(coupons);
                lv_content.setAdapter(CouponXianListAdapter);
            }
        }
    }

    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        MessageUtils.showShortToast(getActivity(), "获取失败");
        lv_content.onRefreshComplete();
    }

    @Override
    public void onNoConnect() {
        super.onNoConnect();
        lv_content.onRefreshComplete();
    }
}