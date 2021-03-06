package com.ctrl.forum.ui.activity.Invitation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.utils.InputMethodUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
* 添加联系人电话 activity
* */

public class AddContactPhoneActivity extends AppToolBarActivity {
    @InjectView(R.id.et_name)//名称
    EditText et_name;
    @InjectView(R.id.et_address)//地址
    EditText et_address;
    @InjectView(R.id.et_tel)//电话
    EditText et_tel;
    @InjectView(R.id.rl_all)
    RelativeLayout rl_all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_phone);
        ButterKnife.inject(this);
        initView();

        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void initView() {
        Intent intent = getIntent();
        et_address.setText(intent.getStringExtra("adress"));
        et_name.setText(intent.getStringExtra("name"));
        et_tel.setText(intent.getStringExtra("tel"));

        rl_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtils.hide(AddContactPhoneActivity.this);
            }
        });
    }


    @Override
    public boolean setupToolBarLeftText(TextView mLeftText) {
        mLeftText.setText("取消");
        mLeftText.setTextColor(getResources().getColor(R.color.text_blue));
        mLeftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {
        TextView tv_title = getmTitle();
        tv_title.setTextColor(getResources().getColor(R.color.text_black));
        return "添加联系电话";
    }

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText("保存");
        mRightText.setTextColor(getResources().getColor(R.color.text_blue));
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPhone()) {
                    Intent intent = new Intent();
                    intent.putExtra("name", et_name.getText().toString().trim());
                    intent.putExtra("adress", et_address.getText().toString().trim());
                    intent.putExtra("tel", et_tel.getText().toString().trim());
                    setResult(RESULT_OK, intent);
                    // 隐藏输入法
                    InputMethodUtils.hide(AddContactPhoneActivity.this);
                    finish();
                }
            }
        });
        return true;
    }

    public boolean checkPhone(){
        if(TextUtils.isEmpty(et_tel.getText())){
            return true;
        }
        if (et_tel.getText().length()==11){
            return true;
        }
        MessageUtils.showShortToast(this, "电话格式不对");
        return false;
    }

}
