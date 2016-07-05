package com.ctrl.forum.utils;


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间处理工具
 */
public class TimeUtils {
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    public static String timeFormat(long timeMillis, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    public static String formatPhotoDate(long time){
        return timeFormat(time, "yyyy-MM-dd");
    }

    public static String formatPhotoDate(String path){
        File file = new File(path);
        if(file.exists()){
            long time = file.lastModified();
            return formatPhotoDate(time);
        }
        return "1970-01-01";
    }

    public static String dateTime(String ctime){
        Long time=Long.parseLong(ctime);
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE) {
            return "刚刚";
        } else if (diff < 2 * MINUTE) {
            return "1分钟前";
        } else if (diff < 50 * MINUTE) {
            return diff / MINUTE + " 分钟前";
        } else if (diff < 90 * MINUTE) {
            return "1小时前";
        } else if (diff < 24 * HOUR) {
            return diff / HOUR + " 小时前";
        } else if (diff < 48 * HOUR) {
            return "昨天";
        } else {
            return diff / DAY + " 天前";
        }

    }

    /*
    * 毫秒转日期
    * */

    public static String date(Long ltiem){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ltiem);
        return formatter.format(calendar.getTime());
    }
    /*
    * 毫秒转日期
    * */

    public static String dates(Long ltiem){
        DateFormat formatter = new SimpleDateFormat("M"+"月"+"d"+"日");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ltiem);
        return formatter.format(calendar.getTime());
    }
}
