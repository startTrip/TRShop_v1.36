package shop.trqq.com.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Toast������
 *
 * @version 2012-5-21 ����9:21:01
 */
public class ToastUtils {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static Object synObj = new Object();

    /**
     * Toast������Ϣ��Ĭ��Toast.LENGTH_SHORT
     *
     * @param act
     * @param msg
     * @version 2012-5-22 ����11:13:10
     */
    public static void showMessage(Context act, String msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    /**
     * Toast������Ϣ��Ĭ��Toast.LENGTH_LONG
     *
     * @param act
     * @param msg
     * @version 2012-5-22 ����11:13:10
     */
    public static void showMessageLong(Context act, String msg) {
        showMessage(act, msg, Toast.LENGTH_LONG);
    }

    /**
     * Toast������Ϣ��Ĭ��Toast.LENGTH_SHORT
     *
     * @param act
     * @param msg
     * @version 2012-5-22 ����11:13:35
     */
    public static void showMessage(Context act, int msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    /**
     * Toast������Ϣ��Ĭ��Toast.LENGTH_LONG
     *
     * @param act
     * @param msg
     * @version 2012-5-22 ����11:13:35
     */
    public static void showMessageLong(Context act, int msg) {
        showMessage(act, msg, Toast.LENGTH_LONG);
    }

    /**
     * Toast������Ϣ
     *
     * @param act
     * @param msg
     * @param len
     * @version 2012-5-22 ����11:14:09
     */
    public static void showMessage(final Context act, final int msg,
                                   final int len) {
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (synObj) {
                            if (toast != null) {
                                // toast.cancel();
                                toast.setText(msg);
                                toast.setDuration(len);
                            } else {
                                toast = Toast.makeText(act, msg, len);
                            }
                            toast.show();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * Toast������Ϣ
     *
     * @param act
     * @param msg
     * @param len
     * @version 2012-5-22 ����11:14:27
     */
    public static void showMessage(final Context act, final String msg,
                                   final int len) {
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (synObj) {

                            {
                                try {
                                    toast = Toast.makeText(act, msg, len);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            toast.show();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * �رյ�ǰToast
     *
     * @version 2012-5-22 ����11:14:45
     */
    public static void cancelCurrentToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}