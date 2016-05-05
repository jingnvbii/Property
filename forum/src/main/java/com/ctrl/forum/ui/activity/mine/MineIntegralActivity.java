package com.ctrl.forum.ui.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
/**
 * 积分商城
 */
public class MineIntegralActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.rl_last)
    RelativeLayout rl_last; //ʣ��
    @InjectView(R.id.rl_dui)
    RelativeLayout rl_dui; //�һ���¼
    @InjectView(R.id.rl_fen)
    RelativeLayout rl_fen; //��ּ�¼
    @InjectView(R.id.rl_shop)
    GridView rl_shop; //ʣ���б�
    @InjectView(R.id.lv_dui)
    ListView lv_dui; //�һ���¼�б�
    @InjectView(R.id.lv_fen)
    ListView lv_fen; //��ּ�¼�б�


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_integral);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        rl_last.setOnClickListener(this);
        rl_dui.setOnClickListener(this);
        rl_fen.setOnClickListener(this);

        rl_shop.setVisibility(View.VISIBLE);
        lv_dui.setVisibility(View.INVISIBLE);
        lv_fen.setVisibility(View.INVISIBLE);
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
    public String setupToolBarTitle() {return getResources().getString(R.string.integral_shop);}

    @Override
    public void onClick(View v) {
      if (v==rl_last){
          rl_shop.setVisibility(View.VISIBLE);
          lv_dui.setVisibility(View.INVISIBLE);
          lv_fen.setVisibility(View.INVISIBLE);
      }
        if (v==rl_dui){
            lv_dui.setVisibility(View.VISIBLE);
            rl_shop.setVisibility(View.INVISIBLE);
            lv_fen.setVisibility(View.INVISIBLE);
        }
        if (v==rl_fen){
            lv_fen.setVisibility(View.VISIBLE);
            lv_dui.setVisibility(View.INVISIBLE);
            rl_shop.setVisibility(View.INVISIBLE);
        }
    }
}
