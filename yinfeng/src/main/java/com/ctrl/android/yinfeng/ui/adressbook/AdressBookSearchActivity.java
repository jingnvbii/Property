package com.ctrl.android.yinfeng.ui.adressbook;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.base.AppToolBarActivity;
import com.ctrl.android.yinfeng.dao.ContactDao;
import com.ctrl.android.yinfeng.ui.adapter.AdressBookSearchAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * (关键字搜索通讯录) activity
 * Created by jason on 2015/12/15.
 */
public class AdressBookSearchActivity extends AppToolBarActivity {
    @InjectView(R.id.lv_search)//列表
    ListView lv_search;

    private ContactDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setContentView(R.layout.activity_adress_book_search);
        /**首次进入该页不让弹出软键盘*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.inject(this);
        init();
    }

    /**
     * 初始化执行的 方法
     * */
    private void init(){
        dao=new ContactDao(this);
        dao.requestContact3GroupList("", getIntent().getStringExtra("keyWord"));
    }


    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (2 == requestCode) {
            AdressBookSearchAdapter adapter = new AdressBookSearchAdapter(this);
            adapter.setList(dao.getListContactGroup3());
            lv_search.setAdapter(adapter);
        }
    }




    /**
     * header 左侧按钮
     * */
    @Override
    public boolean setupToolBarLeftButton(ImageView leftButton) {
        leftButton.setImageResource(R.mipmap.white_arrow_left_none);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MessageUtils.showShortToast(MallShopActivity.this, "AA");
                onBackPressed();
            }
        });
        return true;
    }

    /**
     * 页面标题
     * */
    @Override
    public String setupToolBarTitle() {
        return "通讯录";
    }




}
