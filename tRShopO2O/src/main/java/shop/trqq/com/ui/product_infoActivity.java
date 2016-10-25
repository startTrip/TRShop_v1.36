package shop.trqq.com.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 图文详情 & WebView通用加载Activity
 */
public class product_infoActivity extends BaseActivity {
    private WebView webView;
    private boolean flag = false;
    private String goods_id;
    private String mobile_body;
    // 标题栏标题
    private TextView mHeadTitleTextView;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productinfo);
        this.goods_id = getIntent().getExtras().getString("goods_id");
        // goods_id="235";
        this.mobile_body = getIntent().getExtras().getString("mobile_body");
        mContext = this;
        initTitleBarView();
        // mobile_body="";
        webView = (WebView) findViewById(R.id.product_info_webView);
        WebSettings settings = webView.getSettings();
        // 设置可以支持缩放
        settings.setSupportZoom(true);
        //webView.getSettings().setBuiltInZoomControls(false);
        //自适应屏幕


        if (goods_id.equals("")) {
            settings.setLayoutAlgorithm(
                    WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.loadUrl("file:///android_asset/agreement.html");
        } else if (goods_id.equals("explain")) {
            settings.setLayoutAlgorithm(
                    WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.loadUrl("file:///android_asset/explain.html");
        } else if (goods_id.equals("contactme")) {
            settings.setLayoutAlgorithm(
                    WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.loadUrl("file:///android_asset/contactme.html");
        } else if (goods_id.equals("cooperate")) {
            settings.setLayoutAlgorithm(
                    WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.loadUrl("file:///android_asset/cooperate.html");
        } else {
            // 设置出现缩放工具,放大后会变回适应屏幕，估计图文详情html固定适应屏幕。
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setBuiltInZoomControls(true);
            webView.loadDataWithBaseURL(null, this.mobile_body, "text/html",
                    "utf-8", "");
            loadOnlineproduct_info();
            /*webView.loadUrl(HttpUtil.URL_GOODS_DETAILS_WEB + "&goods_id="
					+ goods_id);*/

        }
        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                // 用Android组件替换
                new AlertDialog.Builder(product_infoActivity.this)
                        .setTitle("JS提示")
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

    private void loadOnlineproduct_info() {
        RequestParams requestParams = new RequestParams();
        requestParams.add("goods_id", goods_id);
        // String uri ="http://shop.trqq.com/mobile/index.php?act=member_index";
        HttpUtil.get(HttpUtil.URL_GOODS_DETAILS_WEB, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        String msgString = new String(responseBody);
                        String meta = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />";
                        String css = "<link rel='stylesheet'  href='file:///android_asset/wap.css' type='text/css'>";
                        css = meta + css + msgString;
                        YkLog.t("loadOnlineproduct_info", css);
                        webView.loadDataWithBaseURL(null, css, "text/html",
                                "utf-8", "");
                        //webView.loadData(css,"text/html","utf-8");//不可行
                        //webView.loadUrl("file:///android_asset/wap.html");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        ToastUtils.showMessage(mContext, "网络数据获取失败");
                    }
                });
    }

    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        if (goods_id.equals("")) {
            mHeadTitleTextView.setText("用户服务协议");
        } else if (goods_id.equals("explain")) {
            mHeadTitleTextView.setText("关于我们");
        } else if (goods_id.equals("contactme")) {
            mHeadTitleTextView.setText("联系我们");
        } else if (goods_id.equals("cooperate")) {
            mHeadTitleTextView.setText("合作与洽谈");
        } else {
            mHeadTitleTextView.setText("图文详情");
        }
    }
}

