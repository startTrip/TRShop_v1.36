package shop.trqq.com.ui.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
import shop.trqq.com.adapter.ListViewStoreAdapter;
import shop.trqq.com.bean.StoreListBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 商店列表Fragment
 */
public class store_listFragment extends Fragment {
    // 全局Context
    private Context appContext;
    // Gson工具
    private Gson gson;
    // 标题栏标题
    private RelativeLayout header_layout;
    private TextView mHeadTitleTextView;

    private ListViewStoreAdapter mListViewStoreAdapter;
    // 资讯加载错误图标
    private ProgressActivity progressActivity;
    private PullToRefreshListView mHomePullToRefreshListView;
    // 加载更多，脚部布局
    private View footView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;

    private List<StoreListBean> mStoreList;

    private int curpageIndex = 1;
    private Boolean hasmore;
    private String keyword;
    private String sc_id = new String();
    private String sc_name;
    private String is_strategic_alliance;

    private View rootView;// 缓存Fragment view
    private ImageView mImageBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_pulltorefreshlistview, container,
                false);
        appContext = getActivity();
        gson = new Gson();
        Bundle bundle = getArguments();
        try {
            sc_id = bundle.getString("sc_id");
            sc_name = bundle.getString("sc_name");
            is_strategic_alliance = bundle.getString("is_strategic_alliance");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        YkLog.t("store_listFragment", is_strategic_alliance + "|" + sc_id + "|" + sc_name);
        mStoreList = new ArrayList<StoreListBean>();
        initTitleBarView(rootView);
        initViews();
        return rootView;
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView(View rootView) {
        mImageBack = (ImageView)rootView.findViewById(R.id.title_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        header_layout = (RelativeLayout) rootView
                .findViewById(R.id.header_relativelayout);
        mHeadTitleTextView = (TextView) rootView
                .findViewById(R.id.head_title_textView);
        if ("1".equals(is_strategic_alliance))
            mHeadTitleTextView.setText("战略联盟商家");
        else {
            mHeadTitleTextView.setText(sc_name);
        }
        if (sc_id == null)
            header_layout.setVisibility(View.GONE);
    }

    private void initViews() {
        mListViewStoreAdapter = new ListViewStoreAdapter(appContext);
        inflater = LayoutInflater.from(appContext);
        footView = inflater.inflate(R.layout.item_load_more, null);
        mLoadMoreButton = (Button) footView.findViewById(R.id.loadMore_button);
        progressActivity = (ProgressActivity) rootView.findViewById(R.id.PullToRefreshListView_progress);
        mHomePullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.shop_pullToRefreshListView);
        mHomePullToRefreshListView.getRefreshableView().addFooterView(footView);
        mHomePullToRefreshListView.setPullToRefreshOverScrollEnabled(false);
        loadOnlineInformationData();
        progressActivity.showLoading();
        mHomePullToRefreshListView
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub
                        String store_id = mStoreList.get(position - 1).getStore_id();
                        if(TextUtils.equals("126",store_id)){
                            UIHelper.showMarket(getActivity());
                        }else {
                            UIHelper.showStore(appContext,store_id);
                        }
                    }
                });
        mHomePullToRefreshListView.setAdapter(mListViewStoreAdapter);
        mHomePullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                isEnabledScrollLast = true;
                footView.setVisibility(View.GONE);
                mLoadMoreButton.setVisibility(View.GONE);
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

    }


    /**
     * 加载店铺列表
     *
     * @param
     */
    private void loadOnlineInformationData() {
        // 加载商品列表
        RequestParams requestParams = new RequestParams();
        requestParams.add("curpage", curpageIndex + "");
        if ("".equals(sc_id)) {
            requestParams.add("is_sa", is_strategic_alliance);
        } else {
            requestParams.add("sc_id", sc_id);
        }
        String uri = HttpUtil.URL_SHOP;
        // loadOnlineStoreData();
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    YkLog.t("shop_list", jsonString);
                    hasmore = true;
                    hasmore = new JSONObject(jsonString).optBoolean("hasmore");
                    // System.out.println("jsonString:" + jsonString);
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    String store_list = jsonObjects.getString("store_list");
                    mStoreList = gson.fromJson(store_list,
                            new TypeToken<List<StoreListBean>>() {
                            }.getType());
                    // AppConfig.setSharedPreferences(appContext, configKey,
                    // goods_list);
                    mListViewStoreAdapter.setData(mStoreList);
                    mListViewStoreAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mStoreList.size() > 0) {
                    progressActivity.showContent();
                } else {
                    Drawable emptyDrawable = getResources().getDrawable(
                            R.drawable.ic_empty);
                    progressActivity.showEmpty(emptyDrawable, "没有数据", "");
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    if (mStoreList.size() == 0) {
                        Drawable errorDrawable = getResources().getDrawable(
                                R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable, "网络开了小差",
                                "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试", "重新连接",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        progressActivity.showLoading();
                                        mStoreList.clear();
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
        // 加载更多数据
        RequestParams requestParams = new RequestParams();
        curpageIndex++;
        requestParams.add("curpage", curpageIndex + "");
        if ("".equals(sc_id)) {
            requestParams.add("is_sa", is_strategic_alliance);
            YkLog.t("store_listFragment", is_strategic_alliance + "|" + sc_id);
        } else {
            requestParams.add("sc_id", sc_id);
        }
        String uri = HttpUtil.URL_SHOP;
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
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
                        // System.out.println(jsonObjects);
                        String store_list = jsonObjects.getString("store_list");

                        List<StoreListBean> tmpList = gson.fromJson(store_list,
                                new TypeToken<List<StoreListBean>>() {
                                }.getType());
                        mStoreList.addAll(tmpList);
                        mListViewStoreAdapter.setData(mStoreList);
                        mListViewStoreAdapter.notifyDataSetChanged();
                        footView.setVisibility(View.GONE);
                    } else {
                        ToastUtils.showMessage(appContext, "已经没有数据了");
                        mLoadMoreButton.setText(R.string.load_finish);
                        mLoadMoreButton.setVisibility(View.VISIBLE);
                        isEnabledScrollLast = false;
                    }
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
/*				mLoadMoreButton.setText(R.string.get_informationData_failure);
				mLoadMoreButton.setVisibility(View.VISIBLE);
				isEnabledScrollLast = false;*/
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mHomePullToRefreshListView.onRefreshComplete();
            }

        });

    }

}
