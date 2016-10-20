package shop.trqq.com.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

import shop.trqq.com.R;
import shop.trqq.com.adapter.ListViewHomeAdapter;
import shop.trqq.com.bean.HomeBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.YkLog;

/**
 * @author Weiss
 *ר�����
 **/
public class SpecialActivity extends BaseActivity implements
        OnItemClickListener {

    private static final String TAG = "SpecialActivity";

    private Context mContext;
    // Gson����
    private Gson gson;
    private String special_id;
    /*
     * private LoopViewPager loopViewPager; private ArrayList<String> imageUris;
     */
    // ����������
    private TextView mHeadTitleTextView;

    private ListViewHomeAdapter listViewHomeAdapter;
    // ��Ѷ���ش���ͼ��
    private ProgressActivity progressActivity;
    private PullToRefreshListView mHomePullToRefreshListView;

    private boolean isEnabledScrollLast = true;
    /**
     * Ҫ��ʾ����ʵ����.
     */

    private List<HomeBean> homeDataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        gson = new Gson();
        mContext = this;
        Intent intent = getIntent();
        try {
            special_id = intent.getStringExtra("special_id");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        homeDataList = new ArrayList<HomeBean>();
        initTitleBarView();
        initViews();
    }

    @Override
    public void onStop() {
        super.onStop();
        listViewHomeAdapter.stopAd();
    }

    @Override
    public void onResume() {
        super.onResume();
        listViewHomeAdapter.startAd();
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        YkLog.i(TAG, "��ʼ����������ͼ");
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadTitleTextView.setText("ר��");
    }

    private void initViews() {
        listViewHomeAdapter = new ListViewHomeAdapter(mContext, new Handler(),
                false);
        progressActivity = (ProgressActivity) findViewById(R.id.PullToRefreshListView_progress);
        mHomePullToRefreshListView = (PullToRefreshListView) findViewById(R.id.home_pullToRefreshListView);
        // mHomePullToRefreshListView.getRefreshableView().addFooterView(footView);
        mHomePullToRefreshListView.setPullToRefreshOverScrollEnabled(false);
        mHomePullToRefreshListView.setOnItemClickListener(this);
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

    public void loadData() {
        mHomePullToRefreshListView.setRefreshing(false);
    }

    private void loadOnlineInformationData() {
        loadData();
        // ������Ѷ����
        RequestParams requestParams = new RequestParams();
        // requestParams.add("information_type_id", informationTypeId);
        // String uri
        // =special_id.equals("")?HttpUtil.URL_HOME:HttpUtil.URL_SPECIAL;
        String uri;
        if (special_id == null) {
            uri = HttpUtil.URL_HOME;
        } else {
            uri = HttpUtil.URL_SPECIAL;
            requestParams.add("special_id", special_id);
            requestParams.add("type", "json");
        }
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    // System.out.println(jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    // refreshTime = jsonObject.getLong("refreshTime");
                    String data = jsonObject.getString("datas");

                    homeDataList = gson.fromJson(data,
                            new TypeToken<List<HomeBean>>() {
                            }.getType());
                    /*
					 * AppConfig.setSharedPreferences(getActivity(), configKey,
					 * data);
					 */
                    // System.out.println(homeDataList.size());
                    listViewHomeAdapter.setData(homeDataList);
                    listViewHomeAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (homeDataList.size() > 0) {
                    progressActivity.showContent();
                } else {
                    Drawable emptyDrawable = getResources().getDrawable(
                            R.drawable.ic_empty);
                    progressActivity.showEmpty(emptyDrawable, "ר����ʱû����", "");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                try {
                    if (homeDataList.size() == 0) {
                        Drawable errorDrawable = getResources().getDrawable(
                                R.drawable.wifi_off);
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
                mHomePullToRefreshListView.onRefreshComplete();
                listViewHomeAdapter.startAd();
            }

        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }
}
