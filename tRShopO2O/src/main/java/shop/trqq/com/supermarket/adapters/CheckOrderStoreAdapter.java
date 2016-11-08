package shop.trqq.com.supermarket.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.supermarket.base.ListViewBaseAdapter;
import shop.trqq.com.supermarket.bean.GoodsInfo;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/10/16
 */
public class CheckOrderStoreAdapter extends ListViewBaseAdapter<GoodsInfo> {
    private Context mContext;
    private CheckOrderGoodsAdapter mCheckOrderGoodsAdapter;

    public CheckOrderStoreAdapter(Context context, List<GoodsInfo> datas) {
        super(context, datas);
        mContext = context ;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            convertView = getLayoutInflater().inflate(R.layout.market_check_store_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoodsInfo goodsInfo = (GoodsInfo) getItem(position);

        if (goodsInfo != null) {

            String store_name = goodsInfo.getStore_name();
            String store_goods_total = goodsInfo.getStore_goods_total();
            int goodsNum = goodsInfo.getGoods_list().size();

            holder.mStoreName.setText(store_name);
            String goods_weight_all = goodsInfo.getGoods_weight_all();
            if(!TextUtils.isEmpty(goods_weight_all)){
                holder.mGoodsInfo.setText("共"+goodsNum+"件商品，总重"+goods_weight_all+"kg  小计:");
            }else {
                double v = 4.8 * goodsInfo.getGoods_list().size();
                String weight = String.format("%.1f",v);
                holder.mGoodsInfo.setText("共"+goodsNum+"件商品，总重"+ weight +"kg  小计:");
            }
            // 设置运费
            String freight = goodsInfo.getStore_shipping();
            if(!TextUtils.isEmpty(freight)){
                if(TextUtils.equals(freight,"0")){
                    holder.mFreight.setText("免配送费");
                }else {
                    holder.mFreight.setText("￥ "+freight+"（超出十公里）");
                }
                Float i = Float.parseFloat(freight) + Float.parseFloat(store_goods_total);;
                holder.mSum_money.setText("￥ "+i);
            }else {
                Float i= Float.parseFloat(store_goods_total)+10;
                holder.mSum_money.setText("￥ "+i);
            }
          //   设置到达时间
            String arrive_time = goodsInfo.getArrive_time();
            holder.mArrive_time.setText(arrive_time);
            List<GoodsInfo.GoodsListBean> goods_list = goodsInfo.getGoods_list();
            mCheckOrderGoodsAdapter = new CheckOrderGoodsAdapter(mContext,goods_list);
            holder.mListView.setAdapter(mCheckOrderGoodsAdapter);
            holder.mListView.setEnabled(false);
        }

        return convertView;
    }

    public static class ViewHolder{

        public TextView mStoreName,mFreight,mArrive_time,mGoodsInfo,mSum_money;
        public ListView mListView;
        public ViewHolder(View itemview)
        {
            mStoreName = (TextView)itemview.findViewById(R.id.store_name);
            mListView = (ListView)itemview.findViewById(R.id.goods_list);
            mFreight = (TextView)itemview.findViewById(R.id.freight);
            mArrive_time = (TextView)itemview.findViewById(R.id.arrive_time);
            mGoodsInfo = (TextView)itemview.findViewById(R.id.store_goods_info);
            mSum_money = (TextView) itemview.findViewById(R.id.sum_money);
        }

    }
}
