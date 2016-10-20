package shop.trqq.com.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.YkLog;


/**
 * 商品列表的适配器recyclerview 升级版
 *
 * @author 星河
 * @created 2015-02-10
 */
public class RecViewGoodsAdapter extends RecyclerView.Adapter<RecViewGoodsAdapter.SimpleViewHolder> {

    private static final String TAG = "ListViewCartAdapter";
    private List<GoodsBean> mData;
    private final Context mContext;
    private int mCurrentItemId = 0;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final LinearLayout listLayout;
        public final RatingBar ratingBar;
        public final ImageView newsImg;
        public final TextView saleNum;
        //        public final  Button delete;
        // 货品数量
        public final TextView newsAbstract;
        public final TextView goodsflag;

        public SimpleViewHolder(View view) {
            super(view);
            listLayout = (LinearLayout) view.findViewById(R.id.list_layout);
            newsImg = (ImageView) view.findViewById(R.id.news_img);
            title = (TextView) view.findViewById(R.id.news_title);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
            saleNum = (TextView) view.findViewById(R.id.news_salenum);
            newsAbstract = (TextView) view.findViewById(R.id.news_abstract);
            goodsflag = (TextView) view.findViewById(R.id.list_goods_flag);
        }
    }


    public RecViewGoodsAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<GoodsBean>();
        YkLog.i(TAG, "购物车适配器构造方法");
    }

    public void addItem(int position, GoodsBean bean) {
        final int id = mCurrentItemId++;
        mData.add(position, bean);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.list_goods, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder mholder, int mposition) {
        // holder.title.setText(mItems.get(position).toString());
        final SimpleViewHolder holder = mholder;
        final int position = mposition;
        final TextView title = holder.title;
        final LinearLayout listLayout = holder.listLayout;
        final RatingBar ratingBar = holder.ratingBar;
        final ImageView newsImg = holder.newsImg;
        final TextView saleNum = holder.saleNum;
//	        public final  Button delete;
        // 货品数量
        final TextView newsAbstract = holder.newsAbstract;
        final TextView goodsflag = holder.goodsflag;
        ImageLoader.getInstance().displayImage(mData.get(position).getGoods_image_url(),
                newsImg, ImageLoadUtils.getoptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        title.setText(mData.get(position).getGoods_name());
        newsAbstract.setText("￥:" + mData.get(position).getGoods_price());
        ratingBar.setRating(mData.get(position).getEvaluation_good_star());
        saleNum.setText("(" + mData.get(position).getGoods_salenum() + "销量)");
        //saleNum.setText("(" + mData.get(position).getEvaluation_count() + "人)");
        final String goods_id = mData.get(position).getGoods_id();
        listLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

					/*historyList = UserManager.getHistoryList();
					for (int i = 0; i < historyList.size(); i++) {
						if(historyList.get(i).getGoods_id().equals(goodsBean.getGoods_id()))
							historyList.remove(i);		
					}			
					historyList.add(0, goodsBean);
					if (historyList.size() == 21)
						historyList.remove(20);
					UserManager.setHistoryList(mContext, historyList);*/
                UserManager.updateHistoryList(mContext, mData.get(position));
                UIHelper.showGoods_Detaill(mContext, goods_id);
            }

        });
        goodsflag.setVisibility(View.GONE);
        try {
            if (mData.get(position).getGroup_flag()) {
                setFlagTextandColor("抢购", "#F98144", goodsflag);
            } else if (mData.get(position).getXianshi_flag()) {
                setFlagTextandColor("限时折扣", "#44BDF9", goodsflag);
            } else if (mData.get(position).getIs_virtual().equals("1")) {// 店铺列表没有，会报错
                setFlagTextandColor("虚拟兑换", "#44BDF9", goodsflag);
            } else if (mData.get(position).getIs_fcode().equals("1")) {
                setFlagTextandColor("F码商品", "#44BDF9", goodsflag);
            } else if (mData.get(position).getIs_presell().equals("1")) {
                setFlagTextandColor("预售商品", "#44BDF9", goodsflag);
            } else if (mData.get(position).getHave_gift().equals("1")) {
                setFlagTextandColor("赠品", "#44BDF9", goodsflag);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<GoodsBean> data) {
        if (data != null) {
            this.mData = data;
/*	        for (int i = 0; i < mData.size(); i++) {
	            addItem(i,mData.get(i));
	        }*/
        }
    }

    public List<GoodsBean> getmData() {
        return mData;
    }

    public void setFlagTextandColor(String str, String color, TextView goodsflag) {
        goodsflag.setText(str);
        goodsflag.setBackgroundColor(Color.parseColor(color));
        goodsflag.setVisibility(View.VISIBLE);
    }


}
