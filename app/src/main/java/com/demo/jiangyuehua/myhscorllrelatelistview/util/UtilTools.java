package com.demo.jiangyuehua.myhscorllrelatelistview.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class UtilTools {

	static int totalWidth=0;

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int len = listAdapter.getCount();

		for (int i = 0; i < len; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			totalWidth=listItem.getMeasuredWidth();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.width=totalWidth;//还是算出宽度，解决多出白边的问题
		listView.setLayoutParams(params);
	}

}
