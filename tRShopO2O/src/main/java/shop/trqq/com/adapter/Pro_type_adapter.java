package shop.trqq.com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import shop.trqq.com.R;
import shop.trqq.com.bean.GoodClassBean;
import shop.trqq.com.bean.Type;
import shop.trqq.com.ui.Base.UIHelper;

public class Pro_type_adapter extends BaseAdapter {
    // ����Context
    private LayoutInflater mInflater;
    private ArrayList<Type> list;
    private Context context;
    private Type type;
    private ArrayList<GoodClassBean> typeList;
    private String[] color = {"#90ca2b", "#ca2b2b", "#2baeca", "#2bc4ca","#852bca",
            "#ca2b63","#2bca90","#aaca2b","#ca2bc4","#ca2b2b","#2bcaa3","#ca582b"};

    public Pro_type_adapter(Context context, ArrayList<Type> list,
                            ArrayList<GoodClassBean> typeList) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.typeList = typeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list != null && list.size() > 0)
            return list.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // if(parent.getChildCount() == position)
        // {
        final MyView view;
        if (convertView == null) {
            view = new MyView();
            convertView = mInflater.inflate(R.layout.list_pro_type_item, null);
            // view.icon=(ImageView)convertView.findViewById(R.id.typeicon);
            view.name = (TextView) convertView.findViewById(R.id.typename);
            view.mCardView = (CardView) convertView.findViewById(R.id.cardview);
            view.mCardView.setCardBackgroundColor(Color.parseColor(color[new Random().nextInt(12)]));
            convertView.setTag(view);
            // �����б����
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.showShop(context, "", typeList.get(position)
                            .getGc_id(), "", "");
                }
            });
        } else {
            view = (MyView) convertView.getTag();
        }

        type = list.get(position);
        /*
		 * String typestr=type.getTypename(); String[] strs=typestr.split("\\");
		 * String typename=strs[0]; for(int i=1;i<strs.length;i++){
		 * typename="/n"+strs[i]; }
		 */
        view.name.setText(type.getTypename());

        return convertView;
        // }
    }

    private class MyView {
        private ImageView icon;
        private TextView name;
        private CardView mCardView;
    }

}
