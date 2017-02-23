package shop.trqq.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.bean.AddressBean;
import shop.trqq.com.ui.Base.UIHelper;

/**
 * Project: TRShop_v1.36
 * Author: wm
 * Data:   2017/2/22
 */
public class RecyclerAddressListAdapter extends RecyclerView.Adapter<RecyclerAddressListAdapter.ViewHolder> {

    public ArrayList<AddressBean> mList;
    public Context mContext;

    public interface onRecyclerItemClick{
        void onItemClick(int index);

        void onAddressDeleteClick(int position);
    }
    public onRecyclerItemClick mOnRecyclerItemClick;

    public void setOnRecyclerItemClick(onRecyclerItemClick onRecyclerItemClick) {
        mOnRecyclerItemClick = onRecyclerItemClick;
    }

    public RecyclerAddressListAdapter(Context context, ArrayList<AddressBean> list) {
        mContext = context;
        if (list == null) {
            mList = new ArrayList<>();
        } else {
            mList = list;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.address_list_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        AddressBean addressBean = mList.get(position);
        if (addressBean != null) {
            holder.mName.setText(addressBean.getTrue_name());
            holder.mPhone.setText(addressBean.getMob_phone());
            holder.mArea.setText(addressBean.getArea_info()+addressBean.getAddress());

            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mOnRecyclerItemClick!=null){
                        mOnRecyclerItemClick.onAddressDeleteClick(position);
                    }

                }
            });


            holder.mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.showAddressEdite(mContext, mList.get(position));
                }
            });
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnRecyclerItemClick!=null){
                        mOnRecyclerItemClick.onItemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setData(ArrayList<AddressBean> addressList) {
        if (addressList != null) {
            mList.clear();
            mList.addAll(addressList);
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName, mPhone, mArea;
        public TextView mEdit, mDelete;
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mName = (TextView) itemView.findViewById(R.id.name);
            mPhone = (TextView) itemView.findViewById(R.id.phone);
            mArea = (TextView) itemView.findViewById(R.id.area_info);
            mEdit = (TextView) itemView.findViewById(R.id.edit);
            mDelete = (TextView) itemView.findViewById(R.id.delete);
        }
    }
}
