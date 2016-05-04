package com.ctrl.android.yinfeng.ui.visit;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppHolder;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.dao.VisitDao;
import com.ctrl.android.yinfeng.utils.S;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 突发到访发布  activity
* */

public class VisitProruptionReleaseActivity extends AppToolBarActivity implements View.OnClickListener {
    @InjectView(R.id.et_visit_visitingpeople)//到访人姓名
    EditText et_visit_visitingpeople;
    @InjectView(R.id.et_visit_name)//拜访人姓名
    EditText et_visit_name;
    @InjectView(R.id.et_visit_stop)//车牌号
    EditText et_visit_stop;
    @InjectView(R.id.et_visit_people_num)//到访人数
    EditText et_visit_people_num;
    @InjectView(R.id.et_visit_stf2op)//预计停留时间
    EditText et_visit_stf2op;
    @InjectView(R.id.spinner_visit_building)//楼号
    Spinner spinner_visit_building;
    @InjectView(R.id.spinner_visit_unit)//单元号
    Spinner spinner_visit_unit;
    @InjectView(R.id.spinner_visit_room)//房间号
    Spinner spinner_visit_room;

    @InjectView(R.id.tv_notice_detail_confirm)//提交预约
    TextView tv_notice_detail_confirm;

    @InjectView(R.id.rb_man)//性别 男
    RadioButton rb_man;
    @InjectView(R.id.rb_woman)//性别 女
    RadioButton rb_woman;
   @InjectView(R.id.cb_drove)//是否驾车
    CheckBox cb_drove;




    private List<String> buildStr=new ArrayList<>();
    private List<String> unitStr=new ArrayList<>();
    private List<String> roomStr=new ArrayList<>();

    private VisitDao vdao;
    private ArrayAdapter<String> adapter;
    private String build;
    private String unit;
    private String room;

    private String gender;

    private String MAN="0";
    private String WOMAN="1";
    private String Drove;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.j_activity_visit_proruption_release);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.inject(this);
        init();
    }

    private void init(){
        vdao=new VisitDao(this);
        vdao.requestBuildingList(Arad.preferences.getString("communityId"));
        tv_notice_detail_confirm.setOnClickListener(this);

        cb_drove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_visit_stop.setClickable(true);
                    et_visit_stop.setEnabled(true);
                }else {
                    et_visit_stop.getText().clear();
                    et_visit_stop.setClickable(false);
                    et_visit_stop.setEnabled(false);
                }
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);


        if(2 == requestCode){
            MessageUtils.showShortToast(this, "添加成功");
            finish();
        }

        if(10==requestCode){
            buildStr=vdao.getListBuilding();
            adapter = new ArrayAdapter<String>(VisitProruptionReleaseActivity.this, R.layout.simple_spinner_item, buildStr);
            //设置下拉列表的风格
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           // adapter.setDropDownViewResource(R.layout.spinner_layout );
            spinner_visit_building.setAdapter(adapter);
            spinner_visit_building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    build = buildStr.get(position);
                    vdao.requestUnitList(AppHolder.getInstance().getStaffInfo().getCommunityId(), build);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        if(11==requestCode){
            unitStr=vdao.getListUnit();
            adapter = new ArrayAdapter<String>(VisitProruptionReleaseActivity.this, R.layout.simple_spinner_item, unitStr);

            //设置下拉列表的风格
           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //adapter.setDropDownViewResource(R.layout. spinner_layout );
            spinner_visit_unit.setAdapter(adapter);
            spinner_visit_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    unit=unitStr.get(position);
                    vdao.requestRoomList(AppHolder.getInstance().getStaffInfo().getCommunityId(),build,unit);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        if(12==requestCode){
            roomStr=vdao.getListRoom();
            adapter = new ArrayAdapter<String>(VisitProruptionReleaseActivity.this, R.layout.simple_spinner_item, roomStr);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           // adapter.setDropDownViewResource(R.layout.spinner_layout);
            spinner_visit_room.setAdapter(adapter);
            spinner_visit_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    room = roomStr.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // room=roomStr.get(0);
                }
            });
        }
    }


    @Override
    public void onClick(View v) {

        if(tv_notice_detail_confirm==v){
            if (checkInput()) {
                String communityId = Arad.preferences.getString("communityId");//具体问题具体分析
                String visitorName = S.getStr(et_visit_visitingpeople.getText().toString());
                String memberName = S.getStr(et_visit_name.getText().toString());
                String numberPlates = S.getStr(et_visit_stop.getText().toString());
                String residenceTime = S.getStr(et_visit_stf2op.getText().toString());
                String peopleNum = S.getStr(et_visit_people_num.getText().toString());
                vdao.requestAddVisit(
                        communityId,
                        build,
                        unit,
                        room,
                        visitorName,
                        memberName,
                        numberPlates,
                        residenceTime ,
                        peopleNum,
                        Drove,
                        gender,
                        "",
                        "",
                        "");
                showProgress(true);
            }

        }

    }

    @Override
    public String setupToolBarTitle() {
        return "突发到访";
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }


    private boolean checkInput(){


        if(S.isNull(et_visit_visitingpeople.getText().toString())){
            MessageUtils.showShortToast(this,"到访人不可为空");
            return false;
        }
        if(S.isNull(et_visit_name.getText().toString())){
            MessageUtils.showShortToast(this,"拜访人不可为空");
            return false;
        }
        if(S.isNull(et_visit_stop.getText().toString())&&cb_drove.isChecked()){
            MessageUtils.showShortToast(this,"车牌号为空");
            return false;
        }
        if(S.isNull(et_visit_stf2op.getText().toString())){
            MessageUtils.showShortToast(this,"预计停留时间为空");
            return false;
        }

        if(S.isNull(build)){
            MessageUtils.showShortToast(this,"楼号为空");
            return false;
        }
        if(S.isNull(unit)){
            MessageUtils.showShortToast(this,"单元号为空");
            return false;
        }
        if(S.isNull(room)){
            MessageUtils.showShortToast(this,"房间号为空");
            return false;
        }

        if(S.isNull(et_visit_people_num.getText().toString())){
            MessageUtils.showShortToast(this,"到访人数为空");
            return false;
        }

        if(rb_man.isChecked()){
            gender=MAN;
        }else {
            gender=WOMAN;
        }
        if(cb_drove.isChecked()){
            Drove="1";
        }else {
            Drove="0";
        }

        return true;
    }



}
