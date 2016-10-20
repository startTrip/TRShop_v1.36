package shop.trqq.com.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.bean.GoodsBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.ImageLoadUtils;

public class GridviewGoodsCommendAdapter extends CommonAdapter<GoodsBean> {

    // construct
    public GridviewGoodsCommendAdapter(Context context, int flag) {
        super(context, new ArrayList<GoodsBean>(),
                R.layout.gridview_goods_commend);
    }

    @Override
    public void convert(ViewHolder holder, final GoodsBean Bean) {
        // TODO Auto-generated method stub
        LinearLayout gridgoods_Layout = (LinearLayout) holder
                .getView(R.id.gridgoods_Layout);
        ImageView goodsImg = (ImageView) holder.getView(R.id.gridgoods_image);
        TextView goodsTitle = (TextView) holder.getView(R.id.gridgoods_title);
        TextView goodsPrice = (TextView) holder.getView(R.id.gridgoods_price);
        goodsTitle.setText(Bean.getGoods_name());
        goodsPrice.setText("гд:" + Bean.getGoods_price());
        String imageUrl = Bean.getGoods_image_url();

        ImageLoader.getInstance().displayImage(imageUrl, goodsImg,
                ImageLoadUtils.getoptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        gridgoods_Layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showGoods_Detaill(mContext, Bean.getGoods_id());
            }
        });
    }

}
