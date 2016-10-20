package shop.trqq.com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
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
 * ��Ʒ�б��������
 * �ѷ�������RecViewCartAdapter
 *
 * @author �Ǻ�
 * @created 2015-02-10
 */
public class ListViewGoodsAdapter extends CommonAdapter<GoodsBean> {

    private static final String TAG = "ListViewGoodsAdapter";
    private List<GoodsBean> historyList;

    /**
     * ��Ʒ�б����������췽��
     *
     * @param context
     */
    public ListViewGoodsAdapter(Context context) {
        super(context, new ArrayList<GoodsBean>(), R.layout.list_goods);
        YkLog.i(TAG, "��Ʒ�б����������췽��");
        historyList = new ArrayList<GoodsBean>();
    }

    // getview�����һ������
    @Override
    public void convert(ViewHolder holder, final GoodsBean goodsBean) {
        LinearLayout listLayout = (LinearLayout) holder
                .getView(R.id.list_layout);
        ImageView newsImg = (ImageView) holder.getView(R.id.news_img);
        TextView newsTitle = (TextView) holder.getView(R.id.news_title);
        RatingBar ratingBar = (RatingBar) holder.getView(R.id.ratingbar);
        TextView saleNum = (TextView) holder.getView(R.id.news_salenum);
        TextView newsAbstract = (TextView) holder.getView(R.id.news_abstract);
        TextView goodsflag = (TextView) holder.getView(R.id.list_goods_flag);
        ImageLoader.getInstance().displayImage(goodsBean.getGoods_image_url(),
                newsImg, ImageLoadUtils.getoptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        newsTitle.setText(goodsBean.getGoods_name());
        newsAbstract.setText("��:" + goodsBean.getGoods_price());
        ratingBar.setRating(goodsBean.getEvaluation_good_star());
        saleNum.setText("(" + goodsBean.getGoods_salenum() + "����)");
        //saleNum.setText("(" + goodsBean.getEvaluation_count() + "��)");
        final String goods_id = goodsBean.getGoods_id();
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
                UserManager.updateHistoryList(mContext, goodsBean);
                UIHelper.showGoods_Detaill(mContext, goods_id);
            }

        });
        goodsflag.setVisibility(View.GONE);
        try {
            if (goodsBean.getGroup_flag()) {
                setFlagTextandColor("����", "#F98144", goodsflag);
            } else if (goodsBean.getXianshi_flag()) {
                setFlagTextandColor("��ʱ�ۿ�", "#44BDF9", goodsflag);
            } else if (goodsBean.getIs_virtual().equals("1")) {// �����б�û�У��ᱨ��
                setFlagTextandColor("����һ�", "#44BDF9", goodsflag);
            } else if (goodsBean.getIs_fcode().equals("1")) {
                setFlagTextandColor("F����Ʒ", "#44BDF9", goodsflag);
            } else if (goodsBean.getIs_presell().equals("1")) {
                setFlagTextandColor("Ԥ����Ʒ", "#44BDF9", goodsflag);
            } else if (goodsBean.getHave_gift().equals("1")) {
                setFlagTextandColor("��Ʒ", "#44BDF9", goodsflag);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		/*
		 * else if(goodsBean.getIs_appoint().equals("1")){
		 * setFlagTextandColor("ԤԼ��Ʒ", "#44BDF9", goodsflag); }
		 */

    }

    public void setFlagTextandColor(String str, String color, TextView goodsflag) {
        goodsflag.setText(str);
        goodsflag.setBackgroundColor(Color.parseColor(color));
        goodsflag.setVisibility(View.VISIBLE);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub

    }

}
