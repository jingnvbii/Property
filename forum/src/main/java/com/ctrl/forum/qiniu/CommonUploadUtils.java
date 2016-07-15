package com.ctrl.forum.qiniu;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CommonUploadUtils {

	final CountDownLatch signal = new CountDownLatch(1);
	private UploadManager uploadManager;
	private String key;
	private ResponseInfo info;
	private JSONObject resp;

	File f;

	private Handler handler;

	private int size = 4048;
	String expectKey = "r=" + size + "k";

	public CommonUploadUtils(Handler handler) {
		uploadManager = new UploadManager();
		this.handler = handler;
	}

	public void runUploadTest() {
		new MyUploadRunnalbe().run();
	}
	class MyUploadRunnalbe implements Runnable {
		public void run() {
			try {
				f = TempFile.createFile(size);
			} catch (IOException e) {
				e.printStackTrace();
			}
			expectKey = expectKey + System.currentTimeMillis() + "";
			uploadManager.put(f, expectKey, QiNiuConfig.token, new UpCompletionHandler() {
				public void complete(String k, ResponseInfo rinfo, JSONObject response) {
					Message msg = new Message();
					msg.obj = response;
					handler.handleMessage(msg);

					Log.i("qiniutest", k + rinfo);
					key = k;
					info = rinfo;
					resp = response;
					signal.countDown();
				}
			}, null);
		}
	}
}
