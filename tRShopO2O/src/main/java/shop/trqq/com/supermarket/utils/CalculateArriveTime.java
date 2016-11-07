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
     * �����볬�еľ���ȥ��������ʱ��
     *
     * @return
     */
    public static String calculateDateByDistance(float distance) {

        Log.d("distance", "calculateDateByDistance: "+distance);
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
