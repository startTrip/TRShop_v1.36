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
 * ȫ��Ӧ�ó����ࣺ���ڱ���͵���ȫ��Ӧ�����ü�������������
 *
 * @author
 * @version 1.0
 * @created 2015-02-10
 */
// LitePal���
public class AppContext extends LitePalApplication {

    private static final String TAG = "AppContext";
    private static final String TAG1 = "JPush";

    public static Double marketLongitude =110.396436;

    public static Double marketLatitude =21.281895;

    // WIFI����
    public static final int NETTYPE_WIFI = 0x01;
    // WAP����
    public static final int NETTYPE_CMWAP = 0x02;
    // NET����
    public static final int NETTYPE_CMNET = 0x03;

    // ȫ��Ӧ�ù������
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
     * ����ȫ��Ӧ�ù������
     *
     * @return ȫ��Ӧ�ù������
     */
    public static AppContext getInstance() {
        YkLog.i(TAG, "��ȡȫ��Ӧ�ù������");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        YkLog.i(TAG, "onCreate����");
        instance = this;
        new UserManager(instance);

        initImageLoader(instance);
//        // ����Bugly
//        CrashReport.initCrashReport(instance, "900013242", false);
        //ButterKnife Debug
        ButterKnife.setDebug(BuildConfig.DEBUG);
        //��ʼ��Ѷ������
        SpeechUtility.createUtility(instance, SpeechConstant.APPID +"=55f78558");
        // ��ʼ��IM
        // initSocketIOWebView();

        // �������ʱ�����߳�
        // Thread.setDefaultUncaughtExceptionHandler(restartHandler);

        SDKInitializer.initialize(getApplicationContext());

        Log.d(TAG1, "[ExampleApplication] onCreate");
        // ��ʼ�� ��������
        JPushInterface.setDebugMode(true); 	// ���ÿ�����־,����ʱ��ر���־
        JPushInterface.init(this);     		// ��ʼ�� JPush
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
            YkLog.i(TAG, "��ʼ��socketIOWebView");
            socketIOWebView.getSettings().setJavaScriptEnabled(true);
            // ���ñ��ص��ö�����ӿ�
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
                    // �������������������������Ը���errorCode��ֵ�����жϣ�������ϸ�Ĵ���
                    socketIOWebView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            //2���������ش��󻹻�ִ��onReceivedError��˲���Ҫѭ��
                            loadIMJS();
                            YkLog.i(TAG, "onReceivedError 2������¼���IM");
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
                 * ����JavaScript Alert�¼�
                 */
                @Override
                public boolean onJsAlert(WebView view, String url,
                                         String message, final JsResult result) {
                    // ��Android����滻
                    new AlertDialog.Builder(instance)
                            .setTitle("��ܰ��ʾ")
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
     * �����������ڲ�������쳣
     */
    @SuppressWarnings("unused")
    private UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            // ���������쳣ʱ,����Ӧ��
            YkLog.e(TAG, "���������쳣ʱ,����Ӧ��");
            restartApp();
        }
    };

    public static void initImageLoader(Context context) {
        YkLog.i(TAG, "��ʼ��ImageLoader");
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
        // ���ǵ���ѭ�����������ǲ������˵ķ�ʽ
        // Intent intent = new Intent(instance, AppStart.class);
        // // ���ݲ�ͬ�ı��뻷�������ò�ͬ���Ƴ���ջ����
        // if (Build.VERSION.SDK_INT >=
        // android.os.Build.VERSION_CODES.HONEYCOMB)
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        // | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // else
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        // | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // instance.startActivity(intent);

        // ��������֮ǰ���԰�������ע�������˳����������δ���֮ǰ
        AppManager.getAppManager().AppExit();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * ��������Ƿ����
     *
     * @return �Ƿ���ñ�ʶ
     */
    public boolean isNetworkConnected() {
        YkLog.i(TAG, "��������Ƿ����");
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * ��ȡ��ǰ��������
     *
     * @return 0��û������ 1��WIFI���� 2��WAP���� 3��NET����
     */
    @SuppressLint("DefaultLocale")
    public int getNetworkType() {
        YkLog.i(TAG, "��ȡ��ǰ��������");
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
     * ��ȡӦ�ð汾��
     *
     * @return �汾��
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
     * ��ȡӦ�ð汾��
     *
     * @return �汾��
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
     * �жϵ�ǰ�汾�Ƿ����Ŀ��汾�ķ���
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }
}
