package shop.trqq.com.supermarket.activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

public class BinderCardActivity extends AppCompatActivity {

    private EditText mCardNumber;
    private EditText mCardName;
    private EditText mPhoneNumber;
    private EditText mCode;
    private TextView mSendCode;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_card);

        initView();
        initData();
        setListener();
    }

    private void setListener() {
//        mSendCode.setOnClickListener(this);
    }


    private void initView() {

        mCardNumber = (EditText) findViewById(R.id.binder_card_number);
        mCardName = (EditText) findViewById(R.id.binder_name);
        mPhoneNumber = (EditText) findViewById(R.id.binder_phone_number);
        mCode = (EditText)findViewById(R.id.binder_code);
        mSendCode = (TextView) findViewById(R.id.binder_send_code);
    }

    private void initData() {

        mContext = BinderCardActivity.this;

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                // 点击返回
                finish();
                break;

            case R.id.binder_send_code:
                String phone_number = mPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone_number.trim())) {
                    Toast.makeText(this,"电话不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone_number.length()<11) {
                    Toast.makeText(this,"电话号码长度不对",Toast.LENGTH_SHORT).show();
                    return;
                }
                // 发送验证码
                sendCode(phone_number);
                break;

            // 点击绑定提交
            case R.id.binder_button:

                // 绑定购物卡
                bindCard();

                break;
        }
    }
    // 绑定购物卡
    private void bindCard() {
        String cardNumber = mCardNumber.getText().toString().trim();
        String cardName = mCardName.getText().toString().trim();
        String phoneNumber = mPhoneNumber.getText().toString().trim();
        String code = mCode.getText().toString().trim();
        if(TextUtils.isEmpty(cardNumber)){
            ToastUtils.showMessage(mContext,"请填写购物卡号");
            return;
        }
        if(TextUtils.isEmpty(cardName)){
            ToastUtils.showMessage(mContext,"请填写持卡人姓名");
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            ToastUtils.showMessage(mContext,"请填写手机号码");
            return;
        }
        if(TextUtils.isEmpty(code)){
            ToastUtils.showMessage(mContext,"请填写验证码");
            return;
        }

//        card_id  -->   购物卡号
//        card_name  -->   购物卡姓名
//        card_phone  -->  购物卡电话
//        member_name  -->  需要绑定的商城会员名称(账号)
        RequestParams requestParams = new RequestParams();
        String nickname = UserManager.getUserInfo().getNickname();
        requestParams.add("card_id",cardNumber);
        requestParams.add("card_name",cardName);
        requestParams.add("card_phone",phoneNumber);
        requestParams.add("verify_code",code);
        requestParams.add("member_name",nickname);

        String binderUrl =HttpUtil.URL_BINDER_CARD;
        HttpUtil.post(binderUrl, new AsyncHttpResponseHandler() {
            ProgressDialog pd;
            @Override
            public void onStart() {
                super.onStart();

                pd = ProgressDialog.show(mContext, null, "正在绑定...",
                        true, true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                sendCancelMessage();
                            }
                        });
                pd.setCanceledOnTouchOutside(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                // TODO: 绑定成功以后 的逻辑 跳转到支付页面去支付
                YkLog.e("bangding",string+"......");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ToastUtils.showMessage(mContext,"请检查网络重新绑定");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                pd.dismiss();
            }
        });
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    private int time = 60;
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            time--;
            if (time > 0) {
                mSendCode.setTextColor(getResources().getColor(R.color.grey_ca));
                mSendCode.setEnabled(false);
                mSendCode.setText(time + "s");
                mHandler.postDelayed(runnable, 1000);
            } else {
                time = 60;
                mSendCode.setEnabled(true);
                mSendCode.setText("发送");

            }
        }
    };

    private void sendCode(String phone_number) {

        String getCodeUrl =
                "http://shop.trqq.com/mobile/index.php?act=shopcard_interface&op=send_auth_code&type=mobile&phone_num="+phone_number;

        HttpUtil.get(getCodeUrl,new AsyncHttpResponseHandler() {

            ProgressDialog pd ;
            @Override
            public void onStart() {
                super.onStart();
                pd = ProgressDialog.show(mContext, null, "正在发送...",
                        true, true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                sendCancelMessage();
                            }
                        });
                pd.setCanceledOnTouchOutside(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String response = jsonObject.optString("msg");

                    ToastUtils.showMessage(mContext,response);
                    String string1 = jsonObject.optString("state");
                    if(string1.trim().equals("true")){
                        mHandler.removeCallbacksAndMessages(null);
                        mHandler.postDelayed(runnable,1000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ToastUtils.showMessage(mContext,"请检查网络重新获取验证码");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                pd.dismiss();
            }
        });
    }
}
