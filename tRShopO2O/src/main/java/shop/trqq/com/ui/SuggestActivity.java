package shop.trqq.com.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.myjson.JSONObject;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 建议反馈
 */
public class SuggestActivity extends BaseActivity {

    private static final String TAG = "SuggestActivity";


    private Context mContext;


    private TextView mHeadTitleTextView;
    private Spinner mSpinner;
    private EditText mEditText;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YkLog.i(TAG, "onCreate建议");
        setContentView(R.layout.activity_suggest);
        mContext = this;
        initView();
        mSpinner = (Spinner) findViewById(R.id.feedback_type_spinner);
        mEditText = (EditText) findViewById(R.id.feedback_content);
        submit = (Button) findViewById(R.id.feedback_submit);
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mEditText.getText().toString().equals("")) {
                    ToastUtils.showMessage(mContext, "内容不能为空");
                } else {
                    add_feedback();
                }
            }
        });

    }

    private void initView() {

        initTitleBarView();

    }


    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText(R.string.suggest);
    }

    private void add_feedback() {
        String feedback;
        feedback = "标题：" + mSpinner.getSelectedItem().toString() + "\n内容：" + mEditText.getText().toString();
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("feedback", feedback);
//		String uri ="http://shop.trqq.com/mobile/index.php?act=member_feedback&op=feedback_add";
        System.err.println(TAG + "：" + feedback);
        HttpUtil.post(HttpUtil.URL_FEEDBACK_ADD, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    //System.err.println(TAG+"："+jsonString);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("datas");
                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                        }
                    } catch (Exception e) {
                        if (new JSONObject(jsonString).getString("datas").equals("1")) {
                            ToastUtils.showMessage(mContext, "谢谢反馈，祝生活愉快");
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
                ToastUtils.showMessage(getApplicationContext(), R.string.get_informationData_failure);
            }
        });
    }

}