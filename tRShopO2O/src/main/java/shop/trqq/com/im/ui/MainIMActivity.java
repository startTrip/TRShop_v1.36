package shop.trqq.com.im.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import shop.trqq.com.AppContext;
import shop.trqq.com.AppManager;
import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseFragmentActivity;

/**
 * IM������
 * Ŀǰֻ�������ϵ��Fragment
 */
public class MainIMActivity extends BaseFragmentActivity {
    private static final String TAG = "MainTabActivity";
    // ����FragmentTabHost����
    private FragmentTabHost mTabHost;
    // ȫ��Application
    private AppContext appContext;
    // ȫ��Context
    //private Context appContext;
    // �Ƿ��½�ı�ʾ
    private boolean isLogin = false;
    private boolean isFirst = true;
    // ����һ������
    private LayoutInflater layoutInflater;

    // �������������Fragment����
    private Class fragmentArray[] = {RecentContactsFragment.class};

    // ������������Ű�ťͼƬ
    private int mImageViewArray[] = {R.drawable.tab_more_btn};

    // Tabѡ�������
    private String mTextviewArray[] = {"�Ự"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);

        initView();
    }

    /**
     * ��ʼ�����
     */
    private void initView() {
        // ʵ�������ֶ���
        layoutInflater = LayoutInflater.from(this);

        // ʵ����TabHost���󣬵õ�TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // �õ�fragment�ĸ���
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            // Ϊÿһ��Tab��ť����ͼ�ꡢ���ֺ�����
            TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
                    .setIndicator(getTabItemView(i));
            // ��Tab��ť��ӽ�Tabѡ���
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            // ����Tab��ť�ı���
            mTabHost.getTabWidget().getChildAt(i)
                    .setBackgroundResource(R.drawable.selector_tab_background);
        }
    }


    /**
     * ��Tab��ť����ͼ�������
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // ����Activity&�Ӷ�ջ���Ƴ�
        AppManager.getAppManager().popActivity(this);
    }


}
