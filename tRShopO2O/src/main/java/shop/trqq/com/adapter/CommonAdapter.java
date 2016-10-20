package shop.trqq.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mData;
    protected LayoutInflater mInflater;
    private int mLayoutId;

    public CommonAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mLayoutId = layoutId;
    }

    public CommonAdapter(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mLayoutId = layoutId;
    }

    public void setData(List<T> data) {
        if (data != null)
            this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                mLayoutId, position);
        convert(holder, mData.get(position));
        return holder.getConVertView();

    }

    public abstract void convert(ViewHolder holder, T t);

}
