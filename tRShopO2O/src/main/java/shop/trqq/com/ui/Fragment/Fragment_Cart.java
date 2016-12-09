package shop.trqq.com.ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.roamer.slidelistview.SlideListView;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.ListViewCartAdapter;
import shop.trqq.com.bean.CartInfoBean;
import shop.trqq.com.supermarket.activitys.MarketGoCartActivity;
import shop.trqq.com.ui.CheckOutActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 购物车Fragment
 */
public class Fragment_Cart extends Fragment implements OnItemClickListener {
    private static final String TAG = "CartFragment";
    private View rootView;
    private Context appContext;
    private Gson gson;

    private SlideListView mSlideListView;
    private ListViewCartAdapter listCartAdapter;
    private TextView mHeadTitleTextView;
    private RelativeLayout mCart_loginlayou;
    private ProgressActivity progressActivity;
    private TextView checkOut;
    private ArrayList<CartInfoBean> cartInfoList;
    private TextView Sum;
    private float SumNumber;
    private ArrayList<String> cartIDList = new ArrayList<String>();
    private ArrayList<CartInfoBean> mCartData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        gson = new Gson();
        cartInfoList = new ArrayList<CartInfoBean>();
        mCartData = new ArrayList<CartInfoBean>();
        initTitleBarView();
        initViews();

        return rootView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        cartInfoList = new ArrayList<CartInfoBean>();
        loadOnlineCartListData();
    }



    @Override
    public void onPause() {
        // TODO Auto-generated method stub

        cartInfoList = listCartAdapter.getmData();
        System.err.println("cartInfoList.size()" + cartInfoList.size());
        if (cartInfoList.size() > 0) {
            YkLog.i(TAG, "���湺�ﳵ��Ϣ");
            cartIDList.clear();
            for (int i = 0; i < cartInfoList.size(); i++) {
                cart_edit_quantity(cartInfoList.get(i).getCart_id(),
                        cartInfoList.get(i).getGoods_num());
                cartIDList.add("," + cartInfoList.get(i).getCart_id() + "|"
                        + cartInfoList.get(i).getGoods_num());
            }
        }
        super.onPause();
    }

    private void initTitleBarView() {
        YkLog.i(TAG, "��ʼ����������ͼ");
        mHeadTitleTextView = (TextView) rootView
                .findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("���ﳵ");
        LinearLayout tLinearLayout=(LinearLayout) rootView
                .findViewById(R.id.header_relativelayout);
        SystemBarHelper.immersiveStatusBar(getActivity(),0);
        SystemBarHelper.setHeightAndPadding(getActivity(), tLinearLayout);
    }

    private void initViews() {
        listCartAdapter = new ListViewCartAdapter(appContext, cartInfoList,
                handler);

        listCartAdapter = new ListViewCartAdapter(appContext, cartInfoList, handler);

        mCart_loginlayou = (RelativeLayout) rootView
                .findViewById(R.id.cart_loginlayou);
        progressActivity = (ProgressActivity) rootView
                .findViewById(R.id.cart_progress);
        progressActivity.showLoading();
        mSlideListView = (SlideListView) rootView
                .findViewById(R.id.cart_list_view);
        mSlideListView.setOnItemClickListener(this);
        mSlideListView.setAdapter(listCartAdapter);
        Sum = (TextView) rootView.findViewById(R.id.textView_sum);
        checkOut = (TextView) rootView.findViewById(R.id.cart_check_out);
        checkOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String str1 = "";
                for (int i = 0; ; i++) {
                    if (i >= cartIDList.size()) {
                        if (cartIDList.size() <= 0)
                            break;
                        String str2 = str1.substring(1, str1.length());
                        Intent localIntent = new Intent(appContext,
                                CheckOutActivity.class);
                        YkLog.t("cart_id", str2);
                        localIntent.putExtra("cart_id", str2);
                        localIntent.putExtra("ifcart", "1");
                        localIntent.putExtra("buystep_flag", "0");
                        startActivity(localIntent);
                        return;
                    }
                    str1 = str1 + (String) cartIDList.get(i);
                }
                // UIHelper.showCheckOut(appContext);
            }
        });

        LinearLayout skipToMarketCart= (LinearLayout)rootView.findViewById(R.id.skip_market_cart);
        // �����ת�����й��ﳵ����
        skipToMarketCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MarketGoCartActivity.class);
                startActivity(intent);
            }
        });
    }

    // �������繺�ﳵ������
    private void loadOnlineCartListData() {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        String uri = HttpUtil.URL_CART_LIST;
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    YkLog.t(TAG, jsonString);
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    String cart_list = jsonObjects.getString("cart_list");
                    mCartData = gson.fromJson(cart_list,
                            new TypeToken<List<CartInfoBean>>() {
                            }.getType());
                   ;
                    if (mCartData != null) {
                        YkLog.e(TAG, cartInfoList.size()+"֮ǰ");
                        for (int i = 0; i < mCartData.size(); i++) {
                            // ��������ܾӳ��� ȥ�������뵽���ܾӳ��й��ﳵ����
                            if(!TextUtils.equals(mCartData.get(i).getStore_id(),"126")){
                                cartInfoList.add(mCartData.get(i));
                            }
                        }
                    }
                    YkLog.e(TAG, cartInfoList.size()+"");
                    cartIDList.clear();
                    System.err.println(cart_list);
                    // ??????????{"code":200,"datas":{"cart_list":[],"sum":"0.00"}}
                    if (cartInfoList.size() == 0&&isAdded()) {
                        // ??????????
                        Drawable emptyDrawable = ContextCompat.getDrawable(appContext, R.drawable.error_cart);
                        progressActivity.showEmpty(emptyDrawable, "���Ĺ��ﳵ�ǿյ�",
                                "���ﳵû����Ʒ����ȥѡ��");
                        cartInfoList.clear();
                        // ????????????onPause()????getData????
                        listCartAdapter.setData(cartInfoList);
                        return;
                    }
                    listCartAdapter.setData(cartInfoList);
                    listCartAdapter.notifyDataSetChanged();
                    SumNumber = Float.parseFloat(jsonObjects.getString("sum"));
                    // System.err.println(SumNumber);
                    Sum.setText("�ܼƣ�" + SumNumber + "Ԫ");
                    for (int i = 0; i < cartInfoList.size(); i++) {
                        cartIDList.add("," + cartInfoList.get(i).getCart_id()
                                + "|" + cartInfoList.get(i).getGoods_num());
                    }
                    progressActivity.showContent();
                } catch (Exception e) {
                    e.printStackTrace();
                    // data err???? ��???
                    Drawable emptyDrawable =ContextCompat.getDrawable(appContext, R.drawable.error_cart);
                    progressActivity.showEmpty(emptyDrawable, "���Ĺ��ﳵ�ǿյ�",
                            "����¼��ͬ���������ֻ����ﳵ����Ʒ");
                    cartInfoList.clear();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    if (cartInfoList.size() == 0) {
                        Drawable errorDrawable =ContextCompat.getDrawable(appContext, R.drawable.wifi_off);
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
            }
        });
    }

    OnClickListener errorClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            progressActivity.showLoading();
            loadOnlineCartListData();
        }
    };

    private void cart_edit_quantity(String cart_id, String quantity) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("cart_id", cart_id);
        requestParams.add("quantity", quantity);
        HttpUtil.post(HttpUtil.URL_CART_EDIT_QUANTITY, requestParams,
                new AsyncHttpResponseHandler() {
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
                                    ToastUtils.showMessage(appContext, errStr);
                                }
                            } catch (Exception e) {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        ToastUtils.showMessage(appContext, R.string.get_informationData_failure);
                    }

                });

    }

    public void RESum(float num) {
        SumNumber = SumNumber + num;
        Sum.setText("�ܼƣ�" + SumNumber + "Ԫ");
    }

    ;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // �����������Ժ��������
            switch (msg.what) {
                case 1:
                    float num = msg.getData().getFloat("SumNum");
                    Log.d("delete",num+"�ܽ��"+SumNumber);
                    SumNumber = SumNumber + num;
//                    String sum = String.format("%.1f", num);
//                    Sum.setText("�ܼƣ�" + sum + "Ԫ");
                    Sum.setText("�ܼƣ�" + SumNumber + "Ԫ");
                    break;
                case 2:
                    // setUserVisibleHint(true);
                    onResume();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }
}