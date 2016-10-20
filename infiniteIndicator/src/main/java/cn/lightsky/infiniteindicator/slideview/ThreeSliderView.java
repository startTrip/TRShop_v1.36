package cn.lightsky.infiniteindicator.slideview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.lightsky.infiniteindicator.R;

/**
 * a simple slider view, which just show an image. If you want to make your own slider view,
 *
 * just extend BaseSliderView, and implement getView() method.
 */
public class ThreeSliderView extends BaseSliderView{
	LinkedHashMap<String,String> url_maps = new LinkedHashMap<String, String>();
	OnClickListener OnClickListener1;
	
    public ThreeSliderView(Context context,LinkedHashMap<String,String> url_maps) {
        super(context);
        this.url_maps=url_maps;
    }


	public LinkedHashMap<String, String> getUrl_maps() {
		return url_maps;
	}

	public void setUrl_maps(LinkedHashMap<String, String> url_maps) {
		this.url_maps = url_maps;
	}

	@Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_3,null);
        List<String> img_list = new ArrayList<String>();
        for(String key : url_maps.keySet()){
        	img_list.add(url_maps.get(key));
        }
        ImageView target1 = (ImageView)v.findViewById(R.id.slider_image1);
        bind3EventAndShow(v, target1,img_list.get(0));
        ImageView target2 = (ImageView)v.findViewById(R.id.slider_image2);
        bind3EventAndShow(v, target2,img_list.get(1));
        ImageView target3 = (ImageView)v.findViewById(R.id.slider_image3);
        bind3EventAndShow(v, target3,img_list.get(2));
        target1.setOnClickListener(OnClickListener1);
        target2.setOnClickListener(OnClickListener1);
        target3.setOnClickListener(OnClickListener1);
        return v;
    }

    //ThreeSliderView.setOnClickListener必须在第一位因为返回的是ThreeSliderView，其它.返回的是BaseSliderView
    public ThreeSliderView setOnClickListener(OnClickListener l){
    	OnClickListener1 = l;
        return this;
    }
}
