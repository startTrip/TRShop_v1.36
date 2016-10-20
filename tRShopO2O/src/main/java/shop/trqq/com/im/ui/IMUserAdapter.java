package shop.trqq.com.im.ui;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import shop.trqq.com.R;
import shop.trqq.com.adapter.CommonAdapter;
import shop.trqq.com.adapter.ViewHolder;
import shop.trqq.com.im.bean.UserBean;
import shop.trqq.com.ui.Base.UIHelper;
import shop.trqq.com.util.ImageLoadUtils;
import shop.trqq.com.util.YkLog;
import shop.trqq.com.widget.BadgeView;

public class IMUserAdapter extends CommonAdapter<UserBean> {

    private BadgeView[] mBadgeViews;

    public IMUserAdapter(Context context) {
        super(context, new ArrayList<UserBean>(), R.layout.im_item_user);
    }

    @Override
    public void setData(List<UserBean> data) {
        // TODO Auto-generated method stub
        super.setData(data);
        if (data != null)
            mBadgeViews = new BadgeView[getCount()];
    }

    public void updataUnNum(int location, int num) {
        // TODO Auto-generated method stu
        if (num > 0) {
            YkLog.t("updata", "" + num);
            mBadgeViews[location].setText("" + num);
            mBadgeViews[location].show();
        } else {
            mBadgeViews[location].hide();
        }
    }

    @Override
    public void convert(ViewHolder holder, final UserBean Bean) {
        // TextView left_tv_time = (TextView) holder.getView(R.id.tv_time);
        RelativeLayout im_userLayout = (RelativeLayout) holder
                .getView(R.id.im_userLayout);
        TextView im_username = (TextView) holder.getView(R.id.im_username);
        ImageView im_avatar = (ImageView) holder.getView(R.id.im_avatar);
        ImageLoader.getInstance().displayImage(Bean.getAvatar(), im_avatar,
                ImageLoadUtils.getUserOptions(),
                ImageLoadUtils.getAnimateFirstDisplayListener());
        im_username.setText(Bean.getU_name());
        /*List<UserBean> mUserBeanList = DataSupport.where("u_id = ?",
				"" + Bean.getU_id()).find(UserBean.class);*/
        // final BadgeView unReadNum=new BadgeView(mContext,im_userLayout);
        final int mPosition = holder.getmPosition();
        if (getCount() > 0) {
            if (mBadgeViews[mPosition] == null) {
                YkLog.t("初始化mBadgeViews", "" + mPosition);
                mBadgeViews[mPosition] = new BadgeView(mContext, im_userLayout);
            }
            updataUnNum(mPosition, Bean.getUnreadnum());
        }
		/*
		 * if(mUserBeanList.size()>0){ //表里没有该用户生成表 UserBean mUserBean=new
		 * UserBean(); mUserBean.setU_id(Bean.getU_id());
		 * mUserBean.setUnreadnum(1); mUserBean.save(); }else{ //更新
		 * if(mUserBeanList.get(0).getUnreadnum()>0){
		 * YkLog.t("Unreadnum",""+mUserBeanList.get(0).getUnreadnum());
		 * unReadNum.setText(""+mUserBeanList.get(0).getUnreadnum());
		 * unReadNum.show(); } }
		 */
        im_userLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mBadgeViews[mPosition].hide();
                UIHelper.showIMChat(mContext, "" + Bean.getU_id(),
                        Bean.getU_name());
            }
        });

        // }
        // System.err.println(Bean.getContent()+"|"+Bean.getType());
    }

}
