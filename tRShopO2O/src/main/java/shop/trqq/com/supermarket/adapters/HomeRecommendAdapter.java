package shop.trqq.com.supermarket.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.supermarket.bean.HomeRecommendData;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/9/22
 */
public class HomeRecommendAdapter extends BaseAdapter{

    ArrayList<HomeRecommendData.DatasBean.GoodsListInfoBean.RecommendedGoodsListBean> mList;
    Context mContext;
    private final int mWidthPixels;

    public HomeRecommendAdapter(Context context, ArrayList<HomeRecommendData.DatasBean.GoodsListInfoBean.RecommendedGoodsListBean> recommendedList) {
        mContext = context;
        mList = recommendedList;
        mWidthPixels = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if(view == null){

            view = LayoutInflater.from(mContext).inflate(R.layout.market_home_grid_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.market_home_goods_iv);
            viewHolder.name = (TextView) view.findViewById(R.id.market_home_goods_name);
            viewHolder.price = (TextView) view.findViewById(R.id.market_home_goods_price);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        HomeRecommendData.DatasBean.GoodsListInfoBean.RecommendedGoodsListBean recommendedGoodsListBean = mList.get(i);
        if (recommendedGoodsListBean != null) {
            String url = recommendedGoodsListBean.getUrl();

            if (!TextUtils.isEmpty(url)) {
                Picasso.with(mContext).load(url).placeholder(R.mipmap.empty_picture).
                        resize(mWidthPixels/2,mWidthPixels/2)
                        .centerCrop()
                        .config(Bitmap.Config.RGB_565)
                        .into(viewHolder.mImageView);
            }
            viewHolder.name.setText(recommendedGoodsListBean.getGoods_name());
            viewHolder.price.setText("гд"+recommendedGoodsListBean.getGoods_price());
        }
        return view;
    }

    private static class ViewHolder{

        public TextView name;
        public TextView price;
        public ImageView mImageView;
    }
}
