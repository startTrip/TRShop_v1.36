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
 * �������     ������Ʒ�Ľ���
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
    // ����������
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
    private String[] mItems = {"���ϸ���"};
    // private String[] disItems = { "˳��", "��ͨ", "����" };
    /**
     * ֧���б��
     **/
    private static final int DIALOG_1 = 1;
    /**
     * �����б��
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
    // ���ؽ���Activity
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

        // ������Ʒ�� id �� ����
        cart_id = getIntent().getStringExtra("cart_id");
        // ifcart���Ƿ��ﳵ��0=��������,1=���ﳵ
        ifcart = getIntent().getStringExtra("ifcart");
        buystep_flag = getIntent().getStringExtra("buystep_flag");
        user = (LinearLayout) findViewById(R.id.balance_user);
        name = (TextView) findViewById(R.id.balance_name);
        phoneNum = (TextView) findViewById(R.id.balance_phoneNum);
        address = (TextView) findViewById(R.id.balance_address);
        progressActivity = (ProgressActivity) findViewById(R.id.checkout_progress);
        /**
         * ��ʼ����������ͼ
         */
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("�˶Թ�����Ϣ");
        pay = (LinearLayout) findViewById(R.id.balance_pay);
        pay_type = (TextView) findViewById(R.id.balance_pay_type);
        pay_type.setText("���ϸ���");
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
        // ֧������
        PayPassword = (EditText) findViewById(R.id.PayPassword);
        // �˶�����
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
        // ʹ�ó�ֵ��֧��
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
        // ʹ��Ԥ����֧��
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

    // �����������Լ���ʼ��
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

    // Activity�ش���ֵ�ǻص�
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ����ش��ĵ�ַ��Ϣ
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            Message msg = new Message();
            msg.setData(bundle);
            msg.what = 2;
            handler.sendMessage(msg);
        }

        // �ش���Ʊ��Ϣ
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

            // ����û���Ϣ Layout ����
            case R.id.balance_user:
                // UIHelper.showAddressList(mContext, freight_hash);
                Intent intent = new Intent(mContext, address_listActivity.class);
                // �˷�
                intent.putExtra("freight_hash", freight_hash);
                // �ص�
                startActivityForResult(intent, 0);
                // context.startActivity(intent);
                break;

            // ���֧����ʽ
            case R.id.balance_pay:
                CreatDialog(DIALOG_1);
                break;
		/*
		 * case R.id.balance_dis: CreatDialog(DIALOG_2); break;
		 */
            //�����Ʊ��Ϣ
            case R.id.balance_invoice:
                Intent Invoiceintent = new Intent(mContext,
                        invoice_listActivity.class);
                Invoiceintent.putExtra("invoice_id", invoice_id);
                Invoiceintent.putExtra("invoice_message", invoice_message_text);
                // �ص�
                startActivityForResult(Invoiceintent, 1);
                // UIHelper.showInvoiceList(mContext);
                break;

            // �˶�����
            case R.id.check_password:
                check_password();
                break;

            // �ύ����
            case R.id.balance_submit:
                // �жϵ�ַ�Ƿ�Ϊ��
                if (address_id == "" || address_id == null) {
                    ToastUtils.showMessage(mContext, "�˶�һ�����ĵ�ַ��Ϣ");
                    break;
                } else {
                    // û��ѡ��Ԥ����
                    if (available[0] == "0" && available[1] == "0") {
                        //ChangeAddressListData();

                        // ���ع�������
                        loadOnlineBuyStep2Data();
                        // ѡ��Ԥ����
                    } else if ((available[0] == "1" || available[1] == "1")
                            && is_pay) {
                        // offpay_hash �Ƿ�֧�ֻ�������hash��ͨ�������ջ���ַ�ӿڻ��
                        // offpay_hash_batch �����Ƿ�֧�ֻ�������hash
                        // ����ChangeAddressListData��ȡ������Ϣ���ύ����Ҫ�õ�
                        //ChangeAddressListData();
                        loadOnlineBuyStep2Data();
                    } else {
                        ToastUtils.showMessage(mContext, "��ѡ����Ԥ������ֵ��������������벢ȷ��");
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
                //builder.setTitle("֧����ʽ");
                builder.setItems(mItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // ����󵯳�����ѡ���˵ڼ���
                        pay_type.setText(mItems[which]);
                        pay_name = mItems[which].equals("���ϸ���") ? "online"
                                : "offline";
                    }
                });
                break;
		/*
		 * case DIALOG_2: builder.setTitle("���ͷ�ʽ"); builder.setItems(disItems,
		 * new DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int which) { // ����󵯳�����ѡ���˵ڼ���
		 * dis_type.setText(disItems[which]); } }); break;
		 */
        }
        builder.create().show();
    }

    /**
     * ���ؽ����б�
     */
    private void loadOnlineBuyStep1Data() {

        progressActivity.showLoading();
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("cart_id", cart_id);     // ��Ʒ�� id �� ����
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
                            //��Ʊ��ϢHash
                            vat_hash = jsonObjects.optString("vat_hash");
                            // �˷� Hash, ѡ�����ʱ��Ϊ�ύ
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
                            // ��Ʊ��Ϣ
                            JSONObject inv_info = jsonObjects
                                    .optJSONObject("inv_info");
                            // ��ƱId
                            invoice_id = inv_info.optString("invoice_id");
                            // ��Ʊ��Ϣ����
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
                                available_predeposit.setText("ʹ��Ԥ����֧�����������Ϊ����"
                                        + predeposit + ")");
                                available_predeposit.setVisibility(View.VISIBLE);
                                availableLayout.setVisibility(View.VISIBLE);
                            }
                            String rc_balance = jsonObjects
                                    .optString("available_rc_balance");
                            //YkLog.t(rc_balance);
                            if (!"null".equals(rc_balance)) {
                                available_rc_balance.setText("ʹ�ó�ֵ��֧������ֵ�����Ϊ����"
                                        + rc_balance + ")");
                                available_rc_balance.setVisibility(View.VISIBLE);
                                availableLayout.setVisibility(View.VISIBLE);
                            }
                            // �ı��ַ��Ϣ

                            ChangeAddressListData(jsonObjects);
/*                            ifshow_offpay = jsonObjects
                                    .optBoolean("ifshow_offpay");
                            if (ifshow_offpay) {// �Ƿ�֧�ֻ�������
                                mItems = new String[]{"���ϸ���", "��������"};
                            }*/
                        } catch (Exception e) {
                            e.printStackTrace();
                            Drawable emptyDrawable = getResources()
                                    .getDrawable(R.drawable.ic_empty);
                            progressActivity.showEmpty(emptyDrawable, "���ݶ�ȡ����",
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
                                        "���翪��С��",
                                        "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����",
                                        "��������", new OnClickListener() {
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
                        // ���ܳɹ�����ʧ�ܣ���Ҫ���������رյ�
                        // mNetProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * �ύ����
     */
    private void loadOnlineBuyStep2Data() {
        RequestParams requestParams = new RequestParams();
        // String pd_pay
        String key = UserManager.getUserInfo().getKey();
        voucher = mListViewCheack_outAdapter.getVoucherStr();
        // System.err.println(voucher);
        requestParams.add("key", key);      // ����
        requestParams.add("cart_id", cart_id);  // ���ﳵ����Ʒ�� id�Ͷ�Ӧ������
        requestParams.add("ifcart", ifcart);   // �Ƿ��Ǵӹ��ﳵ������
        requestParams.add("address_id", address_id); // ��ַ
        requestParams.add("vat_hash", vat_hash);
        requestParams.add("offpay_hash", offpay_hash);
        requestParams.add("offpay_hash_batch", offpay_hash_batch);
        requestParams.add("pay_name", pay_name);
        requestParams.add("invoice_id", invoice_id);
        requestParams.add("voucher", voucher);// ����ȯ��voucher_t_id|store_id|voucher_price����������ö��ŷָ�
        requestParams.add("pd_pay", available[1]);// �Ƿ�ʹ��Ԥ���֧�� 1-ʹ�� 0-��ʹ��
        requestParams.add("rcb_pay", available[0]);// �Ƿ�ʹ�ó�ֵ��֧�� 1-ʹ�� 0-��ʹ��
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

                                    //  ��ת����������Ľ���
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

                        // ���ܳɹ�����ʧ�ܣ���Ҫ���������رյ�
                        // mNetProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    // �ı��ַ�б�����
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
                        //����Ҫ��ȡ�Ƿ�֧�ֻ�������
                        JSONObject jsonObject = new JSONObject(jsonString)
                                .getJSONObject("datas");
                        offpay_hash = jsonObject.optString("offpay_hash");
                        offpay_hash_batch = jsonObject
                                .optString("offpay_hash_batch");
                        allow_offpay= jsonObject
                                .optString("allow_offpay");
                        if("1".equals(allow_offpay)){
                            //�رջ�������
//                            mItems = new String[]{"���ϸ���", "��������"};
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
                                "���翪��С��",
                                "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����",
                                "��������", new OnClickListener() {
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

    // ���֧������
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
                                            "���ι��ｫ��ʹ�ó�ֵ��������Ԥ�������");
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

    // ʹ��Handler ������Ϣ
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:// �����ܽ��
                    totalPriceTextView.setText("��:"
                            + msg.getData().getFloat("store_sumPrice"));
                    break;
                case 2:// ���µ�ַ��Ϣ
                    Bundle bundle = msg.getData();
                    address_id = bundle.getString("address_id");
                    city_id = bundle.getString("city_id");
                    area_id = bundle.getString("area_id");
                    name.setText(bundle.getString("true_name"));
                    address.setText(bundle.getString("area_info") + " "
                            + bundle.getString("address"));
                    phoneNum.setText("  " + bundle.getString("mob_phone"));
                    break;
                case 3:// ���·�Ʊ��Ϣ
                    Bundle bundle3 = msg.getData();
                    invoice_id = bundle3.getString("invoice_id");
                    invoice_message_text = bundle3.getString("invoice_message");
                    invoice_message.setText(invoice_message_text);
                    break;
                case 4:// ����F����Ϣ
                    is_fcode = msg.getData().getBoolean("Is_fcode");
                    if (is_fcode) {
                        fcode_Layout = (LinearLayout) findViewById(R.id.fcode_Layout);
                        fcode_EditText = (EditText) findViewById(R.id.fcode_EditText);
                        fcode_Layout.setVisibility(View.VISIBLE);
                    }
                    break;
			/*
			 * case 5:// ����ȯ��voucher_t_id|store_id|voucher_price����������ö��ŷָ�
			 * voucher= msg.getData().getString("voucher");
			 * voucher=mListViewCheack_outAdapter.getVoucherStr(); break;
			 */
                default:
                    break;
            }
        }
    };

    // ȡStore_Cart_ListBean
    private void setSpeacBean(JSONObject jsonObject,
                              ArrayList<Store_Cart_ListBean> list,JSONObject content) {
        int i = 0;
        // ����
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
                // System.err.println("�̵�����" + list.get(i).getStore_name());
                i++;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
