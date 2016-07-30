package com.demo.jiangyuehua.myhscorllrelatelistview.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.demo.jiangyuehua.myhscorllrelatelistview.R;
import com.demo.jiangyuehua.myhscorllrelatelistview.util.UtilTools;

/**
 * Created by jiangyuehua on 16/7/25.
 */
public class AsyncMeetingView extends LinearLayout{

	private int timeModel=24;
	private int timeStart=7;

	/**item 宽高 最终于工具类转换*/
	private int itemWidth= (int) getResources().getDimension(R.dimen.activity_meeting_times_titelWidth);
	private int ItemHeight= (int) getResources().getDimension(R.dimen.activity_meeting_times_titelHeight);


	private AsyncHorizontalScrollView shs_titel;
	private AsyncHorizontalScrollView shs_rightcontent;

	private ListView lv_left_name;
	private ListView lv_rightcontent;

	private LinearLayout ll_rightitle;
	private Button btn_meetingType;
	private View view_timeBar;

	private BaseAdapter LeftNameAdapter,RightContentAdapter;
	private Context context;

	private View parentView=null;
	private Handler handler=new Handler();

	private String[] times;
	private ScrollView scrollView_parent;
	private AsyncScrollView innerScrollView_content;


	public AsyncMeetingView(Context context) {
		this(context,null);
	}

	public AsyncMeetingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		parentView=LayoutInflater.from(context).inflate(R.layout.activity_main,this);
		initView();
	}

	private void initView() {
		initTitelTimesItem();

		btn_meetingType= (Button) findViewById(R.id.btn_meetingType);
		view_timeBar = (View) findViewById(R.id.view_timeBar);

		lv_rightcontent = (ListView)findViewById(R.id.lv_rightcontent);
		lv_rightcontent.setFocusable(false);

		shs_rightcontent = (AsyncHorizontalScrollView)findViewById(R.id.shs_rightcontent);
		shs_rightcontent.setFocusable(false);//关键关掉的

		lv_left_name = (ListView)findViewById(R.id.lv_left_name);
		lv_left_name.setFocusable(false);

		shs_titel = (AsyncHorizontalScrollView)findViewById(R.id.shs_titel);

//		嵌套问题
		scrollView_parent= (ScrollView) findViewById(R.id.scrollView_parent);
		innerScrollView_content=(AsyncScrollView) findViewById(R.id.innerScrollView_content);
		innerScrollView_content.setParentScrollView(scrollView_parent);
		innerScrollView_content.setBtn_meetingType(btn_meetingType);

		/**解决不置顶的问题*/
		scrollView_parent.setFocusable(true);
		scrollView_parent.setFocusableInTouchMode(true);
		scrollView_parent.requestFocus();

//		innerScrollView_content.scrollToTop(btn_meetingType);

		//初次时间轴不显示问题
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(2000);
//					scrollView_parent.smoothScrollTo(0,0);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();


		Log.v("--------","各参数宽度：shs_titel:"+shs_titel.getMeasuredWidth()
				+" ；shs_rightcontent："+shs_rightcontent.getWidth()
				+"; lv_rightcontent:"+lv_rightcontent.getWidth());

	}

	public void setTimeBarParams(int widht,int color){
		view_timeBar.setLayoutParams(new FrameLayout.LayoutParams(widht, LayoutParams.MATCH_PARENT));
		view_timeBar.setBackgroundColor(color);
	}

	public void setTimeBarTranslationX(final int location){

		view_timeBar.setTranslationX(location);
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				退一格，相对居中显示
		int scrollLocation=location-(int)(getResources().getDimension(R.dimen.activity_meeting_times_titelWidth));
				shs_rightcontent.smoothScrollTo(scrollLocation,0);
//			}
//		},200);

	}

	/**实例时间段*/
	private void initTitelTimesItem() {

		ll_rightitle= (LinearLayout)findViewById(R.id.ll_rightitle);
		times=transFormTime(timeModel,timeStart);
		for (int i=0;i<times.length;i++){
			AsyncMeetingTextView tv_time=new AsyncMeetingTextView(context);
			tv_time.setLayoutParams(
					new LinearLayout.LayoutParams(itemWidth,ItemHeight));
			tv_time.setText(times[i]);
			tv_time.setGravity(Gravity.CENTER);
			tv_time.setTextSize(getResources().getDimension(R.dimen.activity_meeting_times_textSize));
			ll_rightitle.addView(tv_time);

			Log.v("times====",times[i]);
		}

	}

	/**
	 * 根据列数生成相应的时间数组 以8：00/startPoint 起始点 开头
	 * 以第二列开始，第一列为标注：时间/人员
	 */
	public String[] transFormTime(int column,int startPoint) {//24,7

		String[] times = new String[column];
		for (int i = 0; i < times.length; i++) {
			if (i >= startPoint && i < times.length) {// =7 && <24
				if (i != 24) {
					times[i-startPoint] = i + ":00";
				} else {
					times[i-startPoint] = "00:00";//24:00转成 00：00
				}
			}else if(i>=0 && i<startPoint){//
				times[times.length - (startPoint- i)] = i + ":00";//（index 19-24）
			}
		}
		return times;
	}


	public void setTimeModel(int timeModel) {
		this.timeModel = timeModel;
	}

	public void setTimeStart(int timeStart) {
		this.timeStart = timeStart;
	}

	public int getItemWidth() {
		return itemWidth;
	}

	public void setItemWidth(int itemWidth) {
		this.itemWidth = itemWidth;
	}

	public int getItemHeight() {
		return ItemHeight;
	}

	public void setItemHeight(int itemHeight) {
		ItemHeight = itemHeight;
	}


	public void setView_timeBar(View view_timeBar) {
		this.view_timeBar = view_timeBar;
	}

	public Button getBtn_meetingType() {
		return btn_meetingType;
	}

	public String[] getTimes() {
		return times;
	}

	public void setLeftNameAdapter(BaseAdapter leftNameAdapter) {
		LeftNameAdapter = leftNameAdapter;
		if (LeftNameAdapter!=null)
			lv_left_name.setAdapter(LeftNameAdapter);
		shs_titel.setmView(shs_rightcontent);
		UtilTools.setListViewHeightBasedOnChildren(lv_left_name);
	}


	public void setRightContentAdapter(BaseAdapter rightContentAdapter) {
		RightContentAdapter = rightContentAdapter;
		if (RightContentAdapter!=null)
			lv_rightcontent.setAdapter(RightContentAdapter);

		shs_rightcontent.setmView(shs_titel);
		UtilTools.setListViewHeightBasedOnChildren(lv_rightcontent);
	}

}
