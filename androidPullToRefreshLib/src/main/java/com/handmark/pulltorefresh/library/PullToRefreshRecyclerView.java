package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import shop.trqq.com.widget.decoration.DividerItemDecoration;

/**
 * ��������������ʾ���Ѻ�
 * Created by Administrator on 2015/11/24 0024.
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {

	public PullToRefreshRecyclerView(Context context) {
		super(context);
	}

	public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshRecyclerView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshRecyclerView(Context context, Mode mode,
			AnimationStyle animStyle) {
		super(context, mode, animStyle);
	}

	@Override
	public Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected RecyclerView createRefreshableView(Context context,
			AttributeSet attrs) {
		RecyclerView recyclerView = new RecyclerView(context, attrs);
		recyclerView.setId(R.id.recyclerview);
		// �ָ���
		recyclerView.addItemDecoration(new DividerItemDecoration(context,
				DividerItemDecoration.VERTICAL_LIST));
		return recyclerView;
	}

	public void setOnScrollListener(RecyclerView.OnScrollListener mOnRcvScrollListener) {
		mRefreshableView.setOnScrollListener(mOnRcvScrollListener);
	}
	
	public void setAdapter(RecyclerView.Adapter adapter) {
		mRefreshableView.setAdapter(adapter);
	}
	public void smoothScrollToPosition(int postion) {
		mRefreshableView.smoothScrollToPosition(postion);
	}
	public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
		mRefreshableView.setLayoutManager(layoutManager);
	}

	public void addItemDecoration(DividerItemDecoration mDividerItemDecoration) {
		mRefreshableView.addItemDecoration(mDividerItemDecoration);
	}

	@Override
	protected boolean isReadyForPullEnd() {

		int lastVisiblePostion = mRefreshableView
				.getChildPosition(mRefreshableView.getChildAt(mRefreshableView
						.getChildCount() - 1));
		if (lastVisiblePostion >= mRefreshableView.getAdapter().getItemCount() - 1) {
			return mRefreshableView.getChildAt(
					mRefreshableView.getChildCount() - 1).getBottom() <= mRefreshableView
					.getBottom();
		}
		return false;
	}

	@Override
	protected boolean isReadyForPullStart() {
		if (mRefreshableView.getChildCount() <= 0)
			return true;
		int firstVisiblePosition = mRefreshableView
				.getChildPosition(mRefreshableView.getChildAt(0));
		if (firstVisiblePosition == 0) {
			return mRefreshableView.getChildAt(0).getTop() == mRefreshableView
					.getPaddingTop();
		} else {
			return false;
		}
	}
}
