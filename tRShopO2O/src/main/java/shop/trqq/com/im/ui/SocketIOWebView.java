package shop.trqq.com.im.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.util.EncodingUtils;

import shop.trqq.com.UserManager;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.YkLog;

/**
 * @author Weiss
 */
public class SocketIOWebView {

    private static final String TAG = "SocketIOWebView";
    // IM socketIOWebView
    private WebView socketIOWebView;

    private Context mContext;

    public WebView getSocketIOWebView() {
        return socketIOWebView;
    }

    public void loadIMJS() {
        String key = UserManager.getUserInfo().getKey();
        String postData = "key=" + key;
        socketIOWebView.postUrl(HttpUtil.URL_MEMBER_CHAT,
                EncodingUtils.getBytes(postData, "base64"));
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setSocketIOWebView(WebView msocketIOWebView) {
        this.socketIOWebView = msocketIOWebView;
        if (UserManager.isLogin()) {
            YkLog.i(TAG, "初始化socketIOWebView");
            socketIOWebView.getSettings().setJavaScriptEnabled(true);
            // 设置本地调用对象及其接口
            socketIOWebView.addJavascriptInterface(new JavaScriptObject(
                    mContext, socketIOWebView), "Android");
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
                    // 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。

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
                    new AlertDialog.Builder(mContext)
                            .setTitle("JS提示")
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

}
