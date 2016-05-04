package com.ctrl.android.property.staff.ui.device;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.beanu.arad.utils.AnimUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.property.staff.R;
import com.ctrl.android.property.staff.base.AppHolder;
import com.ctrl.android.property.staff.base.AppToolBarActivity;
import com.ctrl.android.property.staff.dao.DeviceDao;
import com.ctrl.android.property.staff.entity.DeviceCate;
import com.ctrl.android.property.staff.entity.Hotline;
import com.ctrl.android.property.staff.ui.adapter.DeviceAdapter;
import com.ctrl.android.property.staff.util.S;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 设备列表 activity
 * Created by Eric on 2015/11/26.
 */
public class DeviceActivity extends AppToolBarActivity implements View.OnClickListener{

    @InjectView(R.id.listView)//通讯列表
    ListView listView;

    @InjectView(R.id.keyword_text)
    EditText keyword_text;
    @InjectView(R.id.search_btn)//
    Button search_btn;

    private DeviceAdapter deviceAdapter;

    private DeviceDao deviceDao;
    private List<DeviceCate> listDeviceCate;

    private String TITLE = "设备养护";

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.device_activity);
        /**首次进入该页不让弹出软键盘*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.inject(this);
        init();
    }

    /**
     * 初始化执行的 方法
     * */
    private void init(){

        search_btn.setOnClickListener(this);

        deviceDao = new DeviceDao(this);
        showProgress(true);
        deviceDao.requestDeviceCategory(AppHolder.getInstance().getStaffInfo().getCommunityId(), "", "");


    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);
        if(0 == requestCode){
            listDeviceCate = deviceDao.getListDeviceCate();

            deviceAdapter = new DeviceAdapter(this);
            deviceAdapter.setList(listDeviceCate);
            listView.setAdapter(deviceAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    index = position;

                    String categoryId = listDeviceCate.get(index).getId();
                    String communityId = AppHolder.getInstance().getStaffInfo().getCommunityId();
                    String name = "";
                    String currentPage = "";
                    String rowCountPerPage = "";
                    showProgress(true);
                    deviceDao.requestDeviceList(categoryId,communityId,name,currentPage,rowCountPerPage);
                }
            });

        }

        if(1 == requestCode){

            //0:展开  1:不展开
            if(listDeviceCate.get(index).getFlg() == 0){
                listDeviceCate.get(index).setFlg(1);
            } else {
                listDeviceCate.get(index).setFlg(0);
            }

            listDeviceCate.get(index).setListDevice(deviceDao.getListDevice());
            deviceAdapter.setList(listDeviceCate);
            deviceAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
    }

    @Override
    public void onClick(View v) {
        if(v == search_btn){

            if(!S.isNull(keyword_text.getText().toString())){
                Intent intent = new Intent(this, DeviceActivity2.class);
                intent.putExtra("keyword",keyword_text.getText().toString());
                startActivity(intent);
                AnimUtil.intentSlidIn(this);
                finish();
            } else {
                MessageUtils.showShortToast(this, "请输入关键字");
            }


        }
    }

    /**
     * header 左侧按钮
     * */
    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.drawable.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MessageUtils.showShortToast(MallShopActivity.this, "AA");
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
//        rightButton.setImageResource(R.drawable.white_cross_icon);
//        rightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MessageUtils.showShortToast(EasyShopAroundActivity.this, "MORE");
//                //showProStyleListPop();
//            }
//        });
//        return true;
//    }

    private List<Hotline> getList(){
        List<Hotline> list = new ArrayList<>();

        for(int i = 0 ; i < 6 ; i ++){
            Hotline line = new Hotline();
            line.setCategory(i + "设备大分类");

            List<Map<String,String>> listmap = new ArrayList<>();
            for(int j = 0 ; j < (((int)(Math.random() * 10)) == 0 ? 1 : ((int)(Math.random() * 10))); j ++){
                Map<String,String> map = new HashMap<>();
                map.put("name",i + j + "设备名称");
                listmap.add(map);
            }

            line.setListMap(listmap);
            list.add(line);

        }

        return list;
    }

}
