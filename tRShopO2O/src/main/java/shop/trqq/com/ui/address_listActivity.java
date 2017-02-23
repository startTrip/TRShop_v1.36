package shop.trqq.com.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.roamer.slidelistview.SlideListView;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.AppContext;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.RecyclerAddressListAdapter;
import shop.trqq.com.bean.AddressBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.widget.DialogTool;

/**
 * 地址列表界面
 */
public class address_listActivity extends BaseActivity implements RecyclerAddressListAdapter.onRecyclerItemClick {

    private Context mContext;
    private TextView mHeadTitleTextView;

    private ProgressActivity progressActivity;
    private LinearLayout address_add;
    private SlideListView listView;
    private ArrayList<AddressBean> addressList;
    private Gson gson;
    private Boolean flag = false;
    private String freight_hash;
    private String mIfcart;
    private String mCart_id;
    private int mPos;
    private int mIfmarket;
    private Drawable mErrorDrawable;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerAddressListAdapter mRecyclerAddressListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_list);
        mContext = this;

        gson = new Gson();
        addressList = new ArrayList<AddressBean>();
        initTitleBarView();
        initView();

        initData();
    }

    private void initView() {
        progressActivity = (ProgressActivity) findViewById(R.id.Addresslist_progress);
        mRecyclerView = (RecyclerView)findViewById(R.id.address_recycler);

        address_add = (LinearLayout) findViewById(R.id.layout_bottom);
    }


    private void initData() {

        Intent intent = getIntent();
        freight_hash =intent.getStringExtra("freight_hash");
        mIfcart = intent.getStringExtra("ifcart");
        mCart_id = intent.getStringExtra("cart_id");
        mIfmarket = intent.getIntExtra("ifmarket",0);

        mRecyclerAddressListAdapter = new RecyclerAddressListAdapter(mContext,addressList);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecyclerAddressListAdapter);

        mRecyclerAddressListAdapter.setOnRecyclerItemClick(this);
        address_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UIHelper.showAddressNew(mContext);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingAddressListData();
    }

    public void setAddress() {

        if (addressList.size() == 0) {
            listView.setVisibility(View.GONE);
            Resources resource = (Resources) getBaseContext().getResources();
            String non = resource.getString(R.string.non_address);
            ToastUtils.showMessage(mContext, non);
        } else {
            listView.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 初始化标题栏
     */
    private void initTitleBarView() {

        mHeadTitleTextView = (TextView) findViewById(R.id.title_address);
        mHeadTitleTextView.setText("管理收货地址");
    }

    private void loadingAddressListData() {
        progressActivity.showLoading();
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        String uri = HttpUtil.URL_ADDRESS_LIST;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    Log.d("addresslist",jsonString);

                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    String add_list = jsonObjects.getString("address_list");
                    addressList = gson.fromJson(add_list,
                            new TypeToken<List<AddressBean>>() {
                            }.getType());

                    mRecyclerAddressListAdapter.setData(addressList);
                    mRecyclerAddressListAdapter.notifyDataSetChanged();

                    if (addressList.size() > 0) {
                        progressActivity.showContent();
                    } else {
                        // 目前还没有地址
                       showAddressEmpty();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                try {
                    mErrorDrawable = getResources().getDrawable(R.drawable.wifi_off);
                    progressActivity.showError(mErrorDrawable, "网络开了小差",
                            "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试",
                            "重新连接",
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    addressList.clear();
                                    loadingAddressListData();
                                }
                            });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void ChangeAddressListData(final int position) {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("freight_hash", freight_hash);
        requestParams.add("ifcart",mIfcart);
        requestParams.add("cart_id",mCart_id);
        requestParams.add("city_id", addressList.get(position).getCity_id());
        requestParams.add("area_id", addressList.get(position).getArea_id());

        String uri = HttpUtil.URL_UPDATE_ADDRESS;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override

            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);

                    Log.d("address",jsonString);
                    JSONObject jsonObject = new JSONObject (jsonString)
                            .getJSONObject("datas");
                    String errStr = jsonObject.optString("error");
                    if (!TextUtils.isEmpty(errStr)) {
                        ToastUtils.showMessage(mContext, errStr);
                    }{
                        JSONObject jsonObject1 = jsonObject.optJSONObject("content");
                        String content = jsonObject.optString("content");
                        String ship = jsonObject1.optString("126");
                        String offpay_hash = jsonObject.optString("offpay_hash");
                        String offpay_hash_batch = jsonObject.optString("offpay_hash_batch");
                        Intent it = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("ship",ship);
                        // 如果是超市跳转过来就判断经纬度
                        if(mIfmarket==1){
                                String longitude = addressList.get(position).getLongitude();
                                String latitude = addressList.get(position).getLatitude();
                                if(TextUtils.isEmpty(longitude)||TextUtils.isEmpty(latitude)){  // 经纬度为空
                                    // 显示提示信息
                                    showAlertDialog(position);
                                }else {
                                    double distance = getDistanceToMarket(latitude, longitude);
                                    // 如果小于 5 公里
                                    if(distance>5000){
                                        // 显示提醒信息
                                        showAlertDialog(position);
                                    }else {     // 大于 5 公里
                                        bundle.putString("longitude",longitude);
                                        bundle.putString("latitude",latitude);
                                        bundle.putString("content",content);
                                        bundle.putString("offpay_hash",offpay_hash);
                                        bundle.putString("offpay_hash_batch",offpay_hash_batch);
                                        bundle.putString("address_id", addressList.get(position).getAddress_id());
                                        bundle.putString("city_id", addressList.get(position).getCity_id());
                                        bundle.putString("area_id", addressList.get(position).getArea_id());
                                        bundle.putString("true_name", addressList.get(position).getTrue_name());
                                        bundle.putString("area_info", addressList.get(position).getArea_info());
                                        bundle.putString("address", addressList.get(position).getAddress());
                                        bundle.putString("mob_phone", addressList.get(position).getMob_phone());
                                        it.putExtras(bundle);
                                        setResult(Activity.RESULT_OK, it);
                                        finish();
                                    }
                                }
                        }else {  // 不是超市就回传这些数据
//
                            bundle.putString("content",content);
                            bundle.putString("offpay_hash",offpay_hash);
                            bundle.putString("offpay_hash_batch",offpay_hash_batch);
                            bundle.putString("address_id", addressList.get(position).getAddress_id());
                            bundle.putString("city_id", addressList.get(position).getCity_id());
                            bundle.putString("area_id", addressList.get(position).getArea_id());
                            bundle.putString("true_name", addressList.get(position).getTrue_name());
                            bundle.putString("area_info", addressList.get(position).getArea_info());
                            bundle.putString("address", addressList.get(position).getAddress());
                            bundle.putString("mob_phone", addressList.get(position).getMob_phone());
                            it.putExtras(bundle);
                            setResult(Activity.RESULT_OK, it);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                try {

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }

    private void showAlertDialog(final int position) {
        DialogTool.createNormalDialog2(mContext,
                "超市只配送五公里范围,以前的地址不能购买",
                "选择其它地址","去新增地址", null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //没有经纬度 跳转到 地址编辑界面去从地图上面选择经纬度的地址
                        UIHelper.showAddressNew(mContext);
                    }
                }).show();
    }

    private void showAddressEmpty(){
        Drawable emptyDrawable = getResources().getDrawable(
                R.drawable.ic_empty);
        progressActivity.showEmpty(emptyDrawable,"地址为空","你还没有添加收货地址");
    }
    /**
     *   计算距离超市的距离
     * @param latitude
     * @param longitude
     * @return
     */
    private double getDistanceToMarket(String latitude, String longitude) {
        LatLng marketLatLng = new LatLng(AppContext.marketLatitude,AppContext.marketLongitude);
        LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
        double distance = DistanceUtil.getDistance(marketLatLng, latLng);
        return distance;
    }

    // 按返回键返回
    public void backPress(View view){
        finish();
    }

    // 删除地址
    private void AddressDelete(String address_id) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("address_id", address_id);
        HttpUtil.post(HttpUtil.URL_ADDRESS_DETELE, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString)
                                .getJSONObject("datas");
                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                        }
                    } catch (Exception e) {
                        if (new JSONObject(jsonString).getString("datas")
                                .equals("1")) {
                            ToastUtils.showMessage(mContext, R.string.delete_success);
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
                ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
            }
        });
    }

    // recyclerView Item 的点击事件
    @Override
    public void onItemClick(int index) {
        Log.d("index",index+"");
        if(freight_hash.equals("")){
        }else {
            mPos = index;
            ChangeAddressListData(mPos);
        }
    }

    @Override
    public void onAddressDeleteClick(int position) {
        Log.d("index",position+"");

        AddressBean addressBean = addressList.remove(position);
        if (addressBean != null) {
            AddressDelete(addressBean.getAddress_id());
        }else {
            ToastUtils.showMessage(mContext,getString(R.string.delete_failure));
        }
        mRecyclerAddressListAdapter.setData(addressList);
        Log.d("size",addressList.size()+"数量");
        if(addressList.size()==0){
            showAddressEmpty();
        }
    }
}
