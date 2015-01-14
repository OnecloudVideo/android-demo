package com.pispower.catalog;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.pispower.R;
import com.pispower.network.VideoClient;

public class CreateCatalogTask extends AsyncTask<String, Void, CatalogInfo> {
	private static final String TAG = "CreateFileTask";
	// 场景对象
	private Context context;
	// listView对象
	private ListView listView;
	// 资源对象
	private Resources resources;

	/**
	 * 有参构造方法
	 * 
	 * @param context
	 * @param listView
	 */
	public CreateCatalogTask(Context context, ListView listView) {
		this.context = context;
		this.listView = listView;
		this.resources = context.getResources();
	}

	@Override
	protected CatalogInfo doInBackground(String... paramArrayOfString) {
	    //获得新建分类的名字
		String catalogName = paramArrayOfString[0];
		//创建用于HTTP通信的客服端
		VideoClient videoClient = new VideoClient();
		//创建分类信息实例对象
		CatalogInfo catalogInfo = new CatalogInfo();
		try {
			//创建分类通过VideoClient
			JSONObject catalogJSONObject = videoClient.createCatalog(catalogName);
		    //创建失败
			if (catalogJSONObject == null) {
				return null;
			}
			catalogInfo.setId(catalogJSONObject.getString("id"));
			catalogInfo.setName(catalogJSONObject.getString("name"));

			//通过VideoClient来获取指定id分类的信息
			JSONObject specialCatalogJSONObject = videoClient
					.getCatalog(catalogJSONObject.getString("id"));
			if (specialCatalogJSONObject == null) {
				catalogInfo.setHoldVideoNums(this.resources
						.getString(R.string.zero));
			} else {
				catalogInfo.setHoldVideoNums(specialCatalogJSONObject
						.getString("videoNumber"));
			}
			return catalogInfo;
		} catch (Exception localException) {
			Log.i(TAG, localException.getMessage());
		}
		return null;
	}

	@Override
	protected void onPostExecute(CatalogInfo catalogInfo) {
		//失败
		if (catalogInfo == null) {
			Toast.makeText(this.context,
					this.resources.getString(R.string.createCatalogFail),
					Toast.LENGTH_LONG).show();
			return;
		}
		//成功，更新相应的ListView
		CatalogListViewAdapter localVideoPackageListAdapter = (CatalogListViewAdapter) this.listView
				.getAdapter();
		localVideoPackageListAdapter.addData(catalogInfo, 0);
		localVideoPackageListAdapter.notifyDataSetChanged();
		
		Toast.makeText(this.context,
				this.resources.getString(R.string.createCatalogSuccess),
				Toast.LENGTH_LONG).show();

	}
}
