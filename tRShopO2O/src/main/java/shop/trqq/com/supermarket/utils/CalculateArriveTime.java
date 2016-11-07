package shop.trqq.com.supermarket.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/11/6
 */
public class CalculateArriveTime {


    /**
     * 根据离超市的距离去计算配送时间
     *
     * @return
     */
    public static String calculateDateByDistance(float distance) {

        Log.d("distance", "calculateDateByDistance: "+distance);
        String arriveTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        long nowTime = calendar.getTime().getTime();

        // 五公里以内 三十分钟送达
        if (distance >= 0 && distance <= 5000) {
            long time = nowTime + 1800000;
            simpleDateFormat.applyPattern("立即送出（大约HH:mm送达）");
            arriveTime = simpleDateFormat.format(new Date(time));
        } else if (5000 <distance && distance <= 10000) {  //五公里到十公里之间
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),15,0,0);
            long specifiedTime = calendar.getTimeInMillis();

            // 下午三点前下单 6 个小时 送达
            if(nowTime < specifiedTime){
                long time = nowTime + 3600000*6;
                simpleDateFormat.applyPattern("立即送出（HH:mm之前送达）");
                arriveTime = simpleDateFormat.format(new Date(time));
            }else {
                // 下午三点以后 下单 明天 11：00 之前 送达
                calendar.add(Calendar.DAY_OF_YEAR,1);
                calendar.add(Calendar.HOUR,-4);
                String dayOfWeek = getDayOfWeek(calendar);
                simpleDateFormat.applyPattern("明天"+"（"+dayOfWeek+"）"+"HH:mm之前送达");
                arriveTime = simpleDateFormat.format(calendar.getTime());
            }
        }else {   //  超过十公里 直接快递   设置三天时间
            calendar.add(Calendar.DAY_OF_YEAR,3);
            String dayOfWeek = getDayOfWeek(calendar);
            simpleDateFormat.applyPattern("预计MM月dd日"+"（"+dayOfWeek+"）"+"前送达");
            arriveTime = simpleDateFormat.format(calendar.getTime());
        }
        return arriveTime;
    }

    /**
     * 得到当前日历是星期几
     * @param calendar
     */
    private static String getDayOfWeek(Calendar calendar) {
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        String day = null;
        switch (i){
            case 1:
                day = "周日";
                break;
            case 2:
                day = "周一";
                break;
            case 3:
                day = "周二";
                break;
            case 4:
                day = "周三";
                break;
            case 5:
                day = "周四";
                break;
            case 6:
                day = "周五";
                break;
            case 7:
                day = "周六";
                break;
        }
        return day;
    }

}
