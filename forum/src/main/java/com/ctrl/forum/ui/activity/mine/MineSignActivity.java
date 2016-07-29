package com.ctrl.forum.ui.activity.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.EditDao;
import com.ctrl.forum.utils.InputMethodUtils;

/**
 * 我的签名
 */
public class MineSignActivity extends AppToolBarActivity {
    private TextView tv_sign;
    private EditText et_sign;
    private EditDao edao;
    private String id;
    private LinearLayout ll_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_sign);
        init();
    }

    private void init() {
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        et_sign = (EditText) findViewById(R.id.et_sign);
        ll_all = (LinearLayout) findViewById(R.id.ll_all);
        et_sign.setText(Arad.preferences.getString("remark"));
        et_sign.addTextChangedListener(mTextWatcher);
        edao = new EditDao(this);
        id = Arad.preferences.getString("memberId");

        ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtils.hide(MineSignActivity.this);
            }
        });
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {}
        @Override
        public void afterTextChanged(Editable s) {
            tv_sign.setText(getResources().getString(R.string.mysign)+s.length()+"/50");}
    };

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
        return getResources().getString(R.string.signname);
    }

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText(getResources().getString(R.string.name_ok));
        mRightText.setTextColor(Color.WHITE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_sign.getText().toString().trim())){
                    MessageUtils.showShortToast(getApplicationContext(),"签名内容不能为空!");
                }else{edao.requestChangeRemark(et_sign.getText().toString().trim(), id);}
            }
        });
        return true;
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==7){
            MessageUtils.showShortToast(this,"修改签名成功");
            Arad.preferences.putString("remark", et_sign.getText().toString().trim());
            Arad.preferences.flush();
            this.finish();
        }
    }
}
