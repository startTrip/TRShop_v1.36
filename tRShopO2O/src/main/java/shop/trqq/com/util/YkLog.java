/**
 *
 */
package shop.trqq.com.util;

import android.util.Log;

/**
 * @author Administrator
 */
public class YkLog {

    private final static boolean IS_PRINTF = true;// ≤‚ ‘Œ™true

    public static void i(String tag, String msg) {
        if (IS_PRINTF)
            Log.i(tag, msg);
    }

    // test
    public static void t(String tag, String msg) {
        if (IS_PRINTF)
            Log.i(tag, msg);
    }

    // test
    public static void t(String msg) {
        if (IS_PRINTF)
            Log.i("trshop", msg);
    }

    public static void e(String tag, String msg) {
        if (IS_PRINTF)
            Log.e(tag, msg);

    }

    public static void longe(String tag, String msg) {
        if (IS_PRINTF) {
            int leng = msg.length() / 2000;
            for (int i = 0; i < leng; i++) {
                Log.e(tag, msg.substring(2000 * i, 2000 * (i + 1)));
            }
            Log.e(tag, msg.substring(2000 * leng));
        }
    }
}
