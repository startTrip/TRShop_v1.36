package shop.trqq.com.ui.Fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.bean.CommentsBean;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.widget.PagerSlidingTabStrip;

/**
 * 商品评论
 */
public class CommentsFragment extends BaseLazyFragment {
    private static final String TAG = "CommentsFragment";

    // 全局Context
    private Context appContext;
    // Gson工具
    private Gson gson;
    /*
	 * private LoopViewPager loopViewPager; private ArrayList<String> imageUris;
	 */

    // 资讯加载错误图标
    private ProgressActivity progressActivity;

    private TextView good_percent_big;
    private TextView good_percent_text;
    private ProgressBar good_percent;
    private TextView normal_percent_text;
    private ProgressBar normal_percent;
    private TextView bad_percent_text;
    private ProgressBar bad_percent;

    // 加载更多，脚部布局
    private View footView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;
    /**
     * 要显示数据实体类.
     */

    private List<CommentsBean> informationList;

    private int informationListIndex = 0;
    private int curpageIndex = 1;

    private View rootView;// 缓存Fragment view
    private String goods_id;
    private String show_pa;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private CommentsListFragment[] mCommentsListFragment = new CommentsListFragment[3];
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private String[] titles = {"好评", "中评", "差评"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_favorites);
        rootView = inflater.inflate(R.layout.fragment_comments, container,
                false);
        appContext = getActivity();
        gson = new Gson();
        informationList = new ArrayList<CommentsBean>();
        Bundle bundle = getArguments();
        goods_id = bundle.getString("goods_id");
        initViews();

        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {

    }

    private void initViews() {
        inflater = LayoutInflater.from(appContext);
        footView = inflater.inflate(R.layout.item_load_more, null);
        mLoadMoreButton = (Button) footView.findViewById(R.id.loadMore_button);
        progressActivity = (ProgressActivity) rootView
                .findViewById(R.id.Comments_list_progress);
        good_percent_big = (TextView) rootView
                .findViewById(R.id.good_percent_big);
        good_percent_text = (TextView) rootView
                .findViewById(R.id.good_percent_text);
        good_percent = (ProgressBar) rootView.findViewById(R.id.good_percent);
        normal_percent_text = (TextView) rootView
                .findViewById(R.id.normal_percent_text);
        normal_percent = (ProgressBar) rootView
                .findViewById(R.id.normal_percent);
        bad_percent_text = (TextView) rootView
                .findViewById(R.id.bad_percent_text);
        bad_percent = (ProgressBar) rootView.findViewById(R.id.bad_percent);
        progressActivity.showLoading();
    }

    /**
     * 加载评论列表
     *
     */
    private void loadOnlineInformationData() {
        RequestParams requestParams = new RequestParams();
        requestParams.add("goods_id", goods_id);
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
                            //ToastUtils.showMessage(appContext, errStr);
                            if (informationList.size() == 0) {
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
                        int good_percentInt = Integer
                                .parseInt(goods_evaluate_info
                                        .getString("good_percent"));
                        int normal_percentInt = Integer
                                .parseInt(goods_evaluate_info
                                        .getString("normal_percent"));
                        int bad_percentInt = Integer
                                .parseInt(goods_evaluate_info
                                        .getString("bad_percent"));
                        good_percent.setProgress(good_percentInt);
                        normal_percent.setProgress(normal_percentInt);
                        bad_percent.setProgress(bad_percentInt);
                        good_percent_big.setText("" + good_percentInt);
                        good_percent_text.setText("好评（" + good_percentInt
                                + "%）");
                        normal_percent_text.setText("中评（" + normal_percentInt
                                + "%）");
                        bad_percent_text.setText("差评（" + bad_percentInt + "%）");
                        titles[0] = "好评("
                                + goods_evaluate_info.getString("good") + ")";
                        titles[1] = "中评("
                                + goods_evaluate_info.getString("normal") + ")";
                        titles[2] = "差评("
                                + goods_evaluate_info.getString("bad") + ")";
                        String comments = jsonObjects.getString("comments");
                        informationList = gson.fromJson(comments,
                                new TypeToken<List<CommentsBean>>() {
                                }.getType());
                        // AppConfig.setSharedPreferences(appContext, configKey,
                        // goods_list);
                        informationListIndex += informationList.size();
                        // 初始化评论Fragmentlist
                        pager = (ViewPager) rootView.findViewById(R.id.pager);
                        tabs = (PagerSlidingTabStrip) rootView
                                .findViewById(R.id.tabs);
                        pager.setAdapter(new MyAdapter(
                                getChildFragmentManager(), titles));
                        tabs.setViewPager(pager);
                        pager.setOffscreenPageLimit(3);
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
                // mHomePullToRefreshListView.onRefreshComplete();
                mHasLoadedOnce = true;
            }

        });
    }

    @Override
    protected void lazyLoad() {
        // TODO Auto-generated method stub
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        loadOnlineInformationData();
    }

    public class MyAdapter extends FragmentPagerAdapter {
        String[] _titles;

        public MyAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            _titles = titles;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            return super.instantiateItem(container, position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return _titles[position];
        }

        @Override
        public int getCount() {
            return _titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            if (mCommentsListFragment[position] == null) {
                mCommentsListFragment[position] = new CommentsListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", String.valueOf(position + 1));
                bundle.putString("goods_id", goods_id);
                mCommentsListFragment[position].setArguments(bundle);
            }
            return mCommentsListFragment[position];
        }
    }
}
