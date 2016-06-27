package com.ctrl.forum.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;
import com.ctrl.forum.dao.KeyDao;
import com.ctrl.forum.entity.ItemValues;

/**
 * webView
 */
public class WebViewActivity extends AppToolBarActivity {

    private WebView webView;
    private Intent intent;
    private String data;
    private String title;
    private String itemKey;
    private KeyDao kdao;
    private ItemValues itemValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.webView);
        intent = getIntent();
        title = intent.getStringExtra("title");
        getData();
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
    public String setupToolBarTitle() {
        return title;
    }

    public void getData() {
        kdao = new KeyDao(this);
        switch (title){
            case "投稿协议":
                itemKey = "POST_PROTOCOL";
                break;
            case "用户使用协议":
                itemKey = "APP_PROTOCOL";
                break;
            case "等级有什么用":
                itemKey = "LEVEL_EFFECT";
                break;
            case "如何快速升级":
                itemKey = "HOW_STEP_UP";
                break;
            case "使用协议":
                itemKey = "APP_PROTOCOL";
                break;
        }
        kdao.ueryDictionary(itemKey);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        if (requestCode == 66) {
            itemValues = kdao.getItemValues();
            if (itemValues!=null){
                data = itemValues.getItemValue();
                webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);//单纯显示文字
                //显示图片可以写webView.loadDataWithBaseURL(baseUrl, string, "text/html", "utf-8", null);
            }
        }
    }
}
