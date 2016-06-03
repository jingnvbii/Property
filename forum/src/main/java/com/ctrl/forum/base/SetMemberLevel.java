package com.ctrl.forum.base;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.ctrl.forum.R;

/**
 * 设置会员等级
 * Created by Administrator on 2016/5/20.
 */
public class SetMemberLevel {
    public static void setLevelImage(Context context,ImageView iv_grade,String memberLevel) {
        if (memberLevel != null) {
            if (memberLevel.equals("")) {
                iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon_un));
            } else {
                int grade = Integer.parseInt(memberLevel);
                switch (grade) {
                    case 8://月
                        iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon_month));
                        break;
                    case 9://季
                        iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon_season));
                        break;
                    case 10://年
                        iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon_year));
                        break;
                    default:
                        iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon + grade));
                        break;
                }
            }
        }else{
            iv_grade.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_icon_un));
        }
    }
}
