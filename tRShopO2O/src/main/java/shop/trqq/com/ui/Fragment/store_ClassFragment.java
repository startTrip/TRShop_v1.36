package shop.trqq.com.ui.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import shop.trqq.com.adapter.ListViewStoreClassAdapter;
import shop.trqq.com.bean.StoreClassBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 店铺分类Fragment
 */
public class store_ClassFragment extends Fragment {
    // 全局Context
    private Context appContext;
    // Gson工具
    private Gson gson;
    /*
     * private LoopViewPager loopViewPager; private ArrayList<String> imageUris;
     */
    // 标题栏标题
    private RelativeLayout header_layout;
    private ListViewStoreClassAdapter mListViewStoreAdapter;
    // 资讯加载错误容器
    private ProgressActivity progressActivity;
    private PullToRefreshListView mHomePullToRefreshListView;
    // 加载更多，脚部布局
    private View footView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;

    private List<StoreClassBean> mStoreList;

    private int curpageIndex = 1;
    private Boolean hasmore;
    private View rootView;// 缓存Fragment view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_pulltorefreshlistview, container,
                false);
        appContext = getActivity();
        gson = new Gson();

        mStoreList = new ArrayList<StoreClassBean>();
        initTitleBarView();
        initViews();
        return rootView;
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        header_layout = (RelativeLayout) rootView
                .findViewById(R.id.header_relativelayout);
        header_layout.setVisibility(View.GONE);
    }

    private void initViews() {
        mListViewStoreAdapter = new ListViewStoreClassAdapter(appContext);
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
                        StoreClassBean mStoreClass = mStoreList.get(position - 1);
                        UIHelper.showStore_list2(appContext, "0", mStoreClass.getSc_id(), mStoreClass.getSc_name());
                    }

                });
        mHomePullToRefreshListView.setAdapter(mListViewStoreAdapter);
        //设置上拉下拉模式
/*		mHomePullToRefreshListView.setMode(Mode.BOTH);  
        mHomePullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				curpageIndex = 1;
				loadOnlineInformationData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (isEnabledScrollLast) {
					loadMoreInformationData();
				}
			}		
		});*/
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
     * @param information_id
     */
    private void loadOnlineInformationData() {
        // 加载商品列表
        RequestParams requestParams = new RequestParams();
        requestParams.add("curpage", curpageIndex + "");
        //requestParams.add("sc_id", "3");
        String uri = HttpUtil.URL_SHOP_CLASS;
        // loadOnlineStoreData();
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    YkLog.t("class_list", jsonString);
                    hasmore = true;
                    hasmore = new JSONObject(jsonString).optBoolean("hasmore");
                    // System.out.println("jsonString:" + jsonString);
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    String class_list = jsonObjects.getString("class_list");
                    mStoreList = gson.fromJson(class_list,
                            new TypeToken<List<StoreClassBean>>() {
                            }.getType());
                    // AppConfig.setSharedPreferences(appContext, configKey,
                    // goods_list);
                    mListViewStoreAdapter.setData(mStoreList);
                    mListViewStoreAdapter.notifyDataSetChanged();
					/*
					 * if (mStoreList.size() == 0) {
					 * mLoadErrorTextView.setText("该分类或搜索\n暂时没有商品");
					 * mLoadErrorLinearLayout.setVisibility(View.VISIBLE);
					 * mHomePullToRefreshListView.setVisibility(View.GONE); } if
					 * (store_id.length() != 0 || store_id != null) { String
					 * store_name = jsonObjects.optString("store_name");
					 * mHeadTitleTextView.setText(store_name); }
					 */
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mStoreList.size() > 0) {
                    progressActivity.showContent();
                } else {
                    Drawable emptyDrawable = getResources().getDrawable(
                            R.drawable.ic_empty);
                    progressActivity.showEmpty(emptyDrawable, "暂时没有店铺", "");
                }
            }

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
        String uri = HttpUtil.URL_SHOP_CLASS;
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
                        String class_list = jsonObjects.getString("class_list");

                        List<StoreClassBean> tmpList = gson.fromJson(class_list,
                                new TypeToken<List<StoreClassBean>>() {
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
                //mLoadMoreButton.setVisibility(View.VISIBLE);
                //isEnabledScrollLast = false;
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mHomePullToRefreshListView.onRefreshComplete();
            }

        });

    }

}
