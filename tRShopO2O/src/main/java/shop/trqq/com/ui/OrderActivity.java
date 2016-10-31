package shop.trqq.com.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.pay.AlipayHelper;
import com.alipay.pay.PayResult;
import com.alipay.sdk.app.PayTask;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.OrderAdapter;
import shop.trqq.com.adapter.Vr_Order_listAdapter;
import shop.trqq.com.bean.OrderGroupHomeListBean;
import shop.trqq.com.bean.Vr_Order_listBean;
import shop.trqq.com.event.EventUpdateOrder;
import shop.trqq.com.supermarket.activitys.ConfirmPayOrderActivity;
import shop.trqq.com.supermarket.view.MyPopupWindow;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 订单界面
 */
public class OrderActivity extends BaseActivity implements OrderAdapter.onClickAliPay {

    private static final int SDK_PAY_FLAG = 1;
    private Context mContext;
    private OrderAdapter orderAdapter;
    private Vr_Order_listAdapter mVr_Order_cellAdapter;
    private ProgressActivity progressActivity;
    private PullToRefreshListView mOrderPullToRefreshListView;
    private ArrayList<OrderGroupHomeListBean> orderGooupList;
    private ArrayList<Vr_Order_listBean> Vr_orderList;
    private Gson gson;
    private int curpageIndex = 1;
    private Boolean hasmore;
    private String filter;
    // 标题栏标题
    private TextView mHeadTitleTextView;
    // 加载更多，脚部布局
    private View footView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;

    private boolean isResume = true;
    private MyPopupWindow mMyPopup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mContext = this;
        gson = new Gson();
        EventBus.getDefault().register(this);
        orderGooupList = new ArrayList<OrderGroupHomeListBean>();
        Vr_orderList = new ArrayList<Vr_Order_listBean>();
        filter = getIntent().getStringExtra("filter");

        // loadOnlineInformationData("6");
        initTitleBarView();
        initViews();

        setListener();
    }

    private void setListener() {
        orderAdapter.setOnClickAliPay(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (isResume)
            loadOrderListDATA();
        isResume = true;
        super.onResume();
    }

    /**
     * 收到消息 进行相关处理（还未使用）
     *
     * @param event
     */
    public void onEventMainThread(EventUpdateOrder event) {
        YkLog.t("EventUpdateOrder", "onEventMainThread收到消息:" + event.getMsg());
        loadOrderListDATA();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText(filter + "订单");
    }

    private void initViews() {

        mMyPopup = MyPopupWindow.getInstance();

        mOrderPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.order_pullToRefreshListView);
        orderAdapter = new OrderAdapter(handler, mContext, filter);
        mVr_Order_cellAdapter = new Vr_Order_listAdapter(handler, mContext,
                filter);

        inflater = LayoutInflater.from(this);
        footView = inflater.inflate(R.layout.item_load_more, null);
        mLoadMoreButton = (Button) footView.findViewById(R.id.loadMore_button);
        progressActivity = (ProgressActivity) findViewById(R.id.order_progress);
        mOrderPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.order_pullToRefreshListView);
        mOrderPullToRefreshListView.getRefreshableView()
                .addFooterView(footView);
        mOrderPullToRefreshListView.setPullToRefreshOverScrollEnabled(false);
        mOrderPullToRefreshListView
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // TODO Auto-generated method stub

                    }

                });
        mOrderPullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {

                        loadOrderListDATA();
                    }
                });

        mOrderPullToRefreshListView
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
                    @Override
                    public void onLastItemVisible() {
                        if (isEnabledScrollLast) {
                            footView.setVisibility(View.VISIBLE);
                            loadMoreOrderListDATA();
                        }
                    }
                });

        progressActivity.showLoading();
    }

    private void loadOrderListDATA() {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        curpageIndex = 1;
        String uri;
        if (filter.equals("虚拟")) {
            uri = HttpUtil.URL_MEMBER_VR_ORDER + "&page=10&curpage="
                    + curpageIndex;
        } else {
            uri = HttpUtil.URL_ORDER_LIST + "&page=10&curpage=" + curpageIndex;
        }
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    YkLog.longe("order_list", jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    try {

                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                        }
                    } catch (Exception e) {
                        hasmore = true;
                        hasmore = new JSONObject(jsonString)
                                .optBoolean("hasmore");
                        if (filter.equals("虚拟")) {
                            String order_list = jsonObject
                                    .getString("order_list");
                            Vr_orderList = gson.fromJson(order_list,
                                    new TypeToken<List<Vr_Order_listBean>>() {
                                    }.getType());

                            mVr_Order_cellAdapter.setData(Vr_orderList);
                            mVr_Order_cellAdapter.notifyDataSetChanged();
                            mOrderPullToRefreshListView
                                    .setAdapter(mVr_Order_cellAdapter);
                            //虚拟订单为空
                            if (Vr_orderList.size() > 0) {
                                progressActivity.showContent();
                            } else {
                                Drawable emptyDrawable = getResources().getDrawable(
                                        R.drawable.ic_empty);
                                progressActivity
                                        .showEmpty(emptyDrawable, "您的订单是空的", "");
                            }
                        } else {

                            String group_list = jsonObject
                                    .getString("order_group_list");
                            YkLog.longe("group_list", jsonString);
                            orderGooupList = gson
                                    .fromJson(
                                            group_list,
                                            new TypeToken<List<OrderGroupHomeListBean>>() {
                                            }.getType());

                            orderAdapter.setData(orderGooupList);
                            orderAdapter.notifyDataSetChanged();
                            mOrderPullToRefreshListView
                                    .setAdapter(orderAdapter);
                            //实物订单为空
                            if (orderGooupList.size() > 0) {
                                progressActivity.showContent();
                            } else {
                                Drawable emptyDrawable = getResources().getDrawable(
                                        R.drawable.ic_empty);
                                progressActivity
                                        .showEmpty(emptyDrawable, "您的订单是空的", "");
                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                try {
                    ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
                    if (orderGooupList.size() == 0) {
                        Drawable errorDrawable = getResources().getDrawable(
                                R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable, "网络开了小差",
                                "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试", "重新连接",
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
                mOrderPullToRefreshListView.onRefreshComplete();
            }
        });
    }

    OnClickListener errorClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            progressActivity.showLoading();
            orderGooupList.clear();
            loadOrderListDATA();
        }
    };

    private void loadMoreOrderListDATA() {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        // requestParams.add("page","2");
        curpageIndex++;
        String uri;
        if (filter.equals("虚拟")) {
            uri = HttpUtil.URL_MEMBER_VR_ORDER + "&page=10&curpage="
                    + curpageIndex;
        } else {
            uri = HttpUtil.URL_ORDER_LIST + "&page=10&curpage=" + curpageIndex;
        }
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    try {

                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                        }
                    } catch (Exception e) {
                        if (hasmore) {
                            hasmore = new JSONObject(jsonString)
                                    .getBoolean("hasmore");
                            if (filter.equals("虚拟")) {
                                String order_list = jsonObject
                                        .getString("order_list");
                                YkLog.longe("order_list", jsonString);
                                List<Vr_Order_listBean> Vr_tmpList = gson
                                        .fromJson(
                                                order_list,
                                                new TypeToken<List<Vr_Order_listBean>>() {
                                                }.getType());
                                Vr_orderList.addAll(Vr_tmpList);
                                mVr_Order_cellAdapter.setData(Vr_orderList);
                                mVr_Order_cellAdapter.notifyDataSetChanged();
                            } else {
                                String group_list = jsonObject
                                        .getString("order_group_list");
                                // System.out.println(group_list);
                                YkLog.longe("group_list", jsonString);
                                List<OrderGroupHomeListBean> tmpList = gson
                                        .fromJson(
                                                group_list,
                                                new TypeToken<List<OrderGroupHomeListBean>>() {
                                                }.getType());
                                orderGooupList.addAll(tmpList);
                                orderAdapter.setData(orderGooupList);
                                orderAdapter.notifyDataSetChanged();
                            }
                        } else {
                            ToastUtils.showMessage(mContext, "已经没有数据了");

                            footView.setVisibility(View.GONE);
                            isEnabledScrollLast = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showMessage(mContext,
                            R.string.get_informationData_failure);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
                mLoadMoreButton.setVisibility(View.VISIBLE);
                isEnabledScrollLast = false;
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //  取消订单或者确认收货以后可以 重新下载数据
                case 1:
                    loadOrderListDATA();

                    // 查看物流状态跳转到相应的 Activity
                case 2://跳转到新Activity返回不刷新
                    isResume = false;
                default:
                    break;
            }
        }
    };

    // 点击支付按钮回调方法
    @Override
    public void clickAlipay(String pay_sn, String pay_amount) {

        Intent intent = new Intent(mContext,ConfirmPayOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("state","1");
        bundle.putString("checkMoney",pay_amount);
        bundle.putString("pay_sn",pay_sn);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    // 支付宝付款
    private void zhifuboPay(String pay_sn) {

        if (TextUtils.isEmpty(AlipayHelper.PARTNER) || TextUtils.isEmpty(AlipayHelper.RSA_PRIVATE) || TextUtils.isEmpty(AlipayHelper.SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        // 订单  ！！！！ 需要设置为自己的信息，在下面
        String orderInfo =
                AlipayHelper.getOrderInfo
                        ("测试的商品", // 商品名称
                                "该测试商品的详细描述", // 商品描述
                                "0.01",pay_sn);// 商品价格
        Log.d("orderInfo",orderInfo);
        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = AlipayHelper.sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         * 最终，支付宝使用 payInfo 来进行支付的操作
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + AlipayHelper.getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                //  支付宝内部的内对象，用于发送请求
                PayTask alipay = new PayTask(OrderActivity.this);
                // 调用支付接口，获取支付结果
                // pay() 这个方法实际发起支付
                // 返回值，包含支付成功失败的信息。
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    // 支付后的消息
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showPayResultSuccess();
                    } else {
                        // 判断resultStastus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    /**
     * 支付成功的弹框
     */
    private void showPayResultSuccess() {

        View viewPop = LayoutInflater.from(mContext).inflate(R.layout.popup_pay_succes, null);
        mMyPopup.showPopupWindowFronCenter(this, viewPop);
        viewPop.findViewById(R.id.tv_pay_know).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        mMyPopup.cancel();
                    }
                });
    }
}
