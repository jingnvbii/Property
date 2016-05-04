package com.ctrl.android.property.eric.ui.mall;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.property.R;
import com.ctrl.android.property.base.AppHolder;
import com.ctrl.android.property.base.AppToolBarActivity;
import com.ctrl.android.property.eric.entity.Address;
import com.ctrl.android.property.eric.ui.adapter.ListAddressAdapter;
import com.ctrl.android.property.eric.util.S;
import com.ctrl.android.property.eric.util.StrConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 支付方式 activity
 * Created by Eric on 2015/9/22.
 */
public class MallLocateActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.search_btn)//
    ImageView search_btn;
    @InjectView(R.id.locate_address)//定位地址
    TextView locate_address;

    @InjectView(R.id.listView)//
    ListView listView;

    private ListAddressAdapter listAddressAdapter;

    private String TITLE = StrConstant.LOCATION_TITLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.mall_locate_activity);
        ButterKnife.inject(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
    }

    private void init(){
        locate_address.setText(S.getStr(AppHolder.getInstance().getBdLocation().getAddrStr()));
        search_btn.setOnClickListener(this);

        listAddressAdapter = new ListAddressAdapter(this);
        listAddressAdapter.setList(getListAddress());
        listView.setAdapter(listAddressAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageUtils.showShortToast(MallLocateActivity.this,getListAddress().get(position).getName());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == search_btn){
            MessageUtils.showShortToast(this,"搜索");
        }
    }

    /**
     * header 左侧按钮
     * */
    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.drawable.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //MessageUtils.showShortToast(PayStyleActivity.this,"返回");
                onBackPressed();
            }
        });
        return true;
    }

    /**
     * 页面标题
     * */
    @Override
    public String setupToolBarTitle() {
        return TITLE;
    }


    /**
     * header 右侧按钮
     * */
//    @Override
//    public boolean setupToolBarRightButton(ImageView rightButton) {
//        rightButton.setImageResource(R.drawable.toolbar_home);
//        rightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toHomePage();
//            }
//        });
//        return true;
//    }


    private List<Address> getListAddress(){
        List<Address> listAddress = new ArrayList<>();

        for(int i = 1 ; i <= 10 ; i ++){
            Address a = new Address();
            a.setId("" + i);
            a.setName(i + "泺口服装城");
            listAddress.add(a);
        }

        return listAddress;
    }
}
