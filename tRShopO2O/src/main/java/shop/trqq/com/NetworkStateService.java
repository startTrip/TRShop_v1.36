package shop.trqq.com;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;

import shop.trqq.com.util.YkLog;

/**
 * 监听网络状态服务
 */
public class NetworkStateService extends Service {

    private static final String TAG = "NetworkStateService";
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private Boolean isNet = true;
    private MyBinder myBinder = new MyBinder();

    public Boolean getIsNet() {
        return isNet;
    }

    public void setIsNet(Boolean isNet) {
        this.isNet = isNet;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                YkLog.i(TAG, "网络状态 已经改变");
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    YkLog.i(TAG, "当前网络名称：" + name);
                    if (!isNet) {
                        if (UserManager.isLogin()) {
                            AppContext mAppContext = (AppContext) getApplicationContext();
                            mAppContext.loadIMJS();
                            isNet = true;
                        }
                        /*
						 * WebView
						 * socketIOWebView=mAppContext.getSocketIOWebView();
						 * 
						 * String key = UserManager.getUserInfo().getKey();
						 * String postData = "key=" + key;
						 * socketIOWebView.postUrl(HttpUtil.URL_MEMBER_CHAT,
						 * EncodingUtils.getBytes(postData, "base64"));
						 */
                    }
                    // doSomething()
                } else {
                    YkLog.i(TAG, "没有可用网络");
                    isNet = false;
                    // doSomething()
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return myBinder;
    }

    public boolean onUnbind(Intent intent) {
        return false;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        // 注册广播接收者接收网络状态的变化
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        //super.onDestroy();
        YkLog.i(TAG, "NetworkStateService已经销毁");
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    // 提供给客户端访问
    public class MyBinder extends Binder {

        public NetworkStateService getService() {
            return NetworkStateService.this;
        }
    }
}
