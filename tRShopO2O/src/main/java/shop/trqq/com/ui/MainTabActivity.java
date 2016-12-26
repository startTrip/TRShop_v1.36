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
 * @author Weiss 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷MainTabActivity
 */
public class MainTabActivity extends BaseFragmentActivity {
    private static final String TAG = "MainTabActivity";
    // 定义FragmentTabHost对象
    private FragmentTabHost mTabHost;
    // 全局Application
    private AppContext appContext;
    // 定义一个布局
    private LayoutInflater layoutInflater;
    // 双击退出助手
    private DoubleClickExitHelper mDoubleClickExitHelper;

    // 定义数组来存放Fragment界面
    private Class fragmentArray[] = {Fragment_Home.class,
            Fragment_Categroy.class,
            Fragment_Cart.class, Fragment_My.class};


    // 定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_home_btn,
            R.drawable.tab_message_btn,
            R.drawable.tab_selfinfo_btn, R.drawable.tab_more_btn};

    // Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "分类","购物车", "我"};
    // IM socketIOWebView
    private WebView socketIOWebView;
    private NetworkStateService mNetworkStateService;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            YkLog.e(TAG, "连接成功");
            // 当Service连接建立成功后，提供给客户端与Service交互的对象（根据Android Doc翻译的，不知道准确否。。。。）
            mNetworkStateService = ((MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            YkLog.e(TAG, "断开连接");
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

        // 初始化双击退出助手
        mDoubleClickExitHelper = new DoubleClickExitHelper(this);
        // 初始化网络状态监听服务
        appContext = (AppContext) getApplication();
        Intent i = new Intent(appContext, NetworkStateService.class);
        try {
            bindService(i, conn, Service.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 初始化百度云推送
        PushManager.startWork(appContext, PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(MainTabActivity.this, "api_key"));
        // Push: 设置tag调用方式，英文逗号分开,发布时注释
        List<String> tags = Utils.getTagsList("five");
        PushManager.setTags(getApplicationContext(), tags);
        //列举tag
//		PushManager.listTags(getApplicationContext());
        // 获取设置界面Preferences，检查更新
        if (AppConfig.getSharedPreferences(appContext).getBoolean(
                "update_switch", true)) {
            UpdateManager.getUpdateManager().checkAppUpdate(this, false);
        }
        initView();
    }
    /**
     * 初始化组件
     */
    @SuppressLint("NewApi")
    private void initView() {
        // 实例化布局对象
        layoutInflater = LayoutInflater.from(this);
        // 初始化IM socketIOWebView
        socketIOWebView = (WebView) findViewById(R.id.msg_WebView);
        appContext.setSocketIOWebView(socketIOWebView);
        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            // 设置分割线宽度为0
            mTabHost.getTabWidget().setShowDividers(0);
        }

        // 得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
                    .setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i)
                    .setBackgroundResource(R.drawable.selector_tab_background);
        }

    }

    /**
     * 给Tab按钮设置图标和文字
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
        // 双击退出程序，或者模拟HOME键
        boolean flag = true;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            YkLog.i(TAG, "点击返回键");
            // 是否退出应用
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
                    // 设置当前Tab
                    mTabHost.setCurrentTab(1);
                    break;
                case 2:
                    // 设置当前Tab
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
