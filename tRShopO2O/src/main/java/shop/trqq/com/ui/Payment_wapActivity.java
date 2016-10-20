package shop.trqq.com.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 支付宝网页支付
 */
public class Payment_wapActivity extends BaseActivity {

    private WebView webView;
    private Context mContext;
    private String pay_sn, order_type;
    // 标题栏标题
    private TextView mHeadTitleTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productinfo);
        mContext = this;
        this.pay_sn = getIntent().getExtras().getString("pay_sn");
        this.order_type = getIntent().getExtras().getString("order_type");
        initTitleBarView();
        webView = (WebView) findViewById(R.id.product_info_webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // webView.loadUrl("http://shop.trqq.com/wap/tmpl/product_info.html?goods_id=244");

        webSettings.setJavaScriptEnabled(true);
        // 2) 第二个坎： 有物
        // 在运行脚本前，要有document对象，至少得load一个空白页
        webView.loadData("", "text/html", "UTF-8");

        webView.setWebViewClient(new MyWebViewClient() {
            // 这个方法在用户试图点开页面上的某个链接时被调用 自动跳转
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    // 如果想继续加载目标页面则调用下面的语句
                    view.loadUrl(url);
                    // 如果不想那url就是目标网址，如果想获取目标网页的内容那你可以用HTTP的API把网页扒下来。
                }
                // 返回true表示停留在本WebView（不跳转到系统的浏览器）
                return true;
            }
        });
        // 第四个坎：console/alert
        webView.setWebChromeClient(new MyWebChromeClient());
        if ("r".equals(order_type)) {
            Pay(pay_sn);
        } else {
            Vr_Pay(pay_sn);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView webView, String url) {
            // webView.loadUrl("javascript:" + url);
        }
    }

    // optional, for show console and alert
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            YkLog.i("test", cm.message() + " -- From line " + cm.lineNumber()
                    + " of " + cm.sourceId());
            return true;
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            ToastUtils.showMessage(mContext, message);
            return true;
        }
    }

    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("支付");
    }

    // 支付
    private void Pay(String pay_sn) {
        RequestParams requestParams = new RequestParams();
        final String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        final String url = HttpUtil.URL_ORDER_PAYMENT + "&pay_sn=" + pay_sn;
        HttpUtil.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    YkLog.t("实品订单aliPay", jsonString);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString)
                                .getJSONObject("datas");
                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                        }
                    } catch (Exception e) {
                        // post方式传送参数
                        String postData = "key=" + key;
                        webView.postUrl(url,
                                EncodingUtils.getBytes(postData, "base64"));
                        // webView.loadUrl(uri);
                        webView.setWebChromeClient(new WebChromeClient() {
                            /**
                             * 处理JavaScript Alert事件
                             */
                            @Override
                            public boolean onJsAlert(WebView view, String url,
                                                     String message, final JsResult result) {
                                // 用Android组件替换
                                new AlertDialog.Builder(mContext)
                                        .setTitle("温馨提示")
                                        .setMessage(message)
                                        .setPositiveButton(
                                                android.R.string.ok,
                                                new AlertDialog.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        result.confirm();
                                                    }
                                                }).setCancelable(false)
                                        .create().show();
                                return true;
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
            }
        });
    }

    // 支付
    private void Vr_Pay(String pay_sn) {
        final String key = UserManager.getUserInfo().getKey();
        final String url = HttpUtil.URL_ORDER_VRPAYMENT + "&pay_sn=" + pay_sn;
        // post方式传送参数
        String postData = "key=" + key;
        webView.postUrl(url, EncodingUtils.getBytes(postData, "base64"));
        // webView.loadUrl(uri);

        YkLog.t("虚拟订单aliPay", url);
        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                // 用Android组件替换
                new AlertDialog.Builder(mContext)
                        .setTitle("温馨提示")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                    }
                                }).setCancelable(false).create().show();
                return true;
            }
        });
    }

}
