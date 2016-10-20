package shop.trqq.com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.bean.Mansong_RulesBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.YkLog;

/**
 * �����ͻ�б��������
 *
 * @author �Ǻ�
 * @created 2015-02-10
 */
public class ListViewMansongAdapter extends CommonAdapter<Mansong_RulesBean> {

    private static final String TAG = "ListViewMansongAdapter";

    Context ListContext;

    /**
     * �����ͻ�б����������췽��
     *
     * @param context
     */
    public ListViewMansongAdapter(Context context) {
        super(context, new ArrayList<Mansong_RulesBean>(),
                R.layout.list_mansong_rules);
        ListContext = context;
        YkLog.i(TAG, "��Ʒ�б����������췽��");
    }

    // getview�����һ������
    @Override
    public void convert(ViewHolder holder, Mansong_RulesBean Bean) {
        LinearLayout list_rules_layout = (LinearLayout) holder
                .getView(R.id.list_rules_layout);
        LinearLayout mansong_goods_layout = (LinearLayout) holder
                .getView(R.id.mansong_goods_layout);
        ImageView mansong_goods_img = (ImageView) holder
                .getView(R.id.mansong_goods_img);
        TextView mansong_info = (TextView) holder.getView(R.id.mansong_info);
        TextView mansong_goods_name = (TextView) holder
                .getView(R.id.mansong_goods_name);

        mansong_goods_name.setText(Bean.getMansong_goods_name());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.GREEN);
        String info = "ÿ�ʶ�����" + Bean.getPrice() + "Ԫ��";
        SpannableStringBuilder builder = new SpannableStringBuilder(info);
        builder.setSpan(redSpan, 5, 5 + Bean.getPrice().toString().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (!Bean.getDiscount().equals("0")) {
            int len = 5 + Bean.getPrice().toString().length() + 4;
            builder.append("����" + Bean.getDiscount() + "Ԫ");
            builder.setSpan(greenSpan, len, len
                            + Bean.getDiscount().toString().length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        // ����Ʒ
        if (!Bean.getGoods_id().equals("0")) {
            builder.append(",����Ʒ��");
            final String goods_id = Bean.getGoods_id();
            list_rules_layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.showGoods_Detaill(ListContext, goods_id);
                }

            });
            mansong_goods_layout.setVisibility(View.VISIBLE);
            // if (Bean.getGoods_image_url() != null) {
            ImageLoader.getInstance().displayImage(Bean.getGoods_image_url(),
                    mansong_goods_img, ImageLoadUtils.getoptions(),
                    ImageLoadUtils.getAnimateFirstDisplayListener());
            // }
        } else {
            mansong_goods_layout.setVisibility(View.GONE);
        }
        mansong_info.setText(builder);

        // list_rules_layout.setVisibility(View.VISIBLE);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub

    }

}
