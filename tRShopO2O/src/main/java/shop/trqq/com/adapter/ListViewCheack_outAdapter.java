package shop.trqq.com.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.bean.Store_Cart_ListBean;
import shop.trqq.com.bean.VoucherListBean;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.MyListView;

/**
 * ��Ϣ�б��������
 *
 * @author �Ǻ�
 * @created 2015-02-10
 */
public class ListViewCheack_outAdapter extends
        CommonAdapter<Store_Cart_ListBean> {

    private static final String TAG = "ListViewCheack_outAdapter";
    private MyListView mListView;
    private ListViewCheack_outGoodsAdapter mListViewCheack_outgoodsAdapter;
    public float store_sumPrice = 0;
    private Handler handler;
    private boolean[] store_sumflag;
    private boolean[] Voucherflag;
    private String[] VoucherStr;
    private ArrayList<String> Voucher_listStr = new ArrayList<String>();
    private Gson gson;

    private float tmpsum2;

    public float getStore_sumPrice() {
        return store_sumPrice;
    }

    /**
     * ��������б����������췽��
     *
     * @param context
     */
    public ListViewCheack_outAdapter(Handler handler, Context context) {
        super(context, new ArrayList<Store_Cart_ListBean>(),
                R.layout.list_check_out);
        this.handler = handler;
        gson = new Gson();
        YkLog.i(TAG, "��������б����������췽��");
    }

    @Override
    public void setData(List<Store_Cart_ListBean> data) {
        // TODO Auto-generated method stub
        super.setData(data);
        store_sumPrice=0;
        store_sumflag = new boolean[mData.size()];
        VoucherStr = new String[mData.size()];
        Voucherflag = new boolean[mData.size()];
        // TestUtils.println_err("mData.size()",String.valueOf(mData.size()) );
    }

    // getview�����һ������
    @Override
    public void convert(ViewHolder holder, Store_Cart_ListBean Bean) {

        TextView Title = (TextView) holder.getView(R.id.storegroup_title);
        final TextView store_sum = (TextView) holder.getView(R.id.store_sum);
        TextView balance_fees = (TextView) holder.getView(R.id.balance_fees);
        LinearLayout store_mansong_rule_layout = (LinearLayout) holder
                .getView(R.id.store_mansong_rule_layout);
        TextView store_mansong_rule_list = (TextView) holder
                .getView(R.id.store_mansong_rule_list);
        TextView store_mansong_rule_discount = (TextView) holder
                .getView(R.id.store_mansong_rule_discount);

        LinearLayout store_voucher_layout = (LinearLayout) holder
                .getView(R.id.store_voucher_layout);
        store_mansong_rule_layout.setVisibility(View.GONE);
        store_voucher_layout.setVisibility(View.GONE);
        float sum = Bean.getStore_goods_total();
        Title.setText("�������ƣ�" + Bean.getStore_name());
        //�ʷ�
       // if (Bean.getFreight().equals("1")) {
            float freightNum = 0.0f;
/*            for (int i = 0; i < Bean.getGoods_lists().size(); i++) {
                freightNum = freightNum
                        + Bean.getGoods_lists().get(i).getGoods_freight();
            }*/

            freightNum=Float.parseFloat(Bean.getStore_freight());

            Log.d("freight2",freightNum+"");
            balance_fees.setText("��:" + freightNum);
            sum = sum + freightNum;
       // }
        mListView = (MyListView) holder.getView(R.id.listView_goods);
        mListViewCheack_outgoodsAdapter = new ListViewCheack_outGoodsAdapter(
                mContext, handler);
        mListViewCheack_outgoodsAdapter.setData(Bean.getGoods_lists());
        mListView.setAdapter(mListViewCheack_outgoodsAdapter);
        //�����
        if (Bean.getStore_mansong_rule_list() != null) {
            String desc = "��" + Bean.getStore_mansong_rule_list().getPrice()
                    + "��" + Bean.getStore_mansong_rule_list().getDiscount()
                    + " ��"
                    + Bean.getStore_mansong_rule_list().getMansong_goods_name()
                    + "";
            store_mansong_rule_list.setText(desc);
            store_mansong_rule_discount.setText("��:-"
                    + Bean.getStore_mansong_rule_list().getDiscount());
            sum = sum
                    - Float.parseFloat(Bean.getStore_mansong_rule_list()
                    .getDiscount());
            store_mansong_rule_layout.setVisibility(View.VISIBLE);
        }

        final int mPosition = holder.getmPosition();
        // û�д����ʱ�������ݣ� "store_voucher_list": [],
        if (Bean.getStore_voucher_list() != null
                && !"[]".equals(Bean.getStore_voucher_list().toString())) {

            Spinner VoucherSpinner = (Spinner) holder
                    .getView(R.id.store_voucher_title);
            final TextView store_voucher_price = (TextView) holder
                    .getView(R.id.store_voucher_price);
            // ����
            List<String> Voucher_list = new ArrayList<String>();
            final ArrayList<VoucherListBean> Voucher_listBean = new ArrayList<VoucherListBean>();
            try {
                setBean(new JSONObject(Bean.getStore_voucher_list().toString()),
                        Voucher_listBean);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (int i = 0; i < Voucher_listBean.size(); i++) {
                Voucher_list.add(Voucher_listBean.get(i).getVoucher_title());
            }
            // ������
            ArrayAdapter<String> Voucher_adapter = new ArrayAdapter<String>(
                    mContext, android.R.layout.simple_spinner_item,
                    Voucher_list);
            // ������ʽ
            Voucher_adapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // ����������
            VoucherSpinner.setAdapter(Voucher_adapter);
            final float tmpsum = sum;
            VoucherSpinner
                    .setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent,
                                                   View view, int position, long id) {// ѡ�����ȯ
                            // TODO Auto-generated method stub
                            // String
                            // str=parent.getItemAtPosition(position).toString();
                            store_voucher_price.setText("-����"
                                    + Voucher_listBean.get(position)
                                    .getVoucher_price());
                            tmpsum2 = tmpsum
                                    - Float.parseFloat(Voucher_listBean.get(
                                    position).getVoucher_price());
                            store_sum.setText("��:" + tmpsum2);
                            // ����ȯ��voucher_t_id|store_id|voucher_price����������ö��ŷָ�
                            if (position == 0) {
                                VoucherStr[mPosition] = "";
                                Voucherflag[mPosition] = false;
                            } else {
                                VoucherStr[mPosition] = Voucher_listBean.get(
                                        position).getVoucher_t_id()
                                        + "|"
                                        + Voucher_listBean.get(position)
                                        .getVoucher_store_id()
                                        + "|"
                                        + Voucher_listBean.get(position)
                                        .getVoucher_price();
                                Voucherflag[mPosition] = true;
                            }
                            if (store_sumflag[mPosition]) {
                                float tmpstore_sumPrice = store_sumPrice
                                        - tmpsum + tmpsum2;
                                Message msg = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putFloat("store_sumPrice",
                                        tmpstore_sumPrice);
                                msg.setData(bundle);
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub

                        }
                    });
            store_voucher_layout.setVisibility(View.VISIBLE);
        }

        store_sum.setText("��:" + sum);

        // �����ݴ��� CheckoutActivity
        if (!store_sumflag[mPosition]) {//������ײ���ʾ������item��getviewȫִ���ˡ�
            Log.d("store_sumPriceǰ",store_sumPrice+"");
            Log.d("store_sumPrice",sum+"");
            store_sumPrice = store_sumPrice + sum;
            Log.d("store_sumPrice��",store_sumPrice+"");
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putFloat("store_sumPrice", store_sumPrice);
            msg.setData(bundle);
            msg.what = 1;
            handler.sendMessage(msg);
            store_sumflag[mPosition] = true;
        }
        // listLayout.setVisibility(View.VISIBLE);
    }

    private void setBean(JSONObject jsonObject, ArrayList<VoucherListBean> list) {
        int i = 0;
        Iterator keyIter = jsonObject.keys();
        VoucherListBean tmpbean = new VoucherListBean();
        tmpbean.setVoucher_price("0");
        tmpbean.setVoucher_title("��ѡ��...");
        list.add(tmpbean);
        while (keyIter.hasNext()) {
            VoucherListBean bean = new VoucherListBean();
            String id = (String) keyIter.next();
            // bean.setSpecID(id);
            bean = gson.fromJson(jsonObject.optString(id),
                    new TypeToken<VoucherListBean>() {
                    }.getType());
            list.add(bean);
            // System.err.println("�̵�����" + list.get(i).getStore_name());
            i++;
        }
    }

    public String getVoucherStr() {
        String tmpVoucherStr = "";
        int tmpj = 0;
        for (int i = 0; i < VoucherStr.length; i++) {
            if (Voucherflag[i]) {
                if (tmpj > 0) {
                    tmpVoucherStr = tmpVoucherStr + ",";
                }
                tmpVoucherStr = tmpVoucherStr + VoucherStr[i];
                tmpj++;
            }
        }
        return tmpVoucherStr;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub

    }

}
