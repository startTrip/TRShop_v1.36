package shop.trqq.com.adapter.recycler;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.ImageLoadUtils;


/**
 * 商品列表的???配器recyclerview QuickRcvAdapter升级??
 *
 * @author 星河
 * @created 2015-02-10
 */
public class RecViewGoodsSpAdapter extends SuperAdapter<GoodsBean> {


    private static final String TAG = "ListViewCartAdapter";
    private int mCurrentItemId = 0;


    public RecViewGoodsSpAdapter(Context context, List<GoodsBean> list, int layoutResId) {
        super(context, list, layoutResId);
    }

/*	public RecViewGoodsAdapter(Context context) {
        mContext = context;
        mData=new ArrayList<GoodsBean>();
		YkLog.i(TAG, "购物车???配器构造方??");
	}*/


    public void setFlagTextandColor(String str, String color, TextView goodsflag) {
        goodsflag.setText(str);
        goodsflag.setBackgroundColor(Color.parseColor(color));
        goodsflag.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBind(int viewType, BaseViewHolder holder, final int position,
                       final GoodsBean bean) {
        // TODO Auto-generated method stub
        final TextView title = holder.getView(R.id.news_title);
        final LinearLayout listLayout = holder.getView(R.id.list_layout);
        final RatingBar ratingBar = holder.getView(R.id.ratingbar);
        final ImageView newsImg = holder.getView(R.id.news_img);
        final TextView saleNum = holder.getView(R.id.news_salenum);
//        public final  Button delete;
        // 货品数量
        final TextView newsAbstract = holder.getView(R.id.news_abstract);
        final TextView goodsflag = holder.getView(R.id.list_goods_flag);
        ImageLoader.getInstance().displayImage(bean.getGoods_image_url(),
                newsImg, ImageLoadUtils.getoptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        title.setText(bean.getGoods_name());
        newsAbstract.setText("￥:" + bean.getGoods_price());
        ratingBar.setRating(bean.getEvaluation_good_star());
        saleNum.setText("(" + bean.getGoods_salenum() + "销量)");
        //saleNum.setText("(" + bean.getEvaluation_count() + "??)");
        final String goods_id = bean.getGoods_id();
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
                UserManager.updateHistoryList(mContext, bean);
                UIHelper.showGoods_Detaill(mContext, goods_id);
            }

        });
        goodsflag.setVisibility(View.GONE);
        try {
            if (bean.getGroup_flag()) {
                setFlagTextandColor("抢购", "#F98144", goodsflag);
            } else if (bean.getXianshi_flag()) {
                setFlagTextandColor("限时折扣", "#44BDF9", goodsflag);
            } else if (bean.getIs_virtual().equals("1")) {// 店铺列表没有，会报错
                setFlagTextandColor("虚拟兑换", "#44BDF9", goodsflag);
            } else if (bean.getIs_fcode().equals("1")) {
                setFlagTextandColor("F码商品", "#44BDF9", goodsflag);
            } else if (bean.getIs_presell().equals("1")) {
                setFlagTextandColor("预售商品", "#44BDF9", goodsflag);
            } else if (bean.getHave_gift().equals("1")) {
                setFlagTextandColor("赠品", "#44BDF9", goodsflag);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
