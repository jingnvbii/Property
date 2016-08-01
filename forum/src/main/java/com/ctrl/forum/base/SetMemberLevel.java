package com.ctrl.forum.base;

import android.content.Context;
import android.widget.TextView;

/**
 * 设置会员等级
 * Created by Administrator on 2016/5/20.
 */
public class SetMemberLevel {

    //设置等级图片
    public static void setLevelImage(Context context,TextView iv_grade,String memberLevel) {
        if (memberLevel != null) {
            if (memberLevel.equals("")) {
                iv_grade.setText("LV.0");
                //iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon));
            } else {
                int grade = Integer.parseInt(memberLevel);
                switch (grade) {
                   /* case 8://月
                        //iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon_month));
                        break;
                    case 9://季
                        iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon_season));
                        break;
                    case 10://年
                        iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon_year));
                        break;*/
                    default:
                        iv_grade.setText("LV."+grade);
                        //iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon + grade));
                        break;
                }
            }
        }else{
            iv_grade.setText("LV.0");
           // iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon));
        }
    }

    //设置评价等级状态(除以2之后传过来)
    public static String setLeveText(float level){
        switch (level+""){
            case "0.5":
                return "差";
            case "1.0":
                return "差";
            case "1.5":
                return "一般";
            case "2.0":
                return "一般";
            case "2.5":
                return "好";
            case "3.0":
                return "好";
            case "3.5":
                return "很好";
            case "4.0":
                return "很好";
            case "4.5":
                return "非常棒";
            case "5.0":
                return "非常棒";
            default:
                return "";
        }
    }
}
