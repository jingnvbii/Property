package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.widget.SlidingUpPanelLayout;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

/**
 * 编辑
 */
public class MineEditActivity extends AppToolBarActivity implements View.OnClickListener{
    private RelativeLayout update_head,rl_ni,rl_jianjie,rl_phone,rl_xiaoqu,rl_pwd;
    private TextView take_picture,choose_phone,cancel;
    private View view;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        initPop();

    }

    private void initPop() {
        view = LayoutInflater.from(this).inflate(R.layout.update_head,null);
        popupWindow = new PopupWindow(view, SlidingUpPanelLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.pop_bg));
        colorDrawable.setAlpha(40);
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setOutsideTouchable(true);

        take_picture = (TextView) view.findViewById(R.id.take_picture);
        choose_phone = (TextView) view.findViewById(R.id.choose_phone);
        cancel = (TextView) view.findViewById(R.id.cancel);
        take_picture.setOnClickListener(this);
        choose_phone.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void init() {
        update_head = (RelativeLayout) findViewById(R.id.update_head);
        rl_ni = (RelativeLayout) findViewById(R.id.rl_ni);
        rl_jianjie = (RelativeLayout) findViewById(R.id.rl_jianjie);
        rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
        rl_xiaoqu = (RelativeLayout) findViewById(R.id.rl_xiaoqu);
        rl_pwd = (RelativeLayout) findViewById(R.id.rl_pwd);

        update_head.setOnClickListener(this);
        rl_ni.setOnClickListener(this);
        rl_jianjie.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_xiaoqu.setOnClickListener(this);
        rl_pwd.setOnClickListener(this);

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
    public String setupToolBarTitle() {return getResources().getString(R.string.bianji);}

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent =null;
        switch (id){
            //修改头像
            case R.id.update_head:
                popupWindow.showAtLocation(this.view, Gravity.BOTTOM, 0, 0);  //弹出
                popupWindow.update();
                break;
            case R.id.rl_ni://昵称
                intent= new Intent(getApplicationContext(), MineNickNameActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_jianjie://签名
                intent = new Intent(getApplicationContext(),MineSignActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_phone://
                break;
            case R.id.rl_xiaoqu://小区
                break;
            case R.id.rl_pwd://修改密码
                intent = new Intent(getApplicationContext(),MineUpdatepwdActivity.class);
                startActivity(intent);
                break;
            case R.id.take_picture: //拍照片
                break;
            case R.id.choose_phone: //选择照片
                break;
            case R.id.cancel: //取消
                popupWindow.dismiss();
                break;
        }
    }


}
