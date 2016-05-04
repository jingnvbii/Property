package com.ctrl.forum.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import android.util.Log;
//正则表达式工具类
public class RegexpUtil {
   /*
   * 判断是否是手机号
   * */
	public static boolean isMobileNO(String mobiles){
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		System.out.println(m.matches()+"---");
		return m.matches();
	}

	

}
