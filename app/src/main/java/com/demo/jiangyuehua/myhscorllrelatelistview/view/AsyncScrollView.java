package com.demo.jiangyuehua.myhscorllrelatelistview.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

/**
 * Created by jiangyuehua on 16/7/26.
 */
public class AsyncScrollView extends ScrollView {

	/**
	 * 外层ScrollView
	 */
	private ScrollView parentScrollView;
	/**
	 * 外层的头部时间控件，用于判断时间轴是否正屏幕内可见
	 * */
	private Button btn_meetingType;

	private int mTop = 10;
	private int lastScrollDelta = 0;
	private int currentY;

	/**判断时间轴是否在屏幕内显示*/
	private boolean isTimeItemShow=true;

	/**判断子控件是否滑动到底部*/
	private boolean isConvertDownGetParent=false;


	public AsyncScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void resume() {
		overScrollBy(0, -lastScrollDelta, 0, getScrollY(), 0, getScrollRange(), 0, 0, true);
		lastScrollDelta = 0;
	}
	/**
	 * 将targetView滚到最顶端
	 */
	public void scrollToTop(View targetView) {
		int oldScrollY = getScrollY();
		int top = targetView.getTop() - mTop;
		int delatY = top - oldScrollY;
		lastScrollDelta = delatY;
		overScrollBy(0, delatY, 0, getScrollY(), 0, getScrollRange(), 0, 0, true);
	}
	private int getScrollRange() {
		int scrollRange = 0;
		if (getChildCount() > 0) {
			View child = getChildAt(0);
			scrollRange = Math.max(0, child.getHeight() - (getHeight()));
		}
		return scrollRange;
	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (parentScrollView == null) {
			return super.onInterceptTouchEvent(ev);
		} else {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				// 将父scrollview的滚动事件拦截
				currentY = (int)ev.getY();
				setParentScrollAble(false);

				return super.onInterceptTouchEvent(ev);
			} else if (ev.getAction() == MotionEvent.ACTION_UP) {
				// 把滚动事件恢复给父Scrollview
				setParentScrollAble(true);
				Log.v("onInterceptTouchEventUp","把滚动事件恢复给父Scrollview");

			} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {

				DisplayMetrics dm=getResources().getDisplayMetrics();

				int[] ints=new int[]{dm.widthPixels,dm.heightPixels};

//					Log.v("onInterceptTouchEventMoveBtn",btn_meetingType.getLocationOnScreen(ints));
				isTimeItemShow=btn_meetingType.getLocalVisibleRect(new Rect(0,0,dm.widthPixels,dm.heightPixels));

				Log.v("onInterceptTouchEventMove","把滚动事件恢复给父Scrollview:"+isTimeItemShow);


			}
		}
		return super.onInterceptTouchEvent(ev);

	}



	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		View child = getChildAt(0);
		if (parentScrollView != null) {
			if (ev.getAction() == MotionEvent.ACTION_MOVE) {
				int height = child.getMeasuredHeight();
				height = height - getMeasuredHeight();

				 Log.d("innr_onTouchEvent","height=" + height);
				int scrollY = getScrollY();
				 Log.d("innr_onTouchEvent","_scrollY=" + scrollY);
				int y = (int)ev.getY();

				// 手指向下滑动
				if (currentY < y) {
					if (scrollY <= 0) {
						// 如果向下滑动到头，就把滚动交给父Scrollview
						setParentScrollAble(true);
						return false;
					} else {

						/**两种情况向下滑动是，要由子控件先切到父控件显示出时间后再内部滑动，
						 * 一是已到达过底部，再滑动子控件，此时先转到父控件先显示出时间轴再滑动子控件
						 * 二是子控件滑动到中间，这时切换到父控件滑动，此时时间轴不可见了，这是再滑动子控件时 判断时间轴是否可见。。。*/
						if ((isConvertDownGetParent==true |isTimeItemShow==false) && height-scrollY>40){
							setParentScrollAble(true);
							isConvertDownGetParent=false;
						}else {
							setParentScrollAble(false);
						}
					}
				} else if (currentY > y) {
					if (scrollY >= height) {
//						子控件已滑动到底部
						isConvertDownGetParent=true;
						// 如果向上滑动到头，就把滚动交给父Scrollview
						setParentScrollAble(true);
						return false;
					} else {
						setParentScrollAble(false);
					}
				}
				currentY = y;
			}
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * 是否把滚动事件交给父scrollview
	 *
	 * @param flag
	 */
	private void setParentScrollAble(boolean flag) {
		parentScrollView.requestDisallowInterceptTouchEvent(!flag);
	}

	public void setParentScrollView(ScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
	}

	public void setBtn_meetingType(Button btn_meetingType) {
		this.btn_meetingType = btn_meetingType;
	}
}
