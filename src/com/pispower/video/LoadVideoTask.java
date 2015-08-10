package com.pispower.video;

import java.util.List;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.TextView;

import com.pispower.R;
import com.pispower.video.sdk.VideoSDK;
import com.pispower.video.sdk.video.VideoInfo;

public class LoadVideoTask extends AsyncTask<String, Void, List<VideoInfo>> {

	private static final String TAG = "LoadVideoTask";

	private ProgressDialog progressDialog;

	private TextView videoEmpTextView;

	private VideoListAdapter videoListAdapter;

	private Resources resources;

	public LoadVideoTask(ProgressDialog progressDialog,
			TextView videoEmpTextView, VideoListAdapter videoListAdapter,
			Resources resources) {
		super();
		this.progressDialog = progressDialog;
		this.videoEmpTextView = videoEmpTextView;
		this.videoListAdapter = videoListAdapter;
		this.resources = resources;
	}

	@Override
	protected void onPostExecute(List<VideoInfo> result) {
		super.onPostExecute(result);
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		if (!isCancelled() && result != null) {
			videoEmpTextView.setText(R.string.videoListEmpty);
			videoListAdapter.setVideoInfoList(result);
			videoListAdapter.notifyDataSetChanged();
		} else {
			videoEmpTextView.setText(R.string.videoListLoadError);
		}
	}

	@Override
	protected List<VideoInfo> doInBackground(String... params) {
		String catalogId = params[0];

		return  new VideoSDK().getVideoService().list(catalogId);
	}

}
