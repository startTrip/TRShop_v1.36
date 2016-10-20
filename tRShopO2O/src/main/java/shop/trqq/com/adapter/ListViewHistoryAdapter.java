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
import shop.trqq.com.util.YkLog;

/**
 * �����¼�б��������
 *
 * @author �Ǻ�
 * @created 2015-02-10
 */
public class ListViewHistoryAdapter extends CommonAdapter<GoodsBean> {

    private static final String TAG = "ListViewHistoryAdapter";

    /**
     * �����¼�б����������췽��
     *
     * @param context
     */
    public ListViewHistoryAdapter(Context context) {
        super(context, new ArrayList<GoodsBean>(), R.layout.list_favorites);
        YkLog.i(TAG, "��ע��Ʒ�б����������췽��");
    }


    // getview�����һ������
    @Override
    public void convert(ViewHolder holder, GoodsBean goodsBean) {
        LinearLayout listLayout = (LinearLayout) holder
                .getView(R.id.list_layout);
        ImageView goodsImg = (ImageView) holder.getView(R.id.favorites_img);
        TextView goodsTitle = (TextView) holder.getView(R.id.favorites_title);
        TextView goodsprice = (TextView) holder.getView(R.id.favorites_price);
        ImageLoader.getInstance().displayImage(goodsBean.getGoods_image_url(),
                goodsImg, ImageLoadUtils.getoptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        goodsTitle.setText(goodsBean.getGoods_name());
        goodsprice.setText("��:" + goodsBean.getGoods_price());
        final String goods_id = goodsBean.getGoods_id();
        listLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showGoods_Detaill(mContext, goods_id);
            }
        });
/*		final int dPosition=holder.getmPosition();
        listLayout.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						new ContextThemeWrapper(
								mContext,
								android.support.v7.appcompat.R.style.Theme_AppCompat_Light_DialogWhenLarge));
				builder.setTitle("�Ƿ�ɾ����ע")  
				.setMessage("ȷ���Ƴ���ע����Ʒ��")  
				.setNegativeButton("ȷ��",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				})  
				.setPositiveButton("ȡ��", null)  
				.show();  
				
				return false;
			}
		});*/
    }


}
