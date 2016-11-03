package shop.trqq.com.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;

public class RegisterActivity extends BaseActivity {
    private Context mContext;
    private Button register;
    private EditText usernanme;
    private EditText password;
    private EditText password_confirm;
    private EditText phone_num;
    private ImageView code_image;
    private Button code_button;
    private EditText code_edittext;
    private CheckBox mAgreement;
    private TextView mAgreement_Text;
    //标题栏标�?
    private TextView mHeadTitleTextView;
    private EditText mVerify_code;
    private TextView mGetCode;

    private EditText mPayPassword;
    private EditText mPayConfirm;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private int time = 60;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = this;
        initTitleBarView();

        // ע�ᰴť
        register = (Button) findViewById(R.id.register_register);
        // �û���
        usernanme = (EditText) findViewById(R.id.register_name);
        // ����
        password = (EditText) findViewById(R.id.register_password1);
        //  ȷ������
        password_confirm = (EditText) findViewById(R.id.register_password2);
        //  ����֧������
        mPayPassword = (EditText) findViewById(R.id.pay_password1);
        //  ȷ������֧������
        mPayConfirm = (EditText) findViewById(R.id.pay_password2);
        //  �ֻ�����
        phone_num = (EditText) findViewById(R.id.register_phone_num);
        //  ������֤��
        mVerify_code = (EditText) findViewById(R.id.register_verify_code);

        mGetCode = (TextView) findViewById(R.id.register_getcode);
//        //  ��֤�밴ť
//        code_button = (Button) findViewById(R.id.register_code_button);
//        //   ������֤��
//        code_edittext = (EditText) findViewById(R.id.register_code_edittext);
//        //   ��֤��ͼƬ
//        code_image = (ImageView) findViewById(R.id.register_code_image);
        mAgreement_Text = (TextView) findViewById(R.id.register_agreement_Text);
        mAgreement = (CheckBox) findViewById(R.id.register_agreement);

//        code_image.setImageBitmap(CodeUtils.getInstance().createBitmap());
        // System.out.println("��֤�룺" + CodeUtils.getInstance().getCode());
        // �����ȡ ������֤��
        mGetCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                    String url = HttpUtil.URL_REGISTER_GET_CODE + phone_num.getText().toString();

                    HttpUtil.get(url, new AsyncHttpResponseHandler() {
                        ProgressDialog pd ;
                        @Override
                        public void onStart() {
                            super.onStart();
                            pd = ProgressDialog.show(mContext, null, "���ڷ���...",
                                    true, true, new OnCancelListener() {
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
                                    handler.removeCallbacksAndMessages(null);
                                    handler.postDelayed(runnable,1000);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            ToastUtils.showMessage(mContext,"�����������»�ȡ��֤��");
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            pd.dismiss();
                        }
                    });
            }
        });

//        code_button.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//
//                code_image.setImageBitmap(CodeUtils.getInstance()
//                        .createBitmap());
//                // System.out.println("��֤�룺" +
//                // CodeUtils.getInstance().getCode());
//            }
//        });
        // ��� �Ķ��û�����Э��
        mAgreement_Text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UIHelper.showProductinfo(mContext, "", "");
            }
        });
        // �������ע��
        register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                register();
            }
        });
    }

    // ��ʱ60s
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if(time>0){
                mGetCode.setTextColor(getResources().getColor(R.color.black_black_text_color));
                mGetCode.setEnabled(false);
                mGetCode.setText(time+" s");
                handler.postDelayed(runnable,1000);
            }else {
                time = 60;
                mGetCode.setEnabled(true);
                mGetCode.setTextColor(getResources().getColor(R.color.red_text_color));
                mGetCode.setText("�� ��");
            }
        }
    };
    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("ע��");
    }

    // ע�᷽�����ڽ���ע��
    private void register() {

        String loginId = usernanme.getText().toString().trim();
        String password1 = password.getText().toString().trim();
        String password2 = password_confirm.getText().toString().trim();
        String phoneNum = phone_num.getText().toString().trim();
        String payPassword = mPayPassword.getText().toString().trim();
        String payPasswordConfirm = mPayConfirm.getText().toString().trim();
        String verify_code = mVerify_code.getText().toString().trim();

        if (!mAgreement.isChecked()) {
            ToastUtils.showMessage(mContext, "���Ķ���ͬ���û�����Э��");
            return;
        }
//        // ��֤����Դ�СдequalsIgnoreCase
//        if (!codeStr.equalsIgnoreCase(CodeUtils.getInstance().getCode())) {
//            ToastUtils.showMessage(mContext, "��֤���������");
//            return;
//        }
        if (loginId.equals("")) {
            ToastUtils.showMessage(mContext, "�˺Ų���Ϊ��");
            return;
        }
        //���Դ�Сд
        Pattern pattern = Pattern.compile("(^(tr|xtr)[0-9]*)|[0-9]*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(loginId);
        if (matcher.matches()) {
            ToastUtils.showMessage(mContext, "��ʽΪtr+������xtr+���֡������ֵ��û�������ע��");
            return;
        }
        if (password1.equals("")) {
            ToastUtils.showMessage(mContext, "���벻��Ϊ��");
            return;
        }

//        // ��������ʽȥ��֤�ֻ����Ƿ���ȷ(��̨��֤)
//        if (!isMobileNO(phoneNum)){
//            ToastUtils.showMessage(mContext, "��������ȷ���ֻ���");
//            return;
//        }
        if (!password1.equals(password2)) {
            ToastUtils.showMessage(mContext, "�����������벻ͬ");
            return;
        }
        if (!payPassword.equals(payPasswordConfirm)){
            ToastUtils.showMessage(mContext, "����֧�����벻ͬ");
            return;
        }
        RequestParams requestParams = new RequestParams();

        requestParams.add("username", loginId);
        requestParams.add("password", password1);
        requestParams.add("password_confirm", password2);
        requestParams.add("paypwd",payPassword);
        requestParams.add("paypwd_confirm",payPasswordConfirm);
        requestParams.add("phone_num",phoneNum);
        requestParams.add("verify_code", verify_code);
        requestParams.add("client", "android");

        HttpUtil.post(HttpUtil.URL_REGISTER, requestParams,
                new AsyncHttpResponseHandler() {
                    ProgressDialog pd;

                    @Override
                    public void onStart() {
                        super.onStart();

                        pd = ProgressDialog.show(mContext, null, "����ע��...",
                                true, true, new OnCancelListener() {
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
                        try {
                            String jsonStr = new String(responseBody);
                            // System.out.println(jsonStr);
                            JSONObject jsonObject = new JSONObject(jsonStr)
                                    .getJSONObject("datas");
                            Log.d("jsonStr",jsonStr);

                            try {
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                String userInfo = new JSONObject(jsonStr)
                                        .getString("datas");

                                // �����ݲ��뱾�����ã��ɹ�����ת��������
                                if (UserManager.jsonToBean(mContext, userInfo)) {
                                    // ע��ɹ�
                                    finish();
                                    UserManager.setUserInfo(true);
                                    ToastUtils.showMessage(mContext, "ע��ɹ�");
                                } else {
                                    ToastUtils.showMessage(mContext,
                                            R.string.request_failed);
                                }
                            }
                        } catch (Exception e) {
                            ToastUtils.showMessage(mContext,
                                    R.string.request_failed);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        pd.dismiss();
                    }
                });

    }

    @Override
    protected void onDestroy() {
        if (handler != null) {

            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    // ��֤�ֻ���Ƿ����?
//    public static boolean isMobileNO(String mobiles) {
//        Pattern p = Pattern
//                .compile("^(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])\\d{8}$");
//        Matcher m = p.matcher(mobiles);
//        return m.matches();
//    }
}
