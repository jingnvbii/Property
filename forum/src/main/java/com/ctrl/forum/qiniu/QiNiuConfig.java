package com.ctrl.forum.qiniu;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.PutPolicy;

import org.json.JSONException;

/**
 * Created by lixiaodaoaaa on 14/10/12.
 */
public final class QiNiuConfig {
    public static final String token = getToken();
    public static final String QINIU_AK = "c10sTG1vRW-TUqWcNYNqyXzp3mT1Vdi0Z9iLNIeI";
    public static final String QINIU_SK = "1CPR0zna7WEHduvP-fymmbKx158lA70cMOWmE9tB";
    public static final String QINIU_BUCKNAME = "ctrl";
    public static final String BASE_URL = "http://7xsf4j.com1.z0.glb.clouddn.com/";

    public static String getToken() {

		Mac mac = new Mac(QiNiuConfig.QINIU_AK, QiNiuConfig.QINIU_SK);
        PutPolicy putPolicy = new PutPolicy(QiNiuConfig.QINIU_BUCKNAME);
		putPolicy.returnBody = "{\"name\": $(fname),\"size\": \"$(fsize)\",\"w\": \"$(imageInfo.width)\",\"h\": \"$(imageInfo.height)\",\"key\":$(etag)}";
		try {
			String uptoken = putPolicy.token(mac);
			System.out.println("debug:uptoken = " + uptoken);
			return uptoken;
		} catch (AuthException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

            return null;
    }
}
