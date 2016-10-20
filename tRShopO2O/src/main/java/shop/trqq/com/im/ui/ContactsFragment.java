package shop.trqq.com.im.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;
import org.myjson.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.im.bean.UserBean;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.YkLog;

/**
 * ͨѶ¼
 * ��ʱû�к��Ѳ����ӿ��ļ� ��������
 */
public class ContactsFragment extends Fragment {
    private static final String TAG = "ContactsFragment";

    // ȫ��Context
    private Context appContext;
    // Gson����
    private Gson gson;
    /*
	 * private LoopViewPager loopViewPager; private ArrayList<String> imageUris;
	 */

    private IMUserAdapter listViewIMUserAdapter;
    // ���ؽ���Activity
    // private ProgressActivity progressActivity;
    private PullToRefreshListView mHomePullToRefreshListView;
    private TextView mHeadTitleTextView;

    private LinearLayout add_MemberLayout;

    // ���ظ��࣬�Ų�����
    private View footView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;
    /**
     * Ҫ��ʾ����ʵ����.
     */

    private List<UserBean> informationList;

    private int informationListIndex = 0;
    private int curpageIndex = 1;

    private View rootView;// ����Fragment view
    private String goods_id, type;
    private String show_pa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_favorites);
        rootView = inflater.inflate(R.layout.im_fragment_contacts, container,
                false);
        appContext = getActivity();
        gson = new Gson();
        informationList = new ArrayList<UserBean>();
		/*
		 * Bundle bundle = getArguments(); goods_id =
		 * bundle.getString("goods_id"); type = bundle.getString("type");
		 */
        initTitleBarView();
        initViews();

        return rootView;
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) rootView
                .findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("��ϵ��");
    }

    private void initViews() {
        listViewIMUserAdapter = new IMUserAdapter(appContext);
        inflater = LayoutInflater.from(appContext);
        footView = inflater.inflate(R.layout.item_load_more, null);
        mLoadMoreButton = (Button) footView.findViewById(R.id.loadMore_button);
        // progressActivity = (ProgressActivity)
        // rootView.findViewById(R.id.PullToRefreshListView_progress);
        mHomePullToRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.list_friends_pullToRefreshListView);
        add_MemberLayout = (LinearLayout) rootView
                .findViewById(R.id.add_MemberLayout);
        add_MemberLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                loadFindMember_List();
            }
        });
        mHomePullToRefreshListView.getRefreshableView().addFooterView(footView);
        mHomePullToRefreshListView.setPullToRefreshOverScrollEnabled(false);
        mHomePullToRefreshListView.setAdapter(listViewIMUserAdapter);
        mHomePullToRefreshListView.setMode(Mode.MANUAL_REFRESH_ONLY);
		/*
		 * mHomePullToRefreshListView .setOnRefreshListener(new
		 * OnRefreshListener<ListView>() {
		 * 
		 * @Override public void onRefresh( PullToRefreshBase<ListView>
		 * refreshView) { curpageIndex=1; loadOnlineInformationData(); } });
		 */
        mHomePullToRefreshListView
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
                    @Override
                    public void onLastItemVisible() {
                        if (isEnabledScrollLast) {
                            footView.setVisibility(View.VISIBLE);
                            // loadMoreInformationData();
                        }
                    }
                });
        // progressActivity.showLoading();
        loadOnlineInformationData();
    }

    /**
     * ��ѯ��Ա�б�
     */
    private void loadFindMember_List() {
        String key = UserManager.getUserInfo().getKey();
        RequestParams requestParams = new RequestParams();
        requestParams.add("key", key);
        requestParams.add("m_name", "123456");
        // requestParams.add("page ", "50");
        final String uri = HttpUtil.URL_MEMBER_CHAT_MEMBER_LIST;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    // hasmore=new JSONObject(jsonString).getBoolean("hasmore");
                    YkLog.i("URL_MEMBER_CHAT_FRIEND_LIST", jsonString);
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    try {
                        String errStr = jsonObjects.getString("error");
                        if (errStr != null) {
                            // ToastUtils.showMessage(appContext, errStr);
							/*
							 * if (informationList.size() == 0){ Drawable
							 * emptyDrawable = getResources().getDrawable(
							 * R.drawable.ic_empty); progressActivity
							 * .showEmpty(emptyDrawable, errStr, ""); }
							 */
                        }
                    } catch (Exception e) {
                        JSONObject list = jsonObjects.getJSONObject("list");
                        setUserBean(list, informationList);
                        // AppConfig.setSharedPreferences(appContext, configKey,
                        // goods_list);
                        listViewIMUserAdapter.setData(informationList);
                        listViewIMUserAdapter.notifyDataSetChanged();
                        informationListIndex += informationList.size();
                        // progressActivity.showContent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    if (informationList.size() == 0) {
						/*
						 * Drawable errorDrawable = getResources().getDrawable(
						 * R.drawable.wifi_off);
						 * progressActivity.showError(errorDrawable, "���翪��С��",
						 * "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����", "��������", new
						 * OnClickListener() {
						 * 
						 * @Override public void onClick(View v) { // TODO
						 * Auto-generated method stub
						 * progressActivity.showLoading();
						 * informationList.clear(); loadOnlineInformationData();
						 * } });
						 */
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                // ���ܳɹ�����ʧ�ܣ���Ҫ���������رյ�
                mHomePullToRefreshListView.onRefreshComplete();
            }

        });
    }

    /**
     * ������ϵ���б� ��ʱû�нӿ��ļ�
     */
    private void loadOnlineInformationData() {
        String key = UserManager.getUserInfo().getKey();
        RequestParams requestParams = new RequestParams();
        requestParams.add("key", key);
        requestParams.add("curpage", String.valueOf(curpageIndex));
        final String uri = HttpUtil.URL_MEMBER_CHAT_FRIEND_LIST;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    // hasmore=new JSONObject(jsonString).getBoolean("hasmore");
                    System.err.println(jsonString);
                    YkLog.t("URL_MEMBER_CHAT_FRIEND_LIST", jsonString);
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    try {
                        String errStr = jsonObjects.getString("error");
                        if (errStr != null) {
                            // ToastUtils.showMessage(appContext, errStr);
							/*
							 * if (informationList.size() == 0){ Drawable
							 * emptyDrawable = getResources().getDrawable(
							 * R.drawable.ic_empty); progressActivity
							 * .showEmpty(emptyDrawable, errStr, ""); }
							 */
                        }
                    } catch (Exception e) {
                        JSONObject list = jsonObjects.getJSONObject("list");
                        setUserBean(list, informationList);
                        // AppConfig.setSharedPreferences(appContext, configKey,
                        // goods_list);
                        listViewIMUserAdapter.setData(informationList);
                        listViewIMUserAdapter.notifyDataSetChanged();
                        informationListIndex += informationList.size();
                        // progressActivity.showContent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    if (informationList.size() == 0) {
						/*
						 * Drawable errorDrawable = getResources().getDrawable(
						 * R.drawable.wifi_off);
						 * progressActivity.showError(errorDrawable, "���翪��С��",
						 * "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����", "��������", new
						 * OnClickListener() {
						 * 
						 * @Override public void onClick(View v) { // TODO
						 * Auto-generated method stub
						 * progressActivity.showLoading();
						 * informationList.clear(); loadOnlineInformationData();
						 * } });
						 */
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                // ���ܳɹ�����ʧ�ܣ���Ҫ���������رյ�
                mHomePullToRefreshListView.onRefreshComplete();
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
	 * private void loadMoreInformationData() { RequestParams requestParams =
	 * new RequestParams(); requestParams.add("goods_id", goods_id);
	 * requestParams.add("type", type); curpageIndex++; final String uri
	 * =HttpUtil.URL_COMMENTS_LIST+"&curpage=" + curpageIndex; HttpUtil.get(uri,
	 * requestParams, new AsyncHttpResponseHandler() {
	 * 
	 * @Override public void onSuccess(int statusCode, Header[] headers, byte[]
	 * responseBody) { try { String jsonString = new String(responseBody);
	 * if(Integer.parseInt(show_pa)>=curpageIndex){ JSONObject jsonObjects =new
	 * JSONObject(jsonString).getJSONObject("datas"); String comments
	 * =jsonObjects.getString("comments"); List<CommentsBean> tmpList =
	 * gson.fromJson(comments, new TypeToken<List<CommentsBean>>() {
	 * }.getType()); informationList.addAll(tmpList);
	 * listViewIMUserAdapter.setData(informationList);
	 * listViewIMUserAdapter.notifyDataSetChanged(); informationListIndex +=
	 * tmpList.size(); }else { ToastUtils.showMessage(appContext, "�Ѿ�û��������");
	 * curpageIndex--; } footView.setVisibility(View.GONE); isEnabledScrollLast
	 * = true; } catch (Exception e) {
	 * ToastUtils.showMessage(appContext,"����ʧ�ܣ����Ժ�����"); curpageIndex--; }
	 * 
	 * }
	 * 
	 * @Override public void onFailure(int statusCode, Header[] headers, byte[]
	 * responseBody, Throwable error) { ToastUtils.showMessage(appContext,
	 * R.string.get_informationData_failure);
	 * mLoadMoreButton.setVisibility(View.VISIBLE); isEnabledScrollLast = false;
	 * }
	 * 
	 * }); }
	 */

}
