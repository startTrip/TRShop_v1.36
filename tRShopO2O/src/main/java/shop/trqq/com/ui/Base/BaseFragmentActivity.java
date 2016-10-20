package shop.trqq.com.ui.Base;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import shop.trqq.com.AppManager;


/**
 * 应用程序Activity的基类
 *
 * @author Weiss
 * @version 1.0
 * @created 2015-02-10
 */
public class BaseFragmentActivity extends FragmentActivity {

    // 是否允许全屏
    private boolean allowFullScreen = true;

    // 是否允许销毁
    private boolean allowDestroy = true;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allowFullScreen = true;
        ;
        // 添加Activity到堆栈
        AppManager.getAppManager().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().popActivity(this);
    }

    //Activity从后台重新回到前台时被调用IM
    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
/*		AppContext mAppContext=(AppContext)getApplication();
        mAppContext.loadIMJS();*/
        super.onRestart();
    }

    public boolean isAllowFullScreen() {
        return allowFullScreen;
    }

    /**
     * 设置是否可以全屏
     *
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.allowFullScreen = allowFullScreen;
    }

    public void setAllowDestroy(boolean allowDestroy) {
        this.allowDestroy = allowDestroy;
    }

    public void setAllowDestroy(boolean allowDestroy, View view) {
        this.allowDestroy = allowDestroy;
        this.view = view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && view != null) {
            view.onKeyDown(keyCode, event);
            if (!allowDestroy) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
