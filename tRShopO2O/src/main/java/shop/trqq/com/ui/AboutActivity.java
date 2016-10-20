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
 * ����Activity
 *
 * @author
 * @version 1.0
 * @date 2015-02-23
 */
public class AboutActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "AboutActivity";
    // ȫ��Context
    private Context mContext;

    // ����������
    private TextView mHeadTitleTextView;
    // �汾����
    private TextView VersionName;

    // ��������ѡ������
    private LinearLayout mExplainLinearLayout;
    // ����ѡ������
    private LinearLayout mContactmeLinearLayout;
    // ��������ѡ������
    private LinearLayout mServeClauseLinearLayout;
    // ��������ѡ������
    private LinearLayout mCooperateLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YkLog.i(TAG, "onCreate����");
        setContentView(R.layout.activity_about);
        mContext = this;

        initView();

    }

    /**
     * ��ʼ����ͼ
     */
    private void initView() {
        YkLog.i(TAG, "��ʼ����ͼ");
        initTitleBarView();

        mExplainLinearLayout = (LinearLayout) findViewById(R.id.explain_linearLayout);
        mContactmeLinearLayout = (LinearLayout) findViewById(R.id.contactme_linearLayout);
        mServeClauseLinearLayout = (LinearLayout) findViewById(R.id.serve_clause_linearLayout);
        mCooperateLinearLayout = (LinearLayout) findViewById(R.id.cooperation_linearLayout);
        VersionName = (TextView) findViewById(R.id.VersionName);
        AppContext appContext = (AppContext) getApplication();
        VersionName.setText("��ǰ�汾:" + appContext.getVersionName());
        mExplainLinearLayout.setOnClickListener(this);
        mContactmeLinearLayout.setOnClickListener(this);
        mServeClauseLinearLayout.setOnClickListener(this);
        mCooperateLinearLayout.setOnClickListener(this);
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        YkLog.i(TAG, "��ʼ����������ͼ");
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText(R.string.about);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // �������������¼�
            case R.id.explain_linearLayout:
                UIHelper.showProductinfo(mContext, "explain", "");
                break;
            // ʹ����ϵ���������¼�
            case R.id.contactme_linearLayout:
                UIHelper.showProductinfo(mContext, "contactme", "");
                break;
            // �������������¼�
            case R.id.serve_clause_linearLayout:
                UIHelper.showProductinfo(mContext, "", "");
                break;
            //������Ǣ̸�����¼�
            case R.id.cooperation_linearLayout:
                UIHelper.showProductinfo(mContext, "cooperate", "");
                break;

            default:
                break;
        }

    }
}