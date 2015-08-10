package com.pispower.catalog;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;

import com.pispower.R;
import com.pispower.video.sdk.VideoSDK;
import com.pispower.video.sdk.catalog.CatalogInfo;

public class LoadCatalogTask extends AsyncTask<Void, Void, List<CatalogInfo>> {
	private static final String TAG = "LoadPackageTask";
 
	// TextView对象
	private TextView listViewEmptyHintTextView;
	// ListView 对象
	// private ListView listView;
	// ProgressDialog 对象
	private ProgressDialog progressDialog;
	// adapter
	private CatalogListViewAdapter catalogListViewAdapter;

	/**
	 * 有参构造方法
	 * 
	 * @param progressDialog
	 * @param listViewEmptyHintTextView
	 * @param catalogListViewAdapter
	 *
	 */
	public LoadCatalogTask(ProgressDialog progressDialog,
			TextView listViewEmptyHintTextView,
			CatalogListViewAdapter catalogListViewAdapter) {
		this.progressDialog = progressDialog;
		// this.listView = listView;
		this.listViewEmptyHintTextView = listViewEmptyHintTextView;
		this.catalogListViewAdapter = catalogListViewAdapter;
	}

	@Override
	protected List<CatalogInfo> doInBackground(Void... paramArrayOfVoid) {
		return new VideoSDK().getCatalogService().list();
	}

	@Override
	protected void onPostExecute(List<CatalogInfo> paramList) {
		super.onPostExecute(paramList);
		if (this.progressDialog.isShowing()) {
			this.progressDialog.dismiss();
		}
		// 为ListView设置Adapter
		if ((!isCancelled()) && (paramList != null)) {
			this.listViewEmptyHintTextView.setText(R.string.noAnyVideos);
			this.catalogListViewAdapter.setDataList(paramList);
		} else {
			this.listViewEmptyHintTextView.setText(R.string.loadError);


			this.catalogListViewAdapter
					.setDataList(new ArrayList<CatalogInfo>());
		}
		this.catalogListViewAdapter.notifyDataSetChanged();
		return;
	}
}