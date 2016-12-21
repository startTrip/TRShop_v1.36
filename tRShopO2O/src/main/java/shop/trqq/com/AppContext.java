package shop.trqq.com;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.apache.http.util.EncodingUtils;
import org.litepal.LitePalApplication;

import java.lang.Thread.UncaughtExceptionHandler;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import shop.trqq.com.im.ui.JavaScriptObject;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.StringUtils;
import shop.trqq.com.util.YkLog;

;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 *
 * @author
 * @version 1.0
 * @created 2015-02-10
 */
// LitePal框架
public class AppContext extends LitePalApplication {

    private static final String TAG = "AppContext";
    private static final String TAG1 = "JPush";

    public static Double marketLongitude =110.396436;

    public static Double marketLatitude =21.281895;

    // WIFI网络
    public static final int NETTYPE_WIFI = 0x01;
    // WAP网络
    public static final int NETTYPE_CMWAP = 0x02;
    // NET网络
    public static final int NETTYPE_CMNET = 0x03;

    // 全局应用管理对象
    protected static AppContext instance;
    // IM socketIOWebView
    private WebView socketIOWebView;
    private Boolean isIMNet;

    public Boolean getIsIMNet() {
        return isIMNet;
    }

    public void setIsIMNet(Boolean isIMNet) {
        this.isIMNet = isIMNet;
    }

    /**
     * 返回全局应用管理对象
     *
     * @return 全局应用管理对象
     */
    public static AppContext getInstance() {
        YkLog.i(TAG, "获取全局应用管理对象");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        YkLog.i(TAG, "onCreate方法");
        instance = this;
        new UserManager(instance);

        initImageLoader(instance);
//        // 调用Bugly
//        CrashReport.initCrashReport(instance, "900013242", false);
        //ButterKnife Debug
        ButterKnife.setDebug(BuildConfig.DEBUG);
        //初始化讯飞语音
        SpeechUtility.createUtility(instance, SpeechConstant.APPID +"=55f78558");
        // 初始化IM
        // initSocketIOWebView();

        // 程序崩溃时触发线程
        // Thread.setDefaultUncaughtExceptionHandler(restartHandler);

        SDKInitializer.initialize(getApplicationContext());

        Log.d(TAG1, "[ExampleApplication] onCreate");
        // 初始化 极光推送
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }

    public WebView getSocketIOWebView() {
        return socketIOWebView;
    }

    public void loadIMJS() {
        String key = UserManager.getUserInfo().getKey();
        String postData = "key=" + key;
        socketIOWebView.postUrl(HttpUtil.URL_MEMBER_CHAT,
                EncodingUtils.getBytes(postData, "base64"));
    }

    public void clearIMJS() {
        socketIOWebView.loadData("", "text/html", "UTF-8");
        // socketIOWebView.loadUrl("file:///android_asset/explain.html");
    }

    public void setSocketIOWebView(WebView msocketIOWebView) {
        this.socketIOWebView = msocketIOWebView;
        initSocketIOWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initSocketIOWebView() {
        if (UserManager.isLogin()) {
            YkLog.i(TAG, "初始化socketIOWebView");
            socketIOWebView.getSettings().setJavaScriptEnabled(true);
            // 设置本地调用对象及其接口
            socketIOWebView.addJavascriptInterface(new JavaScriptObject(
                    instance, socketIOWebView), "Android");
            loadIMJS();

            socketIOWebView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description,
                            failingUrl);
                    YkLog.i(TAG,
                            "-MyWebViewClient->onReceivedError()--\n errorCode="
                                    + errorCode + " \ndescription="
                                    + description + " \nfailingUrl="
                                    + failingUrl);
                    isIMNet = false;
                    // 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
                    socketIOWebView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            //2秒后如果加载错误还会执行onReceivedError因此不需要循环
                            loadIMJS();
                            YkLog.i(TAG, "onReceivedError 2秒后重新加载IM");
                        }
                    }, 2000);
                }


                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
                    super.onPageFinished(view, url);
                    isIMNet = true;
                }


                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            socketIOWebView.setWebChromeClient(new WebChromeClient() {
                /**
                 * 处理JavaScript Alert事件
                 */
                @Override
                public boolean onJsAlert(WebView view, String url,
                                         String message, final JsResult result) {
                    // 用Android组件替换
                    new AlertDialog.Builder(instance)
                            .setTitle("温馨提示")
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok,
                                    new AlertDialog.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            result.confirm();
                                        }
                                    }).setCancelable(false).create().show();
                    return true;
                }
            });
        }
    }

    /**
     * 创建服务用于捕获崩溃异常
     */
    @SuppressWarnings("unused")
    private UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            // 发生崩溃异常时,重启应用
            YkLog.e(TAG, "发生崩溃异常时,重启应用");
            restartApp();
        }
    };

    public static void initImageLoader(Context context) {
        YkLog.i(TAG, "初始化ImageLoader");
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
                context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        // config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public void restartApp() {
        // 考虑到死循环启动，还是采用闪退的方式
        // Intent intent = new Intent(instance, AppStart.class);
        // // 根据不同的编译环境来采用不同的移除堆栈方法
        // if (Build.VERSION.SDK_INT >=
        // android.os.Build.VERSION_CODES.HONEYCOMB)
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        // | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // else
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        // | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // instance.startActivity(intent);

        // 结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
        AppManager.getAppManager().AppExit();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 检测网络是否可用
     *
     * @return 是否可用标识
     */
    public boolean isNetworkConnected() {
        YkLog.i(TAG, "检测网络是否可用");
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    @SuppressLint("DefaultLocale")
    public int getNetworkType() {
        YkLog.i(TAG, "获取当前网络类型");
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!StringUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 获取应用版本号
     *
     * @return 版本号
     */
    public int getVersionCode() {
        try {
            return instance.getPackageManager().getPackageInfo(
                    instance.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取应用版本名
     *
     * @return 版本号
     */
    public String getVersionName() {
        try {
            return instance.getPackageManager().getPackageInfo(
                    instance.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0";
    }

    public int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = android.os.Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
            YkLog.e("AndroidSDKVersionException", e.toString());
        }
        return version;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }
}
