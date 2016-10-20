package shop.trqq.com.im.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import shop.trqq.com.R;
import shop.trqq.com.UserManager;
import shop.trqq.com.adapter.CommonAdapter;
import shop.trqq.com.adapter.ViewHolder;
import shop.trqq.com.im.bean.getmsgBean;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.widget.EmoticonsTextView;

public class IMMsgAdapter extends CommonAdapter<getmsgBean> {
    String t_avatar, t_id;

    public IMMsgAdapter(Context context, String t_id) {
        super(context, new ArrayList<getmsgBean>(), R.layout.im_item_msg);
        this.t_id = t_id;
        //?????????????????????????????????
        t_avatar = "http://shop.trqq.com/data/upload/shop/avatar/avatar_" + t_id + ".jpg";
    }

/*	public void setT_avatar(String t_avatar) {
        this.t_avatar = t_avatar;
	}
	*/

    /**
     * ?????????????????????
     * @param holder
     * @param Bean
     */
    @Override
    public void convert(ViewHolder holder, final getmsgBean Bean) {
        LinearLayout leftLayout = (LinearLayout) holder
                .getView(R.id.leftim_layout);
        LinearLayout rightLayout = (LinearLayout) holder
                .getView(R.id.rightim_layout);
        TextView left_tv_time = (TextView) holder.getView(R.id.tv_time);
        EmoticonsTextView left_tv_message = (EmoticonsTextView) holder
                .getView(R.id.tv_message);
        ImageView left_iv_avatar = (ImageView) holder.getView(R.id.iv_avatar);
        EmoticonsTextView right_tv_message = (EmoticonsTextView) holder
                .getView(R.id.tv_message_right);
        TextView right_tv_time = (TextView) holder.getView(R.id.tv_time_right);
        ImageView right_iv_avatar = (ImageView) holder
                .getView(R.id.iv_avatar_right);

        if (UserManager.getUserInfo().getNickname().equals(Bean.getF_name())) {
            right_tv_time.setText(Bean.getAdd_time());
            right_tv_message.setText(Bean.getT_msg());
            ImageLoader.getInstance().displayImage(
                    UserManager.getavator(mContext), right_iv_avatar,
                    ImageLoadUtils.getUserOptions(),
                    ImageLoadUtils.getAnimateFirstDisplayListener());
            rightLayout.setVisibility(View.VISIBLE);
            leftLayout.setVisibility(View.GONE);
        } else {
            left_tv_time.setText(Bean.getAdd_time());
            left_tv_message.setText(Bean.getT_msg());
            // System.err.println(Bean.getUser().getAvatar());
            try {
                ImageLoader.getInstance().displayImage(t_avatar,
                        left_iv_avatar, ImageLoadUtils.getUserOptions(),
                        ImageLoadUtils.getAnimateFirstDisplayListener());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            rightLayout.setVisibility(View.GONE);
            leftLayout.setVisibility(View.VISIBLE);
        }
        // rightMsg.setText(Bean.getT_msg());
        // }
        // System.err.println(Bean.getContent()+"|"+Bean.getType());
    }

}
