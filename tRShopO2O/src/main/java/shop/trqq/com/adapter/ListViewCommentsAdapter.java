package shop.trqq.com.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.bean.CommentsBean;
import shop.trqq.com.util.YkLog;

/**
 * 信息列表的适配器
 *
 * @author 星河
 * @created 2015-02-10
 */
public class ListViewCommentsAdapter extends CommonAdapter<CommentsBean> {

    private static final String TAG = "ListViewCommentsAdapter";

    /**
     * 商品列表适配器构造方法
     *
     * @param mGoodsBeanList
     * @param context
     */
    public ListViewCommentsAdapter(Context context) {
        super(context, new ArrayList<CommentsBean>(), R.layout.list_comments);
        YkLog.i(TAG, "评论列表适配器构造方法");
    }

    // getview里面的一个方法
    @Override
    public void convert(ViewHolder holder, final CommentsBean Bean) {
        TextView geval_frommembername = (TextView) holder
                .getView(R.id.geval_frommembername);
        RatingBar geval_scores = (RatingBar) holder.getView(R.id.geval_scores);
        TextView geval_addtime = (TextView) holder.getView(R.id.geval_addtime);
        TextView geval_content = (TextView) holder.getView(R.id.geval_content);
        TextView geval_explain = (TextView) holder.getView(R.id.geval_explain);

        geval_frommembername.setText(Bean.getGeval_frommembername());
        geval_scores.setRating(Float.parseFloat(Bean.getGeval_scores()));
        geval_addtime.setText(Bean.getGeval_addtime());
        geval_content.setText("心得：" + Bean.getGeval_content());
        if (Bean.getGeval_explain() != null) {
            geval_explain.setVisibility(View.VISIBLE);
            geval_explain.setText("商家解释：" + Bean.getGeval_explain());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub

    }

}
