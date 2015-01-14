package com.pispower.catalog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.pispower.video.VideoActivity;

public class CatalogListViewItemClickListener implements
		AdapterView.OnItemClickListener {
	// 场景对象
	private Context context;

	/**
	 * 有参构造方法
	 * 
	 * @param context
	 */
	public CatalogListViewItemClickListener(Context context) {
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this.context, VideoActivity.class);
		CatalogInfo localVideoPackageInfo = (CatalogInfo) parent
				.getItemAtPosition(position);
		intent.putExtra("catalogId", localVideoPackageInfo.getId());
		intent.putExtra("folderName", localVideoPackageInfo.getName());
		this.context.startActivity(intent);
	}
}
