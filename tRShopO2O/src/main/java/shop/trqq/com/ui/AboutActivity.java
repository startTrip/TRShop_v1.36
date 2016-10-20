package shop.trqq.com.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import shop.trqq.com.AppContext;
import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.YkLog;

/**
 * 关于Activity
 *
 * @author
 * @version 1.0
 * @date 2015-02-23
 */
public class AboutActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "AboutActivity";
    // 全局Context
    private Context mContext;

    // 标题栏标题
    private TextView mHeadTitleTextView;
    // 版本名字
    private TextView VersionName;

    // 关于我们选项容器
    private LinearLayout mExplainLinearLayout;
    // 帮助选项容器
    private LinearLayout mContactmeLinearLayout;
    // 服务条例选项容器
    private LinearLayout mServeClauseLinearLayout;
    // 服务条例选项容器
    private LinearLayout mCooperateLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YkLog.i(TAG, "onCreate方法");
        setContentView(R.layout.activity_about);
        mContext = this;

        initView();

    }

    /**
     * 初始化视图
     */
    private void initView() {
        YkLog.i(TAG, "初始化视图");
        initTitleBarView();

        mExplainLinearLayout = (LinearLayout) findViewById(R.id.explain_linearLayout);
        mContactmeLinearLayout = (LinearLayout) findViewById(R.id.contactme_linearLayout);
        mServeClauseLinearLayout = (LinearLayout) findViewById(R.id.serve_clause_linearLayout);
        mCooperateLinearLayout = (LinearLayout) findViewById(R.id.cooperation_linearLayout);
        VersionName = (TextView) findViewById(R.id.VersionName);
        AppContext appContext = (AppContext) getApplication();
        VersionName.setText("当前版本:" + appContext.getVersionName());
        mExplainLinearLayout.setOnClickListener(this);
        mContactmeLinearLayout.setOnClickListener(this);
        mServeClauseLinearLayout.setOnClickListener(this);
        mCooperateLinearLayout.setOnClickListener(this);
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        YkLog.i(TAG, "初始化标题栏视图");
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText(R.string.about);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 关于我们项点击事件
            case R.id.explain_linearLayout:
                UIHelper.showProductinfo(mContext, "explain", "");
                break;
            // 使用联系我们项点击事件
            case R.id.contactme_linearLayout:
                UIHelper.showProductinfo(mContext, "contactme", "");
                break;
            // 服务条款项点击事件
            case R.id.serve_clause_linearLayout:
                UIHelper.showProductinfo(mContext, "", "");
                break;
            //合作与洽谈项点击事件
            case R.id.cooperation_linearLayout:
                UIHelper.showProductinfo(mContext, "cooperate", "");
                break;

            default:
                break;
        }

    }
}