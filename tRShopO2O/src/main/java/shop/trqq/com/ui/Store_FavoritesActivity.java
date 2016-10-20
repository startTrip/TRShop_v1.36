package shop.trqq.com.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
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
import shop.trqq.com.adapter.ListViewFavoritesStoreAdapter;
import shop.trqq.com.bean.StoreFavoritesListBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 收藏的店铺
 */
public class Store_FavoritesActivity extends BaseActivity {
    private static final String TAG = "FavoritesListActivity";

    // 全局Context
    private Context appContext;
    // Gson工具
    private Gson gson;
    /*
     * private LoopViewPager loopViewPager; private ArrayList<String> imageUris;
     */
    // 标题栏标题
    private TextView mHeadTitleTextView;

    private ListViewFavoritesStoreAdapter listViewFavoritesStoreAdapter;
    // 资讯加载错误图标
    private ProgressActivity progressActivity;
    private PullToRefreshListView mHomePullToRefreshListView;

    // 加载更多，脚部布局
    private View footView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;
    /**
     * 要显示数据实体类.
     */
    private String voucher_state;

    private List<StoreFavoritesListBean> informationList;

    private int informationListIndex = 0;
    private int curpageIndex = 1;

    private Boolean hasmore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulltorefreshlistview);
        appContext = this;
        gson = new Gson();
        informationList = new ArrayList<StoreFavoritesListBean>();
        initTitleBarView();
        initViews();
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        YkLog.i(TAG, "初始化标题栏视图");
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("我收藏的店铺");
    }

    private void initViews() {
        listViewFavoritesStoreAdapter = new ListViewFavoritesStoreAdapter(
                appContext);
        inflater = LayoutInflater.from(appContext);
        footView = inflater.inflate(R.layout.item_load_more, null);
        mLoadMoreButton = (Button) footView.findViewById(R.id.loadMore_button);
        progressActivity = (ProgressActivity) findViewById(R.id.PullToRefreshListView_progress);
        mHomePullToRefreshListView = (PullToRefreshListView) findViewById(R.id.shop_pullToRefreshListView);
        // mNetProgressBar.setVisibility(View.VISIBLE);
        mHomePullToRefreshListView.getRefreshableView().addFooterView(footView);
        mHomePullToRefreshListView.setPullToRefreshOverScrollEnabled(false);
        progressActivity.showLoading();
        loadOnlineInformationData();

        mHomePullToRefreshListView.setAdapter(listViewFavoritesStoreAdapter);
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

        // loadLocalInformationData("2");

    }

    /**
     * 加载网络数据
     *
     * @param object
     */
    public void loadData() {
        mHomePullToRefreshListView.setRefreshing(false);
    }

    /**
     * 加载商品列表
     *
     * @param information_id
     */
    private void loadOnlineInformationData() {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        final String uri = HttpUtil.URL_STORE_FAVORITES_LIST + "&curpage="
                + curpageIndex + "&page=" + 10;
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    hasmore = true;
                    hasmore = new JSONObject(jsonString).getBoolean("hasmore");
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");

                    String favorites_list = jsonObjects
                            .getString("favorites_list");
                    informationList = gson.fromJson(favorites_list,
                            new TypeToken<List<StoreFavoritesListBean>>() {
                            }.getType());
                    // AppConfig.setSharedPreferences(appContext, configKey,
                    // goods_list);
                    listViewFavoritesStoreAdapter.setData(informationList);
                    listViewFavoritesStoreAdapter.notifyDataSetChanged();
                    informationListIndex += informationList.size();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (informationList.size() > 0) {
                    progressActivity.showContent();
                } else {
                    Drawable emptyDrawable = getResources().getDrawable(
                            R.drawable.ic_empty);
                    progressActivity.showEmpty(emptyDrawable, "没有数据", "");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                ToastUtils.showMessage(appContext,
                        R.string.get_informationData_failure);
                try {
                    if (informationList.size() == 0) {
                        Drawable errorDrawable = getResources().getDrawable(
                                R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable, "网络开了小差",
                                "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试", "重新连接",
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
                // 不管成功或者失败，都要将进度条关闭掉
                mHomePullToRefreshListView.onRefreshComplete();
            }

        });
    }

    private void loadMoreInformationData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        curpageIndex++;
        final String uri = HttpUtil.URL_STORE_FAVORITES_LIST + "&curpage="
                + curpageIndex + "&page=" + 10;
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
                        String favorites_list = jsonObjects
                                .getString("favorites_list");

                        List<StoreFavoritesListBean> tmpList = gson.fromJson(
                                favorites_list,
                                new TypeToken<List<StoreFavoritesListBean>>() {
                                }.getType());
                        informationList.addAll(tmpList);
                        listViewFavoritesStoreAdapter.setData(informationList);
                        listViewFavoritesStoreAdapter.notifyDataSetChanged();
                        informationListIndex += tmpList.size();
                    } else {
                        ToastUtils.showMessage(appContext, "已经没有数据了");
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
