package shop.trqq.com.supermarket.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import shop.trqq.com.R;

public class ComfirmPayOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mback;
    private RelativeLayout mAlipayLayout;
    private RelativeLayout mWeChatLayout;
    private ImageView mIvAlipay;
    private ImageView mIvWeChat;
    private Button mBtPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_pay_order);

        initView();
        setListener();
    }

    private void initView() {

        mback = (ImageView) findViewById(R.id.comfirmpay_back);
        mAlipayLayout = (RelativeLayout) findViewById(R.id.rl_alipay);
        mWeChatLayout = (RelativeLayout) findViewById(R.id.rl_wechat);
        mIvAlipay = (ImageView) findViewById(R.id.iv_alipay_select);
        mIvWeChat = (ImageView) findViewById(R.id.iv_wechat_select);
        mBtPay = (Button) findViewById(R.id.btn_pay);
    }

    private void setListener() {
        mback.setOnClickListener(this);
        mAlipayLayout.setOnClickListener(this);
        mWeChatLayout.setOnClickListener(this);
        mIvAlipay.setOnClickListener(this);
        mIvWeChat.setOnClickListener(this);
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
        }
    }
}
