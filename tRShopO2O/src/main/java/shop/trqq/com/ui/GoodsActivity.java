package shop.trqq.com.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import shop.trqq.com.R;
import shop.trqq.com.adapter.recycler.RecViewGoodsSpAdapter;
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Base.PauseHandler;
import shop.trqq.com.ui.Fragment.Fragment_Goods;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.YkLog;

/*
 * 商品列表界面*/
public class GoodsActivity extends BaseFragmentActivity implements OnClickListener {
    private static final String TAG = "GoodsActivity";

    // 全局Context
    private Context appContext;
    // Gson工具
    private Gson gson;
    /*
     * private LoopViewPager loopViewPager; private ArrayList<String> imageUris;
     */
    // 标题栏标题
    private TextView mHeadTitleTextView;
    //private ListViewGoodsAdapter listViewGoodAdapter;
    private RecViewGoodsSpAdapter mRecViewCartAdapter;
    // 加载进度
    private ProgressActivity progressActivity;
    private PullToRefreshRecyclerView mHomePullToRefreshListView;
    // 排序栏
    private TextView shopNew;
    private TextView shopSales;
    private TextView shopPrice;
    private TextView shopPopularity;

    private LinearLayout store_search_layout;
    private EditText store_search_edit;
    private TextView store_search_button;
    //	@Bind(R.id.float_imageButton)
//	ImageView Go_UpButton;
    // 加载更多，脚部布局
    private View footView;
    private LinearLayout topShopView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;
    /**
     * 要显示数据实体类.
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

    /**
     * Used for "what" parameter to handler messages
     */
    final static int MSG_WHAT = ('F' << 16) + ('T' << 8) + 'A';
    final static int MSG_SHOW_DIALOG = 1;
    private boolean hasSelected = true;
    private ImageView mImageBack;
    private String mStc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        //ButterKnife绑定
        ButterKnife.bind(this);
        appContext = this;
        gson = new Gson();
        Intent intent = getIntent();
        try {
            keyword = intent.getStringExtra("keyword");
            // 商城的分类id
            gc_id = intent.getStringExtra("gc_id");
            store_id = intent.getStringExtra("store_id");
            brand = intent.getStringExtra("brand");
            // 店铺的分类 id
            mStc_id = intent.getStringExtra("stc_id");
            Log.d(TAG, "keyword"+keyword+"|gc_id"+gc_id+"|store_id"+store_id+"|brand"+brand);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //店铺销量的key不同
        if (store_id.length() == 0 || store_id == null) {
            key = "1";
        } else {
            key = "3";
        }
        Log.d(TAG, "key"+key);
        informationList = new ArrayList<GoodsBean>();
        initTitleBarView();
        initViews();
        setListener();
    }

    private void setListener() {
        mImageBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化标题栏视图
     */
    @SuppressLint("NewApi")
    private void initTitleBarView() {
        YkLog.i(TAG, "初始化标题栏视图");
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mImageBack = (ImageView) findViewById(R.id.title_back);
        mImageBack.setVisibility(View.VISIBLE);
        if ( keyword == null||keyword.length()== 0 ) {
            mHeadTitleTextView.setText("商品列表");
        } else if (keyword != "" && keyword != null) {
            mHeadTitleTextView.setText(keyword);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        handler.setActivity(this);
        handler.resume();
    }

    @Override
    public void onPause() {
        super.onPause();

        handler.pause();
    }

    public void onDestroy() {
        super.onDestroy();
        handler.setActivity(null);
    }

    public ConcreteTestHandler handler = new ConcreteTestHandler();
    int value = 1;

    private void fragmentTransaction() {
        handler.sendMessageDelayed(handler.obtainMessage(MSG_WHAT, MSG_SHOW_DIALOG, value++),
                0);
//	FragmentManager fragmentManager = getSupportFragmentManager();
//	FragmentTransaction fragmentTransaction = fragmentManager
//			.beginTransaction();
//	Fragment_Goods fragment = new Fragment_Goods();
//	Bundle bundle = new Bundle();
//	bundle.putString("keyword", keyword);
//	bundle.putString("gc_id", gc_id);
//	bundle.putString("store_id", store_id);
//	bundle.putString("brand", brand);
//	bundle.putString("key", key);
//	bundle.putString("order", order);
//	fragment.setArguments(bundle);
//	// 加到Activity中
//	fragmentTransaction.replace(R.id.fragment, fragment);
//	// 加到后台堆栈中，有下一句代码的话，点击返回按钮是退到Activity界面，没有的话，直接退出Activity
//	// 后面的参数是此Fragment的Tag。相当于id
//	// fragmentTransaction.addToBackStack("fragmentcart");
//	// 避免在异步回调函数中提交transactions
//	fragmentTransaction.commit();
    }

    private void initViews() {
        inflater = LayoutInflater.from(this);
        store_search_layout = (LinearLayout) findViewById(R.id.store_search_layout);
        store_search_edit = (EditText) findViewById(R.id.store_search_edit);
        store_search_button = (TextView) findViewById(R.id.store_search_button);
        if (store_id.length() != 0 && store_id != null) {

            store_search_layout.setVisibility(View.VISIBLE);
            // 点击搜索按钮 加入关键词搜索；
            store_search_button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    keyword = store_search_edit.getText().toString();
                    //鐐规悳绱㈢殑鏃跺?鍙互鎼滅储搴楅摵涓殑鎵?湁鐨勫晢鍝?
                    mStc_id = "";
                    fragmentTransaction();
                }
            });
        }else {
            store_search_layout.setVisibility(View.GONE);
        }
        topShopView = (LinearLayout) inflater.inflate(R.layout.item_shop_top,
                null);
        shopNew = (TextView) findViewById(R.id.shop_new);
        shopSales = (TextView) findViewById(R.id.shop_sales);
        shopPrice = (TextView) findViewById(R.id.shop_price);
        shopPopularity = (TextView) findViewById(R.id.shop_popularity);

        shopSales.setEnabled(false);
        shopNew.setOnClickListener(this);
        shopSales.setOnClickListener(this);
        shopPrice.setOnClickListener(this);
        shopPopularity.setOnClickListener(this);
        fragmentTransaction();

    }

    private int prices = 0;
    private Drawable nav_up;

    /*
     * 改变价格图标
     */
    @SuppressWarnings("deprecation")
    private void changePricesPic() {
        switch (prices) {
            case 1:
                nav_up = getResources().getDrawable(
                        R.drawable.coupon_double_arrow_pressed1);
                break;
            case 2:
                nav_up = getResources().getDrawable(
                        R.drawable.coupon_double_arrow_pressed2);
                break;
            default:
                nav_up = getResources().getDrawable(
                        R.drawable.coupon_double_arrow_normal);
                break;
        }
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
                nav_up.getMinimumHeight());
        shopPrice.setCompoundDrawables(null, null, nav_up, null);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.shop_new:// 新品
                if (store_id.length() == 0 || store_id == null) {//全部商品
                    key = "4";
                } else {// 店铺商品key值不同
                    key = "1";
                }
                prices = 0;
                order = "1";
                shopSales.setEnabled(true);
                shopNew.setEnabled(false);
                shopPopularity.setEnabled(true);
                shopNew.setTextColor(getResources()
                        .getColor(R.color.red_text_color));
                shopSales.setTextColor(getResources().getColor(
                        R.color.black_text_color));
                shopPrice.setTextColor(getResources().getColor(
                        R.color.black_text_color));
                shopPopularity.setTextColor(getResources().getColor(
                        R.color.black_text_color));

                Log.d(TAG, "key"+key+"|prices"+prices+"|order"+order);
                break;
            case R.id.shop_sales:// 销量
                if (store_id.length() == 0 || store_id == null) {
                    key = "1";
                } else {
                    key = "3";
                }
                prices = 0;
                order = "0";               // 销量降序
                shopSales.setEnabled(false);
                shopNew.setEnabled(true);
                shopPopularity.setEnabled(true);
                shopSales.setTextColor(getResources().getColor(
                        R.color.red_text_color));
                shopNew.setTextColor(getResources().getColor(
                        R.color.black_text_color));
                shopPrice.setTextColor(getResources().getColor(
                        R.color.black_text_color));
                shopPopularity.setTextColor(getResources().getColor(
                        R.color.black_text_color));
                Log.d(TAG, "key"+ key+"|prices"+prices+"|order"+order);
                break;
            case R.id.shop_price:// 价格
                if (store_id.length() == 0 || store_id == null) {
                    key = "3";
                } else {
                    key = "2";
                }
                if (prices == 2) {
                    order = "0";
                    prices = 1;
                } else if (prices == 0 || prices == 1) {
                    prices = 2;
                    order = "1";
                }
                shopSales.setEnabled(true);
                shopNew.setEnabled(true);
                shopPopularity.setEnabled(true);
                shopPrice.setTextColor(getResources().getColor(
                        R.color.red_text_color));
                shopNew.setTextColor(getResources().getColor(
                        R.color.black_text_color));
                shopSales.setTextColor(getResources().getColor(
                        R.color.black_text_color));
                shopPopularity.setTextColor(getResources().getColor(
                        R.color.black_text_color));
                Log.d(TAG, "key"+key+"|prices"+prices+"|order"+order);
                break;
            case R.id.shop_popularity:// 人气

                if (store_id.length() == 0 || store_id == null) {
                    key = "2";
                } else {
                    key = "4";
                }
                order = "0";
                prices = 0;
                shopSales.setEnabled(true);
                shopNew.setEnabled(true);
                shopPopularity.setEnabled(false);
                shopPopularity.setTextColor(getResources().getColor(
                        R.color.red_text_color));
                shopNew.setTextColor(getResources().getColor(
                        R.color.black_text_color));
                shopSales.setTextColor(getResources().getColor(
                        R.color.black_text_color));
                shopPrice.setTextColor(getResources().getColor(
                        R.color.black_text_color));
                Log.d(TAG, "key"+key+"|prices"+prices+"|order"+order);
                break;

        }
        //取消请求，防止加载更多点击后崩溃
        HttpUtil.cancelRequests(appContext, true);
        YkLog.t("changePricesPic");
        //HttpUtil.cancelAllRequests(true);
        changePricesPic();
        fragmentTransaction();
    }

    /**
     * 加载网络数据
     */
    public void loadData() {
        mHomePullToRefreshListView.setRefreshing(false);
    }

    /**
     * Message Handler class that supports buffering up of messages when the
     * activity is paused i.e. in the background.
     */
    class ConcreteTestHandler extends PauseHandler {

        /**
         * Activity instance
         */
        protected FragmentActivity activity;

        /**
         * Set the activity associated with the handler
         *
         * @param activity the activity to set
         */
        final void setActivity(FragmentActivity activity) {
            this.activity = activity;
        }

        @Override
        final protected boolean storeMessage(Message message) {
            // All messages are stored by default
            return true;
        }

        ;

        @Override
        final protected void processMessage(Message msg) {

            final FragmentActivity activity = this.activity;
            if (activity != null) {
                switch (msg.what) {

                    case MSG_WHAT:
                        switch (msg.arg1) {
                            case MSG_SHOW_DIALOG:
                                final FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager
                                        .beginTransaction();
                                Fragment_Goods fragment = new Fragment_Goods();
                                Bundle bundle = new Bundle();
                                bundle.putString("key", key);
                                bundle.putString("order", order);
                                bundle.putString("keyword", keyword);
                                // 通过 stc_id 来判断是不是请求店铺分类
                                if(mStc_id==null){
                                    bundle.putString("gc_id", gc_id);
                                    bundle.putString("store_id", store_id);
                                    bundle.putString("brand", brand);
                                    Log.d("lujing","商城");
                                }else {
                                    bundle.putString("store_id",store_id);
                                    bundle.putString("stc_id",mStc_id);
                                    Log.d("lujing","超市分类id");
                                }
                                fragment.setArguments(bundle);
                                // 加到Activity中
                                fragmentTransaction.replace(R.id.fragment, fragment);
                                // 加到后台堆栈中，有下一句代码的话，点击返回按钮是退到Activity界面，没有的话，直接退出Activity
                                // 后面的参数是此Fragment的Tag。相当于id
                                // fragmentTransaction.addToBackStack("fragmentcart");
                                // 避免在异步回调函数中提交transactions
                                fragmentTransaction.commitAllowingStateLoss();
                                break;
                        }
                        break;
                }
            }
        }
    }

}
