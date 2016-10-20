package shop.trqq.com.ui.Base;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import shop.trqq.com.AppManager;


/**
 * Ӧ�ó���Activity�Ļ���
 *
 * @author Weiss
 * @version 1.0
 * @created 2015-02-10
 */
public class BaseFragmentActivity extends FragmentActivity {

    // �Ƿ�����ȫ��
    private boolean allowFullScreen = true;

    // �Ƿ���������
    private boolean allowDestroy = true;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allowFullScreen = true;
        ;
        // ���Activity����ջ
        AppManager.getAppManager().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // ����Activity&�Ӷ�ջ���Ƴ�
        AppManager.getAppManager().popActivity(this);
    }

    //Activity�Ӻ�̨���»ص�ǰ̨ʱ������IM
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
     * �����Ƿ����ȫ��
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
