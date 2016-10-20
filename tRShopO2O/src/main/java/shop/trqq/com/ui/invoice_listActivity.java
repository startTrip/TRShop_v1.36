package shop.trqq.com.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.roamer.slidelistview.MySlideListView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.ListViewInvoiceAdapter;
import shop.trqq.com.bean.invoice_listBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 发票界面
 */
public class invoice_listActivity extends BaseActivity {

    private Context mContext;
    private MySlideListView listView;
    private ImageView bg;

    private ArrayList<invoice_listBean> invoiceList;
    private ListViewInvoiceAdapter addressAdapter;
    private Gson gson;
    private Boolean flag = false;
    private RadioGroup group;
    private String inv_title_select;
    private EditText inv_title;
    private Spinner inv_content;
    private String[] invoice_content_list;
    private Button invoice_save;
    private Button invoice_no;
    // 标题栏标题
    private TextView mHeadTitleTextView;
    private String invoice_id, invoice_message;
    private RadioButton invoice_person;

    // private String freight_hash;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        mContext = this;
        gson = new Gson();
        invoice_id = getIntent().getStringExtra("invoice_id");
        invoice_message = getIntent().getStringExtra("invoice_message");
        invoiceList = new ArrayList<invoice_listBean>();
        initTitleBarView();
        listView = (MySlideListView) findViewById(R.id.address_manage_list);
        bg = (ImageView) findViewById(R.id.address_list_bg);
        addressAdapter = new ListViewInvoiceAdapter(mContext, invoiceList);
        invoice_save = (Button) findViewById(R.id.invoice_save);
        invoice_no = (Button) findViewById(R.id.invoice_no);
        inv_content = (Spinner) findViewById(R.id.invoice_content);
        inv_title = (EditText) findViewById(R.id.invoice_company_text);
        group = (RadioGroup) findViewById(R.id.invoice_RadioGroup);
        invoice_person = (RadioButton) findViewById(R.id.invoice_person);
        invoice_person.setChecked(true);
        inv_title_select = "person";
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                // 获取变更后的选中项的ID
                int radioButtonId = arg0.getCheckedRadioButtonId();
                // 根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                // 更新文本内容，以符合选中项
                if (rb.getText().equals("个人")) {
                    inv_title_select = "person";
                } else {
                    inv_title_select = "company";
                }
            }
        });
        // freight_hash = getIntent().getStringExtra("freight_hash");
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                invoice_id = invoiceList.get(position).getInv_id();
                invoice_message = "普通发票  "
                        + invoiceList.get(position).getInv_title() + " "
                        + invoiceList.get(position).getInv_content();
                setResultfinish();
            }
        });
        invoice_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inv_title_select.equals("company")
                        && "".equals(inv_title.getText().toString())) {
                    ToastUtils.showMessage(mContext, "公司名不可为空");
                } else {
                    addInvoiceListData();
                }
            }
        });
        invoice_no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                invoice_id = "";
                invoice_message = "不需要发票";
                setResultfinish();
            }
        });
        // loadingAddressListData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingInvoiceListData();
        loadingInvoice_content_ListData();
    }

    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("发票");
    }

	/*
     * public void setInvoice() {
	 * 
	 * if(invoiceList.size() == 0) { listView.setVisibility(View.GONE);
	 * Resources resource = (Resources) getBaseContext().getResources(); String
	 * non=resource.getString(R.string.non_address);
	 * ToastUtils.showMessage(mContext, non); bg.setVisibility(View.VISIBLE); }
	 * else { bg.setVisibility(View.GONE); listView.setVisibility(View.VISIBLE);
	 * } }
	 */

	/*
	 * activity返回result是在被finish的时候，也就是说调用setResult()方法必须在finish()之前。
	 * 那么如果在如下方法中调用setResult()也有可能不会返回成功： onPause()， onStop()， onDestroy(),
	 * 因为这些方法调用不一定是在finish之前的，当然在onCreate()就调用setResult肯定是在finish之前的
	 */

    public void setResultfinish() {
        Intent it = new Intent();
        it.putExtra("invoice_id", invoice_id);
        it.putExtra("invoice_message", invoice_message);
        // System.err.println(invoice_id+"|"+invoice_message);
        setResult(3, it);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResultfinish();
        super.onBackPressed();
    }

    private void loadingInvoiceListData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        String uri = HttpUtil.URL_INVOICE_LIST;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    YkLog.t("invoice_list", jsonString);
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    String invoice_list = jsonObjects.getString("invoice_list");
                    invoiceList = gson.fromJson(invoice_list,
                            new TypeToken<List<invoice_listBean>>() {
                            }.getType());

                    addressAdapter.setData(invoiceList);
                    listView.setAdapter(addressAdapter);
                    addressAdapter.notifyDataSetChanged();
                    if (invoiceList.size() == 0)
                        listView.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                try {
                    if (invoiceList.size() == 0)
                        listView.setVisibility(View.GONE);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    // 加载发票内容数据
    private void loadingInvoice_content_ListData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        String uri = HttpUtil.URL_INVOICE_CONTEXT_LIST;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    // TestUtils.println_err("Invoice_content_List",
                    // jsonString);
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    String content_list = jsonObjects
                            .getString("invoice_content_list");

                    content_list = content_list.replaceAll("\"|\\[|\\]", "");
                    YkLog.t("content_list", content_list);

                    invoice_content_list = content_list.split(",");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.myspinner,
                            invoice_content_list);
                    inv_content.setAdapter(adapter);

					/*
					 * addressAdapter.setData(invoiceList);
					 * listView.setAdapter(addressAdapter);
					 * addressAdapter.notifyDataSetChanged();
					 */
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                try {
                    // if (invoiceList.size() == 0)
                    // listView.setVisibility(View.GONE);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    private void addInvoiceListData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("inv_title_select", inv_title_select);
        requestParams.add("inv_content", inv_content.getSelectedItem()
                .toString());
        if (inv_title_select.equals("company"))
            requestParams.add("inv_title", inv_title.getText().toString());
        String uri = HttpUtil.URL_INVOICE_ADD;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    try {
                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                        }
                    } catch (Exception e) {
                        invoice_id = jsonObject.getString("inv_id");
                        if (inv_title_select.equals("company")) {
                            invoice_message = "普通发票  "
                                    + inv_title.getText().toString() + " "
                                    + inv_content.getSelectedItem().toString();
                        } else {
                            invoice_message = "普通发票  个人 "
                                    + inv_content.getSelectedItem().toString();
                        }
                        ToastUtils.showMessage(mContext, "添加成功");
                        listView.setVisibility(View.VISIBLE);
                        loadingInvoiceListData();
                    }
                } catch (Exception e) {
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
        });
    }
}
