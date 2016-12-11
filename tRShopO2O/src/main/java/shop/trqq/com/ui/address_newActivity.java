package shop.trqq.com.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.supermarket.activitys.AddressFromMapActivity;
import shop.trqq.com.supermarket.bean.AddressInfo;
import shop.trqq.com.ui.AddressPopupWindow.OnOptionsSelectListener;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 锟斤拷锟斤拷碌牡锟街?
 */
public class Address_newActivity extends BaseActivity {

    private Context mContext;
    private TextView add_address;
    private Boolean add_flag;
    private EditText name, phone, tel, addressDetail;
    // private TextView address;
    private FrameLayout add_address_add;
    private String area_id = "-1";
    private String city_id = "-1";
    private TextView mHeadTitleTextView;
    private AddressPopupWindow mAddressPopupWindow;
    private String mAddResult;

    private TextView mLocation;
    private AddressInfo mAddressInfo;

    private void SendCityData(AddressInfo addressInfo) {
        if (addressInfo == null) {
            return;
        }

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("true_name",addressInfo.getName());
        requestParams.add("area_id", area_id);
        requestParams.add("city_id", city_id);
        requestParams.add("area_info",addressInfo.getAreaInfo());
        requestParams.add("address", addressInfo.getLocation()+addressInfo.getAddressDetail());
        requestParams.add("tel_phone", addressInfo.getTel_phone());
        requestParams.add("mob_phone", addressInfo.getPhoneNumber());
        requestParams.add("latitude",addressInfo.getLatitude());
        requestParams.add("longitude",addressInfo.getLongitude());
        YkLog.e("intent",requestParams.toString());
        // String uri
        // ="http://shop.trqq.com/mobile/index.php?act=member_address&op=address_add";
        HttpUtil.post(HttpUtil.URL_ADDRESS_ADD, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            // System.err.println(jsonString);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getJSONObject(
                                        "datas").getString("address_id") != null) {
                                    ToastUtils.showMessage(mContext, "添加成功");
                                    // 锟斤拷映晒锟斤拷锟?finish activity
                                    finish();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 得到数据
        SuggestionResult.SuggestionInfo suggestionInfo =  intent.getParcelableExtra("address");

        if (suggestionInfo != null) {
            mLocation.setText(" "+suggestionInfo.key);
            LatLng latLng = suggestionInfo.pt;
            mAddressInfo.setLatitude(latLng.latitude+"");
            mAddressInfo.setLongitude(latLng.longitude+"");

            String city = suggestionInfo.city;
            String district = suggestionInfo.district;

            add_address.setText(""+city+" "+district);
            // 得到从 地图上选择的 地图的 省和市的id
            loadAddressId(city,district);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_new);
        mContext = this;

        initView();
        // loadingGetCityData("19");
        // SendCityData();
        initData();

        setListener();
    }

    @Override
    protected void onResume() {
        add_flag = true;
        // TODO Auto-generated method stub
        super.onResume();
    }

    public void initView() {

        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("添加地址");
        name = (EditText) findViewById(R.id.add_address_name);
        phone = (EditText) findViewById(R.id.add_address_mobileNum);
        // email=(EditText)findViewById(R.id.add_address_email);
        tel = (EditText) findViewById(R.id.add_address_telNum);
        addressDetail = (EditText) findViewById(R.id.add_address_detail);
        add_address_add = (FrameLayout) findViewById(R.id.add_address_add);
        add_address = (TextView) findViewById(R.id.add_address_address);
        mLocation = (TextView)findViewById(R.id.select_location);

        mAddressPopupWindow = new AddressPopupWindow(mContext);
        mAddressPopupWindow
                .setOnoptionsSelectListener(new OnOptionsSelectListener() {

                    @Override
                    public void onOptionsSelect(String options1,
                                                String option2, String options3) {
                        // TODO Auto-generated method stub
                        add_flag = true;
                        mAddResult = options1;
                        // 锟矫碉拷锟斤拷锟叫和碉拷锟斤拷锟絠d
                                city_id = option2;
                        area_id = options3;
                        ToastUtils.showMessage(mContext, mAddResult);
                        add_address.setText(mAddResult);
                    }
                });

        // 锟斤拷锟斤拷锟接碉拷址锟斤拷钮
        add_address_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!mAddressPopupWindow.isShowing()){
                    // 详细的地址信息
                    String mAddressDetail1 = addressDetail.getText().toString().trim();
                    String mAddress = add_address.getText().toString().trim();
                    String mPh = phone.getText().toString().trim();
                    String telephone = tel.getText().toString().trim();
                    String mName = name.getText().toString().trim();
                    String location = mLocation.getText().toString().trim();
                    if (mAddressInfo != null) {
                        mAddressInfo.setName(mName);
                        mAddressInfo.setPhoneNumber(mPh);
                        mAddressInfo.setTel_phone(telephone);
                        mAddressInfo.setAreaInfo(mAddress);
                        mAddressInfo.setAddressDetail(mAddressDetail1);
                        mAddressInfo.setLocation(location);
                        // 检查所填信息是否正确
                        CheckAddressInfo(mAddressInfo);

                    }

                    // 地理编码 去得到该地址的 经纬度
                }
            }
        });
        // address=(TextView)findViewById(R.id.add_address_address);
        add_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (add_flag||!mAddressPopupWindow.isShowing()) {

                    add_flag = false;
                    // UIHelper.showAddressChoose(mContext);
                    mAddressPopupWindow.showAtLocation(add_address,
                            Gravity.BOTTOM, 0, 0);
                    /*
					 * Intent openAddressIntent = new Intent(mContext,
					 * AddressActivity.class);
					 * startActivityForResult(openAddressIntent, 0);
					 */
                }
            }
        });
    }

    // 检查地址信息
    private void CheckAddressInfo(AddressInfo addressInfo) {

        if (TextUtils.isEmpty(addressInfo.getName())) {
            ToastUtils.showMessage(mContext, "请输入姓名");
            return;
        }
        if (addressInfo.getPhoneNumber().length() != 11) {
            ToastUtils.showMessage(mContext, "手机号码输入有误");
            return;
        }
        if (TextUtils.equals(addressInfo.getAreaInfo(),"所在地区")) {
            ToastUtils.showMessage(mContext, "请选择所在的地区");
            return;
        }
        if (TextUtils.isEmpty(addressInfo.getAddressDetail())) {
            ToastUtils.showMessage(mContext, "请输入楼号等信息,否则快递小哥会着急的");
            return;
        }
        if(TextUtils.equals(addressInfo.getLatitude(),"点击选择")){
            ToastUtils.showMessage(mContext,"请选择小区/大厦/学校");
            return;
        }
        // 信息完成这上传服务器
        SendCityData(mAddressInfo);
    }

    private void initData() {

        mAddressInfo = new AddressInfo();


    }

    private void setListener(){

        // 点击选择跳转的 地图上我的位置
        mLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到订单页面
                Intent intent = new Intent(mContext,AddressFromMapActivity.class);

                startActivityForResult(intent,200);
                overridePendingTransition(R.anim.push_right_in,R.anim.push_left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode == 200){
                PoiInfo poiInfo = data.getParcelableExtra("poiInfo");
                ReverseGeoCodeResult.AddressComponent addressDetail = data.getParcelableExtra("address");
                if (poiInfo != null) {
                    mLocation.setText(" "+ poiInfo.name);
                    LatLng latLng = poiInfo.location;
                    mAddressInfo.setLatitude(latLng.latitude+"");
                    mAddressInfo.setLongitude(latLng.longitude+"");
                }
                if (addressDetail!= null) {
                    String province = addressDetail.province;
                    String city = addressDetail.city;
                    String district = addressDetail.district;
                    String location = province+" "+city+" "+district;
                    // 省市区
                    String address = add_address.getText().toString().trim();
                    Log.d("address",1+"");
                    if(!TextUtils.equals(address,location)){  // 用户的 省市区和返回的不一样
                        Log.d("address",2+"");
                        add_address.setText(province+" "+city+" "+district);
                        // 得到从 地图上选择的 地图的 省和市的id
                        loadAddressId(city,district);
                    }

                }
            }
        }
    }

    private void loadAddressId(String city, String district) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key",key);
        requestParams.add("city",city);
        requestParams.add("district",district);
        HttpUtil.post(HttpUtil.URL_ADDRESS_ID, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String string = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("datas");
                    city_id = jsonObject1.optString("city_id");
                    area_id = jsonObject1.optString("district_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}