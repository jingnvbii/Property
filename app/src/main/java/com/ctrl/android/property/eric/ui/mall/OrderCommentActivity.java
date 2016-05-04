package com.ctrl.android.property.eric.ui.mall;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ctrl.android.property.R;
import com.ctrl.android.property.base.AppToolBarActivity;
import com.ctrl.android.property.eric.util.StrConstant;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 订单评价
 * Created by Eric on 2015/11/18
 */
public class OrderCommentActivity extends AppToolBarActivity {

    @InjectView(R.id.order_describe_rating)//相符描述
    RatingBar order_describe_rating;
    @InjectView(R.id.order_send_rating)//发货速度
    RatingBar order_send_rating;
    @InjectView(R.id.order_service_rating)//服务态度
    RatingBar order_service_rating;

    @InjectView(R.id.comment_content)
    EditText comment_content;
    @InjectView(R.id.comment_btn)
    TextView comment_btn;

    private String TITLE = StrConstant.ORDER_COMMENT_TITLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_comment_activity);
        ButterKnife.inject(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //init();
    }

    private void init() {
        //
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        showProgress(false);

    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
    }

    @Override
    public String setupToolBarTitle() {
        return TITLE;
    }

    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.drawable.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

//    @Override
//    public boolean setupToolBarRightText(TextView mRightText) {
//        mRightText.setText(R.string.add);
//        mRightText.setTextColor(getResources().getColor(R.color.text_white));
//        mRightText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(OrderListActivity.this, VisitAddActivity.class);
//                startActivity(intent);
//                AnimUtil.intentSlidIn(OrderListActivity.this);
//            }
//        });
//        return true;
//    }


}
