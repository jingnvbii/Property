package com.ctrl.forum.weixin.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ctrl.forum.base.Constant;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
		api.registerApp(Constant.APP_ID);
	}
}
