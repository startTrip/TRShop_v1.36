package shop.trqq.com;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class AppConfig {

    // APP����
    public static final String APP_NAME = "TRo2o";
    // �û���Ϣ
    public static final String USER_INFO = "user_info";

    public static final String OTHER = "other";

    /**
     * ��ȡPreference����
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * ����Preferenceֵ
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
