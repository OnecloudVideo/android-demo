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

public class CatalogListViewAdapter extends BaseAdapter {
	// 场景对象用来获取 LayoutInflater
	private Context context;
	// 分类信息列表
	private List<CatalogInfo> catalogInfos;

	/**
	 * 有参构造方法
	 * 
	 * @param catalogInfos
	 * @param context
	 */
	public CatalogListViewAdapter(List<CatalogInfo> catalogInfos,
			Context context) {
		this.catalogInfos = catalogInfos;
		this.context = context;
	}

	/**
	 * 添加分类信息集合到分类信息列表中，成功返回true，否则返回false
	 * 
	 * @param paramCollection
	 * @return
	 */
	public boolean addAllData(Collection<CatalogInfo> paramCollection) {
		return this.catalogInfos.addAll(paramCollection);
	}

	/**
	 * 添加单个分类信息到分类信息列表的指定位置
	 * 
	 * @param catlogInfo
	 * @param position
	 */
	public void addData(CatalogInfo catlogInfo, int position) {
		this.catalogInfos.add(position, catlogInfo);
	}

	/**
	 * 设置分类信息列表
	 * 
	 * @param catalogInfos
	 */
	public void setDataList(List<CatalogInfo> catalogInfos) {
		this.catalogInfos = catalogInfos;
	}

	@Override
	public int getCount() {
		return this.catalogInfos.size();
	}

	@Override
	public CatalogInfo getItem(int paramInt) {
		return (CatalogInfo) this.catalogInfos.get(paramInt);
	}

	@Override
	public long getItemId(int paramInt) {
		return paramInt;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View localView = null;
		if (convertView == null) {
			// 通过LayoutInflater来获得layout view
			localView = ((LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.catalog_list_item, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.packageImage = ((ImageView) localView
					.findViewById(R.id.catalogImage));
			viewHolder.name = ((TextView) localView
					.findViewById(R.id.catalogName));
			viewHolder.lastModifiedTime = ((TextView) localView
					.findViewById(R.id.catalogModificationTime));
			viewHolder.holdVideoNums = ((TextView) localView
					.findViewById(R.id.catalogContainsVideoNums));
			localView.setTag(viewHolder);
		} else {
			localView = convertView;
		}
		ViewHolder viewHolder = (ViewHolder) localView.getTag();
		viewHolder.name.setText(((CatalogInfo) this.catalogInfos.get(position))
				.getName());
		viewHolder.lastModifiedTime.setText(((CatalogInfo) this.catalogInfos
				.get(position)).getLastModifiedTime());
		viewHolder.holdVideoNums.setText(((CatalogInfo) this.catalogInfos
				.get(position)).getHoldVideoNums());
		return localView;

	}

	/*
	 * 用来保存所需的view对象，防止每次都需生成新的view对象，用于getView方法中
	 */
	static class ViewHolder {
		protected TextView holdVideoNums;
		protected TextView lastModifiedTime;
		protected TextView name;
		protected ImageView packageImage;
	}
}
