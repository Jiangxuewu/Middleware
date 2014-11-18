package com.j.widget;

import com.j.utils.CommonUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class TipHLayout extends ViewGroup {

	private static final String TAG = "TipHLayout";

	private int mCurScreen = 0;

	private float mLastMotionY;
	
	// 用于判断甩动手势
		private VelocityTracker mVelocityTracker;

		private static final int SNAP_VELOCITY = 600;

		// 滑动控制
		private Scroller mScroller;
	
	public TipHLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		if (changed) {
			int childTop = 0;
			final int childCount = getChildCount();

			for (int i = 0; i < childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childHeight = childView.getMeasuredHeight();
					childView.layout(0, childTop, childView.getMeasuredWidth(),
							childTop + childHeight);
					childTop += childHeight;
				}
			}
//		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int height = MeasureSpec.getSize(heightMeasureSpec);

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		scrollTo(0, mCurScreen * height);

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			}

			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}

			mLastMotionY = y;
			break;

		case MotionEvent.ACTION_MOVE:
			int deltaY = (int) (mLastMotionY - y);
			if (IsCanMove(deltaY)) {
//				if (mOnViewChangeListener != null && deltaY > 0
//						&& mOnViewChangeListener.ifStartLogin(mCurScreen + 1)) {
//					return true;
//				}
				if (mVelocityTracker != null) {
					mVelocityTracker.addMovement(event);
				}

				mLastMotionY = y;

				scrollBy(0, deltaY);
			}
			break;

		case MotionEvent.ACTION_UP:
			int velocityY = 0;
			if (mVelocityTracker != null) {
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				velocityY = (int) mVelocityTracker.getYVelocity();
			}

			if (velocityY > SNAP_VELOCITY && mCurScreen > 0) {
				// Fling enough to move left
				Log.e(TAG, "snap up");
				snapToScreen(mCurScreen - 1);
			} else if (velocityY < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1) {
				// Fling enough to move right
				Log.e(TAG, "snap down");
				snapToScreen(mCurScreen + 1);
			} else if (mCurScreen == getChildCount() - 1) {
				snapToScreen(mCurScreen);
			} else {
				snapToDestination();
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}

			break;
		}

		return true;
	}
	
	private boolean IsCanMove(int deltaY) {
		if (getScrollY() <= 0 && deltaY < -CommonUtils.dp2px(getContext(), 30)) {
			return false;
		}

		if (getScrollY() > getChildCount() * (getHeight() - 1) && deltaY >= 0) {
			return false;
		}

		return true;
	}
	
	public void snapToDestination() {
		Log.e(TAG, "snapToDestination is enter");
		final int screenHeight = getHeight();

		final int destScreen = (getScrollY() + screenHeight / 2) / screenHeight;
		snapToScreen(destScreen);
	}
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}
	
	public void snapToScreen(int whichScreen) {
		Log.e(TAG, "whichScreen is " + whichScreen);
		if (getChildCount() == whichScreen
				&& getScrollY() >= (getChildCount() - 1) * getHeight()) {
//			if (mOnViewChangeListener != null) {
//				mOnViewChangeListener.OnViewChange(whichScreen);
//			}
			Log.e(TAG, "if enter");
		} else {
			Log.e(TAG, "ifelse enter");
			whichScreen = Math.max(0,
					Math.min(whichScreen, getChildCount() - 1));
			if (getScrollY() != (whichScreen * getHeight())) {
				final int delta = whichScreen * getHeight() - getScrollY();
				Log.e(TAG, " else if is enter... delta:" + delta);

				mScroller.startScroll(0, getScrollY(), 0, delta, Math.abs(delta) * 2);

				mCurScreen = whichScreen;
				invalidate(); // Redraw the layout

//				if (mOnViewChangeListener != null) {
//					mOnViewChangeListener.OnViewChange(mCurScreen);
//				}
			}
		}
	}

}
