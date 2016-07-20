package com.ctrl.forum.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/7/18.
 */
public class DownImage {
    /**
     * 获取网络图片
     *
     * @param urlString 如：http://f.hiphotos.baidu.com/image/w%3D2048/sign=3
     *                  b06d28fc91349547e1eef6462769358
     *                  /d000baa1cd11728b22c9e62ccafcc3cec2fd2cd3.jpg
     * @return
     * @date 2014.05.10
     */
    public static Bitmap getNetWorkBitmap(String urlString) throws Exception {
        Bitmap bitmap = null;
        try{
            InputStream in = new java.net.URL(urlString).openStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("bitmap==========",bitmap.toString());
        return bitmap;
    }

}
