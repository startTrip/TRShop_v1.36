package shop.trqq.com.supermarket.activitys;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.pay.AlipayHelper;
import com.alipay.pay.PayResult;
import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.supermarket.bean.OrderBuy2Data;
import shop.trqq.com.supermarket.view.MyPopupWindow;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.widget.DialogTool;

public class ConfirmPayOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SDK_PAY_FLAG = 1;
    private ImageView mback;
    private RelativeLayout mAlipayLayout;
    private RelativeLayout mWeChatLayout;
    private ImageView mIvAlipay;
    private ImageView mIvWeChat;
    private Button mBtPay;
    private TextView mOrderMoney;
    private String mPay_sn;
    private String mCheckMoney;

    // 支付后的消息
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showPayResultSuccess();
                    } else {
                        // 判断resultStastus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ConfirmPayOrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText(ConfirmPayOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                            showPayResultFail();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    private String mResponse;

    /**
     * 支付成功的弹框
     */
    private void showPayResultSuccess() {

        View viewPop = LayoutInflater.from(ConfirmPayOrderActivity.this).inflate(R.layout.popup_pay_succes, null);
        mMyPopup.showPopupWindowFronCenter(this, viewPop);
        viewPop.findViewById(R.id.tv_pay_know).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        mMyPopup.cancel();
                        finish();
                        UIHelper.showOrder(ConfirmPayOrderActivity.this,"");
                    }
                });
    }

    /**
     * 支付失败的弹框
     */
    private void showPayResultFail() {

        View viewPop = LayoutInflater.from(ConfirmPayOrderActivity.this).inflate(R.layout.popup_pay_succes, null);
        mMyPopup.showPopupWindowFronCenter(this, viewPop);
        ((TextView)viewPop.findViewById(R.id.pay_result)).setText("您尚未完成付款");
        TextView toPay = (TextView) viewPop.findViewById(R.id.tv_pay_know);
        toPay.setText("重新支付");
        toPay.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        mMyPopup.cancel();
                    }
                });
    }
    private MyPopupWindow mMyPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_pay_order);

        initView();
        initData();

        setData();
        setListener();
    }

    private void initData(){

        mMyPopup = MyPopupWindow.getInstance();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String state = bundle.getString("state");
        switch (state){
            case "0":                               // 测试用的
                OrderBuy2Data orderInfo = (OrderBuy2Data) bundle.getSerializable("orderInfo");
                mPay_sn = orderInfo.getPay_sn();
                mCheckMoney = orderInfo.getOrder_amount();
                // TODO : 私钥对应公钥不正确（PHP与 java不同）
                // 下载支付信息
//                loadPayInfo();
                break;
            case "1":
                mCheckMoney = intent.getStringExtra("checkMoney");
                mPay_sn = intent.getStringExtra("pay_sn");
                break;
        }
    }


    private void initView() {

        mback = (ImageView) findViewById(R.id.comfirmpay_back);
        mOrderMoney = (TextView) findViewById(R.id.order_money);
        mAlipayLayout = (RelativeLayout) findViewById(R.id.rl_alipay);
        mWeChatLayout = (RelativeLayout) findViewById(R.id.rl_wechat);
        mIvAlipay = (ImageView) findViewById(R.id.iv_alipay_select);
        mIvWeChat = (ImageView) findViewById(R.id.iv_wechat_select);
        mBtPay = (Button) findViewById(R.id.btn_pay);
    }

    private void setData() {
        mOrderMoney.setText("￥"+mCheckMoney);
    }


    private void setListener() {

        mback.setOnClickListener(this);
        mAlipayLayout.setOnClickListener(this);
        mWeChatLayout.setOnClickListener(this);
        mIvAlipay.setOnClickListener(this);
        mIvWeChat.setOnClickListener(this);
        mBtPay.setOnClickListener(this);
    }

    private void loadPayInfo() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key",key);
        requestParams.add("pay_sn",mPay_sn);
        requestParams.add("price",mCheckMoney);
        String url= HttpUtil.HOST+"?act=wnj_payment&op=app_get_rsa";
        HttpUtil.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mResponse = new String(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comfirmpay_back:
                showCancelDialog();
                break;
            case R.id.rl_alipay:
                mIvAlipay.setSelected(true);
                mIvWeChat.setSelected(false);
                mBtPay.setEnabled(true);
                break;
            case R.id.rl_wechat:
                mIvAlipay.setSelected(false);
                mIvWeChat.setSelected(true);
                mBtPay.setEnabled(true);
                break;
            case R.id.iv_alipay_select:
                mIvAlipay.setSelected(true);
                mIvWeChat.setSelected(false);
                mBtPay.setEnabled(true);
                break;
            case R.id.iv_wechat_select:
                mIvWeChat.setSelected(true);
                mIvAlipay.setSelected(false);
                mBtPay.setEnabled(true);
                break;
            case R.id.btn_pay:
                toPay();
                break;
        }
    }

    // 按返回键弹出 Dialog 提示是否取消订单
    private void showCancelDialog() {
        DialogTool.createNormalDialog2(ConfirmPayOrderActivity.this,
                "订单已经生成，是否取消订单？",
                "继续付款",
                "去订单页面取消",
                null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                       // 跳转到订单页面
                        finish();
                        UIHelper.showOrder(ConfirmPayOrderActivity.this,"");
                    }}
                ).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showCancelDialog();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 支付
    private void toPay() {
        if(mIvAlipay.isSelected()){
            zhifubaopay();
        }else if(mIvWeChat.isSelected()){
            weixinpay();
        }
    }

    // 支付宝支付
    private void zhifubaopay() {

        // 订单  ！！！！ 需要设置为自己的信息，在下面
        String orderInfo =
                AlipayHelper.getOrderInfo
                        ("测试的商品", // 商品名称
                                "该测试商品的详细描述", // 商品描述
                                "0.01",mPay_sn);// 商品价格

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = AlipayHelper.sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         * 最终，支付宝使用 payInfo 来进行支付的操作
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + AlipayHelper.getSignType();
            /**
             * 完整的符合支付宝参数规范的订单信息
             */

            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    PayTask alipay = new PayTask(ConfirmPayOrderActivity.this);
                    String result = alipay.pay(payInfo, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
    }


    // 微信支付
    private void weixinpay() {
        ToastUtils.showMessage(ConfirmPayOrderActivity.this,"微信支付待完成");
    }
}

