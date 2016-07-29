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
 * 修改昵称
 */
public class MineNickNameActivity extends AppToolBarActivity {
    private EditText et_nickname;
    private TextView tv_ni;
    private EditDao edao;
    private String id;
    private LinearLayout ll_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        init();

        ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtils.hide(MineNickNameActivity.this);
            }
        });
    }

    private void init() {
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        tv_ni = (TextView) findViewById(R.id.tv_ni);
        ll_all = (LinearLayout) findViewById(R.id.ll_all);
        et_nickname.setText(Arad.preferences.getString("nickName"));
        tv_ni.setText("昵称"+et_nickname.length()+"/11");

        edao = new EditDao(this);
        id =  Arad.preferences.getString("memberId");

        et_nickname.addTextChangedListener(mTextWatcher);
    }


    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            tv_ni.setText(s.length() + "/11");
            if (s.length()==11){
                MessageUtils.showShortToast(getApplicationContext(),"已输入11位,不能继续输入!");
            }
        }
    };

    public Boolean checkInput(){
        if (TextUtils.isEmpty(et_nickname.getText().toString().trim())){
            MessageUtils.showShortToast(this,"昵称不能为空");
            return false;
        }
        return true;
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
        return getResources().getString(R.string.nickname);
    }

    @Override
    public boolean setupToolBarRightText(TextView mRightText) {
        mRightText.setText(getResources().getString(R.string.name_ok));
        mRightText.setTextColor(Color.WHITE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (checkInput()){
                   edao.requestChangeNickName(et_nickname.getText().toString().trim(),id);
               }
            }
        });
        return true;
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 0){
            MessageUtils.showShortToast(this,"修改昵称成功");
            Arad.preferences.putString("nickName", et_nickname.getText().toString().trim());
            Arad.preferences.flush();
            finish();
        }
    }
}
