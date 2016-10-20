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
import shop.trqq.com.adapter.ListViewFavoritesAdapter;
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * �ղ��б�Activity
 */
public class FavoritesListActivity extends BaseActivity {
    private static final String TAG = "FavoritesListActivity";

    // ȫ��Context
    private Context appContext;
    // Gson����
    private Gson gson;
    /*
     * private LoopViewPager loopViewPager; private ArrayList<String> imageUris;
     */
    // ����������
    private TextView mHeadTitleTextView;
    private ListViewFavoritesAdapter listViewGoodAdapter;
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
    private GoodsBean GoodsBean;

    private List<GoodsBean> informationList;

    private int informationListIndex = 0;
    private int curpageIndex = 1;

    private Boolean hasmore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulltorefreshlistview);
        appContext = this;
        gson = new Gson();
        informationList = new ArrayList<GoodsBean>();
        initTitleBarView();
        initViews();
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        YkLog.i(TAG, "��ʼ����������ͼ");
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("��ע��Ʒ�б�");

    }

    private void initViews() {
        listViewGoodAdapter = new ListViewFavoritesAdapter(appContext);
        inflater = LayoutInflater.from(this);
        footView = inflater.inflate(R.layout.item_load_more, null);
        mLoadMoreButton = (Button) footView.findViewById(R.id.loadMore_button);
        progressActivity = (ProgressActivity) findViewById(R.id.PullToRefreshListView_progress);
        mHomePullToRefreshListView = (PullToRefreshListView) findViewById(R.id.shop_pullToRefreshListView);
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

        loadOnlineInformationData();
        progressActivity.showLoading();
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
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        final String uri = HttpUtil.URL_FAVORITES_LIST + "&curpage="
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
                    System.out.println(jsonObjects);
                    String favorites_list = jsonObjects
                            .getString("favorites_list");
                    informationList = gson.fromJson(favorites_list,
                            new TypeToken<List<GoodsBean>>() {
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
                    Drawable emptyDrawable = getResources().getDrawable(
                            R.drawable.ic_empty);
                    progressActivity.showEmpty(emptyDrawable, "û������", "");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    if (informationList.size() == 0) {
                        Drawable errorDrawable = getResources().getDrawable(
                                R.drawable.wifi_off);
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
                mHomePullToRefreshListView.onRefreshComplete();
            }

        });
    }

    private void loadMoreInformationData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        curpageIndex++;
        final String uri = HttpUtil.URL_FAVORITES_LIST + "&curpage="
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

                        List<GoodsBean> tmpList = gson.fromJson(favorites_list,
                                new TypeToken<List<GoodsBean>>() {
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
