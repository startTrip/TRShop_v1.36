package shop.trqq.com.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.ListViewIndate_CodeAdapter;
import shop.trqq.com.bean.Indate_CodeBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 兑换码列表
 */
public class Indate_code_listActivity extends BaseActivity {

    private Context mContext;
    // 标题栏标题
    private TextView mHeadTitleTextView;
    private ListView listView;
    private ArrayList<Indate_CodeBean> mIndate_CodeList;
    private ListViewIndate_CodeAdapter mIndate_CodeAdapter;
    private Gson gson;
    private String order_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indate_code_list);
        mContext = this;
        gson = new Gson();
        mIndate_CodeList = new ArrayList<Indate_CodeBean>();
        initTitleBarView();
        listView = (ListView) findViewById(R.id.indate_code_list);
        mIndate_CodeAdapter = new ListViewIndate_CodeAdapter(mContext);
        order_id = getIntent().getStringExtra("order_id");

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingIndate_codeListData();
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("兑换码列表");
    }

    private void loadingIndate_codeListData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("order_id", order_id);
        HttpUtil.post(HttpUtil.URL_MEMBER_VR_ODER, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("code_list", jsonString);
                            JSONObject jsonObject = new JSONObject(jsonString)
                                    .getJSONObject("datas");
                            try {

                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                String code_list = jsonObject
                                        .getString("code_list");
                                if (code_list.equals("[]")) {
                                    ToastUtils.showMessage(mContext,
                                            "没有可用的兑换码列表");
                                } else {
                                    mIndate_CodeList = gson
                                            .fromJson(
                                                    code_list,
                                                    new TypeToken<List<Indate_CodeBean>>() {
                                                    }.getType());

                                    mIndate_CodeAdapter
                                            .setData(mIndate_CodeList);
                                    listView.setAdapter(mIndate_CodeAdapter);
                                    mIndate_CodeAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        try {
                            // if (cartInfoList.size() == 0)
                            // mLoadunloginLinearLayout.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        // 不管成功或者失败，都要将进度条关闭掉
                        // mNetProgressBar.setVisibility(View.GONE);
                    }
                });
    }

}
