package com.ctrl.forum.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.ctrl.forum.R;


/**
 * Created by jason on 2016/1/8.
 */
public class ShareDialog {

    private final TextView tv_qq;
    private final TextView tv_weixin;
    private final TextView tv_pengyouquan;
    private final TextView tv_sinaweibo;
    private final TextView tv_tecentweibo;
    private final TextView tv_email;
    private final TextView tv_duanxin;
    private AlertDialog dialog;
    private TextView cancelButton;
    private String[] name = {"QQ好友", "微信好友"};

    public ShareDialog(Context context) {

        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM); // 非常重要：设置对话框弹出的位置

        window.setContentView(R.layout.share_dialog);
        cancelButton = (TextView) window.findViewById(R.id.share_cancel);
        tv_weixin = (TextView) window.findViewById(R.id.tv_weixin);
        tv_pengyouquan = (TextView) window.findViewById(R.id.tv_pengyouquan);
        tv_sinaweibo = (TextView) window.findViewById(R.id.tv_sinaweibo);
        tv_qq = (TextView) window.findViewById(R.id.tv_qq);
        tv_tecentweibo = (TextView) window.findViewById(R.id.tv_tecentweibo);
        tv_email = (TextView) window.findViewById(R.id.tv_email);
        tv_duanxin = (TextView) window.findViewById(R.id.tv_duanxin);
    }

    public void setCancelButtonOnClickListener(View.OnClickListener Listener) {
        cancelButton.setOnClickListener(Listener);
    }

    public void setQQButtonOnClickListener(View.OnClickListener Listener) {
        tv_qq.setOnClickListener(Listener);
    }

    public void setWeixinButtonOnClickListener(View.OnClickListener Listener) {
        tv_weixin.setOnClickListener(Listener);
    }
    public void setPengYouQuanButtonOnClickListener(View.OnClickListener Listener) {
        tv_pengyouquan.setOnClickListener(Listener);
    }
    public void setSinaWeiBoButtonOnClickListener(View.OnClickListener Listener) {
        tv_sinaweibo.setOnClickListener(Listener);
    }
    public void setTecentWeiBoButtonOnClickListener(View.OnClickListener Listener) {
        tv_tecentweibo.setOnClickListener(Listener);
    }
    public void setEmailButtonOnClickListener(View.OnClickListener Listener) {
        tv_email.setOnClickListener(Listener);
    }
    public void setDuanXinButtonOnClickListener(View.OnClickListener Listener) {
        tv_duanxin.setOnClickListener(Listener);
    }


    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }
}
