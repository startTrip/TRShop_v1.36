package shop.trqq.com.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.AddressBean;
import shop.trqq.com.ui.AddressPopupWindow.OnOptionsSelectListener;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;

/**
 * 地址编辑
 */
public class address_editeActivity extends BaseActivity {
    private Context mContext;
    private EditText name, phone, tel, addressDetail;
    private TextView address;
    private Button save;
    private AddressBean addressBean;
    private String area_id = "-1";
    private String city_id = "-1";
    private Boolean add_flag;
    // 标题栏标题
    private TextView mHeadTitleTextView;
    //地址选择器
    private AddressPopupWindow mAddressPopupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_edit);
        mContext = this;
        Intent intent = getIntent();
        //bean需要继承Serializable
        addressBean = (AddressBean) intent.getSerializableExtra("AddressBean");
        area_id = addressBean.getArea_id();
        city_id = addressBean.getCity_id();
        initTitleBarView();
        initView();
        //  loadingGetCityData("0");
        //  ADDAddressData();
    }

    @Override
    protected void onResume() {
        add_flag = true;
        // TODO Auto-generated method stub
        super.onResume();
    }

    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("修改收货地址");
    }

    public void initView() {
        name = (EditText) findViewById(R.id.address_manage2_name);
        phone = (EditText) findViewById(R.id.address_manage2_mobileNum);
        tel = (EditText) findViewById(R.id.address_manage2_tel);
        addressDetail = (EditText) findViewById(R.id.address_manage2_detail);
        address = (TextView) findViewById(R.id.address_manage2_address);

        save = (Button) findViewById(R.id.address_manage2_save);
        name.setText(addressBean.getTrue_name());
        phone.setText(addressBean.getMob_phone());
        tel.setText(addressBean.getTel_phone());
        address.setText(addressBean.getArea_info());
        addressDetail.setText(addressBean.getAddress());

        mAddressPopupWindow = new AddressPopupWindow(mContext);
        mAddressPopupWindow.setOnoptionsSelectListener(new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(String options1, String option2, String options3) {
                // TODO Auto-generated method stub
                add_flag = true;
                String addResult = options1;
                city_id = option2;
                area_id = options3;
                ToastUtils.showMessage(mContext, addResult);
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
                if (!mAddressPopupWindow.isShowing())
                    EditCityData();
            }
        });
    }

    private void EditCityData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("address_id", addressBean.getAddress_id());
        requestParams.add("true_name", name.getText().toString());
        requestParams.add("area_id", area_id);
        requestParams.add("city_id", city_id);
        requestParams.add("area_info", address.getText().toString());
        requestParams.add("address", addressDetail.getText().toString());
        requestParams.add("tel_phone", tel.getText().toString());
        requestParams.add("mob_phone", phone.getText().toString());
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

/*	 @Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);

			if (resultCode == Activity.RESULT_OK) {
				add_flag = true;
				Bundle bundle = data.getExtras();
				String addResult = bundle.getString("result");
				city_id= bundle.getString("city_id");
				area_id= bundle.getString("area_id");
				ToastUtils.showMessage(mContext, addResult);
				address.setText(addResult);
				// resultTextView.setText(scanResult);
			}
		}*/
}
