package com.demo.jiangyuehua.myhscorllrelatelistview.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
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
import android.widget.TextView;

import com.demo.jiangyuehua.myhscorllrelatelistview.R;
import com.demo.jiangyuehua.myhscorllrelatelistview.util.UtilTools;

import java.math.BigDecimal;

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
	private MoveView moveView_timeBar;

	private BaseAdapter LeftNameAdapter,RightContentAdapter;
	private Context context;

	private View parentView=null;
	private Handler handler=new Handler();

	private String[] times;
	private ScrollView scrollView_parent;
	private AsyncScrollView innerScrollView_content;

	private FrameLayout fl_moveView;

	private TextView tv_time;
	private String finalTime;

	private String lefttimeStart="";

	private String leftTranTimeStart="";

	//timeBar 移动的单位个数
	private double timeBarUnit=0;
	//timeBar 最终的时间
	private int finalTimeHour=0;
	private int finalTimeMin=0;
	private String finalBarTime="";
	private String strTimeBarUnit;

	private int lvScrollLocationX,lvScrollLocationY;
	private int tbLocationLeft,tbLocationRight;


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
		moveView_timeBar = (MoveView) findViewById(R.id.view_timeBar);

		lv_rightcontent = (ListView)findViewById(R.id.lv_rightcontent);
		lv_rightcontent.setFocusable(false);

		shs_rightcontent = (AsyncHorizontalScrollView)findViewById(R.id.shs_rightcontent);
		shs_rightcontent.setFocusable(false);//关键关掉的

		tv_time = (TextView) findViewById(R.id.tv_time);

		shs_rightcontent.setOnScrollChangedListener(new AsyncHorizontalScrollView.OnScrollChangedListener() {
			@Override
			public void onScrollChangedListener(int l, int t) {
//				Log.v("接口传递的ListView位置信息：","l:"+l+"; t:"+t);
				lvScrollLocationX=l;
				lvScrollLocationY=t;

//				左边滑动量换算成为多少个间隔
				double leftUnit=((double) lvScrollLocationX)/itemWidth;
				BigDecimal bdLeftUnit = new BigDecimal(leftUnit).setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
				String leftScorllXUnit= String.valueOf(bdLeftUnit);//double
//				Log.v("左侧单位：============>",leftScorllXUnit);

				leftScorllXUnit=leftScorllXUnit.replace(".",":");
				String[] leftime=leftScorllXUnit.split(":");

//				Log.v("左侧单位分析：============>",leftime[0]+":"+leftime[1]);


				int timeHour=timeStart+Integer.parseInt(leftime[0]);

				BigDecimal timeMin=new BigDecimal(Integer.parseInt(leftime[1])*0.6).setScale(0,BigDecimal.ROUND_UP);
				int intTimeMin=Integer.parseInt(String.valueOf(timeMin));

				String currMin="";

				if (leftime.length>1){
					if (intTimeMin<60){
						if (intTimeMin==0){
							currMin="00";
						}else {
							if (intTimeMin<10){
								currMin="0"+timeMin;
							}else
							currMin=""+timeMin;
						}
					}else {//=60
						currMin="00";
					}
					lefttimeStart=timeHour+":"+currMin;
					Log.w("content ==lefttimeStart==",lefttimeStart);
					tv_time.setText(lefttimeStart);
				}


				/**
				 * 1、两种情况
				 * 	一：当TimeBar没有移动时以起始时间为基准换距离来换算
				 * 	二：当TimeBar有移动时，起启时间+TimeBar移动的距离得到对应的时间
				 *
				 * */

				if (!TextUtils.isEmpty(strTimeBarUnit)){//TimeBar有移动

					Log.w("此时的开始左边时间====",lefttimeStart);
					leftTranTimeStart=lefttimeStart;

//					换算成时间
					String[] barTimes=strTimeBarUnit.split(":");//如 2.7

					int barTimeHour= Integer.parseInt(barTimes[0]);//小时
					BigDecimal bdtimeMin=new BigDecimal(Integer.parseInt(barTimes[1])*0.6).setScale(0,BigDecimal.ROUND_UP);
					int barTimeMin= Integer.parseInt(String.valueOf(bdtimeMin));//分钟

					int currMoveBarHour=timeHour+barTimeHour;
					int currMoveBarMin= Integer.parseInt(currMin)+barTimeMin;
//					小时自增的问题


					if (currMoveBarMin<60){
						if (currMoveBarMin==0){
							leftTranTimeStart=currMoveBarHour+":00";
						}else {
							if (currMoveBarMin<10){
								leftTranTimeStart=currMoveBarHour+":0"+currMoveBarMin;
							}else
								leftTranTimeStart=currMoveBarHour+":"+currMoveBarMin;
						}
					}else{
						if(currMoveBarMin==60){
							leftTranTimeStart=(currMoveBarHour+1)+":00";
						}else if(currMoveBarMin>60){
//							分钟自增小时后，自增的个位数处理
							if ((currMoveBarMin-60)<10){
								leftTranTimeStart=(currMoveBarHour+1)+":0"+(currMoveBarMin-60);
							}else{
								leftTranTimeStart=(currMoveBarHour+1)+":"+(currMoveBarMin-60);
							}
						}
					}

					Log.w("TimeBar有移动时的自增",currMoveBarHour+":"+currMoveBarMin);
					Log.w("TimeBar有移动时的自增 结束	lefttimeStart-----",lefttimeStart+"/tran:"+leftTranTimeStart);


					tv_time.setText(leftTranTimeStart);

				}

//				else{
//					tv_time.setText(leftTranTimeStart);
//
//				}

			}
		});



		moveView_timeBar.setMoveViewScrollListener(new MoveView.MoveViewScrollListener() {
			@Override
			public void moveViewScrollListener(int viewRectRight, int viewRectLeft) {
//				Log.w("接口传递的TimeBar位置信息：","viewRectRight:"+viewRectRight+"; viewRectLeft:"+viewRectLeft);
				tbLocationLeft=viewRectLeft;
				tbLocationRight=viewRectRight;

				timeBarUnit=((double)(tbLocationLeft-itemWidth))/getResources().getDimension(R.dimen.activity_meeting_times_titelWidth);//得到多少个时间单位，换算成时间
				BigDecimal bdTimeBarUnit = new BigDecimal(timeBarUnit).setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入
				strTimeBarUnit=String.valueOf(bdTimeBarUnit);
				strTimeBarUnit=strTimeBarUnit.replace(".",":");

				/**2，
				 * 也分为二种情况：
				 * 	一：scrollview 是否有移动距离的判断 有的话
				 * 	左边距离换算成时间加上自身移动换算成的时间
				 *
				 * 	二：scrollView有移动，TimeBar二次移动的问题 （这时是因为多加了TimeBar移动的距离）
				 * 	通过
				 *
				 * */
				Log.w("左侧起始时间：",lefttimeStart+"--"+finalBarTime);

				if (!TextUtils.isEmpty(lefttimeStart) /***&& TextUtils.isEmpty(finalBarTime)*/){//内容有移动
//					外界对应的时间
					String[] leftTimes=lefttimeStart.split(":");

//					换算成时间
					String[] barTimes=strTimeBarUnit.split(":");

					int barTimeHour= Integer.parseInt(barTimes[0]);//小时
					BigDecimal bdtimeMin=new BigDecimal(Integer.parseInt(barTimes[1])*0.6).setScale(0,BigDecimal.ROUND_UP);
					int barTimeMin= Integer.parseInt(String.valueOf(bdtimeMin));//分钟

					 finalTimeHour=Integer.parseInt(leftTimes[0])+barTimeHour;
					 finalTimeMin=Integer.parseInt(leftTimes[1])+barTimeMin;//自增效果

//					Log.w("finalTimeMin",finalTimeMin+"");


					if (finalTimeMin<60){
						if (finalTimeMin==0){
							finalBarTime=finalTimeHour+":00";
						}else {
							if (finalTimeMin<10){
								finalBarTime=finalTimeHour+":0"+finalTimeMin;
							}else
								finalBarTime=finalTimeHour+":"+finalTimeMin;
						}
					}else{
						if(finalTimeMin==60){
							finalBarTime=(finalTimeHour+1)+":00";
						}else if(finalTimeMin>60){
//							分钟自增小时后，自增的个位数处理
							if ((finalTimeMin-60)<10){
								finalBarTime=(finalTimeHour+1)+":0"+(finalTimeMin-60);
							}else{
								finalBarTime=(finalTimeHour+1)+":"+(finalTimeMin-60);
							}
						}
					}

					tv_time.setText(finalBarTime);

				}else{//没有移动时，以7点开始

				}


				/**这时初的位置以Timbar为标识 要及时更新TimeBar的相关信息
				 * 这时TimeBar也有移动
				 * */

				if (!TextUtils.isEmpty(lefttimeStart) &&!TextUtils.isEmpty(finalBarTime)){
//					这时是以TimeBar为起始参考点




				}



			}
		});


//		moveView_timeBar.horizontalScrollView=shs_rightcontent;
		moveView_timeBar.setHorizontalScrollView(shs_rightcontent);


		lv_left_name = (ListView)findViewById(R.id.lv_left_name);
		lv_left_name.setFocusable(false);
		shs_titel = (AsyncHorizontalScrollView)findViewById(R.id.shs_titel);


//		嵌套问题
		scrollView_parent= (ScrollView) findViewById(R.id.scrollView_parent);
		innerScrollView_content=(AsyncScrollView) findViewById(R.id.innerScrollView_content);

//		时间移动条父控件
//		moveView_timeBar.parentScrollView=innerScrollView_content;
		moveView_timeBar.setParentScrollView(innerScrollView_content);


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
		moveView_timeBar.setLayoutParams(new FrameLayout.LayoutParams(widht, LayoutParams.MATCH_PARENT));
		moveView_timeBar.setBackgroundColor(color);
	}

	public void setTimeBarTranslationX(final int location){

		moveView_timeBar.setTranslationX(location);
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



	public View getView_timeBar() {
		return moveView_timeBar;
	}

	public int getTimeBarLocationX(){
		if (this.moveView_timeBar!=null){
			return (int) this.moveView_timeBar.getTranslationX();
		}
		return 0;
	}
	public int getTimeBarLocationY(){
		if (this.moveView_timeBar!=null)
			return (int) this.moveView_timeBar.getTranslationY();
		return 0;
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
