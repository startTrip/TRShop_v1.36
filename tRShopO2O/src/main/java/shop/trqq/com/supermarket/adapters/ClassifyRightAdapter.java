package shop.trqq.com.supermarket.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.supermarket.bean.ClassifyData;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/9/19
 */
public class ClassifyRightAdapter extends RecyclerView.Adapter<ClassifyRightAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ClassifyData.InforBean.ListItemsBean.SonItemsBean> mList;
    private OnClassifyRightClick mOnClassifyRightClick;
    public ClassifyRightChildAdapter mClassifyChildAdapter;

    public ClassifyRightChildAdapter getClassifyChildAdapter() {
        return mClassifyChildAdapter;
    }

    public interface OnClassifyRightClick{
        void onClassifyRightClick(int position);
        void onClassifyRightChildClick1(int index,int position);
    }

    public void setOnClassifyRightClick(OnClassifyRightClick onClassifyRightClick) {
        mOnClassifyRightClick = onClassifyRightClick;
    }

    public ClassifyRightAdapter(Context context, List<ClassifyData.InforBean.ListItemsBean.SonItemsBean> sonItemsList) {
        mContext = context;
        mList = (ArrayList<ClassifyData.InforBean.ListItemsBean.SonItemsBean>) sonItemsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.market_classify_right_item, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ClassifyData.InforBean.ListItemsBean.SonItemsBean sonItemsBean = mList.get(position);
        if (sonItemsBean != null) {
            holder.mTextView.setText(sonItemsBean.getType());

            holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnClassifyRightClick.onClassifyRightClick(position);
                }
            });
            List<ClassifyData.InforBean.ListItemsBean.SonItemsBean.SonItemsBean1> sonItems = sonItemsBean.getSonItems();
            mClassifyChildAdapter = new ClassifyRightChildAdapter(mContext,position,sonItems);
            mClassifyChildAdapter.setOnClassifyRightClick(new ClassifyRightChildAdapter.onClassifyRightChildClick() {

                @Override
                public void onClassifyRightChildClick(int index, int position) {
                    mOnClassifyRightClick.onClassifyRightChildClick1(index,position);
                    Log.d("index",index+"mIndex");
                    Log.d("index",position+"position");
                }
            });
            holder.mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,3));

            holder.mRecyclerView.setAdapter(mClassifyChildAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;
        public RelativeLayout mRelativeLayout;
        public RecyclerView mRecyclerView;
        public ViewHolder(final View itemView) {
            super(itemView);
            mRelativeLayout = (RelativeLayout)itemView.findViewById(R.id.rl_classify_right);
            mTextView = (TextView) itemView.findViewById(R.id.tv_classify_right);
            mRecyclerView = (RecyclerView)itemView.findViewById(R.id.rv_classify_item);

        }
    }
}

