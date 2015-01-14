package com.pispower.video;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.pispower.R;
import com.pispower.video.VideoInfo;
import com.pispower.video.play.PlayActivity;

 
public class VideoListItemClickListener implements OnItemClickListener {

	// 场景对象
	private Context context;
	// 资源对象
	private Resources resources;

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

		VideoInfo videoPlayList = (VideoInfo) parent
				.getItemAtPosition(position);

		if (checkIsCanPlay(videoPlayList)) {
			showSurePlayDialog(parent, videoPlayList);
		} else {
			showStatusErrorDialoag();
		}

	}

	private void showSurePlayDialog(final AdapterView<?> parent,
			final VideoInfo videoPlayList) {
		AlertDialog.Builder bulider = new AlertDialog.Builder(context);
		bulider.setMessage(resources.getString(R.string.playVideo)).setTitle(
				R.string.app_name);

		bulider.setPositiveButton(resources.getString(R.string.playYes),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(context, PlayActivity.class);
						intent.putExtra("playUri", videoPlayList.getUrl());
						context.startActivity(intent);
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
		if (!"FINISH".equals(videoInfo.getStatus())
				|| videoInfo.getUrl() == null) {
			return false;
		}
		return true;
	}

}
