package com.pispower.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Collection;
import java.util.List;

import com.pispower.R;

public class CatalogListAdapter extends BaseAdapter
{
  private Context context;
  private List<CatalogInfo> packageList;

  public CatalogListAdapter(List<CatalogInfo> paramList, Context paramContext)
  {
    this.packageList = paramList;
    this.context = paramContext;
  }

  public boolean addAllData(Collection<CatalogInfo> paramCollection)
  {
    return this.packageList.addAll(paramCollection);
  }

  public void addData(CatalogInfo paramVideoPackageInfo, int paramInt)
  {
    this.packageList.add(paramInt, paramVideoPackageInfo);
  }

  public int getCount()
  {
    return this.packageList.size();
  }

  public CatalogInfo getItem(int paramInt)
  {
    return (CatalogInfo)this.packageList.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView=null;
    if (paramView == null)
    {
      localView = ((LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.catalog_list_item, paramViewGroup, false);
      ViewHolder viewHolder = new ViewHolder();
      viewHolder.packageImage = ((ImageView)localView.findViewById(R.id.catalogImage));
      viewHolder.name = ((TextView)localView.findViewById(R.id.catalogName));
      viewHolder.lastModifiedTime = ((TextView)localView.findViewById(R.id.catalogModificationTime));
      viewHolder.holdVideoNums = ((TextView)localView.findViewById(R.id.catalogContainsVideoNums));
      localView.setTag(viewHolder);
    }else{
    	localView=paramView;
    }
      ViewHolder viewHolder = (ViewHolder)localView.getTag();
      viewHolder.name.setText(((CatalogInfo)this.packageList.get(paramInt)).getName());
      viewHolder.lastModifiedTime.setText(((CatalogInfo)this.packageList.get(paramInt)).getLastModifiedTime());
      viewHolder.holdVideoNums.setText(((CatalogInfo)this.packageList.get(paramInt)).getHoldVideoNums());
      return localView;
   
     
  }

  public void setDataList(List<CatalogInfo> paramList)
  {
    this.packageList = paramList;
  }

  static class ViewHolder
  {
    protected TextView holdVideoNums;
    protected TextView lastModifiedTime;
    protected TextView name;
    protected ImageView packageImage;
  }
}
