package shop.trqq.com.util;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * �ַ����������߰�
 *
 * @author
 * @version 1.0
 * @created 2015-02-10
 */
@SuppressLint("SimpleDateFormat")
public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    // private final static SimpleDateFormat dateFormater = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // private final static SimpleDateFormat dateFormater2 = new
    // SimpleDateFormat("yyyy-MM-dd");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * ���ַ���תΪ��������
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * ���Ѻõķ�ʽ��ʾʱ��
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(long oldTime, long nowTime) {
        String timeStr = null;
        Date oldDate = new Date(oldTime);
        Date nowdate = new Date(nowTime);
        Calendar oldCalendar = Calendar.getInstance();
        Calendar nowCalendar = Calendar.getInstance();
        oldCalendar.setTime(oldDate);
        nowCalendar.setTime(nowdate);
        int oldYear = oldCalendar.get(Calendar.YEAR);// ��ȡ���
        int nowYear = nowCalendar.get(Calendar.YEAR);
        int oldMonth = oldCalendar.get(Calendar.MONTH);// ��ȡ��
        int oldDayOfMonth = oldCalendar.get(Calendar.DAY_OF_MONTH);// ��ȡ��
        int oldDayOfYear = oldCalendar.get(Calendar.DAY_OF_YEAR);
        int nowDayOfYear = nowCalendar.get(Calendar.DAY_OF_YEAR);
        int oldHour = oldCalendar.get(Calendar.HOUR_OF_DAY);
        int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
        int oldMinute = oldCalendar.get(Calendar.MINUTE);
        int nowMinute = nowCalendar.get(Calendar.MINUTE);
        if (nowYear > oldYear)
            timeStr = String.format(Locale.CHINA, "%d��%d��%d��", oldYear,
                    oldMonth, oldDayOfMonth);
        else if (nowDayOfYear - oldDayOfYear > 2)
            timeStr = String.format(Locale.CHINA, "%d��%d��", oldMonth,
                    oldDayOfMonth);
        else if (nowDayOfYear - oldDayOfYear > 1)
            timeStr = "ǰ��";
        else if (nowDayOfYear - oldDayOfYear > 0)
            timeStr = "����";
        else if (nowHour - oldHour > 0)
            timeStr = String.format(Locale.CHINA, "%dСʱǰ", nowHour - oldHour);
        else if (nowMinute - oldMinute > 0)
            timeStr = String.format(Locale.CHINA, "%d����ǰ", nowMinute
                    - oldMinute);
        else
            timeStr = "�ո�";
        return timeStr;
    }

    /**
     * ���Ѻõķ�ʽ��ʾʱ��
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = null;
        if (TimeZoneUtil.isInEasternEightZones()) {
            time = toDate(sdate);
        } else {
            time = TimeZoneUtil.transformTime(toDate(sdate),
                    TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());
        }
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // �ж��Ƿ���ͬһ��
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "����ǰ";
            else
                ftime = hour + "Сʱǰ";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "����ǰ";
            else
                ftime = hour + "Сʱǰ";
        } else if (days == 1) {
            ftime = "����";
        } else if (days == 2) {
            ftime = "ǰ��";
        } else if (days > 2 && days <= 10) {
            ftime = days + "��ǰ";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * �жϸ����ַ���ʱ���Ƿ�Ϊ����
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * ����long���͵Ľ��������
     *
     * @return
     */
    public static long getToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        curDate = curDate.replace("-", "");
        return Long.parseLong(curDate);
    }

    /**
     * �жϸ����ַ����Ƿ�հ״��� �հ״���ָ�ɿո��Ʊ�����س��������з���ɵ��ַ��� �������ַ���Ϊnull����ַ���������true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * �ж��ǲ���һ���Ϸ��ĵ����ʼ���ַ
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * �ַ���ת����
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * ����ת����
     *
     * @param obj
     * @return ת���쳣���� 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * ����ת����
     *
     * @param obj
     * @return ת���쳣���� 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * �ַ���ת����ֵ
     *
     * @param b
     * @return ת���쳣���� false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * ��һ��InputStream��ת�����ַ���
     *
     * @param is
     * @return
     */
    public static String toConvertString(InputStream is) {
        StringBuffer res = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader read = new BufferedReader(isr);
        try {
            String line;
            line = read.readLine();
            while (line != null) {
                res.append(line);
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                    isr.close();
                }
                if (null != read) {
                    read.close();
                    read = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
        return res.toString();
    }

    /**
     * �ж�һ���ַ��������ַ��Ƿ�Ϊ��ĸ
     *
     * @param str
     * @return
     */
    public static boolean isLetter(String str) {
        char c = str.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }
}
