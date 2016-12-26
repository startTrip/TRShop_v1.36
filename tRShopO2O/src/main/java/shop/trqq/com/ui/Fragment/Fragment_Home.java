package shop.trqq.com.ui.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zxing.activity.CaptureActivity;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.adapter.ListViewHomeAdapter;
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.bean.HomeBean;
import shop.trqq.com.bean.Home_goods;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.ui.MainTabActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * ��ҳ
 */
public class Fragment_Home extends Fragment {

    private static final String TAG = "FragmentPage1";
    private View rootView;// ����Fragment view
    private Context mContext;
    // Gson����
    private Gson gson;

    // ����������
    private TextView mHeadTitleTextView;
    private ImageView mHeadLeftImg;
    private EditText mHeadEditText;
    private ImageView mHeadRightImg;
    private LinearLayout mLeftLayout;
    private LinearLayout mRightLayout;
    private ViewGroup parent;
    private ListViewHomeAdapter listViewHomeAdapter;
    // ���ؽ���Activity
    private ProgressActivity progressActivity;
    private PullToRefreshListView mHomePullToRefreshListView;
    // ���ظ��࣬�Ų�����
    private View footView;
    private Button mLoadMoreButton;
    private LayoutInflater inflater;
    private boolean isEnabledScrollLast = true;
    /**
     * Ҫ��ʾ����ʵ����.
     */

    private List<HomeBean> homeDataList;

    private int informationListIndex = 0;
    private int position;

    private long refreshTime;
    private Handler mHandler;
    private MainTabActivity mMainTabActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainTabActivity = (MainTabActivity) activity;
        mHandler = mMainTabActivity.setHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        mContext = getActivity();
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            gson = new Gson();
            homeDataList = new ArrayList<HomeBean>();
            initTitleBarView();
            initViews();
        }
        // �����rootView��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�������parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
        parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            listViewHomeAdapter.startAd();
        } else {
            listViewHomeAdapter.stopAd();
        }
    }


    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        YkLog.i(TAG, "��ʼ����������ͼ");
        mHeadLeftImg = (ImageView) rootView
                .findViewById(R.id.head_left_imageView);
        mHeadRightImg = (ImageView) rootView
                .findViewById(R.id.head_right_imageView);
        mLeftLayout = (LinearLayout) rootView
                .findViewById(R.id.head_left_img_linearLayout);
        mRightLayout = (LinearLayout) rootView
                .findViewById(R.id.head_right_img_linearLayout);
        mHeadEditText = (EditText) rootView.findViewById(R.id.head_search_edit);
        // mHeadLeftImg.setImageResource(R.drawable.home_logo);
        mLeftLayout.setVisibility(View.VISIBLE);

        mHeadEditText.setVisibility(View.VISIBLE);
        mHeadRightImg.setImageResource(R.drawable.barcode_normal);
        // mHeadRightImg.setVisibility(View.VISIBLE);
        mRightLayout.setVisibility(View.VISIBLE);
        mHeadEditText.setFocusable(false);
        mHeadEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // ��ת����Fragment��ʽ
				/*
				 * Message msg3 = new Message(); msg3.what = 2;
				 * mHandler.sendMessage(msg3);
				 */
                // ��ת����Activity��ʽ
                UIHelper.showSearch(mContext);
            }
        });
        mHeadRightImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(getActivity(),
                        CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });
        LinearLayout tLinearLayout=(LinearLayout) rootView
                .findViewById(R.id.header_relativelayout);
//        SystemBarHelper.immersiveStatusBar(getActivity(),0);
//        SystemBarHelper.setHeightAndPadding(getActivity(), tLinearLayout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            //ToastUtils.showMessage(getActivity(), scanResult);
            try {
                //��ά����Ʒ����
                String a[] = scanResult.split("goods_id=");
                UIHelper.showGoods_Detaill(mContext, a[1]);
            } catch (Exception e) {
                try {
                    //��ά���������
                    String s[] = scanResult.split("store_id=");
                    UIHelper.showStore(mContext, s[1]);
                } catch (Exception ee) {
                    if ("http://shop.trqq.com".equals(scanResult)) {
                        ToastUtils.showMessage(mContext, "�ܱ�Ǹ���õ��̻�δ���¶�ά��");
                    } else {
                        ToastUtils.showMessage(mContext, "ɨ�������ȷ����̩��ٷ���ά��");
                    }
                }
            }
        }
    }

    private void initViews() {
        listViewHomeAdapter = new ListViewHomeAdapter(mContext, mHandler, true);
        footView = inflater.inflate(R.layout.item_load_more, null);
        mLoadMoreButton = (Button) footView.findViewById(R.id.loadMore_button);
        progressActivity = (ProgressActivity) rootView
                .findViewById(R.id.PullToRefreshListView_progress);
        mHomePullToRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.home_pullToRefreshListView);
        // mHomePullToRefreshListView.getRefreshableView().addFooterView(footView);
        mHomePullToRefreshListView.setPullToRefreshOverScrollEnabled(false);
        mHomePullToRefreshListView.setAdapter(listViewHomeAdapter);
        progressActivity.showLoading();
        loadOnlineInformationData();
        mHomePullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        listViewHomeAdapter.stopAd();
                        loadOnlineInformationData();
                    }
                });

        mHomePullToRefreshListView
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
                    @Override
                    public void onLastItemVisible() {
                        if (isEnabledScrollLast) {
                            // footView.setVisibility(View.VISIBLE);
                            // loadMoreInformationData("1");
                        }
                    }
                });

    }

    /**
     * ������������
     */
    public void loadData() {
        mHomePullToRefreshListView.setRefreshing(false);
    }



    /**
     * ������ҳ����
     */
    private void loadOnlineInformationData() {
        // ������ҳ����
        RequestParams requestParams = new RequestParams();
        String uri = HttpUtil.URL_HOME;
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    // mHomePullToRefreshListView.setVisibility(View.VISIBLE);
                    String jsonString = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String data = jsonObject.getString("datas");
                    homeDataList = gson.fromJson(data,
                            new TypeToken<List<HomeBean>>() {
                            }.getType());
					/*
					 * AppConfig.setSharedPreferences(getActivity(), configKey,
					 * data);
					 */
                    //homeDataList.add(other);
                    loadOnlineGoods_listData("��Ʒ�Ƽ�", "4");
                    listViewHomeAdapter.setData(homeDataList);

                    listViewHomeAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (homeDataList.size() > 0) {
                    progressActivity.showContent();
                } else {
                    Drawable emptyDrawable =ContextCompat.getDrawable(mContext, R.drawable.ic_empty);
                    progressActivity.showEmpty(emptyDrawable, "��ҳ��ʱû����", "");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    if (homeDataList.size() == 0) {
                        Drawable errorDrawable =ContextCompat.getDrawable(mContext, R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable, "���翪��С��",
                                "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����", "��������",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        progressActivity.showLoading();
                                        homeDataList.clear();
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
                // ���ܳɹ�����ʧ�ܣ���Ҫ���������رյ�
                mHomePullToRefreshListView.onRefreshComplete();
                listViewHomeAdapter.startAd();
            }

        });

    }

    private void loadOnlineGoods_listData(final String title, final String key) {
        //http://shop.trqq.com/mobile/index.php?act=goods&op=goods_list&page=6&key=1 ����
        //http://shop.trqq.com/mobile/index.php?act=goods&op=goods_list&page=6&key=4 ��Ʒ
        RequestParams requestParams = new RequestParams();
//		requestParams.add("page", "6");
//		requestParams.add("key", key + "");
//		requestParams.add("salesrank","1");
        String uri;
        if ("1".equals(key)) {//��������
            uri = HttpUtil.URL_HOME_RANK;
        } else {
            uri = HttpUtil.URL_HOME_NEW;
        }
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    // mHomePullToRefreshListView.setVisibility(View.VISIBLE);
                    String jsonString = new String(responseBody);
                    if ("4".equals(key)) {
                        //�˹����ܽ���json
                        jsonString = jsonString.replaceAll("pic_list", "goodlist");
                        jsonString = jsonString.replaceAll("pic_url", "goods_id");
                        jsonString = jsonString.replaceAll("pic_img", "goods_pic");
                    }
                    YkLog.t("URL_GOODSLIST" + key, jsonString);
                    String goodlist = new JSONObject(jsonString)
                            .getString("goodlist");
                    //String goods_list = jsonObjects.getString("goods_list");
                    List<GoodsBean> goodList = gson.fromJson(goodlist,
                            new TypeToken<List<GoodsBean>>() {
                            }.getType());
					/*
					 * AppConfig.setSharedPreferences(getActivity(), configKey,
					 * data);
					 */
                    HomeBean other = new HomeBean();
                    Home_goods other_goods = new Home_goods();
                    other_goods.setTitle(title);
                    other_goods.setItem(goodList);
                    other.setOther2(other_goods);
                    homeDataList.add(2, other);
                    //homeDataList.add(other);
                    listViewHomeAdapter.setData(homeDataList);
                    listViewHomeAdapter.notifyDataSetChanged();
                    informationListIndex += homeDataList.size();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    if (homeDataList.size() == 0) {
                        Drawable errorDrawable = ContextCompat.getDrawable(mContext, R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable, "���翪��С��",
                                "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����", "��������",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        progressActivity.showLoading();
                                        homeDataList.clear();
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
                loadOnlineGoods_listData2("��������", "1");
            }

        });

    }

    /**
     * ��������
     * @param title
     * @param key
     */
    private void loadOnlineGoods_listData2(final String title, final String key) {
        //http://shop.trqq.com/mobile/index.php?act=goods&op=goods_list&page=6&key=1 ����
        //http://shop.trqq.com/mobile/index.php?act=goods&op=goods_list&page=6&key=4 ��Ʒ
        RequestParams requestParams = new RequestParams();
//		requestParams.add("page", "6");
//		requestParams.add("key", key + "");
//		requestParams.add("salesrank","1");
        String uri = HttpUtil.URL_HOME_RANK;
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    // mHomePullToRefreshListView.setVisibility(View.VISIBLE);
                    String jsonString = new String(responseBody);
                    YkLog.t("URL_GOODSLIST" + key, jsonString);
                    String goodlist = new JSONObject(jsonString)
                            .getString("goodlist");
                    //String goods_list = jsonObjects.getString("goods_list");
                    List<GoodsBean> goodList = gson.fromJson(goodlist,
                            new TypeToken<List<GoodsBean>>() {
                            }.getType());
					/*
					 * AppConfig.setSharedPreferences(getActivity(), configKey,
					 * data);
					 */
                    HomeBean other = new HomeBean();
                    Home_goods other_goods = new Home_goods();
                    other_goods.setTitle(title);
                    other_goods.setItem(goodList);
                    other.setOther(other_goods);
                    homeDataList.add(2, other);
                    //homeDataList.add(other);
                    listViewHomeAdapter.setData(homeDataList);
                    listViewHomeAdapter.notifyDataSetChanged();
                    informationListIndex += homeDataList.size();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    if (homeDataList.size() == 0) {
                        Drawable errorDrawable = ContextCompat.getDrawable(mContext, R.drawable.wifi_off);
                        progressActivity.showError(errorDrawable, "���翪��С��",
                                "���Ӳ������磬��ȷ��һ���������翪�أ����߷�����������æ�����Ժ�����", "��������",
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        progressActivity.showLoading();
                                        homeDataList.clear();
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
            }

        });

    }

}
