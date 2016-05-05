package com.ctrl.forum.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ctrl.forum.R;
import com.ctrl.forum.entity.Coupon;
import com.ctrl.forum.ui.adapter.MineCouponListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * �Ż݄�-�ѹ���
 */
public class MineCouponPastFragment extends Fragment {
    private List<Coupon> coupons;
    private ListView lv_content;
    private MineCouponListAdapter couponListAdapter;
   // private View view;

    public static MineCouponPastFragment newInstance() {
        MineCouponPastFragment fragment = new MineCouponPastFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MineCouponPastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine_notificationi, container, false);
        lv_content = (ListView) v.findViewById(R.id.lv_content);
        initData();
        //view = LayoutInflater.from(getActivity()).inflate(R.layout.item_hui_past,null);
        //couponListAdapter = new CouponListAdapter(getActivity(),coupons,view);
        couponListAdapter = new MineCouponListAdapter(getActivity(),coupons);
        lv_content.setAdapter(couponListAdapter);
        return v;
    }

    //��ʼ�����
    private void initData() {
        coupons = new ArrayList<>();
        for (int i=0;i<3;i++){
            Coupon coupon = new Coupon();
           coupon.setName(i+"");
            coupons.add(coupon);
        }

    }



}
