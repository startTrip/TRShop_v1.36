package shop.trqq.com.supermarket.utils;

import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/11/6
 */
public class DistanceUtils implements OnGetGeoCoderResultListener {




    // 获取反地理编码对象
    private GeoCoder mGeoCoder = GeoCoder.newInstance();

    /**
     * 注册结果监听回调
     *
     * @param mOnResultMapListener
     */
    public void registerOnResult(OnResultMapListener mOnResultMapListener) {
        this.mOnResultMapListener = mOnResultMapListener;
    }

    private OnResultMapListener mOnResultMapListener;


    /**
     * 数据结果回传
     *
     * @author NapoleonBai
     *
     */
    public interface OnResultMapListener {

        public void onReverseGeoCodeResult(Map<String, Object> map);

        public void onGeoCodeResult(Map<String, Object> map);
    }

    /**
     * 进行反地理编码
     *
     * @param latitude
     *            纬度信息
     * @param lontitude
     *            经度信息
     */
    public void getAddress(double latitude, double lontitude) {
        LatLng mLatLng = new LatLng(latitude, lontitude);
        // 反地理编码请求参数对象
        ReverseGeoCodeOption mReverseGeoCodeOption = new ReverseGeoCodeOption();
        // 设置请求参数
        mReverseGeoCodeOption.location(mLatLng);
        // 发起反地理编码请求(经纬度->地址信息)
        mGeoCoder.reverseGeoCode(mReverseGeoCodeOption);

    }


    /**
     * 根据城市,地址信息进行地理编码
     *
     * @param city
     *            城市,不能为null
     * @param address
     *            详细地址,不为null
     */
    public void getLocation(String city, String address) {
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(address)) {
            return;
        }
        GeoCodeOption mGeoCodeOption = new GeoCodeOption();
        mGeoCodeOption.address(address);
        mGeoCodeOption.city(city);
        mGeoCoder.geocode(mGeoCodeOption);
        // 设置查询结果监听者
        mGeoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        // 用来存储获取的定位信息
         Map<String, Object> map = new HashMap<String, Object>();

        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            // 没有检测到结果
            map.put("noResult",1);
        }else {

            map.put("latLng", geoCodeResult.getLocation());
            map.put("address", geoCodeResult.getAddress());
        }
        if (mOnResultMapListener != null) {
            mOnResultMapListener.onGeoCodeResult(map);
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }



    /**
     * 根据离超市的距离去计算配送时间
     *
     * @return
     */
    public static String calculateDateByDistance(float distance) {

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
            Log.d("NNNNNNN", "calculateDateByDistance: "+arriveTime);

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
