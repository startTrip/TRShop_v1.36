package shop.trqq.com.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.bean.Home1Bean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.ToastUtils;

public class GridviewHome3Adapter extends CommonAdapter<Home1Bean> {

    // construct
    public GridviewHome3Adapter(Context context) {
        super(context, new ArrayList<Home1Bean>(), R.layout.gridview_home3);
    }

    private void clickimg(String type, String data) {
        if (type.equals("url")) {
            try {
                String a[] = data.split("goods_id=");
                UIHelper.showGoods_Detaill(mContext, a[1]);
            } catch (Exception e) {
                ToastUtils.showMessage(mContext, "跳转出错，请确认是泰润官方商品商品");
            }
        } else if (type.equals("keyword")) {
            UIHelper.showShop(mContext, data, "", "", "");
        } else if (type.equals("goods")) {
            UIHelper.showGoods_Detaill(mContext, data);
        } else if (type.equals("special")) {
            UIHelper.showSpecial(mContext, data);
        }
    }

    @Override
    public void convert(ViewHolder holder, final Home1Bean Bean) {
        // TODO Auto-generated method stub
        ImageView home3Img = (ImageView) holder.getView(R.id.home3_image);

        String imageUrl = Bean.getImage();
        ImageLoader.getInstance().displayImage(imageUrl, home3Img,
                ImageLoadUtils.getoptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        home3Img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickimg(Bean.getType(), Bean.getData());
            }
        });
    }

}
