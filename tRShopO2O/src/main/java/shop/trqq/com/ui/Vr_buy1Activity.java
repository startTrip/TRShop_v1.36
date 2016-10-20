package shop.trqq.com.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONObject;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 虚拟购买
 */
public class Vr_buy1Activity extends BaseActivity implements OnClickListener {

    // 标题栏标题
    private TextView mHeadTitleTextView;
    /*
     * private LinearLayout goods; private TextView goods_num; private
     * LinearLayout redPaper; private TextView redPaper_name; private
     * LinearLayout score; private TextView score_num;
     */
    private CheckBox available_rc_balance;
    private CheckBox available_predeposit;

    private LinearLayout PayPasswordLayout;
    private EditText PayPassword;
    private Button check_password;

    private TextView totalPriceTextView;
    private FrameLayout submit;
    private TextView vrbuy1_title;
    private TextView vrbuy1_price;
    private TextView vrbuy1_good_num;
    private ImageView vrbuy1_img;
    private Context mContext;
    private EditText mobilePhone;

    private String cart_id, quantity;
    private String address_id;
    private String password, mobileNum;

    private String available[] = {"0", "0"};
    private Boolean is_pay = false;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        loadOnlineBuyStep1Data();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_buy1);
        mContext = this;
        cart_id = getIntent().getStringExtra("goods_id");
        quantity = getIntent().getStringExtra("quantity");
        /**
         * 初始化标题栏视图
         */
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("核对购物信息");

        mobilePhone = (EditText) findViewById(R.id.mobileNum);
        totalPriceTextView = (TextView) findViewById(R.id.balance_total);
        submit = (FrameLayout) findViewById(R.id.balance_submit);
        available_predeposit = (CheckBox) findViewById(R.id.available_predeposit);
        available_rc_balance = (CheckBox) findViewById(R.id.available_rc_balance);
        PayPassword = (EditText) findViewById(R.id.PayPassword);
        check_password = (Button) findViewById(R.id.check_password);
        PayPasswordLayout = (LinearLayout) findViewById(R.id.PayPassword_Layout);
        vrbuy1_title = (TextView) findViewById(R.id.vrbuy1_title);
        vrbuy1_price = (TextView) findViewById(R.id.vrbuy1_price);
        vrbuy1_good_num = (TextView) findViewById(R.id.vrbuy1_good_num);
        vrbuy1_img = (ImageView) findViewById(R.id.vrbuy1_img);
        check_password.setOnClickListener(this);
        /*
		 * goods.setOnClickListener(this); redPaper.setOnClickListener(this);
		 * score.setOnClickListener(this);
		 */
        submit.setOnClickListener(this);
        available_rc_balance
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            available[0] = "1";
                            check_passwordVisibility();
                        } else {
                            available[0] = "0";
                            check_passwordVisibility();
                        }
                    }
                });
        available_predeposit
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            available[1] = "1";
                            check_passwordVisibility();
                        } else {
                            available[1] = "0";
                            check_passwordVisibility();
                        }
                    }
                });

        loadOnlineBuyStep1Data();

    }

    private void check_passwordVisibility() {
        if (available[0] == "1" || available[1] == "1") {
            PayPasswordLayout.setVisibility(View.VISIBLE);
        } else {
            is_pay = false;
            PayPassword.setText("");
            password = "";
            PayPasswordLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.check_password:
                check_password();
                break;
            case R.id.balance_submit:
                if (mobilePhone.getText().toString().length() >= 11) {
                    // 没有选择预付款
                    if (available[0] == "0" && available[1] == "0") {
                        loadOnlineBuyStep2Data();
                        // 选择预付款
                    } else if ((available[0] == "1" || available[1] == "1")
                            && is_pay) {
                        // offpay_hash 是否支持货到付款hash，通过更换收货地址接口获得
                        // offpay_hash_batch 店铺是否支持货到付款hash
                        // 调用ChangeAddressListData获取上面信息，提交订单要用到
                        loadOnlineBuyStep2Data();
                    } else {
                        ToastUtils.showMessage(mContext, "您选择了预付款或充值卡付款，请输入密码并确认");
                    }
                } else {
                    ToastUtils.showMessage(mContext, "手机号码不能为空或者少于11位数");
                }

                break;
        }
    }

    /**
     * 加载结算列表
     */
    private void loadOnlineBuyStep1Data() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("goods_id", cart_id);
        requestParams.add("quantity", quantity);
        HttpUtil.post(HttpUtil.URL_MEMBER_VR_BUY2, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.longe("vrbuy1jsonString", jsonString);
                            JSONObject jsonObjects = new JSONObject(jsonString)
                                    .getJSONObject("datas");
                            JSONObject goods_info = jsonObjects
                                    .getJSONObject("goods_info");
                            // YkLog.longe("vrbuy1goods_info", goods_info);
                            float price = Float.parseFloat(goods_info
                                    .optString("goods_price"));
                            int good_num = Integer.parseInt(goods_info
                                    .optString("quantity"));
                            vrbuy1_title.setText(goods_info
                                    .optString("goods_name"));
                            vrbuy1_price.setText("单价(元)￥：" + price);
                            vrbuy1_good_num.setText("数量：" + good_num);
                            ImageLoader.getInstance().displayImage(
                                    goods_info.optString("goods_image_url"),
                                    vrbuy1_img,
                                    ImageLoadUtils.getoptions(),
                                    ImageLoadUtils
                                            .getAnimateFirstDisplayListener());
                            totalPriceTextView.setText("￥:" + price * good_num);
                            JSONObject member_info = jsonObjects
                                    .getJSONObject("member_info");
                            available_predeposit.setText("使用预付款支付（可用余额为￥："
                                    + member_info
                                    .optString("available_predeposit")
                                    + ")");
                            available_rc_balance.setText("使用充值卡支付（充值卡余额为￥："
                                    + member_info
                                    .optString("available_rc_balance")
                                    + ")");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {

                        try {
                            // if (cartInfoList.size() == 0)
                            // mLoadunloginLinearLayout.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        // ChangeAddressListData();
                        // 不管成功或者失败，都要将进度条关闭掉
                        // mNetProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 加载结算列表
     */
    private void loadOnlineBuyStep2Data() {
        RequestParams requestParams = new RequestParams();
        mobileNum = mobilePhone.getText().toString();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("goods_id", cart_id);
        requestParams.add("quantity", quantity);
        requestParams.add("buyer_phone", mobileNum);
        requestParams.add("pd_pay", available[1]);// 是否使用预存款支付 1-使用 0-不使用
        requestParams.add("rcb_pay", available[0]);// 是否使用充值卡支付 1-使用 0-不使用
        requestParams.add("password", password);
        // requestParams.add("fcode", "");
        // TestUtils.println_out("pay_name", pay_name);
        HttpUtil.post(HttpUtil.URL_MEMBER_VR_BUY3, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("vr_buy", jsonString);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                finish();
                                UIHelper.showOrder(mContext, "虚拟");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {

                        try {
                            // if (cartInfoList.size() == 0)
                            // mLoadunloginLinearLayout.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                        // 不管成功或者失败，都要将进度条关闭掉
                        // mNetProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    // 检查支付密码
    private void check_password() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        password = PayPassword.getText().toString();
        requestParams.add("key", key);
        requestParams.add("password", password);
        HttpUtil.post(HttpUtil.URL_CHECK_PASSWORD, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("check_password", jsonString);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getString(
                                        "datas").equals("1")) {
                                    is_pay = true;
                                    ToastUtils.showMessage(mContext,
                                            "本次购物将会使用充值卡余额或者预付款余额");
                                    PayPasswordLayout.setVisibility(View.GONE);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        ToastUtils.showMessage(getApplicationContext(),
                                R.string.get_informationData_failure);
                    }
                });
    }

}
