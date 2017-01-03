package shop.trqq.com.supermarket.fragments;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import shop.trqq.com.NetworkStateService;
import shop.trqq.com.R;
import shop.trqq.com.bean.Mb_SlidersBean;
import shop.trqq.com.supermarket.adapters.HomeClassifyAdapter;
import shop.trqq.com.supermarket.adapters.HomeNewAdapter;
import shop.trqq.com.supermarket.adapters.HomePagerAdapter;
import shop.trqq.com.supermarket.adapters.HomeRecommendAdapter;
import shop.trqq.com.supermarket.bean.HomeClassifyTitle;
import shop.trqq.com.supermarket.bean.HomeRecommendData;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketHomeFragment extends Fragment implements ViewPager.OnPageChangeListener, HomeClassifyAdapter.ClassifyClickCallback, AdapterView.OnItemClickListener {

    private static final String TAG = MarketHomeFragment.class.getSimpleName();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewPager mViewPager;
    private HomePagerAdapter mTopAdapter;
    private int position;
    private ArrayList<Mb_SlidersBean> mTopData;
    private LinearLayout mIndicatorContainer;
    private ArrayList<ImageView> mTopIndicators;
    private int mDensityDpi;
    private Timer mTimer;
    private RecyclerView mClassifyRecyclerView;
    private ArrayList<HomeClassifyTitle> mClassifyData;
    private HomeClassifyAdapter mHomeClassifyAdapter;
    private boolean mClassifyComplete;
    private Gson mGson;

    private boolean mIsSwitch = true;
    private GridView mGvRecommend;
    private GridView mGvNew;
    private ArrayList<HomeRecommendData.DatasBean.GoodsListInfoBean.RecommendedGoodsListBean> mRecommendedList;
    private HomeRecommendAdapter mHomeRecommendAdapter;
    private ArrayList<HomeRecommendData.DatasBean.GoodsListInfoBean.NewGoodsListBean> mHomeNewList;
    private HomeNewAdapter mHomeNewAdapter;
    private boolean mRecommendComplete;
    private TextView mTvAllGoods;
    private ProgressActivity mProgressActivity;
    private NetworkStateService mNetworkStateService;
    private ImageView mIvSearch;
    private TextView mHome_location;

    // 广播接收者去接收 定位的消息
    private BroadcastReceiver mBroadcastReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String city = intent.getStringExtra("city");
            if (city != null) {
                mHome_location.setText(city);
            }
        }
    };
    private Drawable mErrorDrawable;

    public MarketHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market_home, container, false);


        initView(view);

        initData();
        setData();
        setListener();

        mProgressActivity.showLoading();

        getData();

        return view;
    }

    private void initView(View view) {

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.home_refresh_layout);
        mViewPager = (ViewPager)view.findViewById(R.id.home_pager);

        mHome_location = (TextView) view.findViewById(R.id.home_location);

        mIvSearch = (ImageView)view.findViewById(R.id.market_home_search);
        mClassifyRecyclerView = (RecyclerView)view.findViewById(R.id.home_classify);
        mIndicatorContainer = (LinearLayout)view.findViewById(R.id.home_dot_indicator_container);
        mTvAllGoods = (TextView)view.findViewById(R.id.market_home_all);
        // 设置进度
        mProgressActivity = (ProgressActivity)view.findViewById(R.id.market_home_progress);

        mGvRecommend = (GridView)view.findViewById(R.id.market_home_recommend);
        mGvNew = (GridView)view.findViewById(R.id.market_home_new);
    }


    private void initData() {

        mErrorDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.wifi_off);

        Intent intent = new Intent(getActivity().getApplicationContext(), NetworkStateService.class);
        getActivity().getApplicationContext().bindService(intent, new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                NetworkStateService.MyBinder binder = (NetworkStateService.MyBinder) iBinder;
                mNetworkStateService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, Service.BIND_AUTO_CREATE);

        mGson =  new Gson();

        // 获取屏幕的 dpi
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mDensityDpi = displayMetrics.densityDpi;


        mTopData = new ArrayList<>();

        mTopIndicators = new ArrayList<>();

        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff552d"));
        mSwipeRefreshLayout.setDistanceToTriggerSync(200);


        mClassifyData = new ArrayList<>();
        mHomeClassifyAdapter = new HomeClassifyAdapter(getActivity(),mClassifyData);
        // 推荐
        mRecommendedList = new ArrayList<>();
        mHomeRecommendAdapter = new HomeRecommendAdapter(getActivity(),mRecommendedList);

        mHomeNewList = new ArrayList<>();
        mHomeNewAdapter = new HomeNewAdapter(getActivity(),mHomeNewList);
    }

    private void setData() {

        // 开启轮播
        startPagerSwitch();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        mClassifyRecyclerView.setLayoutManager(gridLayoutManager);
        mClassifyRecyclerView.setAdapter(mHomeClassifyAdapter);

        mGvRecommend.setNumColumns(2);

        mGvRecommend.setAdapter(mHomeRecommendAdapter);
        Drawable drawable = getResources().getDrawable(R.drawable.selector_listview_item);
        mGvRecommend.setSelector(drawable);

        mGvNew.setNumColumns(2);
        mGvNew.setAdapter(mHomeNewAdapter);
        mGvNew.setSelector(drawable);
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsSwitch = false ;   // 停止轮播
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsSwitch = true ;   // 开启轮播
        IntentFilter intentFilter = new IntentFilter("city");
        getActivity().registerReceiver(mBroadcastReceive,intentFilter);
    }

    private void startPagerSwitch(){
        mTimer = new Timer();
        mTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        pagerSwitchHandler.sendEmptyMessage(0);
                    }
                },
                5000, 4000);
    }
    // 设置handler去改变ViewPager
    private Handler pagerSwitchHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(mIsSwitch){
                int index = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(index+1);
            }
        }
    };

    // 设置监听器
    private void setListener() {
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showShop(getActivity(), "", "", "126", "");
            }
        });
        mViewPager.addOnPageChangeListener(this);

        mHomeClassifyAdapter.setClassifyClickCallback(this);
        // 设置下拉刷新的监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                RefreshData();
            }
        });

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        mSwipeRefreshLayout.setEnabled(false);
                        break;
                    // 鎶捣鍚庣户缁疆锟?
                    case MotionEvent.ACTION_UP:
                        // 杩涜椤甸潰鍒囨崲
                        mIsSwitch = true;
                        mSwipeRefreshLayout.setEnabled(true);
                        break;

                    case MotionEvent.ACTION_CANCEL:

                        break;
                }
                return false;
            }
        });
        // 设置全部商品的点击监听
        mTvAllGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showShop(getActivity(), "", "", "126", "");
            }
        });

        mGvRecommend.setOnItemClickListener(this);
        mGvNew.setOnItemClickListener(this);
    }

    private void RefreshData() {

        mRecommendComplete = false;

        getData();
    }

    private void getData() {

        // 得到推荐的数据
        getRecommendData();

    }

    private void getRecommendData() {

        RequestParams requestParams = new RequestParams();
        HttpUtil.get(HttpUtil.URL_MARKET_HOME, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {

                    String response = new String(responseBody);
                    Log.d("response",response);
                    HomeRecommendData homeRecommendData = mGson.fromJson(response, HomeRecommendData.class);
                    HomeRecommendData.DatasBean datas = homeRecommendData.getDatas();
                    HomeRecommendData.DatasBean.GoodsListInfoBean goods_list_info = datas.getGoods_list_info();
                    // 得到商品推荐的 数据
                    List<HomeRecommendData.DatasBean.GoodsListInfoBean.RecommendedGoodsListBean> recommended_goods_list = goods_list_info.getRecommended_goods_list();
                    if ( recommended_goods_list!= null) {
                        mRecommendedList.clear();
                        mRecommendedList.addAll(recommended_goods_list);
                    }
                        mHomeRecommendAdapter.notifyDataSetChanged();

                    // 得到新品 的数据
                    List<HomeRecommendData.DatasBean.GoodsListInfoBean.NewGoodsListBean> new_goods_list = goods_list_info.getNew_goods_list();
                    if (new_goods_list != null) {
                        mHomeNewList.clear();
                        mHomeNewList.addAll(new_goods_list);
                    }
                        mHomeNewAdapter.notifyDataSetChanged();

                        // gson解析有序
                        JsonObject jsonObject = new JsonParser().parse(response)
                                .getAsJsonObject();
                        JsonObject data = jsonObject.getAsJsonObject("datas");
                        JsonObject goodslist_info = data
                                .getAsJsonObject("goods_list_info");
                        JsonObject goodsstore = goodslist_info
                                .getAsJsonObject("goods_store");

                        if (goodsstore.get("mb_sliders").isJsonObject()) {
                            mTopData.clear();
                            mViewPager.setVisibility(View.VISIBLE);
                            JsonObject mb_sliders = goodsstore
                                    .getAsJsonObject("mb_sliders");
                            for (Map.Entry<String, JsonElement> entry : mb_sliders
                                    .entrySet()) {
                                Mb_SlidersBean bean = new Mb_SlidersBean();
                                bean = mGson.fromJson(entry.getValue(),
                                        Mb_SlidersBean.class);
                                mTopData.add(bean);
                            }

                            mTopAdapter = new HomePagerAdapter(getActivity(), mTopData, mViewPager);
                            mViewPager.setAdapter(mTopAdapter);

                            if (mTopAdapter != null) {

                                mTopAdapter.setOnBannerImageClickListener(new HomePagerAdapter.onBannerImageClickListener() {
                                    @Override
                                    public void onBannerImageClick(int i) {
                                        // 鎸変笅   鍋滄杞挱
                                        if(i==0){
                                            mIsSwitch = false;
                                        }else if (i==1){   // 鎶捣  缁х画杞挱
                                            mIsSwitch = true;;
                                        }
                                    }
                                });
                            }
                            initIndicatorsDot();

                            mViewPager.setCurrentItem(1,false);
                        }else {
                            mViewPager.setVisibility(View.GONE);
                        }
                    JsonArray j = goodslist_info.getAsJsonArray("top_goods_class_list");

                    String type = mGson.toJson(j);
                    List<HomeClassifyTitle> homeClassifyTitles = mGson.fromJson(type, new TypeToken<List<HomeClassifyTitle>>(){}.getType());

                    if (homeClassifyTitles != null) {
                        mClassifyData.clear();
                        mClassifyData.addAll(homeClassifyTitles);
                    }

                    mHomeClassifyAdapter.notifyDataSetChanged();

                    // 得到中间分类的数据
                    mRecommendComplete = true;
                    // 展示数据
                    showContent();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                mRecommendComplete=true;
                    Boolean isNet = mNetworkStateService.getIsNet();
                    if(!isNet){
                        //  设置错误的数据
                        setOnFailure();
                    }else {
                        // 刷新数据
                        RefreshData();
                    }
            }
            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }

    private void showContent(){
        if (mRecommendComplete){

            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if(mProgressActivity.isLoading()){
                mProgressActivity.showContent();
            }
        }
    }
    // 当加载错误时显示错误的信息
    private void setOnFailure() {

        mProgressActivity.showError(mErrorDrawable,"网络开了小差",
                "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试","重新连接",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mProgressActivity.showLoading();
                        mRecommendComplete = false;
                        getData();
                    }
                });
    }

    // 初始化指示器小圆点
    private void initIndicatorsDot() {

        if(mTopData.size()>1) {
            // 当小圆点没有的时候才往里面加
            if(mTopIndicators.size() ==0){
                int dp = mDensityDpi/160;
                for (int i = 0; i < mTopData.size(); i++) {
                    ImageView iv = new ImageView(getActivity());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8*dp, 8*dp);
                    params.setMargins(0,0,8*dp,8*dp);
                    iv.setLayoutParams(params);
                    mTopIndicators.add(iv);
                    mIndicatorContainer.addView(iv);
                    if (i==0){
                        iv.setImageResource(R.mipmap.dot_read);
                    }else {
                        iv.setImageResource(R.mipmap.dot_gary);
                    }
                }
            }
        }else {
            //
            mTopIndicators.clear();
            mIndicatorContainer.removeAllViews();
        }
    }

    @Override
    public void onDestroy() {

        if (mBroadcastReceive != null) {
            getActivity().unregisterReceiver(mBroadcastReceive);
        }
        if (pagerSwitchHandler != null) {
            pagerSwitchHandler.removeCallbacksAndMessages(null);
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    // 当ViewPager 滑动完以后调用
    @Override
    public void onPageSelected(int position) {

        if(mTopIndicators.size()>1){
            for (int i = 0; i < mTopIndicators.size(); i++) {
                mTopIndicators.get(i).setImageResource(R.mipmap.dot_gary);
            }
            if(position==mTopIndicators.size()+1){
                mTopIndicators.get(0).setImageResource(R.mipmap.dot_read);;
            }else if(position==0){
                mTopIndicators.get(mTopIndicators.size()-1).setImageResource(R.mipmap.dot_read);
            }else {
                mTopIndicators.get(position-1).setImageResource(R.mipmap.dot_read);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    @Override
    public void onClassifyClick(int position) {

        HomeClassifyTitle homeClassifyTitle = mClassifyData.get(position);
        if (homeClassifyTitle != null) {
            // 根据Id 去跳转到相应的
            String stc_id = homeClassifyTitle.getStc_id();
            String store_id = homeClassifyTitle.getStore_id();
            UIHelper.showMarketGoodList(getActivity(),store_id,stc_id,"");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()){
            case R.id.market_home_recommend:
                HomeRecommendData.DatasBean.GoodsListInfoBean.RecommendedGoodsListBean recommendedGoodsListBean = mRecommendedList.get(i);
                if (recommendedGoodsListBean != null) {
                    UIHelper.showGoods_Detaill(getActivity(), recommendedGoodsListBean.getGoods_id());
                }
                break;
            case R.id.market_home_new:
                HomeRecommendData.DatasBean.GoodsListInfoBean.NewGoodsListBean newGoodsListBean = mHomeNewList.get(i);
                if (newGoodsListBean != null) {
                    UIHelper.showGoods_Detaill(getActivity(), newGoodsListBean.getGoods_id());
                }
                break;
        }
    }
}
