package shop.trqq.com.supermarket.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Project: Market
 * Author: wm
 * Data:   2016/9/30
 */
public abstract class ListViewBaseAdapter<T> extends BaseAdapter {

    private Context mContext;
    private List<T> datas;

    private LayoutInflater mLayoutInflater;

    public ListViewBaseAdapter(Context context, List<T> datas) {
        mContext = context;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addDatas(List<T> list){
        datas.clear();
        datas.addAll(list);
        notifyDataSetChanged();
    }

    // 清除数据
    public void clearDatas(){
        datas.clear();
        notifyDataSetChanged();
    }

    public LayoutInflater getLayoutInflater(){
        return mLayoutInflater;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getItemView(position,convertView,parent);
    }

    public abstract View getItemView(int position, View convertView, ViewGroup parent);
}
