package com.ctrl.forum.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class SysUtils {
	
	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	  
	  @SuppressWarnings("deprecation")
	public static int getScreenWidth(Activity activity){
		  int width = 0;
		  WindowManager windowManager = activity.getWindowManager();    
          Display display = windowManager.getDefaultDisplay();    
          width=display.getWidth();
		  return width;
	  }

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.0f);
	}


	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.0f);
	}


	public static int px2sp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.0f);
	}


	public static int sp2px(Context context, float spValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (spValue * scale + 0.0f);
	}

	
}
 