package shop.trqq.com.ui.Base;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

import shop.trqq.com.AppManager;
import shop.trqq.com.R;

/**
 * 双击退出 创建日期 2014-05-12
 *
 * @author 火蚁 (http://my.oschina.net/LittleDY)
 */
public class DoubleClickExitHelper {

    private final Activity mActivity;

    private boolean isOnKeyBacking;
    private Handler mHandler;
    private Toast mBackToast;

    public DoubleClickExitHelper(Activity activity) {
        mActivity = activity;
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Activity onKeyDown事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false;
        }
        if (isOnKeyBacking) {
            mHandler.removeCallbacks(onBackTimeRunnable);
            if (mBackToast != null) {
                mBackToast.cancel();
            }
            // 退出
/*			Intent i = new Intent(mActivity.getApplicationContext(), NetworkStateService.class);
            mActivity.getApplication().stopService(i);*/
            AppManager.getAppManager().AppExit();
            return true;
        } else {
            isOnKeyBacking = true;
            if (mBackToast == null) {
                mBackToast = Toast.makeText(mActivity, R.string.back_exit_tips,
                        Toast.LENGTH_SHORT);
            }
            mBackToast.show();
            mHandler.postDelayed(onBackTimeRunnable, 2000);
            return true;
        }
    }

    private Runnable onBackTimeRunnable = new Runnable() {

        @Override
        public void run() {
            isOnKeyBacking = false;
            if (mBackToast != null) {
                mBackToast.cancel();
            }
        }
    };
}
