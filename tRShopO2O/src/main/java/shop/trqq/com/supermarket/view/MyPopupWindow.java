package shop.trqq.com.supermarket.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
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

	/***
	 * 
	 * 底部ppupwindow
	 * 
	 * @param view
	 *            ,要填充的布局view'
	 * @param viewId
	 *            ，放位置的控件(最外层布局的id)
	 * @param direction
	 *            ,弹框要显示的位置 1：底部 2：中间
	 */
	@SuppressWarnings("deprecation")
	public void showAsPopuWindow(final Activity activity, final View view,
			int direction, final int viewId) {
		// 实例化popupWindow
		mPopupWindow = new PopupWindow();
		mPopupWindow.setContentView(view);
		mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		mPopupWindow.setHeight(LayoutParams.MATCH_PARENT);
		// setBackgroundAlpha(activity, 0.6f);// 设置屏幕透明度，这个方法在大屏手机有时候不起作用（比如华为）
		// 控制键盘是否可以获得焦点
		mPopupWindow.setFocusable(true);
		ColorDrawable cd = new ColorDrawable(0xb0000000);
		mPopupWindow.setBackgroundDrawable(cd);
		mPopupWindow.setOutsideTouchable(true);
		// 在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		// 设置popupWindow弹出窗体的背景
		// mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		if (direction == 1) {
			mPopupWindow.showAtLocation(view.findViewById(viewId),
					Gravity.BOTTOM, 0, 0);
			// 开始动画
			view.startAnimation(AnimationUtils.loadAnimation(activity,
					R.anim.slid_in_bottom));
		} else if (direction == 2) {
			mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		}
		// popupWindow隐藏时回复屏幕原来的正常透明度
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				setBackgroundAlpha(activity, 1.0f);

			}
		});
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				int height = view.findViewById(viewId).getTop();
				int yy = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (yy < height) {
						mPopupWindow.dismiss();
					}
				}
				return true;
			}
		});

	}

	/**
	 * 品目中间的popupwindow
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
