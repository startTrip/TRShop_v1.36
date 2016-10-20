package shop.trqq.com.im.ui;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;
import org.myjson.JSONException;
import org.myjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;
import shop.trqq.com.AppContext;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.im.bean.UserBean;
import shop.trqq.com.im.bean.getmsgBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.KeyBoardUtils;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * @author Weiss
 * 发送信息界面
 */
public class IMMsgActivity extends BaseActivity {

    private static final String TAG = "IMMsgActivity";
    private ListView msgListView;
    private TextView mHeadTitleTextView;
    private Button btn_chat_emo;
    private EditText edit_user_comment;
    private Button btn_chat_send;
    private GridView GridView_chat_emo;
    private GridviewEMOAdapter mGridviewEMOAdapter;
    private KPSwitchPanelLinearLayout mPanelRoot;

    private WebView webView;
    private IMMsgAdapter adapter;
    private Context mContext;
    private Gson gson;
    private List<getmsgBean> msgList = new ArrayList<getmsgBean>();
    private MyBroadcastReceiver generalReceiver;
    private final static String ACTION_GENERAL_SEND = "com.trqq.shop.im.getmsg";

    String[] emo = {"biggrin", "cry", "curse", "dizzy", "funk", "handshake",
            "huffy", "hug", "kiss", "lol", "loveliness", "mad", "moon", "sad",
            "shocked", "shutup", "shy", "sleepy", "smile", "sun", "sweat",
            "titter", "tongue", "victory"};
    List<String> emolist = new ArrayList<String>();
    ;
    String t_id, t_name, t_avatar;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immsg);
        mContext = this;
        gson = new Gson();
        this.t_id = getIntent().getExtras().getString("t_id");
        this.t_name = getIntent().getExtras().getString("t_name");
        //unreadnum设置为0
        UserBean updateUser = new UserBean();
        updateUser.setToDefault("unreadnum");
        updateUser.updateAll("u_id = ?", t_id);
        //GetUserInfo();
        initTitleBarView();
        clearNotification();
        mPanelRoot = (KPSwitchPanelLinearLayout) findViewById(R.id.panel_root);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        GridView_chat_emo = (GridView) findViewById(R.id.GridView_chat_emo);
        mGridviewEMOAdapter = new GridviewEMOAdapter(mContext);
        for (String tmpemo : emo) {
            emolist.add(tmpemo);
        }
        mGridviewEMOAdapter.setData(emolist);
        GridView_chat_emo.setAdapter(mGridviewEMOAdapter);

        // 注册广播
        generalReceiver = new MyBroadcastReceiver();
        registerReceiver(generalReceiver, new IntentFilter(ACTION_GENERAL_SEND));
        // clearNotification1(mContext);
        AppContext mAppContext = (AppContext) getApplicationContext();
        webView = mAppContext.getSocketIOWebView();

        adapter = new IMMsgAdapter(mContext, t_id);
        msgListView.setAdapter(adapter);
        btn_chat_emo = (Button) findViewById(R.id.btn_chat_emo);
        edit_user_comment = (EditText) findViewById(R.id.edit_user_comment);
        btn_chat_send = (Button) findViewById(R.id.btn_chat_send);
        // UserInfo()没有保存用户ID用用户名称代替
        String myname = UserManager.getUserInfo().getNickname();
        // 读取数据库 自己ID发送目标联系人ID信息，和目标联系人ID发送 自己ID信息
        List<getmsgBean> allmsg = DataSupport.where(
                "(f_id = ? and t_name= ?)or (t_id= ? and f_name= ?)", t_id,
                myname, t_id, myname).find(getmsgBean.class);
        System.err.println(UserManager.getUserInfo().getNickname());
        msgList.addAll(allmsg);
        for (getmsgBean tmpmsg : msgList) {
            YkLog.e("tmpmsg", tmpmsg.getId() + "|" + tmpmsg.getT_msg());
        }
        adapter.setData(msgList);
        adapter.notifyDataSetChanged();
        msgListView.setSelection(adapter.getCount());
        KeyboardUtil.attach(this, mPanelRoot,
                // Add keyboard showing state callback, do like this when you want to listen in the
                // keyboard's show/hide change.
                new KeyboardUtil.OnKeyboardShowingListener() {
                    @Override
                    public void onKeyboardShowing(boolean isShowing) {
                        Log.d(TAG, String.format("Keyboard is %s", isShowing ? "showing" : "hiding"));
                    }
                });
        KPSwitchConflictUtil.attach(mPanelRoot, btn_chat_emo, edit_user_comment,
                new KPSwitchConflictUtil.SwitchClickListener() {
                    @Override
                    public void onClickSwitch(boolean switchToPanel) {
                        if (switchToPanel) {
                            edit_user_comment.clearFocus();
                        } else {
                            edit_user_comment.requestFocus();
                        }
                    }
                });
/*        btn_chat_emo.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (GridView_chat_emo.getVisibility() == View.GONE) {
//					InputMethodManager inputMethodManager = (InputMethodManager)               getSystemService(Context.INPUT_METHOD_SERVICE);
//					inputMethodManager.hideSoftInputFromWindow(IMMsgActivity.this.getCurrentFocus().getWindowToken(),
//					InputMethodManager.HIDE_NOT_ALWAYS);
                    KeyBoardUtils.closeKeybord(edit_user_comment, mContext);
                    GridView_chat_emo.setVisibility(View.VISIBLE);
                } else {
                    GridView_chat_emo.setVisibility(View.GONE);
                }
            }
        });*/
        edit_user_comment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                GridView_chat_emo.setVisibility(View.GONE);
                KeyBoardUtils.openKeybord(edit_user_comment, mContext);
            }
        });
        btn_chat_send.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String editStr = edit_user_comment.getText().toString();
                AppContext mAppContext = (AppContext) getApplicationContext();
                if (mAppContext.getIsIMNet()) {
                    if (!"".equals(editStr) && editStr != null) {
                        send_msg(editStr);
                        edit_user_comment.setText("");
                    }
                } else {
                    ToastUtils.showMessage(mContext, "发送失败，请稍候再试");
                }
            }
        });
        GridView_chat_emo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String edit = ":" + emo[position] + ":";
                // 获取光标位置
                int EditTextCursorIndex = edit_user_comment.getSelectionStart();
                // 插入
                edit_user_comment.getText().insert(EditTextCursorIndex, edit);
                String editStr = edit_user_comment.getText().toString();
                edit_user_comment.setText(editStr);
                edit_user_comment.setSelection(EditTextCursorIndex
                        + edit.length());
            }
        });
    }

    public String getT_id() {
        return t_id;
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText(t_name);
    }

    // 删除通知
    private void clearNotification() {
        // 启动后删除之前我们定义的通知
        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(mContext.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }


    private void send_msg(final String msg) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        // 86 123456
        requestParams.add("t_id", t_id);
        requestParams.add("t_name", t_name);
        requestParams.add("t_msg", msg);

		/*
         * requestParams.add("u_id", "1"); requestParams.add("t", "member_id");
		 */
        HttpUtil.post(HttpUtil.URL_MEMBER_CHAT_SEND_MSG, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            YkLog.t("send_msg", jsonString);
                            JSONObject msgJSON = new JSONObject(jsonString)
                                    .getJSONObject("datas")
                                    .getJSONObject("msg");
                            getmsgBean bean = gson.fromJson(msgJSON.toString(),
                                    getmsgBean.class);
                            webView.loadUrl("javascript:node_send_msg("
                                    + msgJSON.toString() + ")");
                            bean.save();
                            msgList.add(bean);
                            adapter.setData(msgList);
                            adapter.notifyDataSetChanged();
                            msgListView.setSelection(adapter.getCount());

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
        // IMchatSocket();
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mPanelRoot.getVisibility() == View.VISIBLE) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    // 删除通知
    private void clearNotification(Context context) {
        // 启动后删除之前我们定义的通知
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    // 取getmsgBean
    private void setmsgBean(JSONObject jsonObject, List<getmsgBean> list) {
        int i = 0;
        // 无序
        Iterator keyIter = jsonObject.keys();
        int maxid = 0;
        while (keyIter.hasNext()) {
            try {
                getmsgBean bean = new getmsgBean();
                String id = (String) keyIter.next();
                // bean.setSpecID(id);
                maxid = Integer.parseInt(id);
                bean = gson.fromJson(jsonObject.optString(id),
                        new TypeToken<getmsgBean>() {
                        }.getType());
                if (t_id.equals(String.valueOf(bean.getF_id()))) {
                    list.add(bean);
                    // bean.save();
                }
                // System.err.println("信息" + list.get(i).getT_msg());
                i++;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(generalReceiver);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getStringExtra("get_msg");
            YkLog.i("BroadcastReceiverget_msg:", json);
            setmsgBean(new JSONObject(json), msgList);
			/*
			 * msgList = gson.fromJson(json, new TypeToken<List<getmsgBean>>() {
			 * }.getType());
			 */
            // 数据库保存
            // DataSupport.saveAll(msgList);
            adapter.setData(msgList);
            adapter.notifyDataSetChanged();
            msgListView.setSelection(adapter.getCount());
        }
    }

}