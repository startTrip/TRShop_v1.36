package shop.trqq.com.supermarket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.sug.SuggestionResult;

import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.supermarket.base.ListViewBaseAdapter;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2016/12/7
 */
public class SuggestInfoAdapter extends ListViewBaseAdapter<SuggestionResult.SuggestionInfo>{

    private Context mContext;

    public SuggestInfoAdapter(Context context, List<SuggestionResult.SuggestionInfo> datas) {
        super(context, datas);
        mContext = context;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.select_address_item, parent, false);
            viewHolder =  new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PoiInfo poiInfo = (PoiInfo) getItem(position);

        if (poiInfo != null) {

            viewHolder.name.setText(poiInfo.name);
            viewHolder.address.setText(poiInfo.address);
        }
        return convertView;
    }

    public static class ViewHolder{

        TextView name;
        TextView address;
        TextView title;
        public ViewHolder(View convertView) {

            name = (TextView) convertView.findViewById(R.id.name);
            address = (TextView) convertView.findViewById(R.id.address);
            title = (TextView) convertView.findViewById(R.id.location);
        }
    }
}
