package shop.trqq.com;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.YkLog;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 *
 * @author Weiss
 * @version 2.0
 * @created 2016-03-5
 */
public class AppStart extends BaseActivity {

    private static final String TAG = "AppStart";

    final AppContext ac = (AppContext) getApplication();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YkLog.i(TAG, "onCreate方法");
//        SystemBarHelper.tintStatusBar(this, getResources().getColor(R.color.start_bg_color));
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
/*        final View view = View.inflate(this, R.layout.activity_start, null);
        LinearLayout wellcome = (LinearLayout) view
                .findViewById(R.id.app_start_view);
        // check(wellcome);
        // Android 启动 白屏问题 解决 http://blog.csdn.net/iloveforeign/article/details/8660617
        setContentView(view);*/
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UIHelper.showMain(AppStart.this);
            }
        },1000);


    }

}