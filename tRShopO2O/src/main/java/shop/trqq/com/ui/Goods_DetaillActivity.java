package shop.trqq.com.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.myjson.JSONObject;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.supermarket.activitys.SubmitOrderActivity;
import shop.trqq.com.ui.Base.BaseFragmentActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.ui.Fragment.CommentsFragment;
import shop.trqq.com.ui.Fragment.product_detail_Fragment;
import shop.trqq.com.ui.Fragment.product_info_Fragment;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.widget.FragmentPagerAdapter;
import shop.trqq.com.widget.MutipleTouchVerticalViewPager;
import shop.trqq.com.widget.VerticalPagerSlidingTabStrip;

/**
 *  ��Ʒ��Ϣ ViewPager չʾ��ͬ�� Fragment
 */
public class Goods_DetaillActivity extends BaseFragmentActivity {


    VerticalPagerSlidingTabStrip tabs;
    MutipleTouchVerticalViewPager pager;
    DisplayMetrics dm;
    //��Ʒ������Ϣ
    product_detail_Fragment afrag;
    //��Ʒͼ������
    product_info_Fragment bfrag;
    //��Ʒ����
    CommentsFragment cfrag;
    String[] titles = {"��Ʒ����", "ͼ������", "��Ʒ����"};
    String goods_id;
    TextView mHeadTitleTextView;
    DisplayMetrics metrics;

    private Context mContext;

    // ��ע
    private TextView product_focus;
    // ���ﳵ
    private TextView product_cart;
    // ��ӵ����ﳵ
    private TextView cart_add;
    // ��������
    private TextView cart_buy;
    private Boolean isFavoritesFlag = false;

    private Drawable focusdrawableTop;
    private String mStore_id;
    private String goodNumber;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = Goods_DetaillActivity.this;

        goods_id = getIntent().getStringExtra("goods_id");
        //������RelativeLayout���ײ��˵�����android:layout_alignParentBottom="true" ����ײ��˵�������
        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        getWindow().setSoftInputMode(mode);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        initView();

        initData();
        setListener();
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("��Ʒ��Ϣ");
    }

    public DisplayMetrics getMetrics() {
        return metrics;
    }

    private void initView() {
        setContentView(R.layout.activity_goods_detail);
        dm = getResources().getDisplayMetrics();
        // ��װ�� ViewPager
        pager = (MutipleTouchVerticalViewPager) findViewById(R.id.pager);
        tabs = (VerticalPagerSlidingTabStrip) findViewById(R.id.tabs);
        pager.setAdapter(new MyAdapter(getSupportFragmentManager(), titles));
        tabs.setViewPager(pager);
        initTitleBarView();
        //����viewpager�������ٸ���ʾ����
        pager.setOffscreenPageLimit(3);

        mLinearLayout = (LinearLayout) findViewById(R.id.layout_bottom);

        product_focus = (TextView)findViewById(R.id.product_focus);
        product_cart = (TextView)findViewById(R.id.product_cart);
        cart_add = (TextView)findViewById(R.id.cart_add);

        cart_buy = (TextView)findViewById(R.id.cart_buy);
    }


    private void initData() {

        if (UserManager.isLogin()) {
            isFavorites();
        }
        focusdrawableTop = getResources().getDrawable(
                R.drawable.nearby_focus_on);

        mLinearLayout.setVisibility(View.GONE);
    }

    private void setListener() {
        // ��ע��������¼�
        product_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavoritesFlag) {
                    delFaavoritesData();
                } else {
                    addFaavoritesData();
                }
            }
        });

        // ���ﳵ����¼��ļ���
        product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // ����ǳ��е���Ʒ����ת����Ʒ����
                if(mStore_id.equals("126")){
                    UIHelper.showMarketCart(mContext);
                }else {
                    UIHelper.showCart(mContext);
                }
            }
        });

        // ��ӵ����ﳵ ����¼��ļ���
        cart_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (UserManager.isLogin()) {
                    // ��ӵ����ﳵ
                    add_cart();
                } else {
                    ToastUtils.showMessage(mContext, "���¼");
                    UIHelper.showPersonalActivity(mContext);
                }
            }
        });

        // �����������
        cart_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.isLogin()) {
                    if (mStore_id.equals("126")) {
                        Intent localIntent = new Intent(mContext,
                                SubmitOrderActivity.class);
                        // ��Ʒ�� id �� ���������
                        String cart_id = goods_id + "|" + goodNumber;
                        localIntent.putExtra("cart_id", cart_id);
                        // ֱ�ӽ����ʶΪ0
                        localIntent.putExtra("ifcart", "0");
                        //localIntent.putExtra("buystep_flag", "0");
                        startActivity(localIntent);
                    } else {
                        Intent localIntent = new Intent(mContext,
                                CheckOutActivity.class);
                        // ��Ʒ�� id �� ���������
                        String cart_id = goods_id + "|" + goodNumber;
                        localIntent.putExtra("cart_id", cart_id);
                        // ֱ�ӽ����ʶΪ0
                        localIntent.putExtra("ifcart", "0");
                        //localIntent.putExtra("buystep_flag", "0");
                        startActivity(localIntent);
                    }
                } else {
                    ToastUtils.showMessage(mContext, "���¼");
                    UIHelper.showPersonalActivity(mContext);
                }
            }
        });
    }

    // ��ӵ��ղؼ�
    private void addFaavoritesData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("goods_id", goods_id);
        HttpUtil.post(HttpUtil.URL_ADD_FAVORITES, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            // TestUtils.println_err(TAG,jsonString);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getString(
                                        "datas").equals("1")) {
                                    ToastUtils.showMessage(mContext, "�ɹ��ղ�");
                                    product_focus
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    null, focusdrawableTop,
                                                    null, null);
                                    product_focus.setText("�ѹ�ע");
                                    isFavoritesFlag = true;
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
                        ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
                    }
                });
    }

    // ɾ���ղؼ�
    private void delFaavoritesData() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("fav_id", goods_id);
        HttpUtil.post(HttpUtil.URL_FAVORITES_DELETE, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);

                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getString(
                                        "datas").equals("1")) {
                                    ToastUtils.showMessage(mContext, "�ɹ�ȡ���ղ�");
                                    Drawable drawableTop = ContextCompat.getDrawable(mContext, R.drawable.nearby_focus_off);
                                    product_focus
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    null, drawableTop, null,
                                                    null);
                                    product_focus.setText("��ע");
                                    isFavoritesFlag = false;
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
                        ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
                    }
                });
    }

    // �Ƿ��Ѿ��ղ�
    private void isFavorites() {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("goods_id", goods_id);
        HttpUtil.post(HttpUtil.URL_FAVORITES_FLAG, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                isFavoritesFlag = false;
                                if (errStr != null) {
                                    // ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getString(
                                        "datas").equals("1")) {
                                    // ToastUtils.showMessage(mContext, "�Ѿ���ע");
                                    Drawable focusdrawableTop = getResources()
                                            .getDrawable(
                                                    R.drawable.nearby_focus_on);
                                    product_focus
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    null, focusdrawableTop,
                                                    null, null);
                                    product_focus.setText("�ѹ�ע");
                                    isFavoritesFlag = true;
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
                        ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
                    }
                });
    }


    // ��ӵ����ﳵ!!!!!!
    private void add_cart() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("goods_id", goods_id);
        requestParams.add("quantity", goodNumber);
        HttpUtil.post(HttpUtil.URL_ADD_CART, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            // TestUtils.println_err(TAG,jsonString);
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString).getJSONObject("datas");
                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                if (new JSONObject(jsonString).getString(
                                        "datas").equals("1")) {
                                    ToastUtils.showMessage(mContext, "�Ѽ��빺�ﳵ");
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
                        ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
                    }
                });
    }


    public void deliverGoodMessage(String store_id, String number) {
        mStore_id = store_id;
        goodNumber= number;
        if(!TextUtils.isEmpty(mStore_id)&&!TextUtils.isEmpty(goodNumber)){
            mLinearLayout.setVisibility(View.VISIBLE);
        }
    }


    // ������ adapter
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

        /**
         *  ���ݲ�ͬ��position ���ز�ͬ�� Fragment
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (afrag == null) {
                        // ��Ʒ�����б�
                        afrag = new product_detail_Fragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", goods_id);
                        afrag.setArguments(bundle);
                    }
                    return afrag;
                case 1:
                    if (bfrag == null) {
                        bfrag = new product_info_Fragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", goods_id);
                        bundle.putString("mobile_body", "");
                        bfrag.setArguments(bundle);
                    }
                    return bfrag;
                case 2:
                    if (cfrag == null) {
                        cfrag = new CommentsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("goods_id", goods_id);
                        cfrag.setArguments(bundle);
                    }
                    return cfrag;
                default:
                    return null;
            }
        }
    }
}
