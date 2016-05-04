package com.ctrl.android.yinfeng.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.entity.Kind;
import com.ctrl.android.yinfeng.ui.ereport.EReportActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/23.
 */
public class SelectPicPopupWindow extends PopupWindow {


    private final ScrollerNumberPicker pv_report;
    private Button btn_confirm, btn_cancel;
    private View mMenuView;

    private String kindName;
    private List<Kind> data;
    private ArrayList<String> eventKindIdStrList=new ArrayList<>();
    private String kindId;
    public int kId;


    public SelectPicPopupWindow(final Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_window_type, null);
        btn_confirm = (Button) mMenuView.findViewById(R.id.btn_confirm);
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        pv_report=(ScrollerNumberPicker)mMenuView.findViewById(R.id.pv_report);

        //取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //确定按钮
        btn_confirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EReportActivity activity = (EReportActivity) context;
                activity.setKindId(data.get(kId).getId());
                dismiss();
            }
        });
        //设置按钮监听
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }


    public void setData(final List<Kind> data){
        this.data=data;
        for(int i=0;i<data.size();i++){
            eventKindIdStrList.add(data.get(i).getKindName());
        }
        pv_report.setData(eventKindIdStrList);
        pv_report.setDefault(0);
        pv_report.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {


            @Override
            public void endSelect(int id, String text) {

                kId=id;
/*
                Log.d("kindId","data.get(id).getId(): " + data.get(id).getId());
                Log.d("kindId","kind: " + kindId);*/
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
    }

}
