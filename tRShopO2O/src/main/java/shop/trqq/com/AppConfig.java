package shop.trqq.com;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class AppConfig {

    // APP名称
    public static final String APP_NAME = "TRo2o";
    // 用户信息
    public static final String USER_INFO = "user_info";

    public static final String OTHER = "other";

    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 设置Preference值
     *
     * @param context
     * @param configKey
     * @param value
     */
    public static void setSharedPreferences(Context context, String configKey,
                                            Object value) {
        Editor editor = getSharedPreferences(context).edit();
        if (value instanceof Boolean) {
            editor.putBoolean(configKey, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(configKey, (Integer) value);
        } else if (value instanceof String) {
            editor.putString(configKey, (String) value);
        } else if (value instanceof Float) {
            editor.putFloat(configKey, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(configKey, (Long) value);
        }
        editor.commit();
    }

}
