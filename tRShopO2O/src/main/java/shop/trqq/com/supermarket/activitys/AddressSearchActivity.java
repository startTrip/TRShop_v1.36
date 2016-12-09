package shop.trqq.com.supermarket.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.vlonjatg.progressactivity.ProgressActivity;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.supermarket.adapters.AddressListAdapter;
import shop.trqq.com.ui.Address_newActivity;

public class AddressSearchActivity extends AppCompatActivity implements OnGetSuggestionResultListener {

    private static final String TAG = AddressSearchActivity.class.getSimpleName();
    private ProgressActivity mProgressActivity;
    private ListView mListView;
    private SuggestionSearch mSuggestionSearch;
    private EditText mSearch;
    private ArrayList<SuggestionResult.SuggestionInfo> mSearchData;
    private String mCity;
    private Context mContext;
    private AddressListAdapter mAddressListAdapter;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);

        initView();

        initData();

        setListener();
    }

    private void initView() {

        mProgressActivity = (ProgressActivity)findViewById(R.id.search_progress);
        mListView = (ListView) findViewById(R.id.list_view);
        mSearch = (EditText) findViewById(R.id.search_tv);
    }


    private void initData() {

        mContext = AddressSearchActivity.this;
        Intent intent = getIntent();
        mCity = intent.getStringExtra("city");

        mBack = (ImageView) findViewById(R.id.title_back);

        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        mSearchData = new ArrayList<>();

        mAddressListAdapter = new AddressListAdapter(mContext,mSearchData);
        mListView.setAdapter(mAddressListAdapter);

    }


    private void setListener() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext,Address_newActivity.class);
                intent.putExtra("address",mSearchData.get(i));
                startActivity(intent);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
            }
        });

        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        mSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                if(!TextUtils.isEmpty(mCity)){
                    mProgressActivity.showLoading();
                    mSuggestionSearch
                            .requestSuggestion((new SuggestionSearchOption())
                                    .keyword(cs.toString()).city(mCity));
                }
            }
        });

    }


    // 收到建议结果的时候监听
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

        if(mProgressActivity.isLoading()){
            mProgressActivity.showContent();
        }
        if(suggestionResult.error == SearchResult.ERRORNO.NETWORK_ERROR){
            showNetWorkError();
        }
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            Log.d("suggest","suggestion");
            return;
        }

        List<SuggestionResult.SuggestionInfo> allSuggestions = suggestionResult.getAllSuggestions();
        Log.d(TAG, "onGetSuggestionResult: "+allSuggestions.size());
        mAddressListAdapter.addDatas(allSuggestions);

    }

    // 显示网络异常错误
    private void showNetWorkError() {
        Drawable errorDrawable = ContextCompat.getDrawable(mContext, R.drawable.wifi_off);
        mProgressActivity.showError(errorDrawable,"您的手机网络不太顺畅哦",
                "","重新加载",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // 重新加载地图
                        /**
                         * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                         */
                        if(!TextUtils.isEmpty(mCity)){
                            mProgressActivity.showLoading();
                            mSuggestionSearch
                                    .requestSuggestion((new SuggestionSearchOption())
                                            .keyword(mSearch.getText().toString()).city(mCity));
                        }
                    }
                });
    }
}
