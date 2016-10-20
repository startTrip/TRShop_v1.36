package shop.trqq.com.supermarket.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private List<ClassifyData.InforBean.ListItemsBean.SonItemsBean> mSonItemsList;
    private ClassifyRightAdapter mClassifyRightAdapter;

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

    }

    private void initData() {
        // 根据不同的值 进行不同数据的展示
        Bundle bundle = getArguments();
        ClassifyData.InforBean.ListItemsBean listItemsBean =bundle.getParcelable("listItemsBean");
        if (listItemsBean != null) {
            mSonItemsList = listItemsBean.getSonItems();
        }
        mClassifyRightAdapter = new ClassifyRightAdapter(getActivity(),mSonItemsList);
    }

    private void setData() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mClassifyRightAdapter);
    }


    private void setListener() {
//         设置回调监听
        mClassifyRightAdapter.setOnClassifyRightClick(this);

    }

    // 点击右边的内容的标题跳转到详情页面并将 ID 传过去
    @Override
    public void onClassifyRightClick(int position) {
//        Intent intent = new Intent(getActivity(), MarketClassifyDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("source","classify");
//        bundle.putString("id",mSonItemsList.get(position).getId());
//        intent.putExtras(bundle);
//
//        Log.d("id",mSonItemsList.get(position).getId());
//        getActivity().startActivity(intent);
        UIHelper.showShop(getActivity(), "", "", "126", "");
    }

    // 点击 recyclerView 里面的内容跳转到详情页面并将 ID 传过去
    @Override
    public void onClassifyRightChildClick1(int index, int position) {
//        Intent intent = new Intent(getActivity(),MarketClassifyDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("source","classify");
//        bundle.putString("id",mSonItemsList.get(index).getSonItems(). get(position).getId());
//        intent.putExtras(bundle);
//
//        Log.d("id",mSonItemsList.get(index).getSonItems().get(position).getId());
//        getActivity().startActivity(intent);
        UIHelper.showShop(getActivity(), "", "", "126", "");
    }
}