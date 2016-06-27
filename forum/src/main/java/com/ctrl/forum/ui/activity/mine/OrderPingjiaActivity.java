package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.SetMemberLevel;
import com.ctrl.forum.dao.OrderDao;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 订单评价
 */
public class OrderPingjiaActivity extends AppToolBarActivity {
    private OrderDao odao;

    @InjectView(R.id.ratingBar)
    RatingBar ratingBar;
    @InjectView(R.id.good)
    TextView good;
    @InjectView(R.id.et_content)
    EditText et_content;
    @InjectView(R.id.tv_pingjia)
    TextView tv_pingjia;

    private String id,companyId,content;
    private Float level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pingjia);
        ButterKnife.inject(this);

        initData();
    }

    private void initData() {
        id = getIntent().getStringExtra("id");
        companyId = getIntent().getStringExtra("companyId");

        odao = new OrderDao(this);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                good.setText(SetMemberLevel.setLeveText(rating));
                level = rating*2;
            }
        });

        tv_pingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = et_content.getText().toString();
                if (content.length()> 19){
                    odao.requestEvalutionOrder(Arad.preferences.getString("memberId"),id,level+"",content,companyId);
                }else{
                    MessageUtils.showShortToast(getApplicationContext(),"输入的内容不能少于20字!");
                }
            }
        });

    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==7){
            MessageUtils.showShortToast(this,"评价成功");
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
    public String setupToolBarTitle() {return "订单状态";}

}
