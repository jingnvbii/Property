package com.ctrl.forum.ui.activity.Invitation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_phone);
        ButterKnife.inject(this);
        // 隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
                Intent intent =new Intent();
                intent.putExtra("name",et_name.getText().toString().trim());
                intent.putExtra("adress",et_address.getText().toString().trim());
                intent.putExtra("tel",et_tel.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });


        return true;
    }


}
