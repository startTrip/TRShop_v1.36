package shop.trqq.com.supermarket.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.supermarket.bean.OrderBuy2Data;
import shop.trqq.com.supermarket.view.MyPopupWindow;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;

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

    // ֧�������Ϣ
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * ͬ�����صĽ��������õ�����˽�����֤����֤�Ĺ����뿴https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) �����̻������첽֪ͨ
                     */
                    String resultInfo = payResult.getResult();// ͬ��������Ҫ��֤����Ϣ

                    String resultStatus = payResult.getResultStatus();
                    // �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showPayResultSuccess();
                    } else {
                        // �ж�resultStastus Ϊ��"9000"��������֧��ʧ��
                        // "8000"����֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ConfirmPayOrderActivity.this, "֧�����ȷ����", Toast.LENGTH_SHORT).show();

                        } else {
                            // ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
                            Toast.makeText(ConfirmPayOrderActivity.this, "֧��ʧ��", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
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
            case "0":                               // �����õ�
                OrderBuy2Data orderInfo = (OrderBuy2Data) bundle.getSerializable("orderInfo");
                mPay_sn = orderInfo.getPay_sn();
                mCheckMoney = orderInfo.getOrder_amount();
                break;
            case "1":
                mCheckMoney = intent.getStringExtra("checkMoney");
                mPay_sn = intent.getStringExtra("pay_sn");
                break;
        }
        // ����֧����Ϣ
//        loadPayInfo();
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
        mOrderMoney.setText("��"+mCheckMoney);
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
        requestParams.add("pay_sn",mPay_sn);
        requestParams.add("price","0.01");
        requestParams.add("key",key);
        String url= HttpUtil.HOST+"/mobile/index.php?act=wnj_payment&op=app_get_rsa";
        HttpUtil.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                finish();
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

    private void toPay() {
        if(mIvAlipay.isSelected()){
            zhifubaopay();
        }else if(mIvWeChat.isSelected()){
            weixinpay();
        }
    }

    // ֧����֧��
    private void zhifubaopay() {

        // ����  �������� ��Ҫ����Ϊ�Լ�����Ϣ��������
        String orderInfo =
                AlipayHelper.getOrderInfo
                        ("���Ե���Ʒ", // ��Ʒ����
                                "�ò�����Ʒ����ϸ����", // ��Ʒ����
                                "0.01",mPay_sn);// ��Ʒ�۸�
        Log.d("orderInfo",orderInfo);
        /**
         * �ر�ע�⣬�����ǩ���߼���Ҫ���ڷ���ˣ�����˽Կй¶�ڴ����У�
         */
        String sign = AlipayHelper.sign(orderInfo);
        try {
            /**
             * �����sign ��URL����
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * �����ķ���֧���������淶�Ķ�����Ϣ
         * ���գ�֧����ʹ�� payInfo ������֧���Ĳ���
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + AlipayHelper.getSignType();
        /**
         * �����ķ���֧���������淶�Ķ�����Ϣ
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
        // �����첽����
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * ֧���ɹ��ĵ���
     */
    private void showPayResultSuccess() {

        View viewPop = LayoutInflater.from(ConfirmPayOrderActivity.this).inflate(R.layout.popup_pay_succes, null);
        mMyPopup.showPopupWindowFronCenter(this, viewPop);
        viewPop.findViewById(R.id.tv_pay_know).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        mMyPopup.cancel();
                    }
                });
    }
    // ΢��֧��
    private void weixinpay() {
        ToastUtils.showMessage(ConfirmPayOrderActivity.this,"΢��֧�������");
    }

}

