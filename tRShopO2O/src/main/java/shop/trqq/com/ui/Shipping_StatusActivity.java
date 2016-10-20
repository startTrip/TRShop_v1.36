package shop.trqq.com.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 物流信息
 */
public class Shipping_StatusActivity extends BaseActivity {

    // 标题栏标题
    private TextView mHeadTitleTextView;
    private Gson gson;
    private Context mContext;
    private String order_id;
    private TextView mExpress_name;
    private TextView mExpress_orderNum;
    private TextView mExpress_info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_status);
        order_id = getIntent().getExtras().getString("order_id");
        mContext = this;
        gson = new Gson();
        initTitleBarView();
        // mobile_body="";
        mExpress_name = (TextView) findViewById(R.id.express_name);
        mExpress_orderNum = (TextView) findViewById(R.id.express_orderNum);
        mExpress_info = (TextView) findViewById(R.id.express_info);
        Search_Deliver(order_id);
    }

    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("物流信息");
    }

    // 订单物流信息
    private void Search_Deliver(String order_id) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("order_id", order_id);
        HttpUtil.post(HttpUtil.URL_QUERY_DELIVER, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("Search_Deliver", jsonString);
                            JSONObject jsonObject = new JSONObject(jsonString)
                                    .getJSONObject("datas");
                            try {

                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                mExpress_name.setText("物流公司: "
                                        + jsonObject.optString("express_name"));
                                mExpress_orderNum.setText("运单号码: "
                                        + jsonObject.optString("shipping_code"));
                                String deliver_info = jsonObject
                                        .optString("deliver_info");
                                deliver_info = deliver_info.substring(2,
                                        deliver_info.length() - 2);
                                String strs[] = deliver_info.split("\",\"");
                                deliver_info = "";
                                for (String element : strs) {
                                    deliver_info = deliver_info + "\n"
                                            + element;
                                }
                                deliver_info = deliver_info.replace("&nbsp;",
                                        " ");
                                /*
								 * JSONArray deliver_info= jsonObject
								 * .optJSONArray("deliver_info");
								 */
                                mExpress_info.setText(deliver_info);
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
}
