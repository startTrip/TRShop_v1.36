package shop.trqq.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import java.util.Vector;

import shop.trqq.com.R;
import shop.trqq.com.ui.Fragment.product_detail_Fragment;

public class GridviewSpecAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    String[] text;
    private int lastPosition = -1; // ��¼��һ��ѡ�е�ͼƬλ�ã�-1��ʾδѡ���κ�ͼƬ
    private int listPosition;
    private boolean multiChoose; // ��ʾ��ǰ�������Ƿ������ѡ
    private Vector<Boolean> mImage_bs = new Vector<Boolean>(); // ����һ��������Ϊѡ���������
    private product_detail_Fragment goodactivity;

    // construct
    public GridviewSpecAdapter(Context context, String[] text, boolean isMulti,
                               product_detail_Fragment goodactivity, int listPosition) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.goodactivity = goodactivity;
        multiChoose = isMulti;
        this.text = text;
        this.listPosition = listPosition;
        // multiChoose = isMulti;
        for (int i = 0; i < text.length; i++)
            mImage_bs.add(false);

    }

    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public Object getItem(int position) {
        return text[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RadioButton tx = new RadioButton(mContext);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview, null);
            tx = (RadioButton) convertView.findViewById(R.id.grid_textView);
            convertView.setTag(tx);

        } else {
            tx = (RadioButton) convertView.getTag();
        }
        if (mImage_bs.get(position)) {
            tx.setBackgroundResource(R.drawable.yuanjiao_choice);
        } else {
            tx.setBackgroundResource(R.drawable.yuanjiao);
        }
        tx.setText(text[position]);
        tx.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
				 * boolean selected=mImage_bs.get(position);
				 * mImage_bs.set(position, !selected);
				 */
                changeState(position);
                goodactivity.onspec(listPosition, position);
            }
        });
        return convertView;
    }

    // �޸�ѡ�е�״̬
    public void changeState(int position) {
        // ��ѡʱ
        if (multiChoose == true) {
            mImage_bs.setElementAt(!mImage_bs.elementAt(position), position); // ֱ��ȡ������
        }
        // ��ѡʱ
        else {
            if (lastPosition != -1)
                mImage_bs.setElementAt(false, lastPosition); // ȡ����һ�ε�ѡ��״̬
            mImage_bs.setElementAt(!mImage_bs.elementAt(position), position); // ֱ��ȡ������
            lastPosition = position; // ��¼����ѡ�е�λ��
        }
        notifyDataSetChanged(); // ֪ͨ���������и���
    }

}
