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
            YkLog.i(TAG, "��ʼ��socketIOWebView");
            socketIOWebView.getSettings().setJavaScriptEnabled(true);
            // ���ñ��ص��ö�����ӿ�
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
                    // �������������������������Ը���errorCode��ֵ�����жϣ�������ϸ�Ĵ���

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
                    new AlertDialog.Builder(mContext)
                            .setTitle("JS��ʾ")
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
