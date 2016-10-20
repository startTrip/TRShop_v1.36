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

public class GridviewGoodsAdapter extends CommonAdapter<GoodsBean> {
    private int TYPE;
    public static final int TYPE_STOREGOODS = 1;// ���̽�����Ʒ��Gridview
    public static final int TYPE_GOODS = 2;// ��ҳGOODS����
    public static final int TYPE_GOODS1 = 3;// ��ҳGOODS1���ʹ���
    public static final int TYPE_GOODS2 = 4;// ��ҳGOODS2��������

    // construct
    public GridviewGoodsAdapter(Context context, int flag) {
        super(context, new ArrayList<GoodsBean>(), R.layout.gridview_goods);
        TYPE = flag;
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
        goodsPrice.setText("��" + Bean.getGoods_promotion_price());
        String imageUrl;
        if (TYPE == TYPE_STOREGOODS) {
            imageUrl = Bean.getUrl();
        } else {
            imageUrl = Bean.getGoods_image();
        }
        if (TYPE == TYPE_GOODS1) {
            ImageView goodsbg = (ImageView) holder.getView(R.id.gridgoods_bg);
            goodsbg.setVisibility(View.VISIBLE);
        }
        if (TYPE == TYPE_GOODS2) {
            ImageView goodsbg = (ImageView) holder.getView(R.id.gridgoods_bg);
            goodsbg.setImageResource(R.drawable.groupbuybg);
            goodsbg.setVisibility(View.VISIBLE);
        }
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
