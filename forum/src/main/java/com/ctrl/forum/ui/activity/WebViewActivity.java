package com.ctrl.forum.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.ctrl.forum.R;
import com.ctrl.forum.base.AppToolBarActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebViewActivity extends AppToolBarActivity {

    private WebView webView;
    private Intent intent;
    private String data;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.webView);
        intent = getIntent();
        data = intent.getStringExtra("data");
        title = intent.getStringExtra("title");

        webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);//单纯显示文字
        //显示图片可以写webView.loadDataWithBaseURL(baseUrl, string, "text/html", "utf-8", null);
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
}
