package shop.trqq.com.supermarket.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/12/28
 */
public class SystemUtils
{

    /**
     * ��ȡ�ֻ�����
     * @return
     */
    public static String getPhoneNumber(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    /**
     * ��ȡ�ֻ�IMEI��
     */
    public static String getPhoneIMEI(Context cxt) {
        TelephonyManager tm = (TelephonyManager) cxt
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
    /**
     * ��ȡ�ֻ�IMSI
     * @return
     */
    public static String getDeviceIMSI(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    /**
     * ��ȡ�ֻ�ϵͳSDK�汾
     *
     * @return ��API 17 �򷵻� 17
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * ��ȡϵͳ�汾
     *
     * @return ����2.3.3
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * ����ϵͳ���Ͷ���
     */
    public static void sendSMS(Context cxt, String smsBody) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        cxt.startActivity(intent);
    }
    /**
     * ����ϵͳ��绰
     *
     * @param activity
     * @param telString
     */
    public static boolean makeCall(Activity activity, String telString)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telString));
            activity.startActivity(intent);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
//	public static boolean makeCall(Activity activity, String telString)
//	{
//		try
//		{
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			intent.setData(Uri.parse("tel:" + telString));
//			activity.startActivity(intent);
//			return true;
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//
//		return false;
//	}

    /**
     * �ж������Ƿ�����
     */
    public static boolean checkNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;// �����Ƿ�����
    }

    /**
     * �����Ƿ����
     * @return
     */
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager mConnMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnMan.getActiveNetworkInfo();
        if (info == null) { return false; }
        return info.isConnected();
    }

//    /**
//     * ��wifi���������Ƿ���
//     */
//    public static boolean checkOnlyWifi(Context context) {
//        if (PreferenceHelper.readBoolean(context,
//                KJConfig.SETTING_FILE, KJConfig.ONLY_WIFI)) {
//            return isWiFi(context);
//        } else {
//            return true;
//        }
//    }

    /**
     * �ж��Ƿ�Ϊwifi����
     */
    public static boolean isWiFi(Context cxt) {
        ConnectivityManager cm = (ConnectivityManager) cxt
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // wifi��״̬��ConnectivityManager.TYPE_WIFI
        // 3G��״̬��ConnectivityManager.TYPE_MOBILE
        NetworkInfo.State state = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        return NetworkInfo.State.CONNECTED == state;
    }



    /**
     * �������뷨
     * @param a
     */
    public static void hideInputMethod(Activity a)
    {
        InputMethodManager imm = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
        {
            View focus = a.getCurrentFocus();
            if (focus != null)
            {
                IBinder binder = focus.getWindowToken();
                if (binder != null)
                {
                    imm.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }

    }

    /**
     * need < uses-permission android:name =��android.permission.GET_TASKS�� />
     * �ж��Ƿ�ǰ̨����
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * �жϵ�ǰӦ�ó����Ƿ��̨����
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context
                    .getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // ��̨����
                    return true;
                } else {
                    // ǰ̨����
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * ָ�������Ƿ�������
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPackageRunning(Context context, String packageName)
    {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = manager.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo taskInfo : list)
        {

            if (taskInfo.topActivity.getPackageName().equals(packageName)
                    || taskInfo.baseActivity.getPackageName().equals(packageName)) {

                return true; }
        }

        return false;
    }

    /**
     * app�Ƿ����
     * @param a
     * @param packageName
     * @return
     */
    public static boolean isAppExists(Activity a, String packageName)
    {

        PackageInfo packageInfo;
        try
        {
            packageInfo = a.getPackageManager().getPackageInfo(packageName, 0);

        }
        catch (PackageManager.NameNotFoundException e)
        {
            packageInfo = null;
            e.printStackTrace();
        }

        return packageInfo == null ? false : true;
    }


    /**
     * �ж��ֻ��Ƿ���˯��
     */
    public static boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        return isSleeping;
    }

    /**
     * ��װapk
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * ��ȡ��ǰ����汾��
     * @param
     * @return
     */
    public static String getVersionCode(Context ctx)
    {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pi;
        try
        {
            pi = pm.getPackageInfo(ctx.getPackageName(), 0);

            return String.valueOf(pi.versionCode);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * �ص�home����̨����
     */
    public static void goHome(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    /**
     * ��ǩ���ַ���ת������Ҫ��32λǩ��
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = { 48, 49, 50, 51, 52, 53, 54, 55,
                56, 57, 97, 98, 99, 100, 101, 102 };
        try {
            MessageDigest localMessageDigest = MessageDigest
                    .getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0;; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * ��ȡ�豸�Ŀ����ڴ��С
     *
     * @param cxt
     *            Ӧ�������Ķ���context
     * @return ��ǰ�ڴ��С
     */
    public static int getDeviceUsableMemory(Context cxt) {
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // ���ص�ǰϵͳ�Ŀ����ڴ�
        return (int) (mi.availMem / (1024 * 1024));
    }

    /**
     * �����̨���������
     *
     * @param cxt
     *            Ӧ�������Ķ���context
     * @return �����������
     */
    public static int gc(Context cxt) {
        long i = getDeviceUsableMemory(cxt);
        int count = 0; // ������Ľ�����
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        // ��ȡ�������е�service�б�
        List<ActivityManager.RunningServiceInfo> serviceList = am
                .getRunningServices(100);
        if (serviceList != null)
            for (ActivityManager.RunningServiceInfo service : serviceList) {
                if (service.pid == android.os.Process.myPid())
                    continue;
                try {
                    android.os.Process.killProcess(service.pid);
                    count++;
                } catch (Exception e) {
                    e.getStackTrace();
                    continue;
                }
            }

        // ��ȡ�������еĽ����б�
        List<ActivityManager.RunningAppProcessInfo> processList = am
                .getRunningAppProcesses();
        if (processList != null)
            for (ActivityManager.RunningAppProcessInfo process : processList) {
                // һ����ֵ����RunningAppProcessInfo.IMPORTANCE_SERVICE�Ľ��̶���ʱ��û�û��߿ս�����
                // һ����ֵ����RunningAppProcessInfo.IMPORTANCE_VISIBLE�Ľ��̶��Ƿǿɼ����̣�Ҳ�����ں�̨������
                if (process.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    // pkgList �õ��ý��������еİ���
                    String[] pkgList = process.pkgList;
                    for (String pkgName : pkgList) {
                        Log.d(SystemUtils.class.getName(),"======����ɱ��������" + pkgName);
                        try {
                            am.killBackgroundProcesses(pkgName);
                            count++;
                        } catch (Exception e) { // ��ֹ���ⷢ��
                            e.getStackTrace();
                            continue;
                        }
                    }
                }
            }
        Log.d(SystemUtils.class.getName(),"������" + (getDeviceUsableMemory(cxt) - i)
                + "M�ڴ�");
        return count;
    }


    /**
     * ǿ���˳�ϵͳ�����Ƽ�ʹ�ã�
     * @param a
     */
    public static void quitApplication(Activity a)
    {
        a.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    /**
     * ��ȡ���е�app�б�
     *
     * @param a
     * @return
     */
    public static List<ResolveInfo> parserAllAppList(Activity a)
    {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return a.getPackageManager().queryIntentActivities(intent, 0);
    }

    /**
     * ��ȡ����ip
     *
     * @return
     */
    public static String getExternalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface netIntf = en.nextElement();
                for (Enumeration<InetAddress> enumIp = netIntf.getInetAddresses(); enumIp.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIp.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address))
                    {
                        String ip = inetAddress.getHostAddress().toString();
                        Log.d("SystemUtils", "ip" + ip);
                        return ip;
                    }
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * �½�uuid
     *
     * @return
     */
    public static String newRandomUUID()
    {
        String uuidRaw = UUID.randomUUID().toString();
        return uuidRaw.replaceAll("-", "");
    }

}

