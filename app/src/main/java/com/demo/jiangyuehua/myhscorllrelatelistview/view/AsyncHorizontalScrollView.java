package com.demo.jiangyuehua.myhscorllrelatelistview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by jiangyuehua on 16/7/24.
 */
public class AsyncHorizontalScrollView extends HorizontalScrollView {

	private View mView;

	private int scrollChangedX;

	private OnScrollChangedListener onScrollChangedListener;

	public AsyncHorizontalScrollView(Context context) {
		super(context);
	}

	public AsyncHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AsyncHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mView!=null)
		mView.scrollTo(l,t);
//		Log.v("滑动值：",l+"===="+t);
		scrollChangedX=l;

		if (onScrollChangedListener!=null){
			onScrollChangedListener.onScrollChangedListener(l,t);
		}
	}

	public void setmView(View mViwe) {
		this.mView = mViwe;
	}



//	内部类接口

	public  interface OnScrollChangedListener{

		void onScrollChangedListener(int l,int t);

	}

	public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener){
		this.onScrollChangedListener=onScrollChangedListener;
	}


	public int getScrollChangedX() {
		return scrollChangedX;
	}
}
