package shop.trqq.com.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.ListViewCheack_outAdapter;
import shop.trqq.com.bean.AddressBean;
import shop.trqq.com.bean.Store_Cart_ListBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.MyListView;

/**
 * 结算界面     用于商品的结算
 */
public class CheckOutActivity extends BaseActivity implements OnClickListener {
    private LinearLayout user;
    private TextView name;
    private TextView phoneNum;
    private TextView address;
    private LinearLayout pay;
    private TextView pay_type;
    /*
     * private LinearLayout dis; private TextView dis_type;
     */
    private LinearLayout invoice;
    private TextView invoice_message;
    // 标题栏标题
    private TextView mHeadTitleTextView;
    /*
     * private LinearLayout goods; private TextView goods_num; private
     * LinearLayout redPaper; private TextView redPaper_name; private
     * LinearLayout score; private TextView score_num;
     */
    private TextView fees;
    private CheckBox available_rc_balance;
    private CheckBox available_predeposit;
    private LinearLayout availableLayout;

    private LinearLayout PayPasswordLayout;
    private EditText PayPassword;
    private Button check_password;

    private LinearLayout fcode_Layout;
    private EditText fcode_EditText;

    private TextView totalPriceTextView;
    private FrameLayout submit;
    private TextView text_balance_redPaper;
    private TextView text_balance_score;
    private ImageView arrow_balance_redpocket;
    private ImageView arrow_balance_score;
    private Context mContext;
    private ArrayList<AddressBean> addressInfoList;
    private ArrayList<Store_Cart_ListBean> store_Cart_List;
    private MyListView mListView;
    private ListViewCheack_outAdapter mListViewCheack_outAdapter;
    private Gson gson;
    private String[] mItems = {"线上付款"};
    // private String[] disItems = { "顺丰", "申通", "邮政" };
    /**
     * 支付列表框
     **/
    private static final int DIALOG_1 = 1;
    /**
     * 配送列表框
     **/
    private static final int DIALOG_2 = 2;
    private String cart_id, buystep_flag, freight_hash, ifcart;
    private String address_id, vat_hash, offpay_hash, offpay_hash_batch,
            pay_name;
    private String invoice_id, invoice_message_text, voucher, pd_pay, password,
            fcode,allow_offpay;
    private Boolean ifshow_offpay;
    private String city_id, area_id;
    private String available[] = {"0", "0"};
    private Boolean is_fcode = false;
    private Boolean is_pay = false;
    // 加载进度Activity
    private ProgressActivity progressActivity;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // loadOnlineBuyStep1Data();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        mContext = this;
        gson = new Gson();
        pay_name = "online";
        voucher = "";

        // 包含商品的 id 和 数量
        cart_id = getIntent().getStringExtra("cart_id");
        // ifcart：是否购物车，0=立即购买,1=购物车
        ifcart = getIntent().getStringExtra("ifcart");
        buystep_flag = getIntent().getStringExtra("buystep_flag");
        user = (LinearLayout) findViewById(R.id.balance_user);
        name = (TextView) findViewById(R.id.balance_name);
        phoneNum = (TextView) findViewById(R.id.balance_phoneNum);
        address = (TextView) findViewById(R.id.balance_address);
        progressActivity = (ProgressActivity) findViewById(R.id.checkout_progress);
        /**
         * 初始化标题栏视图
         */
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("核对购物信息");
        pay = (LinearLayout) findViewById(R.id.balance_pay);
        pay_type = (TextView) findViewById(R.id.balance_pay_type);
        pay_type.setText("线上付款");
        /*
		 * dis = (LinearLayout) findViewById(R.id.balance_dis); dis_type =
		 * (TextView) findViewById(R.id.balance_dis_type);
		 */
        invoice = (LinearLayout) findViewById(R.id.balance_invoice);
        invoice_message = (TextView) findViewById(R.id.balance_invoice_message);

		/*
		 * goods = (LinearLayout) findViewById(R.id.balance_goods); goods_num =
		 * (TextView) findViewById(R.id.balance_goods_num); redPaper =
		 * (LinearLayout) findViewById(R.id.balance_redPaper); redPaper_name =
		 * (TextView) findViewById(R.id.balance_redPaper_name); score =
		 * (LinearLayout) findViewById(R.id.balance_score); score_num =
		 * (TextView) findViewById(R.id.balance_score_num);
		 */

        fees = (TextView) findViewById(R.id.balance_fees);
        totalPriceTextView = (TextView) findViewById(R.id.balance_total);
        submit = (FrameLayout) findViewById(R.id.balance_submit);
        mListView = (MyListView) findViewById(R.id.listView_store_carts);
        availableLayout = (LinearLayout) findViewById(R.id.available_Layout);
        available_predeposit = (CheckBox) findViewById(R.id.available_predeposit);
        available_rc_balance = (CheckBox) findViewById(R.id.available_rc_balance);
        // 支付密码
        PayPassword = (EditText) findViewById(R.id.PayPassword);
        // 核对密码
        check_password = (Button) findViewById(R.id.check_password);
        PayPasswordLayout = (LinearLayout) findViewById(R.id.PayPassword_Layout);

		/*
		 * text_balance_redPaper=(TextView)findViewById(R.id.text_balance_redPaper
		 * );
		 * text_balance_score=(TextView)findViewById(R.id.text_balance_score);
		 * arrow_balance_redpocket
		 * =(ImageView)findViewById(R.id.arrow_balance_redpocket);
		 * arrow_balance_score
		 * =(ImageView)findViewById(R.id.arrow_balance_score);
		 */

        user.setOnClickListener(this);
        pay.setOnClickListener(this);
        // dis.setOnClickListener(this);
        invoice.setOnClickListener(this);

        check_password.setOnClickListener(this);
		/*
		 * goods.setOnClickListener(this); redPaper.setOnClickListener(this);
		 * score.setOnClickListener(this);
		 */
        submit.setOnClickListener(this);
        // 使用充值卡支付
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
        // 使用预付款支付
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

    // 检查密码可视以及初始化
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

    // Activity回传数值是回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 点击回传的地址信息
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            Message msg = new Message();
            msg.setData(bundle);
            msg.what = 2;
            handler.sendMessage(msg);
        }

        // 回传发票信息
         if (resultCode == 3) {
            Bundle bundle = data.getExtras();
            // System.err.println( bundle.getString("invoice_message"));
            Message msg = new Message();
            msg.setData(bundle);
            msg.what = 3;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            // 点击用户信息 Layout 布局
            case R.id.balance_user:
                // UIHelper.showAddressList(mContext, freight_hash);
                Intent intent = new Intent(mContext, address_listActivity.class);
                // 运费
                intent.putExtra("freight_hash", freight_hash);
                // 回调
                startActivityForResult(intent, 0);
                // context.startActivity(intent);
                break;

            // 点击支付方式
            case R.id.balance_pay:
                CreatDialog(DIALOG_1);
                break;
		/*
		 * case R.id.balance_dis: CreatDialog(DIALOG_2); break;
		 */
            //点击发票信息
            case R.id.balance_invoice:
                Intent Invoiceintent = new Intent(mContext,
                        invoice_listActivity.class);
                Invoiceintent.putExtra("invoice_id", invoice_id);
                Invoiceintent.putExtra("invoice_message", invoice_message_text);
                // 回调
                startActivityForResult(Invoiceintent, 1);
                // UIHelper.showInvoiceList(mContext);
                break;

            // 核对密码
            case R.id.check_password:
                check_password();
                break;

            // 提交订单
            case R.id.balance_submit:
                // 判断地址是否为空
                if (address_id == "" || address_id == null) {
                    ToastUtils.showMessage(mContext, "核对一下您的地址信息");
                    break;
                } else {
                    // 没有选择预付款
                    if (available[0] == "0" && available[1] == "0") {
                        //ChangeAddressListData();

                        // 下载购买数据
                        loadOnlineBuyStep2Data();
                        // 选择预付款
                    } else if ((available[0] == "1" || available[1] == "1")
                            && is_pay) {
                        // offpay_hash 是否支持货到付款hash，通过更换收货地址接口获得
                        // offpay_hash_batch 店铺是否支持货到付款hash
                        // 调用ChangeAddressListData获取上面信息，提交订单要用到
                        //ChangeAddressListData();
                        loadOnlineBuyStep2Data();
                    } else {
                        ToastUtils.showMessage(mContext, "您选择了预付款或充值卡付款，请输入密码并确认");
                    }
                }
                break;
        }
    }

    public void CreatDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(
                        this,
                        android.support.v7.appcompat.R.style.Theme_AppCompat_Light_DialogWhenLarge));

        switch (id) {
            case DIALOG_1:
                //builder.setTitle("支付方式");
                builder.setItems(mItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击后弹出窗口选择了第几项
                        pay_type.setText(mItems[which]);
                        pay_name = mItems[which].equals("线上付款") ? "online"
                                : "offline";
                    }
                });
                break;
		/*
		 * case DIALOG_2: builder.setTitle("配送方式"); builder.setItems(disItems,
		 * new DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int which) { // 点击后弹出窗口选择了第几项
		 * dis_type.setText(disItems[which]); } }); break;
		 */
        }
        builder.create().show();
    }

    /**
     * 加载结算列表
     */
    private void loadOnlineBuyStep1Data() {

        progressActivity.showLoading();
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("cart_id", cart_id);     // 商品的 id 和 数量
        requestParams.add("ifcart", ifcart);
        HttpUtil.post(HttpUtil.URL_BUY_STEP1, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.longe("buy1jsonString", jsonString);
                            JSONObject jsonObjects = new JSONObject(jsonString)
                                    .getJSONObject("datas");
                            //发票信息Hash
                            vat_hash = jsonObjects.optString("vat_hash");
                            // 运费 Hash, 选择地区时作为提交
                            freight_hash = jsonObjects
                                    .optString("freight_hash");
                            try {
                                JSONObject add_js = jsonObjects
                                        .getJSONObject("address_info");
                                address_id = add_js.optString("address_id");
                                city_id = add_js.optString("city_id");
                                area_id = add_js.optString("area_id");
                                name.setText(add_js.getString("true_name"));
                                address.setText(add_js.optString("area_info")
                                        + " " + add_js.optString("address"));
                                phoneNum.setText("  "
                                        + add_js.getString("mob_phone"));
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            // 发票信息
                            JSONObject inv_info = jsonObjects
                                    .optJSONObject("inv_info");
                            // 发票Id
                            invoice_id = inv_info.optString("invoice_id");
                            // 发票信息内容
                            invoice_message_text = inv_info
                                    .optString("content");
                            invoice_message.setText(invoice_message_text);
                            /*store_Cart_List = new ArrayList<Store_Cart_ListBean>();
                            setSpeacBean(jsonObjects
                                            .getJSONObject("store_cart_list"),
                                    store_Cart_List);
                            mListViewCheack_outAdapter = new ListViewCheack_outAdapter(
                                    handler, mContext);
                            mListViewCheack_outAdapter.setData(store_Cart_List);
                            mListView.setAdapter(mListViewCheack_outAdapter);
                            float totalPrice = 0;
                            for (int i = 0; i < store_Cart_List.size(); i++) {
                                totalPrice = totalPrice
                                        + store_Cart_List.get(i)
                                        .getStore_goods_total();
                            }*/
                            String predeposit = jsonObjects
                                    .optString("available_predeposit");
                            if (!predeposit.equals("null")) {
                                available_predeposit.setText("使用预付款支付（可用余额为￥："
                                        + predeposit + ")");
                                available_predeposit.setVisibility(View.VISIBLE);
                                availableLayout.setVisibility(View.VISIBLE);
                            }
                            String rc_balance = jsonObjects
                                    .optString("available_rc_balance");
                            //YkLog.t(rc_balance);
                            if (!"null".equals(rc_balance)) {
                                available_rc_balance.setText("使用充值卡支付（充值卡余额为￥："
                                        + rc_balance + ")");
                                available_rc_balance.setVisibility(View.VISIBLE);
                                availableLayout.setVisibility(View.VISIBLE);
                            }
                            // 改变地址信息

                            ChangeAddressListData(jsonObjects);
/*                            ifshow_offpay = jsonObjects
                                    .optBoolean("ifshow_offpay");
                            if (ifshow_offpay) {// 是否支持货到付款
                                mItems = new String[]{"线上付款", "货到付款"};
                            }*/
                        } catch (Exception e) {
                            e.printStackTrace();
                            Drawable emptyDrawable = getResources()
                                    .getDrawable(R.drawable.ic_empty);
                            progressActivity.showEmpty(emptyDrawable, "数据读取错误",
                                    "");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {

                        try {
                            if (store_Cart_List.size() == 0) {
                                Drawable errorDrawable = getResources()
                                        .getDrawable(R.drawable.wifi_off);
                                progressActivity.showError(errorDrawable,
                                        "网络开了小差",
                                        "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试",
                                        "重新连接", new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method
                                                // stub
                                                progressActivity.showLoading();
                                                store_Cart_List.clear();
                                                loadOnlineBuyStep1Data();
                                            }
                                        });
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
//                        progressActivity.showContent();
                        // 不管成功或者失败，都要将进度条关闭掉
                        // mNetProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 提交订单
     */
    private void loadOnlineBuyStep2Data() {
        RequestParams requestParams = new RequestParams();
        // String pd_pay
        String key = UserManager.getUserInfo().getKey();
        voucher = mListViewCheack_outAdapter.getVoucherStr();
        // System.err.println(voucher);
        requestParams.add("key", key);      // 令牌
        requestParams.add("cart_id", cart_id);  // 购物车中商品的 id和对应的数量
        requestParams.add("ifcart", ifcart);   // 是否是从购物车中来的
        requestParams.add("address_id", address_id); // 地址
        requestParams.add("vat_hash", vat_hash);
        requestParams.add("offpay_hash", offpay_hash);
        requestParams.add("offpay_hash_batch", offpay_hash_batch);
        requestParams.add("pay_name", pay_name);
        requestParams.add("invoice_id", invoice_id);
        requestParams.add("voucher", voucher);// 代金券，voucher_t_id|store_id|voucher_price，多个店铺用逗号分割
        requestParams.add("pd_pay", available[1]);// 是否使用预存款支付 1-使用 0-不使用
        requestParams.add("rcb_pay", available[0]);// 是否使用充值卡支付 1-使用 0-不使用
        requestParams.add("password", password);
        fcode = "";
        if (is_fcode) {
            fcode = fcode_EditText.getText().toString();
        }
        requestParams.add("fcode", fcode);
        YkLog.t("pay_name", pay_name);
        HttpUtil.post(HttpUtil.URL_BUY_STEP2, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            // TestUtils.println_out("buy2jsonString",
                            // jsonString);
                            JSONObject jsonObject = new JSONObject(jsonString)
                                    .getJSONObject("datas");
                            try {
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (jsonObject.getString("pay_sn") != null) {
                                    finish();

                                    //  跳转到订单付款的界面
                                    UIHelper.showOrder(mContext, "");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {

                        try {
                            // if (store_Cart_List.size() == 0)
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

    // 改变地址列表数据
    private void ChangeAddressListData(final JSONObject storecartlist) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("freight_hash", freight_hash);
        requestParams.add("city_id", city_id);
        requestParams.add("area_id", area_id);
        String uri = HttpUtil.URL_UPDATE_ADDRESS;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    // System.err.println("URL_UPDATE_ADDRESS" + jsonString);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString)
                                .getJSONObject("datas");
                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        YkLog.t("ChangeAddressListData", jsonString);
                        //还需要获取是否支持货到付款
                        JSONObject jsonObject = new JSONObject(jsonString)
                                .getJSONObject("datas");
                        offpay_hash = jsonObject.optString("offpay_hash");
                        offpay_hash_batch = jsonObject
                                .optString("offpay_hash_batch");
                        allow_offpay= jsonObject
                                .optString("allow_offpay");
                        if("1".equals(allow_offpay)){
                            //关闭货到付款
//                            mItems = new String[]{"线上付款", "货到付款"};
                        }
                        JSONObject content=jsonObject
                                .getJSONObject("content");
                        store_Cart_List = new ArrayList<Store_Cart_ListBean>();
                        setSpeacBean(storecartlist.getJSONObject("store_cart_list"),
                                store_Cart_List,content);
                        mListViewCheack_outAdapter = new ListViewCheack_outAdapter(
                                handler, mContext);
                        mListViewCheack_outAdapter.setData(store_Cart_List);
                        mListView.setAdapter(mListViewCheack_outAdapter);

/*                        float totalPrice = 0;
                        for (int i = 0; i < store_Cart_List.size(); i++) {
                            totalPrice = totalPrice
                                    + store_Cart_List.get(i)
                                    .getStore_goods_total()+ Float.parseFloat(store_Cart_List.get(i)
                                    .getStore_freight());
                        }*/
/*                        for(Store_Cart_ListBean bean:store_Cart_List){
                            String freight=content.getString();
                        }*/
                        //loadOnlineBuyStep2Data();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                try {
                    if (store_Cart_List.size() == 0) {
                        Drawable errorDrawable = getResources()
                                .getDrawable(R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable,
                                "网络开了小差",
                                "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试",
                                "重新连接", new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method
                                        // stub
                                        progressActivity.showLoading();
                                        store_Cart_List.clear();
                                        loadOnlineBuyStep1Data();
                                    }
                                });
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressActivity.showContent();
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
                            // TestUtils.println_out("check_password",jsonString);
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
                                    ToastUtils.showMessage(mContext,
                                            "本次购物将会使用充值卡余额或者预付款余额");
                                    is_pay = true;
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

    // 使用Handler 更新消息
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:// 更新总金额
                    totalPriceTextView.setText("￥:"
                            + msg.getData().getFloat("store_sumPrice"));
                    break;
                case 2:// 更新地址信息
                    Bundle bundle = msg.getData();
                    address_id = bundle.getString("address_id");
                    city_id = bundle.getString("city_id");
                    area_id = bundle.getString("area_id");
                    name.setText(bundle.getString("true_name"));
                    address.setText(bundle.getString("area_info") + " "
                            + bundle.getString("address"));
                    phoneNum.setText("  " + bundle.getString("mob_phone"));
                    break;
                case 3:// 更新发票信息
                    Bundle bundle3 = msg.getData();
                    invoice_id = bundle3.getString("invoice_id");
                    invoice_message_text = bundle3.getString("invoice_message");
                    invoice_message.setText(invoice_message_text);
                    break;
                case 4:// 更新F码信息
                    is_fcode = msg.getData().getBoolean("Is_fcode");
                    if (is_fcode) {
                        fcode_Layout = (LinearLayout) findViewById(R.id.fcode_Layout);
                        fcode_EditText = (EditText) findViewById(R.id.fcode_EditText);
                        fcode_Layout.setVisibility(View.VISIBLE);
                    }
                    break;
			/*
			 * case 5:// 代金券，voucher_t_id|store_id|voucher_price，多个店铺用逗号分割
			 * voucher= msg.getData().getString("voucher");
			 * voucher=mListViewCheack_outAdapter.getVoucherStr(); break;
			 */
                default:
                    break;
            }
        }
    };

    // 取Store_Cart_ListBean
    private void setSpeacBean(JSONObject jsonObject,
                              ArrayList<Store_Cart_ListBean> list,JSONObject content) {
        int i = 0;
        // 无序
        Iterator keyIter = jsonObject.keys();
        while (keyIter.hasNext()) {
            try {
                Store_Cart_ListBean bean = new Store_Cart_ListBean();
                String id = (String) keyIter.next();

                // bean.setSpecID(id);
                bean = gson.fromJson(jsonObject.optString(id),
                        new TypeToken<Store_Cart_ListBean>() {
                        }.getType());
                bean.setStore_freight(content.getString(id));
                list.add(bean);
                // System.err.println("商店名称" + list.get(i).getStore_name());
                i++;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
