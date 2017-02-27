package shop.trqq.com.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.bean.GoodClassBean;
import shop.trqq.com.bean.Type;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.MyGridView;

/**
 * 二级分类列表的适配器
 *
 * @author 星河
 * @created 2015-02-10
 */
public class ListViewGoodsTypeAdapter extends CommonAdapter<GoodClassBean> {

    private static final String TAG = "ListViewNewsAdapter";
    private ArrayList<Type> list;
    private Type type;
    private ArrayList<GoodClassBean> typeList;
    private String json;
    private Gson gson;

    /**
     * 第三级分类列表适配器构造方法
     *
     * @param context
     */
    public ListViewGoodsTypeAdapter(Context context) {
        super(context, new ArrayList<GoodClassBean>(),
                R.layout.fragment_pro_type);
        YkLog.i(TAG, "第三级分类列表适配器构造方法");
        gson = new Gson();

    }

    // String[] 转 ArrayList<String>
    ArrayList<String> trans(String[] sa) {
        ArrayList<String> als = new ArrayList<String>(0);
        for (int i = 0; i < sa.length; i++) {
            als.add(sa[i]);
        }
        return als;
    }

    @Override
    public void convert(ViewHolder holder, GoodClassBean goodsBean) {

        ProgressBar progressBar = (ProgressBar) holder
                .getView(R.id.progressBar);
        // ImageView hint_img=(ImageView) holder.getView(R.id.hint_img);
        MyGridView listView = (MyGridView) holder.getView(R.id.listView);
        // 二级分类标题
        ((TextView) holder.getView(R.id.toptype)).setText(goodsBean
                .getGc_name());
        loadOnlineTYPEListData(progressBar, listView, goodsBean.getGc_id());

    }

    private void loadOnlineTYPEListData(final ProgressBar progressBar,
                                        final MyGridView gridView, String gc_id) {
        RequestParams requestParams = new RequestParams();
        String uri = HttpUtil.URL_GOODSCLASS + "&gc_id=" + gc_id;
        HttpUtil.get(uri, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                try {
                    String jsonString = new String(responseBody);
                    JSONObject jsonObjects = new JSONObject(jsonString)
                            .getJSONObject("datas");
                    String type_list = jsonObjects.getString("class_list");
                    typeList = gson.fromJson(type_list,
                            new TypeToken<List<GoodClassBean>>() {
                            }.getType());
					/*
					 * AppConfig.setSharedPreferences(getActivity(), "typeList",
					 * type_list);
					 */
                    list = new ArrayList<Type>();
                    for (int i = 0; i < typeList.size(); i++) {
                        type = new Type(i, typeList.get(i).getGc_name(), "");
                        list.add(type);
                    }
                    progressBar.setVisibility(View.GONE);
                    // 第三级分类
                    Pro_type_adapter adapter = new Pro_type_adapter(mContext,
                            list, typeList);
                    gridView.setAdapter(adapter);
					/*
					 * gridView.setOnItemClickListener(new OnItemClickListener()
					 * {
					 * 
					 * @Override public void onItemClick(AdapterView<?> arg0,
					 * View arg1, int arg2,long arg3) {
					 * 
					 * 
					 * } });
					 */
                    // carInfoListIndex += cartInfoList.size();
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                try {
                    // if (typeList.size() == 0)
                    // mLoadunloginLinearLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                // 不管成功或者失败，都要将进度条关闭掉
                // mNetProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        // TODO Auto-generated method stub

    }

}
