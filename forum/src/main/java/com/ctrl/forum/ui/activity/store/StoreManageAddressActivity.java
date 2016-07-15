package com.ctrl.forum.ui.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.AddressDao;
import com.ctrl.forum.entity.Address;
import com.ctrl.forum.ui.adapter.StoreManageAddressListViewAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 商城管理收货地址 activity
* */

public class StoreManageAddressActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.rl_add_address)//新增地址
            RelativeLayout rl_add_address;
    @InjectView(R.id.lv_manage_address)//地址列表
            PullToRefreshListView lv_manage_address;
    private AddressDao adao;
    private StoreManageAddressListViewAdapter adapter;
    private int mposition;
    private Intent intent;
    private List<Address> listAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manage_address);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
    }

    private void initData() {
        adao = new AddressDao(this);
        adapter=new StoreManageAddressListViewAdapter(this);
        lv_manage_address.setAdapter(adapter);
        intent = new Intent();
    }

    private void initView() {
        rl_add_address.setOnClickListener(this);
        lv_manage_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adao.requestUpdateDefaultReceiveAddress(adao.getListAddress().get(position - 1).getIsDefault(),
                        adao.getListAddress().get(position - 1).getId(),
                        Arad.preferences.getString("memberId"));
                mposition=position-1;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adao.requestGetAddressList(Arad.preferences.getString("memberId"));
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 0) {
            listAddress=adao.getListAddress();
           // MessageUtils.showShortToast(this, "获取地址列表成功");
                adapter.setList(listAddress);
        }
        if (requestCode == 4) {
            if (getIntent().getFlags()!=888){
                MessageUtils.showShortToast(this, "设置默认地址成功");
            }
            adao.requestGetAddressList(Arad.preferences.getString("memberId"));
            intent.putExtra("id",adao.getListAddress().get(mposition).getId());
            intent.putExtra("province",adao.getListAddress().get(mposition).getProvince());
            intent.putExtra("city",adao.getListAddress().get(mposition).getCity());
            intent.putExtra("area",adao.getListAddress().get(mposition).getArea());
            intent.putExtra("name",adao.getListAddress().get(mposition).getReceiveName());
            intent.putExtra("tel",adao.getListAddress().get(mposition).getMobile());
            intent.putExtra("address",adao.getListAddress().get(mposition).getAddressBase()+adao.getListAddress().get(mposition).getAddressDetail());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_add_address:
                Intent intent = new Intent(this, StoreAddAddressActivity.class);
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                break;
        }
    }
    @Override
    public String setupToolBarTitle() {
        return "管理配送地址";
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.jiantou_left_white);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

}
