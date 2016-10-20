package shop.trqq.com.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.ui.Fragment.product_detail_Fragment;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.MyGridView;

/**
 * 信息列表的适配器
 *
 * @author 星河
 * @created 2015-02-10
 */
public class ListViewSpecAdapter extends CommonAdapter<String[]> {

    private static final String TAG = "ListViewNewsAdapter";
    // 图片加载工具
    private DisplayImageOptions options;

    private Context ListContext;
    private product_detail_Fragment goodactivity;

    /**
     * 商品列表适配器构造方法
     *
     * @param context
     */
    public ListViewSpecAdapter(Context context,
                               product_detail_Fragment goodactivity) {
        super(context, new ArrayList<String[]>(), R.layout.list_gridview);
        ListContext = context;
        this.goodactivity = goodactivity;
        YkLog.i(TAG, "商品规格适配器构造方法");
    }

    // getview里面的一个方法
    @Override
    public void convert(ViewHolder holder, String[] specBean) {
        LinearLayout listLayout = (LinearLayout) holder
                .getView(R.id.grid_specLayout);
        MyGridView specGridView = (MyGridView) holder.getView(R.id.grid_spec);
        TextView title = (TextView) holder.getView(R.id.title_spec);
        title.setText(specBean[0] + ":");
        String[] specGrid = new String[specBean.length - 2];
        int positionState = 0;
        for (int i = 0; i < specGrid.length; i++) {
            specGrid[i] = specBean[i + 2];
            if (specGrid[i].equals(specBean[1])) {
                positionState = i;
            }
        }
        final int mPosition = holder.getmPosition();
        GridviewSpecAdapter spec_adapter = new GridviewSpecAdapter(mContext, specGrid,
                false, goodactivity, mPosition);
        specGridView.setAdapter(spec_adapter);
        spec_adapter.changeState(positionState);
        listLayout.setVisibility(View.VISIBLE);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub

    }

}
