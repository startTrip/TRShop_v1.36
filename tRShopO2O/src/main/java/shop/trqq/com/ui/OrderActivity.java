package shop.trqq.com.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
 * ��������
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
    // ����������
    private TextView mHeadTitleTextView;
    // ���ظ��࣬�Ų�����
    private View footView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;

    private boolean isResume = true;
    private MyPopupWindow mMyPopup;
    private ImageView mImageBack;

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

        mImageBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
     * �յ���Ϣ ������ش�����δʹ�ã�
     *
     * @param event
     */
    public void onEventMainThread(EventUpdateOrder event) {
        YkLog.t("EventUpdateOrder", "onEventMainThread�յ���Ϣ:" + event.getMsg());
        loadOrderListDATA();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {

        mImageBack = (ImageView) findViewById(R.id.title_back);
        mImageBack.setVisibility(View.VISIBLE);

        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText(filter + "����");
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
        if (filter.equals("����")) {
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
                        if (filter.equals("����")) {
                            String order_list = jsonObject
                                    .getString("order_list");
                            Vr_orderList = gson.fromJson(order_list,
                                    new TypeToken<List<Vr_Order_listBean>>() {
                                    }.getType());

                            mVr_Order_cellAdapter.setData(Vr_orderList);
                            mVr_Order_cellAdapter.notifyDataSetChanged();
                            mOrderPullToRefreshListView
                                    .setAdapter(mVr_Order_cellAdapter);
                            //���ⶩ��Ϊ��
                            if (Vr_orderList.size() > 0) {
                                progressActivity.showContent();
                            } else {
                                Drawable emptyDrawable = getResources().getDrawable(
                                        R.drawable.ic_empty);
                                progressActivity
                                        .showEmpty(emptyDrawable, "���Ķ����ǿյ�", "");
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
                            //ʵ�ﶩ��Ϊ��
                            if (orderGooupList.size() > 0) {
                                progressActivity.showContent();
                            } else {
                                Drawable emptyDrawable = getResources().getDrawable(
                                        R.drawable.ic_empty);
                                progressActivity
                                        .showEmpty(emptyDrawable, "���Ķ����ǿյ�", "");
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
                        progressActivity.showError(errorDrawable, "���翪��С��",
                                "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����", "��������",
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
        if (filter.equals("����")) {
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
                            if (filter.equals("����")) {
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
                            ToastUtils.showMessage(mContext, "�Ѿ�û��������");

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
                //  ȡ����������ȷ���ջ��Ժ���� ������������
                case 1:
                    loadOrderListDATA();

                    // �鿴����״̬��ת����Ӧ�� Activity
                case 2://��ת����Activity���ز�ˢ��
                    isResume = false;
                default:
                    break;
            }
        }
    };

    // ���֧����ť�ص�����
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
}
