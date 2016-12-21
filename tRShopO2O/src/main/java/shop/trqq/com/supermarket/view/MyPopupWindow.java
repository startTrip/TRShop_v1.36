package shop.trqq.com.supermarket.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import shop.trqq.com.R;


public class MyPopupWindow {

	private static MyPopupWindow instance;

	public PopupWindow mPopupWindow;

	private MyPopupWindow() {
	}
	public static MyPopupWindow getInstance() {
		if (instance == null) {
			instance = new MyPopupWindow();
		}
		return instance;
	}

	/**
	 *
	 * @param activity
	 * @param popupView     弹出的视图
	 * @param dismissView    点击消失的视图
	 * @param animView			动画视图
     * @param dissmiss			点击消失动画是否消失
     */
	public void showNormalPopupWindow(Activity activity, View popupView, View dismissView, View animView, final boolean dissmiss){

		// 触摸时获取焦点
		popupView.setFocusableInTouchMode(true);
		mPopupWindow = new PopupWindow(popupView,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		//指定透明背景，back键相关
		mPopupWindow.setBackgroundDrawable(new ColorDrawable());
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);

		mPopupWindow.setAnimationStyle(R.style.PopupAnimaFade);

		if (dismissView != null) {
			dismissView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(dissmiss){
						mPopupWindow.dismiss();
					}
				}
			});
			if (animView != null) {
				animView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
			}
		}

		Animation showAnimation = getDefaultScaleAnimation();
		mPopupWindow.showAtLocation(activity.findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0,0);
		if (showAnimation != null && animView != null) {
			animView.clearAnimation();
			animView.startAnimation(showAnimation);
		}
	}

	/**
	 * 生成自定义ScaleAnimation
	 */
	protected Animation getDefaultScaleAnimation() {
		Animation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f
		);
		scaleAnimation.setDuration(300);
		scaleAnimation.setInterpolator(new AccelerateInterpolator());
		scaleAnimation.setFillEnabled(true);
		scaleAnimation.setFillAfter(true);
		return scaleAnimation;
	}

	/**
	 * 生成TranslateAnimation
	 *
	 * @param durationMillis 动画显示时间
	 * @param start          初始位置
	 */
	protected Animation getTranslateAnimation(int start, int end, int durationMillis) {
		Animation translateAnimation = new TranslateAnimation(0, 0, start, end);
		translateAnimation.setDuration(durationMillis);
		translateAnimation.setFillEnabled(true);
		translateAnimation.setFillAfter(true);
		return translateAnimation;
	}


	/**
	 * 屏幕中间的popupwindow
	 * 
	 * @param activity
	 * @param view
	 */
	public void showPopupWindowFronCenter(final Activity activity,
			final View view) {
		try {
			mPopupWindow = new PopupWindow(view);
			mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
			mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置mPopupWindow是否可点击
			mPopupWindow.setFocusable(true);

			mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			// setBackgroundAlpha(activity, 0.6f);// 设置屏幕透明度
			mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setOutsideTouchable(true) ;
			// 设置popupwindow弹出时的背景色--半透明
			ColorDrawable cd = new ColorDrawable(0xb0000000);
			mPopupWindow.setBackgroundDrawable(cd);
			mPopupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					setBackgroundAlpha(activity, 1.0f);

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 *            屏幕透明度0.0-1.0 1表示完全不透明
	 */
	private void setBackgroundAlpha(Activity activity, float bgAlpha) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = bgAlpha;
		activity.getWindow().setAttributes(lp);
	}

	public void cancel() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();

		}
	}

}
