package shop.trqq.com.ui.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jpush.ExampleUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import de.greenrobot.event.EventBus;
import shop.trqq.com.AppConfig;
import shop.trqq.com.AppContext;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.event.EventIM_UnRead;
import shop.trqq.com.im.bean.UserBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.ui.PersonalActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.UpdateManager;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.BadgeView;

public class Fragment_My extends Fragment implements OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = Fragment_My.class.getSimpleName();
    private View rootView;// ����Fragment view
    private Context mContext;
    private Button login;
    private Button register;
    // �û�ͷ��
    private ImageView mUserIconImageView;
    // �û��ǳ�
    private TextView mNicknameTextView;
    private TextView mpointTextView;
    private TextView mpredepoitTextView;
    // �û���Ϣ����
    private FrameLayout mUserInfoFrameLayout;
    // ��½ǰ������
    private RelativeLayout mUnLoginRelativeLayout;
    // ��½�������
    private RelativeLayout mLoginRelativeLayout;
    private LinearLayout orderLayout;
    private LinearLayout vr_orderLayout;
    private LinearLayout mLogoffLayout;
    private LinearLayout mSuggestLayout;
    private LinearLayout mUpdateLayout;
    private LinearLayout mAboutLayout;
    private LinearLayout mSysMsgLayout;
    private LinearLayout mVoucherLayout;
    private LinearLayout mStoreLayout;
    private TextView Logoff;
    private TextView Favorites;
    private TextView History;
    private TextView address;
    private ImageView imchat;
    private BadgeView imBadge;
    private BadgeView msgBadge;
    private SwipeRefreshLayout  refresh_layout;// support-V4 19����ˢ�¿ؼ�
    /**
     * �Ƿ�������ݱ�־
     **/
    private boolean isLoadingData = false;

    private MymsgBroadcastReceiver generalReceiver;
    private final static String ACTION_GENERAL_SEND = "com.baidu.push.trshop";

    @Bind(R.id.wallet_Taifubao)
    TextView wallet_Taifubao;
    @Bind(R.id.wallet_consumepay)
    TextView wallet_consumepay;
    @Bind(R.id.wallet_productpay)
    TextView wallet_productpay;
    @Bind(R.id.tongyong_pay)
    TextView wallet_tongyongpay;

    //@Bind(R.id.userIcon_imageView)
    //CustomShapeImageView mUserIcon;

    // private TextView Daifukuan,Daishouhuo,Daipingjia; //�����ջ���������?


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // if (rootView == null) {
        rootView = inflater.inflate(R.layout.fragment_my, container, false);
        //ButterKnife��
        ButterKnife.bind(this, rootView);
        mContext = getActivity();
        login = (Button) rootView.findViewById(R.id.login_button);
        register = (Button) rootView.findViewById(R.id.register_button);
        // δ��¼�Ĳ���
        mUnLoginRelativeLayout = (RelativeLayout) rootView
                .findViewById(R.id.unlogin_relativeLayout);

        // ��½�Ժ�Ĳ���
        mLoginRelativeLayout = (RelativeLayout) rootView
                .findViewById(R.id.login_relativeLayout);
        // ע��
        mLogoffLayout = (LinearLayout) rootView
                .findViewById(R.id.logoff_layout);
        mUpdateLayout = (LinearLayout) rootView
                .findViewById(R.id.item_update_Layout);
        mAboutLayout = (LinearLayout) rootView
                .findViewById(R.id.item_about_Layout);
        mSysMsgLayout = (LinearLayout) rootView
                .findViewById(R.id.item_info_Layout);
        mUserIconImageView = (ImageView) rootView
                .findViewById(R.id.userIcon_imageView);
        mNicknameTextView = (TextView) rootView
                .findViewById(R.id.nickname_textView);
        mpointTextView = (TextView) rootView.findViewById(R.id.point);
        mpredepoitTextView = (TextView) rootView
                .findViewById(R.id.predepoit);
        orderLayout = (LinearLayout) rootView
                .findViewById(R.id.order_layout);
        vr_orderLayout = (LinearLayout) rootView
                .findViewById(R.id.vr_order_layout);
        mVoucherLayout = (LinearLayout) rootView
                .findViewById(R.id.item_voucher_Layout);
        mStoreLayout = (LinearLayout) rootView
                .findViewById(R.id.item_store_Layout);
        imchat = (ImageView) rootView.findViewById(R.id.im_chat);
        Favorites = (TextView) rootView.findViewById(R.id.My_Favorites);
        History = (TextView) rootView.findViewById(R.id.My_History);
        address = (TextView) rootView.findViewById(R.id.My_Address);
        mSuggestLayout = (LinearLayout) rootView
                .findViewById(R.id.item_Suggest_Layout);
/*
            wallet_Taifubao = (TextView) rootView.findViewById(R.id.wallet_Taifubao);
            wallet_consumepay = (TextView) rootView.findViewById(R.id.wallet_consumepay);
            wallet_productpay = (TextView) rootView.findViewById(R.id.wallet_productpay);
*/

        refresh_layout = (SwipeRefreshLayout) rootView
                .findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refresh_layout.setOnRefreshListener(this);

        imBadge = new BadgeView(mContext, imchat);
        msgBadge = new BadgeView(mContext, mSysMsgLayout);

        // ע��㲥
        generalReceiver = new MymsgBroadcastReceiver();
        mContext.registerReceiver(generalReceiver, new IntentFilter(
                ACTION_GENERAL_SEND));
            /*
			 * Daifukuan= (TextView) rootView.findViewById(R.id.daifukuan);
			 * Daishouhuo= (TextView) rootView.findViewById(R.id.daishouhuo);
			 * Daipingjia= (TextView) rootView.findViewById(R.id.daipingjia);
			 * Daifukuan.setOnClickListener(this);
			 * Daishouhuo.setOnClickListener(this);
			 * Daifukuan.setOnClickListener(this);
			 * Daipingjia.setOnClickListener(this);
			 */
        Favorites.setOnClickListener(this);
        History.setOnClickListener(this);
        mSysMsgLayout.setOnClickListener(this);
        address.setOnClickListener(this);
        mSuggestLayout.setOnClickListener(this);
        mUpdateLayout.setOnClickListener(this);
        mAboutLayout.setOnClickListener(this);
        orderLayout.setOnClickListener(this);
        vr_orderLayout.setOnClickListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        mVoucherLayout.setOnClickListener(this);
        mStoreLayout.setOnClickListener(this);
        imchat.setOnClickListener(this);
//        SystemBarHelper.immersiveStatusBar(getActivity(),0);
        //mUserIcon.setOnClickListener(this);
       /* }
        // �����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�������parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
*/
        return rootView;
    }

    @Override
    public void onResume() {

        // �Ѿ���¼
        if (UserManager.isLogin()) {
            // ��ȡ��������Ϣ
            loadOnlineMydata();

            // ���õ�½�����
            mNicknameTextView.setText(UserManager.getUserInfo().getNickname());
            Logoff = (TextView) rootView.findViewById(R.id.item_logoff_text);
            Logoff.setOnClickListener(this);
			/*
			 * int sex = UserManager.getUserInfo().getSex(); if (sex == 0)
			 * mSexImageView.setImageResource(R.drawable.icon_hollow_man); else
			 * if (sex == 1)
			 * mSexImageView.setImageResource(R.drawable.icon_hollow_woman);
			 */

            mLogoffLayout.setVisibility(View.VISIBLE);
            // mFunctionLinearLayout.setVisibility(View.VISIBLE);
            mLoginRelativeLayout.setVisibility(View.VISIBLE);
            mUnLoginRelativeLayout.setVisibility(View.INVISIBLE);

            // δ��IM��Ϣ
            List<UserBean> mUserBeanList = DataSupport.where("unreadnum > ?",
                    "" + 0).find(UserBean.class);
            if (mUserBeanList.size() > 0) {
                // ��δ��IM��Ϣ
                imBadge.setText("");
                imBadge.setTextSize(8);
                imBadge.show();
            } else {
                imBadge.hide();
            }
        } else {
            mLogoffLayout.setVisibility(View.GONE);
            // mFunctionLinearLayout.setVisibility(View.GONE);
            mLoginRelativeLayout.setVisibility(View.INVISIBLE);
            mUnLoginRelativeLayout.setVisibility(View.VISIBLE);
        }
        int num = AppConfig.getSharedPreferences(mContext).getInt("msgnum", 0);
        // YkLog.t("msgnum", num+"");
        setMsgBadge(num);
        super.onResume();
        Log.d("username","onresume");
        IntentFilter intentFilter = new IntentFilter("alias");
        getActivity().registerReceiver(mBroadcastReceive,intentFilter);
    }

    private static final int MSG_SET_ALIAS = 1001;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(mContext.getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (ExampleUtil.isConnected(mContext.getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
            }
        }
    };

    // �㲥������ȥ���� ���ñ�������Ϣ
    private BroadcastReceiver mBroadcastReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String username = intent.getStringExtra("username");
            Log.d("username",username);
            //����JPush API����Alias
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, username));
        }
    };

    private void setMsgBadge(int num) {
        if (num > 0) {
            msgBadge.setText(num + "");
            msgBadge.setTextSize(12);
            msgBadge.show();
        } else {
            msgBadge.hide();
        }
    }

    /**
     * ���ö������ڼ��ص�״̬
     */
    protected void setSwipeRefreshLoadingState() {
        if (refresh_layout != null) {
            refresh_layout.setRefreshing(true);
            // ��ֹ����ظ�ˢ��
            refresh_layout.setEnabled(false);
        }
    }

    /**
     * ���ö���������ϵ�״̬
     */
    protected void setSwipeRefreshLoadedState() {
        if (refresh_layout != null) {
            refresh_layout.setRefreshing(false);
            refresh_layout.setEnabled(true);
        }
    }

    // �����ҵ���Ϣ
    private void loadOnlineMydata() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        isLoadingData = true;
        // String uri ="http://shop.trqq.com/mobile/index.php?act=member_index";
        HttpUtil.post(HttpUtil.URL_MYSTOIRE, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("URL_MYSTOIRE��", jsonString);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    if ("���¼".equals(errStr)) {
                                        ToastUtils.showMessage(mContext,
                                                "��ס��¼�ѹ��ڣ������µ�¼");
                                        UserManager.setUserInfo(false);
                                        UserManager.cleanInfo(mContext);
                                        AppContext mAppContext = (AppContext) mContext
                                                .getApplicationContext();
                                        mAppContext.clearIMJS();
                                        onResume();
                                    } else {
                                        ToastUtils
                                                .showMessage(mContext, errStr);
                                    }
                                }
                            } catch (Exception e) {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                JSONObject member_info = jsonObject
                                        .getJSONObject("member_info");
                                // ToastUtils.showMessage(mContext,
                                // member_info.getString("avator"));
                                // UserManager.getUserInfo().setUserIcon(member_info.getString("avator"));
                                UserManager.setavator(mContext,
                                        member_info.getString("avator"));
                                ImageLoader.getInstance()
                                        .displayImage(
                                                UserManager.getUserInfo()
                                                        .getUserIcon(),
                                                mUserIconImageView);
                                mpointTextView.setText("���֣�"
                                        + member_info.getString("point"));
                                mpredepoitTextView.setText("Ԥ��"
                                        + member_info.getString("predepoit"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        ToastUtils.showMessage(getActivity(),
                                R.string.get_informationData_failure);
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();
                        // ����̩��������Ϣ;
                        Taifubao();
                    }

                });
    }

    // ̩����
    private void Taifubao() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        String uri = HttpUtil.HOST+"?act=member_index&op=bao";
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);

                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String credit = jsonObject.getString("credit");
                        String consume = jsonObject.getString("consume");
                        String product = jsonObject.getString("product");
                        String tongyong = jsonObject.getString("tyong");
                        wallet_Taifubao.setText(credit + "\n̩����");
                        wallet_consumepay.setText(consume + "\n���ѻ���");
                        wallet_productpay.setText(product + "\n��Ʒ����");
                        wallet_tongyongpay.setText(tongyong+"\nͨ�û���");
                    } catch (Exception e) {
                        //����̩�����������
                        if ("0".equals(jsonString)) {
                            wallet_Taifubao.setText("�������\n̩����");
                            wallet_consumepay.setText("�������\n���ѻ���");
                            wallet_productpay.setText("�������\n��Ʒ����");
                            wallet_tongyongpay.setText("�������\nͨ�û���");
                        }
                        //��һ��ͨ�û�
                        if ("1".equals(jsonString)) {
                            wallet_Taifubao.setText("0.00\n̩����");
                            wallet_consumepay.setText("0.00\n���ѻ���");
                            wallet_productpay.setText("0.00\n��Ʒ����");
                            wallet_tongyongpay.setText("0.00\nͨ�û���");
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
                ToastUtils.showMessage(getActivity(),
                        R.string.get_informationData_failure);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                isLoadingData = false;
                setSwipeRefreshLoadedState();
            }
        });
    }

    // ע��
    private void loginOut() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("username", UserManager.getUserInfo().getNickname());
        requestParams.add("client", "android");
        // String uri ="http://shop.trqq.com/mobile/index.php?act=logout";
        HttpUtil.post(HttpUtil.URL_LOGIN_OUT, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("loginOut", jsonString);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getString(
                                        "datas").equals("1")) {
                                    ToastUtils.showMessage(mContext, "�ɹ�ע��");
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
                        ToastUtils.showMessage(getActivity(),
                                R.string.get_informationData_failure);
                    }
                });
    }

    @Override
    public void onRefresh() {
        new MyAsyncTask().execute();
    }
    private class MyAsyncTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            // true��ˢ�¿�ʼ����������ˢ�µ�UI��ʽ.
            refresh_layout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Object o) {
            if ( isLoadingData|| !UserManager.isLogin()) {
                setSwipeRefreshLoadedState();
                return;
            } else {
                setSwipeRefreshLoadingState();
                loadOnlineMydata();
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            /*try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }*/
            return null;
        }
    }

    @Override
    public void onPause() {
        //YkLog.t("MyonPause", "onPause");
        //setSwipeRefreshLoadedState();
        super.onPause();
    }


    /*
         * δ��ϵͳ��Ϣ�����㲥
         */
    public class MymsgBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int num = intent.getIntExtra("msgnum", 0);
            YkLog.t("BroadcastReceivermsgnum:", num + "");
            setMsgBadge(num);
        }
    }

    /**
     * �յ���Ϣ ������ش��� eventbus����Register��Unregister֮ǰ�����յ�������Ϣ
     *
     * @param event
     */
    public void onEventMainThread(EventIM_UnRead event) {
        // if(event.getItems().size()>0)
        YkLog.t("EventIM_UnRead_my", "" + event.getUnReadNum());
        imBadge.setText("");
        imBadge.setTextSize(8);
        imBadge.show();
    }



    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        try {
            mContext.unregisterReceiver(generalReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Unregister
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    @Override
    public void onDestroyView() {
        YkLog.t("MY���ɼ�");
        setSwipeRefreshLoadedState();
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_layout:// �ҵĶ���
                if (UserManager.isLogin()) {
                    UIHelper.showOrder(mContext, "");
                } else {
                    ToastUtils.showMessage(mContext, "���¼");
                }
                break;
		/*
		 * case R.id.daifukuan: UIHelper.showOrder(getActivity(),"���?); break;
		 * case R.id.daishouhuo: UIHelper.showOrder(getActivity(),"���ջ�"); break;
		 */
            case R.id.vr_order_layout:// ���ⶩ��
                if (UserManager.isLogin()) {
                    UIHelper.showOrder(mContext, "����");
                } else {
                    ToastUtils.showMessage(mContext, "���¼");
                }
                break;
            case R.id.login_button:// ��¼
                UIHelper.showLoginDialog(mContext);
                break;
            case R.id.register_button:// ע��
                if(mContext.getClass().equals(PersonalActivity.class)){
                    ((PersonalActivity)mContext).finish();
                }
                UIHelper.showRegister(mContext);
                break;
            case R.id.item_logoff_text:// ע��
                loginOut();
                UserManager.setUserInfo(false);
                UserManager.cleanInfo(mContext);
                AppContext mAppContext = (AppContext)mContext
                        .getApplicationContext();
                mAppContext.clearIMJS();
                wallet_Taifubao.setText("0.00\n̩����");
                wallet_consumepay.setText("0.00\n���ѻ���");
                wallet_productpay.setText("0.00\n��Ʒ����");
                wallet_tongyongpay.setText("0.00\nͨ�û���");
                onResume();
                break;
            case R.id.My_Favorites:// ��ע����Ʒ
                if (UserManager.isLogin()) {
                    UIHelper.showFavorites(mContext);
                } else {
                    ToastUtils.showMessage(mContext, "���¼");
                }
                break;
            case R.id.My_Address:// ��ַ����
                if (UserManager.isLogin()) {
                    UIHelper.showAddressList(mContext, "");
                } else {
                    ToastUtils.showMessage(mContext, "���¼");
                }
                break;
            case R.id.My_History:// �����¼
                UIHelper.showHistory(mContext);
                break;
            case R.id.item_Suggest_Layout:// ���鷴��
                if (UserManager.isLogin()) {
                    UIHelper.showSuggest(mContext);
                } else {
                    ToastUtils.showMessage(mContext, "���¼");
                }
                break;
            case R.id.item_update_Layout:// �汾����
                UpdateManager.getUpdateManager().checkAppUpdate(mContext, true);
                break;
            case R.id.item_about_Layout:// ����
                UIHelper.showSetting(mContext);
//			UIHelper.showAbout(mContext);
                break;
            case R.id.im_chat:// im����
                if (UserManager.isLogin()) {
                    UIHelper.showMainIM(mContext);
                } else {
                    ToastUtils.showMessage(mContext, "���¼");
                }
                break;
            case R.id.item_info_Layout:// ϵͳ��Ϣ
                UIHelper.showSystemMsg(mContext);
                break;
            case R.id.item_voucher_Layout:// �����
                if (UserManager.isLogin()) {
                    UIHelper.showVoucher(mContext);
                } else {
                    ToastUtils.showMessage(mContext, "���¼");
                }
                break;
            case R.id.item_store_Layout:// �ղصĵ���
                if (UserManager.isLogin()) {
                    UIHelper.showFavoritesStore(mContext);
                } else {
                    ToastUtils.showMessage(mContext, "���¼");
                }
                break;
//		case R.id.userIcon_imageView://���ͷ��
//				UIHelper.showSetting(mContext);
//			break;

            default:
                break;
        }

    }
}