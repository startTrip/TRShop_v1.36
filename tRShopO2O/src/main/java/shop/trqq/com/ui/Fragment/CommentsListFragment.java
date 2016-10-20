package shop.trqq.com.ui.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.adapter.ListViewCommentsAdapter;
import shop.trqq.com.bean.CommentsBean;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
/**
 * ������Ʒ�����б�
 */
public class CommentsListFragment extends Fragment {
    private static final String TAG = "CommentsListActivity";

    // ȫ��Context
    private Context appContext;
    // Gson����
    private Gson gson;
    /*
	 * private LoopViewPager loopViewPager; private ArrayList<String> imageUris;
	 */

    private ListViewCommentsAdapter listViewGoodAdapter;
    // ���ؽ���Activity
    private ProgressActivity progressActivity;
    private PullToRefreshListView mHomePullToRefreshListView;

    // ���ظ��࣬�Ų�����
    private View footView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;
    /**
     * Ҫ��ʾ����ʵ����.
     */

    private List<CommentsBean> informationList;

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
        rootView = inflater.inflate(R.layout.fragment_commentslist, container,
                false);
        appContext = getActivity();
        gson = new Gson();
        informationList = new ArrayList<CommentsBean>();
        Bundle bundle = getArguments();
        goods_id = bundle.getString("goods_id");
        type = bundle.getString("type");
        initViews();

        return rootView;
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {

    }

    private void initViews() {
        listViewGoodAdapter = new ListViewCommentsAdapter(appContext);
        inflater = LayoutInflater.from(appContext);
        footView = inflater.inflate(R.layout.item_load_more, null);
        mLoadMoreButton = (Button) footView.findViewById(R.id.loadMore_button);
        progressActivity = (ProgressActivity) rootView
                .findViewById(R.id.PullToRefreshListView_progress);
        mHomePullToRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.shop_pullToRefreshListView);
        mHomePullToRefreshListView.getRefreshableView().addFooterView(footView);
        mHomePullToRefreshListView.setPullToRefreshOverScrollEnabled(false);
        mHomePullToRefreshListView.setAdapter(listViewGoodAdapter);
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
                            loadMoreInformationData();
                        }
                    }
                });
        progressActivity.showLoading();
        loadOnlineInformationData();
    }

    /**
     * ������������
     */
    public void loadData() {
        mHomePullToRefreshListView.setRefreshing(false);
    }

    /**
     * ������Ʒ�б�
     */
    private void loadOnlineInformationData() {
        RequestParams requestParams = new RequestParams();
        requestParams.add("goods_id", goods_id);
        requestParams.add("type", type);
        final String uri = HttpUtil.URL_COMMENTS_LIST + "&curpage="
                + curpageIndex;
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    // hasmore=new JSONObject(jsonString).getBoolean("hasmore");
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    try {
                        String errStr = jsonObjects.getString("error");
                        if (errStr != null) {
                            // ToastUtils.showMessage(appContext, errStr);
                            if (informationList.size() == 0&&isAdded()) {
                                Drawable emptyDrawable = ContextCompat.getDrawable(appContext, R.drawable.ic_empty);
                                progressActivity.showEmpty(emptyDrawable,
                                        errStr, "");
                            }
                        }
                    } catch (Exception e) {
                        JSONObject goods_evaluate_info = jsonObjects
                                .getJSONObject("goods_evaluate_info");
                        JSONObject page = goods_evaluate_info
                                .getJSONObject("page");
                        show_pa = page.getString("show_pa");
                        String comments = jsonObjects.getString("comments");
                        informationList = gson.fromJson(comments,
                                new TypeToken<List<CommentsBean>>() {
                                }.getType());
                        // AppConfig.setSharedPreferences(appContext, configKey,
                        // goods_list);
                        listViewGoodAdapter.setData(informationList);
                        listViewGoodAdapter.notifyDataSetChanged();
                        informationListIndex += informationList.size();
                        progressActivity.showContent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    if (informationList.size() == 0&&isAdded()) {
                        Drawable errorDrawable =ContextCompat.getDrawable(appContext, R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable, "���翪��С��",
                                "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����", "��������",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        progressActivity.showLoading();
                                        informationList.clear();
                                        loadOnlineInformationData();
                                    }
                                });
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

    private void loadMoreInformationData() {
        RequestParams requestParams = new RequestParams();
        requestParams.add("goods_id", goods_id);
        requestParams.add("type", type);
        curpageIndex++;
        final String uri = HttpUtil.URL_COMMENTS_LIST + "&curpage="
                + curpageIndex;
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    if (Integer.parseInt(show_pa) >= curpageIndex) {
                        JSONObject jsonObjects = new JSONObject(jsonString)
                                .getJSONObject("datas");
                        String comments = jsonObjects.getString("comments");
                        List<CommentsBean> tmpList = gson.fromJson(comments,
                                new TypeToken<List<CommentsBean>>() {
                                }.getType());
                        informationList.addAll(tmpList);
                        listViewGoodAdapter.setData(informationList);
                        listViewGoodAdapter.notifyDataSetChanged();
                        informationListIndex += tmpList.size();
                    } else {
                        ToastUtils.showMessage(appContext, "�Ѿ�û��������");
                        curpageIndex--;
                    }
                    footView.setVisibility(View.GONE);
                    isEnabledScrollLast = true;
                } catch (Exception e) {
                    ToastUtils.showMessage(appContext, "����ʧ�ܣ����Ժ�����");
                    curpageIndex--;
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                ToastUtils.showMessage(appContext,
                        R.string.get_informationData_failure);
                mLoadMoreButton.setVisibility(View.VISIBLE);
                isEnabledScrollLast = false;
            }

        });
    }

}
