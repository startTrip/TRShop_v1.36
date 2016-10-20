package shop.trqq.com.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.roamer.slidelistview.SlideBaseAdapter;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.bean.CartInfoBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.ToastUtils;
import shop.trqq.com.util.YkLog;

/**
 * 购物车列表的适配器
 *
 * @author 星河
 * @created 2015-02-10
 */
public class ListViewCartAdapter extends SlideBaseAdapter {

    private static final String TAG = "ListViewCartAdapter";
    private ArrayList<CartInfoBean> mData;
    private Handler handler;

    public ListViewCartAdapter(Context context, ArrayList<CartInfoBean> data,
                               Handler handler) {
        super(context);
        mData = data;
        YkLog.i(TAG, "购物车适配器构造方法");
        this.handler = handler;

    }

    @Override
    public int getFrontViewId(int position) {
        return R.layout.list_cart;
    }

    @Override
    public int getLeftBackViewId(int position) {
        return 0;
    }

    @Override
    public int getRightBackViewId(int position) {
        // TODO Auto-generated method stub
        return R.layout.list_cart_backview;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(ArrayList<CartInfoBean> data) {
        if (data != null)
            this.mData = data;
    }

    public ArrayList<CartInfoBean> getmData() {
        return mData;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = createConvertView(position);
            holder = new ViewHolder();
            holder.cart_goodLayout = (LinearLayout) convertView.findViewById(R.id.cart_goodLayout);
            holder.title = (TextView) convertView.findViewById(R.id.cart_title);
            holder.img = (ImageView) convertView.findViewById(R.id.cart_img);
            holder.price = (TextView) convertView.findViewById(R.id.cart_price);
            holder.delete = (Button) convertView
                    .findViewById(R.id.cart_back_del);
            holder.shopname = (TextView) convertView
                    .findViewById(R.id.cartgroup_title);
            // holder.goods_num=(TextView)convertView.findViewById(R.id.pop_num);
            holder.pop_reduce = (TextView) convertView
                    .findViewById(R.id.pop_reduce);
            holder.pop_num = (TextView) convertView.findViewById(R.id.pop_num);
            holder.pop_add = (TextView) convertView.findViewById(R.id.pop_add);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(
                mData.get(position).getGoods_image_url(), holder.img, ImageLoadUtils.getoptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        holder.shopname.setText("店铺名称：" + mData.get(position).getStore_name());
        holder.title.setText(mData.get(position).getGoods_name());
        holder.price
                .setText("单价:" + "￥" + mData.get(position).getGoods_price());
        holder.pop_num.setText(mData.get(position).getGoods_num());

        holder.pop_reduce.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(holder.pop_num.getText().toString());
                if (1 < num) {//&&!isedit
                    num--;
                    String quantity = String.valueOf(num);
                    holder.pop_num.setText(quantity);
                    mData.get(position).setGoods_num(quantity);
                    float price = -Float.parseFloat(mData.get(position)
                            .getGoods_price());
                    //	cart_edit_quantity(mData.get(position).getCart_id(),quantity,holder);
                    mData.get(position).setGoods_num(quantity);
                    sendMsgSum(price);
                }
            }
        });
        holder.pop_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(holder.pop_num.getText().toString());
                //	 if(!isedit){
                num++;
                String quantity = String.valueOf(num);
                holder.pop_num.setText(quantity);
                mData.get(position).setGoods_num(quantity);
                float price = Float.parseFloat(mData.get(position)
                        .getGoods_price());
                mData.get(position).setGoods_num(quantity);
                sendMsgSum(price);
                //cart_edit_quantity(mData.get(position).getCart_id(), quantity,holder);
                /*}else{
					YkLog.i("cart_edit_quantity", "正在编辑");
				}*/
            }
        });
        final String good_id = mData.get(position).getGoods_id();
        holder.cart_goodLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UIHelper.showGoods_Detaill(mContext, good_id);
            }
        });
        if (holder.delete != null) {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float price = -Float.parseFloat(mData.get(position)
                            .getGoods_price())
                            * Float.parseFloat(mData.get(position)
                            .getGoods_num());
                    cartDetele(mData.get(position).getCart_id(), position, price);

                    // Toast.makeText(mContext, "Click delete:" + position,
                    // Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }

    private void cartDetele(String cart_id, final int position, final float price) {
        RequestParams requestParams = new RequestParams();
        String key = UserManager.getUserInfo().getKey();
        requestParams.add("key", key);
        requestParams.add("cart_id", cart_id);
        HttpUtil.post(HttpUtil.URL_CART_DETELE, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    //System.err.println(TAG + "：" + jsonString);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString)
                                .getJSONObject("datas");
                        String errStr = jsonObject.getString("error");
                        if (errStr != null) {
                            ToastUtils.showMessage(mContext, errStr);
                        }
                    } catch (Exception e) {
                        if (new JSONObject(jsonString).getString("datas")
                                .equals("1")) {
                            ToastUtils.showMessage(mContext, "删除成功");
                            mData.remove(position);
                            if (mData.size() == 0) {
                                Message msg = new Message();
                                msg.what = 2;
                                handler.sendMessage(msg);
                            } else {
                                notifyDataSetChanged();
                                sendMsgSum(price);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
            }
        });
    }
/*
	private void cart_edit_quantity(String cart_id, String quantity,final ViewHolder holder) {
		RequestParams requestParams = new RequestParams();
		String key = UserManager.getUserInfo().getKey();
		requestParams.add("key", key);
		requestParams.add("cart_id", cart_id);
		requestParams.add("quantity", quantity);
		HttpUtil.post(HttpUtil.URL_CART_EDIT_QUANTITY, requestParams, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				try {
					String jsonString = new String(responseBody);
					JSONObject jsonObject = new JSONObject(jsonString)
							.getJSONObject("datas");
					//System.err.println(TAG + "cart_edit_quantity：" + jsonString);
					try {
						String errStr = jsonObject.getString("error");
						if (errStr != null) {
							ToastUtils.showMessage(mContext, errStr);
						}
					} catch (Exception e) {
						String total_price=jsonObject.getString("total_price");
						String quantityStr=jsonObject.getString("quantity");
						String goods_price=jsonObject.getString("goods_price");
						holder.pop_num.setText(quantityStr);
						float price = Float.parseFloat(goods_price);
						price=	isadd?+price:-price;
							sendMsgSum(price);
							// ToastUtils.showMessage(ListContext,"成功编辑");
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				ToastUtils.showMessage(mContext, R.string.get_informationData_failure);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			//	isedit=false;
			}
			
		});
		
	}*/

    private void sendMsgSum(float num) {
        Message msg = new Message();
        msg.what = 1;
        msg.getData().putFloat("SumNum", num);
        handler.sendMessage(msg);
    }


    class ViewHolder {
        private LinearLayout cart_goodLayout;
        private TextView shopname;
        private TextView title;
        private ImageView img;
        private TextView price;
        private Button delete;
        // 货品数量
        private TextView pop_reduce;
        private TextView pop_num;
        private TextView pop_add;
    }

}
