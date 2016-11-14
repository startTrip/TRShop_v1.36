package shop.trqq.com.ui.Fragment;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.myjson.JSONException;
import org.myjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.lightsky.infiniteindicator.InfiniteIndicatorLayout;
import cn.lightsky.infiniteindicator.slideview.BaseSliderView;
import cn.lightsky.infiniteindicator.slideview.DefaultSliderView;
import de.greenrobot.event.EventBus;
import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.GridviewGoodsCommendAdapter;
import shop.trqq.com.adapter.ListViewGiftAdapter;
import shop.trqq.com.adapter.ListViewMansongAdapter;
import shop.trqq.com.adapter.ListViewSpecAdapter;
import shop.trqq.com.adapter.OrderAdapter;
import shop.trqq.com.bean.Gift_ArrayBean;
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.bean.GoodsDetails;
import shop.trqq.com.bean.Mansong_RulesBean;
import shop.trqq.com.bean.SpecBean;
import shop.trqq.com.bean.Store_CreditBean;
import shop.trqq.com.event.EventGoodmInfo;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.ui.Goods_DetaillActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.MyGridView;
import shop.trqq.com.widget.MyListView;

public class product_detail_Fragment extends Fragment implements OnClickListener {

    private static final String TAG = "product_detail_Fragment";
    // 标题栏标题
    private TextView mHeadTitleTextView;
    private TextView product_price;
    private TextView product_name;


    private TextView goods_storage;
    private TextView goods_jingle;
    private TextView goods_marketprice;
    private TextView goods_salenum;
    private LinearLayout is_virtual;
    private TextView virtual_indate;
    private LinearLayout is_fcode_Layout;
    private LinearLayout is_presell_Layout;
    private TextView presell_deliverdate_str;
    private LinearLayout promotion_price_layout;
    private LinearLayout product_info_Layout;
    private LinearLayout store_Layout;
    private TextView store_TextView;
    private TextView store_info;
    private TextView store_search;
    private TextView store_service;
    private TextView goods_service;
    private TextView promotion_price;
    private TextView product_price_text;


    private Context mContext;

    // 店铺评分
    private TextView store_desccredit;
    private TextView store_desccredit_credit;
    private TextView store_desccredit_percent;
    private TextView store_servicecredit;
    private TextView store_servicecredit_credit;
    private TextView store_servicecredit_percent;
    private TextView store_deliverycredit;
    private TextView store_deliverycredit_credit;
    private TextView store_deliverycredit_percent;
    // 规格列表
    private MyListView specListView;
    private ListViewSpecAdapter specListView_adapter;
    private List<String[]> speclistString;
    // 活动列表
    private MyListView mansongListView;
    private ListViewMansongAdapter mansongView_adapter;
    private List<Mansong_RulesBean> mansongList;
    private TextView mansong_name;
    private TextView mansong_info;
    private LinearLayout mansong_layout;
    // 赠品列表
    private LinearLayout giftLayout;
    private MyListView giftListView;
    private ListViewGiftAdapter giftListView_adapter;
    private List<Gift_ArrayBean> giftList;
    // 推荐商品
    // private LinearLayout goods_commendLayout;
    private MyGridView goods_commend_GridView;
    private GridviewGoodsCommendAdapter goods_commend_adapter;
    private List<GoodsBean> goods_commendList;
    // 货品数量
    private TextView pop_reduce;
    private EditText pop_num;
    private TextView pop_add;
    private String goods_id;
    private String mobile_body;
    private GoodsDetails goodinfo = new GoodsDetails();
    private Gson gson;

    private ArrayList<SpecBean> SpecnameList;

    private JSONObject spec_name, spec_list, spec_value;
    private InfiniteIndicatorLayout indicator;// 商品图片
    private String store_id;
    private String store_name;
    private String storage;

    private View rootView;// 缓存Fragment view

    private ProgressActivity progressActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater
                .inflate(R.layout.activity_product, container, false);
        mContext = getActivity();
        gson = new Gson();
        //EventBus.getDefault().register(mContext);
        Bundle bundle = getArguments();
        goods_id = bundle.getString("goods_id");
        // initTitleBarView();
        initViews();
        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister
        //EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化标题栏视图
     */
    private void initTitleBarView() {
        YkLog.i(TAG, "初始化标题栏视图");
        mHeadTitleTextView = (TextView) rootView
                .findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("商品详情");
    }

    private void initViews() {

        indicator = (InfiniteIndicatorLayout) rootView
                .findViewById(R.id.indicator_good_image);
        product_name = (TextView) rootView.findViewById(R.id.product_name);
        product_price = (TextView) rootView.findViewById(R.id.product_price);

        goods_storage = (TextView) rootView.findViewById(R.id.goods_storage);
        goods_jingle = (TextView) rootView.findViewById(R.id.goods_jingle);
        goods_salenum = (TextView) rootView.findViewById(R.id.goods_salenum);
        goods_marketprice = (TextView) rootView
                .findViewById(R.id.goods_marketprice);
        // 中横线
        goods_marketprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        goods_commend_GridView = (MyGridView) rootView
                .findViewById(R.id.goods_commend_GridView);
        giftListView = (MyListView) rootView.findViewById(R.id.listView_gift);
        giftLayout = (LinearLayout) rootView.findViewById(R.id.gift_layout);
        is_virtual = (LinearLayout) rootView
                .findViewById(R.id.is_virtual_Layout);
        product_info_Layout = (LinearLayout) rootView
                .findViewById(R.id.product_info_Layout);
        store_Layout = (LinearLayout) rootView.findViewById(R.id.store_Layout);
        virtual_indate = (TextView) rootView.findViewById(R.id.virtual_indate);
        is_fcode_Layout = (LinearLayout) rootView
                .findViewById(R.id.is_fcode_Layout);
        is_presell_Layout = (LinearLayout) rootView
                .findViewById(R.id.is_presell_Layout);
        presell_deliverdate_str = (TextView) rootView
                .findViewById(R.id.presell_deliverdate_str);
        promotion_price_layout = (LinearLayout) rootView
                .findViewById(R.id.promotion_price_layout);
        promotion_price = (TextView) rootView
                .findViewById(R.id.promotion_price);
        product_price_text = (TextView) rootView
                .findViewById(R.id.product_price_text);

        pop_reduce = (TextView) rootView.findViewById(R.id.pop_reduce);
        pop_num = (EditText) rootView.findViewById(R.id.pop_num);
        pop_add = (TextView) rootView.findViewById(R.id.pop_add);
        store_TextView = (TextView) rootView.findViewById(R.id.store_go);
        store_info = (TextView) rootView.findViewById(R.id.store_info);
        goods_service = (TextView) rootView.findViewById(R.id.goods_service);
        store_search = (TextView) rootView.findViewById(R.id.store_search);
        store_service = (TextView) rootView.findViewById(R.id.store_service);

        // 店铺信息
        store_desccredit = (TextView) rootView
                .findViewById(R.id.store_desccredit);
        store_desccredit_credit = (TextView) rootView
                .findViewById(R.id.store_desccredit_credit);
        store_desccredit_percent = (TextView) rootView
                .findViewById(R.id.store_desccredit_percent);
        store_servicecredit = (TextView) rootView
                .findViewById(R.id.store_servicecredit);
        store_servicecredit_credit = (TextView) rootView
                .findViewById(R.id.store_servicecredit_credit);
        store_servicecredit_percent = (TextView) rootView
                .findViewById(R.id.store_servicecredit_percent);
        store_deliverycredit = (TextView) rootView
                .findViewById(R.id.store_deliverycredit);
        store_deliverycredit_credit = (TextView) rootView
                .findViewById(R.id.store_deliverycredit_credit);
        store_deliverycredit_percent = (TextView) rootView
                .findViewById(R.id.store_deliverycredit_percent);

        pop_reduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String numStr=pop_num.getText() .toString();
                if("".equals(numStr)){
                    numStr="0";
                }
                int num = Integer.parseInt(numStr);
                if (1 < num) {
                    num--;
                    pop_num.setText(String.valueOf(num));
                }
            }
        });



        progressActivity = (ProgressActivity) rootView
                .findViewById(R.id.progress);
        progressActivity.showLoading();

        loadOnlineproductdata();
    }

    // 加载网络商品详情数据
    private void loadOnlineproductdata() {
        RequestParams requestParams = new RequestParams();
        if (UserManager.isLogin()) {
            String key = UserManager.getUserInfo().getKey();
            requestParams.add("key", key);
        }
        String uri = HttpUtil.URL_GOODSDETAILS + "&goods_id=" + goods_id;
        // System.err.println(TAG + "：" + uri);
        HttpUtil.post(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(jsonString)
                            .optJSONObject("datas");
                    YkLog.longe(TAG, jsonString);
                    try {
                        String errStr = jsonObject.getString("error");
                        if (!TextUtils.isEmpty(errStr)) {
                            ToastUtils.showMessage(mContext, errStr);
                                UIHelper.showPersonalActivity(getActivity());
                                getActivity().finish();
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
                            //本地测试
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
                        JSONObject store_infoJSON = jsonObject
                                .optJSONObject("store_info");
                        store_id = store_infoJSON.optString("store_id");
                        store_name = store_infoJSON.optString("store_name");
                        store_info.setText("店铺：" + store_name);
                        goods_service
                                .setText("由" + store_name + "负责发货，并提供售后服务");
                        // 店铺评分
                        JSONObject store_credit = store_infoJSON
                                .getJSONObject("store_credit");
                        String store_desccreditStr = store_credit
                                .getJSONObject("store_desccredit").toString();
                        Store_CreditBean store_desccreditBean = new Store_CreditBean();
                        store_desccreditBean = gson.fromJson(
                                store_desccreditStr, Store_CreditBean.class);
                        // store_desccredit.setText(store_desccreditBean.getText());
                        store_desccredit_credit.setText(store_desccreditBean
                                .getCredit());
                        store_desccredit_percent.setText(store_desccreditBean
                                .getPercent_text());

                        String store_servicecreditStr = store_credit
                                .getJSONObject("store_servicecredit")
                                .toString();
                        Store_CreditBean store_servicecreditBean = new Store_CreditBean();
                        store_servicecreditBean = gson.fromJson(
                                store_servicecreditStr, Store_CreditBean.class);
                        // store_servicecredit.setText(store_servicecreditBean.getText());
                        store_servicecredit_credit
                                .setText(store_servicecreditBean.getCredit());
                        store_servicecredit_percent
                                .setText(store_servicecreditBean
                                        .getPercent_text());

                        String store_deliverycreditStr = store_credit
                                .getJSONObject("store_deliverycredit")
                                .toString();
                        Store_CreditBean store_deliverycreditBean = new Store_CreditBean();
                        store_deliverycreditBean = gson
                                .fromJson(store_deliverycreditStr,
                                        Store_CreditBean.class);
                        // store_deliverycredit.setText(store_deliverycreditBean.getText());
                        store_deliverycredit_credit
                                .setText(store_deliverycreditBean.getCredit());
                        store_deliverycredit_percent
                                .setText(store_deliverycreditBean
                                        .getPercent_text());

                        product_name.setText(goodinfo.getGoods_name());
                        product_price.setText("￥ " + goodinfo.getGoods_price());
                        goods_marketprice.setText("￥ "
                                + goodinfo.getGoods_marketprice());
                        storage = goodinfo.getGoods_storage();
                        goods_storage.setText("库存：" + storage + "件");
                        goods_salenum.setText(goodinfo.getGoods_salenum() + "件");
                        mobile_body = goodinfo.getMobile_body();
                        if (goodinfo.getPromotion_type() != null
                                && !goodinfo.getPromotion_type().equals("")) {
                            // 促销类型0为无，1为抢购groupbuy，2为限时折扣xianshi
                            String promotiontype_text = "";
                            System.err.println("getPromotion_type:"
                                    + goodinfo.getPromotion_type());
                            if (goodinfo.getPromotion_type().equals("groupbuy"))
                                promotiontype_text = " (抢购)";
                            if (goodinfo.getPromotion_type().equals("xianshi"))
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
                        if (goodinfo.getIs_virtual().equals("1")) {//虚拟购买

                            String time = OrderAdapter.getDateToString(Long
                                    .parseLong(goodinfo.getVirtual_indate()) * 1000);
                            virtual_indate.setText("即日起  到  " + time + "每人次限购"
                                    + goodinfo.getVirtual_limit() + "件");
                            is_virtual.setVisibility(View.VISIBLE);
                        }
                        if (goodinfo.getIs_presell().equals("1")) {

                            String time = OrderAdapter.getDateToString(Long
                                    .parseLong(goodinfo
                                            .getPresell_deliverdate()) * 1000);
                            presell_deliverdate_str.setText(time + "日发货");
                            is_presell_Layout.setVisibility(View.VISIBLE);
                        }
                        if (goodinfo.getIs_fcode().equals("1")) {//F码购买
                            pop_add.setVisibility(View.GONE);
                            pop_reduce.setVisibility(View.GONE);
                            is_fcode_Layout.setVisibility(View.VISIBLE);
                        }
                        if (goodinfo.getHave_gift().equals("gift")) { // 赠品
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
                        // 推荐商品
                        if (jsonObject.getJSONArray("goods_commend_list")
                                .length() > 0) {
                            goods_commendList = new ArrayList<GoodsBean>();
                            String goods_commend_array = jsonObject
                                    .getJSONArray("goods_commend_list")
                                    .toString();
                            goods_commendList = gson.fromJson(
                                    goods_commend_array,
                                    new TypeToken<List<GoodsBean>>() {
                                    }.getType());
                            goods_commend_adapter = new GridviewGoodsCommendAdapter(
                                    mContext, 5);
                            goods_commend_adapter.setData(goods_commendList);
                            goods_commend_GridView
                                    .setAdapter(goods_commend_adapter);
                            // giftLayout.setVisibility(View.VISIBLE);
                        }
                        try {
                            // 活动
                            mansong_layout = (LinearLayout) rootView
                                    .findViewById(R.id.mansong_layout);
                            mansongListView = (MyListView) rootView
                                    .findViewById(R.id.listView_mansong_rules);
                            mansong_name = (TextView) rootView
                                    .findViewById(R.id.mansong_name);
                            mansong_info = (TextView) rootView
                                    .findViewById(R.id.mansong_info);
                            mansongList = new ArrayList<Mansong_RulesBean>();
                            JSONObject mansongData = jsonObject
                                    .getJSONObject("mansong_info");

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
                            mansong_layout.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            YkLog.i(TAG, "没有活动内容");
                            e.printStackTrace();
                        }

                        try {
                            // 规格
                            specListView = (MyListView) rootView
                                    .findViewById(R.id.list_spec);
                            SpecnameList = new ArrayList<SpecBean>();
                            spec_list = jsonObject.getJSONObject("spec_list");
                            spec_name = goods_info.optJSONObject("spec_name");
                            spec_value = goods_info.optJSONObject("spec_value");
							/*
							 * TestUtils.println_err("spec_name:",
							 * spec_name.toString());
							 */
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

                            specListView_adapter = new ListViewSpecAdapter(
                                    mContext, product_detail_Fragment.this);
                            specListView_adapter.setData(speclistString);
                            specListView.setAdapter(specListView_adapter);
                            specListView.setVisibility(View.VISIBLE);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //发送EventBus消息
                        EventBus.getDefault().post(new EventGoodmInfo(goods_id, mobile_body));
                        product_info_Layout
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        UIHelper.showProductinfo(mContext,
                                                goods_id, mobile_body);
										/*
										 * Bundle bundle = new Bundle();
										 * bundle.putString("mobile_body",
										 * mobile_body); setArguments(bundle);
										 */
                                    }
                                });
                        store_Layout.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(store_id.equals("126")){
                                    UIHelper.showMarket(mContext);
                                }else {
                                    UIHelper.showStore(mContext, store_id);
                                }
                            }
                        });
                        // 进入店铺
                        store_TextView
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(store_id.equals("126")){
                                            UIHelper.showMarket(mContext);
                                        }else {
                                            UIHelper.showStore(mContext, store_id);
                                        }
                                    }
                                });
                        // 店铺搜索
                        store_search.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UIHelper.showShop(mContext, "", "", store_id,
                                        "");
                            }
                        });
                        final String member_id = store_infoJSON
                                .getString("member_id");
                        final String member_name = store_infoJSON
                                .getString("member_name");
                        // 联系客服
                        store_service.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (UserManager.isLogin()) {
                                    UIHelper.showIMChat(mContext, member_id,
                                            member_name);
                                } else {
                                    ToastUtils.showMessage(mContext, "请登录");
                                    UIHelper.showPersonalActivity(mContext);
                                }
                            }
                        });
                        pop_num.addTextChangedListener(textWatcher);
                        // 购买数量增加按钮
                        pop_add.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String numStr=pop_num.getText() .toString();
                                if("".equals(numStr)){
                                    numStr="0";
                                }
                                int num = Integer.parseInt(numStr);
                                if (num < Integer.parseInt(storage)) {
                                    num++;
                                    pop_num.setText(String.valueOf(num));
                                }
                            }
                        });
                        // 虚拟商品和抢购商品
                        final String is_virtual = goods_info
                                .getString("is_virtual");
                        final int IsHaveBuy = jsonObject.getInt("IsHaveBuy");
                        if (is_virtual.equals("1")
                                || goodinfo.getPromotion_type().equals(
                                "groupbuy")
                                || goodinfo.getIs_fcode().equals("1")) {
//                            cart_add.setVisibility(View.GONE);
                        }
                        // 已经参加过抢购
//                        if (goodinfo.getPromotion_type().equals("groupbuy")
//                                && IsHaveBuy == 1) {
////                            cart_buy.setText("你已参加本商品抢购活动");
////                            cart_buy.setBackgroundColor(Color.GRAY);
////                            cart_buy.setOnClickListener(null);
//                        } else {
//
//                        }

                        String number = pop_num.getText().toString();
                        ((Goods_DetaillActivity)mContext).deliverGoodMessage(store_id, number);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressActivity.showContent();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                try {
                    if (isAdded()) {
                        ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
                        Drawable errorDrawable = mContext.getResources()
                                .getDrawable(R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable, "网络开了小差",
                                "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试", "重新连接",
                                errorClickListener);
                    }
                } catch (NotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                super.onFinish();

            }

        });
    }

    OnClickListener errorClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            progressActivity.showLoading();
            loadOnlineproductdata();
        }
    };

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
                // Yklog.t("spec_listspceID:", spec_listspceID);
            }
            // 布局还原默认值
            product_price_text.setText("价格：");
            product_price.setTextColor(Color.parseColor("#FF0000"));
            product_price.getPaint().setFlags(0);
            promotion_price_layout.setVisibility(View.GONE);
            giftLayout.setVisibility(View.GONE);
            // 跳转规格新goods_id
            goods_id = spec_list.optString(spec_listspceID);
            loadOnlineproductdata();
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
                                UIHelper.showVrBuy1(mContext,
                                        goods_id, pop_num.getText()
                                                .toString());
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


    // 编辑框数量限制
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            try {
                int num = Integer.parseInt(pop_num.getText().toString());
                int storagenum = Integer.parseInt(storage);
                if (num > storagenum) {
                    pop_num.setText(storage);
                }
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            // 清空数字前面输入零情况
            String text = s.toString();
            if (text.length() > 0) {
                String firstNum = text.substring(0, 1);
                if ("0".equals(firstNum)) {
                    s.delete(0, 1);
                }
            }
        }

    };


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
