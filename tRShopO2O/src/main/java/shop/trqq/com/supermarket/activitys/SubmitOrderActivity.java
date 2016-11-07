package shop.trqq.com.supermarket.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import shop.trqq.com.supermarket.bean.OrderBuy2Data;
import shop.trqq.com.supermarket.utils.CalculateArriveTime;
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
    private String freight_hash;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                JSONObject jsonObject = (JSONObject) msg.obj;
                JSONObject store_cart_list = jsonObject.optJSONObject("store_cart_list");
                if (store_cart_list != null) {

                    String string = store_cart_list.optString("126");

                    GoodsInfo goodsInfo = mGson.fromJson(string, GoodsInfo.class);

                    // 根据距离设置送达时间
                    setArriveTime(goodsInfo);

                    int size = goodsInfo.getGoods_list().size();
                    mStoreData.add(goodsInfo);
                    mCheckOrderStoreAdapter.addDatas(mStoreData);

                    JSONObject jsonObject1 = store_cart_list.optJSONObject("126");
                    mStoreGoodsTotal = jsonObject1.optString("store_goods_total");
                    String store_shipping = jsonObject1.optString("store_shipping");
                    if (!TextUtils.isEmpty(store_shipping)) {
                        Float i= Float.parseFloat(mStoreGoodsTotal) + Float.parseFloat(store_shipping);
                        mCheckMoney.setText("￥"+String.format("%.2f",i));
                    }else {
                        Float i= Float.parseFloat(mStoreGoodsTotal)+10;
                        mCheckMoney.setText("￥"+String.format("%.2f",i));
                    }
                    mGoodsSum.setText(size+"");
                }

//                    // 设置地址信息
//                    JSONObject add_js = jsonObject
//                            .optJSONObject("address_info");
//                    // 已经有地址信息
//                    if (add_js!=null){
//                        address_id = add_js.optString("address_id");
//                        city_id = add_js.optString("city_id");
//                        area_id = add_js.optString("area_id");
//
//                        mName.setText(add_js.getString("true_name"));
//                        mAddress_info.setText(add_js.optString("area_info")
//                                + " " + add_js.optString("address"));
//                        mPhoneNumber.setText("  "
//                                + add_js.getString("mob_phone"));
//                    }else {
                mChangeAdressLayout.setVisibility(View.GONE);
                mNoAddressLayout.setVisibility(View.VISIBLE);
                mCheckSubmit.setEnabled(false);
//                    }
                if (mProgressActivity.isLoading()){
                    mProgressActivity.showContent();
                }
                ChangeAddressListData();
            }
        }
    };

    private void setArriveTime(GoodsInfo goodsInfo) {
        SharedPreferences sharedPreferences = AppConfig.getSharedPreferences(SubmitOrderActivity.this);
        float distance = sharedPreferences.getFloat("distance", -1.0f);

        // 得到预计的送达时间
        String arriveTime = CalculateArriveTime.calculateDateByDistance(distance);
        goodsInfo.setArrive_time(arriveTime);
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
    }


    private void initData() {

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
        // 点击提交订单监听
        mCheckSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断地址是否为空
                if (address_id == "" || address_id == null) {
                    ToastUtils.showMessage(SubmitOrderActivity.this, "核对一下您的地址信息");
                }else {
                    // 提交订单
                    SubmitOrder();

                }

            }
        });

        // 点击更换地址
        mChangeAdressLayout.setOnClickListener(this);
        mNoAddressLayout.setOnClickListener(this);
    }

    // 提交订单
    private void SubmitOrder() {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);      // 令牌
        requestParams.add("cart_id", mCart_id);  // 购物车中商品的 id和对应的数量
        requestParams.add("ifcart", mIfcart);   // 是否是从购物车中来的
        requestParams.add("address_id", address_id); // 地址
        requestParams.add("vat_hash", vat_hash);
        requestParams.add("offpay_hash", mOffpay_hash);
        requestParams.add("offpay_hash_batch", mOffpay_hash_batch);
        requestParams.add("pay_name", "online");
        requestParams.add("invoice_id", "");
        requestParams.add("voucher","");// 代金券，voucher_t_id|store_id|voucher_price，多个店铺用逗号分割
        requestParams.add("pd_pay", "0");// 是否使用预存款支付 1-使用 0-不使用
        requestParams.add("rcb_pay", "0");// 是否使用充值卡支付 1-使用 0-不使用
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
                        String order_sn = jsonObject1.optString("order_sn");
                        String pay_sn = jsonObject1.optString("pay_sn");
                        if(!TextUtils.isEmpty(order_sn)){
                            OrderBuy2Data orderBuy2Data = mGson.fromJson(jsonObject1.toString(), OrderBuy2Data.class);
                            // 跳转到确认订单支付界面
                            Intent intent = new Intent(SubmitOrderActivity.this,ConfirmPayOrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("orderInfo",orderBuy2Data);
                            bundle.putString("state","0");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(SubmitOrderActivity.this,ConfirmPayOrderActivity.class);
                            String checkMoney = mCheckMoney.getText().toString().substring(1);
                            Bundle bundle = new Bundle();
                            bundle.putString("state","1");
                            bundle.putString("checkMoney",checkMoney);
                            bundle.putString("pay_sn",pay_sn);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        // 销毁生成订单页面
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

        requestParams.add("cart_id", mCart_id);     // 商品的 id 和 数量
        requestParams.add("ifcart", mIfcart);

        HttpUtil.post(HttpUtil.URL_BUY_STEP1, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                try {
                    JSONObject jsonObject1 = new JSONObject(string);
                    JSONObject jsonObjects = jsonObject1.optJSONObject(("datas"));
                    //发票信息Hash
                    vat_hash = jsonObjects.optString("vat_hash");
                    // 运费 Hash, 选择地区时作为提交
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
                        "网络开了小差",
                        "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试",
                        "重新连接", new View.OnClickListener() {
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
        // 地址回传的信息
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
             // 锟斤拷锟斤拷锟结交锟斤拷锟斤拷
             mCheckSubmit.setEnabled(true);
             String ship = bundle.getString("ship");
             if (!TextUtils.isEmpty(ship)) {
                 Float i = Float.parseFloat(mStoreGoodsTotal) + Float.parseFloat(ship);
                 mCheckMoney.setText(String.format("%.2f",i));
                 for(int j = 0; j < mStoreData.size(); j++){
                     GoodsInfo goodsInfo = mStoreData.get(j);
                     goodsInfo.setStore_shipping(ship);
                 }
                 mCheckOrderStoreAdapter.addDatas(mStoreData);
                 mCheckOrderStoreAdapter.notifyDataSetChanged();
             }
         }else {
             mNoAddressLayout.setVisibility(View.VISIBLE);
             mChangeAdressLayout.setVisibility(View.GONE);
             mCheckSubmit.setEnabled(false);
         }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no_address_layout:
                case R.id.check_address_layout:

                Intent intent1 = new Intent(SubmitOrderActivity.this, address_listActivity.class);
                    // 运费
                intent1.putExtra("freight_hash",freight_hash);
                intent1.putExtra("cart_id",mCart_id);
                intent1.putExtra("ifcart",mIfcart);
                    // 回传结果
                startActivityForResult(intent1, 0);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
