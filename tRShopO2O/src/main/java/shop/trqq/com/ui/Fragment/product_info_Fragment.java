package shop.trqq.com.ui.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import de.greenrobot.event.EventBus;
import shop.trqq.com.R;
import shop.trqq.com.event.EventGoodmInfo;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 图文详情Fragment
 */
public class product_info_Fragment extends BaseLazyFragment {
    private WebView webView;
    private boolean flag = false;
    private String goods_id;
    private String mobile_body;
    // 标题栏标题
    private TextView mHeadTitleTextView;
    private View rootView;// 缓存Fragment view
    // 全局Context
    private Context mContext;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.fragment_productinfo, container,
                false);
        // setContentView(R.layout.activity_productinfo);
        Bundle bundle = getArguments();
        goods_id = bundle.getString("goods_id");
        // goods_id="235";
        mobile_body = bundle.getString("mobile_body");

        webView = (WebView) rootView.findViewById(R.id.product_info_webView);
        WebSettings settings = webView.getSettings();
        // 设置可以支持缩放
        settings.setSupportZoom(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        // settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // }
        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                // 用Android组件替换
                new AlertDialog.Builder(mContext)
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
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register
        EventBus.getDefault().register(this);
    }

    /**
     * 收到消息 进行相关处理
     *
     * @param event
     */
    public void onEventMainThread(EventGoodmInfo event) {
        YkLog.t("EventGoodmInfo", event.getMsg());
        mobile_body = event.getMsg();
        goods_id = event.getGoods_id();
        // 如果有电脑版就覆盖手机版
        webView.loadDataWithBaseURL(null, this.mobile_body, "text/html",
                "utf-8", "");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister
        EventBus.getDefault().unregister(this);
    }

    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) rootView
                .findViewById(R.id.head_title_textView);
        if (goods_id.equals("")) {
            mHeadTitleTextView.setText("用户服务协议");
        } else if (goods_id.equals("explain")) {
            mHeadTitleTextView.setText("说明");
        } else {
            mHeadTitleTextView.setText("图文详情");
        }
    }

    @Override
    protected void lazyLoad() {
        // TODO Auto-generated method stub
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        webView.loadDataWithBaseURL(null, this.mobile_body, "text/html",
                "utf-8", "");
        /*
		 * webView.loadUrl(HttpUtil.URL_GOODS_DETAILS_WEB+"&goods_id="+goods_id);
		 * mHasLoadedOnce = true;
		 */
        loadOnlineproduct_info();

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
                        // webView.loadData(css,"text/html","utf-8");//不可行
                        // webView.loadUrl("file:///android_asset/wap.html");
                        mHasLoadedOnce = true;
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        ToastUtils.showMessage(mContext, "网络数据获取失败");
                    }
                });
    }
}
