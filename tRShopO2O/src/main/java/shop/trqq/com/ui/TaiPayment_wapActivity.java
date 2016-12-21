package shop.trqq.com.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.text.DecimalFormat;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.MD5Util;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

public class TaiPayment_wapActivity extends BaseActivity {
    private WebView webView;
    private Context mContext;
    private String pay_sn, pay_amount, payment_code, payment_codeapi;
    private String order_amount = new String();
    // ����������
    private TextView mHeadTitleTextView;

    private Button TaiPay_Submit;
    private TextView TaiPay_sn;
    private TextView TaiPay_amount;
    private TextView TaiPayorder_amount;
    private EditText TaiPay_Password;
    private boolean btIsUse = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taipayment);
        mContext = this;
        this.pay_sn = getIntent().getExtras().getString("pay_sn");
        this.pay_amount = getIntent().getExtras().getString("pay_amount");
        this.payment_code = getIntent().getExtras().getString("payment_code");
        initTitleBarView();

        TaiPay_sn = (TextView) findViewById(R.id.TaiPay_sn);
        TaiPay_amount = (TextView) findViewById(R.id.TaiPay_amount);
        TaiPayorder_amount = (TextView) findViewById(R.id.TaiPayorder_amount);
        TaiPay_Submit = (Button) findViewById(R.id.TaiPay_Submit);
        TaiPay_Password = (EditText) findViewById(R.id.TaiPay_Password);
        float amount = Float.parseFloat(pay_amount);

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        TaiPay_sn.setText("ʵ�ﶩ�����׺ţ�" + pay_sn);
        TaiPay_amount.setText("������ܽ�" +
                decimalFormat.format(Double.parseDouble(pay_amount)) + "Ԫ");
        if ("taifubao".equals(payment_code)) {
            amount = (int) ((amount / 7) * 100);
            order_amount = "" + amount / 100;
            payment_codeapi = "tbao";
        } else if ("consumepay".equals(payment_code)) {
            //order_amount = decimalFormat.format(amount / 7);
            amount = (int) ((amount / 7) * 100);
            order_amount = "" + amount / 100;
            payment_codeapi = "consume";
        } else if ("productpay".equals(payment_code)) {
            amount = (int) ((amount / 1) * 100);
            order_amount = "" + amount / 100;
            //order_amount = decimalFormat.format(amount / 1);
            payment_codeapi = "product";
        }else if ("tyongpay".equals(payment_code)) {
            amount = (int) ((amount / 1) * 100);
            order_amount = "" + amount / 100;
            //order_amount = decimalFormat.format(amount / 1);
            payment_codeapi = "tyong";
        }
        TaiPayorder_amount.setText("ʵ���ܽ�" + order_amount + "Ԫ");
        TaiPay_Submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(btIsUse){      // ��ֹ��ε��
                    btIsUse = false;
                    loadOnlineTaiPay();
                }
            }
        });
        /*
		 * webView = (WebView) findViewById(R.id.product_info_webView);
		 * WebSettings webSettings = webView.getSettings();
		 * webSettings.setSupportZoom(true);
		 * webSettings.setBuiltInZoomControls(false); webSettings
		 * .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //
		 * webView
		 * .loadUrl("http://shop.trqq.com/wap/tmpl/product_info.html?goods_id=244"
		 * );
		 * 
		 * webSettings.setJavaScriptEnabled(true); // 2) �ڶ������� ���� //
		 * �����нű�ǰ��Ҫ��document�������ٵ�loadһ���հ�ҳ webView.loadData("", "text/html",
		 * "UTF-8");
		 * 
		 * webView.setWebViewClient(new MyWebViewClient() { //
		 * ����������û���ͼ�㿪ҳ���ϵ�ĳ������ʱ������ �Զ���ת
		 * 
		 * @Override public boolean shouldOverrideUrlLoading(WebView view,
		 * String url) { if (url != null) { // ������������Ŀ��ҳ���������������
		 * view.loadUrl(url); // ���������url����Ŀ����ַ��������ȡĿ����ҳ���������������HTTP��API����ҳ��������
		 * } // ����true��ʾͣ���ڱ�WebView������ת��ϵͳ��������� return true; } });
		 * //���ĸ�����console/alert webView.setWebChromeClient(new
		 * MyWebChromeClient()); webView.setWebChromeClient(new
		 * WebChromeClient() {
		 *//**
         * ����JavaScript Alert�¼�
         */
		/*
		 * @Override public boolean onJsAlert(WebView view, String url, String
		 * message, final JsResult result) { // ��Android����滻 new
		 * AlertDialog.Builder(mContext) .setTitle("��ܰ��ʾ") .setMessage(message)
		 * .setPositiveButton( android.R.string.ok, new
		 * AlertDialog.OnClickListener() { public void onClick( DialogInterface
		 * dialog, int which) { result.confirm(); } }).setCancelable(false)
		 * .create().show(); return true; } });
		 */
        // TaiPay();
    }

/*	private class MyWebViewClient extends WebViewClient {
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
	}*/

    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        ImageView back = (ImageView) findViewById(R.id.title_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
            }
        });
        String HeadTitle = "";
        if ("taifubao".equals(payment_code)) {
            HeadTitle = "̩����֧��";
        } else if ("consumepay".equals(payment_code)) {
            HeadTitle = "���ѻ���֧��";
        } else if ("productpay".equals(payment_code)) {
            HeadTitle = "��Ʒ����֧��";
        }else if ("tyongpay".equals(payment_code)) {
            HeadTitle = "ͨ�û���֧��";
        }
        mHeadTitleTextView.setText(HeadTitle);
    }

    // ����̩����
    private void loadOnlineTaiPay() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("my", order_amount);
        requestParams.add("pay_sn", pay_sn);
        requestParams.add("pay", payment_codeapi);
        String Password = TaiPay_Password.getText().toString();
        requestParams.add("pw2", MD5Util.getMD5String(Password));
        // String uri ="http://shop.trqq.com/mobile/index.php?act=member_index";
        HttpUtil.post(HttpUtil.URL_ORDER_TAIPAYMENT, requestParams,
                new AsyncHttpResponseHandler() {
                    ProgressDialog pd;
                    @Override
                    public void onStart() {
                        super.onStart();
                        pd = ProgressDialog.show(mContext, null,
                                "����֧��...", true, true,
                                new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        sendCancelMessage();
                                    }
                                });
                        pd.setCanceledOnTouchOutside(false);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        String msgString = new String(responseBody);
                        YkLog.t("URL_ORDER_TAIPAYMENT_old��", msgString);
                        msgString = msgString.replaceAll("\\s*|\t|\r|\n", "");
                        YkLog.t("URL_ORDER_TAIPAYMENT��", msgString);
                        int msg;
                        try {
                            msg = Integer.parseInt(msgString);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            msg=-1;//δ֪����
                        }
                        String message = new String();
                        switch (msg) {
                            case 0:
                                message = "һ��ͨ�û���Ϣ��������ϵ�̳ǹ�����Ա";
                                break;
                            case 1:
                                message = "֧����������";
                                break;
                            case 2:
                                message = "֧�������������������";
                                TaiPay_Password.setText("");
                                break;
                            case 3:
                                message = "�������㣬���ֵ���ٽ���֧��";
                                break;
                            case 4:
                                message = "֧���ɹ�";
                                TaiPay_Submit.setEnabled(false);
                                TaiPay_Submit.setText("֧���ɹ�");
                                TaiPay_Submit.setOnClickListener(null);
                                TaiPay_Submit.setBackgroundColor(Color.GRAY);
                                break;
                            case 5:
                                message = "֧��ʧ��";
                                break;
                            case 6:
                                message = "�ö�������֧��";
                                break;
                            case 7:
                                message = "�����ܾ�,������ɳ,̩����ʴ�Ƶ��Ʒ����ʹ�ò�Ʒ���ֶһ�֧����";
                                break;
                            case 8:
                                message = "��������̩�����û�";
                                break;
                            case 9:
                                message = "�ö��������쳣���������������ʣ�����ϵ������Ա";
                                break;
                            case 10:
                                message = "�쳣����ɹ�";
                                break;
                            case 11:
                                message = "�����ܾ�,������ɳ,̩����ʴ�Ƶ�,ս�������̼Ҳ�Ʒ����ʹ�����ѻ��ֶһ�֧��!";
                                break;
                            default:
                                message = "δ֪����";
                                break;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                new ContextThemeWrapper(
                                        mContext,
                                        android.support.v7.appcompat.R.style.Theme_AppCompat_Light_DialogWhenLarge));
                        final String finalMessage = message;
                        builder.setTitle("��ܰ��ʾ")
                                .setMessage(message)
                                .setPositiveButton(android.R.string.ok,
                                        new AlertDialog.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                    if (TextUtils.equals(finalMessage,"֧���ɹ�")){
                                                    finish();
                                                }
                                            }
                                        }).setCancelable(false).create().show();
                        btIsUse = true;  // �����������Ժ�ſ������µ��
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        ToastUtils.showMessage(mContext, "�������ݻ�ȡʧ��");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        pd.dismiss();
                    }
                });
    }

    // ̩����֧��
/*    private void TaiPay() {
        final String key = UserManager.getUserInfo().getKey();
        final String url = "http://shop.trqq.com/mobile/api/payment/taifubao/taifubao.php?"
                + "&pay_sn="
                + pay_sn
                + "&pay_amount="
                + pay_amount
                + "&payment_code=" + payment_code;
        // String postData = "key=" + key;
        syncCookie(mContext, "http://shop.trqq.com/wap", key);
		*//*
		 * mWebView = (WebView) findViewById(R.id.rank_webview);
		 * mWebView.setHorizontalScrollBarEnabled(false);
		 * mWebView.getSettings().setJavaScriptEnabled(true);
		 * mWebView.setWebChromeClient(new WebChromeClient());
		 *//*
        webView.loadUrl(url);
        YkLog.t("̩����", url);
		*//*
		 * webView.postUrl(url, EncodingUtils.getBytes(postData, "base64"));
		 *//*
        // webView.loadUrl(uri);

    }*/

    /**
     * Sync Cookie
     */
    private void syncCookie(Context context, String url, String key) {
        try {
            YkLog.t("Nat: webView.syncCookie.url", url);

            CookieSyncManager.createInstance(context);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// �Ƴ�
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if (oldCookie != null) {
                YkLog.t("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
            }

            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("key=%s", key));
            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(url, cookieValue);
            CookieSyncManager.getInstance().sync();

            String newCookie = cookieManager.getCookie(url);
            if (newCookie != null) {
                YkLog.t("Nat: webView.syncCookie.newCookie", newCookie);
            }
        } catch (Exception e) {
            YkLog.t("Nat: webView.syncCookie failed", e.toString());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
            return  false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
