package com.ctrl.forum.ui.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.utils.MessageUtils;
import com.beanu.arad.widget.SlidingUpPanelLayout;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.CouponsDao;
import com.ctrl.forum.entity.Coupon;
import com.ctrl.forum.ui.adapter.MineCouponXianListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

/**
 * 现金劵-未使用
 */
public class MineCashCouponFragment extends ToolBarFragment implements View.OnClickListener{
    private List<Coupon> coupons ;
    private PullToRefreshListView lv_content;
    private MineCouponXianListAdapter couponXianListAdapter;
    private CouponsDao cdao;
    private View popView;
    private PopupWindow popupWindow;
    private TextView tv_no,tv_yes,tv_share;
    private String couponId; //现金劵的id
    private int PAGE_NUM=1;

    public static MineCashCouponFragment newInstance() {
        MineCashCouponFragment fragment = new MineCashCouponFragment();
        return fragment;
    }

    public MineCashCouponFragment() {
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

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (coupons.size() > 0) {
                    couponId = coupons.get(position).getId();//点击的现金劵的id
                    String useType = coupons.get(position).getUseType();//使用方式（0：使用核销、1：分享核销）
                    if (useType.equals("0")) {
                        popView = LayoutInflater.from(getActivity()).inflate(R.layout.coupon_share, null);
                        tv_no = (TextView) popView.findViewById(R.id.tv_no);
                        tv_share = (TextView) popView.findViewById(R.id.tv_share);
                    }
                    if (useType.equals("1")) {
                        popView = LayoutInflater.from(getActivity()).inflate(R.layout.coupon_cost, null);
                        tv_no = (TextView) popView.findViewById(R.id.tv_no);
                        tv_yes = (TextView) popView.findViewById(R.id.tv_yes);
                    }
                    initPop();
                    popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);  //在底部
                    popupWindow.update();
                }
            }
        });

        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (coupons!=null) {
                    coupons.clear();
                    PAGE_NUM = 1;
                } else {
                    lv_content.onRefreshComplete();
                }
                cdao.getMemberCoupons("1", "0", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (coupons!=null) {
                    PAGE_NUM += 1;
                    cdao.getMemberCoupons("1", "0", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        return v;
    }

    private void initData() {
        cdao = new CouponsDao(this);
        cdao.getMemberCoupons("1", "0", Arad.preferences.getString("memberId"), PAGE_NUM + "", Constant.PAGE_SIZE + "");

        int resources = R.layout.item_mine_xian;
        couponXianListAdapter = new MineCouponXianListAdapter(getActivity(), resources);
        lv_content.setAdapter(couponXianListAdapter);

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==0){
            MessageUtils.showShortToast(getActivity(),"获取现金劵成功");
            coupons = cdao.getCoupons();
            if (coupons!=null) {
                couponXianListAdapter.setMessages(coupons);
            }
        }
        if (requestCode==2){
            MessageUtils.showShortToast(getActivity(),"使用成功");
        }
    }

    //初始化弹窗
    private void initPop() {
        popupWindow = new PopupWindow(popView, SlidingUpPanelLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.pop_bg));
        colorDrawable.setAlpha(40);
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setOutsideTouchable(true);

        tv_no.setOnClickListener(this);
        tv_yes.setOnClickListener(this);
        tv_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_share://立即分享
               //分享成功后使用此优惠劵

                break;
            case R.id.tv_no: //否
                popupWindow.dismiss();
                break;
            case R.id.tv_yes: //是
                //使用此优惠劵,优惠劵的id为couponId;
                cdao.useCashCoupons(couponId);
                break;
        }
    }
    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        MessageUtils.showShortToast(getActivity(), "获取失败");
        lv_content.onRefreshComplete();
    }
}
