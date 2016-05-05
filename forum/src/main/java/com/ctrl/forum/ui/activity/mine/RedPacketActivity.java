package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ctrl.forum.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class RedPacketActivity extends ActionBarActivity implements View.OnClickListener{
    @InjectView(R.id.et_phone)
    EditText et_phone; //�绰����
    @InjectView(R.id.iv_get)
    ImageView iv_get; //�����ȡ
    @InjectView(R.id.rl_friend)
    RelativeLayout rl_friend;//������ȡ�����
    @InjectView(R.id.rl_juan)
    RelativeLayout rl_juan;

    private String phone_num;//����ĵ绰����
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet);
        ButterKnife.inject(this);
        intiView();
    }

    //��ʼ���ؼ�
    private void intiView() {
        iv_get.setOnClickListener(this);
        rl_friend.setVisibility(View.INVISIBLE);
        rl_juan.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View v) {
        if (v==iv_get){
            iv_get.setVisibility(View.INVISIBLE);
            et_phone.setVisibility(View.INVISIBLE);
            phone_num = et_phone.getText().toString();
            rl_friend.setVisibility(View.VISIBLE);//��ʾ������ȡ���
            rl_juan.setVisibility(View.VISIBLE);
        }
    }
}
