package com.demo.jiangyuehua.myhscorllrelatelistview.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.demo.jiangyuehua.myhscorllrelatelistview.R;

/**
 * Created by jiangyuehua on 16/8/4.
 */
public class MoveView extends View {

	private static final String TAG = "MoveView";

	// 分别记录上次滑动的坐标
	private int mLastX = 0;
	private int mLastY = 0;

	private int moveLastX = 0;
	private int moveLastY = 0;

	private int mScaledTouchSlop;

//	父控件(外围可滚动的view)
	private AsyncHorizontalScrollView horizontalScrollView=null;



// 父控件 解决的是多嵌套的问题
	private AsyncScrollView parentScrollView;

	private MoveViewScrollListener moveViewScrollListener;



	public MoveView(Context context) {
		this(context,null);
	}

	public MoveView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public MoveView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mScaledTouchSlop = ViewConfiguration.get(getContext())
				.getScaledTouchSlop();
		Log.d(TAG, "sts:" + mScaledTouchSlop);
	}




	int	mX=0;
	int	mY=0;
//	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();


		switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				mX= (int) event.getX();
				mY= (int) event.getY();

				Log.v("down---------",mX+"/"+mY);
				setParentScrollAble(false);

				break;
			case MotionEvent.ACTION_MOVE:

				setParentScrollAble(true);

				int deltaX = x - mLastX;
				int deltaY = y - mLastY;
//				Log.d(TAG, "move, deltaX:" + deltaX + " deltaY:" + deltaY);
				int translationX = (int)getTranslationX() + deltaX;
				int translationY = (int)getTranslationY() + deltaY;


//				是否超出屏幕判断
				Rect currentViewRect = new Rect();
				boolean partVisible =this.getGlobalVisibleRect(currentViewRect);
//				右边界
				boolean totalWidthVisible = (currentViewRect.right) >= parentScrollView.getMeasuredWidth();
//				左边界
				boolean totalWidthVisibleLeft = (currentViewRect.left) >= getResources().getDimension(R.dimen.activity_meeting_times_titelWidth);//名称的宽度


//				Log.e("是否超出屏幕判断：",currentViewRect.right+":"+currentViewRect.left+"/"
//						+parentScrollView.getMeasuredWidth()+"---"+totalWidthVisibleLeft+"/"+"/"+totalWidthVisible);

				if (moveViewScrollListener!=null){
					moveViewScrollListener.moveViewScrollListener(currentViewRect.right,currentViewRect.left);
				}

				/**尾部时超出*/
				if (totalWidthVisible==false){//未超出时
	//					自移动
					if (translationX>=0){//负方向不可移动
						setTranslationX(translationX);
					}else{
//						如果左侧还有可滑动
						int moveX= (int) (event.getX()-mX);
						if (horizontalScrollView.getScrollChangedX()>0){
//							外部联动了
							horizontalScrollView.smoothScrollBy(moveX/6,0);
//							setTranslationX(translationX);
						}

					}

					Log.d(TAG, "move, translationX:" + translationX + " translationY:" + translationY);
				}else{//超出时
					/**起点时超出判断*/
						int moveX= (int) (event.getX()-mX);
						Log.v("jiangyuehua--moveX--"+mScaledTouchSlop,event.getX()+":"+mX+"/内部滑动量："+(moveX/6));
							//外部关联移动
						if (Math.abs(moveX)>mScaledTouchSlop && moveX<this.getWidth()){//小于自身
							if (moveX<0){
								setTranslationX(translationX);
							}else{
								horizontalScrollView.smoothScrollBy(moveX/6,0);
							}
						}

				}



//				0:90:240:450
//				Log.v(TAG, "当前MoveBar的位置 :" +getLeft()+":"+ getTop()+":"+getRight()+":"+getBottom());
//				Log.v(TAG, "getTranslationX :" + getTranslationX()+":getTranslationY："+getTranslationY());


				break;
			case MotionEvent.ACTION_UP:


				break;
			default:
				break;
		}

		mLastX = x;
		mLastY = y;

		moveLastX = mX;
		moveLastY = mY;
		return true;
	}

	private void setParentScrollAble(boolean flag) {
		parentScrollView.requestDisallowInterceptTouchEvent(!flag);
	}


	public interface MoveViewScrollListener{
		void moveViewScrollListener(int viewRectRight,int viewRectLeft);
	}

	public void setMoveViewScrollListener(MoveViewScrollListener moveViewScrollListener){
		this.moveViewScrollListener=moveViewScrollListener;
	}


	public void setHorizontalScrollView(AsyncHorizontalScrollView horizontalScrollView) {
		this.horizontalScrollView = horizontalScrollView;
	}

	public void setParentScrollView(AsyncScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
	}
}
