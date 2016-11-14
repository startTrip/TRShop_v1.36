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
 * ï¿½ï¿½ï¿½ï¿½ÂµÄµï¿½Ö?
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
    private String mAddResult;

    private void SendCityData() {
        String ph = phone.getText().toString();
        if(ph.trim().length()!=11){
            ToastUtils.showMessage(mContext,"ÊÖ»úºÅÂëÊäÈëÓÐÎó");
        }else {
            RequestParams requestParams = new RequestParams();
            String key = UserManager.getUserInfo().getKey();
            requestParams.add("key", key);
            requestParams.add("true_name", name.getText().toString());
            requestParams.add("area_id", area_id);
            requestParams.add("city_id", city_id);
            requestParams.add("area_info", add_address.getText().toString());
            requestParams.add("address", addressDetail.getText().toString());
            requestParams.add("tel_phone", tel.getText().toString());
            requestParams.add("mob_phone",ph);
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
                                        ToastUtils.showMessage(mContext, "Ìí¼Ó³É¹¦");
                                        // ï¿½ï¿½Ó³É¹ï¿½ï¿½ï¿?finish activity
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
        mHeadTitleTextView.setText("Ìí¼ÓµØÖ·");
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
                        mAddResult = options1;
                        // ï¿½Ãµï¿½ï¿½ï¿½ï¿½ÐºÍµï¿½ï¿½ï¿½ï¿½id
                        city_id = option2;
                        area_id = options3;
                        ToastUtils.showMessage(mContext, mAddResult);
                        add_address.setText(mAddResult);
                    }
                });

        // ï¿½ï¿½ï¿½ï¿½ï¿½Óµï¿½Ö·ï¿½ï¿½Å¥
        add_address_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!mAddressPopupWindow.isShowing()){
                    // µØÀí±àÂë È¥µÃµ½¸ÃµØÖ·µÄ ¾­Î³¶È
                    SendCityData();
                }
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

}