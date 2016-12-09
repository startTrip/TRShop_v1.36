package shop.trqq.com.supermarket.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import shop.trqq.com.AppConfig;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.supermarket.adapters.CheckOrderStoreAdapter;
import shop.trqq.com.supermarket.bean.GoodsInfo;
import shop.trqq.com.supermarket.utils.DistanceUtils;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.ui.address_listActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;

public class SubmitOrderActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int SDK_PAY_FLAG = 1;
    private RelativeLayout mChangeAdressLayout;
    private ImageView mTvBack;
    private TextView mName;
    private TextView mPhoneNumber;
    private TextView mAddress_info;
    private String mCart_id;
    private String mIfcart;
    private String address_id;
    private String city_id;
    private String area_id;
    private String vat_hash;
    private String password;
    private String freight_hash;
    private String available[] = {"0", "0"};
    private DistanceUtils mDistanceUtils;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                JSONObject jsonObject = (JSONObject) msg.obj;
                JSONObject store_cart_list = jsonObject.optJSONObject("store_cart_list");
                if (store_cart_list != null) {

                    String string = store_cart_list.optString("126");

                    GoodsInfo goodsInfo = mGson.fromJson(string, GoodsInfo.class);

                    // �����ʹ�ʱ��
                    goodsInfo.setStore_shipping("0.0");
                    goodsInfo.setArrive_time("��ѡ���ַ");
                    int size = goodsInfo.getGoods_list().size();
                    mStoreData.add(goodsInfo);

                    mCheckOrderStoreAdapter.addDatas(mStoreData);

                    JSONObject jsonObject1 = store_cart_list.optJSONObject("126");
                    mStoreGoodsTotal = jsonObject1.optString("store_goods_total");

                    Float i = Float.parseFloat(mStoreGoodsTotal);
                    mCheckMoney.setText("��" + String.format("%.2f", i));

                    mGoodsSum.setText(size + "");
                }

//                // ���õ�ַ��Ϣ
//                JSONObject add_js = jsonObject
//                        .optJSONObject("address_info");
//                 // �Ѿ��е�ַ��Ϣ
//                if (add_js != null) {
//                    address_id = add_js.optString("address_id");
//                    city_id = add_js.optString("city_id");
//                    area_id = add_js.optString("area_id");
//
//                    mName.setText(add_js.optString("true_name"));
//                    mAddress_info.setText(add_js.optString("area_info")
//                            + " " + add_js.optString("address"));
//                    mPhoneNumber.setText("optString"
//                            + add_js.optString("mob_phone"));
//
//                } else {
                mChangeAdressLayout.setVisibility(View.GONE);
                mNoAddressLayout.setVisibility(View.VISIBLE);
                mCheckSubmit.setEnabled(false);
//                }
                // ʹ��Ԥ���֧��
                String predeposit = jsonObject
                        .optString("available_predeposit");
                if (!predeposit.equals("null")) {
                    available_predeposit.setText("ʹ��Ԥ����֧�����������Ϊ����"
                            + predeposit + ")");
                    available_predeposit.setVisibility(View.VISIBLE);
                    availableLayout.setVisibility(View.VISIBLE);
                }
                // ʹ�ó�ֵ��֧��
                String rc_balance = jsonObject
                        .optString("available_rc_balance");
                //YkLog.t(rc_balance);
                if (!"null".equals(rc_balance)) {
                    available_rc_balance.setText("ʹ�ó�ֵ��֧������ֵ�����Ϊ����"
                            + rc_balance + ")");
                    available_rc_balance.setVisibility(View.VISIBLE);
                    availableLayout.setVisibility(View.VISIBLE);
                }
                if (mProgressActivity.isLoading()) {
                    mProgressActivity.showContent();
                }
                ChangeAddressListData();
            }
        }
    };
    private boolean hasSubmit = true;
    private String mDistance;
    private LinearLayout availableLayout;
    private CheckBox available_predeposit;
    private CheckBox available_rc_balance;
    private EditText PayPassword;
    private Button check_password;
    private LinearLayout PayPasswordLayout;
    private Context mContext;
    private boolean is_pay;

    private String getArriveTime(float distance) {
        // �õ�Ԥ�Ƶ��ʹ�ʱ��
        String arriveTime = DistanceUtils.calculateDateByDistance(distance);
        return arriveTime;
    }

    private float getDistance(){
        SharedPreferences sharedPreferences = AppConfig.getSharedPreferences(SubmitOrderActivity.this);
        float distance = sharedPreferences.getFloat("distance", -1.0f);
        return distance;
    }

    private ArrayList<GoodsInfo> mStoreData;
    private CheckOrderStoreAdapter mCheckOrderStoreAdapter;
    private ArrayList<GoodsInfo> mList;
    private ListView mStoreListView;
    private TextView mGoodsSum;
    private TextView mCheckMoney;
    private Button mCheckSubmit;
    private Gson mGson;
    private ProgressActivity mProgressActivity;
    private RelativeLayout mNoAddressLayout;
    private String mOffpay_hash;
    private String mOffpay_hash_batch;
    private String mStoreGoodsTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);
        initView();
        initData();

        setListener();
    }

    private void initView() {
        mProgressActivity = (ProgressActivity) findViewById(R.id.submitorder_progress);

        mChangeAdressLayout = (RelativeLayout) findViewById(R.id.check_address_layout);
        mNoAddressLayout = (RelativeLayout) findViewById(R.id.no_address_layout);

        mTvBack = (ImageView) findViewById(R.id.checkout_back);
        mName = (TextView) findViewById(R.id.receiver_name);
        mPhoneNumber = (TextView) findViewById(R.id.phone_number);
        mAddress_info = (TextView) findViewById(R.id.address_info);

        mStoreListView = (ListView) findViewById(R.id.checkout_store_list);

        mGoodsSum = (TextView) findViewById(R.id.check_goods_sum);
        mCheckMoney = (TextView) findViewById(R.id.check_sum_money);
        mCheckSubmit = (Button)findViewById(R.id.bt_check_submit);


        availableLayout = (LinearLayout) findViewById(R.id.available_Layout);
        available_predeposit = (CheckBox) findViewById(R.id.available_predeposit);
        available_rc_balance = (CheckBox) findViewById(R.id.available_rc_balance);
        // 支付密码
        PayPassword = (EditText) findViewById(R.id.PayPassword);
        // 核对密码
        check_password = (Button) findViewById(R.id.check_password);
        PayPasswordLayout = (LinearLayout) findViewById(R.id.PayPassword_Layout);

    }


    private void initData() {

        mContext = SubmitOrderActivity.this;

        Intent intent = getIntent();
        mCart_id = intent.getStringExtra("cart_id");
        mIfcart = intent.getStringExtra("ifcart");

        mGson = new Gson();

        mStoreData = new ArrayList<>();
        mList = new ArrayList<>();

        mCheckOrderStoreAdapter = new CheckOrderStoreAdapter(this, mList);

        mStoreListView.setAdapter(mCheckOrderStoreAdapter);


        mProgressActivity.showLoading();
        loadOnlineBuyStep1Data();

    }

    private void ChangeAddressListData() {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("distance","100");
        requestParams.add("freight_hash", freight_hash);
        requestParams.add("ifcart", mIfcart);
        requestParams.add("cart_id", mCart_id);
        requestParams.add("city_id", city_id);
        requestParams.add("area_id", area_id);
        String uri = HttpUtil.URL_UPDATE_ADDRESS;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("datas");
                    String errStr = jsonObject1.optString("error");

                    if (!TextUtils.isEmpty(errStr)) {
//                        ToastUtils.showMessage(SubmitOrderActivity.this, errStr);
                    }else {
                        mOffpay_hash = jsonObject1.optString("offpay_hash");
                        mOffpay_hash_batch = jsonObject1.optString("offpay_hash_batch");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void setListener() {

        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // ����ύ��������?
        mCheckSubmit.setOnClickListener(this);

        // ������ַ
        mChangeAdressLayout.setOnClickListener(this);
        mNoAddressLayout.setOnClickListener(this);

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
        check_password.setOnClickListener(this);

    }

    // �����������Լ���ʼ��
    private void check_passwordVisibility() {
        if (available[0] == "1" || available[1] == "1") {
            PayPasswordLayout.setVisibility(View.VISIBLE);
        } else {
            PayPassword.setText("");
            is_pay = false;
            password = "";
            PayPasswordLayout.setVisibility(View.INVISIBLE);
        }
    }

    // �ύ����
    private void SubmitOrder() {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);      // ����
        requestParams.add("cart_id", mCart_id);  // ���ﳵ����Ʒ�� id�Ͷ�Ӧ������
        requestParams.add("ifcart", mIfcart);   // �Ƿ��Ǵӹ��ﳵ������
        requestParams.add("address_id", address_id); // ��ַ
        requestParams.add("distance",mDistance);
        requestParams.add("vat_hash", vat_hash);
        requestParams.add("offpay_hash", mOffpay_hash);
        requestParams.add("offpay_hash_batch", mOffpay_hash_batch);
        requestParams.add("pay_name", "online");
        requestParams.add("invoice_id", "");
        requestParams.add("voucher","");// ����ȯ��voucher_t_id|store_id|voucher_price����������ö��ŷָ�
        requestParams.add("pd_pay", "0");// �Ƿ�ʹ��Ԥ���֧�� 1-ʹ�� 0-��ʹ��
        requestParams.add("rcb_pay", "0");// �Ƿ�ʹ�ó�ֵ��֧�� 1-ʹ�� 0-��ʹ��
        requestParams.add("fcode","");
        HttpUtil.post(HttpUtil.URL_BUY_STEP2, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("datas");
                    String error = jsonObject1.optString("error");
                    if (!TextUtils.isEmpty(error)) {
                        ToastUtils.showMessage(SubmitOrderActivity.this,error);
                    }else {
                        String pay_sn = jsonObject1.optString("pay_sn");
//                        Intent intent = new Intent(SubmitOrderActivity.this,ConfirmPayOrderActivity.class);
//                        String checkMoney = mCheckMoney.getText().toString().substring(1);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("state","1");
//                        bundle.putString("checkMoney",checkMoney);
//                        bundle.putString("pay_sn",pay_sn);
//                        intent.putExtras(bundle);
                        //  跳转到订单付款的界面
                        UIHelper.showOrder(SubmitOrderActivity.this, "");
//                        startActivity(intent);
                        // �����ɶ���ҳ��
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void loadOnlineBuyStep1Data(){


        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();

        requestParams.add("key",key);

        Log.d("key",key);
        requestParams.add("cart_id", mCart_id);     // ��Ʒ�� id �� ����
        requestParams.add("ifcart", mIfcart);

        HttpUtil.post(HttpUtil.URL_BUY_STEP1, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);

                try {
                    JSONObject jsonObject1 = new JSONObject(string);
                    JSONObject jsonObjects = jsonObject1.optJSONObject(("datas"));
                    //��Ʊ��ϢHash
                    vat_hash = jsonObjects.optString("vat_hash");
                    // �˷� Hash, ѡ�����ʱ��Ϊ�ύ
                    freight_hash = jsonObjects
                            .optString("freight_hash");

                    Message message = new Message();
                    message.obj = jsonObjects;
                    message.what = 1;
                    mHandler.sendMessage(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Drawable errorDrawable = getResources()
                        .getDrawable(R.drawable.wifi_off);
                mProgressActivity.showError(errorDrawable,
                        "���翪��С��",
                        "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����",
                        "��������", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method
                                // stub
                                mProgressActivity.showLoading();
                                loadOnlineBuyStep1Data();
                            }
                        });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // ��ַ�ش�����Ϣ
        if(requestCode==0&&resultCode== Activity.RESULT_OK){
            mNoAddressLayout.setVisibility(View.GONE);
            mChangeAdressLayout.setVisibility(View.VISIBLE);
            Bundle bundle = data.getExtras();
            address_id = bundle.getString("address_id");
            city_id = bundle.getString("city_id");
            area_id = bundle.getString("area_id");
            mName.setText(bundle.getString("true_name"));
            mAddress_info.setText(bundle.getString("area_info") + " "
                    + bundle.getString("address"));
            mPhoneNumber.setText("  " + bundle.getString("mob_phone"));
            mOffpay_hash = bundle.getString("offpay_hash");
            mOffpay_hash_batch = bundle.getString("offpay_hash_batch");
            // �����ύ����
            mCheckSubmit.setEnabled(true);
            String ship = bundle.getString("ship");
            mDistance = bundle.getString("distance");


            /**
             *  -1     û�ж�λ������ľ�γ��
             *  0      �幫�ﷶΧ֮��
             *  1      �幫�ﵽʮ����֮��
             *  2      ����ʮ���ﷶΧ��
             */
            switch (mDistance) {
                case "-1":
                    if(TextUtils.equals(ship,"0")){  // û�еõ���ַ�ľ�γ�� ���ڳ࿲����ϼɽ��֮��
                        float distance1 = getDistance();   // �õ�ǰ���볬��ȥ������ʱ��
                        UpdateAdapter(ship,distance1);
                    }else {                            // 3��֮���ʹ�
                        UpdateAdapter(ship, 20000f);
                    }
                    break;
                case "0":    // 5���ﷶΧ֮��
                    UpdateAdapter(ship,3000f);
                    break;
                case "1":
                    UpdateAdapter(ship,8000f);
                    break;
                case "2":
                    UpdateAdapter(ship,20000f);
                    break;
            }
        }else {
            Float i = Float.parseFloat(mStoreGoodsTotal);
            mCheckMoney.setText("��"+String.format("%.2f",i));
            for(int j = 0; j < mStoreData.size(); j++){
                GoodsInfo goodsInfo = mStoreData.get(j);
                goodsInfo.setStore_shipping("0.0");
                goodsInfo.setArrive_time("��ѡ���ַ");
            }
            mCheckOrderStoreAdapter.addDatas(mStoreData);
            mCheckOrderStoreAdapter.notifyDataSetChanged();
            mNoAddressLayout.setVisibility(View.VISIBLE);
            mChangeAdressLayout.setVisibility(View.GONE);
            mCheckSubmit.setEnabled(false);
        }
    }

    // ��������
    private void UpdateAdapter(String ship,Float f) {


        for(int j = 0; j < mStoreData.size(); j++){
            GoodsInfo goodsInfo = mStoreData.get(j);
            String arriveTime = getArriveTime(f);
            goodsInfo.setArrive_time(arriveTime);
            goodsInfo.setStore_shipping(ship);
        }
        mCheckOrderStoreAdapter.addDatas(mStoreData);
        mCheckOrderStoreAdapter.notifyDataSetChanged();

        Float i = Float.parseFloat(mStoreGoodsTotal) + Float.parseFloat(ship);
        mCheckMoney.setText("��"+String.format("%.2f",i));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_address_layout:
            case R.id.check_address_layout:
                hasSubmit = true;
                Intent intent1 = new Intent(SubmitOrderActivity.this, address_listActivity.class);
                // �˷�
                intent1.putExtra("freight_hash",freight_hash);
                intent1.putExtra("cart_id",mCart_id);
                intent1.putExtra("ifcart",mIfcart);
                // �ش����
                startActivityForResult(intent1, 0);

                break;
            case R.id.bt_check_submit:
                // ����ַ�Ƿ�Ϊ��
                if (address_id == "" || address_id == null) {
                    ToastUtils.showMessage(SubmitOrderActivity.this, "�˶�һ�����ĵ�ַ��Ϣ");
                }else {
                    if(hasSubmit){

                        hasSubmit = false; // ��ֹ�ظ��ύ
                        // �ύ����
                        SubmitOrder();
                    }

                }
                break;
            case R.id.check_password:
                check_password();
                break;
        }
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
                                            "���ι��ｫ��ʹ�ù��￨��������֧��");
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


    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
