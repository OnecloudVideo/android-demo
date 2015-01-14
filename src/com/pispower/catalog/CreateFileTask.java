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

public class CreateFileTask extends AsyncTask<String, Void, CatalogInfo>
{
  private static final String TAG = "CreateFileTask";
  private Context context;
  private ListView listView;
  private Resources resources;

  public CreateFileTask(Context paramContext, ListView paramListView)
  {
    this.context = paramContext;
    this.listView = paramListView;
    this.resources = paramContext.getResources();
  }

  @Override
  protected CatalogInfo doInBackground(String... paramArrayOfString)
  {
    String str = paramArrayOfString[0];
    VideoClient localVideoClient = new VideoClient();
    CatalogInfo localVideoPackageInfo = new CatalogInfo();
    try
    {
      JSONObject localJSONObject1 = localVideoClient.createCatalog(str);
      if (localJSONObject1 == null)
        return null;
      localVideoPackageInfo.setId(localJSONObject1.getString("id"));
      localVideoPackageInfo.setName(localJSONObject1.getString("name"));
      JSONObject localJSONObject2 = localVideoClient.getCatalog(localJSONObject1.getString("id"));
      if (localJSONObject2 == null)
        localVideoPackageInfo.setHoldVideoNums(this.resources.getString(2131034144));
      localVideoPackageInfo.setHoldVideoNums(localJSONObject2.getString("videoNumber"));
      return localVideoPackageInfo;
    }
    catch (Exception localException)
    {
      Log.i(TAG, localException.getMessage());
    }
    return null;
  }

  @Override
  protected void onPostExecute(CatalogInfo paramVideoPackageInfo)
  {
    if (paramVideoPackageInfo == null)
    {
      Toast.makeText(this.context, this.resources.getString(R.string.createCatalogFail), Toast.LENGTH_LONG).show();
      return;
    }   
      CatalogListAdapter localVideoPackageListAdapter = (CatalogListAdapter)this.listView.getAdapter();
      localVideoPackageListAdapter.addData(paramVideoPackageInfo, 0);
      localVideoPackageListAdapter.notifyDataSetChanged();
      Toast.makeText(this.context, this.resources.getString(R.string.createCatalogSuccess), Toast.LENGTH_LONG).show();
   
  }
}
