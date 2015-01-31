package com.pispower.catalog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.Toast;

import com.pispower.R;
import com.pispower.util.NetworkInspection;
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
		if (NetworkInspection.isExistingAnyNetwork(context)) {
			Intent intent = new Intent(this.context, VideoActivity.class);
		   HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) parent.getAdapter();  
		   CatalogListViewAdapter adapter = (CatalogListViewAdapter)listAdapter.getWrappedAdapter(); 
			CatalogInfo localVideoPackageInfo = (CatalogInfo) 
					adapter.getItem(position);
			intent.putExtra("catalogId", localVideoPackageInfo.getId());
			intent.putExtra("folderName", localVideoPackageInfo.getName());
			this.context.startActivity(intent);
		} else {
           Toast.makeText(context, R.string.noAnyNetworks, Toast.LENGTH_LONG).show();
		}

	}
}
