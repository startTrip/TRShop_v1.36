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
 * ֧������ҳ֧��
 */
public class Payment_wapActivity extends BaseActivity {

    private WebView webView;
    private Context mContext;
    private String pay_sn, order_type;
    // ����������
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
        // 2) �ڶ������� ����
        // �����нű�ǰ��Ҫ��document�������ٵ�loadһ���հ�ҳ
        webView.loadData("", "text/html", "UTF-8");

        webView.setWebViewClient(new MyWebViewClient() {
            // ����������û���ͼ�㿪ҳ���ϵ�ĳ������ʱ������ �Զ���ת
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    // ������������Ŀ��ҳ���������������
                    view.loadUrl(url);
                    // ���������url����Ŀ����ַ��������ȡĿ����ҳ���������������HTTP��API����ҳ��������
                }
                // ����true��ʾͣ���ڱ�WebView������ת��ϵͳ���������
                return true;
            }
        });
        // ���ĸ�����console/alert
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
        mHeadTitleTextView.setText("֧��");
    }

    // ֧��
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
                    YkLog.t("ʵƷ����aliPay", jsonString);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString)
                                .getJSONObject("datas");
                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                        }
                    } catch (Exception e) {
                        // post��ʽ���Ͳ���
                        String postData = "key=" + key;
                        webView.postUrl(url,
                                EncodingUtils.getBytes(postData, "base64"));
                        // webView.loadUrl(uri);
                        webView.setWebChromeClient(new WebChromeClient() {
                            /**
                             * ����JavaScript Alert�¼�
                             */
                            @Override
                            public boolean onJsAlert(WebView view, String url,
                                                     String message, final JsResult result) {
                                // ��Android����滻
                                new AlertDialog.Builder(mContext)
                                        .setTitle("��ܰ��ʾ")
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

    // ֧��
    private void Vr_Pay(String pay_sn) {
        final String key = UserManager.getUserInfo().getKey();
        final String url = HttpUtil.URL_ORDER_VRPAYMENT + "&pay_sn=" + pay_sn;
        // post��ʽ���Ͳ���
        String postData = "key=" + key;
        webView.postUrl(url, EncodingUtils.getBytes(postData, "base64"));
        // webView.loadUrl(uri);

        YkLog.t("���ⶩ��aliPay", url);
        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * ����JavaScript Alert�¼�
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                // ��Android����滻
                new AlertDialog.Builder(mContext)
                        .setTitle("��ܰ��ʾ")
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
