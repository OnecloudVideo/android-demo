package com.pispower.catalog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.pispower.video.VideoActivity;

public class CatalogListItemClickListener
  implements AdapterView.OnItemClickListener
{
  private Context context;

  public CatalogListItemClickListener(Context paramContext)
  {
    this.context = paramContext;
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    Intent localIntent = new Intent(this.context, VideoActivity.class);
    CatalogInfo localVideoPackageInfo = (CatalogInfo)paramAdapterView.getItemAtPosition(paramInt);
    localIntent.putExtra("catalogId", localVideoPackageInfo.getId());
    localIntent.putExtra("folderName", localVideoPackageInfo.getName());
    this.context.startActivity(localIntent);
  }
}