package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.ui.activity.rim.RimCollectServeActivity;
import com.ctrl.forum.ui.activity.rim.ItemRimActivity;
import com.ctrl.forum.ui.adapter.RimGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 周边fragment
 * Created by jason on 2016/4/7.
 */
public class RimFragment extends ToolBarFragment implements View.OnClickListener{

    @InjectView(R.id.gv_hot)
    GridView gv_hot;
    @InjectView(R.id.gv_phone)
    GridView gv_phone;
    @InjectView(R.id.gv_serve)
    GridView gv_serve;
    @InjectView(R.id.gv_shop)
    GridView gv_shop;
    @InjectView(R.id.gv_doctor)
    GridView gv_doctor;
    @InjectView(R.id.gv_cate)
    GridView gv_cate;
    @InjectView(R.id.gv_beautiful)
    GridView gv_beautiful;
    @InjectView(R.id.gv_fun)
    GridView gv_fun;
    @InjectView(R.id.gv_play)
    GridView gv_play;
    @InjectView(R.id.gv_mom)
    GridView gv_mom;
    @InjectView(R.id.gv_more)
    GridView gv_more;
    @InjectView(R.id.rl_hot)
    RelativeLayout rl_hot;
    @InjectView(R.id.rl_collect)
    RelativeLayout rl_collect; //我的收藏

    private List<String> data;
    private List<String> data1;
    private RimGridViewAdapter rimGridViewAdapter;

    public static RimFragment newInstance() {
        RimFragment fragment = new RimFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rim, container, false);
        ButterKnife.inject(this, view);
        initView();
        initData();
        rimGridViewAdapter = new RimGridViewAdapter(data,getActivity());
        gv_hot.setAdapter(rimGridViewAdapter);
        gv_phone.setAdapter(rimGridViewAdapter);
        gv_serve.setAdapter(rimGridViewAdapter);
        gv_shop.setAdapter(rimGridViewAdapter);
        rimGridViewAdapter = new RimGridViewAdapter(data1,getActivity());
        gv_doctor.setAdapter(rimGridViewAdapter);
        gv_beautiful.setAdapter(rimGridViewAdapter);
        gv_fun.setAdapter(rimGridViewAdapter);
        gv_play.setAdapter(rimGridViewAdapter);
        gv_mom.setAdapter(rimGridViewAdapter);
        gv_more.setAdapter(rimGridViewAdapter);

        // gv_hot.setEnabled(true);
        gv_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("positon=========", position + "");
            }
        });
        return view;
    }

    private void initView() {
        rl_hot.setOnClickListener(this);
        rl_collect.setOnClickListener(this);
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0;i<12;i++){
            data.add(i,getResources().getString(R.string.item_one)+i);
        }
        data1 = new ArrayList<>();
        for (int i=0;i<8;i++){
            data1.add(i,getResources().getString(R.string.item_one)+i);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onClick(View v) {
        Intent intent=null;
        Object tag = v.getTag();
        switch (v.getId()){
            case R.id.rl_hot:
                intent = new Intent(getActivity(),ItemRimActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_collect:
                intent = new Intent(getActivity(),RimCollectServeActivity.class);
                startActivity(intent);
                break;
        }
    }

}
