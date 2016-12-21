package shop.trqq.com.supermarket.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vlonjatg.progressactivity.ProgressActivity;

import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.supermarket.adapters.ClassifyRightAdapter;
import shop.trqq.com.supermarket.bean.ClassifyData;
import shop.trqq.com.ui.Base.UIHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassifyRightFragment extends Fragment implements ClassifyRightAdapter.OnClassifyRightClick {


    private RecyclerView mRecyclerView;
    private List<ClassifyData.DatasBean.ChildrenBean> mSonItemsList;
    private ClassifyRightAdapter mClassifyRightAdapter;
    private ProgressActivity mProgressActivity;

    public ClassifyRightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.market_fragment_classify_right, container, false);

        initView(view);
        initData();
        setData();
        setListener();
        return view;
    }


    private void initView(View view) {

        mRecyclerView = (RecyclerView)view.findViewById(R.id.classify_right_rv);
        mProgressActivity = (ProgressActivity)view.findViewById(R.id.market_classify_progress);
    }

    private void initData() {
        mProgressActivity.showLoading();
        // 根据不同的值 进行不同数据的展示
        Bundle bundle = getArguments();
        ClassifyData.DatasBean listItemsBean = (ClassifyData.DatasBean)bundle.getSerializable("listItemsBean");
        if (listItemsBean != null) {
            mSonItemsList = listItemsBean.getChildren();
        }
        mClassifyRightAdapter = new ClassifyRightAdapter(getActivity(),mSonItemsList);
    }

    private void setData() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mRecyclerView.setAdapter(mClassifyRightAdapter);
        if(mProgressActivity.isLoading()){
            mProgressActivity.showContent();
        }
    }


    private void setListener() {
//         设置回调监听
        mClassifyRightAdapter.setOnClassifyRightClick(this);

    }

    // 点击右边的内容的标题跳转到详情页面并将 ID 传过去
    @Override
    public void onClassifyRightClick(int position) {

        ClassifyData.DatasBean.ChildrenBean childrenBean = mSonItemsList.get(position);

        if (childrenBean != null) {

            String stc_id = childrenBean.getStc_id();
            String store_id = childrenBean.getStore_id();
            UIHelper.showMarketGoodList(getActivity(),store_id,stc_id, "");
        }
    }

}