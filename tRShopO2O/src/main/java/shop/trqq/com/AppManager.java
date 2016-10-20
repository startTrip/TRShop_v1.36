package shop.trqq.com;

import android.app.Activity;

import java.util.Stack;

import shop.trqq.com.util.YkLog;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 *
 * @author
 * @version 1.0
 * @created 2015-02-10
 */
public class AppManager {

    private static final String TAG = "AppManager";
    // Activity堆栈
    private static Stack<Activity> activityStack;
    // 应用管理对象
    private static AppManager instance;

    private AppManager() {
        YkLog.i(TAG, "初始化应用管理对象");
    }

    /**
     * 单一实例
     *
     * @return 应用管理对象
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 移除所有Activity
     */
    public void popAllActivity() {
        YkLog.i(TAG, "移除所有Activity");
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
     * 移除指定Activity
     */
    public void popActivity(Activity activity) {
        YkLog.i(TAG, "移除指定Activity：" + activity.getClass().getSimpleName());
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        YkLog.i(TAG, "获取下一个Activity：" + activity.getClass().getSimpleName());
        return activity;
    }

    /**
     * 获取当前ActivityString（堆栈中最后一个压入的）
     */
    public String currentActivityString() {
        Activity activity = activityStack.lastElement();
        YkLog.i(TAG, "获取下一个Activity：" + activity.getClass().getSimpleName());
        return activity.getClass().getSimpleName();
    }

    /**
     * 添加Activity
     */
    public void pushActivity(Activity activity) {
        YkLog.i(TAG, "添加Activity：" + activity.getClass().getSimpleName());
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除除了制定的其余Activity
     */
    public void popAllActivityExceptOne(Class<?> cls) {
        YkLog.i(TAG, "移除除了制定的其余Activity：" + cls.getSimpleName());

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
     * 根据类名获取Activity对象
     *
     * @param cls 制定类名
     * @return Activity对象
     */
    public Activity getActivity(Class<?> cls) {
        YkLog.i(TAG, "根据类名获取Activity对象：" + cls.getSimpleName());
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(cls))
                return activityStack.get(i);
        }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        YkLog.i(TAG, "退出应用程序");
        try {
            popAllActivity();

            System.exit(0);
        } catch (Exception e) {
        }
    }
}