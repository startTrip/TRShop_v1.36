package shop.trqq.com.ui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.push.Utils;

import java.util.List;

import shop.trqq.com.AppConfig;
import shop.trqq.com.AppContext;
import shop.trqq.com.NetworkStateService;
import shop.trqq.com.NetworkStateService.MyBinder;
import shop.trqq.com.R;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Base.DoubleClickExitHelper;
import shop.trqq.com.ui.Fragment.Fragment_Cart;
import shop.trqq.com.ui.Fragment.Fragment_Categroy;
import shop.trqq.com.ui.Fragment.Fragment_Home;
import shop.trqq.com.ui.Fragment.Fragment_My;
import shop.trqq.com.util.UpdateManager;
import shop.trqq.com.util.YkLog;

/**
 * @author Weiss ����������MainTabActivity
 */
public class MainTabActivity extends BaseFragmentActivity {
    private static final String TAG = "MainTabActivity";
    // ����FragmentTabHost����
    private FragmentTabHost mTabHost;
    // ȫ��Application
    private AppContext appContext;
    // ����һ������
    private LayoutInflater layoutInflater;
    // ˫���˳�����
    private DoubleClickExitHelper mDoubleClickExitHelper;

    // �������������Fragment����
    private Class fragmentArray[] = {Fragment_Home.class,
            Fragment_Categroy.class,
            Fragment_Cart.class, Fragment_My.class};


    // ������������Ű�ťͼƬ
    private int mImageViewArray[] = {R.drawable.tab_home_btn,
            R.drawable.tab_message_btn,
            R.drawable.tab_selfinfo_btn, R.drawable.tab_more_btn};

    // Tabѡ�������
    private String mTextviewArray[] = {"��ҳ", "����","���ﳵ", "��"};
    // IM socketIOWebView
    private WebView socketIOWebView;
    private NetworkStateService mNetworkStateService;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            YkLog.e(TAG, "���ӳɹ�");
            // ��Service���ӽ����ɹ����ṩ���ͻ�����Service�����Ķ��󣨸���Android Doc����ģ���֪��׼ȷ�񡣡�������
            mNetworkStateService = ((MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            YkLog.e(TAG, "�Ͽ�����");
            // mNetworkStateService = null;
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if(TextUtils.equals("1",type)){
            Intent intent1 = new Intent(this,OrderActivity.class);
            intent1.putExtra("filter","");
            startActivity(intent1);
        }

        // ��ʼ��˫���˳�����
        mDoubleClickExitHelper = new DoubleClickExitHelper(this);
        // ��ʼ������״̬��������
        appContext = (AppContext) getApplication();
        Intent i = new Intent(appContext, NetworkStateService.class);
        try {
            bindService(i, conn, Service.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ��ʼ���ٶ�������
        PushManager.startWork(appContext, PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(MainTabActivity.this, "api_key"));
        // Push: ����tag���÷�ʽ��Ӣ�Ķ��ŷֿ�,����ʱע��
        List<String> tags = Utils.getTagsList("five");
        PushManager.setTags(getApplicationContext(), tags);
        //�о�tag
//		PushManager.listTags(getApplicationContext());
        // ��ȡ���ý���Preferences��������
        if (AppConfig.getSharedPreferences(appContext).getBoolean(
                "update_switch", true)) {
            UpdateManager.getUpdateManager().checkAppUpdate(this, false);
        }
        initView();
    }
    /**
     * ��ʼ�����
     */
    @SuppressLint("NewApi")
    private void initView() {
        // ʵ�������ֶ���
        layoutInflater = LayoutInflater.from(this);
        // ��ʼ��IM socketIOWebView
        socketIOWebView = (WebView) findViewById(R.id.msg_WebView);
        appContext.setSocketIOWebView(socketIOWebView);
        // ʵ����TabHost���󣬵õ�TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            // ���÷ָ��߿��Ϊ0
            mTabHost.getTabWidget().setShowDividers(0);
        }

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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // ˫���˳����򣬻���ģ��HOME��
        boolean flag = true;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            YkLog.i(TAG, "������ؼ�");
            // �Ƿ��˳�Ӧ��
            return mDoubleClickExitHelper.onKeyDown(keyCode, event);
        } else {
            flag = super.onKeyDown(keyCode, event);
        }
        return flag;
    }

    public Handler setHandler() {
        return handler;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // ���õ�ǰTab
                    mTabHost.setCurrentTab(1);
                    break;
                case 2:
                    // ���õ�ǰTab
                    mTabHost.setCurrentTab(2);
                    break;
                case 3:
                    mTabHost.setCurrentTab(3);
                    break;
                case 4:
                    mTabHost.setCurrentTab(4);
                    break;
                default:
                    break;
            }
        }
    };
}
