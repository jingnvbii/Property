package com.ctrl.forum.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.base.SetMemberLevel;
import com.ctrl.forum.customview.MineHeadView;
import com.ctrl.forum.dao.MemberDao;
import com.ctrl.forum.entity.LevelInfo;
import com.ctrl.forum.ui.activity.WebViewActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 我的等级
 */
public class MineGradeActivity extends AppToolBarActivity implements View.OnClickListener{
    @InjectView(R.id.iv_header)
    MineHeadView iv_header;
    @InjectView(R.id.tv_stage) //todayExp
    TextView tv_stage;
    @InjectView(R.id.now_stage) //currentExp
    TextView now_stage;
    @InjectView(R.id.tv_MaxExp)
    TextView tv_MaxExp;  //dayMaxExp
    @InjectView(R.id.tv_end)  //nexeLevel
    TextView tv_end;
    @InjectView(R.id.tv_num)//nextLevelExp
    TextView tv_num;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id.iv_grade)
    TextView iv_grade;
    @InjectView(R.id.fast_stage)
    RelativeLayout fast_stage;  //如何快速升级
    @InjectView(R.id.use_grade)
    RelativeLayout use_grade; //等级有什么用
    @InjectView(R.id.pb_grade)
    ProgressBar pb_grade;

    private MemberDao mdao;
    private LevelInfo levelInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_grade);
        ButterKnife.inject(this);
        initView();
        initData();
        putViewData();
    }

    private void initData() {
        levelInfo = new LevelInfo();
        mdao = new MemberDao(this);
        mdao.getLevelInfo(Arad.preferences.getString("memberId"));
    }

    private void initView() {
        fast_stage.setOnClickListener(this);
        use_grade.setOnClickListener(this);

        tv_name.setText(Arad.preferences.getString("nickName"));
        String grad = Arad.preferences.getString("memberLevel"); //等级
        String imgUrl = Arad.preferences.getString("imgUrl");
        if (imgUrl!=null&&!imgUrl.equals(""))
           Arad.imageLoader.load(imgUrl).placeholder(getResources().getDrawable(R.mipmap.my_gray)).into(iv_header);//设置头像
        SetMemberLevel.setLevelImage(this, iv_grade, grad);//设置等级
    }

    public void putViewData(){
        tv_stage.setText(Arad.preferences.getInteger("todayExp")+"");
        now_stage.setText(Arad.preferences.getInteger("currentExp")+"");
        tv_MaxExp.setText("(每日上限"+Arad.preferences.getInteger("dayMaxExp")+"点)");
        tv_end.setText(Arad.preferences.getInteger("nexeLevel") + "");
        tv_num.setText(Arad.preferences.getInteger("nextLevelExp")+"");
        pb_grade.setMax(Arad.preferences.getInteger("nexeLevel"));
        pb_grade.setProgress(Arad.preferences.getInteger("currentExp"));
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
    public String setupToolBarTitle() {return getResources().getString(R.string.vip_grade);}

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fast_stage:
                Intent intent = new Intent(this,WebViewActivity.class);
                intent.putExtra("title", "如何快速升级");
                startActivity(intent);
                break;
            case R.id.use_grade:
                Intent intent1 = new Intent(this,WebViewActivity.class);
                intent1.putExtra("title", "等级有什么用");
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode==1){
            levelInfo = mdao.getLevelInfos();
            if (levelInfo!=null){
                int nextLevelExp  = levelInfo.getNextLevelExp()-levelInfo.getCurrentExp();
                Arad.preferences.putInteger("todayExp", levelInfo.getTodayExp());
                Arad.preferences.putInteger("currentExp", levelInfo.getCurrentExp());
                Arad.preferences.putInteger("dayMaxExp", levelInfo.getDayMaxExp());
                Arad.preferences.putInteger("nexeLevel",levelInfo.getNextLevelExp());
                Arad.preferences.putInteger("nextLevelExp", nextLevelExp);
                Arad.preferences.flush();
                putViewData();
            }
        }
    }

}
