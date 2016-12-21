package shop.trqq.com.ui.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import shop.trqq.com.R;
import shop.trqq.com.adapter.recycler.RecViewGoodsSpAdapter;
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.recyclerview.OnRcvScrollListener;

/**
 * ��Ʒ�б�Fragment
 */
public class Fragment_Goods extends Fragment implements OnClickListener {
    private static final String TAG = "Fragment_Goods";
    // ȫ��Context
    private Context appContext;
    // Gson����
    private Gson gson;
    // ����������
    private TextView mHeadTitleTextView;
    //private ListViewGoodsAdapter listViewGoodAdapter;
    private RecViewGoodsSpAdapter mRecViewCartAdapter;
    // ���ؽ���
    private ProgressActivity progressActivity;
    private PullToRefreshRecyclerView mHomePullToRefreshListView;
    // ������
    private TextView shopNew;
    private TextView shopSales;
    private TextView shopPrice;
    private TextView shopPopularity;

    private LinearLayout store_search_layout;
    private EditText store_search_edit;
    private TextView store_search_button;
    @Bind(R.id.float_imageButton)
    ImageView Go_UpButton;
    // ���ظ��࣬�Ų�����
    private View footView;
    private LinearLayout topShopView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;
    /**
     * Ҫ��ʾ����ʵ����.
     */
    private GoodsBean GoodsBean;

    private List<GoodsBean> informationList;

    private boolean isfinish = true;
    private int informationListIndex = 0;
    private int curpageIndex = 1;
    private String key = "1";
    private String order = "0";
    private Boolean hasmore;
    private String keyword;
    private String gc_id;
    private String store_id;
    private String brand;

    private View rootView;// ����Fragment view
    private String mStc_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goods, container,
                false);
        //ButterKnife��
        ButterKnife.bind(this, rootView);
        appContext = getActivity();
        gson = new Gson();
//		Intent intent = getIntent();
        Bundle bundle = getArguments();
        mStc_id = bundle.getString("stc_id");
        key = bundle.getString("key");
        order = bundle.getString("order");
        keyword = bundle.getString("keyword");
        if (mStc_id ==null) {
            try {
                gc_id = bundle.getString("gc_id");
                store_id = bundle.getString("store_id");
                brand = bundle.getString("brand");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            store_id = bundle.getString("store_id");
        }
        informationList = new ArrayList<GoodsBean>();

        initViews();
        return rootView;
    }


    private void initViews() {
        //listViewGoodAdapter = new ListViewGoodsAdapter(appContext);
        inflater = LayoutInflater.from(appContext);
        footView = inflater.inflate(R.layout.item_load_more, null);
        footView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mLoadMoreButton = (Button) footView.findViewById(R.id.loadMore_button);
        progressActivity = (ProgressActivity) rootView.findViewById(R.id.shop_progress);
        //Go_UpButton = (ImageView) rootView.findViewById(R.id.float_imageButton);

        mHomePullToRefreshListView = (PullToRefreshRecyclerView) rootView.findViewById(R.id.shop_pullToRefreshListView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(appContext);
//		StaggeredGridLayoutManager SGlayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mHomePullToRefreshListView.setLayoutManager(layoutManager);
        mRecViewCartAdapter = new RecViewGoodsSpAdapter(appContext, informationList, R.layout.list_goods);
        mHomePullToRefreshListView.setAdapter(mRecViewCartAdapter);
        mRecViewCartAdapter.addFooterView(footView);
        //mHomePullToRefreshListView.setPullToRefreshOverScrollEnabled(false);
        progressActivity.showLoading();
        loadOnlineInformationData();
        mHomePullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<RecyclerView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<RecyclerView> refreshView) {
                        isEnabledScrollLast = true;
                        footView.setVisibility(View.GONE);
                        mLoadMoreButton.setVisibility(View.GONE);
                        curpageIndex = 1;
                        loadOnlineInformationData();
                    }

                });

        // ����ʾ����������/�ײ�����Ӱ�����ٻ��ƣ�
        mHomePullToRefreshListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mHomePullToRefreshListView.setOnScrollListener(new OnRcvScrollListener() {

            @Override
            public void onScrolled(int distanceX, int distanceY) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollUp() {
                // TODO Auto-generated method stub
                Go_UpButton.setVisibility(View.GONE);
            }

            @Override
            public void onScrollDown() {
                // TODO Auto-generated method stub
                Go_UpButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBottom() {
                // TODO Auto-generated method stub
                if (isEnabledScrollLast) {
                    footView.setVisibility(View.VISIBLE);
                    loadMoreInformationData();
                }
            }
        });
        Go_UpButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //��������
                mHomePullToRefreshListView.getRefreshableView().smoothScrollToPosition(0);
            }
        });
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

        // ������Ʒ�б�
        RequestParams requestParams = new RequestParams();
        requestParams.add("page", "10");
        requestParams.add("curpage", curpageIndex + "");
        requestParams.add("key", key + "");
        requestParams.add("order",order + "");
        String uri;
        if (store_id.length() == 0 || store_id == null) {
            requestParams.add("brand", brand);
            requestParams.add("keyword", keyword + "");
            requestParams.add("gc_id", gc_id + "");
            uri = HttpUtil.URL_GOODSLIST;
            YkLog.e("lujing","�̳�");
        } else if(!TextUtils.isEmpty(mStc_id)){    // ����ǳ��и��ݷ���������
            requestParams.add("stc_id",mStc_id);
            requestParams.add("inkeyword", keyword + "");
            requestParams.add("store_id", store_id + "");
            uri = HttpUtil.URL_MARKET_GOODS_LIST;
            YkLog.e("lujing","����ͨ������");
        }else {  // ������������̵Ĺؼ���
            requestParams.add("inkeyword", keyword + "");
            requestParams.add("store_id", store_id + "");
            uri = HttpUtil.URL_STORE;
            YkLog.e("lujing","����ͨ������");
        }
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    hasmore = true;
                    hasmore = new JSONObject(jsonString).optBoolean("hasmore");
                    // System.out.println("jsonString:" + jsonString);
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    String goods_list = jsonObjects.getString("goods_list");
                    informationList.clear();
                    informationList = gson.fromJson(goods_list,
                            new TypeToken<List<GoodsBean>>() {
                            }.getType());
                    // AppConfig.setSharedPreferences(appContext, configKey,
                    // goods_list);
                    mRecViewCartAdapter.replaceAll(informationList);
                    //mRecViewCartAdapter.notifyDataSetChanged();
                    //informationListIndex += informationList.size();
                    if (store_id.length() != 0 && store_id != null) {
                        String store_name = jsonObjects.optString("store_name");
                        mHeadTitleTextView.setText(store_name);
                    }
                    if (!hasmore) {
                        footView.setVisibility(View.VISIBLE);
                        mLoadMoreButton.setText(R.string.load_finish);
                        mLoadMoreButton.setVisibility(View.VISIBLE);
                        isEnabledScrollLast = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (informationList.size() > 0) {
                    progressActivity.showContent();
                } else {
                    if (isAdded()) {
                        Drawable emptyDrawable = ContextCompat.getDrawable(appContext, R.drawable.ic_empty);
/*                        Drawable emptyDrawable = getResources().getDrawable(
                                R.drawable.ic_empty);*/
                        progressActivity.showEmpty(emptyDrawable, "�÷��������\n��ʱû����Ʒ",
                                "");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    if (informationList.size() == 0 && isAdded()) {
                        Drawable errorDrawable = ContextCompat.getDrawable(appContext, R.drawable.wifi_off);
/*                        Drawable errorDrawable = getResources().getDrawable(
                                R.drawable.wifi_off);*/
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
        isfinish = false;
        // ���ظ�������
        RequestParams requestParams = new RequestParams();
        requestParams.add("page", "10");
        requestParams.add("key", key + "");
        requestParams.add("order", order + "");
        curpageIndex++;
        requestParams.add("curpage", curpageIndex + "");
        String uri;
        // ����Ǹ��� id ȥ�����̳������
        if (store_id.length() == 0 || store_id == null) {
            requestParams.add("keyword", keyword + "");
            requestParams.add("gc_id", gc_id + "");
            uri = HttpUtil.URL_GOODSLIST;
        } else if(!TextUtils.isEmpty(mStc_id)){    // ����ǳ��е�
                requestParams.add("stc_id",mStc_id);
                requestParams.add("inkeyword", keyword + "");
                requestParams.add("store_id", store_id + "");
                uri = HttpUtil.URL_MARKET_GOODS_LIST;
        }else {  // ������������̵Ĺؼ���
            requestParams.add("inkeyword", keyword + "");
            requestParams.add("store_id", store_id + "");
            uri = HttpUtil.URL_STORE;
        }
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
                        String goods_list = jsonObjects.getString("goods_list");

                        List<GoodsBean> tmpList = gson.fromJson(goods_list,
                                new TypeToken<List<GoodsBean>>() {
                                }.getType());
                        //informationList.addAll(tmpList);
                        mRecViewCartAdapter.addAll(tmpList);
                        //mRecViewCartAdapter.notifyDataSetChanged();
                        //informationListIndex += tmpList.size();
                        footView.setVisibility(View.GONE);
                    } else {
                        ToastUtils.showMessage(appContext, "�Ѿ�û��������");
                        footView.setVisibility(View.VISIBLE);
                        mLoadMoreButton.setText(R.string.load_finish);
                        mLoadMoreButton.setVisibility(View.VISIBLE);
                        isEnabledScrollLast = false;
                    }
                    //footView.setVisibility(View.GONE);
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
                isfinish = true;
            }
        });

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
