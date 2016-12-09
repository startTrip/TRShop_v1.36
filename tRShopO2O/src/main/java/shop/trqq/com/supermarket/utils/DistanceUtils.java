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




    // ��ȡ������������
    private GeoCoder mGeoCoder = GeoCoder.newInstance();

    /**
     * ע���������ص�
     *
     * @param mOnResultMapListener
     */
    public void registerOnResult(OnResultMapListener mOnResultMapListener) {
        this.mOnResultMapListener = mOnResultMapListener;
    }

    private OnResultMapListener mOnResultMapListener;


    /**
     * ���ݽ���ش�
     *
     * @author NapoleonBai
     *
     */
    public interface OnResultMapListener {

        public void onReverseGeoCodeResult(Map<String, Object> map);

        public void onGeoCodeResult(Map<String, Object> map);
    }

    /**
     * ���з��������
     *
     * @param latitude
     *            γ����Ϣ
     * @param lontitude
     *            ������Ϣ
     */
    public void getAddress(double latitude, double lontitude) {
        LatLng mLatLng = new LatLng(latitude, lontitude);
        // ��������������������
        ReverseGeoCodeOption mReverseGeoCodeOption = new ReverseGeoCodeOption();
        // �����������
        mReverseGeoCodeOption.location(mLatLng);
        // ���𷴵����������(��γ��->��ַ��Ϣ)
        mGeoCoder.reverseGeoCode(mReverseGeoCodeOption);

    }


    /**
     * ���ݳ���,��ַ��Ϣ���е������
     *
     * @param city
     *            ����,����Ϊnull
     * @param address
     *            ��ϸ��ַ,��Ϊnull
     */
    public void getLocation(String city, String address) {
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(address)) {
            return;
        }
        GeoCodeOption mGeoCodeOption = new GeoCodeOption();
        mGeoCodeOption.address(address);
        mGeoCodeOption.city(city);
        mGeoCoder.geocode(mGeoCodeOption);
        // ���ò�ѯ���������
        mGeoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        // �����洢��ȡ�Ķ�λ��Ϣ
         Map<String, Object> map = new HashMap<String, Object>();

        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            // û�м�⵽���
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
     * �����볬�еľ���ȥ��������ʱ��
     *
     * @return
     */
    public static String calculateDateByDistance(float distance) {

        String arriveTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        long nowTime = calendar.getTime().getTime();

        // �幫������ ��ʮ�����ʹ�
        if (distance >= 0 && distance <= 5000) {
            long time = nowTime + 1800000;
            simpleDateFormat.applyPattern("�����ͳ�����ԼHH:mm�ʹ");
            arriveTime = simpleDateFormat.format(new Date(time));
        } else if (5000 <distance && distance <= 10000) {  //�幫�ﵽʮ����֮��

            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),15,0,0);
            long specifiedTime = calendar.getTimeInMillis();

            // ��������ǰ�µ� 6 ��Сʱ �ʹ�
            if(nowTime < specifiedTime){
                long time = nowTime + 3600000*6;
                simpleDateFormat.applyPattern("�����ͳ���HH:mm֮ǰ�ʹ");
                arriveTime = simpleDateFormat.format(new Date(time));
            }else {
                // ���������Ժ� �µ� ���� 11��00 ֮ǰ �ʹ�
                calendar.add(Calendar.DAY_OF_YEAR,1);
                calendar.add(Calendar.HOUR,-4);
                String dayOfWeek = getDayOfWeek(calendar);
                simpleDateFormat.applyPattern("����"+"��"+dayOfWeek+"��"+"HH:mm֮ǰ�ʹ�");
                arriveTime = simpleDateFormat.format(calendar.getTime());
            }
            Log.d("NNNNNNN", "calculateDateByDistance: "+arriveTime);

        }else {   //  ����ʮ���� ֱ�ӿ��   ��������ʱ��
            calendar.add(Calendar.DAY_OF_YEAR,3);
            String dayOfWeek = getDayOfWeek(calendar);
            simpleDateFormat.applyPattern("Ԥ��MM��dd��"+"��"+dayOfWeek+"��"+"ǰ�ʹ�");
            arriveTime = simpleDateFormat.format(calendar.getTime());
        }
        return arriveTime;
    }

    /**
     * �õ���ǰ���������ڼ�
     * @param calendar
     */
    private static String getDayOfWeek(Calendar calendar) {
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        String day = null;
        switch (i){
            case 1:
                day = "����";
                break;
            case 2:
                day = "��һ";
                break;
            case 3:
                day = "�ܶ�";
                break;
            case 4:
                day = "����";
                break;
            case 5:
                day = "����";
                break;
            case 6:
                day = "����";
                break;
            case 7:
                day = "����";
                break;
        }
        return day;
    }
}
