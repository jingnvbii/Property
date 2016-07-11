package com.ctrl.forum.ui.activity.mine;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.beanu.arad.widget.SlidingUpPanelLayout;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.MineStoreDao;
import com.ctrl.forum.entity.CouponsPackag;
import com.ctrl.forum.entity.MemberInfo;
import com.ctrl.forum.ui.adapter.MineMerchantCouponAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 现金卷赠与
 */
public class MineMerchantCouponActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.lv_content)
    ListView lv_content;

    private View view;
    private PopupWindow popupWindow;
    private TextView tv_phone,member_name;
    private Button give,bt_cancle;
    private MineStoreDao mdao;
    private MemberInfo memberInfo;
    private List<CouponsPackag> couponsPackags;
    private String phone;
    private  MineMerchantCouponAdapter mineMerchantCouponAdapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_merchant_coupon);
        ButterKnife.inject(this);

        mineMerchantCouponAdapter = new MineMerchantCouponAdapter(this);
        lv_content.setAdapter(mineMerchantCouponAdapter);
        mineMerchantCouponAdapter.setOnButton(this);

        mdao = new MineStoreDao(this);
        mdao.queryCouponsPackageByCompanyId(Arad.preferences.getString("companyId"));

        initPop();

    }

    //初始化弹窗
    private void initPop() {
        view = LayoutInflater.from(this).inflate(R.layout.popwindows_coupon,null);
        popupWindow = new PopupWindow(view, SlidingUpPanelLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.pop_bg));
        colorDrawable.setAlpha(40);
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setOutsideTouchable(true);

        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        member_name = (TextView) view.findViewById(R.id.member_name);
        give = (Button) view.findViewById(R.id.give);
        bt_cancle = (Button) view.findViewById(R.id.bt_cancle);

        give.setOnClickListener(this);
        bt_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Object id = v.getTag();
        switch (v.getId()){
            case R.id.bt_ok:
                position = (int)id;
                EditText et_phone = MineMerchantCouponAdapter.et_text;
                if (et_phone.getText().toString().equals("")){
                    MessageUtils.showShortToast(this,"手机号不能为空!");
                }else{
                    phone = et_phone.getText().toString();
                }
                mdao.getMemberName(phone);
                break;
            case R.id.bt_cancle:
                popupWindow.dismiss();
                break;
            case R.id.give:
                mdao.sendGiftVoucher(Arad.preferences.getString("companyId"),couponsPackags.get(position).getPackageId(),phone);
                popupWindow.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==5){
            memberInfo = mdao.getMemberInfo();
            if (memberInfo!=null){
                popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 0);  //在底部
                tv_phone.setText(phone);
                member_name.setText(memberInfo.getMemberName());
                popupWindow.update();
            }
        }
        if (requestCode==6){
            couponsPackags = mdao.getCouponsPackags();
            if (couponsPackags!=null){
                mineMerchantCouponAdapter.setList(couponsPackags);
            }
        }
        if (requestCode==7){
            MessageUtils.showShortToast(this,"赠与成功");
        }
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

    @Override
    public String setupToolBarTitle() {return "现金卷赠与";}
}
