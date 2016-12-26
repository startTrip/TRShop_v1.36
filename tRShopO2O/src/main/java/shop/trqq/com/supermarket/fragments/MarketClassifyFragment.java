package shop.trqq.com.supermarket.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.LinkedList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.supermarket.bean.ClassifyData;
import shop.trqq.com.util.HttpUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketClassifyFragment extends Fragment implements View.OnClickListener {


    private Gson mGson;

    public MarketClassifyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market_classify, container, false);

        initView(view);
        initData();
        setData();

        setListener();
        return view;
    }

    private LinkedList<String> mLeftData;
    private LinearLayout mLinearLayout;
    private LinkedList<ClassifyRightFragment> mFragments;
    private List<ClassifyData.DatasBean> mListItems;

    private void initView(View view) {

        mLinearLayout = (LinearLayout)view.findViewById(R.id.classify_left_layout);

    }

    private void initData() {
        mGson = new Gson();

        mLeftData = new LinkedList<>();

        mFragments = new LinkedList<>();
        getData();
    }


    private void getData() {

        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key",key);

        HttpUtil.post(HttpUtil.URL_MARKET_CLASSIFY, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                ClassifyData classifyData = mGson.fromJson(response, ClassifyData.class);
                if (classifyData != null) {

                    mListItems = classifyData.getDatas();
                    if ( mListItems!= null) {

                        int size = mListItems.size();
                        for (int i = 0; i < size; i++) {
                            // �õ�����
                            mLeftData.add(mListItems.get(i).getStc_name());
                        }
                        addViewToLayout();
                        setFragment();

                        // ��ʼ������ Ĭ��ѡ�е�һ��Fragment
                        setTextColor(0);
                        switchFragment(0);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void setFragment() {

        if (mLeftData != null) {

            for (int i = 0; i < mLeftData.size(); i++) {
                ClassifyRightFragment classifyRightFragment = new ClassifyRightFragment();
                mFragments.add(classifyRightFragment);
            }
            // �õ�Fragment ������
            FragmentManager childFragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();

            for (int i = 0; i < mFragments.size(); i++) {

                ClassifyRightFragment fragment = mFragments.get(i);
                fragmentTransaction.add(R.id.classify_fragment_container, fragment,i+"");
                ClassifyData.DatasBean datasBean = mListItems.get(i);
                Bundle bundle = new Bundle();
                bundle.putSerializable("listItemsBean",datasBean);
                fragment.setArguments(bundle);
                fragmentTransaction.hide(fragment);
            }
            fragmentTransaction.show(mFragments.get(0));
            fragmentTransaction.commit();
        }
    }

    // ��TextView ��ӵ�������ȥ
    private void addViewToLayout() {
        // ��ӵ� ��ߵ� Layout��ȥ
        int size = mLeftData.size();
        for (int i = 0; i < size; i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.market_classify_left_item,null);
            TextView textView = (TextView) view.findViewById(R.id.tv_item_classify);
            // ���õ�������¼�
            view.setOnClickListener(this);
            textView.setText(mLeftData.get(i));
            mLinearLayout.addView(view);
        }
    }

    private void setData() {

    }


    private void setListener() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // textView�ĵ���¼�
    @Override
    public void onClick(View v) {

        int i = mLinearLayout.indexOfChild(v);

        // �����������ɫ
        setTextColor(i);

        // ��ʾ��ͬ��Fragment
        switchFragment(i);
    }

    // �л���ͬ��Fragment
    private void switchFragment(int index) {

        if(index>=0&& index<mFragments.size()){
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            for (int i = 0; i < mFragments.size(); i++) {
                ClassifyRightFragment classifyRightFragment = mFragments.get(i);
                if(i== index){
                    fragmentTransaction.show(classifyRightFragment);
                }else {
                    fragmentTransaction.hide(classifyRightFragment);
                }
            }
            fragmentTransaction.commit();
        }
    }

    /**
     *  �����������ɫ
     * @param position ѡ�е�position
     */
    private void setTextColor(int position) {
        // ��TextView Ϊ��ѡ�е�״̬
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            LinearLayout layout= (LinearLayout) mLinearLayout.getChildAt(i);
            layout.setBackgroundColor(Color.parseColor("#f1eeee"));
            RelativeLayout childAt = (RelativeLayout) layout.getChildAt(0);
            ((TextView)childAt.getChildAt(0)).setTextColor(Color.BLACK);
            childAt.getChildAt(1).setBackgroundColor(Color.parseColor("#f1eeee"));
        }
        // �õ����Ϊѡ�е�״̬
        LinearLayout layout= (LinearLayout) mLinearLayout.getChildAt(position);
        layout.setBackgroundColor(Color.WHITE);
        RelativeLayout childAt = (RelativeLayout) layout.getChildAt(0);
        ((TextView)childAt.getChildAt(0)).setTextColor(Color.parseColor("#ff552d"));
        childAt.getChildAt(1).setBackgroundColor(Color.parseColor("#ff552d"));
    }
}
