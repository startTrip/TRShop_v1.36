package shop.trqq.com.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.ui.AddressPopupWindow.OnOptionsSelectListener;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;

/**
 * 添加新的地址
 */
public class address_newActivity extends BaseActivity {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_new);
        mContext = this;

        initView();
        // loadingGetCityData("19");
        // SendCityData();

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

        mAddressPopupWindow = new AddressPopupWindow(mContext);
        mAddressPopupWindow
                .setOnoptionsSelectListener(new OnOptionsSelectListener() {

                    @Override
                    public void onOptionsSelect(String options1,
                                                String option2, String options3) {
                        // TODO Auto-generated method stub
                        add_flag = true;
                        String addResult = options1;
                        // 得到城市和地区的id
                        city_id = option2;
                        area_id = options3;
                        ToastUtils.showMessage(mContext, addResult);
                        add_address.setText(addResult);
                    }
                });

        // 点击添加地址按钮
        add_address_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!mAddressPopupWindow.isShowing())
                    SendCityData();
            }
        });
        // address=(TextView)findViewById(R.id.add_address_address);
        add_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (add_flag || !mAddressPopupWindow.isShowing()) {
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

    private void SendCityData() {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("true_name", name.getText().toString());
        requestParams.add("area_id", area_id);
        requestParams.add("city_id", city_id);
        requestParams.add("area_info", add_address.getText().toString());
        requestParams.add("address", addressDetail.getText().toString());
        requestParams.add("tel_phone", tel.getText().toString());
        requestParams.add("mob_phone", phone.getText().toString());
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
                                    // 添加成功后 finish activity
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
}