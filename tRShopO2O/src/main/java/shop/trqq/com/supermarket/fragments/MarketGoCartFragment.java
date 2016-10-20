package shop.trqq.com.supermarket.fragments;


import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.supermarket.activitys.SubmitOrderActivity;
import shop.trqq.com.supermarket.adapters.GoCartListViewAdapter;
import shop.trqq.com.supermarket.bean.GoCartGoods;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketGoCartFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private ListView mMarket_lv;
    private ListView mFresh_lv;
    private View mView_title;
    private ArrayList<GoCartGoods.DatasBean.CartListBean> mMarketData;
    public static final String TAG = MarketGoCartFragment.class.getSimpleName();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==100){

                GoCartGoods goCartGoods = (GoCartGoods) msg.obj;

                List<GoCartGoods.DatasBean.CartListBean> cart_list =  goCartGoods.getDatas().getCart_list();
                // 如果商城购物车有数据
                if(cart_list!=null){

                    mMarketGocart.clear();
                    for (int i = 0; i < cart_list.size(); i++) {
                        // 如果是万能居超市
                        if(cart_list.get(i).getStore_id().equals(126+"")){
                            mMarketGocart.add(cart_list.get(i));
                        }
                    }
                    mGoCartListViewAdapter.addDatas(mMarketGocart);
                }
                if(mMarketData.size()==0){
                    Drawable emptyDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.error_cart);
                    mProgressActivity.showEmpty(emptyDrawable, "您的购物车是空的",
                            "购物车没有商品，快去选购");
                    return;
                }
                // 设置为不可用
                mBtCheck.setEnabled(false);
                mCb_Goods_All.setChecked(false);
                if(mProgressActivity.isLoading()){
                    mProgressActivity.showContent();
                }
            }
        }
    };
    private GoCartListViewAdapter mGoCartListViewAdapter;
    private boolean mIsUse;
    private TextView mTv_goods_change;
    private TextView mStoreName;
    private CheckBox mCb_Goods_All;
    private CheckBox mCheckTitle;
    private TextView mTvPrice;
    private Button mBtCheck;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<String> mCartIdList;
    private Gson mGson;
    private ProgressActivity mProgressActivity;
    private boolean mIsAll = true;
    private boolean mIsAllSelected;
    private StringBuilder mBuilder;
    private ArrayList<GoCartGoods.DatasBean.CartListBean> mMarketGocart;

    public MarketGoCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market_go_cart, container, false);

        initView(view);

        initData();
        setData();

        setListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        mProgressActivity.showLoading();
        loadNetworkData();
    }

    private void setListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNetworkData();
            }
        });
        // 店铺名监听事件
        mStoreName.setOnClickListener(this);
        // 点击编辑
        mTv_goods_change.setOnClickListener(this);

        // 点击全选
        mCb_Goods_All.setOnCheckedChangeListener(this);
        // 点击头部的CheckBox
        mCheckTitle.setOnCheckedChangeListener(this);

        // 当改变数据是就会调用这个方法去改变总的金额
        mGoCartListViewAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                String money = String.format("%.1f", getMoney());
                Log.d("fff",money);
                mTvPrice.setText("总计："+ money +"元");

            }
        });

        // 点击结算按钮跳转到结算界面
        mBtCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cart_id = getStringCartId();
                Log.d(TAG, "onClick: "+cart_id);
                Intent intent = new Intent(getActivity(),SubmitOrderActivity.class);
                intent.putExtra("cart_id",cart_id);
                intent.putExtra("ifcart", "1");
                startActivity(intent);
            }
        });
        // 点击跳转到商品的详情页面
        mMarket_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UIHelper.showGoods_Detaill(getActivity(),mMarketData.get(position-1).getGoods_id());
            }
        });
    }


    private String getStringCartId(){
        String string = null;
        mBuilder.delete(0,mBuilder.length());
        for (int i = 0; i < mMarketData.size(); i++) {
            boolean isCheck = mMarketData.get(i).getIsCheck();
            if (isCheck){
                mBuilder.append(","+mMarketData.get(i).getCart_id()+ "|" + mMarketData.get(i).getGoods_num());
            }
        }
        string = mBuilder.deleteCharAt(0).toString();
        return  string;
    }

    // 设置选中的状态
    private void setCheckState() {
        int m = 0 ;
        for (int i = 0; i < mMarketData.size(); i++) {
            if(mMarketData.get(i).getIsCheck()){
                m++;
            }
        }
        // 被选中
        if(m>0){
            mBtCheck.setEnabled(true);
            mBtCheck.setText("结算（"+m+")");
            if(m==mMarketData.size()){  // 全部被选中
                mIsAllSelected = true;
                mIsAll = true;
                mCb_Goods_All.setChecked(true);
                mCheckTitle.setChecked(true);
            }else {
                if (mIsAllSelected){
                    mIsAll = false;
                }else {
                    mIsAll = true;
                }
                mIsAllSelected=false;
                mCb_Goods_All.setChecked(false);
                mCheckTitle.setChecked(false);
            }
        }else {
            mIsAllSelected= false;
            mIsAll = true;
            mBtCheck.setEnabled(false);
            mBtCheck.setText("结算（0）");
            mCb_Goods_All.setChecked(false);
            mCheckTitle.setChecked(false);
        }
    }

    // 初始化数据
    private void initData() {

        mBuilder = new StringBuilder();
        mGson = new Gson();

        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLACK,getResources().getColor(R.color.mainColor));

        // 购物车数据
        mMarketData = new ArrayList<>();

        // 超市的商品
        mMarketGocart = new ArrayList<>();
        mCartIdList = new ArrayList<>();
        // 给ListView添加头部布局
        mMarket_lv.addHeaderView(mView_title);
//        mFresh_lv.addHeaderView(mView_title);

        mGoCartListViewAdapter = new GoCartListViewAdapter(getActivity(),mMarketData,this,this);

    }

    // 下载网络购物车数据
    private void loadNetworkData() {


        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();

        Log.d(TAG, "loadNetworkData: "+key);
        requestParams.add("key",key);

        HttpUtil.post(HttpUtil.URL_CART_LIST, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);

                if (string != null) {

                    GoCartGoods GoCartGoods = mGson.fromJson(string, GoCartGoods.class);

                    Message message = new Message();
                    message.obj = GoCartGoods;
                    message.what = 100;
                    mHandler.sendMessage(message);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Drawable errorDrawable = getResources().getDrawable(R.drawable.wifi_off);
                mProgressActivity.showError(errorDrawable,"网络开了小差",
                        "连接不上网络，请确认一下您的网络开关，或者服务器网络正忙，请稍后再试","重新连接",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                mProgressActivity.showLoading();
                                loadNetworkData();
                            }
                        });
            }

            @Override
            public void onFinish() {

                if (mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void setData() {

        mMarket_lv.setAdapter(mGoCartListViewAdapter);
        Drawable drawable = getResources().getDrawable(R.drawable.selector_listview_item);
        mMarket_lv.setSelector(drawable);

    }

    // 初始化视图
    private void initView(View view) {

        mProgressActivity = (ProgressActivity) view.findViewById(R.id.market_gocart_progress);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);

        mView_title = LayoutInflater.from(getActivity()).inflate(R.layout.cart_title, null);
        mTv_goods_change =(TextView) mView_title.findViewById(R.id.goods_chang);
        mStoreName = (TextView)mView_title.findViewById(R.id.gocart_lv_title);

        mMarket_lv = (ListView) view.findViewById(R.id.market_goods_lv);
//        mFresh_lv = (ListView) view.findViewById(R.id.market_fresh_lv);

        mCb_Goods_All = (CheckBox)view.findViewById(R.id.cb_gocart_all);
        mCheckTitle = (CheckBox)mView_title.findViewById(R.id.gocart_market_all);

        mTvPrice = (TextView)view.findViewById(R.id.tv_gocart_price);

        mBtCheck = (Button)view.findViewById(R.id.bt_main_ensure);
    }

    // 得到商品的总价
    private  Double getMoney()
    {
        Double money = 0.0;
        for (int i = 0; i < mMarketData.size(); i++) {

            if (mMarketData.get(i).getIsCheck())
            {
                int count=Integer.parseInt(mMarketData.get(i).getGoods_num());
                Double price=Double.parseDouble(mMarketData.get(i).getGoods_price());
                Double itemMoney=count*price;
                money+=itemMoney;
            }
        }
        return money;
    }

    // 点击事件回调方法
    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.gocart_lv_title:

                    break;

                case R.id.goods_chang:
                    mIsUse=!mIsUse;
                    if(mIsUse){
                        mTv_goods_change.setText("完成");
                    }else {
                        mTv_goods_change.setText("编辑");
                    }
                    mGoCartListViewAdapter.setDelete(mIsUse);
                    mGoCartListViewAdapter.notifyDataSetChanged();
                    break;

                // 点击添加数量
                case R.id.bt_item_add:

                    Integer addPosition = (Integer) v.getTag();
                    GoCartGoods.DatasBean.CartListBean cartListBean = mMarketData.get(addPosition);
                    cartListBean.setIsCheck(true);
                    int i = Integer.parseInt(cartListBean.getGoods_num());
                    cartListBean.setGoods_num(i+1+"");
                    // 向服务器提交数量
                    String cart_id2 = cartListBean.getCart_id();
                    cart_edit_quantity(cart_id2,i+1+"");
                    mGoCartListViewAdapter.notifyDataSetChanged();
                    break;

                // 点击减少数量
                case R.id.bt_item_sub:

                    Integer subPosition = (Integer) v.getTag();
                    GoCartGoods.DatasBean.CartListBean cartListBean1 = mMarketData.get(subPosition);
                    String cart_id = cartListBean1.getCart_id();
                    // 设置为选中状态
                    cartListBean1.setIsCheck(true);
                    int num = Integer.parseInt(cartListBean1.getGoods_num());
                    if(num<2){
                        cartListBean1.setGoods_num(1+"");
                        // 向服务器提交数量
                        cart_edit_quantity(cart_id,1+"");
                    }else {
                        cartListBean1.setGoods_num((num-1)+"");

                        cart_edit_quantity(cart_id,(num-1)+"");
                    }
                    mGoCartListViewAdapter.notifyDataSetChanged();

                    break;

                // 点击删除按钮，通过后台删除相应的条目
                case R.id.tv_goods_delete:

                    Integer position= (Integer) v.getTag();
                    if (position != null) {

                        GoCartGoods.DatasBean.CartListBean cartListBean2 = mMarketData.get(position);

                        mMarketData.remove(cartListBean2);
                        Log.d("bbbbbb",mMarketData.size()+"");
                        if(mMarketData.size()==0){
                            Drawable emptyDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.error_cart);
                            mProgressActivity.showEmpty(emptyDrawable, "您的购物车是空的",
                                    "购物车没有商品，快去选购");
                        }
                        String cart_id1 = cartListBean2.getCart_id();
                        mGoCartListViewAdapter.notifyDataSetChanged();
                        // 删除购物车中的指定的商品
                        cartDetele(cart_id1);
                    }
                    break;
            }
        }
    }


    // CheckBox 状态改变时就会回调这个方法
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.gocart_market_all:
                if(mIsAll){  // 设置全选按钮有效
                    mCb_Goods_All.setChecked(isChecked);
                    for (int i = 0; i < mMarketData.size(); i++) {
                        mMarketData.get(i).setIsCheck(isChecked);
                    }
                }else {
                    mIsAll = true;
                }
                mGoCartListViewAdapter.notifyDataSetChanged();
                break;

            // 全选CheckBox
            case R.id.cb_gocart_all:
                mCheckTitle.setChecked(isChecked);

                break;

            case R.id.cb_item_check:

                Integer position = (Integer) buttonView.getTag();
                GoCartGoods.DatasBean.CartListBean cartListBean = mMarketData.get(position);
                cartListBean.setIsCheck(isChecked);

                // 设置选中状态
                setCheckState();
                mGoCartListViewAdapter.notifyDataSetChanged();
                break;
        }
    }

    // 改变商品的数量时就会提交
    private void cart_edit_quantity(String cart_id, String quantity) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key",key);
        requestParams.add("cart_id", cart_id);
        requestParams.add("quantity", quantity);
        HttpUtil.post(HttpUtil.URL_CART_EDIT_QUANTITY, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);

                if (string != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            Toast.makeText(getActivity(), errStr, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                    }
                }
            }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
    }

    // 删除指定的商品
    private void cartDetele(String cart_id){
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key",key);
        requestParams.add("cart_id", cart_id);

        HttpUtil.post(HttpUtil.URL_CART_DETELE, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonString);
                    String errStr = jsonObject.getString("error");
                    if (errStr != null) {
                        Toast.makeText(getActivity(), errStr, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    try {
                        if (jsonObject.getString("datas")
                                .equals("1")) {
                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
    }

}
