package shop.trqq.com.ui.Fragment;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.ListViewVoucherAdapter;
import shop.trqq.com.bean.VoucherListBean;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * ������б�Fragment
 *
 * @author Weiss
 */
public class VoucherListFragment extends Fragment {
    private static final String TAG = "FavoritesListActivity";

    // ȫ��Context
    private Context appContext;
    // Gson����
    private Gson gson;
    /*
     * private LoopViewPager loopViewPager; private ArrayList<String> imageUris;
     */
    // ����������
    private RelativeLayout header_layout;
    private TextView mHeadTitleTextView;

    private ListViewVoucherAdapter listViewGoodAdapter;
    // ��Ѷ���ش���ͼ��
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
    private String voucher_state;

    private List<VoucherListBean> informationList;

    private int informationListIndex = 0;
    private int curpageIndex = 1;

    private Boolean hasmore;

    private View rootView;// ����Fragment view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_pulltorefreshlistview, container,
                false);
        appContext = getActivity();
        gson = new Gson();
        informationList = new ArrayList<VoucherListBean>();
        Bundle bundle = getArguments();
        voucher_state = bundle.getString("voucher_state");
        initTitleBarView();
        initViews();
        return rootView;
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        YkLog.i(TAG, "��ʼ����������ͼ");
        header_layout = (RelativeLayout) rootView
                .findViewById(R.id.header_relativelayout);
        header_layout.setVisibility(View.GONE);

    }

    private void initViews() {
        listViewGoodAdapter = new ListViewVoucherAdapter(appContext);
        inflater = LayoutInflater.from(appContext);
        footView = inflater.inflate(R.layout.item_load_more, null);
        mLoadMoreButton = (Button) footView.findViewById(R.id.loadMore_button);
        progressActivity = (ProgressActivity) rootView
                .findViewById(R.id.PullToRefreshListView_progress);
        mHomePullToRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.shop_pullToRefreshListView);
        // mNetProgressBar.setVisibility(View.VISIBLE);
        mHomePullToRefreshListView.getRefreshableView().addFooterView(footView);
        mHomePullToRefreshListView.setPullToRefreshOverScrollEnabled(false);
        mHomePullToRefreshListView.setAdapter(listViewGoodAdapter);
        mHomePullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        curpageIndex = 1;
                        loadOnlineInformationData();
                    }
                });

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
     *
     * @param object
     */
    public void loadData() {
        mHomePullToRefreshListView.setRefreshing(false);
    }

    /**
     * ������Ʒ�б�
     *
     * @param information_id
     */
    private void loadOnlineInformationData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("voucher_state", voucher_state);
        final String uri = HttpUtil.URL_VOUCHER + "&curpage=" + curpageIndex
                + "&page=" + 10;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    System.out.println(jsonString);
                    hasmore = true;
                    hasmore = new JSONObject(jsonString).getBoolean("hasmore");
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");

                    String voucher_list = jsonObjects.getString("voucher_list");
                    informationList = gson.fromJson(voucher_list,
                            new TypeToken<List<VoucherListBean>>() {
                            }.getType());
                    // AppConfig.setSharedPreferences(appContext, configKey,
                    // goods_list);
                    listViewGoodAdapter.setData(informationList);
                    listViewGoodAdapter.notifyDataSetChanged();
                    informationListIndex += informationList.size();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (informationList.size() > 0) {
                    progressActivity.showContent();
                } else {
                    if (isAdded()) {
                        Drawable emptyDrawable = appContext.getResources()
                                .getDrawable(R.drawable.ic_empty);
                        progressActivity.showEmpty(emptyDrawable, "û������", "");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                ToastUtils.showMessage(appContext,
                        R.string.get_informationData_failure);
                try {
                    if (informationList.size() == 0) {
                        Drawable errorDrawable = appContext.getResources()
                                .getDrawable(R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable, "���翪��С��",
                                "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����", "��������",
                                errorClickListener);
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

    OnClickListener errorClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            progressActivity.showLoading();
            informationList.clear();
            loadOnlineInformationData();
        }
    };

    private void loadMoreInformationData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        curpageIndex++;
        final String uri = HttpUtil.URL_VOUCHER + "&curpage=" + curpageIndex
                + "&page=" + 10;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    if (hasmore) {
                        hasmore = new JSONObject(jsonString)
                                .getBoolean("hasmore");
                        JSONObject jsonObjects = new JSONObject(jsonString)
                                .getJSONObject("datas");
                        System.out.println(jsonObjects);
                        String voucher_list = jsonObjects
                                .getString("voucher_list");

                        List<VoucherListBean> tmpList = gson.fromJson(
                                voucher_list,
                                new TypeToken<List<VoucherListBean>>() {
                                }.getType());
                        informationList.addAll(tmpList);
                        listViewGoodAdapter.setData(informationList);
                        listViewGoodAdapter.notifyDataSetChanged();
                        informationListIndex += tmpList.size();
                    } else {
                        ToastUtils.showMessage(appContext, "�Ѿ�û��������");
                    }
                    footView.setVisibility(View.GONE);
                    isEnabledScrollLast = true;
                } catch (Exception e) {
                    ToastUtils.showMessage(appContext,
                            R.string.get_informationData_failure);
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
