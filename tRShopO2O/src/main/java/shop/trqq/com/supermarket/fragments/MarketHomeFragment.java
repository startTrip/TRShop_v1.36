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
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import shop.trqq.com.AppContext;
import shop.trqq.com.NetworkStateService;
import shop.trqq.com.R;
import shop.trqq.com.supermarket.MarketMainActivity;
import shop.trqq.com.supermarket.adapters.HomeClassifyAdapter;
import shop.trqq.com.supermarket.adapters.HomeNewAdapter;
import shop.trqq.com.supermarket.adapters.HomePagerAdapter;
import shop.trqq.com.supermarket.adapters.HomeRecommendAdapter;
import shop.trqq.com.supermarket.bean.HomeClassifyTitle;
import shop.trqq.com.supermarket.bean.HomeRecommendData;
import shop.trqq.com.supermarket.bean.HomeTopImage;
import shop.trqq.com.supermarket.config.NetConfig;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.YkLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketHomeFragment extends Fragment implements ViewPager.OnPageChangeListener, HomeClassifyAdapter.ClassifyClickCallback, AdapterView.OnItemClickListener {

    private static final String TAG = MarketHomeFragment.class.getSimpleName();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewPager mViewPager;
    private HomePagerAdapter mTopAdapter;
    private int position;
    private ArrayList<HomeTopImage.DataBean> mTopData;
    private LinearLayout mIndicatorContainer;
    private ArrayList<ImageView> mTopIndicators;
    private int mDensityDpi;
    private Timer mTimer;
    private RecyclerView mClassifyRecyclerView;
    private ArrayList<HomeClassifyTitle> mClassifyData;
    private HomeClassifyAdapter mHomeClassifyAdapter;
    private boolean mClassifyComplete;
    private boolean mTopComplete;
    private Gson mGson;
    private TimerTask mTask;
    // 默认轮播
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

    // �㲥������ȥ���� ��λ����Ϣ
    private BroadcastReceiver mBroadcastReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String city = intent.getStringExtra("city");
            Log.d(TAG, "onReceive: "+city);
            if (city != null) {
                mHome_location.setText(city);
            }
        }
    };

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
        // 显示进度
        mProgressActivity = (ProgressActivity)view.findViewById(R.id.market_home_progress);

        mGvRecommend = (GridView)view.findViewById(R.id.market_home_recommend);
        mGvNew = (GridView)view.findViewById(R.id.market_home_new);
    }


    private void initData() {
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

        // 得到手机的Dpi
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mDensityDpi = displayMetrics.densityDpi;


        mTopData = new ArrayList<>();

        mTopIndicators = new ArrayList<>();

        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff552d"));
        mSwipeRefreshLayout.setDistanceToTriggerSync(100);

        mClassifyData = new ArrayList<>();
        mHomeClassifyAdapter = new HomeClassifyAdapter(getActivity(),mClassifyData);
        // 下方推荐的数�?
        mRecommendedList = new ArrayList<>();
        mHomeRecommendAdapter = new HomeRecommendAdapter(getActivity(),mRecommendedList);

        mHomeNewList = new ArrayList<>();
        mHomeNewAdapter = new HomeNewAdapter(getActivity(),mHomeNewList);

    }

    private void setData() {

        startPagerSwitch();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        mClassifyRecyclerView.setLayoutManager(gridLayoutManager);
        mClassifyRecyclerView.setAdapter(mHomeClassifyAdapter);

        mGvRecommend.setNumColumns(2);
//        mGvRecommend.setSelector(getResources().getDrawable(R.drawable.selector_tab_background));
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
        mIsSwitch = true ;   // �?��轮播
        IntentFilter intentFilter = new IntentFilter("city");
        getActivity().registerReceiver(mBroadcastReceive,intentFilter);
    }

    // 5秒以后启动，�?3 秒切换一�?
    private void startPagerSwitch(){
        mTimer = new Timer();
        mTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        pagerSwitchHandler.sendEmptyMessage(0);
                    }
                },
                4000, 4000);
    }
    // 自定义Handler,处理定时器的页面切换
    private Handler pagerSwitchHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(mIsSwitch){
                int index = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(index+1);
            }
        }
    };

    // 设置监听�?
    private void setListener() {
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showShop(getActivity(), "", "", "126", "");
            }
        });
        mViewPager.addOnPageChangeListener(this);

        mHomeClassifyAdapter.setClassifyClickCallback(this);
        // 下拉刷新 监听事件
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // ˢ�����?
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
                    // 抬起后继续轮�?
                    case MotionEvent.ACTION_UP:
                        // 进行页面切换
                        mIsSwitch = true;
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        mSwipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
        // 点击 加载全部商品
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
        mTopComplete = false;
        mClassifyComplete = false;
        mRecommendComplete = false;
        // TODO : ����ʧ���Ժ� ���Ƿ�����ݻ�û�м������������?
        getData();
    }

    private void getData() {

        YkLog.i("onfailure1", AppContext.getInstance().getNetworkType()+"");
        // 得到头部轮播图的数据
        getTopData();
        // 得到中间分类的数�?
        getClassifyTitle();
        // 得到下方推荐的数�?
        getRecommendData();

    }

    private void getRecommendData() {

        RequestParams requestParams = new RequestParams();
        requestParams.add("store_id","126");
        HttpUtil.get(HttpUtil.URL_STORE_DETAIL, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (responseBody != null) {

                    String response = new String(responseBody);
                    HomeRecommendData homeRecommendData = mGson.fromJson(response, HomeRecommendData.class);
                    HomeRecommendData.DatasBean datas = homeRecommendData.getDatas();
                    HomeRecommendData.DatasBean.GoodsListInfoBean goods_list_info = datas.getGoods_list_info();
                    // 得到 热门推荐数据
                    List<HomeRecommendData.DatasBean.GoodsListInfoBean.RecommendedGoodsListBean> recommended_goods_list = goods_list_info.getRecommended_goods_list();
                    if ( recommended_goods_list!= null) {
                        mRecommendedList.clear();
                        mRecommendedList.addAll(recommended_goods_list);
                    }
                        mHomeRecommendAdapter.notifyDataSetChanged();

                    // 得到新品推荐数据
                    List<HomeRecommendData.DatasBean.GoodsListInfoBean.NewGoodsListBean> new_goods_list = goods_list_info.getNew_goods_list();
                    if (new_goods_list != null) {
                        mHomeNewList.clear();
                        mHomeNewList.addAll(new_goods_list);
                    }
                        mHomeNewAdapter.notifyDataSetChanged();

                    mRecommendComplete = true;
                    // չʾ����
                    showContent();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                mRecommendComplete=true;
                    Boolean isNet = mNetworkStateService.getIsNet();
                    if(!isNet){
                        //  ����ʧ�ܵ����?
                        setOnFailure();
                    }else {
                        // ˢ�����?
                        RefreshData();
                    }
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }

    // 得到中间分类数据
    private void getClassifyTitle() {

        RequestParams requestParams = new RequestParams();
        requestParams.add("agent_id","101");

        HttpUtil.post(NetConfig.BASEHOMECLASSIFY, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String jsonString = new String(responseBody);

                    JSONObject jsonObject = new JSONObject(jsonString);

                    JSONObject infor = jsonObject.optJSONObject("infor");
                    String type = infor.optString("type");

                    List<HomeClassifyTitle> homeClassifyTitles = mGson.fromJson(type, new TypeToken<List<HomeClassifyTitle>>(){}.getType());

                    if (homeClassifyTitles != null) {
                        mClassifyData.clear();
                        mClassifyData.addAll(homeClassifyTitles);
                    }

                    mHomeClassifyAdapter.notifyDataSetChanged();

                    mClassifyComplete = true;
                    // չʾ����
                    showContent();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                mClassifyComplete = true;
                // ��һ������ʧ��
                    Boolean isNet = mNetworkStateService.getIsNet();
                    if(!isNet){
                        //  ����ʧ�ܵ����?
                        setOnFailure();
                    }else {
                        // ˢ�����?
                        RefreshData();
                    }

            }
            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


    // 得到头部�?轮播图数�?
    private void getTopData() {

        HttpUtil.get(NetConfig.DISCOVERBANNER, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String response = new String (responseBody);

                HomeTopImage homeTopImage = mGson.fromJson(response, HomeTopImage.class);

                List<HomeTopImage.DataBean> data = homeTopImage.getData();

                // 判断是不是为null
                if (data != null) {
                    mTopData.clear();
                    mTopData.addAll(data);
                }
                mTopAdapter = new HomePagerAdapter(getActivity(), mTopData, mViewPager);
                mViewPager.setAdapter(mTopAdapter);

                if (mTopAdapter != null) {

                    mTopAdapter.setOnBannerImageClickListener(new HomePagerAdapter.onBannerImageClickListener() {
                        @Override
                        public void onBannerImageClick(int i) {
                            // 按下   停止轮播
                            if(i==0){
                                mIsSwitch = false;
                            }else if (i==1){   // 抬起  继续轮播
                                mIsSwitch = true;;
                            }
                        }
                    });
                }
                initIndicatorsDot();

                mViewPager.setCurrentItem(1,false);

                // ��������������
                mTopComplete = true;
                showContent();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                mTopComplete = true;
                    Boolean isNet = mNetworkStateService.getIsNet();

                    YkLog.i("onfailure", AppContext.getInstance().getNetworkType()+"");
                    if(!isNet){
                        setOnFailure();
                    }else {
                        // ˢ�����?
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
        if (mTopComplete&&mClassifyComplete&&mRecommendComplete){

            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if(mProgressActivity.isLoading()){
                mProgressActivity.showContent();
            }
        }
    }
    // ����ʧ��ʱ�����?
    private void setOnFailure() {

        Drawable errorDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.wifi_off);
        mProgressActivity.showError(errorDrawable,"���翪��С��",
                "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����","��������",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mProgressActivity.showLoading();
                        mTopComplete = false;
                        mClassifyComplete = false;
                        mRecommendComplete = false;
                        getData();
                    }
                });
    }

    // 初始化小圆点
    private void initIndicatorsDot() {
        if (mTopIndicators.size()==0) {

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceive != null) {
            getActivity().unregisterReceiver(mBroadcastReceive);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

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

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onClassifyClick(int position) {

        if(position==0||position==1||position==2||position==5||position==6||position==7||position==8){
//            String id = mClassifyData.get(position).getId();
//            Intent intent = new Intent(getActivity(), MarketClassifyDetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("id",id);
//            bundle.putString("source","home");
//            intent.putExtras(bundle);
//            getActivity().startActivity(intent);
            UIHelper.showShop(getActivity(), "", "", "126", "");
        }else if(position==9){

            ((MarketMainActivity)getActivity()).showClassifyFragment();

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
