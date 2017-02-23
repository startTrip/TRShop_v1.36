package shop.trqq.com.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import shop.trqq.com.bean.AddressBean;
import shop.trqq.com.supermarket.activitys.AddressFromMapActivity;
import shop.trqq.com.supermarket.bean.AddressInfo;
import shop.trqq.com.supermarket.view.MyPopupWindow;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;

/**
 * 地址编辑
 */
public class Address_editeActivity extends BaseActivity {
    private Context mContext;
    private EditText name, phone, tel, addressDetail;
    private TextView address;
    private FrameLayout save;
    private AddressBean addressBean;
    private String area_id = "-1";
    private String city_id = "-1";
    private Boolean add_flag;
    // 标题栏标题
    private TextView mHeadTitleTextView;
    //地址选择器
    private AddressPopupWindow mAddressPopupWindow;
    private TextView mLocation;
    private LatLng mLatLng;
    private AddressInfo mAddressInfo;
    private View mPopupView;
    private MyPopupWindow mMyPopupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_edit);
        mContext = this;

        initTitleBarView();
        initView();

        initData();
        //  loadingGetCityData("0");
        //  ADDAddressData();
        setListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 得到数据
        SuggestionResult.SuggestionInfo suggestionInfo =  intent.getParcelableExtra("address");

        if (suggestionInfo != null) {
            mLocation.setText(" "+suggestionInfo.key);
            mLatLng = suggestionInfo.pt;
            mAddressInfo.setLatitude(mLatLng.latitude+"");
            mAddressInfo.setLongitude(mLatLng.longitude+"");

            String city = suggestionInfo.city;
            String district = suggestionInfo.district;

            address.setText(""+city+" "+district);
            // 得到从 地图上选择的 地图的 省和市的id
            loadAddressId(city,district);
        }
    }


    @Override
    protected void onResume() {
        add_flag = true;
        // TODO Auto-generated method stub
        super.onResume();
    }

    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.title_address);
        mHeadTitleTextView.setText("修改地址");
    }

    public void initView() {

        mPopupView = LayoutInflater.from(mContext).inflate(R.layout.popup_center_layout, null);

        name = (EditText) findViewById(R.id.address_manage2_name);
        phone = (EditText) findViewById(R.id.address_manage2_mobileNum);
        tel = (EditText) findViewById(R.id.address_manage2_tel);
        address = (TextView) findViewById(R.id.address_manage2_address);

        // 选择小区
        mLocation = (TextView)findViewById(R.id.select_location);
        // 手动填写的
        addressDetail = (EditText) findViewById(R.id.add_address_detail);

        save = (FrameLayout) findViewById(R.id.address_manage2_save);

    }

    private void initData() {

        mMyPopupWindow = MyPopupWindow.getInstance();

        mAddressInfo = new AddressInfo();
        Intent intent = getIntent();
        //bean需要继承Serializable
        addressBean = (AddressBean) intent.getSerializableExtra("AddressBean");
        area_id = addressBean.getArea_id();
        city_id = addressBean.getCity_id();

        name.setText(addressBean.getTrue_name());
        phone.setText(addressBean.getMob_phone());
        tel.setText(addressBean.getTel_phone());
        address.setText(addressBean.getArea_info());


        String latitude = addressBean.getLatitude();
        String longitude = addressBean.getLongitude();
        String garden = addressBean.getGarden();
        String address = addressBean.getAddress();
        // 没有选择小区
        if(TextUtils.isEmpty(garden)||TextUtils.isEmpty(latitude)){
            mLocation.setText(" 点击选择");
            addressDetail.setText("");
        }else {
            mAddressInfo.setLatitude(latitude);
            mAddressInfo.setLongitude(longitude);
            mLatLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
            mLocation.setText(garden);
            addressDetail.setText(address.replace(garden,""));
        }

        mAddressPopupWindow = new AddressPopupWindow(mContext);
    }

    private void setListener() {

        // 点击选择跳转的 地图上我的位置
        mLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到订单页面
                Intent intent = new Intent(mContext,AddressFromMapActivity.class);
                String area = address.getText().toString();
                String location = mLocation.getText().toString().trim();
                intent.putExtra("from","edite");
                if(!TextUtils.equals("所在地区",area)){   // 如果手动选了地址
                    intent.putExtra("area",area);
                    intent.putExtra("hasSelected",true);
                    if(!TextUtils.equals("点击选择",location)){  // 已经是返回回来以后的,重新点击以后跳转到刚才的位置
                        intent.putExtra("hasLatlng",true);
                        intent.putExtra("latlng",mLatLng);
                    }
                }
                startActivityForResult(intent,200);
                overridePendingTransition(R.anim.push_right_in,R.anim.push_left_out);
            }
        });

        mAddressPopupWindow.setOnoptionsSelectListener(new AddressPopupWindow.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(String options1, String option2, String options3) {
                // TODO Auto-generated method stub
                add_flag = true;
                String addResult = options1;
                city_id = option2;
                area_id = options3;
                ToastUtils.showMessage(mContext, addResult);
                mLocation.setText(" 点击选择");
                addressDetail.setText("");
                address.setText(addResult);
            }
        });
        address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (add_flag || !mAddressPopupWindow.isShowing()) {
                    add_flag = false;
                    //UIHelper.showAddressChoose(mContext);
                    mAddressPopupWindow.showAtLocation(address, Gravity.BOTTOM, 0, 0);
                    /*	Intent openAddressIntent = new Intent(mContext,
								AddressActivity.class);
						startActivityForResult(openAddressIntent, 0);*/
                }
            }
        });
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!mAddressPopupWindow.isShowing()){
                    // 详细的地址信息
                    String mAddressDetail1 = addressDetail.getText().toString().trim();
                    String mAddress = address.getText().toString().trim();
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
                // 编辑地址
                EditCityData(mAddressInfo);
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
                    mLatLng = poiInfo.location;
                    mAddressInfo.setLatitude(mLatLng.latitude+"");
                    mAddressInfo.setLongitude(mLatLng.longitude+"");
                }
                if (addressDetail!= null) {
                    String province = addressDetail.province;
                    String city = addressDetail.city;
                    String district = addressDetail.district;
                    String location = province+" "+city+" "+district;
                    // 省市区
                    String address1 = address.getText().toString().trim();

                    if(!TextUtils.equals(address1,location)){  // 用户的 省市区和返回的不一样

                        address.setText(province+" "+city+" "+district);
                        // 得到从 地图上选择的 地图的 省和市的id
                        loadAddressId(city,district);
                    }
                }
            }
        }
    }

    // 通过城市和地区去 下载地区 id;
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
        if (TextUtils.equals(addressInfo.getAreaInfo(), "所在地区")) {
            ToastUtils.showMessage(mContext, "请选择所在的地区");
            return;
        }
        if (TextUtils.isEmpty(addressInfo.getAddressDetail())) {
            ToastUtils.showMessage(mContext, "请输入地址详细信息,否则快递小哥会着急的");
            return;
        }
        if (TextUtils.equals(addressInfo.getLocation(), "点击选择")) {
            ToastUtils.showMessage(mContext, "请选择小区/大厦/学校");
            return;
        }
        // 编辑数据，上传到服务器
        EditCityData(mAddressInfo);
    }

    // 重新编辑数据
    private void EditCityData(AddressInfo addressInfo) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("address_id", addressBean.getAddress_id());
        requestParams.add("true_name", addressInfo.getName());
        requestParams.add("area_id", area_id);
        requestParams.add("city_id", city_id);
        requestParams.add("area_info", addressInfo.getAreaInfo());
        requestParams.add("address", addressInfo.getLocation()+addressInfo.getAddressDetail());
        requestParams.add("tel_phone", tel.getText().toString());
        requestParams.add("mob_phone", phone.getText().toString());
        requestParams.add("garden",addressInfo.getLocation());
        requestParams.add("latitude",addressInfo.getLatitude());
        requestParams.add("longitude",addressInfo.getLongitude());
        //String uri ="http://shop.trqq.com/mobile/index.php?act=member_address&op=address_add";
        HttpUtil.post(HttpUtil.URL_ADDRESS_EDIT, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    System.err.println(jsonString);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString)
                                .getJSONObject("datas");
                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                        }
                    } catch (Exception e) {
                        if (new JSONObject(jsonString).getString("datas").equals("1")) {
                            ToastUtils.showMessage(mContext, "修改成功");
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

                try {
                    //		if (cartInfoList.size() == 0)
                    //	mLoadunloginLinearLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                // 不管成功或者失败，都要将进度条关闭掉
                //	mNetProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            // 警告是否保存当前的地址信息
            showAlertPopupWindow();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showAlertPopupWindow() {
        View animView = mPopupView.findViewById(R.id.popup_anima);
        View dismissView = mPopupView.findViewById(R.id.dismiss_view);
        // 点击确认销毁 activity
        animView.findViewById(R.id.confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        animView.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyPopupWindow.cancel();
            }
        });
        mMyPopupWindow.showNormalPopupWindow(Address_editeActivity.this,mPopupView,dismissView,animView,false);
    }

    // 按返回键 销毁当前的界面
    public void backPress(View view){
        showAlertPopupWindow();
    }
}
