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
public class HomeNewAdapter extends BaseAdapter{

    ArrayList<HomeRecommendData.DatasBean.GoodsListInfoBean.NewGoodsListBean> mList;
    Context mContext;


    public HomeNewAdapter(Context context, ArrayList<HomeRecommendData.DatasBean.GoodsListInfoBean.NewGoodsListBean> homeNewList) {
        mContext = context;
        mList = homeNewList;
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
        HomeRecommendData.DatasBean.GoodsListInfoBean.NewGoodsListBean newGoodsListBean = mList.get(i);
        if (newGoodsListBean != null) {
            String url = newGoodsListBean.getUrl();

            if (!TextUtils.isEmpty(url)) {
                Picasso.with(mContext).load(url).placeholder(R.drawable.icon_downloading)
                        .config(Bitmap.Config.RGB_565)
                        .into(viewHolder.mImageView);
            }
            viewHolder.name.setText(newGoodsListBean.getGoods_name());
            viewHolder.price.setText("гд"+newGoodsListBean.getGoods_price());
        }
        return view;
    }

    private static class ViewHolder{

        public TextView name;
        public TextView price;
        public ImageView mImageView;
    }
}
