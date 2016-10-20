package shop.trqq.com.im.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.myjson.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.event.EventIM_UnRead;
import shop.trqq.com.im.bean.UserBean;
import shop.trqq.com.im.ui.IMMsgActivity.MyBroadcastReceiver;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.YkLog;

/**
 * �����ϵ��
 */

public class RecentContactsFragment extends Fragment {

    private static final String TAG = "RecentContactsFragment";

    // ȫ��Context
    private Context mContext;
    // Gson����
    private Gson gson;
    // ���ؽ���Activity
    private ProgressActivity progressActivity;

    private IMUserAdapter listViewIMUserAdapter;
    // ���ؽ���Activity
    // private ProgressActivity progressActivity;
    private ListView mListView;
    private TextView mHeadTitleTextView;

    // ���ظ��࣬�Ų�����
    private View footView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;
    /**
     * Ҫ��ʾ����ʵ����.
     */
    private List<UserBean> UserList;

    private int curpageIndex = 1;

    private View rootView;// ����Fragment view
    private String show_pa;

    private MyBroadcastReceiver unreadnameReceiver;
    private final static String ACTION_GENERAL_SEND_USER = "com.trqq.shop.im.unreadnum";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_favorites);
        rootView = inflater.inflate(R.layout.im_fragment_recent, container,
                false);
        mContext = getActivity();
        gson = new Gson();
        UserList = new ArrayList<UserBean>();
        /*
		 * Bundle bundle = getArguments(); goods_id =
		 * bundle.getString("goods_id"); type = bundle.getString("type");
		 */
        initTitleBarView();
        initViews();

        // ע��㲥
/*		unreadnameReceiver = new MyBroadcastReceiver();
		mContext.registerReceiver(unreadnameReceiver, new IntentFilter(
				ACTION_GENERAL_SEND_USER));*/
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register
        EventBus.getDefault().register(this);
    }

    /**
     * �յ���Ϣ ������ش���
     *
     * @param event
     */
    public void onEventMainThread(EventIM_UnRead event) {
        //if(event.getItems().size()>0)
        int unReadNum = event.getUnReadNum();
        int u_id = event.getU_id();
        YkLog.t("EventIM_UnRead", "" + event.getUnReadNum());
        Boolean isRecent = true;
        for (int i = 0; i < UserList.size(); i++) {

            if (UserList.get(i).getU_id() == u_id) {
                UserList.get(i).setUnreadnum(unReadNum);
                isRecent = false;
                listViewIMUserAdapter.updataUnNum(i, unReadNum);
            }
        }

        // �����������ϵ�����¼���
        if (isRecent) {
            YkLog.t("EventIM_UnReadRE:", "" + u_id);
            UserList.clear();
            loadOnlineInformationData();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Unregister
        EventBus.getDefault().unregister(this);
        //mContext.unregisterReceiver(unreadnameReceiver);
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) rootView
                .findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("�����ϵ��");

    }

    private void initViews() {
        listViewIMUserAdapter = new IMUserAdapter(mContext);
        inflater = LayoutInflater.from(mContext);
        footView = inflater.inflate(R.layout.item_load_more, null);

        mLoadMoreButton = (Button) footView.findViewById(R.id.loadMore_button);
        // progressActivity = (ProgressActivity)
        // rootView.findViewById(R.id.PullToRefreshListView_progress);
        mListView = (ListView) rootView.findViewById(R.id.recent_list);
        mListView.setAdapter(listViewIMUserAdapter);
        progressActivity = (ProgressActivity) rootView
                .findViewById(R.id.Recent_progress);
		/*
		 * mHomePullToRefreshListView.getRefreshableView().addFooterView(footView
		 * );
		 * mHomePullToRefreshListView.setPullToRefreshOverScrollEnabled(false);
		 * mHomePullToRefreshListView.setAdapter(listViewIMUserAdapter);
		 * mHomePullToRefreshListView.setMode(Mode.MANUAL_REFRESH_ONLY);
		 * mHomePullToRefreshListView .setOnRefreshListener(new
		 * OnRefreshListener<ListView>() {
		 * 
		 * @Override public void onRefresh( PullToRefreshBase<ListView>
		 * refreshView) { curpageIndex=1; loadOnlineInformationData(); } });
		 * mHomePullToRefreshListView .setOnLastItemVisibleListener(new
		 * OnLastItemVisibleListener() {
		 * 
		 * @Override public void onLastItemVisible() { if (isEnabledScrollLast)
		 * { footView.setVisibility(View.VISIBLE); //loadMoreInformationData();
		 * } } });
		 */
        // progressActivity.showLoading();
        loadOnlineInformationData();
    }

    private void LoadUnReadNum() {
        for (UserBean tmpUser : UserList) {
            List<UserBean> mUserBeanList = DataSupport.where("u_id = ?",
                    "" + tmpUser.getU_id()).find(UserBean.class);
            if (mUserBeanList.size() > 0) {
                // ����
                if (mUserBeanList.get(0).getUnreadnum() > 0) {
                    YkLog.t("Unreadnum", ""
                            + mUserBeanList.get(0).getUnreadnum());
                    tmpUser.setUnreadnum(mUserBeanList.get(0).getUnreadnum());
                }
            }
        }
    }

    /**
     * ���������ϵ���б�
     */
    private void loadOnlineInformationData() {
        progressActivity.showLoading();
        String key = UserManager.getUserInfo().getKey();
        RequestParams requestParams = new RequestParams();
        requestParams.add("key", key);
        // requestParams.add("page ", "50");
        final String uri = HttpUtil.URL_MEMBER_CHAT_GET_USER_LIST;
        // final String uri =HttpUtil.URL_MEMBER_CHAT_FRIEND_LIST+"&curpage=" +
        // curpageIndex;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    // hasmore=new JSONObject(jsonString).getBoolean("hasmore");
                    YkLog.i("�����ϵ���б�", jsonString);
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    String listStr = jsonObjects.getString("list");
                    if ("[]".equals(listStr)) {
                        Drawable emptyDrawable = getResources().getDrawable(
                                R.drawable.ic_empty);
                        progressActivity.showEmpty(emptyDrawable, "����û�������ϵ��",
                                "");
                    } else {

                        JSONObject list = jsonObjects.getJSONObject("list");
                        setUserBean(list, UserList);
                        LoadUnReadNum();
                        listViewIMUserAdapter.setData(UserList);
                        listViewIMUserAdapter.notifyDataSetChanged();
                        if (UserList.size() > 0) {
                            progressActivity.showContent();
                        } else {
                            Drawable emptyDrawable = getResources()
                                    .getDrawable(R.drawable.ic_empty);
                            progressActivity.showEmpty(emptyDrawable,
                                    "����û�������ϵ��", "");
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    Drawable errorDrawable = getResources().getDrawable(
                            R.drawable.wifi_off);
                    progressActivity.showError(errorDrawable, "���翪��С��",
                            "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����", "��������",
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    UserList.clear();
                                    loadOnlineInformationData();
                                }
                            });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

        });
    }

    // ȡUserBean
    private void setUserBean(JSONObject jsonObject, List<UserBean> list) {
        int i = 0;
        // ����
        Iterator keyIter = jsonObject.keys();
        while (keyIter.hasNext()) {
            try {
                UserBean bean = new UserBean();
                String id = (String) keyIter.next();
                // bean.setSpecID(id);
                bean = gson.fromJson(jsonObject.optString(id),
                        new TypeToken<UserBean>() {
                        }.getType());
                list.add(bean);
                // System.err.println("��Ϣ" + list.get(i).getT_msg());
                i++;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
/*
	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int unReadNum = intent.getIntExtra("unReadNum", 0);
			int u_id = intent.getIntExtra("u_id", 0);
			YkLog.t("BroadcastReceiverget_user:", "" + unReadNum);
			Boolean isRecent = true;
			for (int i = 0; i < UserList.size(); i++) {

				if (UserList.get(i).getU_id() == u_id) {
					UserList.get(i).setUnreadnum(unReadNum);
					isRecent = false;
					listViewIMUserAdapter.updataUnNum(i, unReadNum);
				}
			}

			// �����������ϵ�����¼���
			if (isRecent) {
				YkLog.t("BroadcastReceiverget_userRE:", "" + u_id);
				UserList.clear();
				loadOnlineInformationData();
			}
		}
	}*/


}
