package shop.trqq.com.supermarket.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.supermarket.bean.HomeClassifyTitle;
import shop.trqq.com.supermarket.utils.ToRoundBitmap;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/9/18
 */
public class HomeClassifyAdapter extends RecyclerView.Adapter<HomeClassifyAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<HomeClassifyTitle> mList ;

    private ClassifyClickCallback mClassifyClickCallback;
    private final int mWidthPixels;

    public interface ClassifyClickCallback{
        void onClassifyClick(int position);
    }

    public void setClassifyClickCallback(ClassifyClickCallback classifyClickCallback) {
        mClassifyClickCallback = classifyClickCallback;
    }

    public HomeClassifyAdapter(Context context, ArrayList<HomeClassifyTitle> classifyData) {
        mContext = context;
        mWidthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        mList = classifyData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.market_layout_home_classify_item,null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        HomeClassifyTitle homeClassifyTitle = mList.get(position);

        String stc_img_path = homeClassifyTitle.getStc_img_path();
        if (!TextUtils.isEmpty(stc_img_path)) {
            // Ô²ÐÎÍ¼Æ¬
            Picasso.with(mContext)
                    .load(stc_img_path).resize(mWidthPixels*2/15,mWidthPixels*2/15).centerCrop()
                    .config(Bitmap.Config.RGB_565).transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap bitmap) {
                    Bitmap roundBitmap = ToRoundBitmap.toRoundBitmap(bitmap);
                    if (roundBitmap!=bitmap) {
                        bitmap.recycle();
                    }
                    return roundBitmap;
                }
                @Override
                public String key() {
                    return "picasso";
                }
            }).into(holder.imageView);
        }

        holder.mName.setText(homeClassifyTitle.getStc_name());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClassifyClickCallback != null) {
                    mClassifyClickCallback.onClassifyClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        View mView;
        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            imageView =(ImageView) itemView.findViewById(R.id.iv_home_classify_item);
            mName = (TextView) itemView.findViewById(R.id.tv_home_classify_item);
        }
    }


}
