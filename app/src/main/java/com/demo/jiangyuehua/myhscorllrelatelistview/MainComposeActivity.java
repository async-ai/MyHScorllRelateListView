package com.demo.jiangyuehua.myhscorllrelatelistview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.demo.jiangyuehua.myhscorllrelatelistview.entity.meeting.MeetingUserInfo;
import com.demo.jiangyuehua.myhscorllrelatelistview.view.AsyncMeetingItemTextView;
import com.demo.jiangyuehua.myhscorllrelatelistview.view.AsyncMeetingView;

import java.util.ArrayList;
import java.util.List;

public class MainComposeActivity extends AppCompatActivity {

	private List<String> ls_userNames=null;
	private List<MeetingUserInfo> ls_meetingUserInfos=null;

	private AsyncMeetingView meetingView=null;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_compose);

		context=MainComposeActivity.this;

		initLeftData();
		initMeetintUserInfo();

		meetingView= (AsyncMeetingView) findViewById(R.id.meetingView);
		meetingView.setRightContentAdapter(new RightContentAdapter(context,ls_meetingUserInfos));
		meetingView.setLeftNameAdapter(new LeftNameAdapter(context,ls_userNames));


		final Button meetingType= meetingView.getBtn_meetingType();
		meetingType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				meetingView.setTimeBarParams(barMinuteTransCurrWidth(100), 0x6000ff00);
				meetingView.setTimeBarTranslationX(getBarTranslationX("15:00"));
			}
		});




	}

	/**得到指定时间点的相应位置 100分钟 单为为60dp*/
	public int getBarTranslationX(String currentTime){
		int location=0;
// 以默认宽度为一个单位
		int unit= (int) getResources().getDimension(R.dimen.activity_meeting_times_titelWidth);
		Log.v("分钟60转换后","==="+unit);

		String[] times=meetingView.getTimes();
		int length=times.length;
		int index=0;
		for (int i=0; i<length;i++){
			if (currentTime.equals(times[i])){
				index=i;
			}
		}

		location=(index*unit);
		return location;
	}


/**模拟下午15：00点开始于16：40结束 一个bar宽度计算，一个bar位置计算*/
		public int barMinuteTransCurrWidth(int minute){
			int barCurrWidth=0;
				DisplayMetrics dm=getResources().getDisplayMetrics();
				float density=dm.density;
				barCurrWidth= (int) (minute*density);
			return barCurrWidth;
		}




	/**左侧名称*/
	class LeftNameAdapter extends BaseAdapter {

		private List<String> listNames = null;
		private Context context;

		public LeftNameAdapter(Context context, List<String> listNames) {
			this.listNames = listNames;
			this.context = context;
		}

		@Override
		public int getCount() {
			return listNames.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Viewholder viewholder=null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.meeting_layout_item_name,null);

				viewholder=new Viewholder();
				viewholder.tv_meetingName= (TextView) convertView.findViewById(R.id.tv_meetingName);
				convertView.setTag(viewholder);
			}else {
				viewholder= (Viewholder) convertView.getTag();
			}
			viewholder.tv_meetingName.setText(listNames.get(position).toString());

			return convertView;
		}

		class Viewholder{
			TextView tv_meetingName;
		}
	}



	class RightContentAdapter extends BaseAdapter {
		private List<MeetingUserInfo> listUserInfos = null;
		private Context context;

		public RightContentAdapter(Context context, List<MeetingUserInfo> listUserInfos) {
			this.listUserInfos = listUserInfos;
			this.context = context;
		}

		@Override
		public int getCount() {
			return listUserInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Viewholder viewholder=null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.meeting_layout_item_userinfo,null);

				viewholder=new Viewholder();


				viewholder.tv_01= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_01);
				viewholder.tv_02= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_02);
				viewholder.tv_03= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_03);
				viewholder.tv_04= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_04);
				viewholder.tv_05= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_05);
				viewholder.tv_06= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_06);
				viewholder.tv_07= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_07);
				viewholder.tv_08= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_08);
				viewholder.tv_09= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_09);
				viewholder.tv_10= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_10);
				viewholder.tv_11= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_11);
				viewholder.tv_12= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_12);
				viewholder.tv_13= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_13);
				viewholder.tv_14= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_14);
				viewholder.tv_15= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_15);
				viewholder.tv_16= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_16);
				viewholder.tv_17= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_17);
				viewholder.tv_18= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_18);
				viewholder.tv_19= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_19);
				viewholder.tv_20= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_20);
				viewholder.tv_21= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_21);
				viewholder.tv_22= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_22);
				viewholder.tv_23= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_23);
				viewholder.tv_24= (AsyncMeetingItemTextView) convertView.findViewById(R.id.tv_24);
				convertView.setTag(viewholder);
			}else {
				viewholder= (Viewholder) convertView.getTag();
			}

			MeetingUserInfo meetingUserInfo=listUserInfos.get(position);

			viewholder.tv_01.setText(meetingUserInfo.getTxt1());
			viewholder.tv_02.setText(meetingUserInfo.getTxt2());
			viewholder.tv_03.setText(meetingUserInfo.getTxt3());
			viewholder.tv_04.setText(meetingUserInfo.getTxt4());
			viewholder.tv_05.setText(meetingUserInfo.getTxt5());
			viewholder.tv_06.setText(meetingUserInfo.getTxt6());
			viewholder.tv_07.setText(meetingUserInfo.getTxt7());
			viewholder.tv_08.setText(meetingUserInfo.getTxt8());
			viewholder.tv_09.setText(meetingUserInfo.getTxt9());
			viewholder.tv_10.setText(meetingUserInfo.getTxt10());
			viewholder.tv_11.setText(meetingUserInfo.getTxt11());
			viewholder.tv_12.setText(meetingUserInfo.getTxt12());
			viewholder.tv_13.setText(meetingUserInfo.getTxt13());
			viewholder.tv_14.setText(meetingUserInfo.getTxt14());
			viewholder.tv_15.setText(meetingUserInfo.getTxt15());
			viewholder.tv_16.setText(meetingUserInfo.getTxt16());
			viewholder.tv_17.setText(meetingUserInfo.getTxt17());
			viewholder.tv_18.setText(meetingUserInfo.getTxt18());
			viewholder.tv_19.setText(meetingUserInfo.getTxt19());
			viewholder.tv_20.setText(meetingUserInfo.getTxt20());
			viewholder.tv_21.setText(meetingUserInfo.getTxt21());
			viewholder.tv_22.setText(meetingUserInfo.getTxt22());
			viewholder.tv_23.setText(meetingUserInfo.getTxt23());
			viewholder.tv_24.setText(meetingUserInfo.getTxt24());

			return convertView;
		}

		class Viewholder{

			AsyncMeetingItemTextView tv_01,tv_02,tv_03,tv_04,tv_05,tv_06,tv_07,tv_08,tv_09,tv_10,
					tv_11,tv_12,tv_13,tv_14,tv_15,tv_16,tv_17,tv_18,tv_19,tv_20,
					tv_21,tv_22,tv_23,tv_24;

		}
	}


	private void initLeftData() {
		ls_userNames=new ArrayList<String>();
		for (int i=0;i<24;i++)
		ls_userNames.add("aaaa"+i);
	}

	private  void initMeetintUserInfo(){
		ls_meetingUserInfos=new ArrayList<MeetingUserInfo>();
		for (int i=0;i<24;i++)
		ls_meetingUserInfos.add(new MeetingUserInfo("11","12","13","14","15","16","17","18","19","20",
				"21","22","23","24","25","26","27","28","29","30","31","32","33","34"));
	}


}
