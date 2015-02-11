package com.pispower.video;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.Toast;

import com.pispower.R;
import com.pispower.util.NetworkInspection;
import com.pispower.video.play.PlayActivity;

public class VideoListItemClickListener implements OnItemClickListener {

	// 场景对象
	private Context context;
	// 资源对象
	private Resources resources;

	private String seleteClarity;

	private Map<String, String> clarityUrlMap;
	private String[] claritys;

	/**
	 * 有参构造方法
	 * 
	 * @param context
	 */
	public VideoListItemClickListener(Context context) {
		this.context = context;
		this.resources = context.getResources();
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, View view,
			final int position, long id) {

		
		 HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) parent.getAdapter();  
		 VideoListAdapter adapter = (VideoListAdapter)listAdapter.getWrappedAdapter(); 
		   VideoInfo videoPlayList = (VideoInfo) 
					adapter.getItem(position);

		if (checkIsCanPlay(videoPlayList)) {
			showSurePlayDialog(parent, videoPlayList);
		} else {
			showStatusErrorDialoag();
		}

	}

	private void showSurePlayDialog(final AdapterView<?> parent,
			final VideoInfo videoPlayList) {
		clarityUrlMap = videoPlayList.getClarityUrlMap();
		claritys=new String[clarityUrlMap.size()];
// 		claritys = (String[]) clarityUrlMap.keySet().toArray();
 		 Set<String>claritySet=   clarityUrlMap.keySet();
 	     Iterator<String> iterator= claritySet.iterator();
 	     int cur=0;
 		 while(iterator.hasNext()){
 			claritys[cur]=iterator.next();
 			cur++;
 		 }
		 
		AlertDialog.Builder bulider = new AlertDialog.Builder(context);
		bulider.setTitle(R.string.selectClarityVideo);
		bulider.setSingleChoiceItems(claritys, 0, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				seleteClarity = claritys[which];
			}

		});
		bulider.setPositiveButton(resources.getString(R.string.playYes),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (NetworkInspection.isExistingAnyNetwork(context)) {
							Intent intent = new Intent(context,
									PlayActivity.class);
							if (seleteClarity == null) {
								seleteClarity = claritys[0];
							}
							intent.putExtra(resources.getString(R.string.playUri),
									clarityUrlMap.get(seleteClarity));
							context.startActivity(intent);
						} else {
							Toast.makeText(context, R.string.noAnyNetworks,
									Toast.LENGTH_LONG).show();
						}

					}
				});

		bulider.setNegativeButton(resources.getString(R.string.playNo),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		bulider.show();
	}

	private void showStatusErrorDialoag() {
		AlertDialog.Builder bulider = new AlertDialog.Builder(context);
		bulider.setMessage(resources.getString(R.string.statusError)).setTitle(
				R.string.app_name);
		bulider.setPositiveButton(resources.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		bulider.show();
	}

	private boolean checkIsCanPlay(VideoInfo videoInfo) {
		if (resources.getString(R.string.AUDIT_SUCCESS).equals(
				videoInfo.getStatus())
				|| resources.getString(R.string.videoUploadSuccess).equals(
						videoInfo.getStatus())) {
			return true;
		}
		return false;
	}

}
