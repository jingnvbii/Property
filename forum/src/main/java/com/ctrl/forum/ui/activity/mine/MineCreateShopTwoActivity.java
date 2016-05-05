package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

public class MineCreateShopTwoActivity extends AppToolBarActivity implements View.OnClickListener{
    private EditText et_shop_name,et_address,et_apply_name,et_shop_phone;
    private TextView tv_num,tv_commit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_shop_two);

        init();
        et_shop_name.addTextChangedListener(mTextWatcher);

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
            tv_num.setText(s.length()+"/20");
        }
    };

    private void init() {
        et_shop_name = (EditText) findViewById(R.id.et_shop_name);
        et_address = (EditText) findViewById(R.id.et_shop_address);
        et_apply_name = (EditText) findViewById(R.id.et_apply_name);
        et_shop_phone = (EditText) findViewById(R.id.et_shop_phone);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_commit = (TextView) findViewById(R.id.tv_commit);

        et_shop_name.setOnClickListener(this);
        et_address.setOnClickListener(this);
        et_apply_name.setOnClickListener(this);
        et_shop_phone.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.apply_shop);}

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_commit:
                Intent intent = new Intent(this,MineCreateShopThreeActivity.class);
                startActivity(intent);
            break;
        }
    }
}
