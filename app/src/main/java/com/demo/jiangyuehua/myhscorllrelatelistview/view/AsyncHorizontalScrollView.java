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
	}

	public void setmView(View mViwe) {
		this.mView = mViwe;
	}

}
