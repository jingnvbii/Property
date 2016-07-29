package com.ctrl.forum.ui.activity.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.SetDao;
import com.ctrl.forum.utils.InputMethodUtils;

/**
 * 意见反馈
 */
public class MineFeedBackActivity extends AppToolBarActivity {
    private SetDao sdao;
    private EditText et_content;
    private EditText et_phone;
    private LinearLayout ll_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_feed);

        initView();
    }

    private void initView() {
        sdao = new SetDao(this);

        et_content = (EditText) findViewById(R.id.et_content);
        et_phone = (EditText) findViewById(R.id.et_phone);
        ll_all = (LinearLayout) findViewById(R.id.ll_all);

        ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtils.hide(MineFeedBackActivity.this);
            }
        });
    }
    //检查输入内容是否为空
    public boolean checkInput(){
        if (TextUtils.isEmpty(et_phone.getText().toString())){
            MessageUtils.showShortToast(this, "手机号不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(et_content.getText().toString())){
            MessageUtils.showShortToast(this, "输入内容不能为空!");
            return false;
        }
        if (et_phone.getText().length()==11){
            return true;
        }
        if (et_phone.getText().length()<11){
            MessageUtils.showShortToast(this, "手机号格式不正确!");
            return false;
        }

        return  true;
    }

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
    public String setupToolBarTitle() {
        return getResources().getString(R.string.feedback);
    }

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("发送");
        mRightText.setTextColor(Color.WHITE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    sdao.opinionFeedback(Arad.preferences.getString("memberId"), et_content.getText().toString(), et_phone.getText().toString());
                }
            }
        });
        return true;
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==0){
            MessageUtils.showShortToast(this, "发送成功");
            finish();
        }
    }
}
