package shop.trqq.com.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

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

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.ListViewAddressAdapter;
import shop.trqq.com.bean.AddressBean;
import shop.trqq.com.supermarket.utils.DistanceUtils;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;

/**
 * ï¿½ï¿½Ö·ï¿½Ð±ï¿½
 */
public class address_listActivity extends BaseActivity {

    private Context mContext;
    // ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½
    private TextView mHeadTitleTextView;
    // ï¿½ï¿½ï¿½Ø½ï¿½ï¿½Activity
    private ProgressActivity progressActivity;
    private TextView address_add;
    private SlideListView listView;
    private ArrayList<AddressBean> addressList;
    private ListViewAddressAdapter addressAdapter;
    private Gson gson;
    private Boolean flag = false;
    private String freight_hash;
    private String mIfcart;
    private String mCart_id;
    private DistanceUtils mDistanceUtils;
    private int mPos;
    private double mDistance1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_list);
        mContext = this;
        gson = new Gson();
        addressList = new ArrayList<AddressBean>();
        initTitleBarView();
        progressActivity = (ProgressActivity) findViewById(R.id.Addresslist_progress);
        listView = (SlideListView) findViewById(R.id.address_manage_list);
        addressAdapter = new ListViewAddressAdapter(mContext, addressList);
        address_add = (TextView) findViewById(R.id.address_add);
        Intent intent = getIntent();
        freight_hash =intent.getStringExtra("freight_hash");
        mIfcart = intent.getStringExtra("ifcart");
        mCart_id = intent.getStringExtra("cart_id");

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //System.err.println(freight_hash);
                if (freight_hash.equals("")) {
                    // ToastUtils.showMessage(mContext, "ï¿½ï¿½Ê±ï¿½ï¿½ï¿½ï¿½ï¿½Þ¸ï¿½");
                } else {
                    mPos = position;          // ¼ÇÂ¼µã»÷µÄListView ÌõÄ¿µÄÎ»ÖÃ
//                    String area_info = addressList.get(position).getArea_info();
//                    String address = addressList.get(position).getAddress();
//                    mDistanceUtils.getLocation(area_info,address);
                    ChangeAddressListData(mPos);
                }
            }
        });
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
     * ï¿½ï¿½Ê¼ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Í¼
     */
    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("ÊÕ»õµØÖ·¹ÜÀí");
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
                    // System.out.println("addlistjsonString" + jsonString);
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    String add_list = jsonObjects.getString("address_list");
                    addressList = gson.fromJson(add_list,
                            new TypeToken<List<AddressBean>>() {
                            }.getType());

                    addressAdapter.setData(addressList);
                    listView.setAdapter(addressAdapter);
                    addressAdapter.notifyDataSetChanged();
                    if (addressList.size() > 0) {
                        progressActivity.showContent();
                    } else {
                        //ï¿½ï¿½ï¿½ï¿½ï¿½Ð©ï¿½ï¿½Í¼ï¿½ã²»ï¿½ï¿½ï¿½ï¿½ï¿½Ø¡ï¿?
                       /* List<Integer> skipIds = new ArrayList<Integer>();
					    skipIds.add(R.id.layout_bottom);*/
                        Drawable emptyDrawable = getResources().getDrawable(
                                R.drawable.ic_empty);
                        progressActivity.showEmpty(emptyDrawable,"µØÖ·Îª¿Õ","Äã»¹Ã»ÓÐÌí¼ÓÊÕ»õµØÖ·");
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                try {
                    Drawable errorDrawable = getResources().getDrawable(
                            R.drawable.wifi_off);
                    progressActivity.showError(errorDrawable, "ÍøÂç¿ªÁËÐ¡²î",
                            "Á¬½Ó²»ÉÏÍøÂç£¬ÇëÈ·ÈÏÒ»ÏÂÄúµÄÍøÂç¿ª¹Ø£¬»òÕß·þÎñÆ÷ÍøÂçÕýÃ¦£¬ÇëÉÔºóÔÙÊÔ",
                            "ÖØÐÂÁ¬½Ó",
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
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
                // ï¿½ï¿½ï¿½Ü³É¹ï¿½ï¿½ï¿½ï¿½ï¿½Ê§ï¿½Ü£ï¿½ï¿½ï¿½Òªï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ø±Õµï¿?
                // mNetProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void ChangeAddressListData(final int position) {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("freight_hash", freight_hash);
//        requestParams.add("distance",distance);
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
//                        ToastUtils.showMessage(mContext, "ï¿½Þ¸Ä³É¹ï¿½");
                        JSONObject jsonObject1 = jsonObject.optJSONObject("content");
                        String content = jsonObject.optString("content");
                        String ship = jsonObject1.optString("126");
                        String offpay_hash = jsonObject.optString("offpay_hash");
                        String offpay_hash_batch = jsonObject.optString("offpay_hash_batch");
                        Intent it = new Intent();
                        Bundle bundle = new Bundle();

                        bundle.putString("ship",ship);

//                        bundle.putString("distance",distance);
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

}
