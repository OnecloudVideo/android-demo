package com.pispower.catalog;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;

import com.pispower.AppContext;
import com.pispower.R;
import com.pispower.video.sdk.VideoSDK;
import com.pispower.video.sdk.catalog.request.CatalogCreateRequest;
import com.pispower.video.sdk.catalog.CatalogInfo;
import com.pispower.video.sdk.core.VideoSDKException;

public class CreateCatalogTask extends AsyncTask<String, Void, CatalogInfo> {
	private static final String TAG = "CreateFileTask";
	// 场景对象
	private Context context;
	//CatalogListViewAdapter 对象
	private CatalogListViewAdapter catalogListViewAdapter;
	// 资源对象
	private Resources resources;

	/**
	 * 有参构造方法
	 * 
	 * @param context
	 * @param catalogListViewAdapter
	 */
	public CreateCatalogTask(Context context, CatalogListViewAdapter catalogListViewAdapter) {
		this.context = context;
		this.catalogListViewAdapter = catalogListViewAdapter;
		this.resources = context.getResources();
	}

	@Override
	protected CatalogInfo doInBackground(String... paramArrayOfString) {
		// 获得新建分类的名字
		String catalogName = paramArrayOfString[0];

		try {
			return AppContext.getSDK().getCatalogService().create(new CatalogCreateRequest(catalogName));
		} catch (VideoSDKException e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(CatalogInfo catalogInfo) {
		// 失败
		if (catalogInfo == null) {
			Toast.makeText(this.context,
					this.resources.getString(R.string.createCatalogFail),
					Toast.LENGTH_LONG).show();
			return;
		}
		
		// 成功，更新相应的ListView
		catalogListViewAdapter.addData(catalogInfo, 0);
		catalogListViewAdapter.notifyDataSetChanged();

		Toast.makeText(this.context,
				this.resources.getString(R.string.createCatalogSuccess),
				Toast.LENGTH_LONG).show();

	}
}
