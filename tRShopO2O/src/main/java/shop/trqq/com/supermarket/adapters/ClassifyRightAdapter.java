package shop.trqq.com.supermarket.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    private int mWidthPixels;
    private Context mContext;
    private ArrayList<ClassifyData.DatasBean.ChildrenBean> mList;
    private OnClassifyRightClick mOnClassifyRightClick;
    public ClassifyRightChildAdapter mClassifyChildAdapter;

    public ClassifyRightChildAdapter getClassifyChildAdapter() {
        return mClassifyChildAdapter;
    }

    public interface OnClassifyRightClick{
        void onClassifyRightClick(int position);
    }

    public void setOnClassifyRightClick(OnClassifyRightClick onClassifyRightClick) {
        mOnClassifyRightClick = onClassifyRightClick;
    }

    public ClassifyRightAdapter(Context context, List<ClassifyData.DatasBean.ChildrenBean> sonItemsList) {
        mContext = context;
        mList = (ArrayList<ClassifyData.DatasBean.ChildrenBean>) sonItemsList;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        mWidthPixels = displayMetrics.widthPixels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.market_classify_right_child_item,null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ClassifyData.DatasBean.ChildrenBean sonItemsBean = mList.get(position);
        if (sonItemsBean != null) {
            holder.mTextView.setText(sonItemsBean.getStc_name());
            String stc_img_path = sonItemsBean.getStc_img_path();
            if(!TextUtils.isEmpty(stc_img_path)){

                Picasso.with(mContext)
                        .load(stc_img_path)
                        .placeholder(R.drawable.icon_downloading)
                        .resize(mWidthPixels*1/5,mWidthPixels)
                        .centerInside().
                        error(R.mipmap.empty_picture)
                        .config(Bitmap.Config.RGB_565).into(holder.mImageView);
            }else {
//                holder.mImageView.setMaxWidth(mWidthPixels*1/5);
//                holder.mImageView.setMaxHeight(mWidthPixels*1/5);
//                holder.mImageView.setImageResource(R.mipmap.empty_picture);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnClassifyRightClick != null) {

                        mOnClassifyRightClick.onClassifyRightClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView;
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.classify_right_child_iv);
            mTextView = (TextView)itemView.findViewById(R.id.classify_right_child_tv);
        }
    }
}

