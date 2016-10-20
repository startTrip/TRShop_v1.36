package shop.trqq.com.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.myjson.JSONException;
import org.myjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.lightsky.infiniteindicator.InfiniteIndicatorLayout;
import cn.lightsky.infiniteindicator.slideview.BaseSliderView;
import cn.lightsky.infiniteindicator.slideview.DefaultSliderView;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.ListViewGiftAdapter;
import shop.trqq.com.adapter.ListViewMansongAdapter;
import shop.trqq.com.adapter.ListViewSpecAdapter;
import shop.trqq.com.adapter.OrderAdapter;
import shop.trqq.com.bean.Gift_ArrayBean;
import shop.trqq.com.bean.GoodsDetails;
import shop.trqq.com.bean.Mansong_RulesBean;
import shop.trqq.com.bean.SpecBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.MyListView;

/**
 *作废，新版Goods_DetaillActivity
 *商品详情
 * @author Weiss
 */
public class product_detailActivity extends BaseActivity {
    private static final String TAG = "product_detailActivity";
    // 标题栏标题
    private TextView mHeadTitleTextView;
    private TextView product_price;
    private TextView product_name;
    private TextView product_focus;
    private TextView product_cart;
    private TextView goods_storage;
    private TextView goods_jingle;
    private TextView goods_marketprice;
    private TextView goods_salenum;
    private LinearLayout is_virtual;
    private TextView virtual_indate;
    private LinearLayout promotion_price_layout;
    private LinearLayout product_info_Layout;
    private LinearLayout store_Layout;
    private TextView promotion_price;
    private TextView product_price_text;
    private TextView cart_add;
    private Context mContext;
    private Drawable focusdrawableTop;

    private MyListView specListView;
    private ListViewSpecAdapter specListView_adapter;
    private List<String[]> speclistString;

    private MyListView mansongListView;
    private ListViewMansongAdapter mansongView_adapter;
    private List<Mansong_RulesBean> mansongList;
    private TextView mansong_name;
    private TextView mansong_info;

    private LinearLayout giftLayout;
    private MyListView giftListView;
    private ListViewGiftAdapter giftListView_adapter;
    private List<Gift_ArrayBean> giftList;
    // 货品数量
    private TextView pop_reduce;
    private TextView pop_num;
    private TextView pop_add;
    private String goods_id;
    private String mobile_body;
    private GoodsDetails goodinfo = new GoodsDetails();
    private Gson gson;

    private ArrayList<SpecBean> SpecnameList;

    private JSONObject spec_name, spec_list, spec_value;
    private InfiniteIndicatorLayout indicator;
    private String store_id;
    private Boolean isFavoritesFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        mContext = this;
        gson = new Gson();
        Intent intent = getIntent();
        goods_id = intent.getStringExtra("goods_id");
        initTitleBarView();
        initViews();
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        YkLog.i(TAG, "初始化标题栏视图");
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("商品详情");
    }

    private void initViews() {

        indicator = (InfiniteIndicatorLayout) findViewById(R.id.indicator_good_image);
        product_name = (TextView) findViewById(R.id.product_name);
        product_price = (TextView) findViewById(R.id.product_price);
        product_focus = (TextView) findViewById(R.id.product_focus);
        product_cart = (TextView) findViewById(R.id.product_cart);
        goods_storage = (TextView) findViewById(R.id.goods_storage);
        goods_jingle = (TextView) findViewById(R.id.goods_jingle);
        goods_salenum = (TextView) findViewById(R.id.goods_salenum);
        goods_marketprice = (TextView) findViewById(R.id.goods_marketprice);
        goods_marketprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        giftListView = (MyListView) findViewById(R.id.listView_gift);
        giftLayout = (LinearLayout) findViewById(R.id.gift_layout);
        is_virtual = (LinearLayout) findViewById(R.id.is_virtual_Layout);
        product_info_Layout = (LinearLayout) findViewById(R.id.product_info_Layout);
        store_Layout = (LinearLayout) findViewById(R.id.store_Layout);
        virtual_indate = (TextView) findViewById(R.id.virtual_indate);
        promotion_price_layout = (LinearLayout) findViewById(R.id.promotion_price_layout);
        promotion_price = (TextView) findViewById(R.id.promotion_price);
        product_price_text = (TextView) findViewById(R.id.product_price_text);
        cart_add = (TextView) findViewById(R.id.cart_add);
        focusdrawableTop = getResources().getDrawable(
                R.drawable.nearby_focus_on);
        pop_reduce = (TextView) findViewById(R.id.pop_reduce);
        pop_num = (TextView) findViewById(R.id.pop_num);
        pop_add = (TextView) findViewById(R.id.pop_add);
        pop_reduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(pop_num.getText().toString());
                if (1 < num) {
                    num--;
                    pop_num.setText(String.valueOf(num));
                }
            }
        });

        product_focus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isFavoritesFlag) {
                    delFaavoritesData();
                } else {
                    addFaavoritesData();
                }

            }
        });
        product_cart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UIHelper.showCart(mContext);
            }
        });

        if (UserManager.isLogin()) {
            isFavorites();
        }
        loadOnlineproductdata("3");
    }

    // 加载网络商品详情数据
    private void loadOnlineproductdata(final String shopId) {
        RequestParams requestParams = new RequestParams();
        String uri = HttpUtil.URL_GOODSDETAILS + "&goods_id=" + goods_id;
        // System.err.println(TAG + "：" + uri);
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(jsonString)
                            .optJSONObject("datas");
                    // TestUtils.println_err(TAG, jsonString);
                    try {
                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                            finish();
                        }
                    } catch (Exception erre) {
                        String gooddata = jsonObject.optString("goods_info");
                        JSONObject goods_info = new JSONObject(gooddata);
                        goodinfo = GoodsDetails.newInstanceList(gooddata);
                        // System.err.println("goodinfo.getSpec_name()："+goodinfo.getSpec_name());
                        String imageUrl = jsonObject.getString("goods_image");
                        String[] strs = imageUrl.split(",");
                        indicator.removeAllSliders();
                        for (String element : strs) {
                            DefaultSliderView textSliderView = new DefaultSliderView(
                                    mContext);
                            // textSliderView.getBundle().putSerializable("extra",
                            // element);
                            element = element.replace("127.0.0.1",
                                    "172.25.25.1");
                            textSliderView.image(element).setScaleType(
                                    BaseSliderView.ScaleType.Fit);
                            indicator.addSlider(textSliderView);
                        }
                        indicator
                                .setIndicatorPosition(InfiniteIndicatorLayout.IndicatorPosition.Center_Bottom);

						/*
                         * ImageLoader.getInstance().displayImage(strs[0],
						 * product_image, options);
						 */
                        if (!goodinfo.getGoods_jingle().equals("")) {
                            goods_jingle.setText(goodinfo.getGoods_jingle());
                            goods_jingle.setVisibility(View.VISIBLE);
                        }
                        JSONObject store_info = jsonObject
                                .optJSONObject("store_info");
                        store_id = store_info.optString("store_id");
                        product_name.setText(goodinfo.getGoods_name());
                        product_price.setText("￥ " + goodinfo.getGoods_price());
                        goods_marketprice.setText("￥ "
                                + goodinfo.getGoods_marketprice());
                        final String storage = goodinfo.getGoods_storage();
                        goods_storage.setText("库存：" + storage + "件");
                        goods_salenum.setText(goodinfo.getGoods_salenum() + "件");
                        mobile_body = goodinfo.getMobile_body();
                        if (!goodinfo.getPromotion_type().equals("0")) {
                            // 促销类型0为无，1为抢购，2为限时折扣
                            String promotiontype_text = "";
                            if (goodinfo.getPromotion_type().equals("1"))
                                promotiontype_text = " (抢购)";
                            if (goodinfo.getPromotion_type().equals("2"))
                                promotiontype_text = " (限时折扣)";
                            promotion_price.setText("￥ "
                                    + goodinfo.getPromotion_price()
                                    + promotiontype_text);
                            product_price_text.setText("原售价：");
                            product_price.setTextColor(Color
                                    .parseColor("#9e9e9e"));
                            product_price.getPaint().setFlags(
                                    Paint.STRIKE_THRU_TEXT_FLAG);
                            promotion_price_layout.setVisibility(View.VISIBLE);
                        }
                        if (goodinfo.getIs_virtual().equals("1")) {

                            String time = OrderAdapter.getDateToString(Long
                                    .parseLong(goodinfo.getVirtual_indate()) * 1000);
                            virtual_indate.setText("即日起  到  " + time + "每人次限购"
                                    + goodinfo.getVirtual_limit() + "件");
                            is_virtual.setVisibility(View.VISIBLE);
                        }
                        if (goodinfo.getHave_gift().equals("gift")) {
                            // 赠品
                            giftList = new ArrayList<Gift_ArrayBean>();
                            String gift_array = jsonObject.getJSONArray(
                                    "gift_array").toString();
                            giftList = gson.fromJson(gift_array,
                                    new TypeToken<List<Gift_ArrayBean>>() {
                                    }.getType());
                            giftListView_adapter = new ListViewGiftAdapter(
                                    mContext);
                            giftListView_adapter.setData(giftList);
                            giftListView.setAdapter(giftListView_adapter);
                            giftLayout.setVisibility(View.VISIBLE);
                        }
                        try {
                            // 活动
                            mansongList = new ArrayList<Mansong_RulesBean>();
                            JSONObject mansongData = jsonObject
                                    .getJSONObject("mansong_info");
                            mansongListView = (MyListView) findViewById(R.id.listView_mansong_rules);
                            mansong_name = (TextView) findViewById(R.id.mansong_name);
                            mansong_info = (TextView) findViewById(R.id.mansong_info);
                            mansong_name.setText("活动名称："
                                    + mansongData.getString("mansong_name"));
                            mansong_info.setText("活动描述：");
                            String ruleslist = mansongData
                                    .getJSONArray("rules").toString();
                            mansongList = gson.fromJson(ruleslist,
                                    new TypeToken<List<Mansong_RulesBean>>() {
                                    }.getType());
                            mansongView_adapter = new ListViewMansongAdapter(
                                    mContext);
                            mansongView_adapter.setData(mansongList);
                            mansongListView.setAdapter(mansongView_adapter);
                            mansongListView.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            YkLog.i(TAG, "没有活动内容");
                            e.printStackTrace();
                        }

                        try {
                            // 规格
                            SpecnameList = new ArrayList<SpecBean>();
                            spec_list = jsonObject.getJSONObject("spec_list");
                            spec_name = goods_info.optJSONObject("spec_name");
                            spec_value = goods_info.optJSONObject("spec_value");
                            YkLog.t("spec_name:",
                                    spec_name.toString());
                            setSpeacBean(spec_name, SpecnameList);
                            ArrayList<SpecBean> specBean = new ArrayList<SpecBean>();
                            setSpeacBean(spec_value, specBean);
                            ArrayList<SpecBean> Goods_specBean = new ArrayList<SpecBean>();
                            setSpeacBean(
                                    goods_info.optJSONObject("goods_spec"),
                                    Goods_specBean);
                            speclistString = new ArrayList<String[]>();
                            for (int i = 0; i < SpecnameList.size(); i++) {
                                ArrayList<SpecBean> specBean2 = new ArrayList<SpecBean>();
                                setSpeacBean(spec_value
                                        .optJSONObject(SpecnameList.get(i)
                                                .getSpecID()), specBean2);
                                String[] specValue = new String[specBean2
                                        .size() + 2];
                                specValue[0] = SpecnameList.get(i)
                                        .getSpecName();
                                specValue[1] = Goods_specBean.get(i)
                                        .getSpecName();
                                for (int j = 2; j < specBean2.size() + 2; j++) {
                                    specValue[j] = specBean2.get(j - 2)
                                            .getSpecName();
                                }
                                speclistString.add(specValue);

                            }
                            specListView = (MyListView) findViewById(R.id.list_spec);
							/*
							 * specListView_adapter = new ListViewSpecAdapter(
							 * mContext, product_detailActivity.this);
							 */
                            specListView_adapter.setData(speclistString);
                            specListView.setAdapter(specListView_adapter);
                            specListView.setVisibility(View.VISIBLE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        product_info_Layout
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        UIHelper.showProductinfo(mContext,
                                                goods_id, mobile_body);
                                    }
                                });
                        store_Layout.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                // UIHelper.showShop(mContext, "", "",store_id);
                                UIHelper.showStore(mContext, store_id);
                            }
                        });
                        pop_add.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int num = Integer.parseInt(pop_num.getText()
                                        .toString());
                                if (num < Integer.parseInt(storage)) {
                                    num++;
                                    pop_num.setText(String.valueOf(num));
                                }
                            }
                        });
                        final String is_virtual = goods_info
                                .getString("is_virtual");
                        if (is_virtual.equals("1")
                                || goodinfo.getPromotion_type().equals("1"))
                            cart_add.setText("立即购买");
                        cart_add.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (is_virtual.equals("1")) {
                                    // cart_add.setText("立即购买");
                                    loadVrBuyStep1Data();
                                    UIHelper.showVrBuy1(mContext, goods_id,
                                            pop_num.getText().toString());
                                } else if (goodinfo.getPromotion_type().equals(
                                        "1")) {
                                    // cart_add.setText("立即购买");
                                    Intent localIntent = new Intent(mContext,
                                            CheckOutActivity.class);
                                    localIntent.putExtra("cart_id", goods_id
                                            + "|"
                                            + pop_num.getText().toString());
                                    localIntent.putExtra("ifcart", "0");
                                    startActivity(localIntent);
                                } else {
                                    add_cart();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                ToastUtils.showMessage(getApplicationContext(), R.string.get_informationData_failure);
            }
        });
    }

    // 规格方框点击事件
    public void onspec(int listposition, int gridposition) {

        try {
            JSONObject Goods_spec = new JSONObject(goodinfo.getGoods_spec());
            ArrayList<SpecBean> specBean2 = new ArrayList<SpecBean>();
            setSpeacBean(spec_value.optJSONObject(SpecnameList
                    .get(listposition).getSpecID()), specBean2);
            String spceID = specBean2.get(gridposition).getSpecID();
            ArrayList<SpecBean> specBean3 = new ArrayList<SpecBean>();
            setSpeacBean(Goods_spec, specBean3);
            String spec_listspceID = (listposition == 0) ? spceID : specBean3
                    .get(0).getSpecID();
            for (int i = 1; i < specBean3.size(); i++) {
                if (i == listposition) {
                    spec_listspceID = spec_listspceID + "|" + spceID;
                } else {
                    spec_listspceID = spec_listspceID + "|"
                            + specBean3.get(i).getSpecID();
                }
                // TestUtils.println_err("spec_listspceID:", spec_listspceID);
            }
            // 布局还原默认值
            product_price_text.setText("价格：");
            product_price.setTextColor(Color.parseColor("#FF0000"));
            product_price.getPaint().setFlags(0);
            promotion_price_layout.setVisibility(View.GONE);
            giftLayout.setVisibility(View.GONE);

            goods_id = spec_list.optString(spec_listspceID);
            loadOnlineproductdata(goods_id);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // JSONObject转换规格List<SpecBean>
    private void setSpeacBean(JSONObject jsonObject, ArrayList<SpecBean> list) {
        int i = 0;
        // 无序
        Iterator keyIter = jsonObject.keys();
        while (keyIter.hasNext()) {
            try {
                SpecBean bean = new SpecBean();
                String id = (String) keyIter.next();
                bean.setSpecID(id);
                bean.setSpecName(jsonObject.opt(id).toString());
                list.add(bean);
                // System.err.println(list.get(i).getSpecID());
                i++;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // 虚拟购买
    private void loadVrBuyStep1Data() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("cart_id", goods_id);
        requestParams.add("quantity", pop_num.getText().toString());
        HttpUtil.post(HttpUtil.URL_MEMBER_VR_BUY1, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody) {
                        try {
                            String jsonString = new String(responseBody);
                            JSONObject jsonObject = new JSONObject(jsonString)
                                    .getJSONObject("datas");
                            YkLog.t(TAG, jsonString);
                            try {

                                String errStr = jsonObject.getString("error");
                                if (errStr != null) {
                                    ToastUtils.showMessage(mContext, errStr);
                                }
                            } catch (Exception e) {
                                // ToastUtils.showMessage(mContext,
                                // jsonObject.toString());

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        ToastUtils.showMessage(getApplicationContext(),
                                R.string.get_informationData_failure);
                    }
                });
    }

    // 添加到购物车
    private void add_cart() {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("goods_id", goods_id);
        requestParams.add("quantity", pop_num.getText().toString());
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
                                    ToastUtils.showMessage(mContext, "已加入购物车");
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
                        ToastUtils.showMessage(getApplicationContext(),
                                R.string.get_informationData_failure);
                    }
                });
    }

    // 添加到收藏夹
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
                                    ToastUtils.showMessage(mContext, "成功收藏");
                                    product_focus
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    null, focusdrawableTop,
                                                    null, null);
                                    product_focus.setText("已关注");
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
                        ToastUtils.showMessage(getApplicationContext(),
                                R.string.get_informationData_failure);
                    }
                });
    }

    // 删除收藏夹
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
                            YkLog.t(TAG, jsonString + goods_id);
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
                                    ToastUtils.showMessage(mContext, "成功取消收藏");
                                    Drawable drawableTop = getResources()
                                            .getDrawable(
                                                    R.drawable.nearby_focus_off);
                                    product_focus
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    null, drawableTop, null,
                                                    null);
                                    product_focus.setText("关注");
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
                        ToastUtils.showMessage(getApplicationContext(),
                                R.string.get_informationData_failure);
                    }
                });
    }

    // 是否已经收藏
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
                                    // ToastUtils.showMessage(mContext, "已经关注");
                                    Drawable focusdrawableTop = getResources()
                                            .getDrawable(
                                                    R.drawable.nearby_focus_on);
                                    product_focus
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    null, focusdrawableTop,
                                                    null, null);
                                    product_focus.setText("已关注");
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
                        ToastUtils.showMessage(getApplicationContext(),
                                R.string.get_informationData_failure);
                    }
                });
    }
}
