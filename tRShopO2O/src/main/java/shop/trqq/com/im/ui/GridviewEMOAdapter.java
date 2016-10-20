package shop.trqq.com.im.ui;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.adapter.CommonAdapter;
import shop.trqq.com.adapter.ViewHolder;

public class GridviewEMOAdapter extends CommonAdapter<String> {

    // construct
    public GridviewEMOAdapter(Context context) {
        super(context, new ArrayList<String>(), R.layout.im_gridview_emo);
    }

    @Override
    public void convert(ViewHolder holder, final String emo) {
        // TODO Auto-generated method stub
        LinearLayout gridemo_Layout = (LinearLayout) holder
                .getView(R.id.gridemo_Layout);
        ImageView gridemo_image = (ImageView) holder
                .getView(R.id.gridemo_image);
        int emoid = mContext.getResources().getIdentifier(emo, "drawable",
                mContext.getPackageName());
        gridemo_image.setImageResource(emoid);
    }

}
