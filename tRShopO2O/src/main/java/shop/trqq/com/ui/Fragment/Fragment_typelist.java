package shop.trqq.com.ui.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shop.trqq.com.R;
import shop.trqq.com.adapter.ListViewGoodsTypeAdapter;
import shop.trqq.com.bean.GoodClassBean;
import shop.trqq.com.util.HttpUtil;
import shop.trqq.com.util.YkLog;

/**
 * 第二级分类LIST容器
 *
 * @author Weiss
 */
public class Fragment_typelist extends ListFragment {
    private String TAG = Fragment_typelist.class.getName();
    private ListView list;
    private ListViewGoodsTypeAdapter typeAdapter;
    ;
    private ArrayList<GoodClassBean> typeList;
    private Gson gson;
    private Context mContext;
    private String gc_id;


//    public Fragment_typelist(String gc_id) {
//        this.gc_id = gc_id;
//    }

    /**
     * 空的构造函数
     */
//    public Fragment_typelist() {
//        gc_id = new String();
//    }

    /**
     * @描述 在onCreateView中加载布局
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_typelist, container,
                false);
        gc_id = getArguments().getString("gc_id");
        list = (ListView) view.findViewById(android.R.id.list);
        gson = new Gson();
        typeAdapter = new ListViewGoodsTypeAdapter(mContext);
        loadOnlineTYPEListData(gc_id);
        return view;
    }

    private void loadOnlineTYPEListData(String gc_id) {

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
                    setListAdapter(typeAdapter);
                    //第三级分类适配器
                    typeAdapter.setData(typeList);
                    //typeAdapter.notifyDataSetChanged();
                    // carInfoListIndex += cartInfoList.size();
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                try {
                    // if (typeList.size() == 0)
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> view = (HashMap<String, Object>) l
                    .getItemAtPosition(position);
            Toast.makeText(mContext, TAG + l.getItemIdAtPosition(position),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            YkLog.e(TAG, "");
        }

    }

    private List<? extends Map<String, ?>> getData(String[] strs) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < strs.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", strs[i]);
            list.add(map);

        }

        return list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

}
