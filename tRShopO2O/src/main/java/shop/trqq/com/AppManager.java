package shop.trqq.com;

import android.app.Activity;

import java.util.Stack;

import shop.trqq.com.util.YkLog;

/**
 * Ӧ�ó���Activity�����ࣺ����Activity�����Ӧ�ó����˳�
 *
 * @author
 * @version 1.0
 * @created 2015-02-10
 */
public class AppManager {

    private static final String TAG = "AppManager";
    // Activity��ջ
    private static Stack<Activity> activityStack;
    // Ӧ�ù������
    private static AppManager instance;

    private AppManager() {
        YkLog.i(TAG, "��ʼ��Ӧ�ù������");
    }

    /**
     * ��һʵ��
     *
     * @return Ӧ�ù������
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * �Ƴ�����Activity
     */
    public void popAllActivity() {
        YkLog.i(TAG, "�Ƴ�����Activity");
        while (true) {
            if (activityStack.size() == 0)
                break;
            Activity activity = activityStack.lastElement();
            if (activity == null)
                break;
            popActivity(activity);
        }
    }

    /**
     * �Ƴ�ָ��Activity
     */
    public void popActivity(Activity activity) {
        YkLog.i(TAG, "�Ƴ�ָ��Activity��" + activity.getClass().getSimpleName());
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * ��ȡ��ǰActivity����ջ�����һ��ѹ��ģ�
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        YkLog.i(TAG, "��ȡ��һ��Activity��" + activity.getClass().getSimpleName());
        return activity;
    }

    /**
     * ��ȡ��ǰActivityString����ջ�����һ��ѹ��ģ�
     */
    public String currentActivityString() {
        Activity activity = activityStack.lastElement();
        YkLog.i(TAG, "��ȡ��һ��Activity��" + activity.getClass().getSimpleName());
        return activity.getClass().getSimpleName();
    }

    /**
     * ���Activity
     */
    public void pushActivity(Activity activity) {
        YkLog.i(TAG, "���Activity��" + activity.getClass().getSimpleName());
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * �Ƴ������ƶ�������Activity
     */
    public void popAllActivityExceptOne(Class<?> cls) {
        YkLog.i(TAG, "�Ƴ������ƶ�������Activity��" + cls.getSimpleName());

        do {

            if (activityStack == null) {
                activityStack = new Stack<Activity>();
            }

            for (int i = 0; i < activityStack.size(); i++) {
                if (activityStack.get(i).getClass().equals(cls))
                    continue;
                else
                    popActivity(activityStack.get(i));
            }
        } while (false);

    }

    /**
     * ����������ȡActivity����
     *
     * @param cls �ƶ�����
     * @return Activity����
     */
    public Activity getActivity(Class<?> cls) {
        YkLog.i(TAG, "����������ȡActivity����" + cls.getSimpleName());
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(cls))
                return activityStack.get(i);
        }
        return null;
    }

    /**
     * �˳�Ӧ�ó���
     */
    public void AppExit() {
        YkLog.i(TAG, "�˳�Ӧ�ó���");
        try {
            popAllActivity();

            System.exit(0);
        } catch (Exception e) {
        }
    }
}