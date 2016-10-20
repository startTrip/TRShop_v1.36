package shop.trqq.com.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.ListViewHistoryAdapter;
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.ui.Base.BaseActivity;
import shop.trqq.com.util.YkLog;

/**
 * �����¼����
 */
public class HistoryListActivity extends BaseActivity {
    private static final String TAG = "HistoryListActivity";

    // ȫ��Context
    private Context mContext;
    /*
     * private LoopViewPager loopViewPager; private ArrayList<String> imageUris;
     */
    // ����������
    private TextView mHeadTitleTextView;
    private TextView mHeadRightTextView;

    private ListViewHistoryAdapter listViewGoodAdapter;
    // ��Ѷ���ش���ͼ��
    private ProgressActivity progressActivity;
    private PullToRefreshListView mHomePullToRefreshListView;

    /**
     * Ҫ��ʾ����ʵ����.
     */
    private List<GoodsBean> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulltorefreshlistview);
        mContext = this;
        historyList = new ArrayList<GoodsBean>();
        initTitleBarView();
        initViews();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        loadhistoryListData();
        super.onResume();
    }

    /**
     * ��ʼ����������ͼ
     */
    private void initTitleBarView() {
        YkLog.i(TAG, "��ʼ����������ͼ");
        mHeadTitleTextView = (TextView) findViewById(R.id.head_title_textView);
        mHeadRightTextView = (TextView) findViewById(R.id.head_right_textView);
        mHeadTitleTextView.setText("�����¼�б�");
        mHeadRightTextView.setText("���");
        mHeadRightTextView.setVisibility(View.VISIBLE);
        mHeadRightTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                historyList.clear();
                UserManager.setHistoryList(mContext, historyList);
                //AppConfig.setSharedPreferences(mContext, "historyList", "");
                onResume();
            }
        });
    }

    private void initViews() {
        listViewGoodAdapter = new ListViewHistoryAdapter(mContext);
        progressActivity = (ProgressActivity) findViewById(R.id.PullToRefreshListView_progress);
        progressActivity.showLoading();
        mHomePullToRefreshListView = (PullToRefreshListView) findViewById(R.id.shop_pullToRefreshListView);

        mHomePullToRefreshListView.setPullToRefreshOverScrollEnabled(false);
        mHomePullToRefreshListView.setAdapter(listViewGoodAdapter);
    }

    /**
     * ������������
     *
     * @param object
     */
    public void loadData() {
        mHomePullToRefreshListView.setRefreshing(false);
    }

    private void loadhistoryListData() {
        historyList = UserManager.getHistoryList();
        listViewGoodAdapter.setData(historyList);
        listViewGoodAdapter.notifyDataSetChanged();
        progressActivity.showContent();
        if (historyList.size() == 0) {
            Drawable emptyDrawable = getResources().getDrawable(
                    R.drawable.ic_empty);
            progressActivity.showEmpty(emptyDrawable, "��û�������¼\nȥ����ٻ�����", "");
        }
        // mHomePullToRefreshListView.onRefreshComplete();
    }

}
